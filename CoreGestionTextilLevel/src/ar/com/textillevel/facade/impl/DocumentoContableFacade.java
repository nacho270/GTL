package ar.com.textillevel.facade.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionExceptionSinRollback;
import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.dao.api.local.DocumentoContableDAOLocal;
import ar.com.textillevel.dao.api.local.FacturaDAOLocal;
import ar.com.textillevel.dao.api.local.ParametrosGeneralesDAOLocal;
import ar.com.textillevel.dao.api.local.RemitoSalidaDAOLocal;
import ar.com.textillevel.entidades.config.ConfiguracionNumeracionFactura;
import ar.com.textillevel.entidades.config.NumeracionFactura;
import ar.com.textillevel.entidades.config.ParametrosGenerales;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.factura.DocumentoContableCliente;
import ar.com.textillevel.entidades.enums.EEstadoImpresionDocumento;
import ar.com.textillevel.entidades.enums.EPosicionIVA;
import ar.com.textillevel.entidades.enums.ETipoFactura;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.local.DocumentoContableFacadeLocal;
import ar.com.textillevel.facade.api.remote.DocumentoContableFacadeRemote;
import ar.com.textillevel.modulos.fe.ConfiguracionAFIPHolder;
import ar.com.textillevel.modulos.fe.cliente.responses.DummyResponse;
import ar.com.textillevel.modulos.fe.connector.AFIPConnector;
import ar.com.textillevel.modulos.fe.connector.DatosRespuestaAFIP;
import ar.com.textillevel.modulos.fe.to.EstadoServidorAFIP;

@Stateless
public class DocumentoContableFacade implements DocumentoContableFacadeLocal, DocumentoContableFacadeRemote {

	private static final Logger logger = Logger.getLogger(DocumentoContableFacade.class);
	
	@EJB
	private ParametrosGeneralesDAOLocal paramGeneralesDAO;

	@EJB
	private FacturaDAOLocal facturaDAO;

	@EJB
	private DocumentoContableDAOLocal docContableDAO;

	@EJB
	private RemitoSalidaDAOLocal remitoSalidaDAO;

	public Integer getProximoNroDocumentoContable(EPosicionIVA posIva, ETipoDocumento tipoDoc) {
		ParametrosGenerales parametrosGenerales = paramGeneralesDAO.getParametrosGenerales();
		Integer proximoNumeroDeFactura = getLastNumeroFactura(posIva.getTipoFactura(), tipoDoc);
		if (proximoNumeroDeFactura == null) {
			ConfiguracionNumeracionFactura configuracionFactura = parametrosGenerales.getConfiguracionFacturaByTipoFactura(posIva.getTipoFactura());
			if (configuracionFactura == null) {
				throw new RuntimeException("Falta configurar el número de comienzo de factura en los parámetros generales.");
			}
			NumeracionFactura numeracionActual = configuracionFactura.getNumeracionActual(DateUtil.getHoy());
			if(numeracionActual != null){
				proximoNumeroDeFactura = numeracionActual.getNroDesde();
			}else{
				throw new RuntimeException("No hay una configuracion de números de factura vigente para " + DateUtil.dateToString(DateUtil.getHoy()));
			}
			Integer ultimaFacturaRS = remitoSalidaDAO.getUltimoNumeroFactura(posIva);
			if(ultimaFacturaRS!=null){
				proximoNumeroDeFactura = Math.max(proximoNumeroDeFactura, ultimaFacturaRS);
			}
		} else {
			if(tipoDoc == ETipoDocumento.FACTURA) { //Sólo cuando es FACTURA porque NC/ND siguen su propia numeración
				Integer ultimaFacturaRS = remitoSalidaDAO.getUltimoNumeroFactura(posIva);
				proximoNumeroDeFactura = getMaximo(proximoNumeroDeFactura,ultimaFacturaRS);
			}
		}
		return proximoNumeroDeFactura + 1;
	}

	private Integer getLastNumeroFactura(ETipoFactura tipoFactura, ETipoDocumento tipoDoc){
		return facturaDAO.getLastNumeroFactura(tipoFactura, tipoDoc);
	}

	private Integer getMaximo(Integer... numeros){
		Integer max = 0;
		for(Integer n : numeros){
			if(n > max){
				max = n;
			}
		}
		return max;
	}

	@SuppressWarnings("unchecked")
	public <D extends DocumentoContableCliente> D autorizarDocumentoContableAFIP(D docContable) throws ValidacionExceptionSinRollback, ValidacionException {
		if(ConfiguracionAFIPHolder.getInstance().isHabilitado()) {
			try {
				logger.info("Autorizando " + docContable.getTipoDocumento().getDescripcion() + " Nro: " + docContable.getNroFactura());
				DatosRespuestaAFIP respAFIP = AFIPConnector.getInstance().autorizarDocumento(docContable, paramGeneralesDAO.getParametrosGenerales().getNroSucursal(), docContable.getTipoDocumento().getIdTipoDocAFIP(docContable.getTipoFactura()));
				boolean autorizada = respAFIP.isAutorizada(); 
				if(autorizada) {
					logger.info("Autorizacion existosa de " + docContable.getTipoDocumento().getDescripcion() + " Nro: " + docContable.getNroFactura());
					docContable.setCaeAFIP(respAFIP.getCae());
					docContable.setEstadoImpresion(EEstadoImpresionDocumento.AUTORIZADO_AFIP);
				} else {
					logger.info("No se ha podido autorizar " + docContable.getTipoDocumento().getDescripcion() + " Nro: " + docContable.getNroFactura());
				}
				docContable.setObservacionesAFIP(respAFIP.getObservaciones() == null ? null : respAFIP.getObservaciones().substring(0, Math.min(DocumentoContableCliente.LONG_OBS_AFIP-1, respAFIP.getObservaciones().length())));
				docContable = (D)docContableDAO.save(docContable);
				// hago eager al cliente
				docContable.getCliente().getCelular();
				if(!autorizada) {
					List<String> msg = new ArrayList<String>();
					msg.add(docContable.getObservacionesAFIP() == null ? "" : docContable.getObservacionesAFIP());
					throw new ValidacionExceptionSinRollback(EValidacionException.DOCUMENTO_CONTABLE_NO_SE_PUDO_AUTORIZAR_AFIP.getInfoValidacion(), msg); 
				}
			} catch (RemoteException e) {
				List<String> msg = new ArrayList<String>();
				msg.add(e.getMessage());
				throw new ValidacionException(EValidacionException.DOCUMENTO_CONTABLE_FALLO_CONEXION_AFIP.getInfoValidacion(), msg); 
			}
		}
		return docContable;
	}

	public void checkAutorizacionAFIP(DocumentoContableCliente docContable) throws ValidacionException {
		if(ConfiguracionAFIPHolder.getInstance().isHabilitado() && docContable.getCaeAFIP() != null && (docContable.getEstadoImpresion() == EEstadoImpresionDocumento.AUTORIZADO_AFIP || docContable.getEstadoImpresion() == EEstadoImpresionDocumento.IMPRESO)) {
			throw new ValidacionException(EValidacionException.DOCUMENTO_CONTABLE_YA_AUTORIZADO.getInfoValidacion());
		}
	}

	public void checkImpresionDocumentoContable(DocumentoContableCliente documento) throws ValidacionException {
		if(ConfiguracionAFIPHolder.getInstance().isHabilitado() && documento.getCaeAFIP() == null) {
			throw new ValidacionException(EValidacionException.DOCUMENTO_CONTABLE_NO_SE_PUEDE_IMPRIMIR.getInfoValidacion());			
		}
	}

	public List<DocumentoContableCliente> getDocumentosContablesSinCAE() {
		return docContableDAO.getAllSinCAE();
	}

	public EstadoServidorAFIP getEstadoServidorAFIP(int nroSucursal) throws ValidacionException {
		if(ConfiguracionAFIPHolder.getInstance().isHabilitado()) {
			try {
				logger.info("Consultando estado de servicios AFIP");
				DummyResponse informeEstadoServicio = AFIPConnector.getInstance().informeEstadoServicio();
				logger.info("Realizando prueba de autenticacion");
				boolean okAuth = ConfiguracionAFIPHolder.getInstance().getAuthData() != null;
				logger.info("Consultando ultima FC A autorizada");
				String ultimaFCAuth = "" + AFIPConnector.getInstance().getUltimoComprobante(nroSucursal, ETipoDocumento.FACTURA.getIdTipoDocAFIP(ETipoFactura.A)).getCbteNro();
				logger.info("Consultando ultima NC A autorizada");
				String ultimaNCAuth = "" + AFIPConnector.getInstance().getUltimoComprobante(nroSucursal, ETipoDocumento.NOTA_CREDITO.getIdTipoDocAFIP(null)).getCbteNro();
				logger.info("Consultando ultima ND A autorizada");
				String ultimaNDAuth = "" + AFIPConnector.getInstance().getUltimoComprobante(nroSucursal, ETipoDocumento.NOTA_DEBITO.getIdTipoDocAFIP(null)).getCbteNro();
				return new EstadoServidorAFIP(informeEstadoServicio, okAuth, ultimaFCAuth, ultimaNCAuth, ultimaNDAuth);
			}catch(Exception e) {
				return new EstadoServidorAFIP(new DummyResponse("error", "error", "error"), false, " - ", " - ", " - ");
			}
		}else{
			throw new ValidacionException(EValidacionException.SERVICIO_AFIP_NO_HABILITADO.getInfoValidacion()); 
		}
	}
}
package ar.com.textillevel.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.IgnoreDependency;

import ar.clarin.fwjava.auditoria.evento.enumeradores.EnumTipoEvento;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionExceptionSinRollback;
import ar.com.textillevel.dao.api.local.ChequeDAOLocal;
import ar.com.textillevel.dao.api.local.CorreccionDAOLocal;
import ar.com.textillevel.dao.api.local.CorreccionFacturaProveedorDAOLocal;
import ar.com.textillevel.dao.api.local.PagoOrdenDePagoDAOLocal;
import ar.com.textillevel.dao.api.local.ParametrosGeneralesDAOLocal;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.documentos.factura.CorreccionFactura;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.NotaCredito;
import ar.com.textillevel.entidades.documentos.factura.NotaDebito;
import ar.com.textillevel.entidades.documentos.factura.proveedor.CorreccionFacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaDebitoProveedor;
import ar.com.textillevel.entidades.documentos.to.CorreccionFacturaMobTO;
import ar.com.textillevel.entidades.enums.ETipoCorreccionFactura;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.local.CorreccionFacadeLocal;
import ar.com.textillevel.facade.api.local.CorreccionFacturaProveedorFacadeLocal;
import ar.com.textillevel.facade.api.local.CuentaFacadeLocal;
import ar.com.textillevel.facade.api.local.DocumentoContableFacadeLocal;
import ar.com.textillevel.facade.api.remote.AuditoriaFacadeLocal;
import ar.com.textillevel.facade.api.remote.CorreccionFacadeRemote;

@Stateless
public class CorreccionFacade implements CorreccionFacadeLocal, CorreccionFacadeRemote {

	@EJB
	private CorreccionDAOLocal correccionDao;
	
	@EJB
	private CorreccionFacturaProveedorDAOLocal correccionProveedorDAO;
	
	@EJB
	private CuentaFacadeLocal cuentaFacade;
	
	@EJB
	@IgnoreDependency
	private ChequeDAOLocal chequeDao;
	
	@EJB
	@IgnoreDependency
	private CorreccionFacturaProveedorFacadeLocal correccionProvedorFacade;
	
	@EJB
	@IgnoreDependency
	private PagoOrdenDePagoDAOLocal pagoOrdenDePagoDAO;

	@EJB
	private DocumentoContableFacadeLocal docContableFacade; 

	@EJB
	private AuditoriaFacadeLocal<CorreccionFactura> auditoriaFacade;

	@EJB
	private AuditoriaFacadeLocal<CorreccionFacturaProveedor> auditoriaFacade2;
	
	@EJB
	private ParametrosGeneralesDAOLocal parametrosGeneralesDAO;

	public CorreccionFactura guardarCorreccionYGenerarMovimiento(CorreccionFactura correccion, String usuario) throws ValidacionException, ValidacionExceptionSinRollback {
		correccion = guardarYGenerarMovimientoInterno(correccion);
		auditoriaFacade.auditar(usuario, "Actualización de nota de "+ correccion.getTipo().getDescripcion() + " Nº: " + correccion.getNroFactura(), EnumTipoEvento.ALTA, correccion);
		return docContableFacade.autorizarDocumentoContableAFIP(correccion);
	}

	private CorreccionFactura guardarYGenerarMovimientoInterno(CorreccionFactura correccion) {
		correccion = correccionDao.save(correccion);
		if (correccion.getTipo() == ETipoCorreccionFactura.NOTA_DEBITO) {
			correccion = cuentaFacade.crearMovimientoDebe((NotaDebito) correccion);
		} else {
			cuentaFacade.crearMovimientoHaber((NotaCredito) correccion);
		}
		return correccion;
	}

	public CorreccionFactura actualizarCorreccion(CorreccionFactura correccion) throws CLException {
		return correccionDao.save(correccion);
		//auditoriaFacade.auditar(usuario, "Actualización de nota de "+correccion.getTipo().getDescripcion() +" Nº: " + correccion.getNroCorreccion(), EnumTipoEvento.MODIFICACION, correccion);
	}

	public CorreccionFactura getCorreccionByNumero(Integer idNumero) {
		return correccionDao.getCorreccionByNumero(idNumero);
	}

	public List<NotaCredito> getNotaCreditoPendienteUsarList(Integer idCliente) {
		return correccionDao.getNotaCreditoPendienteUsarList(idCliente);
	}

	public void cambiarVerificacionCorreccion(CorreccionFactura correccion, boolean verificada, String usrName) {
		correccion.setVerificada(verificada);
		if(verificada){
			correccion.setUsuarioVerificador(usrName);
		}
		correccion = correccionDao.save(correccion);
		auditoriaFacade.auditar(usrName, "Cambio de verificación nota de "+correccion.getTipo().getDescripcion() +" Nº: ", EnumTipoEvento.MODIFICACION, correccion);
	}

	public NotaCredito getNotaDeCreditoByFacturaRelacionada(Factura factura) {
		return correccionDao.getNotaDeCreditoByFacturaRelacionada(factura);
	}

	public CorreccionFactura editarCorreccion(CorreccionFactura correccion, String usuario) throws ValidacionException {
		docContableFacade.checkAutorizacionAFIP(correccion);		
		eliminarCorreccionInterno(correccion);
		correccion = guardarYGenerarMovimientoInterno(correccion);
		auditoriaFacade.auditar(usuario, "Edición de nota de "+correccion.getTipo().getDescripcion() +" Nº: " + correccion.getNroFactura(), EnumTipoEvento.MODIFICACION, correccion);
		return correccion;
	}

	public void anularCorreccion(CorreccionFactura correccion, String usrName) throws CLException, ValidacionException {
		docContableFacade.checkAutorizacionAFIP(correccion);
		correccion = getCorreccionByNumero(correccion.getNroFactura());
		if(correccion instanceof NotaDebito){
			NotaDebito notaDebito = (NotaDebito)correccion;
			if(correccionDao.notaDebitoSeUsaEnRecibo(notaDebito)){
				throw new ValidacionException(EValidacionException.NOTA_DEBITO_SE_USA_EN_RECIBO.getInfoValidacion());
			}
			
			cuentaFacade.borrarMovimientoNotaDebitoCliente((NotaDebito)correccion);

			//Rollback del cheque. Paso de rechazado al estado anterior
			if(notaDebito.getChequeRechazado()!=null){
				Cheque chequeAPonerEnCartera = notaDebito.getChequeRechazado();
				chequeAPonerEnCartera.setEstadoCheque(notaDebito.getEstadoAnteriorCheque());
				chequeDao.save(chequeAPonerEnCartera);
			}
		}else{
			NotaCredito notaCredito = (NotaCredito) correccion;
//			if(notaCredito.getFacturasRelacionadas().size()>0){
//				throw new ValidacionException(EValidacionException.NOTA_CREDITO_TIENE_FACTURAS_RELACIONADAS.getInfoValidacion());
//			}
			if(correccionDao.notaCreditoSeUsaEnRecibo(notaCredito)){
				throw new ValidacionException(EValidacionException.NOTA_CREDITO_SE_USA_EN_RECIBO.getInfoValidacion());
			}
			cuentaFacade.borrarMovimientoNotaCreditoCliente(notaCredito);
		}
		correccion.setAnulada(true);
		correccionDao.save(correccion);
		auditoriaFacade.auditar(usrName, "Anulación de nota de "+correccion.getTipo().getDescripcion() +" Nº: ", EnumTipoEvento.MODIFICACION, correccion);
	}
	
	public void eliminarCorreccion(CorreccionFactura correccion, String usrName) throws CLException, ValidacionException {
		docContableFacade.checkAutorizacionAFIP(correccion);
		correccion = getCorreccionByNumero(correccion.getNroFactura());
		
		if(correccion instanceof NotaDebito){
			NotaDebito notaDebito = (NotaDebito)correccion;
			if(notaDebito.getChequeRechazado()!=null){
				Cheque c =notaDebito.getChequeRechazado();
				c.setEstadoCheque(notaDebito.getEstadoAnteriorCheque());
				chequeDao.save(c);
				CorreccionFacturaProveedor ndp = correccionProvedorFacade.obtenerNotaDeDebitoByCheque(c);
				if(ndp!=null){
					//correccionProvedorFacade.borrarCorreccionFacturaProveedor(ndp, usrName);
					if(pagoOrdenDePagoDAO.existsNotaDebitoProvEnPagoOrdenDePago((NotaDebitoProveedor)ndp)) {
						throw new ValidacionException(EValidacionException.NOTA_DEBITO_PROV_EXISTE_EN_ORDEN_DE_PAGO.getInfoValidacion());
					}
					cuentaFacade.borrarMovimientoNotaDebitoProveedor((NotaDebitoProveedor)ndp);
					correccionProveedorDAO.removeById(ndp.getId());
					auditoriaFacade2.auditar(usrName, "borrado de nota de débito de proveedor "+" Nº: " + ndp.getNroCorreccion(), EnumTipoEvento.BAJA, ndp);
				}
			}
		}
		
		correccion = eliminarCorreccionInterno(correccion);
		auditoriaFacade.auditar(usrName, "Eliminación de nota de "+correccion.getTipo().getDescripcion() +" Nº: " + correccion.getNroFactura(), EnumTipoEvento.BAJA, correccion);
	}

	private CorreccionFactura eliminarCorreccionInterno(CorreccionFactura correccion) throws ValidacionException {
		docContableFacade.checkAutorizacionAFIP(correccion);
		correccion = getCorreccionByNumero(correccion.getNroFactura()); 
		if(correccion instanceof NotaCredito){
			if(((NotaCredito)correccion).getFacturasRelacionadas().size()>0){
				throw new ValidacionException(EValidacionException.NOTA_CREDITO_TIENE_FACTURAS_RELACIONADAS.getInfoValidacion());
			}
			if(correccionDao.notaCreditoSeUsaEnRecibo((NotaCredito)correccion)){
				throw new ValidacionException(EValidacionException.NOTA_CREDITO_SE_USA_EN_RECIBO.getInfoValidacion());
			}
			cuentaFacade.borrarMovimientoNotaCreditoCliente((NotaCredito)correccion);
		}else{
			if(correccionDao.notaDebitoSeUsaEnRecibo((NotaDebito)correccion)){
				throw new ValidacionException(EValidacionException.NOTA_DEBITO_SE_USA_EN_RECIBO.getInfoValidacion());
			}
			cuentaFacade.borrarMovimientoNotaDebitoCliente((NotaDebito)correccion);
		}
		correccionDao.removeById(correccion.getId());
		return correccion;
	}

	public NotaDebito getNotaDebitoByCheque(Cheque cheque) {
		return correccionDao.getNotaDebitoByCheque(cheque);
	}

	public CorreccionFacturaMobTO getCorreccionMobById(Integer idCorreccion) {
		CorreccionFactura cf = correccionDao.getCorreccionById(idCorreccion);
		return new CorreccionFacturaMobTO(cf, parametrosGeneralesDAO.getParametrosGenerales());
	}

}
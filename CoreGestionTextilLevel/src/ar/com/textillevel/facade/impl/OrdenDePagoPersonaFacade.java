package ar.com.textillevel.facade.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.auditoria.evento.enumeradores.EnumTipoEvento;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.dao.api.local.OrdenDePagoPersonaDAOLocal;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.documentos.pagopersona.OrdenDePagoAPersona;
import ar.com.textillevel.entidades.documentos.pagopersona.formapago.FormaPagoOrdenDePagoPersona;
import ar.com.textillevel.entidades.documentos.pagopersona.formapago.FormaPagoOrdenDePagoPersonaCheque;
import ar.com.textillevel.entidades.enums.EEstadoCheque;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.local.ChequeFacadeLocal;
import ar.com.textillevel.facade.api.local.CuentaFacadeLocal;
import ar.com.textillevel.facade.api.local.OrdenDePagoPersonaFacadeLocal;
import ar.com.textillevel.facade.api.local.ParametrosGeneralesFacadeLocal;
import ar.com.textillevel.facade.api.remote.AuditoriaFacadeLocal;
import ar.com.textillevel.facade.api.remote.OrdenDePagoPersonaFacadeRemote;
import edu.emory.mathcs.backport.java.util.Collections;

@Stateless
public class OrdenDePagoPersonaFacade implements OrdenDePagoPersonaFacadeRemote, OrdenDePagoPersonaFacadeLocal {

	@EJB
	private OrdenDePagoPersonaDAOLocal ordenDao;
	
	@EJB
	private ParametrosGeneralesFacadeLocal parametrosGeneralesFacade;
	
	@EJB
	private ChequeFacadeLocal chequeFacade;
	
	@EJB
	private CuentaFacadeLocal cuentaFacade;
	
	@EJB
	private AuditoriaFacadeLocal<OrdenDePagoAPersona> auditoriaFacade;
	
	public Integer getProximoNumeroOrden() {
		Integer nro = ordenDao.getUltimoNumeroOrden();
		if(nro == null){
			return parametrosGeneralesFacade.getParametrosGenerales().getNroComienzoOrdenDeDeposito();
		}
		return nro+1;
	}

	public OrdenDePagoAPersona guardarOrden(OrdenDePagoAPersona orden, String usrName) throws FWException, ValidacionException {
		orden = guardarInterno(orden, usrName);
		auditoriaFacade.auditar(usrName, "Creacion de Orden de Pago a persona Nº: " + orden.getNroOrden(), EnumTipoEvento.ALTA, orden);
		return orden;
	}

	@SuppressWarnings("unchecked")
	private OrdenDePagoAPersona guardarInterno(OrdenDePagoAPersona orden, String usrName) throws FWException, ValidacionException {
		checkUnicidadNroOrden(orden.getNroOrden());
		guardarCheques(orden, usrName);
		try {
			orden = ordenDao.save(orden);
			cuentaFacade.crearMovimientoHaberPersona(orden);
		} catch(Exception e) {
			if(e.getMessage().contains("ConstraintViolationException")) {
				throw new ValidacionException(EValidacionException.getEnum(EValidacionException.ORDEN_DE_PAGO_PERSONA_DUPLICADA.getCodigo()).getInfoValidacion(), Collections.singletonList(orden.getNroOrden().toString()));
			} else {
				throw e;
			}
		}
		return orden;
	}

	@SuppressWarnings("unchecked")
	private void checkUnicidadNroOrden(Integer nroOrden) throws ValidacionException {
		OrdenDePagoAPersona odp = getOrdenByNro(nroOrden);
		if(odp != null) {
			throw new ValidacionException(EValidacionException.getEnum(EValidacionException.ORDEN_DE_PAGO_PERSONA_DUPLICADA.getCodigo()).getInfoValidacion(), Collections.singletonList(nroOrden.toString()));
		}
	}

	private void guardarCheques(OrdenDePagoAPersona orden, String usrName) throws FWException {
		for(FormaPagoOrdenDePagoPersona fp : orden.getFormasDePago()){
			if(fp instanceof FormaPagoOrdenDePagoPersonaCheque){
				Cheque c = ((FormaPagoOrdenDePagoPersonaCheque)fp).getCheque();
				c.setEstadoCheque(EEstadoCheque.SALIDA_PERSONA);
				c.setPersonaSalida(orden.getPersona());
				c.setFechaSalida(orden.getFecha());
				chequeFacade.grabarCheque(c, usrName);
			}
		}
	}

	public OrdenDePagoAPersona getOrdenByNro(Integer nroOrden) {
		return ordenDao.getOrdenByNro(nroOrden);
	}

	public void eliminarOrden(OrdenDePagoAPersona orden, String usuario) throws FWException{
		orden = borrarOrdenDePagoInterno(orden, usuario);
		auditoriaFacade.auditar(usuario, "Eliminación de órden de pago a persona  Nº: " + orden.getNroOrden(),EnumTipoEvento.BAJA,orden);
	}
	
	private OrdenDePagoAPersona borrarOrdenDePagoInterno(OrdenDePagoAPersona orden, String usuario) throws FWException {
		orden = ordenDao.getOrdenByNro(orden.getNroOrden());
		borrarCheques(orden, usuario);
		cuentaFacade.borrarMovimientoOrdenDePagoPersona(orden);
		ordenDao.removeById(orden.getId());
		return orden;
	}

	private void borrarCheques(OrdenDePagoAPersona orden, String usuario) throws FWException {
		for(FormaPagoOrdenDePagoPersona fp : orden.getFormasDePago()){
			if(fp instanceof FormaPagoOrdenDePagoPersonaCheque){
				Cheque c = ((FormaPagoOrdenDePagoPersonaCheque)fp).getCheque();
				c.setEstadoCheque(EEstadoCheque.EN_CARTERA);
				c.setPersonaSalida(null);
				c.setFechaSalida(null);
				chequeFacade.grabarCheque(c, usuario);
			}
		}
	}

	public OrdenDePagoAPersona editarOrden(OrdenDePagoAPersona orden, String usuario) throws FWException {
		OrdenDePagoAPersona ordenAnterior = ordenDao.getOrdenByNro(orden.getNroOrden());
		cuentaFacade.actualizarMovimientoOrdenDePagoPersona(orden,ordenAnterior.getMontoTotal());
		borrarCheques(orden, usuario);
		guardarCheques(orden, usuario);
		orden = ordenDao.save(orden);
		auditoriaFacade.auditar(usuario, "Edición de Orden de pago a persona Nº: " + orden.getNroOrden(), EnumTipoEvento.MODIFICACION, orden);
		return orden;
	}

	public void confirmarOrden(OrdenDePagoAPersona ordenDePago, String usuario) {
		ordenDePago.setUsuarioVerificador(usuario);
		ordenDao.save(ordenDePago);
		auditoriaFacade.auditar(usuario, "Verificación de Orden de pago a persona Nº: " + ordenDePago.getNroOrden(), EnumTipoEvento.MODIFICACION, ordenDePago);
	}

	@Override
	public void marcarEntregada(String numero, String nombreTerminal) {
		OrdenDePagoAPersona odp = ordenDao.getOrdenByNro(Integer.valueOf(numero));
		if (odp == null) {
			throw new RuntimeException("Orden de pago no encontrada");
		}
		if (odp.getEntregado() == null || odp.getEntregado().equals(Boolean.FALSE)) {
			odp.setEntregado(true);
			odp.setFechaHoraEntregada(DateUtil.getAhora());
			odp.setTerminalEntrega(nombreTerminal);
			auditoriaFacade.auditar(nombreTerminal, "Marcar orden de pago a persona numero: " + numero + " como entregada",
					EnumTipoEvento.MODIFICACION, odp);
		}
	}

	@Override
	public void reingresar(String numero, String nombreTerminal) {
		OrdenDePagoAPersona odp = ordenDao.getOrdenByNro(Integer.valueOf(numero));
		if (odp == null) {
			throw new RuntimeException("Orden de pago no encontrada");
		}
		if (odp.getEntregado() != null && odp.getEntregado().equals(Boolean.TRUE)) {
			odp.setEntregado(false);
			odp.setFechaHoraEntregada(DateUtil.getAhora());
			odp.setTerminalEntrega(nombreTerminal);
			auditoriaFacade.auditar(nombreTerminal, "Reingreso orden de pago a persona numero: " + numero, EnumTipoEvento.MODIFICACION, odp);
		}
	}
}

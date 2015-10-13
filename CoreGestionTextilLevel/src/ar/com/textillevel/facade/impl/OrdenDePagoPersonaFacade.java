package ar.com.textillevel.facade.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.auditoria.evento.enumeradores.EnumTipoEvento;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.textillevel.dao.api.local.OrdenDePagoPersonaDAOLocal;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.documentos.pagopersona.OrdenDePagoAPersona;
import ar.com.textillevel.entidades.documentos.pagopersona.formapago.FormaPagoOrdenDePagoPersona;
import ar.com.textillevel.entidades.documentos.pagopersona.formapago.FormaPagoOrdenDePagoPersonaCheque;
import ar.com.textillevel.entidades.enums.EEstadoCheque;
import ar.com.textillevel.facade.api.local.ChequeFacadeLocal;
import ar.com.textillevel.facade.api.local.CuentaFacadeLocal;
import ar.com.textillevel.facade.api.local.ParametrosGeneralesFacadeLocal;
import ar.com.textillevel.facade.api.remote.AuditoriaFacadeLocal;
import ar.com.textillevel.facade.api.remote.OrdenDePagoPersonaFacadeRemote;

@Stateless
public class OrdenDePagoPersonaFacade implements OrdenDePagoPersonaFacadeRemote{

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

	public OrdenDePagoAPersona guardarOrden(OrdenDePagoAPersona orden, String usrName) throws FWException {
		orden = guardarInterno(orden, usrName);
		auditoriaFacade.auditar(usrName, "Creacion de Orden de Pago a persona Nº: " + orden.getNroOrden(), EnumTipoEvento.ALTA, orden);
		return orden;
	}

	private OrdenDePagoAPersona guardarInterno(OrdenDePagoAPersona orden, String usrName) throws FWException {
		guardarCheques(orden, usrName);
		orden = ordenDao.save(orden);
		cuentaFacade.crearMovimientoHaberPersona(orden);
		return orden;
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
}

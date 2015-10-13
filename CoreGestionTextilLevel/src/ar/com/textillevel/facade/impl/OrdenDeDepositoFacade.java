package ar.com.textillevel.facade.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.auditoria.evento.enumeradores.EnumTipoEvento;
import ar.com.textillevel.dao.api.local.ChequeDAOLocal;
import ar.com.textillevel.dao.api.local.OrdenDeDepositoDAOLocal;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.documentos.ordendedeposito.DepositoCheque;
import ar.com.textillevel.entidades.documentos.ordendedeposito.OrdenDeDeposito;
import ar.com.textillevel.entidades.enums.EEstadoCheque;
import ar.com.textillevel.facade.api.local.CuentaFacadeLocal;
import ar.com.textillevel.facade.api.local.ParametrosGeneralesFacadeLocal;
import ar.com.textillevel.facade.api.remote.AuditoriaFacadeLocal;
import ar.com.textillevel.facade.api.remote.OrdenDeDepositoFacadeRemote;

@Stateless
public class OrdenDeDepositoFacade implements OrdenDeDepositoFacadeRemote{

	@EJB
	private OrdenDeDepositoDAOLocal ordenDao;
	
	@EJB
	private ParametrosGeneralesFacadeLocal parametrosGeneralesFacade;
	
	@EJB
	private AuditoriaFacadeLocal<OrdenDeDeposito> auditoriaFacade;
	
	@EJB
	private ChequeDAOLocal chequeDao;
	
	@EJB
	private CuentaFacadeLocal cuentaFacade;
	
	public Integer getNewNroOrden() {
		Integer nro = ordenDao.getLastNroOrden();
		if(nro == null){
			return parametrosGeneralesFacade.getParametrosGenerales().getNroComienzoOrdenDeDeposito();
		}
		return nro +1;
	}

	public OrdenDeDeposito getOrdenByNro(Integer nroOrden) {
		return ordenDao.getOrdenByNro(nroOrden);
	}

	public OrdenDeDeposito guardarOrden(OrdenDeDeposito orden, String usuario) {
		for(DepositoCheque dep : orden.getDepositos()){
			Cheque cheque = dep.getCheque();
			cheque.setEstadoCheque(EEstadoCheque.SALIDA_BANCO);
			cheque.setBancoSalida(orden.getBanco());
			cheque.setFechaSalida(orden.getFecha());
			chequeDao.save(cheque);
		}
		orden = ordenDao.save(orden);
		cuentaFacade.crearMovimientoHaberBanco(orden);
		auditoriaFacade.auditar(usuario, "Creción de la órden de depósito Nº: " + orden.getNroOrden(), EnumTipoEvento.ALTA, orden);
		return orden;
	}
}

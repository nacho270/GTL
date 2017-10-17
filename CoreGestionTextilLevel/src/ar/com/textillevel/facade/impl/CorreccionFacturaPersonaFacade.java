package ar.com.textillevel.facade.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.auditoria.evento.enumeradores.EnumTipoEvento;
import ar.com.textillevel.dao.api.local.CorreccionFacturaPersonaDAOLocal;
import ar.com.textillevel.entidades.documentos.pagopersona.CorreccionFacturaPersona;
import ar.com.textillevel.entidades.documentos.pagopersona.NotaDebitoPersona;
import ar.com.textillevel.entidades.documentos.pagopersona.visitor.ICorreccionFacturaPersonaVisitor;
import ar.com.textillevel.entidades.gente.Persona;
import ar.com.textillevel.facade.api.local.CorreccionFacturaPersonaFacadeLocal;
import ar.com.textillevel.facade.api.local.CuentaFacadeLocal;
import ar.com.textillevel.facade.api.remote.AuditoriaFacadeLocal;
import ar.com.textillevel.facade.api.remote.CorreccionFacturaPersonaFacadeRemote;

@Stateless
public class CorreccionFacturaPersonaFacade implements CorreccionFacturaPersonaFacadeLocal, CorreccionFacturaPersonaFacadeRemote {

	@EJB
	private CorreccionFacturaPersonaDAOLocal correccionDAO;

	@EJB
	private CuentaFacadeLocal cuentaFacade;
	
	@EJB
	private AuditoriaFacadeLocal<CorreccionFacturaPersona> auditoriaFacade;
	
	public CorreccionFacturaPersona guardarCorreccionYGenerarMovimiento(CorreccionFacturaPersona correccion, String usuario, String obsMovimiento) {
		CorreccionFacturaPersonaPersisterVisitor cfppv = new CorreccionFacturaPersonaPersisterVisitor(usuario, obsMovimiento);
		correccion.accept(cfppv);
		return cfppv.getCorreccionFacturaPersona();
	}

	public void confirmarND(NotaDebitoPersona notaDebitoPersona, String usrName) {
		notaDebitoPersona.setUsuarioConfirmacion(usrName);
		correccionDAO.save(notaDebitoPersona);
		auditoriaFacade.auditar(usrName, "Verificación de Nota de Débito de persona Nº: " + notaDebitoPersona.getNroCorreccion(), EnumTipoEvento.MODIFICACION, notaDebitoPersona);
	}

	public void eliminarCorreccion(CorreccionFacturaPersona correccionFacturaPersona, String usrName) {
		CorreccionFacturaPersona cfp = correccionDAO.getById(correccionFacturaPersona.getId());
		if(cfp instanceof NotaDebitoPersona) {
			NotaDebitoPersona ndp = (NotaDebitoPersona)cfp;
			cuentaFacade.borrarMovimientoNotaDebitoPersona(ndp);
			correccionDAO.removeById(ndp.getId());
			auditoriaFacade.auditar(usrName, "Borrado de nota de débito de persona Nº: " + ndp.getNroCorreccion(), EnumTipoEvento.BAJA, ndp);
			return;
		}
	}
	
	private class CorreccionFacturaPersonaPersisterVisitor implements ICorreccionFacturaPersonaVisitor {

		private final String usuario;
		private final String obsMovimiento;
		private CorreccionFacturaPersona correccionFacturaPersona;

		public CorreccionFacturaPersonaPersisterVisitor(String usuario, String obsMovimiento) {
			this.usuario = usuario;
			this.obsMovimiento = obsMovimiento;
		}

		public CorreccionFacturaPersona getCorreccionFacturaPersona() {
			return correccionFacturaPersona;
		}

		public void visit(NotaDebitoPersona ndp) {
			ndp.setMontoFaltantePorPagar(ndp.getMontoTotal());
			ndp.setUsuarioConfirmacion(usuario);
			if(ndp.getId() == null) { //es una nueva ND
				ndp = (NotaDebitoPersona)correccionDAO.save(ndp);
				correccionFacturaPersona = ndp;
				cuentaFacade.crearMovimientoDebePersona(ndp, obsMovimiento);
				auditoriaFacade.auditar(usuario, "Creación de nota de débito de persona " + ndp.getPersona().toString() + "Nº: " + ndp.getNroCorreccion(), EnumTipoEvento.ALTA, ndp);
			} else {
				cuentaFacade.borrarMovimientoNotaDebitoPersona(ndp);
				ndp = (NotaDebitoPersona)correccionDAO.save(ndp);
				cuentaFacade.crearMovimientoDebePersona(ndp, obsMovimiento);
				auditoriaFacade.auditar(usuario, "Modificación de nota de débito de persona " + ndp.getPersona().toString() + "Nº: " + ndp.getNroCorreccion(), EnumTipoEvento.MODIFICACION, ndp);
			}
		}

	}

	public NotaDebitoPersona getNDPersonaByIdEager(Integer idND) {
		return correccionDAO.getNDByIdEager(idND);
	}

	public boolean existeNroNDParaPersona(Integer nroND, Persona persona) {
		return correccionDAO.existeNroNDParaPersona(nroND, persona);
	}

}
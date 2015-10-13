package ar.com.textillevel.gui.modulos.agenda;

import java.util.List;

import ar.com.fwcommon.boss.BossError;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.ModuloModel;
import ar.com.textillevel.entidades.gente.IAgendable;
import ar.com.textillevel.facade.api.remote.AgendaFacadeRemote;
import ar.com.textillevel.gui.modulos.agenda.builders.BuilderAccionesAgenda;
import ar.com.textillevel.gui.modulos.agenda.cabecera.ModeloCabeceraAgenda;
import ar.com.textillevel.util.GTLBeanFactory;

public class ModuloAgendaModel extends ModuloModel<IAgendable, ModeloCabeceraAgenda>{

	public ModuloAgendaModel(Integer id) throws FWException {
		super(id, BuilderAccionesAgenda.getInstance(),
				BuilderAccionesAgenda.getInstance(), 
				BuilderAccionesAgenda.getInstance(), 
				BuilderAccionesAgenda.getInstance(),
				BuilderAccionesAgenda.getInstance());
		setTitulo("Agenda");
	}

	@Override
	public List<IAgendable> buscarItems(ModeloCabeceraAgenda modeloCabecera) {
		try{
			AgendaFacadeRemote agendaFacade = GTLBeanFactory.getInstance().getBean2(AgendaFacadeRemote.class);
			return agendaFacade.buscar(modeloCabecera.getCriterioBusqueda(), modeloCabecera.getTipoBusqueda(),modeloCabecera.getRubroPersona());
		}catch (FWException ex) {
			BossError.gestionarError(ex);
		}
		return null;
	}
}
package ar.com.textillevel.gui.modulos.eventos;

import java.util.List;

import ar.com.fwcommon.auditoria.ejb.Evento;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.ModuloModel;
import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.facade.api.remote.EventoFacadeRemote;
import ar.com.textillevel.gui.modulos.eventos.builders.BuilderAccionesVisorEventos;
import ar.com.textillevel.gui.modulos.eventos.cabecera.CabeceraVisorEventos;
import ar.com.textillevel.gui.modulos.eventos.cabecera.ModeloCabeceraVisorEventos;
import ar.com.textillevel.util.GTLBeanFactory;

public class ModuloVisorEventosModel extends ModuloModel<Evento, ModeloCabeceraVisorEventos>{

	public ModuloVisorEventosModel(Integer id) throws FWException {
		super(id, BuilderAccionesVisorEventos.getInstance(), 
				  BuilderAccionesVisorEventos.getInstance(), 
				  BuilderAccionesVisorEventos.getInstance(), 
				  BuilderAccionesVisorEventos.getInstance());
		setTitulo("Visor de eventos");
	}
	
	@Override
	public List<Evento> buscarItems(ModeloCabeceraVisorEventos modeloCabecera) {
		return GTLBeanFactory.getInstance().getBean2(EventoFacadeRemote.class).
				getEventosPorUsuarioYFechaPaginado(modeloCabecera.getNombreUsuario(),
						modeloCabecera.getFechaDesde(), DateUtil.getManiana(modeloCabecera.getFechaHasta()), 
						modeloCabecera.getPaginaActual(), CabeceraVisorEventos.MAX_ROWS);
	}
}

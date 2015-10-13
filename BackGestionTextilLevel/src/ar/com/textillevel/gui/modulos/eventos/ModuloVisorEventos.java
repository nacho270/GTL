package ar.com.textillevel.gui.modulos.eventos;

import java.util.ArrayList;
import java.util.List;

import ar.com.fwcommon.auditoria.ejb.Evento;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.cabecera.Cabecera;
import ar.com.fwcommon.templates.modulo.model.ModuloModel;
import ar.com.textillevel.gui.modulos.eventos.cabecera.CabeceraVisorEventos;
import ar.com.textillevel.gui.modulos.eventos.cabecera.ModeloCabeceraVisorEventos;

public class ModuloVisorEventos extends ModuloTemplate<Evento, ModeloCabeceraVisorEventos>{

	private static final long serialVersionUID = -8869723182549481855L;

	public ModuloVisorEventos(Integer idModulo) throws FWException {
		super(idModulo);
		actualizar();
		pack();
	}

	@Override
	protected Cabecera<ModeloCabeceraVisorEventos> createCabecera() {
		return new CabeceraVisorEventos();
	}

	@Override
	protected List<ModuloModel<Evento, ModeloCabeceraVisorEventos>> createModulosModel() throws FWException {
		List<ModuloModel<Evento, ModeloCabeceraVisorEventos>> modulosModel = new ArrayList<ModuloModel<Evento, ModeloCabeceraVisorEventos>>();
		modulosModel.add(new ModuloVisorEventosModel(getIdModulo()));
		return modulosModel;	
	}
	
	@Override
	protected Class<?> [] listenUpdateFor(){
		Class<?> [] clases = new Class<?>[2];
		clases[0] = Evento.class;
		return clases;
	}
}

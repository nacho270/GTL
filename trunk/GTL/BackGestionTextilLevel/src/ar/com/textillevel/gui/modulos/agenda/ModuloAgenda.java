package ar.com.textillevel.gui.modulos.agenda;

import java.util.ArrayList;
import java.util.List;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.ModuloTemplate;
import ar.clarin.fwjava.templates.modulo.cabecera.Cabecera;
import ar.clarin.fwjava.templates.modulo.model.ModuloModel;
import ar.com.textillevel.entidades.gente.IAgendable;
import ar.com.textillevel.gui.modulos.agenda.cabecera.CabeceraAgenda;
import ar.com.textillevel.gui.modulos.agenda.cabecera.ModeloCabeceraAgenda;

public class ModuloAgenda extends ModuloTemplate<IAgendable, ModeloCabeceraAgenda>{

	private static final long serialVersionUID = 1339954315028981028L;

	public ModuloAgenda(Integer idModulo) throws CLException {
		super(idModulo);
		actualizar();
		pack();
	}

	@Override
	protected Cabecera<ModeloCabeceraAgenda> createCabecera() {
		return new CabeceraAgenda();
	}

	@Override
	protected List<ModuloModel<IAgendable, ModeloCabeceraAgenda>> createModulosModel()throws CLException {
		List<ModuloModel<IAgendable,ModeloCabeceraAgenda>> modulosModel = new ArrayList<ModuloModel<IAgendable,ModeloCabeceraAgenda>>();
		modulosModel.add(new ModuloAgendaModel(getIdModulo()));
		return modulosModel;	
	}
	
	@Override
	protected Class<?> [] listenUpdateFor(){
		Class<?> [] clases = new Class<?>[2];
		clases[0] = IAgendable.class;
		return clases;
	}
}

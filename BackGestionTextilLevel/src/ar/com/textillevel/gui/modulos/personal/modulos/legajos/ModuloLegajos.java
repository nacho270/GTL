package ar.com.textillevel.gui.modulos.personal.modulos.legajos;

import java.util.ArrayList;
import java.util.List;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.cabecera.Cabecera;
import ar.com.fwcommon.templates.modulo.model.ModuloModel;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.cabecera.CabeceraLegajos;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.cabecera.ModeloCabeceraLegajos;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;

public class ModuloLegajos extends ModuloTemplate<Empleado, ModeloCabeceraLegajos>{

	private static final long serialVersionUID = 1559337530291271435L;

	public ModuloLegajos(Integer idModulo) throws FWException {
		super(idModulo);
		actualizar();
		pack();
	}

	@Override
	protected Cabecera<ModeloCabeceraLegajos> createCabecera() {
		return new CabeceraLegajos();
	}

	@Override
	protected List<ModuloModel<Empleado, ModeloCabeceraLegajos>> createModulosModel() throws FWException {
		List<ModuloModel<Empleado,ModeloCabeceraLegajos>> modulosModel = new ArrayList<ModuloModel<Empleado,ModeloCabeceraLegajos>>();
		modulosModel.add(new ModuloLegajosModel(getIdModulo()));
		return modulosModel;	
	}
	
	@Override
	protected Class<?> [] listenUpdateFor(){
		Class<?> [] clases = new Class<?>[2];
		clases[0] = Empleado.class;
		return clases;
	}
}

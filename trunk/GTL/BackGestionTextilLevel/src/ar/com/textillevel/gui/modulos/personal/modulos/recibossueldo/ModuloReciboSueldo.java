package ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo;

import java.util.ArrayList;
import java.util.List;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.ModuloTemplate;
import ar.clarin.fwjava.templates.modulo.cabecera.Cabecera;
import ar.clarin.fwjava.templates.modulo.model.ModuloModel;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.cabecera.CabeceraReciboSueldo;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.cabecera.ModeloCabeceraReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.to.InfoReciboSueltoTO;

public class ModuloReciboSueldo extends ModuloTemplate<InfoReciboSueltoTO, ModeloCabeceraReciboSueldo> {

	private static final long serialVersionUID = 1L;

	public ModuloReciboSueldo(Integer idModulo) throws CLException {
		super(idModulo);
		actualizar();
		pack();
	}

	@Override
	protected Cabecera<ModeloCabeceraReciboSueldo> createCabecera() {
		return new CabeceraReciboSueldo();
	}

	@Override
	protected List<ModuloModel<InfoReciboSueltoTO, ModeloCabeceraReciboSueldo>> createModulosModel() throws CLException {
		List<ModuloModel<InfoReciboSueltoTO, ModeloCabeceraReciboSueldo>> modulosModel = new ArrayList<ModuloModel<InfoReciboSueltoTO, ModeloCabeceraReciboSueldo>>();
		modulosModel.add(new ModuloReciboSueldoModel(getIdModulo()));
		return modulosModel;
	}

	@Override
	protected Class<?> [] listenUpdateFor(){
		Class<?> [] clases = new Class<?>[2];
		clases[0] = Empleado.class;
		return clases;
	}
}

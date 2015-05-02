package ar.com.textillevel.gui.modulos.personal.modulos.vales;

import java.util.ArrayList;
import java.util.List;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.ModuloTemplate;
import ar.clarin.fwjava.templates.modulo.cabecera.Cabecera;
import ar.clarin.fwjava.templates.modulo.model.ModuloModel;
import ar.com.textillevel.gui.modulos.personal.modulos.vales.cabecera.CabeceraValesAnticipo;
import ar.com.textillevel.gui.modulos.personal.modulos.vales.cabecera.ModeloCabeceraValesAnticipo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.vale.ValeAnticipo;

public class ModuloValesAnticipo extends ModuloTemplate<ValeAnticipo, ModeloCabeceraValesAnticipo> {

	private static final long serialVersionUID = 1234062409180895905L;

	public ModuloValesAnticipo(Integer idModulo) throws CLException {
		super(idModulo);
		actualizar();
		pack();
	}

	@Override
	protected Cabecera<ModeloCabeceraValesAnticipo> createCabecera() {
		return new CabeceraValesAnticipo();
	}

	@Override
	protected List<ModuloModel<ValeAnticipo, ModeloCabeceraValesAnticipo>> createModulosModel() throws CLException {
		List<ModuloModel<ValeAnticipo,ModeloCabeceraValesAnticipo>> modulosModel = new ArrayList<ModuloModel<ValeAnticipo,ModeloCabeceraValesAnticipo>>();
		modulosModel.add(new ModuloValesAnticipoModel(getIdModulo()));
		return modulosModel;	
	}

	@Override
	protected Class<?> [] listenUpdateFor(){
		Class<?> [] clases = new Class<?>[2];
		clases[0] = ValeAnticipo.class;
		return clases;
	}
}

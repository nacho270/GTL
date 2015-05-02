package ar.com.textillevel.gui.modulos.cheques;

import java.util.ArrayList;
import java.util.List;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.ModuloTemplate;
import ar.clarin.fwjava.templates.modulo.cabecera.Cabecera;
import ar.clarin.fwjava.templates.modulo.model.ModuloModel;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.gui.modulos.cheques.cabecera.CabeceraCheques;
import ar.com.textillevel.gui.modulos.cheques.cabecera.ModeloCabeceraCheques;

public class ModuloCheques extends ModuloTemplate<Cheque, ModeloCabeceraCheques> {

	private static final long serialVersionUID = 7002425448395339601L;

	public ModuloCheques(Integer idModulo) throws CLException {
		super(idModulo);
		setTiempoRefresco(0);
		actualizar();
		pack();
	}

	@Override
	protected Cabecera<ModeloCabeceraCheques> createCabecera() {
		return new CabeceraCheques();
	}

	@Override
	protected List<ModuloModel<Cheque, ModeloCabeceraCheques>> createModulosModel() throws CLException {
		List<ModuloModel<Cheque, ModeloCabeceraCheques>> modulosModel = new ArrayList<ModuloModel<Cheque, ModeloCabeceraCheques>>();
		modulosModel.add(new ModuloChequesModel(getIdModulo()));
		return modulosModel;	
	}
	
	@Override
	protected Class<?> [] listenUpdateFor(){
		Class<?> [] clases = new Class<?>[2];
		clases[0] = Cheque.class;
		return clases;
	}

}

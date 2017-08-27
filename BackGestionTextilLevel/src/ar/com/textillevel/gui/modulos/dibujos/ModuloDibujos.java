package ar.com.textillevel.gui.modulos.dibujos;

import java.util.ArrayList;
import java.util.List;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.cabecera.Cabecera;
import ar.com.fwcommon.templates.modulo.model.ModuloModel;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.gui.modulos.dibujos.cabecera.CabeceraDibujos;
import ar.com.textillevel.gui.modulos.dibujos.cabecera.ModeloCabeceraDibjuos;

public class ModuloDibujos extends ModuloTemplate<DibujoEstampado, ModeloCabeceraDibjuos> {

	private static final long serialVersionUID = 7002425448395339601L;

	public ModuloDibujos(Integer idModulo) throws FWException {
		super(idModulo);
		setTiempoRefresco(0);
		actualizar();
		pack();
	}

	@Override
	protected Cabecera<ModeloCabeceraDibjuos> createCabecera() {
		return new CabeceraDibujos();
	}

	@Override
	protected List<ModuloModel<DibujoEstampado, ModeloCabeceraDibjuos>> createModulosModel() throws FWException {
		List<ModuloModel<DibujoEstampado, ModeloCabeceraDibjuos>> modulosModel = new ArrayList<ModuloModel<DibujoEstampado, ModeloCabeceraDibjuos>>();
		modulosModel.add(new ModuloDibujosModel(getIdModulo()));
		return modulosModel;	
	}
	
	@Override
	protected Class<?> [] listenUpdateFor(){
		Class<?> [] clases = new Class<?>[2];
		clases[0] = DibujoEstampado.class;
		return clases;
	}
}

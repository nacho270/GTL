package ar.com.textillevel.gui.modulos.remitoentrada;

import java.util.ArrayList;
import java.util.List;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.cabecera.Cabecera;
import ar.com.fwcommon.templates.modulo.model.ModuloModel;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.gui.modulos.remitoentrada.cabecera.CabeceraRemitoEntrada;
import ar.com.textillevel.gui.modulos.remitoentrada.cabecera.ModeloCabeceraRemitoEntrada;

public class ModuloRemitoEntrada extends ModuloTemplate<RemitoEntrada, ModeloCabeceraRemitoEntrada> {

	private static final long serialVersionUID = 7002425448395339601L;

	public ModuloRemitoEntrada(Integer idModulo) throws FWException {
		super(idModulo);
		setTiempoRefresco(0);
		actualizar();
		pack();
	}

	@Override
	protected Cabecera<ModeloCabeceraRemitoEntrada> createCabecera() {
		return new CabeceraRemitoEntrada();
	}

	@Override
	protected List<ModuloModel<RemitoEntrada, ModeloCabeceraRemitoEntrada>> createModulosModel() throws FWException {
		List<ModuloModel<RemitoEntrada, ModeloCabeceraRemitoEntrada>> modulosModel = new ArrayList<ModuloModel<RemitoEntrada, ModeloCabeceraRemitoEntrada>>();
		modulosModel.add(new ModuloRemitoEntradaModel(getIdModulo()));
		return modulosModel;	
	}

	
	@Override
	protected Class<?> [] listenUpdateFor(){
		Class<?> [] clases = new Class<?>[2];
		clases[0] = RemitoEntrada.class;
		return clases;
	}

}
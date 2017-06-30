package ar.com.textillevel.gui.modulos.remitosalida;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.cabecera.Cabecera;
import ar.com.fwcommon.templates.modulo.model.ModuloModel;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.gui.modulos.remitosalida.cabecera.CabeceraRemitoSalida;
import ar.com.textillevel.gui.modulos.remitosalida.cabecera.ModeloCabeceraRemitoSalida;
import ar.com.textillevel.gui.util.GenericUtils;

public class ModuloRemitoSalida extends ModuloTemplate<RemitoSalida, ModeloCabeceraRemitoSalida> {

	private static final long serialVersionUID = 7002425448395339601L;

	public ModuloRemitoSalida(Integer idModulo) throws FWException {
		super(idModulo);
		setTiempoRefresco(0);
		actualizar();
		pack();
		Dimension d = GenericUtils.getDimensionPantalla();
		setSize(new Dimension((int)d.getWidth()-10, (int)d.getHeight()-70));
	}

	@Override
	protected Cabecera<ModeloCabeceraRemitoSalida> createCabecera() {
		return new CabeceraRemitoSalida();
	}

	@Override
	protected List<ModuloModel<RemitoSalida, ModeloCabeceraRemitoSalida>> createModulosModel() throws FWException {
		List<ModuloModel<RemitoSalida, ModeloCabeceraRemitoSalida>> modulosModel = new ArrayList<ModuloModel<RemitoSalida, ModeloCabeceraRemitoSalida>>();
		modulosModel.add(new ModuloRemitoSalidaModel(getIdModulo()));
		return modulosModel;
	}

	
	@Override
	protected Class<?> [] listenUpdateFor(){
		Class<?> [] clases = new Class<?>[2];
		clases[0] = RemitoEntrada.class;
		return clases;
	}

}
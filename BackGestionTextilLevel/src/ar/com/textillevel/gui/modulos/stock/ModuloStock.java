package ar.com.textillevel.gui.modulos.stock;

import java.util.ArrayList;
import java.util.List;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.ModuloTemplate;
import ar.clarin.fwjava.templates.modulo.cabecera.Cabecera;
import ar.clarin.fwjava.templates.modulo.model.ModuloModel;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.gui.modulos.stock.cabecera.CabeceraStock;
import ar.com.textillevel.gui.modulos.stock.cabecera.ModeloCabeceraStock;
import ar.com.textillevel.gui.modulos.stock.model.ModuloStockAnilinasModel;
import ar.com.textillevel.gui.modulos.stock.model.ModuloStockCabezalesModel;
import ar.com.textillevel.gui.modulos.stock.model.ModuloStockCilindros;
import ar.com.textillevel.gui.modulos.stock.model.ModuloStockMaterialesDeConstruccionModel;
import ar.com.textillevel.gui.modulos.stock.model.ModuloStockPigmentosModel;
import ar.com.textillevel.gui.modulos.stock.model.ModuloStockQuimicosModel;
import ar.com.textillevel.gui.modulos.stock.model.ModuloStockTelasFisicasModel;
import ar.com.textillevel.gui.modulos.stock.model.ModuloStockTelasModel;

public class ModuloStock extends ModuloTemplate<ItemMateriaPrimaTO, ModeloCabeceraStock> {

	private static final long serialVersionUID = -2712489350418372636L;

	public ModuloStock(Integer idModulo) throws CLException {
		super(idModulo, EModelChangeType.TYPE_TAB);
		actualizar();
		pack();
	}

	@Override
	protected Cabecera<ModeloCabeceraStock> createCabecera() {
		return new CabeceraStock();
	}

	@Override
	protected List<ModuloModel<ItemMateriaPrimaTO, ModeloCabeceraStock>> createModulosModel() throws CLException {
		List<ModuloModel<ItemMateriaPrimaTO, ModeloCabeceraStock>> modulosModel = new ArrayList<ModuloModel<ItemMateriaPrimaTO, ModeloCabeceraStock>>();
		modulosModel.add(new ModuloStockTelasModel(getIdModulo()));
		modulosModel.add(new ModuloStockTelasFisicasModel(getIdModulo()));
		modulosModel.add(new ModuloStockAnilinasModel(getIdModulo()));
		modulosModel.add(new ModuloStockQuimicosModel(getIdModulo()));
		modulosModel.add(new ModuloStockPigmentosModel(getIdModulo()));
		modulosModel.add(new ModuloStockMaterialesDeConstruccionModel(getIdModulo()));
		modulosModel.add(new ModuloStockCilindros(getIdModulo()));
		modulosModel.add(new ModuloStockCabezalesModel(getIdModulo()));
		return modulosModel;
	}

	@Override
	protected Class<?>[] listenUpdateFor() {
		Class<?>[] clases = new Class<?>[1];
		clases[0] = ItemMateriaPrimaTO.class;
		return clases;
	}
}

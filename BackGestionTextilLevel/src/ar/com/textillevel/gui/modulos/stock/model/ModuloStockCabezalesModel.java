package ar.com.textillevel.gui.modulos.stock.model;

import java.util.List;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.ModuloModel;
import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.facade.api.remote.PrecioMateriaPrimaFacadeRemote;
import ar.com.textillevel.gui.modulos.stock.builders.BuilderModuloStockCabezal;
import ar.com.textillevel.gui.modulos.stock.cabecera.ModeloCabeceraStock;
import ar.com.textillevel.util.GTLBeanFactory;

public class ModuloStockCabezalesModel extends ModuloModel<ItemMateriaPrimaTO, ModeloCabeceraStock> {

	public ModuloStockCabezalesModel(Integer id) throws FWException {
		super(id, BuilderModuloStockCabezal.getInstance(), 
				  BuilderModuloStockCabezal.getInstance(), 
				  BuilderModuloStockCabezal.getInstance(),
				  BuilderModuloStockCabezal.getInstance(), 
				  BuilderModuloStockCabezal.getInstance());

		setTitulo("Cabezales");
	}

	@Override
	public List<ItemMateriaPrimaTO> buscarItems(ModeloCabeceraStock modeloCabecera) {
		List<ItemMateriaPrimaTO> precioMateriaPrimaByTipo = GTLBeanFactory.getInstance().getBean2(PrecioMateriaPrimaFacadeRemote.class).getItemsMateriaPrimaByTipoMateriaPrima(
				ETipoMateriaPrima.CABEZAL);
		return precioMateriaPrimaByTipo;
	}

}

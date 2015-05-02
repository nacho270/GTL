package ar.com.textillevel.gui.modulos.stock.model;

import java.util.List;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.ModuloModel;
import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.facade.api.remote.PrecioMateriaPrimaFacadeRemote;
import ar.com.textillevel.gui.modulos.stock.builders.BuilderModuloStockTelas;
import ar.com.textillevel.gui.modulos.stock.cabecera.ModeloCabeceraStock;
import ar.com.textillevel.util.GTLBeanFactory;

public class ModuloStockTelasModel extends ModuloModel<ItemMateriaPrimaTO, ModeloCabeceraStock> {

	public ModuloStockTelasModel(Integer id) throws CLException{
		super(id, BuilderModuloStockTelas.getInstance(),
				  BuilderModuloStockTelas.getInstance(),
				  BuilderModuloStockTelas.getInstance(),
				  BuilderModuloStockTelas.getInstance(), 
				  BuilderModuloStockTelas.getInstance());
		
		setTitulo("Telas (Stock propio)");
	}
	
	@Override
	public List<ItemMateriaPrimaTO> buscarItems(ModeloCabeceraStock modeloCabecera) {
		 List<ItemMateriaPrimaTO> precioMateriaPrimaByTipo = GTLBeanFactory.getInstance().getBean2(PrecioMateriaPrimaFacadeRemote.class).getItemsMateriaPrimaByTipoMateriaPrima(ETipoMateriaPrima.TELA);
		 return precioMateriaPrimaByTipo;
	}
}

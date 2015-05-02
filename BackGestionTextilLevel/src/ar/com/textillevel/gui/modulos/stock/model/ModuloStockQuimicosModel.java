package ar.com.textillevel.gui.modulos.stock.model;

import java.util.List;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.ModuloModel;
import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.facade.api.remote.PrecioMateriaPrimaFacadeRemote;
import ar.com.textillevel.gui.modulos.stock.builders.BuilderModuloStockQuimicos;
import ar.com.textillevel.gui.modulos.stock.cabecera.ModeloCabeceraStock;
import ar.com.textillevel.util.GTLBeanFactory;

public class ModuloStockQuimicosModel extends ModuloModel<ItemMateriaPrimaTO, ModeloCabeceraStock> {

	public ModuloStockQuimicosModel(Integer id) throws CLException{
		super(id, BuilderModuloStockQuimicos.getInstance(),
				  BuilderModuloStockQuimicos.getInstance(),
				  BuilderModuloStockQuimicos.getInstance(),
				  BuilderModuloStockQuimicos.getInstance(),
				  BuilderModuloStockQuimicos.getInstance());
		
		setTitulo("Quimicos");
	}
	
	@Override
	public List<ItemMateriaPrimaTO> buscarItems(ModeloCabeceraStock modeloCabecera) {
		 List<ItemMateriaPrimaTO> precioMateriaPrimaByTipo = GTLBeanFactory.getInstance().getBean2(PrecioMateriaPrimaFacadeRemote.class).getItemsMateriaPrimaByTipoMateriaPrima(ETipoMateriaPrima.QUIMICO);
		 return precioMateriaPrimaByTipo;
	}
}

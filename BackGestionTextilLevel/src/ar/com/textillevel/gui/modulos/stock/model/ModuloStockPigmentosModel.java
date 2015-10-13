package ar.com.textillevel.gui.modulos.stock.model;

import java.util.List;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.ModuloModel;
import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.facade.api.remote.PrecioMateriaPrimaFacadeRemote;
import ar.com.textillevel.gui.modulos.stock.builders.BuilderModuloStockPigmentos;
import ar.com.textillevel.gui.modulos.stock.cabecera.ModeloCabeceraStock;
import ar.com.textillevel.util.GTLBeanFactory;

public class ModuloStockPigmentosModel extends ModuloModel<ItemMateriaPrimaTO, ModeloCabeceraStock>{

	public ModuloStockPigmentosModel(Integer id) throws FWException{
		super(id, BuilderModuloStockPigmentos.getInstance(),
				  BuilderModuloStockPigmentos.getInstance(),
				  BuilderModuloStockPigmentos.getInstance(),
				  BuilderModuloStockPigmentos.getInstance(),
				  BuilderModuloStockPigmentos.getInstance());
		
		setTitulo("Pigmentos");
	}
	
	@Override
	public List<ItemMateriaPrimaTO> buscarItems(ModeloCabeceraStock modeloCabecera) {
		 List<ItemMateriaPrimaTO> precioMateriaPrimaByTipo = GTLBeanFactory.getInstance().getBean2(PrecioMateriaPrimaFacadeRemote.class).getItemsMateriaPrimaByTipoMateriaPrima(ETipoMateriaPrima.PIGMENTO);
		 return precioMateriaPrimaByTipo;
	}
}

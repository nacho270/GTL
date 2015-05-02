package ar.com.textillevel.gui.modulos.stock.model;

import java.util.List;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.ModuloModel;
import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.facade.api.remote.PrecioMateriaPrimaFacadeRemote;
import ar.com.textillevel.facade.api.remote.TipoAnilinaFacadeRemote;
import ar.com.textillevel.gui.modulos.stock.builders.BuilderModuloStockAnilina;
import ar.com.textillevel.gui.modulos.stock.cabecera.ModeloCabeceraStock;
import ar.com.textillevel.util.GTLBeanFactory;

public class ModuloStockAnilinasModel extends ModuloModel<ItemMateriaPrimaTO, ModeloCabeceraStock>{

	public ModuloStockAnilinasModel (Integer idModulo) throws CLException{
		super(idModulo, BuilderModuloStockAnilina.getInstance(),
				  BuilderModuloStockAnilina.getInstance(),
				  BuilderModuloStockAnilina.getInstance(),
				  BuilderModuloStockAnilina.getInstance(),
				  BuilderModuloStockAnilina.getInstance());
		
		setTitulo("Anilinas");
	}
	
	@Override
	public List<ItemMateriaPrimaTO> buscarItems(ModeloCabeceraStock modeloCabecera) {
		List<ItemMateriaPrimaTO> precioMateriaPrimaByTipo = GTLBeanFactory.getInstance().getBean2(PrecioMateriaPrimaFacadeRemote.class).getItemsMateriaPrimaByTipoMateriaPrima(ETipoMateriaPrima.ANILINA);
		actualizarFiltros();
		return precioMateriaPrimaByTipo;
	}
	
	private void actualizarFiltros(){
		BuilderModuloStockAnilina.getInstance().getFiltroTipoAnilina().setValoresLista(GTLBeanFactory.getInstance().getBean2(TipoAnilinaFacadeRemote.class).getAllOrderByName());
	}
}

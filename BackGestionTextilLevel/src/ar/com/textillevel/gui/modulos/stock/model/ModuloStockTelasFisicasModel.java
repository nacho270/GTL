package ar.com.textillevel.gui.modulos.stock.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.ModuloModel;
import ar.com.textillevel.entidades.ventas.DetallePiezaFisicaTO;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.facade.api.remote.PrecioMateriaPrimaFacadeRemote;
import ar.com.textillevel.gui.modulos.stock.builders.BuilderModuloStockTelasFisicas;
import ar.com.textillevel.gui.modulos.stock.cabecera.ModeloCabeceraStock;
import ar.com.textillevel.util.GTLBeanFactory;

public class ModuloStockTelasFisicasModel extends ModuloModel<ItemMateriaPrimaTO, ModeloCabeceraStock> {

	private Set<DetallePiezaFisicaTO> piezasElegidas = new HashSet<DetallePiezaFisicaTO>();

	public ModuloStockTelasFisicasModel(Integer id) throws CLException{
		super(id, BuilderModuloStockTelasFisicas.getInstance(),
				  BuilderModuloStockTelasFisicas.getInstance(),
				  BuilderModuloStockTelasFisicas.getInstance(),
				  BuilderModuloStockTelasFisicas.getInstance(),
				  BuilderModuloStockTelasFisicas.getInstance());
		
		setTitulo("Telas (Stock físico)");
	}

	@Override
	public List<ItemMateriaPrimaTO> buscarItems(ModeloCabeceraStock modeloCabecera) {
		 List<ItemMateriaPrimaTO> precioMateriaPrimaByTipo = GTLBeanFactory.getInstance().getBean2(PrecioMateriaPrimaFacadeRemote.class).getStockTelasFisicas();
		 return precioMateriaPrimaByTipo;
	}

	public Set<DetallePiezaFisicaTO> getPiezasElegidas() {
		return piezasElegidas;
	}

}

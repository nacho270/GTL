package ar.com.textillevel.gui.modulos.personal.abm.tareas.handler;

import java.math.BigDecimal;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Categoria;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.ConfiguracionValorHoraCategoria;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Puesto;
import ar.com.textillevel.modulos.personal.facade.api.remote.ConfiguracionVHCategoriaFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class ValorHoraHandler {
	
	private static ValorHoraHandler instance = new ValorHoraHandler();

	private ConfiguracionVHCategoriaFacadeRemote configFacade;
	
	private ConfiguracionVHCategoriaFacadeRemote getConfigFacade() {
		if(configFacade == null) {
			this.configFacade = GTLPersonalBeanFactory.getInstance().getBean2(ConfiguracionVHCategoriaFacadeRemote.class);
		}
		return configFacade;
	}

	private ValorHoraHandler() {
	}

	public static ValorHoraHandler getInstance() {
		return instance;
	}

	public BigDecimal getValorHoraCategoriaPuesto(Categoria cat, Puesto puesto) {
		ConfiguracionValorHoraCategoria config = getConfigFacade().getConfigActualBySindicato(cat.getSindicato().getId());
		if(config == null) {
			return null;
		} else {
			return config.getValorHoraParaPuestoAndCategoria(cat, puesto);
		}
	}

}
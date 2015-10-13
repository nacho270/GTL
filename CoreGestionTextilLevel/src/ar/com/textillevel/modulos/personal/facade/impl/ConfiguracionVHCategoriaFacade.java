package ar.com.textillevel.modulos.personal.facade.impl;

import java.sql.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.modulos.personal.dao.api.ConfiguracionVHCategoriaDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.ConfiguracionValorHoraCategoria;
import ar.com.textillevel.modulos.personal.facade.api.remote.ConfiguracionVHCategoriaFacadeRemote;

@Stateless
public class ConfiguracionVHCategoriaFacade implements ConfiguracionVHCategoriaFacadeRemote {

	@EJB
	private ConfiguracionVHCategoriaDAOLocal configDAO;

	public void remove(ConfiguracionValorHoraCategoria config) {
		configDAO.removeById(config.getId());
	}

	public ConfiguracionValorHoraCategoria save(ConfiguracionValorHoraCategoria config) {
		return configDAO.save(config);
	}

	public List<ConfiguracionValorHoraCategoria> getAllByIdSindicato(Integer idSindicato) {
		return configDAO.getAllByIdSindicato(idSindicato);
	}

	public ConfiguracionValorHoraCategoria getByIdEager(Integer idConfig) {
		return configDAO.getByIdEager(idConfig);
	}

	public ConfiguracionValorHoraCategoria getConfigActualBySindicato(Integer idSindicato) {
		return configDAO.getConfigByFechaAndSindicato(DateUtil.getHoy(), idSindicato);
	}

	public ConfiguracionValorHoraCategoria getConfigByFechaAndSindicato(Date fecha, Integer idSindicato) {
		return configDAO.getConfigByFechaAndSindicato(fecha, idSindicato);
	}

}
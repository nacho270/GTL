package ar.com.textillevel.modulos.personal.facade.impl;

import java.sql.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.modulos.personal.dao.api.ConfiguracionVacacionesDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.vacaciones.ConfiguracionVacaciones;
import ar.com.textillevel.modulos.personal.facade.api.local.ConfiguracionVacacionesFacadeLocal;
import ar.com.textillevel.modulos.personal.facade.api.remote.ConfiguracionVacacionesFacadeRemote;

@Stateless
public class ConfiguracionVacacionesFacade implements ConfiguracionVacacionesFacadeRemote, ConfiguracionVacacionesFacadeLocal{

	@EJB
	private ConfiguracionVacacionesDAOLocal confVacacionesDao;
	
	public ConfiguracionVacaciones getConfiguracionVacaciones(Date fecha) {
		return confVacacionesDao.getConfiguracionVacaciones(fecha);
	}

	public ConfiguracionVacaciones save(ConfiguracionVacaciones configuracion) {
		return confVacacionesDao.save(configuracion);
	}

	public void remove(ConfiguracionVacaciones configuracionActual) {
		confVacacionesDao.removeById(configuracionActual.getId());
	}

	public List<ConfiguracionVacaciones> getAll() {
		return confVacacionesDao.getAllOrderBy("fechaVigencia");
	}
}

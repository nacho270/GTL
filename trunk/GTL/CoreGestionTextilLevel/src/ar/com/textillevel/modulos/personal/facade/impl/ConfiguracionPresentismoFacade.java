package ar.com.textillevel.modulos.personal.facade.impl;

import java.sql.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.modulos.personal.dao.api.ConfiguracionPresentismoDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.entidades.presentismo.ConfiguracionPresentismo;
import ar.com.textillevel.modulos.personal.facade.api.local.ConfiguracionPresentismoFacadeLocal;
import ar.com.textillevel.modulos.personal.facade.api.remote.ConfiguracionPresentismoFacadeRemote;

@Stateless
public class ConfiguracionPresentismoFacade implements ConfiguracionPresentismoFacadeRemote, ConfiguracionPresentismoFacadeLocal {

	@EJB
	private ConfiguracionPresentismoDAOLocal confDao;
	
	public ConfiguracionPresentismo save(ConfiguracionPresentismo configuracion) {
		return confDao.save(configuracion);
	}

	public List<ConfiguracionPresentismo> getAllBySindicato(Sindicato sindicato) {
		return confDao.getAllBySindicato(sindicato);
	}

	public void remove(ConfiguracionPresentismo configuracion) {
		confDao.removeById(configuracion.getId());
	}

	public List<ConfiguracionPresentismo> getAll() {
		return confDao.getAll();
	}

	public ConfiguracionPresentismo getConfiguracionPosteriorBySindicato(Sindicato sindicato, Date fecha) {
		return confDao.getConfiguracionPosteriorBySindicato(sindicato,fecha);
	}

	public ConfiguracionPresentismo getConfiguracionPresentismoByFechaYSindicato(Date fecha, Sindicato sindicato) {
		return confDao.getConfiguracionPresentismoByFechaYSindicato(fecha,sindicato);
	}
}

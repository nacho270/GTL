package ar.com.textillevel.modulos.personal.dao.api;

import java.sql.Date;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.personal.entidades.vacaciones.ConfiguracionVacaciones;

@Local
public interface ConfiguracionVacacionesDAOLocal extends DAOLocal<ConfiguracionVacaciones, Integer>{

	public ConfiguracionVacaciones getConfiguracionVacaciones(Date fecha);

}

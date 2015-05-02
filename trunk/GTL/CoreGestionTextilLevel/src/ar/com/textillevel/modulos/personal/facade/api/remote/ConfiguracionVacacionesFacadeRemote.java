package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.sql.Date;
import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.personal.entidades.vacaciones.ConfiguracionVacaciones;

@Remote
public interface ConfiguracionVacacionesFacadeRemote {
	public ConfiguracionVacaciones getConfiguracionVacaciones(Date fecha);
	public ConfiguracionVacaciones save(ConfiguracionVacaciones configuracion);
	public void remove(ConfiguracionVacaciones configuracionActual);
	public List<ConfiguracionVacaciones> getAll();
}

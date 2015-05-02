package ar.com.textillevel.modulos.personal.facade.api.local;

import java.sql.Date;

import javax.ejb.Local;

import ar.com.textillevel.modulos.personal.entidades.vacaciones.ConfiguracionVacaciones;

@Local
public interface ConfiguracionVacacionesFacadeLocal {
	public ConfiguracionVacaciones getConfiguracionVacaciones(Date fecha);
}

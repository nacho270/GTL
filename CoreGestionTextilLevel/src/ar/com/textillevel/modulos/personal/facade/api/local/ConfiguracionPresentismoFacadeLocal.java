package ar.com.textillevel.modulos.personal.facade.api.local;

import java.sql.Date;

import javax.ejb.Local;

import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.entidades.presentismo.ConfiguracionPresentismo;

@Local
public interface ConfiguracionPresentismoFacadeLocal {
	public ConfiguracionPresentismo getConfiguracionPresentismoByFechaYSindicato(Date fecha,Sindicato sindicato);
}

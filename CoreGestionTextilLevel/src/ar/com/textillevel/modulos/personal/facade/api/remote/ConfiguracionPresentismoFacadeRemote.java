package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.sql.Date;
import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.entidades.presentismo.ConfiguracionPresentismo;

@Remote
public interface ConfiguracionPresentismoFacadeRemote {

	public ConfiguracionPresentismo save(ConfiguracionPresentismo configuracion);
	public List<ConfiguracionPresentismo> getAllBySindicato(Sindicato sindicato);
	public void remove(ConfiguracionPresentismo configuracion);
	public List<ConfiguracionPresentismo> getAll();
	public ConfiguracionPresentismo getConfiguracionPosteriorBySindicato(Sindicato sindicato, Date fecha);
	public ConfiguracionPresentismo getConfiguracionPresentismoByFechaYSindicato(Date fecha,Sindicato sindicato);

}

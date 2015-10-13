package ar.com.textillevel.modulos.personal.dao.api;

import java.sql.Date;
import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.entidades.presentismo.ConfiguracionPresentismo;

@Local
public interface ConfiguracionPresentismoDAOLocal extends DAOLocal<ConfiguracionPresentismo, Integer>{

	public List<ConfiguracionPresentismo> getAllBySindicato(Sindicato sindicato);
	public ConfiguracionPresentismo getConfiguracionPosteriorBySindicato(Sindicato sindicato, Date fecha);
	public ConfiguracionPresentismo getConfiguracionPresentismoByFechaYSindicato(Date fecha, Sindicato sindicato);

}

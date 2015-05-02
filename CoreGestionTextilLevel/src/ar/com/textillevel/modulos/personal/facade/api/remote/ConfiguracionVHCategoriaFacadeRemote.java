package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.sql.Date;
import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.ConfiguracionValorHoraCategoria;

@Remote
public interface ConfiguracionVHCategoriaFacadeRemote {

	public void remove(ConfiguracionValorHoraCategoria config);
	public ConfiguracionValorHoraCategoria save(ConfiguracionValorHoraCategoria config);
	public List<ConfiguracionValorHoraCategoria> getAllByIdSindicato(Integer idSindicato);
	public ConfiguracionValorHoraCategoria getByIdEager(Integer idSindicato);
	public ConfiguracionValorHoraCategoria getConfigActualBySindicato(Integer idSindicato);
	public ConfiguracionValorHoraCategoria getConfigByFechaAndSindicato(Date fecha, Integer idSindicato);

}

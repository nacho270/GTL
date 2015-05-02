package ar.com.textillevel.modulos.personal.dao.api;

import java.sql.Date;
import java.util.List;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.ConfiguracionValorHoraCategoria;

@Local
public interface ConfiguracionVHCategoriaDAOLocal extends DAOLocal<ConfiguracionValorHoraCategoria, Integer> {

	public List<ConfiguracionValorHoraCategoria> getAllByIdSindicato(Integer idSindicato);

	public ConfiguracionValorHoraCategoria getByIdEager(Integer id);

	public ConfiguracionValorHoraCategoria getConfigByFechaAndSindicato(Date fecha, Integer idSindicato);

}

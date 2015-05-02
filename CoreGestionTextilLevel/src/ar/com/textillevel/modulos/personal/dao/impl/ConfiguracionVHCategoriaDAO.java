package ar.com.textillevel.modulos.personal.dao.impl;

import java.sql.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.ConfiguracionVHCategoriaDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.CategoriaValorPuesto;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.ConfiguracionValorHoraCategoria;

@Stateless
@SuppressWarnings("unchecked")
public class ConfiguracionVHCategoriaDAO extends GenericDAO<ConfiguracionValorHoraCategoria, Integer> implements ConfiguracionVHCategoriaDAOLocal {

	public List<ConfiguracionValorHoraCategoria> getAllByIdSindicato(Integer idSindicato) {
		String hql = "SELECT c From ConfiguracionValorHoraCategoria c WHERE c.sindicato.id = :idSindicato ORDER BY c.fechaDesde ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idSindicato", idSindicato);
		return q.getResultList();
	}

	public ConfiguracionValorHoraCategoria getByIdEager(Integer id) {
		ConfiguracionValorHoraCategoria config = getById(id);
		doEager(config);
		return config;
	}

	public ConfiguracionValorHoraCategoria getConfigByFechaAndSindicato(Date fecha, Integer idSindicato) {
		String hql = "SELECT c From ConfiguracionValorHoraCategoria c " +
					 "WHERE c.sindicato.id = :idSindicato AND " +
					 " 		:fecha BETWEEN c.fechaDesde AND c.fechaHasta ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idSindicato", idSindicato);
		q.setParameter("fecha", fecha);
		List<ConfiguracionValorHoraCategoria> configList = q.getResultList();
		if(configList.isEmpty()) {
			return null;
		} else if(configList.size() == 1) {
			ConfiguracionValorHoraCategoria configuracionValorHoraCategoria = configList.get(0);
			doEager(configuracionValorHoraCategoria);
			return configuracionValorHoraCategoria;
		} else {
			throw new IllegalStateException("Existen dos configuraciones vigentes para el sindicato con id " + idSindicato);
		}
	}

	private void doEager(ConfiguracionValorHoraCategoria config) {
		for(CategoriaValorPuesto cvp : config.getCategoriasValoresPuesto()) {
			cvp.getValoresPuesto().size();
			cvp.getCategoria().getPuestos().size();
		}
	}
	
}

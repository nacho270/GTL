package ar.com.textillevel.modulos.personal.dao.impl;

import java.sql.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.ConfiguracionAntiguedadDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.antiguedad.Antiguedad;
import ar.com.textillevel.modulos.personal.entidades.antiguedad.ConfiguracionAntiguedad;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;

@Stateless
public class ConfiguracionAntiguedadDAO extends GenericDAO<ConfiguracionAntiguedad, Integer> implements ConfiguracionAntiguedadDAOLocal {

	@SuppressWarnings("unchecked")
	public Date getConfiguracionParaFechaAnterior(Sindicato sindicato, Date fechaDesde) {
		String hql = " SELECT ca.fechaDesde FROM ConfiguracionAntiguedad ca WHERE ca.sindicato.id = :idSindicato AND ca.fechaDesde <= :fechaDesde ORDER BY ca.fechaDesde DESC ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idSindicato", sindicato.getId());
		q.setParameter("fechaDesde", fechaDesde);
		List<Date> fechas = q.getResultList();
		if(fechas!=null && !fechas.isEmpty()){
			return fechas.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<ConfiguracionAntiguedad> getAllConfiguracionesBySindicato(Sindicato sindicatoSeleccionado) {
		String hql = " SELECT ca FROM ConfiguracionAntiguedad ca WHERE ca.sindicato.id = :idSindicato ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idSindicato", sindicatoSeleccionado.getId());
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public ConfiguracionAntiguedad getConfiguracionVigenteParaFecha(Sindicato sindicato, Date fecha) {
		String hql = " SELECT ca FROM ConfiguracionAntiguedad ca WHERE ca.sindicato.id = :idSindicato AND ca.fechaDesde <= :fecha ORDER BY ca.fechaDesde DESC ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idSindicato", sindicato.getId());
		q.setParameter("fecha", fecha);
		List<ConfiguracionAntiguedad> configs = q.getResultList();
		if(configs!=null && !configs.isEmpty()){
			ConfiguracionAntiguedad configuracionAntiguedad = configs.get(0);
			for(Antiguedad a : configuracionAntiguedad.getAntiguedades()) {
				a.getValoresAntiguedad().size();
			}
			return configuracionAntiguedad;
		}
		return null;
	}

}

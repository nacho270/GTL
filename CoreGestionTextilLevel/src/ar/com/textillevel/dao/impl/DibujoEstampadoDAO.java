package ar.com.textillevel.dao.impl;

import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.DibujoEstampadoDAOLocal;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.entidades.ventas.articulos.EEstadoDibujo;
import ar.com.textillevel.entidades.ventas.articulos.VarianteEstampado;

@Stateless
@SuppressWarnings("unchecked")
public class DibujoEstampadoDAO extends GenericDAO<DibujoEstampado, Integer> implements DibujoEstampadoDAOLocal {

	public DibujoEstampado getByIdEager(Integer idDibujoEstampado) {
		Query query = getEntityManager().createQuery("FROM DibujoEstampado AS de " +
				 									 "LEFT JOIN FETCH de.variantes var " +
				 									 "WHERE de.id = :idDibujoEstampado");
		query.setParameter("idDibujoEstampado", idDibujoEstampado);
		List<DibujoEstampado> resultList = query.getResultList();
		if(resultList.isEmpty()) {
			return null;
		}
		DibujoEstampado dibujoEstampado = (DibujoEstampado)resultList.get(0);
		for(VarianteEstampado variante : dibujoEstampado.getVariantes()) {
			variante.getColores().size();
		}
		return dibujoEstampado;
	}

	public boolean existsNroDibujo(Integer idDibujo, Integer nro) {
		Query query = getEntityManager().createQuery("SELECT 1 FROM DibujoEstampado AS de " +
													 "WHERE de.id != :idDibujo AND de.nroDibujo = :nro");
		query.setParameter("idDibujo", idDibujo == null ? -1 : idDibujo);
		query.setParameter("nro", nro);
		return !query.getResultList().isEmpty();
	}

	@Override
	public List<DibujoEstampado> getByNroClienteYEstado(Integer nroCliente, EEstadoDibujo estadoDibujo) {
		Query query = getEntityManager().createQuery(" SELECT de FROM DibujoEstampado de " +
													 " WHERE 1 = 1" +
													 (nroCliente != null ? " AND de.cliente.nroCliente = :nroCliente" : "") +
													 (estadoDibujo != null ? " AND de.idEstado = :idEstado" : "") +
													 " ORDER BY de.nroDibujo ASC ");
		if (nroCliente != null) {
			query.setParameter("nroCliente", nroCliente);
		}
		if (estadoDibujo != null) {
			query.setParameter("idEstado", estadoDibujo.getId());
		}
		List<DibujoEstampado> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			return Collections.emptyList();
		}
		for (DibujoEstampado de : resultList) {
			if (de.getCliente() != null) {
				de.getCliente().getCodigoPostal();
			}
			if(de.getVariantes() != null) {
				for (VarianteEstampado variante : de.getVariantes()) {
					variante.getColores().size();
				}
			}
		}
		return resultList;
	}

	@Override
	public List<DibujoEstampado> getAllByClienteAndClienteDefault(Cliente cliente) {
		Query query = getEntityManager().createQuery("FROM DibujoEstampado AS de " + 
													 "LEFT JOIN FETCH de.cliente cl " + 
													 "WHERE cl IS NULL OR cl.id = :idCliente " + 
													 "ORDER BY de.nroDibujo");
		query.setParameter("idCliente", cliente.getId());
		return query.getResultList();
	}

}
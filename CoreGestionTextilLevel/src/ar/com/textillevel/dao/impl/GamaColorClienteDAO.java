package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.GamaColorClienteDAOLocal;
import ar.com.textillevel.entidades.ventas.articulos.GamaColorCliente;

@Stateless
public class GamaColorClienteDAO extends GenericDAO<GamaColorCliente, Integer> implements GamaColorClienteDAOLocal {

	@SuppressWarnings("unchecked")
	public List<GamaColorCliente> getByCliente(Integer idCliente) {
		Query q =  getEntityManager().createQuery(" SELECT gcc FROM GamaColorCliente gcc WHERE gcc.cliente.id = :idCliente");
		q.setParameter("idCliente", idCliente);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public GamaColorCliente getByGama(Integer idGamaOriginal) {
		Query q =  getEntityManager().createQuery(" SELECT gcc FROM GamaColorCliente gcc WHERE gcc.gamaOriginal.id = :idGamaOriginal");
		q.setParameter("idGamaOriginal", idGamaOriginal);
		List<GamaColorCliente> resultList = q.getResultList();
		return resultList!=null && !resultList.isEmpty()?resultList.get(0):null;
	}
}

package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.fwcommon.util.NumUtil;
import ar.com.textillevel.dao.api.local.ServicioDAOLocal;
import ar.com.textillevel.entidades.documentos.factura.proveedor.Servicio;
import ar.com.textillevel.entidades.gente.Proveedor;

@Stateless
@SuppressWarnings("unchecked")
public class ServicioDAO extends GenericDAO<Servicio, Integer> implements ServicioDAOLocal{

	public List<Servicio> getAllOrderByNameEager() {
		String hql = " SELECT s FROM Servicio s JOIN FETCH s.proveedor ORDER BY s.nombre ";
		return getEntityManager().createQuery(hql).getResultList();
	}

	public List<Servicio> getServiciosByProveedor(Integer idProveedor) {
		String hql = " SELECT s FROM Servicio s WHERE s.proveedor.id = :idProveedor ORDER BY s.nombre";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idProveedor", idProveedor);
		return q.getResultList();
	}

	public boolean existeServicio(String nombreServicio, Proveedor proveedor, Integer idAExcluir) {
		String hql = " SELECT COUNT(*) FROM Servicio s " +
					 " WHERE s.proveedor.id = :idProveedor  AND s.nombre = :nombreServicio "+
					 (idAExcluir==null?" " : " AND s.id != :idAExcluir ");
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("nombreServicio", nombreServicio);
		q.setParameter("idProveedor", proveedor.getId());
		if(idAExcluir!=null){
			q.setParameter("idAExcluir", idAExcluir);	
		}
		return NumUtil.toInteger(q.getSingleResult())>0;
	}
}

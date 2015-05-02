package ar.com.textillevel.dao.impl;

import java.sql.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.RemitoEntradaProveedorDAOLocal;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;
import ar.com.textillevel.entidades.gente.Proveedor;

@Stateless
@SuppressWarnings("unchecked")
public class RemitoEntradaProveedorDAO extends GenericDAO<RemitoEntradaProveedor, Integer> implements RemitoEntradaProveedorDAOLocal {

	public List<RemitoEntradaProveedor> getRemitosNoAsocByProveedor(Proveedor proveedor) {
		Query q = getEntityManager().createQuery(
				"SELECT r " +
				"FROM RemitoEntradaProveedor r " +
				"JOIN FETCH r.proveedor p " +
				"WHERE p.id = :idProveedor AND " +
				"NOT EXISTS (SELECT 1 " +
				"			  FROM FacturaProveedor fp " +
				"			  WHERE r IN ELEMENTS(fp.remitoList)" +
				"			) " +
				"ORDER BY r.fechaEmision"
				);
		q.setParameter("idProveedor", proveedor.getId());
		List<RemitoEntradaProveedor> lista = q.getResultList();
		return lista;
	}

	public List<RemitoEntradaProveedor> getRemitosByProveedor(Proveedor proveedor) {
		Query q = getEntityManager().createQuery("SELECT rep FROM RemitoEntradaProveedor rep WHERE rep.proveedor.id = :idProveedor ORDER BY rep.fechaEmision");
		q.setParameter("idProveedor", proveedor.getId());
		List<RemitoEntradaProveedor> lista = q.getResultList();
		for(RemitoEntradaProveedor r : lista){
			r.getPiezas().size();
		}
		return (List<RemitoEntradaProveedor>)q.getResultList();
	}

	public RemitoEntradaProveedor getByIdEager(Integer idRemito) {
		RemitoEntradaProveedor rep = getById(idRemito);
		rep.getPiezas().size();
		rep.getProveedor().getCelular();
		return rep;
	}

	public boolean existeNroFacturaByProveedor(Integer idRemitoEntrada, String nroRemitoEntrada, Integer idProveedor) {
		String hql = "SELECT rr "
				+ " FROM RemitoEntradaProveedor rr "
				+ " WHERE rr.proveedor.id = :idProveedor AND rr.nroRemito = :nroRemitoEntrada AND rr.id != :idRemitoEntrada ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idProveedor", idProveedor);
		q.setParameter("nroRemitoEntrada", nroRemitoEntrada);
		q.setParameter("idRemitoEntrada", idRemitoEntrada == null ? -1 : idRemitoEntrada);
		List<RemitoEntradaProveedor> lista = q.getResultList();
		return !lista.isEmpty();
	}

	public List<RemitoEntradaProveedor> getRemitoEntradaByFechasAndProveedor(Date fechaDesde, Date fechaHasta, Integer idProveedor) {
		String hql = "SELECT rr "
				+ " FROM RemitoEntradaProveedor rr "
				+ " WHERE rr.proveedor.id = :idProveedor AND rr.fechaEmision between :fechaDesde AND :fechaHasta " +
				  " ORDER BY rr.fechaEmision";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idProveedor", idProveedor);
		q.setParameter("fechaDesde", fechaDesde);
		q.setParameter("fechaHasta", fechaHasta);
		List<RemitoEntradaProveedor> lista = q.getResultList();
		return lista;
	}

	public RemitoEntradaProveedor getREProveedorByIdRECliente(Integer idRECliente) {
		String hql = "SELECT rr "
				   + " FROM RemitoEntradaProveedor rr "
				   + " WHERE rr.remitoEntrada.id = :idRECliente ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idRECliente", idRECliente);
		List<RemitoEntradaProveedor> resultList = q.getResultList();
		if(resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0);
		}
	}
	
	

}
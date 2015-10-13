package ar.com.textillevel.dao.impl;

import java.sql.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.MovimientoCuentaArticuloDAOLocal;
import ar.com.textillevel.entidades.cuentaarticulo.movimientos.MovimientoCuentaArticulo;

@Stateless
@SuppressWarnings("unchecked")
public class MovimientoCuentaArticuloDAO extends GenericDAO<MovimientoCuentaArticulo, Integer> implements MovimientoCuentaArticuloDAOLocal {

	public List<MovimientoCuentaArticulo> getMovimientosCuentaArticulo(Integer idCtaArticulo, Date fechaDesde, Date fechaHasta) {
		String query = " SELECT mov From MovimientoCuentaArticulo mov " +
				   	   " WHERE mov.cuenta.id = :idCuenta "+
				   	   (fechaDesde!=null? " AND :fechaDesde <= mov.fechaHora " : "")+
				   	   (fechaHasta!=null? " AND :fechaHasta >= mov.fechaHora " : "") +
				   	   " ORDER BY mov.fechaHora ASC ";
		Query q = getEntityManager().createQuery(query);
		q.setParameter("idCuenta", idCtaArticulo);
		if(fechaDesde!=null){
			q.setParameter("fechaDesde", fechaDesde);
		}
		if(fechaHasta != null){
			q.setParameter("fechaHasta", fechaHasta);
		}
		return q.getResultList();
	}

	public List<MovimientoCuentaArticulo> getMovimientosCuentaArticuloByRemitoSalida(Integer idRemitoSalida) {
		String query = " SELECT mov From MovimientoDebeCuentaArticulo mov " +
	   	   			   " WHERE mov.remitoSalida.id = :idRemitoSalida ";
		Query q = getEntityManager().createQuery(query);
		q.setParameter("idRemitoSalida", idRemitoSalida);
		return q.getResultList();
	}

	public List<MovimientoCuentaArticulo> getMovimientosCuentaArticuloByRemitoEntrada(Integer idRemitoEntrada) {
		String query = " SELECT mov From MovimientoHaberCuentaArticulo mov " +
					   " WHERE mov.remitoEntrada.id = :idRemitoEntrada ";
		Query q = getEntityManager().createQuery(query);
		q.setParameter("idRemitoEntrada", idRemitoEntrada);
		return q.getResultList();
	}

}
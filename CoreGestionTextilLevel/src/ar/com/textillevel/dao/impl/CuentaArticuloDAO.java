package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.CuentaArticuloDAOLocal;
import ar.com.textillevel.entidades.cuentaarticulo.CuentaArticulo;
import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;

@Stateless
@SuppressWarnings("unchecked")
public class CuentaArticuloDAO extends GenericDAO<CuentaArticulo, Integer> implements CuentaArticuloDAOLocal {

	public CuentaArticulo getCuentaArticulo(Cliente cliente, Articulo articulo, EUnidad tipoUnidad) {
		String hql = "SELECT c " +
					  "From CuentaArticulo c " +
					  "WHERE c.cliente.id = :idCliente AND " +
					  "c.articulo.id = :idArticulo AND " +
					  "c.idTipoUnidad = :idTipoUnidad";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idCliente", cliente.getId());
		q.setParameter("idArticulo", articulo.getId());
		q.setParameter("idTipoUnidad", tipoUnidad.getId());
		List<CuentaArticulo> list = q.getResultList();
		if(!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

}

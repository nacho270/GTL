package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.ProductoDAOLocal;
import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.entidades.ventas.productos.ProductoTenido;

@Stateless
public class ProductoDAO extends GenericDAO<Producto, Integer> implements ProductoDAOLocal{

	@SuppressWarnings("unchecked")
	public Producto getProductoByNombre(String nombre) {
		String hql = " SELECT p FROM Producto p WHERE p.descripcion = :descripcion)";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("descripcion", nombre);
		List<ProductoTenido> list = q.getResultList();
		if(list == null || list.isEmpty()){
			return null;
		}
		return list.get(0);
	}
}

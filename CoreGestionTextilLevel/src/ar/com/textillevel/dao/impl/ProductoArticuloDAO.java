package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.ProductoArticuloDAOLocal;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.productos.Producto;

@Stateless
public class ProductoArticuloDAO extends GenericDAO<ProductoArticulo, Integer> implements ProductoArticuloDAOLocal {

	@SuppressWarnings("unchecked")
	public ProductoArticulo getProductoArticulo(Producto prod, Articulo art) {
		String hql = " SELECT pa " +
					 " FROM ProductoArticulo pa " +
					 " WHERE pa.producto = :producto AND pa.articulo = :articulo ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("producto", prod);
		q.setParameter("articulo", art);
		List<ProductoArticulo> resultQueryList = q.getResultList();
		if(resultQueryList.isEmpty()) {
			return null;
		} else if(resultQueryList.size() == 1) {
			return resultQueryList.get(0);
		}
		throw new RuntimeException("Existen 2 ProductoArticulo con PROD: " + prod + " y ART: " + art);
	}

}
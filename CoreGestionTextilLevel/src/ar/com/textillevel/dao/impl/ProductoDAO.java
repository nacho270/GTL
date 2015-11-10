package ar.com.textillevel.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.ProductoDAOLocal;
import ar.com.textillevel.entidades.ventas.productos.Producto;

@Stateless
public class ProductoDAO extends GenericDAO<Producto, Integer> implements ProductoDAOLocal {

	@SuppressWarnings("unchecked")
	public Producto getProductoDelMismoTipoYDatos(Producto producto) {
		String hql = "";
		Map<String, Object> params = new HashMap<String, Object>();
		hql = "SELECT p FROM Producto p WHERE p.descripcion = :descripcion";
		params.put("descripcion", producto.getDescripcion());		
		Query q = getEntityManager().createQuery(hql);
		for(String key : params.keySet()) {
			q.setParameter(key, params.get(key));
		}
		List<Producto> productos = q.getResultList();
		if(productos.isEmpty()) {
			return null;
		} else if(productos.size() == 1) {
			return productos.get(0);
		}
		throw new RuntimeException("Existe más de un producto repetido con datos: " + producto);
	}

}
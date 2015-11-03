package ar.com.textillevel.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.ProductoDAOLocal;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.entidades.ventas.productos.ProductoEstampado;
import ar.com.textillevel.entidades.ventas.productos.ProductoTenido;

@Stateless
public class ProductoDAO extends GenericDAO<Producto, Integer> implements ProductoDAOLocal {

	@SuppressWarnings("unchecked")
	public Producto getProductoDelMismoTipoYDatos(Producto producto) {
		String hql = "";
		Map<String, Object> params = new HashMap<String, Object>();
		if(producto.getTipo() == ETipoProducto.TENIDO) {
			hql = "SELECT pt FROM ProductoTenido pt WHERE pt.gamaColor = :gamaColor AND pt.color = :color";
			params.put("gamaColor", ((ProductoTenido)producto).getGamaColor());
			params.put("color", ((ProductoTenido)producto).getColor());
		} else if(producto.getTipo() == ETipoProducto.ESTAMPADO) {
			hql = "SELECT pe FROM ProductoEstampado pe WHERE pe.dibujo = :dibujo AND pe.variante = :variante";
			params.put("dibujo", ((ProductoEstampado)producto).getDibujo());
			params.put("variante", ((ProductoEstampado)producto).getVariante());
		} else {
			hql = "SELECT p FROM Producto p WHERE p.descripcion = :descripcion";
			params.put("descripcion", producto.getDescripcion());		
		}
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
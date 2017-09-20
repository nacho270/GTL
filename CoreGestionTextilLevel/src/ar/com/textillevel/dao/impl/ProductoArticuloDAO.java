package ar.com.textillevel.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.ProductoArticuloDAOLocal;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;

@Stateless
@SuppressWarnings("unchecked")
public class ProductoArticuloDAO extends GenericDAO<ProductoArticulo, Integer> implements ProductoArticuloDAOLocal {

	public ProductoArticulo getProductoArticulo(ProductoArticulo pa) {
		Map<String, Object> mapParams = new HashMap<String, Object>();
		String hql = " SELECT pa " +
					 " FROM ProductoArticulo pa " +
					 " WHERE pa.producto = :producto AND pa.articulo = :articulo ";
		mapParams.put("producto", pa.getProducto());
		mapParams.put("articulo", pa.getArticulo());
		if(pa.getTipo() == ETipoProducto.TENIDO) {
			hql += " AND pa.gamaColor = :gama AND pa.color = :color ";
			mapParams.put("gama", pa.getGamaColor());
			mapParams.put("color", pa.getColor());
		}
		if(pa.getTipo() == ETipoProducto.ESTAMPADO) {
			hql += " AND pa.dibujo = :dibujo AND pa.variante = :variante ";
			mapParams.put("dibujo", pa.getDibujo());
			mapParams.put("variante", pa.getVariante());
		}
		Query q = getEntityManager().createQuery(hql);
		for(String key : mapParams.keySet()) {
			q.setParameter(key, mapParams.get(key));
		}
		List<ProductoArticulo> resultQueryList = q.getResultList();
		if(resultQueryList.isEmpty()) {
			return null;
		} else if(resultQueryList.size() == 1) {
			return resultQueryList.get(0);
		}
		throw new RuntimeException("Existen 2 ProductoArticulo : " + pa);
	}

	public List<ProductoArticulo> getProductosArticuloByDibujo(DibujoEstampado dibujo) {
		String hql = " SELECT pa " +
				 	 " FROM ProductoArticulo pa " +
				 	 " WHERE pa.dibujo = :dibujo ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("dibujo", dibujo);
		return q.getResultList();
	}

}
package ar.com.textillevel.facade.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.dao.api.local.ProductoArticuloDAOLocal;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.facade.api.remote.ProductoArticuloFacadeRemote;

@Stateless
public class ProductoArticuloFacade implements ProductoArticuloFacadeRemote {

	@EJB
	private ProductoArticuloDAOLocal prodArticuloDao;

	public List<ProductoArticulo> save(List<ProductoArticulo> paList) {
		List<ProductoArticulo> paListPersisted = new ArrayList<ProductoArticulo>();
		for (ProductoArticulo pa : paList) {
			ProductoArticulo paExistente = prodArticuloDao.getProductoArticulo(pa);
			if (paExistente == null) {
				ProductoArticulo persistentPA = prodArticuloDao.save(pa);
				paListPersisted.add(persistentPA);
				persistentPA.setPrecioCalculado(pa.getPrecioCalculado());
			} else {
				paListPersisted.add(paExistente);
				paExistente.setPrecioCalculado(pa.getPrecioCalculado());
			}
		}
		return paListPersisted;
	}

	public ProductoArticulo getById(Integer id) {
		ProductoArticulo pa = prodArticuloDao.getById(id);
		if (pa == null) {
			return null;
		}
		if (pa.getArticulo() != null) {
			pa.getArticulo().getAncho();
			pa.getArticulo().getTipoArticulo().getNombre();
		}
		if (pa.getColor() != null) {
			pa.getColor().getNombre();
			if (pa.getColor().getGama() != null) {
				pa.getColor().getGama().getNombre();
			}
		}
		if (pa.getDibujo() != null) {
			pa.getDibujo().getAnchoCilindro();
		}
		if (pa.getGamaColor() != null) {
			pa.getGamaColor().getNombre();
		}
		if (pa.getProducto() != null) {
			pa.getProducto().getDescripcion();
		}
		if (pa.getVariante() != null) {
			pa.getVariante().getNombre();
		}
		return pa;
	}

}
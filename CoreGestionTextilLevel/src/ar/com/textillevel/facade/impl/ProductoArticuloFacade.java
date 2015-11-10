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
		for(ProductoArticulo pa : paList) {
			ProductoArticulo paExistente = prodArticuloDao.getProductoArticulo(pa);
			if(paExistente == null) {
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

}
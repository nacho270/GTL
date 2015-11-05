package ar.com.textillevel.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.dao.api.local.ArticuloDAOLocal;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.facade.api.remote.ArticuloFacadeRemote;

@Stateless
public class ArticuloFacade implements ArticuloFacadeRemote {

	@EJB
	private ArticuloDAOLocal articuloDao;
	
	public List<Articulo> getAllOrderByName() {
		return articuloDao.getAllOrderBy("nombre");
	}

	public List<Articulo> getArticulosConAlgunaPMPConStockInicial() {
		return articuloDao.getArticulosConAlgunaPMPConStockInicial();
	}
	
	public void remove(Articulo articulo) {
		articuloDao.removeById(articulo.getId());
	}

	public Articulo save(Articulo articulo) {
		return articuloDao.save(articulo);
	}

	public Articulo getById(Integer idArticulo) {
		return articuloDao.getById(idArticulo);
	}

	public List<Articulo> getAllByTipoArticuloOrderByName(Integer idTipoArticulo) {
		return articuloDao.getAllByTipoArticuloOrderByName(idTipoArticulo);
	}

}
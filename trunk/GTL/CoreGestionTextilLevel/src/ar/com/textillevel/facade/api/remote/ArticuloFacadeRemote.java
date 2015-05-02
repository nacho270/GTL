package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.ventas.articulos.Articulo;

@Remote
public interface ArticuloFacadeRemote {
	public Articulo save(Articulo articulo);
	public void remove(Articulo articulo);
	public List<Articulo> getAllOrderByName();
	public Articulo getById(Integer idArticulo);
	public List<Articulo> getArticulosConAlgunaPMPConStockInicial();
}

package ar.com.textillevel.facade.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.dao.api.local.ListaDePreciosDAOLocal;
import ar.com.textillevel.dao.api.local.ProductoDAOLocal;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.cotizacion.DefinicionPrecio;
import ar.com.textillevel.entidades.ventas.cotizacion.ListaDePrecios;
import ar.com.textillevel.entidades.ventas.cotizacion.VersionListaDePrecios;
import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.remote.ListaDePreciosFacadeRemote;

@Stateless
public class ListaDePreciosFacade implements ListaDePreciosFacadeRemote {

	@EJB
	private ListaDePreciosDAOLocal listaDePreciosDAOLocal;

	@EJB
	private ProductoDAOLocal productoDAOLocal;
	
	public ListaDePrecios getListaByIdCliente(Integer idCliente) {
		return listaDePreciosDAOLocal.getListaByIdCliente(idCliente);
	}

	public ListaDePrecios save(ListaDePrecios listaDePreciosActual) {
		return listaDePreciosDAOLocal.save(listaDePreciosActual);
	}

	public void remove(ListaDePrecios listaDePreciosActual) {
		listaDePreciosDAOLocal.removeById(listaDePreciosActual.getId());
	}
	
	public Float getPrecioProducto(Producto producto, Cliente cliente) throws ValidacionException {
		VersionListaDePrecios versionActual = getVersionListaPrecioActual(cliente);
		DefinicionPrecio definicion = versionActual.getDefinicionPorTipoProducto(producto.getTipo());
		return definicion.getPrecio(producto);
	}

	private VersionListaDePrecios getVersionListaPrecioActual(Cliente cliente) throws ValidacionException {
		ListaDePrecios lista = getListaByIdCliente(cliente.getId());
		if (lista == null) {
			throw new ValidacionException(EValidacionException.CLIENTE_SIN_LISTA_PRECIOS.getInfoValidacion());
		}
		return lista.getVersionActual();
	}

	public List<Producto> getProductos(Cliente cliente) throws ValidacionException {
		List<Producto> allProductosCliente = new ArrayList<Producto>();
		VersionListaDePrecios versionListaPrecio = getVersionListaPrecioActual(cliente);
		List<Producto> productos = productoDAOLocal.getAll();
		Map<ETipoProducto, DefinicionPrecio> definicionMap = new HashMap<ETipoProducto, DefinicionPrecio>();
		for(ETipoProducto tp : ETipoProducto.values()) {
			definicionMap.put(tp, versionListaPrecio.getDefinicionPorTipoProducto(tp));
		}
		for(Producto p : productos) {
			DefinicionPrecio definicion = definicionMap.get(p.getTipo());
			if(definicion != null) {
				Float precio = definicion.getPrecio(p);
				if(precio != null) {
					p.setPrecioCalculado(precio);
					allProductosCliente.add(p);
				}
			}
		}
		return allProductosCliente;
	}

}
package ar.com.textillevel.facade.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.dao.api.local.ListaDePreciosDAOLocal;
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
		ListaDePrecios lista = getListaByIdCliente(cliente.getId());
		if (lista == null) {
			throw new ValidacionException(EValidacionException.CLIENTE_SIN_LISTA_PRECIOS.getInfoValidacion());
		}
		VersionListaDePrecios versionActual = lista.getVersionActual();
		DefinicionPrecio definicion = versionActual.getDefinicionPorTipoProducto(producto.getTipo());
		return definicion.getPrecio(producto);
	}
}

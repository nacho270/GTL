package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.cotizacion.ListaDePrecios;
import ar.com.textillevel.entidades.ventas.cotizacion.VersionListaDePrecios;
import ar.com.textillevel.entidades.ventas.productos.Producto;

@Remote
public interface ListaDePreciosFacadeRemote {

	public ListaDePrecios getListaByIdCliente(Integer idCliente);

	public ListaDePrecios save(ListaDePrecios listaDePrecios);

	public void remove(ListaDePrecios listaDePrecios);

	public Float getPrecioProducto(Producto producto, Cliente cliente) throws ValidacionException;

	public List<Producto> getProductos(Cliente cliente) throws ValidacionException;

	public VersionListaDePrecios getVersionListaPrecioActual(Cliente cliente) throws ValidacionException;

}

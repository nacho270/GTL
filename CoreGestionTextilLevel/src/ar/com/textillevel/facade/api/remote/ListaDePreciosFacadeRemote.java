package ar.com.textillevel.facade.api.remote;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.DatosAumentoTO;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.entidades.ventas.cotizacion.Cotizacion;
import ar.com.textillevel.entidades.ventas.cotizacion.ListaDePrecios;
import ar.com.textillevel.entidades.ventas.cotizacion.VersionListaDePrecios;

@Remote
public interface ListaDePreciosFacadeRemote {

	public ListaDePrecios getListaByIdCliente(Integer idCliente);

	public ListaDePrecios save(ListaDePrecios listaDePrecios, Set<Integer> idVersionesABorrar);

	public void remove(ListaDePrecios listaDePrecios);

	public Float getPrecioProducto(ProductoArticulo productoArticulo, Cliente cliente) throws ValidacionException;

	public Float getPrecioProductoPorVersion(ProductoArticulo productoArticulo, Integer idVersion);

	public VersionListaDePrecios getVersionListaPrecioActual(Cliente cliente) throws ValidacionException;

	public Cotizacion generarCotizacion(Cliente cliente, VersionListaDePrecios versionListaDePrecios, Integer validez) throws ValidacionException;

	public Cotizacion getUltimaCotizacionVigente(VersionListaDePrecios version);
	
	public Cotizacion getCotizacionVigente(Cliente cliente);

	public VersionListaDePrecios getVersionActual(Cliente cliente) throws ValidacionException;

	public List<Cliente> getClientesConListaDePrecios();

	public void aumentarPrecios(Cliente cliente, Date inicioValidez, List<DatosAumentoTO> datosAumento, boolean actualizarCotizacion) throws ValidacionException;
	
	public void borrarCotizacion(Cotizacion cotizacion);
}


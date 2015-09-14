package ar.com.textillevel.gui.util;

import java.math.BigDecimal;
import java.util.List;

import javax.swing.JDialog;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.cotizacion.VersionListaDePrecios;
import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;
import ar.com.textillevel.facade.api.remote.ListaDePreciosFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class ProductosAndPreciosHelper {

	private static final int NRO_CLIENTE_DEFAULT = 1;

	private ListaDePreciosFacadeRemote listaDePreciosFacade;
	private ClienteFacadeRemote clienteFacade;
	
	private JDialog dialogoLlamador;
	private Cliente cliente;

	public ProductosAndPreciosHelper(JDialog dialogoLlamador, Cliente cliente) {
		this.dialogoLlamador = dialogoLlamador;
		this.cliente = cliente;
	}

	public BigDecimal getPrecio(Producto producto) {
		Float precio = null;
		boolean precioProdClienteOK = true;
		try {
			precio = getListaDePreciosFacade().getPrecioProducto(producto, cliente);
			precioProdClienteOK = precio != null;
		} catch (ValidacionException e) {
			precioProdClienteOK = false;
		}
		if(!precioProdClienteOK) {
			int resp = CLJOptionPane.showQuestionMessage(dialogoLlamador, StringW.wordWrap("El cliente no posee una lista de precios o bien la tiene pero el producto no está definido en ella.\n ¿Desea usar la lista de precios por defecto?"), "Advertencia");
			if(resp == CLJOptionPane.YES_OPTION) {
				boolean precioProdClienteDefaultOK = true;
				Cliente clienteDefault = getClienteFacade().getClienteByNumero(NRO_CLIENTE_DEFAULT);
				precioProdClienteDefaultOK = clienteDefault != null;
				if(clienteDefault != null) {
					try {
						precio = getListaDePreciosFacade().getPrecioProducto(producto, clienteDefault);
						return new BigDecimal(precio.floatValue());
					} catch (ValidacionException e1) {
						precioProdClienteDefaultOK = false;
					}
				}
				if(!precioProdClienteDefaultOK) {
					CLJOptionPane.showQuestionMessage(dialogoLlamador, StringW.wordWrap("El cliente por defecto no fue cargado o bien el producto no está definido en su lista de precios."), "Advertencia");					
				}
			}
		} else {
			return new BigDecimal(precio.floatValue());
		}
		return null;
	}

	public ResultProductosTO getInfoProductosAndListaDePrecios() {
		try {
			return new ResultProductosTO(getListaDePreciosFacade().getVersionListaPrecioActual(cliente), getListaDePreciosFacade().getProductos(cliente));
		} catch (ValidacionException e) {
			int resp = CLJOptionPane.showQuestionMessage(dialogoLlamador, StringW.wordWrap("El cliente no posee una lista de precios y no se pueden recuperar los productos.\n ¿Desea usar la lista de precios por defecto?"), "Advertencia");
			if(resp == CLJOptionPane.YES_OPTION) {
				Cliente clienteDefault = getClienteFacade().getClienteByNumero(NRO_CLIENTE_DEFAULT);
				if(clienteDefault == null) {
					CLJOptionPane.showErrorMessage(dialogoLlamador, "No fue cargado el cliente NRO. '" + NRO_CLIENTE_DEFAULT + "'.", "Advertencia");
				} else {
					try {
						return new ResultProductosTO(getListaDePreciosFacade().getVersionListaPrecioActual(clienteDefault), getListaDePreciosFacade().getProductos(clienteDefault));
					} catch (ValidacionException e1) {
						CLJOptionPane.showErrorMessage(dialogoLlamador, "La lista de precios por defecto tampoco fue cargada.", "Advertencia");
					}
				}
			}
		}
		return null;
	}

	private ListaDePreciosFacadeRemote getListaDePreciosFacade() {
		if(listaDePreciosFacade == null) {
			this.listaDePreciosFacade = GTLBeanFactory.getInstance().getBean2(ListaDePreciosFacadeRemote.class);
		}
		return listaDePreciosFacade;
	}

	private ClienteFacadeRemote getClienteFacade() {
		if(clienteFacade == null) {
			this.clienteFacade = GTLBeanFactory.getInstance().getBean2(ClienteFacadeRemote.class);
		}
		return clienteFacade;
	}

	public static class ResultProductosTO {
		
		public VersionListaDePrecios versionListaDePrecios;
		public List<Producto> productos;

		public ResultProductosTO(VersionListaDePrecios versionListaDePrecios, List<Producto> productos) {
			this.versionListaDePrecios = versionListaDePrecios;
			this.productos = productos;
		}

	}

}
package ar.com.textillevel.gui.util;

import java.math.BigDecimal;

import javax.swing.JDialog;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.entidades.ventas.cotizacion.VersionListaDePrecios;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;
import ar.com.textillevel.facade.api.remote.ListaDePreciosFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class ProductosAndPreciosHelper {

	private static final int NRO_CLIENTE_DEFAULT = 1;
	private static final String PREGUNTA_CLIENTE_SIN_LISTA_DE_PRECIOS = "El cliente no posee una lista de precios y no se pueden obtener los productos.\n �Desea usar la lista de precios por defecto?";

	private ListaDePreciosFacadeRemote listaDePreciosFacade;
	private ClienteFacadeRemote clienteFacade;
	
	private JDialog dialogoLlamador;
	private Cliente cliente;

	public ProductosAndPreciosHelper(JDialog dialogoLlamador, Cliente cliente) {
		this.dialogoLlamador = dialogoLlamador;
		this.cliente = cliente;
	}

	public BigDecimal getPrecio(ProductoArticulo productoArticulo) {
		Float precio = null;
		boolean precioProdClienteOK = true;
		try {
			precio = getListaDePreciosFacade().getPrecioProducto(productoArticulo, cliente);
			precioProdClienteOK = precio != null;
		} catch (ValidacionException e) {
			precioProdClienteOK = false;
		}
		if(!precioProdClienteOK) {
			int resp = FWJOptionPane.showQuestionMessage(dialogoLlamador, StringW.wordWrap(PREGUNTA_CLIENTE_SIN_LISTA_DE_PRECIOS), "Advertencia");
			if(resp == FWJOptionPane.YES_OPTION) {
				boolean precioProdClienteDefaultOK = true;
				Cliente clienteDefault = getClienteFacade().getClienteByNumero(NRO_CLIENTE_DEFAULT);
				precioProdClienteDefaultOK = clienteDefault != null;
				if(clienteDefault != null) {
					try {
						precio = getListaDePreciosFacade().getPrecioProducto(productoArticulo, clienteDefault);
						if(precio == null) {
							precioProdClienteDefaultOK = false;
						} else {
							return new BigDecimal(precio.floatValue());
						}
					} catch (ValidacionException e1) {
						precioProdClienteDefaultOK = false;
					}
				}
				if(!precioProdClienteDefaultOK) {
					FWJOptionPane.showWarningMessage(dialogoLlamador, StringW.wordWrap("El cliente por defecto no fue cargado o bien el producto '" + productoArticulo + "' no est� definido en su lista de precios. Tambi�n puede revisar si en la lista de precios figura una cotizaci�n que no incluya el producto, si es as�, borrela e intente de nuevo."), "Advertencia");					
				}
			}
		} else {
			return new BigDecimal(precio.floatValue());
		}
		return null;
	}

	public ResultProductosTO getInfoProductosAndListaDePrecios() {
		try {
			return new ResultProductosTO(getListaDePreciosFacade().getVersionListaPrecioActual(cliente));
		} catch (ValidacionException e) {
			int resp = FWJOptionPane.showQuestionMessage(dialogoLlamador, StringW.wordWrap(PREGUNTA_CLIENTE_SIN_LISTA_DE_PRECIOS), "Advertencia");
			if(resp == FWJOptionPane.YES_OPTION) {
				Cliente clienteDefault = getClienteFacade().getClienteByNumero(NRO_CLIENTE_DEFAULT);
				if(clienteDefault == null) {
					FWJOptionPane.showErrorMessage(dialogoLlamador, "No fue cargado el cliente NRO. '" + NRO_CLIENTE_DEFAULT + "'.", "Advertencia");
				} else {
					try {
						return new ResultProductosTO(getListaDePreciosFacade().getVersionListaPrecioActual(clienteDefault));
					} catch (ValidacionException e1) {
						FWJOptionPane.showErrorMessage(dialogoLlamador, "La lista de precios por defecto tampoco fue cargada.", "Advertencia");
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

		public ResultProductosTO(VersionListaDePrecios versionListaDePrecios) {
			this.versionListaDePrecios = versionListaDePrecios;
		}

	}

}
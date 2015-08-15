package ar.com.textillevel.util;

import java.math.BigDecimal;

import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.facade.api.remote.ListaDePreciosFacadeRemote;

public class GestorDeFacturas {
	
	private static GestorDeFacturas instance;
	private ListaDePreciosFacadeRemote listaDePreciosFacade;
	
	private GestorDeFacturas(){
		
	}
	
	public static GestorDeFacturas getInstance(){
		if(instance == null){
			instance = new GestorDeFacturas();
		}
		
		return instance;
	}
	
	public BigDecimal getPrecio(Producto p, Integer idCliente) {
//		ListaDePrecios lp = getListaDePreciosFacade().getListaByIdCliente(idCliente);
//		if (lp == null) {
//			return p.getPrecioDefault();
//		}
//		for (PrecioProducto pp : lp.getPrecios()) {
//			if (pp.getProducto().getId().equals(p.getId())) {
//				return pp.getPrecio();
//			}
//		}
//
//		return p.getPrecioDefault();
		// TODO... seguramente esta clase no exista mas y se haga en un facade directamente
		return null;
	}

	@SuppressWarnings("unused")
	private ListaDePreciosFacadeRemote getListaDePreciosFacade() {
		if (listaDePreciosFacade == null) {
			listaDePreciosFacade = GTLBeanFactory.getInstance().getBean2(ListaDePreciosFacadeRemote.class);
		}
		return listaDePreciosFacade;
	}
}

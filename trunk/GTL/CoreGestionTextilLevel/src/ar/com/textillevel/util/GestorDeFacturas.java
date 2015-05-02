package ar.com.textillevel.util;

import java.math.BigDecimal;
import java.util.List;

import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFactura;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaProducto;
import ar.com.textillevel.entidades.ventas.productos.ListaDePrecios;
import ar.com.textillevel.entidades.ventas.productos.PrecioProducto;
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
	
	public BigDecimal getMontoFactura(Factura factura, List<PrecioProducto> precios){
		BigDecimal montoFinal = new BigDecimal(0);
		montoFinal.add(this.getSubtotal(factura,precios));
		montoFinal.add(this.aplicarImpuestos(factura));
		return montoFinal;
		
	}
	
	public BigDecimal getSubTotalFactura(Factura factura){
		double suma = 0;
		for(ItemFactura f: factura.getItems()){
			suma+=f.getImporte().doubleValue();
		}
		return new BigDecimal(suma);
		
	}
	
	public BigDecimal geTotalFactura(Factura factura){
		double subTotal = getSubTotalFactura(factura).doubleValue();
		if(factura.getPorcentajeIVAInscripto()!=null){
			subTotal*=factura.getPorcentajeIVAInscripto().doubleValue()/100;
		}
		if(factura.getPorcentajeIVANoInscripto()!=null){
			subTotal*=factura.getPorcentajeIVANoInscripto().doubleValue()/100;
		}
		if(factura.getMontoImpuestos()!=null){
			subTotal+=factura.getMontoImpuestos().doubleValue();
		}
		return new BigDecimal(subTotal);
	}
	
	private BigDecimal aplicarImpuestos(Factura factura) {
		// TODO Auto-generated method stub
		return null;
	}

	private BigDecimal getSubtotal(Factura factura,List<PrecioProducto> precios){
		BigDecimal suma = new BigDecimal(0);
		for(ItemFactura it : factura.getItems()){//TODO: VER SI SE PUEDE APLICAR EL VISITOR DEL PACKAGE ar.com.textillevel.entidades.documentos.factura.itemfactura.visitor
			BigDecimal precioUnitario = ((it instanceof ItemFacturaProducto)? getPrecioUnitario((ItemFacturaProducto)it,precios):it.getImporte());
			suma.add(precioUnitario.multiply(it.getCantidad()));
		}
		return suma;
	}

	private BigDecimal getPrecioUnitario(ItemFacturaProducto it,List<PrecioProducto> precios) {
		for(PrecioProducto pp : precios){
			if(pp.getProducto().equals(it.getProducto())){
				return pp.getPrecio();
			}
		}
		return it.getImporte();
	}
	
	public void actualizarEstadoFacturas(){
		/**
		 * TODO: ESTE METODO DEBERIA ACTUALIZAR LAS FACTURAS IMPAGAS. DEBE SALDAR PRIMERO LAS MAS VIEJAS, SALDAR TODAS LAS QUE PUEDA,
		 * CERRALAS Y LAS QUE NO, INDICAR QUE MONTO FALTA PAGAR
		 */
	}
	
	public BigDecimal getPrecio(Producto p, Integer idCliente) {
		ListaDePrecios lp = getListaDePreciosFacade().getListaByIdCliente(idCliente);
		if (lp == null) {
			return p.getPrecioDefault();
		}
		for (PrecioProducto pp : lp.getPrecios()) {
			if (pp.getProducto().getId().equals(p.getId())) {
				return pp.getPrecio();
			}
		}

		return p.getPrecioDefault();
	}

	private ListaDePreciosFacadeRemote getListaDePreciosFacade() {
		if (listaDePreciosFacade == null) {
			listaDePreciosFacade = GTLBeanFactory.getInstance().getBean2(ListaDePreciosFacadeRemote.class);
		}
		return listaDePreciosFacade;
	}
}

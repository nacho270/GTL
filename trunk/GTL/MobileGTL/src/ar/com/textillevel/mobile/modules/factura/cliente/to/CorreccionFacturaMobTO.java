package ar.com.textillevel.mobile.modules.factura.cliente.to;

import java.io.Serializable;
import java.util.List;

import ar.com.textillevel.mobile.modules.common.DocumentoRelMobTO;
import ar.com.textillevel.mobile.modules.cuenta.to.CuentaOwnerTO;

public class CorreccionFacturaMobTO implements Serializable {

	private static final long serialVersionUID = 8911443081882101624L;

	private String nroCorreccion;
	private String fecha;
	private String tipoDocumento;
	private CuentaOwnerTO cliente;
	private String porcIvaInsc;
	private String subTotal;
	private String totalIvaInscr;
	private String totalFactura;
	private List<ItemFacturaTO> items;
	private List<DocumentoRelMobTO> facturas;

	public String getNroCorreccion() {
		return nroCorreccion;
	}

	public void setNroFactura(String nroCorreccion) {
		this.nroCorreccion = nroCorreccion;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public CuentaOwnerTO getCliente() {
		return cliente;
	}

	public void setCliente(CuentaOwnerTO cliente) {
		this.cliente = cliente;
	}

	public String getPorcIvaInsc() {
		return porcIvaInsc;
	}

	public void setPorcIvaInsc(String porcIvaInsc) {
		this.porcIvaInsc = porcIvaInsc;
	}

	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	public String getTotalIvaInscr() {
		return totalIvaInscr;
	}

	public void setTotalIvaInscr(String totalIvaInscr) {
		this.totalIvaInscr = totalIvaInscr;
	}

	public String getTotalFactura() {
		return totalFactura;
	}

	public void setTotalFactura(String totalFactura) {
		this.totalFactura = totalFactura;
	}

	public List<ItemFacturaTO> getItems() {
		return items;
	}

	public void setItems(List<ItemFacturaTO> items) {
		this.items = items;
	}

	public List<DocumentoRelMobTO> getFacturas() {
		return facturas;
	}

}
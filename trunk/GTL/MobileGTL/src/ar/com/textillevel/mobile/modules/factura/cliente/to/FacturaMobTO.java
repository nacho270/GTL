package ar.com.textillevel.mobile.modules.factura.cliente.to;

import java.io.Serializable;
import java.util.List;

import ar.com.textillevel.mobile.modules.common.DocumentoRelMobTO;
import ar.com.textillevel.mobile.modules.cuenta.to.CuentaOwnerTO;


public class FacturaMobTO implements Serializable {

	private static final long serialVersionUID = -6642092684793930515L;

	private String nroFactura;
	private String fecha;
	private String tipoDocumento;
	private String tipoFactura;
	private String condicionVenta;
	private CuentaOwnerTO cliente;
	private String porcIvaInsc;
	private String subTotal;
	private String totalIvaInscr;
	private String totalFactura;
	private List<ItemFacturaTO> items;
	private List<DocumentoRelMobTO> remitos;

	public String getNroFactura() {
		return nroFactura;
	}

	public void setNroFactura(String nroFactura) {
		this.nroFactura = nroFactura;
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

	public String getTipoFactura() {
		return tipoFactura;
	}

	public void setTipoFactura(String tipoFactura) {
		this.tipoFactura = tipoFactura;
	}

	public String getCondicionVenta() {
		return condicionVenta;
	}

	public void setCondicionVenta(String condicionVenta) {
		this.condicionVenta = condicionVenta;
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

	public List<DocumentoRelMobTO> getRemitos() {
		return remitos;
	}

	public void setRemitos(List<DocumentoRelMobTO> remitos) {
		this.remitos = remitos;
	}

}
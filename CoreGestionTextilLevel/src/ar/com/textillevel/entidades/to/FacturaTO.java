package ar.com.textillevel.entidades.to;

import java.io.Serializable;
import java.util.List;

public class FacturaTO implements Serializable {

	private static final long serialVersionUID = 8911443081882101624L;

	private String nroFactura;
	private String fecha;
	private String tipoDocumento;
	private String tipoFactura;
	private String condicionVenta;
	private String nroRemito;
	private ClienteTO cliente;
	private String porcIvaInsc;
	private String subTotal;
	private String totalIvaInscr;
	private String totalFactura;
	private String caeAFIP;
	private List<ItemFacturaTO> items;

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

	public String getNroRemito() {
		return nroRemito;
	}

	public void setNroRemito(String nroRemito) {
		this.nroRemito = nroRemito;
	}

	public ClienteTO getCliente() {
		return cliente;
	}

	public void setCliente(ClienteTO cliente) {
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

	public String getCaeAFIP() {
		return caeAFIP;
	}

	public void setCaeAFIP(String caeAFIP) {
		this.caeAFIP = caeAFIP;
	}

}

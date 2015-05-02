package ar.com.textillevel.entidades.to.facturab;

import java.io.Serializable;
import java.util.List;


public class FacturaBTO implements Serializable {

	private static final long serialVersionUID = 7953249060791936363L;

	private String diaFecha;
	private String mesFecha;
	private String anioFecha;
	private ClienteBTO cliente;
	private String ventaContado;
	private String ventaCtaCTe;
	private String nroRemito;
	private List<ItemFacturaBTO> items;
	private String totalFactura;

	public String getDiaFecha() {
		return diaFecha;
	}

	public void setDiaFecha(String diaFecha) {
		this.diaFecha = diaFecha;
	}

	public String getMesFecha() {
		return mesFecha;
	}

	public void setMesFecha(String mesFecha) {
		this.mesFecha = mesFecha;
	}

	public String getAnioFecha() {
		return anioFecha;
	}

	public void setAnioFecha(String anioFecha) {
		this.anioFecha = anioFecha;
	}

	public ClienteBTO getCliente() {
		return cliente;
	}

	public void setCliente(ClienteBTO cliente) {
		this.cliente = cliente;
	}

	public String getVentaContado() {
		return ventaContado;
	}

	public void setVentaContado(String ventaContado) {
		this.ventaContado = ventaContado;
	}

	public String getVentaCtaCTe() {
		return ventaCtaCTe;
	}

	public void setVentaCtaCTe(String ventaCtaCTe) {
		this.ventaCtaCTe = ventaCtaCTe;
	}

	public String getNroRemito() {
		return nroRemito;
	}

	public void setNroRemito(String nroRemito) {
		this.nroRemito = nroRemito;
	}

	public List<ItemFacturaBTO> getItems() {
		return items;
	}

	public void setItems(List<ItemFacturaBTO> items) {
		this.items = items;
	}

	public String getTotalFactura() {
		return totalFactura;
	}

	public void setTotalFactura(String totalFactura) {
		this.totalFactura = totalFactura;
	}

}

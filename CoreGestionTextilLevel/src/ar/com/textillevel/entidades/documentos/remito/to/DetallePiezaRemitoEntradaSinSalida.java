package ar.com.textillevel.entidades.documentos.remito.to;

import java.io.Serializable;

import ar.com.textillevel.util.ODTCodigoHelper;

public class DetallePiezaRemitoEntradaSinSalida implements Serializable {

	private static final long serialVersionUID = -5825614584384464342L;

	private Integer nroRemito;
	private Integer idODT;
	private String codigoODT;
	private String producto;
	private Integer cantPiezas;
	private double metrosTotales;

	public DetallePiezaRemitoEntradaSinSalida() {

	}

	public DetallePiezaRemitoEntradaSinSalida(Integer nroRemito, Integer idODT, String codigoODT, String producto, Integer cantPiezas, double metrosTotales) {
		this.nroRemito = nroRemito;
		this.idODT = idODT;
		this.codigoODT = codigoODT;
		this.producto = producto;
		this.cantPiezas = cantPiezas;
		this.metrosTotales = metrosTotales;
	}

	public Integer getNroRemito() {
		return nroRemito;
	}

	public void setNroRemito(Integer nroRemito) {
		this.nroRemito = nroRemito;
	}

	public String getCodigoODT() {
		return codigoODT;
	}

	public void setCodigoODT(String codigoODT) {
		this.codigoODT = codigoODT;
	}

	public Integer getCantPiezas() {
		return cantPiezas;
	}

	public void setCantPiezas(Integer cantPiezas) {
		this.cantPiezas = cantPiezas;
	}

	public double getMetrosTotales() {
		return metrosTotales;
	}

	public void setMetrosTotales(double metrosTotales) {
		this.metrosTotales = metrosTotales;
	}

	public Integer getIdODT() {
		return idODT;
	}

	public void setIdODT(Integer idODT) {
		this.idODT = idODT;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(ODTCodigoHelper.getInstance().formatCodigo(codigoODT)).append(" - ")
		   .append(producto).append(" - ")
		   .append("Remito : " + nroRemito).append(" - ")
		   .append("[ " + cantPiezas + " PIEZAS").append(", ").append(metrosTotales).append(" MTS.]");
		return str.toString();
	}

}
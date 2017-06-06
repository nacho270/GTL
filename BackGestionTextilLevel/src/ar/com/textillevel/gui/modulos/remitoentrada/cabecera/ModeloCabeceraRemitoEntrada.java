package ar.com.textillevel.gui.modulos.remitoentrada.cabecera;

import java.sql.Date;

import ar.com.textillevel.entidades.documentos.remito.enums.ESituacionODTRE;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.productos.Producto;

public class ModeloCabeceraRemitoEntrada {

	private Date fechaDesde;
	private Date fechaHasta;
	private Cliente cliente;
	private Producto producto;
	private Integer nroRemito;
	private ESituacionODTRE situacionODT;
	private boolean buscarPorFiltros;

	public ModeloCabeceraRemitoEntrada() {
		this.buscarPorFiltros = true;
	}
	
	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Integer getNroRemito() {
		return nroRemito;
	}

	public void setNroRemito(Integer nroRemito) {
		this.nroRemito = nroRemito;
	}

	public void setBuscarPorFiltros(boolean buscarPorFiltros) {
		this.buscarPorFiltros = buscarPorFiltros;
	}

	public boolean isBuscarPorFiltros() {
		return buscarPorFiltros;
	}

	public ESituacionODTRE getSituacionODT() {
		return situacionODT;
	}

	public void setSituacionODT(ESituacionODTRE situacionODT) {
		this.situacionODT = situacionODT;
	}

}
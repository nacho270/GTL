package ar.com.textillevel.gui.modulos.odt.cabecera;

import java.sql.Date;

import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.modulos.odt.enums.EEstadoODT;

public class ModeloCabeceraODT {

	public Date fechaDesde;
	public Date fechaHasta;
	public EEstadoODT estadoODT;
	private Cliente cliente;
	private Producto producto;
	private String codigoODT;
	private boolean conProductoParcial;
	private boolean buscarPorFiltros;

	public ModeloCabeceraODT() {
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

	public EEstadoODT getEstadoODT() {
		return estadoODT;
	}

	public void setEstadoODT(EEstadoODT estadoODT) {
		this.estadoODT = estadoODT;
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

	public String getCodigoODT() {
		return codigoODT;
	}

	public void setCodigoODT(String codigoODT) {
		this.codigoODT = codigoODT;
	}

	public boolean isBuscarPorFiltros() {
		return buscarPorFiltros;
	}

	public void setBuscarPorFiltros(boolean buscarPorFiltros) {
		this.buscarPorFiltros = buscarPorFiltros;
	}

	public boolean isConProductoParcial() {
		return conProductoParcial;
	}

	public void setConProductoParcial(boolean conProductoParcial) {
		this.conProductoParcial = conProductoParcial;
	}

}
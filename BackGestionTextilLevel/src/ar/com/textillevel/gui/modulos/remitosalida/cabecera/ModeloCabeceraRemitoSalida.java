package ar.com.textillevel.gui.modulos.remitosalida.cabecera;

import java.sql.Date;

import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.gente.Proveedor;

public class ModeloCabeceraRemitoSalida {

	private Date fechaDesde;
	private Date fechaHasta;
	private Cliente cliente;
	private Proveedor proveedor;
	private Integer nroRemito;
	private boolean buscarPorFiltros;

	public ModeloCabeceraRemitoSalida() {
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

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
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
}
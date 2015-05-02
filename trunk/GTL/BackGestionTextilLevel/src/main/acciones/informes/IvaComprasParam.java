package main.acciones.informes;

import java.sql.Date;

import ar.com.textillevel.entidades.gente.Proveedor;

public class IvaComprasParam {

	private Date fechaDesde;
	private Date fechaHasta;
	private Proveedor proveedor;

	public IvaComprasParam(Date fechaDesde, Date fechaHasta, Proveedor proveedor) {
		this.fechaDesde = fechaDesde;
		this.fechaHasta = fechaHasta;
		this.proveedor = proveedor;
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

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

}

package ar.com.fwcommon.templates.modulo.gui.filtros;

import java.io.Serializable;
import java.sql.Date;


public class RangoFechas implements Serializable {

	private Date fechaDesde;
	private Date fechaHasta;
	
	public RangoFechas(){
		super();
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
	
	
}

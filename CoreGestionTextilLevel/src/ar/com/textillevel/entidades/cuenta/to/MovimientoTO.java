package ar.com.textillevel.entidades.cuenta.to;

import java.io.Serializable;

public class MovimientoTO implements Serializable {

	private static final long serialVersionUID = -2661295753816545599L;

	private Integer idMovimiento;
	private String descripcion;
	private Float monto;
	private Float saldoParcial;
	private Integer idDocuemento;
	private Integer idTipoDocumento;
	private String observaciones;
	private boolean transporte;

	public MovimientoTO() {
		this.monto = 0f;
		this.transporte = false;
	}

	public Integer getIdMovimiento() {
		return idMovimiento;
	}

	public void setIdMovimiento(Integer idMovimiento) {
		this.idMovimiento = idMovimiento;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Float getMonto() {
		return monto;
	}

	public void setMonto(Float monto) {
		this.monto = monto;
	}

	public Float getSaldoParcial() {
		return saldoParcial;
	}

	public void setSaldoParcial(Float saldoParcial) {
		this.saldoParcial = saldoParcial;
	}

	public Integer getIdDocuemento() {
		return idDocuemento;
	}

	public void setIdDocuemento(Integer idDocuemento) {
		this.idDocuemento = idDocuemento;
	}

	public Integer getIdTipoDocumento() {
		return idTipoDocumento;
	}

	public void setIdTipoDocumento(Integer idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public boolean isTransporte() {
		return transporte;
	}

	public void setTransporte(boolean transporte) {
		this.transporte = transporte;
	}
}

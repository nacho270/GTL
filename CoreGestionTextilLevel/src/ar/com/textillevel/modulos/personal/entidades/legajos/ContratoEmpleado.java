package ar.com.textillevel.modulos.personal.entidades.legajos;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import ar.com.textillevel.modulos.personal.entidades.contratos.Contrato;

@Entity
@Table(name = "T_PERS_CONTRATO_EMPLEADO")
public class ContratoEmpleado implements Serializable {

	private static final long serialVersionUID = -6661982260863131267L;

	private Integer id;
	private Empleado empleado;
	private Contrato contrato;
	private Date fechaDesde;
	private Integer cantidadDias;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@OneToOne(mappedBy = "contratoEmpleado", fetch = FetchType.LAZY)
	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "F_CONTRATO_P_ID", nullable = false)
	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	@Column(name="A_FECHA_DESDE",nullable=false)
	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	@Column(name="A_CANTIDAD_DIAS")
	public Integer getCantidadDias() {
		return cantidadDias;
	}

	public void setCantidadDias(Integer cantidadDias) {
		this.cantidadDias = cantidadDias;
	}
}

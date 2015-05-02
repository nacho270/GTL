package ar.com.textillevel.modulos.personal.entidades.legajos.legales;

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
import javax.persistence.Table;

import ar.com.textillevel.modulos.personal.entidades.commons.EmpresaSeguros;

@Entity
@Table(name = "T_PERS_DATOS_SEGURO")
public class DatosSeguroEmpleado implements Serializable {

	private static final long serialVersionUID = -3367265784454894020L;

	private Integer id;
	private EmpresaSeguros aseguradora;
	private Integer nroPoliza;
	private Date fechaAlta;
	private Date fechaBaja;

	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="F_EMP_SEG_P_ID",nullable=false)
	public EmpresaSeguros getAseguradora() {
		return aseguradora;
	}

	public void setAseguradora(EmpresaSeguros aseguradora) {
		this.aseguradora = aseguradora;
	}

	@Column(name="A_NRO_POLIZA",nullable=false)
	public Integer getNroPoliza() {
		return nroPoliza;
	}
	
	public void setNroPoliza(Integer nroPoliza) {
		this.nroPoliza = nroPoliza;
	}
	
	@Column(name="A_FECHA_ALTA",nullable=false)
	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	@Column(name="A_FECHA_BAJA",nullable=true)
	public Date getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
}

package ar.com.textillevel.modulos.personal.entidades.legajos;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.com.textillevel.modulos.personal.entidades.legajos.domicilio.Domicilio;

@Entity
@Table(name="T_PERS_DATOS_EMPLS_ANT")
public class DatosEmpleoAnterior implements Serializable {

	private static final long serialVersionUID = 4501193124170671396L;

	private Integer id;
	private String nombreEmpleador;
	private Domicilio domicilioEmpleador;
	private Date fechaDesde;
	private Date fechaHasta;
	private String tareas;
	private Boolean certificado;
	private String referencia;
	
	public DatosEmpleoAnterior() {
		domicilioEmpleador = new Domicilio();
	}

	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="A_NOMBRE_EMPLEADOR")
	public String getNombreEmpleador() {
		return nombreEmpleador;
	}

	public void setNombreEmpleador(String nombreEmpleador) {
		this.nombreEmpleador = nombreEmpleador;
	}

	@ManyToOne(fetch=FetchType.EAGER,cascade={CascadeType.ALL})
	@JoinColumn(name="F_DOMICILIO_P_ID",nullable=false)
	public Domicilio getDomicilioEmpleador() {
		return domicilioEmpleador;
	}

	public void setDomicilioEmpleador(Domicilio domicilioEmpleador) {
		this.domicilioEmpleador = domicilioEmpleador;
	}

	@Column(name="A_FECHA_DESDE",nullable=false)
	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	@Column(name="A_FECHA_HASTA",nullable=false)
	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	@Column(name="A_TAREAS",nullable=false)
	public String getTareas() {
		return tareas;
	}

	public void setTareas(String tareas) {
		this.tareas = tareas;
	}

	@Column(name="A_CERTIFICADO",nullable=false,columnDefinition="INTEGER UNSIGNED DEFAULT 0")
	public Boolean getCertificado() {
		return certificado;
	}
	
	public void setCertificado(Boolean certificado) {
		this.certificado = certificado;
	}
	
	@Column(name="A_REFERENCIA",nullable=false)
	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
}

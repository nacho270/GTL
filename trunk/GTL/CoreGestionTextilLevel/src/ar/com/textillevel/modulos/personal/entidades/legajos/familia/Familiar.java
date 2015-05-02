package ar.com.textillevel.modulos.personal.entidades.legajos.familia;

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
import javax.persistence.Transient;

import ar.com.textillevel.modulos.personal.entidades.legajos.domicilio.Domicilio;

@Entity
@Table(name="T_PERS_FAMILIAR")
public class Familiar implements Serializable {

	private static final long serialVersionUID = -8615551658902368927L;

	private Integer id;
	private Integer idParentesco;
	private String apellido;
	private String nombre;
	private Date fechaNacimiento;
	private Domicilio domicilio;
	private Integer nroDocumento;

	public Familiar() {
		domicilio = new Domicilio();
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

	private Integer getIdParentesco() {
		return idParentesco;
	}

	private void setIdParentesco(Integer idParentesco) {
		this.idParentesco = idParentesco;
	}

	@Column(name="A_APELLIDO",nullable=false)
	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	@Column(name="A_NOMBRE",nullable=false)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name="A_FECHA_NAC",nullable=false)
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	@ManyToOne(fetch=FetchType.EAGER,cascade={CascadeType.ALL})
	@JoinColumn(name="F_DOMICILIO_P_ID",nullable=false)
	public Domicilio getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(Domicilio domicilio) {
		this.domicilio = domicilio;
	}

	@Column(name="A_NRODOC",nullable=false)
	public Integer getNroDocumento() {
		return nroDocumento;
	}

	public void setNroDocumento(Integer nroDocumento) {
		this.nroDocumento = nroDocumento;
	}
	
	@Transient
	public EParentesco getParentesco(){
		return EParentesco.getById(getIdParentesco());
	}
	
	public void setParentesco(EParentesco parentesco){
		if(parentesco == null){
			setIdParentesco(null);
			return;
		}
		setIdParentesco(parentesco.getId());
	}

	@Override
	public String toString() {
		return nombre + " " + apellido;
	}
	
	
}

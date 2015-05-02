package ar.com.textillevel.entidades.gente;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import ar.com.textillevel.entidades.enums.EPosicionIVA;

@Entity
@Table(name="T_PERSONA")
public class Persona implements Serializable, IAgendable {

	private static final long serialVersionUID = 5610829864855884878L;
	
	private Integer id;
	private String nombres;
	private String apellido;
	private InfoDireccion infoDireccion;
	private Rubro rubroPersona;
	private String telefono;
	private String celular;
	private String contacto;
	private String email;
	private String observaciones;

	public Persona() {
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "P_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="A_TELEFONO", nullable=true, length=20)
	public String getTelefono() {
		return telefono;
	}

	@Column(name="A_CELULAR",nullable=true, length=20)
	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}


	@Column(name="A_CONTACTO", nullable=true, length=50)
	public String getContacto() {
		return contacto;
	}

	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	@Column(name="A_OBSERVACIONES", nullable=true, length=1000)
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="F_INFO_DIRECCION_P_ID")
	public InfoDireccion getInfoDireccion() {
		return infoDireccion;
	}

	public void setInfoDireccion(InfoDireccion infoDireccion) {
		this.infoDireccion = infoDireccion;
	}

	@ManyToOne
	@JoinColumn(name="F_RUBRO_P_ID", nullable=false)
	@Fetch(FetchMode.JOIN)
	public Rubro getRubroPersona() {
		return rubroPersona;
	}

	public void setRubroPersona(Rubro rubroPersona) {
		this.rubroPersona = rubroPersona;
	}

	@Column(name="A_EMAIL", nullable=true, length=100)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name="A_NOMBRES", nullable=false, length=100)
	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	@Column(name="A_APELLIDO", nullable=false, length=100)
	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	
	@Override
	@Transient
	public String toString() {
		return getRazonSocial();
	}

	@Transient
	public String getDireccion() {
		if(getInfoDireccion() != null) {
			return getInfoDireccion().getDireccion();
		}
		return null;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Persona other = (Persona) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Transient
	public Integer getCodigoPostal() {
		if(getInfoDireccion() != null && getInfoDireccion().getLocalidad() != null) {
			return getInfoDireccion().getLocalidad().getCodigoPostal();
		}
		return null;
	}

	@Transient
	public String getCuit() {
		return "No aplica";
	}

	@Transient
	public String getLocalidad() {
		if(getInfoDireccion() != null && getInfoDireccion().getLocalidad() != null) {
			return getInfoDireccion().getLocalidad().getNombreLocalidad();
		}
		return null;
	}

	@Transient
	public String getRazonSocial() {
		return getNombres() + " " + getApellido();
	}
	
	@Transient
	public String getFax() {
		return "";
	}

	@Transient
	public String getIdentificadorInterno() {
		return null;
	}

	@Transient
	public EPosicionIVA getPosicionIva() {
		return null;
	}

	@Transient
	public String getCondicionDeVenta() {
		return "";
	}

	@Transient
	public String getNroIngresosBrutos() {
		return "";
	}

}
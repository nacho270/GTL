package ar.com.textillevel.modulos.personal.entidades.legajos.domicilio;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.com.textillevel.entidades.gente.InfoLocalidad;

@Entity
@Table(name = "T_PERS_DOMICILIO")
public class Domicilio implements Serializable {

	private static final long serialVersionUID = -2981839184315899381L;

	private Integer id;
	private String telefono;
	private InfoLocalidad infoLocalidad;
	private Integer numero;
	private String piso;
	private String departamento;
	private String calle;

	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="A_TELEFONO",nullable=true)
	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	@ManyToOne
	@JoinColumn(name="F_INFO_LOC_P_ID",nullable=false)
	public InfoLocalidad getInfoLocalidad() {
		return infoLocalidad;
	}

	public void setInfoLocalidad(InfoLocalidad infoLocalidad) {
		this.infoLocalidad = infoLocalidad;
	}

	@Column(name="A_NUMERO",nullable=false)
	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	@Column(name="A_PISO",nullable=true)
	public String getPiso() {
		return piso;
	}

	public void setPiso(String piso) {
		this.piso = piso;
	}

	@Column(name="A_DTO",nullable=true)
	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	@Column(name="A_CALLE",nullable=false)
	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	@Override
	public String toString() {
		StringBuilder fullDireccion = new StringBuilder();
		fullDireccion.append(calle).append(" ");
		if(numero != null) {
			fullDireccion.append(numero).append(" ");
		}
		if(piso != null) {
			fullDireccion.append(piso).append(" ");
		}
		fullDireccion.append("- ").append(infoLocalidad.toString());
		return fullDireccion.toString();
	}


}

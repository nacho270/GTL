package ar.com.textillevel.modulos.personal.entidades.legajos.legales;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.com.textillevel.modulos.personal.entidades.commons.Art;

@Entity
@Table(name="T_PERS_DATOS_ART")
public class DatosArt implements Serializable{

	private static final long serialVersionUID = 2344878020137710436L;

	private Integer id;
	private Art art;
	private Integer nroCertificado;
	
	private String beneficiario1;
	private String beneficiario2;
	
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
	@JoinColumn(name="F_ART_P_ID",nullable=false)
	public Art getArt() {
		return art;
	}
	
	public void setArt(Art art) {
		this.art = art;
	}
	
	@Column(name="A_NRO_CERT",nullable=false)
	public Integer getNroCertificado() {
		return nroCertificado;
	}

	public void setNroCertificado(Integer nroCertificado) {
		this.nroCertificado = nroCertificado;
	}

	@Column(name="A_BENEFICIARIO1",nullable=false)
	public String getBeneficiario1() {
		return beneficiario1;
	}
	
	public void setBeneficiario1(String beneficiario1) {
		this.beneficiario1 = beneficiario1;
	}
	
	@Column(name="A_BENEFICIARIO2",nullable=false)
	public String getBeneficiario2() {
		return beneficiario2;
	}
	
	public void setBeneficiario2(String beneficiario2) {
		this.beneficiario2 = beneficiario2;
	}
}

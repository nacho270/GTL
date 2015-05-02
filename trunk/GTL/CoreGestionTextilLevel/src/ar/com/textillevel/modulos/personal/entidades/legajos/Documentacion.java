package ar.com.textillevel.modulos.personal.entidades.legajos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ar.com.textillevel.modulos.personal.entidades.commons.AFJP;

@Embeddable
public class Documentacion implements Serializable {

	private static final long serialVersionUID = 7817098307301599361L;

	private String cuit;
	private Integer nroDocumento;
	private Integer nroCedula;
	private Integer nroImpuestoALasGanancias;
	private AFJP Afjp;
	private Integer nroAfiliacionAFJP;

	@Column(name="A_CUIT",nullable=false)
	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	@Column(name="A_NRODOC",nullable=true)
	public Integer getNroDocumento() {
		return nroDocumento;
	}

	public void setNroDocumento(Integer nroDocumento) {
		this.nroDocumento = nroDocumento;
	}

	@Column(name="A_NROCEDULA",nullable=true)
	public Integer getNroCedula() {
		return nroCedula;
	}

	public void setNroCedula(Integer nroCedula) {
		this.nroCedula = nroCedula;
	}

	@Column(name="A_NRO_IMP_GAN",nullable=true)
	public Integer getNroImpuestoALasGanancias() {
		return nroImpuestoALasGanancias;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_AFJP_P_ID")
	public AFJP getAfjp() {
		return Afjp;
	}
	
	public void setAfjp(AFJP afjp) {
		Afjp = afjp;
	}
	
	public void setNroImpuestoALasGanancias(Integer nroImpuestoALasGanancias) {
		this.nroImpuestoALasGanancias = nroImpuestoALasGanancias;
	}

	@Column(name="A_NRO_AFJP",nullable=true)
	public Integer getNroAfiliacionAFJP() {
		return nroAfiliacionAFJP;
	}

	public void setNroAfiliacionAFJP(Integer nroAfiliacionAFJP) {
		this.nroAfiliacionAFJP = nroAfiliacionAFJP;
	}
}

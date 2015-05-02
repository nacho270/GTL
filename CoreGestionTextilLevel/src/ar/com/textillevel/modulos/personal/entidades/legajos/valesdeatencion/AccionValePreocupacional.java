package ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue(value="ACCVP")
public class AccionValePreocupacional extends AccionValeAtencion<ValePreocupacional> {

	private static final long serialVersionUID = 6621166345683885266L;

	private String observaciones;

	@Column(name = "A_OBSERVACIONES", nullable=true)
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	@Override
	@Transient
	public String getDescrResumen() {
		StringBuilder sb = new StringBuilder();
		sb = sb.append("Estado del Vale: " + getEstadoValeAtencion().toString());
		return sb.toString();
	}

	@Override
	public String calculateNombreDocumento() {
		return "VALEPREOC-" + getId();
	}

}
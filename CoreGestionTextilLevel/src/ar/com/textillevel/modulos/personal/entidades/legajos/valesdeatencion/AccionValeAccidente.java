package ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.fwcommon.util.DateUtil;

@Entity
@DiscriminatorValue(value="ACCVA")
public class AccionValeAccidente extends AccionValeAtencion<ValeAccidente> {

	private static final long serialVersionUID = -4513134974953052695L;

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
		if(getFechaControl() != null) {
			sb.append("\n").append("Fecha de Control: " + DateUtil.dateToString(getFechaControl()));
		}
		return sb.toString();
	}

	@Override
	public String calculateNombreDocumento() {
		return "VALEACC-" + getId();
	}

}
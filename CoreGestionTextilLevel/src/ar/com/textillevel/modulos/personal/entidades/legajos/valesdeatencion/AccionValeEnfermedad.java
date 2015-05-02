package ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.clarin.fwjava.util.DateUtil;

@Entity
@DiscriminatorValue(value="ACCVE")
public class AccionValeEnfermedad extends AccionValeAtencion<ValeEnfermedad> {

	private static final long serialVersionUID = -1155846774350375083L;

	private Integer diasReposo;
	private String observaciones;

	@Column(name = "A_DIAS_REPOSO", nullable=true)
	public Integer getDiasReposo() {
		return diasReposo;
	}

	public void setDiasReposo(Integer diasReposo) {
		this.diasReposo = diasReposo;
	}

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
		return "VALEENF-" + getId();
	}

}
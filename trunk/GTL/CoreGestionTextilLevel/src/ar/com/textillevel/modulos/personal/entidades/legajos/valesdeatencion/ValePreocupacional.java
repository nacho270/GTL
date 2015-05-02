package ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue(value="VALPRE")
public class ValePreocupacional extends ValeAtencion {

	private static final long serialVersionUID = -6612458657032774910L;

	private String observaciones;
	private Horario horarioIngreso;
	
	public ValePreocupacional() {
		setEstadoValeAtencion(EEStadoValeEnfermedad.JUSTIFICADO_Y_ALTA);
		setAsistioAlTrabajo(true);
		this.horarioIngreso = new Horario();
		setHorarioSalida(new Horario());
	}

	@Column(name = "A_OBSERVACIONES", nullable=true)
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	@Embedded
	public Horario getHorarioIngreso() {
		return horarioIngreso;
	}

	public void setHorarioIngreso(Horario horarioIngreso) {
		this.horarioIngreso = horarioIngreso;
	}

	@Override
	@Transient
	public String getResumen() {
		StringBuilder sb = new StringBuilder();
		if(getAsistioAlTrabajo() != null && getAsistioAlTrabajo()) {
			sb.append("Ingresó a las: " + getHorarioIngreso().toString() + " hrs.");
		} else {
			sb.append("No asistió al trabajo");
		}
		return sb.toString();
	}

	@Override
	@Transient
	public ETipoVale getTipoVale() {
		return ETipoVale.PREOCUPACIONAL;
	}

}
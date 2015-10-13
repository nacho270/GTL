package ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.StringUtil;

@Entity
@DiscriminatorValue(value="VALENF")
public class ValeEnfermedad extends ValeAtencion {

	private static final long serialVersionUID = 596056047052750717L;

	private String descrEnfermedad;
	private String diagnostico;
	private Horario horarioTrabajo;

	public ValeEnfermedad() {
		this.horarioTrabajo = new Horario();
		setHorarioSalida(new Horario());
		setAsistioAlTrabajo(true);
		setEstadoValeAtencion(EEStadoValeEnfermedad.ABIERTO);
	}

	@Column(name = "A_DESCR_ENFERMEDAD", nullable=true)
	public String getDescrEnfermedad() {
		return descrEnfermedad;
	}

	public void setDescrEnfermedad(String descrEnfermedad) {
		this.descrEnfermedad = descrEnfermedad;
	}

	@Column(name = "A_DIAGNOSTICO", nullable=true)
	public String getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	@Embedded
	public Horario getHorarioTrabajo() {
		return horarioTrabajo;
	}

	public void setHorarioTrabajo(Horario horarioTrabajo) {
		this.horarioTrabajo = horarioTrabajo;
	}

	@Override
	@Transient
	public String getResumen() {
		StringBuilder sb = new StringBuilder();
		sb = sb.append("Enfermedad:" + getDescrEnfermedad());
		if(getAsistioAlTrabajo() != null && getAsistioAlTrabajo()) {
			sb = sb.append("\n").append("Se retira: " + getHorarioSalida().toString() + " hrs.");
			sb = sb.append("\n").append("Debió trabajar hasta: " + getHorarioTrabajo().toString() + " hrs.");
		} else {
			sb = sb.append("\n").append("No asistió al trabajo.");
		}
		sb = sb.append("\n").append("Estado del Vale: " + getEstadoValeAtencion().toString());
		if(getEstadoValeAtencion() == EEStadoValeEnfermedad.JUSTIFICADO_Y_CONTROL && getFechaControl() != null) {
			sb.append("\n").append("Fecha de Control: " + DateUtil.dateToString(getFechaControl()));
		}
		if(getEstadoValeAtencion() == EEStadoValeEnfermedad.JUSTIFICADO_Y_ALTA && getFechaAlta() != null) {
			sb.append("\n").append("Fecha de Alta: " + DateUtil.dateToString(getFechaAlta()));
		}
		if(!StringUtil.isNullOrEmpty(getDiagnostico())) {
			sb.append("\n").append("Diagnóstico: " + getDiagnostico());
		}
		return sb.toString();
	}

	@Override
	@Transient
	public ETipoVale getTipoVale() {
		return ETipoVale.ENFERMEDAD;
	}

}
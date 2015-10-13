package ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.fwcommon.util.DateUtil;

@Entity
@DiscriminatorValue(value="VALACC")
public class ValeAccidente extends ValeAtencion {

	private static final long serialVersionUID = -8887496058137966467L;

	private String descrAccidente;
	private Horario horarioTrabajo;

	public ValeAccidente() {
		this.horarioTrabajo = new Horario();
		setHorarioSalida(new Horario());
		setEstadoValeAtencion(EEStadoValeEnfermedad.ABIERTO);
		setAsistioAlTrabajo(true);
	}

	@Column(name = "A_DESCR_ACCIDENTE", nullable=true)
	public String getDescrAccidente() {
		return descrAccidente;
	}

	public void setDescrAccidente(String descrAccidente) {
		this.descrAccidente = descrAccidente;
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
		sb.append("Accidente:" + getDescrAccidente());
		sb = sb.append("\n").append("Se retira: " + getHorarioSalida().toString() + " hrs.");
		sb = sb.append("\n").append("Debió trabajar hasta: " + getHorarioTrabajo().toString() + " hrs.");
		sb = sb.append("\n").append("Estado del Vale: " + getEstadoValeAtencion().toString());
		if(getEstadoValeAtencion() == EEStadoValeEnfermedad.JUSTIFICADO_Y_CONTROL && getFechaControl() != null) {
			sb.append("\n").append("Fecha de Control: " + DateUtil.dateToString(getFechaControl()));
		}
		if(getEstadoValeAtencion() == EEStadoValeEnfermedad.JUSTIFICADO_Y_ALTA && getFechaAlta() != null) {
			sb.append("\n").append("Fecha de Alta: " + DateUtil.dateToString(getFechaAlta()));
		}
		return sb.toString();
	}

	@Override
	@Transient
	public ETipoVale getTipoVale() {
		return ETipoVale.ACCIDENTE;
	}

}
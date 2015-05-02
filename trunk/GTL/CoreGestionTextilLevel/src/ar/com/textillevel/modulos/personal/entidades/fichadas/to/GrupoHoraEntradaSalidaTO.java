package ar.com.textillevel.modulos.personal.entidades.fichadas.to;

import java.io.Serializable;
import java.sql.Timestamp;

public class GrupoHoraEntradaSalidaTO implements Serializable {

	private static final long serialVersionUID = -7449963127397352673L;

	private Timestamp horaEntrada;
	private Timestamp horaSalida;

	public GrupoHoraEntradaSalidaTO() {
		super();
	}

	public GrupoHoraEntradaSalidaTO(Timestamp horaEntrada, Timestamp horaSalida) {
		super();
		this.horaEntrada = horaEntrada;
		this.horaSalida = horaSalida;
	}

	public Timestamp getHoraEntrada() {
		return horaEntrada;
	}

	public void setHoraEntrada(Timestamp horaEntrada) {
		this.horaEntrada = horaEntrada;
	}

	public Timestamp getHoraSalida() {
		return horaSalida;
	}

	public void setHoraSalida(Timestamp horaSalida) {
		this.horaSalida = horaSalida;
	}

	public boolean estaCompleto() {
		return horaEntrada!=null && horaSalida!=null;
	}
	
	public long getDiferenciaHorario(){
		return horaSalida.getTime() - horaEntrada.getTime();
	}
}

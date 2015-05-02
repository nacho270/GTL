package ar.com.textillevel.gui.acciones.odt.event;

import ar.com.textillevel.modulos.odt.to.ODTTO;

public class BotonCambiarMaquinaEvent {
	
	private Integer idMaquina;
	private Integer idTipoMaquina;
	private ODTTO odtTO;

	public BotonCambiarMaquinaEvent(ODTTO odtTO,Integer idTipoMaquina, Integer idMaquina) {
		this.odtTO = odtTO;
		this.idTipoMaquina = idTipoMaquina;
	}

	public Integer getIdTipoMaquina() {
		return idTipoMaquina;
	}

	public void setIdTipoMaquina(Integer idTipoMaquina) {
		this.idTipoMaquina = idTipoMaquina;
	}

	public ODTTO getOdtTO() {
		return odtTO;
	}

	public void setOdtTO(ODTTO odtTO) {
		this.odtTO = odtTO;
	}

	public Integer getIdMaquina() {
		return idMaquina;
	}

	public void setIdMaquina(Integer idMaquina) {
		this.idMaquina = idMaquina;
	}
}
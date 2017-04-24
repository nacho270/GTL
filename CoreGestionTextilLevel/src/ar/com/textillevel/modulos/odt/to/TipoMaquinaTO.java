package ar.com.textillevel.modulos.odt.to;

import java.io.Serializable;

import ar.com.textillevel.modulos.odt.entidades.maquinas.TipoMaquina;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;

public class TipoMaquinaTO implements Serializable {

	private static final long serialVersionUID = 5889484357895480827L;

	private Integer idTipoMaquina;
	private String tipoMaquina;
	private Byte ordenTipoMaquina;
	private ESectorMaquina sector;

	public TipoMaquinaTO() {

	}

	public TipoMaquinaTO(TipoMaquina tipoMaquina) {
		this.idTipoMaquina = tipoMaquina.getId();
		this.ordenTipoMaquina = tipoMaquina.getOrden();
		this.tipoMaquina = tipoMaquina.getNombre();
		this.sector = tipoMaquina.getSectorMaquina();
	}

	public Integer getIdTipoMaquina() {
		return idTipoMaquina;
	}

	public void setIdTipoMaquina(Integer idTipoMaquina) {
		this.idTipoMaquina = idTipoMaquina;
	}

	public String getTipoMaquina() {
		return tipoMaquina;
	}

	public void setTipoMaquina(String tipoMaquina) {
		this.tipoMaquina = tipoMaquina;
	}

	public Byte getOrdenTipoMaquina() {
		return ordenTipoMaquina;
	}

	public void setOrdenTipoMaquina(Byte ordenTipoMaquina) {
		this.ordenTipoMaquina = ordenTipoMaquina;
	}

	public ESectorMaquina getSector() {
		return sector;
	}

	public void setSector(ESectorMaquina sector) {
		this.sector = sector;
	}

}

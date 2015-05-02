package ar.com.textillevel.modulos.odt.to;

import java.io.Serializable;

import ar.com.textillevel.modulos.odt.entidades.maquinas.TipoMaquina;

public class TipoMaquinaTO implements Serializable {

	private static final long serialVersionUID = 5889484357895480827L;

	private Integer idTipoMaquina;
	private String tipoMaquina;
	private Byte ordenTipoMaquina;

	public TipoMaquinaTO() {

	}

	public TipoMaquinaTO(Integer idTipoMaquina, String tipoMaquina, Byte ordenTipoMaquina) {
		this.idTipoMaquina = idTipoMaquina;
		this.tipoMaquina = tipoMaquina;
		this.ordenTipoMaquina = ordenTipoMaquina;
	}

	public TipoMaquinaTO(TipoMaquina tipoMaquina) {
		this.idTipoMaquina = tipoMaquina.getId();
		this.ordenTipoMaquina = tipoMaquina.getOrden();
		this.tipoMaquina = tipoMaquina.getNombre();
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

}

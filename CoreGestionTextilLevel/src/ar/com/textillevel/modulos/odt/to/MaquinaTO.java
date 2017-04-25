package ar.com.textillevel.modulos.odt.to;

import java.io.Serializable;

import ar.com.textillevel.modulos.odt.entidades.maquinas.Maquina;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;

public class MaquinaTO implements Serializable {

	private static final long serialVersionUID = 6270515099522844018L;

	private Integer id;
	private String nombre;
	private Integer idTipoMaquina;
	private ESectorMaquina sector;
	
	public MaquinaTO(Maquina maquina) {
		this.id = maquina.getId();
		this.nombre = maquina.getNombre();
		this.idTipoMaquina = maquina.getTipoMaquina().getId();
		this.sector = maquina.getTipoMaquina().getSectorMaquina();
	}

	public Integer getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	
	public Integer getIdTipoMaquina() {
		return idTipoMaquina;
	}

	public ESectorMaquina getSector() {
		return sector;
	}

}

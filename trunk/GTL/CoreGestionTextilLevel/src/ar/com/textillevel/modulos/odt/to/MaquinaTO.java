package ar.com.textillevel.modulos.odt.to;

import java.io.Serializable;

import ar.com.textillevel.modulos.odt.entidades.maquinas.Maquina;

public class MaquinaTO implements Serializable {

	private static final long serialVersionUID = 6270515099522844018L;

	private Integer id;
	private String nombre;
	private Integer idTipoMaquina;
	
	public MaquinaTO(Maquina maquina) {
		this.id = maquina.getId();
		this.nombre = maquina.getNombre();
		this.idTipoMaquina = maquina.getTipoMaquina().getId();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	
	public Integer getIdTipoMaquina() {
		return idTipoMaquina;
	}

	
	public void setIdTipoMaquina(Integer idTipoMaquina) {
		this.idTipoMaquina = idTipoMaquina;
	}
}

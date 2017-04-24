package ar.com.textillevel.modulos.odt.to;

import java.io.Serializable;
import ar.com.textillevel.modulos.odt.entidades.maquinas.Maquina;

public class InfoAsignacionMaquinaTO implements Serializable {

	private static final long serialVersionUID = 5307607845710121463L;

	private Maquina maquina;
	private short ordenEnMaquina;

	public InfoAsignacionMaquinaTO(Maquina maquina, short ordenEnMaquina) {
		this.maquina = maquina;
		this.ordenEnMaquina = ordenEnMaquina;
	}

	public Maquina getMaquina() {
		return maquina;
	}

	public short getOrdenEnMaquina() {
		return ordenEnMaquina;
	}

}

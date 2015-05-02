package ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.wf;

import java.io.Serializable;
import java.util.Set;

import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.EEStadoCartaDocumento;

public abstract class EstadoSancion implements Serializable {

	private static final long serialVersionUID = -5342359925997016917L;

	private EEStadoCartaDocumento enumEstado;
	
	public EEStadoCartaDocumento getEnumEstado() {
		return enumEstado;
	}

	public EstadoSancion(EEStadoCartaDocumento enumEstado) {
		this.enumEstado = enumEstado;
	}

	public abstract Set<EstadoSancion> getEstadosSiguientes();

	public abstract void accept(IEstadoSancionVisitor visitor);

}


package ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.wf;

import java.io.Serializable;
import java.util.Set;

import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.EEStadoValeEnfermedad;

public abstract class EstadoValeAtencion implements Serializable {

	private static final long serialVersionUID = 2113159948404460337L;

	private EEStadoValeEnfermedad enumEstado;
	
	public EEStadoValeEnfermedad getEnumEstado() {
		return enumEstado;
	}

	public EstadoValeAtencion(EEStadoValeEnfermedad enumEstado) {
		this.enumEstado = enumEstado;
	}

	public abstract Set<EstadoValeAtencion> getEstadosSiguientes();

	public abstract void accept(IEstadoValeAtencionVisitor visitor);

}


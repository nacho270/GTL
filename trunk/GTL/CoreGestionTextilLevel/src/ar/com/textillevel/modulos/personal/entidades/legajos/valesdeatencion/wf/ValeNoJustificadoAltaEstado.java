package ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.wf;

import java.util.Collections;
import java.util.Set;

import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.EEStadoValeEnfermedad;

public class ValeNoJustificadoAltaEstado extends EstadoValeAtencion {

	private static final long serialVersionUID = -6464740119367203270L;

	public ValeNoJustificadoAltaEstado() {
		super(EEStadoValeEnfermedad.NO_JUSTIFICADO);
	}

	@Override
	public Set<EstadoValeAtencion> getEstadosSiguientes() {
		return Collections.emptySet();
	}

	@Override
	public void accept(IEstadoValeAtencionVisitor visitor) {
		visitor.visit(this);
	}


}

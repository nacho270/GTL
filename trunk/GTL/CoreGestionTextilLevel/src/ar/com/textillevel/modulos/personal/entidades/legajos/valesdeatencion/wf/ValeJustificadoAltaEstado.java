package ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.wf;

import java.util.Collections;
import java.util.Set;

import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.EEStadoValeEnfermedad;

public class ValeJustificadoAltaEstado extends EstadoValeAtencion {

	private static final long serialVersionUID = 4091661646701404337L;

	public ValeJustificadoAltaEstado() {
		super(EEStadoValeEnfermedad.JUSTIFICADO_Y_ALTA);
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
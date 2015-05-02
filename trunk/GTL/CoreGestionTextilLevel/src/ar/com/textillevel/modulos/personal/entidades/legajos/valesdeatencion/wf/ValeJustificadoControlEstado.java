package ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.wf;

import java.util.HashSet;
import java.util.Set;

import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.EEStadoValeEnfermedad;

public class ValeJustificadoControlEstado extends EstadoValeAtencion {

	private static final long serialVersionUID = -5858127054170791933L;

	public ValeJustificadoControlEstado() {
		super(EEStadoValeEnfermedad.JUSTIFICADO_Y_CONTROL);
	}

	@Override
	public Set<EstadoValeAtencion> getEstadosSiguientes() {
		Set<EstadoValeAtencion> estadosSiguientes = new HashSet<EstadoValeAtencion>();
		estadosSiguientes.add(new ValeJustificadoControlEstado());
		estadosSiguientes.add(new ValeJustificadoAltaEstado());
		return estadosSiguientes;
	}

	@Override
	public void accept(IEstadoValeAtencionVisitor visitor) {
		visitor.visit(this);
	}


}

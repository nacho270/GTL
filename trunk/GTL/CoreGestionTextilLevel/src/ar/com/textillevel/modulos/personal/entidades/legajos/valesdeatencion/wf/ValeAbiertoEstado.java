package ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.wf;

import java.util.HashSet;
import java.util.Set;

import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.EEStadoValeEnfermedad;

public class ValeAbiertoEstado extends EstadoValeAtencion {

	private static final long serialVersionUID = -8394906645246339957L;

	public ValeAbiertoEstado() {
		super(EEStadoValeEnfermedad.ABIERTO);
	}

	@Override
	public Set<EstadoValeAtencion> getEstadosSiguientes() {
		Set<EstadoValeAtencion> estadosSiguientes = new HashSet<EstadoValeAtencion>();
		estadosSiguientes.add(new ValeJustificadoAltaEstado());
		estadosSiguientes.add(new ValeNoJustificadoAltaEstado());
		estadosSiguientes.add(new ValeJustificadoControlEstado());
		return estadosSiguientes;
	}

	@Override
	public void accept(IEstadoValeAtencionVisitor visitor) {
		visitor.visit(this);
	}

}
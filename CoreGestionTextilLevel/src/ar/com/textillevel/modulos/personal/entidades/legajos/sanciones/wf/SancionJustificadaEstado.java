package ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.wf;

import java.util.Collections;
import java.util.Set;

import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.EEStadoCartaDocumento;

public class SancionJustificadaEstado extends EstadoSancion {

	private static final long serialVersionUID = -1734789051203363786L;

	public SancionJustificadaEstado() {
		super(EEStadoCartaDocumento.JUSTIFICADA);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<EstadoSancion> getEstadosSiguientes() {
		return Collections.EMPTY_SET;
	}

	@Override
	public void accept(IEstadoSancionVisitor visitor) {
		visitor.visit(this);
	}

}

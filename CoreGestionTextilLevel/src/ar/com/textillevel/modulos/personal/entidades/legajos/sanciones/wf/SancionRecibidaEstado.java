package ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.wf;

import java.util.HashSet;
import java.util.Set;

import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.EEStadoCartaDocumento;

public class SancionRecibidaEstado extends EstadoSancion {

	private static final long serialVersionUID = -7617477369654640458L;

	public SancionRecibidaEstado() {
		super(EEStadoCartaDocumento.RECIBIDA);
	}

	@Override
	public Set<EstadoSancion> getEstadosSiguientes() {
		Set<EstadoSancion> estadosSiguientes = new HashSet<EstadoSancion>();
		estadosSiguientes.add(new SancionJustificadaEstado());
		return estadosSiguientes;
	}

	@Override
	public void accept(IEstadoSancionVisitor visitor) {
		visitor.visit(this);
	}

}

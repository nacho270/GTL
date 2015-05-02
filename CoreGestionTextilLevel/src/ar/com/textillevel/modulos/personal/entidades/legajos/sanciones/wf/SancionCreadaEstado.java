package ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.wf;

import java.util.HashSet;
import java.util.Set;

import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.EEStadoCartaDocumento;

public class SancionCreadaEstado extends EstadoSancion {

	private static final long serialVersionUID = -5175245788572553926L;

	public SancionCreadaEstado() {
		super(EEStadoCartaDocumento.CREADA);
	}

	@Override
	public Set<EstadoSancion> getEstadosSiguientes() {
		Set<EstadoSancion> estadosSiguientes = new HashSet<EstadoSancion>();
		estadosSiguientes.add(new SancionImpresaEstado());
		return estadosSiguientes;
	}

	@Override
	public void accept(IEstadoSancionVisitor visitor) {
		visitor.visit(this);
	}

}

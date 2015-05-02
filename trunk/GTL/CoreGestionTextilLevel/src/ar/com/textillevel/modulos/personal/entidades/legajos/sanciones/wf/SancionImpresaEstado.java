package ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.wf;

import java.util.HashSet;
import java.util.Set;

import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.EEStadoCartaDocumento;

public class SancionImpresaEstado extends EstadoSancion {

	private static final long serialVersionUID = 7379694214650956694L;

	public SancionImpresaEstado() {
		super(EEStadoCartaDocumento.IMPRESA);
	}

	@Override
	public Set<EstadoSancion> getEstadosSiguientes() {
		Set<EstadoSancion> estadosSiguientes = new HashSet<EstadoSancion>();
		estadosSiguientes.add(new SancionEnviadaEstado());
		return estadosSiguientes;
	}

	@Override
	public void accept(IEstadoSancionVisitor visitor) {
		visitor.visit(this);
	}

}

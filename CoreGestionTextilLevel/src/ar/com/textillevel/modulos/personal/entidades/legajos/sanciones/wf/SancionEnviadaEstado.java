package ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.wf;

import java.util.HashSet;
import java.util.Set;

import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.EEStadoCartaDocumento;

public class SancionEnviadaEstado extends EstadoSancion {

	private static final long serialVersionUID = 7958195033444381003L;

	public SancionEnviadaEstado() {
		super(EEStadoCartaDocumento.ENVIADA);
	}

	@Override
	public Set<EstadoSancion> getEstadosSiguientes() {
		Set<EstadoSancion> estadosSiguientes = new HashSet<EstadoSancion>();
		estadosSiguientes.add(new SancionRecibidaEstado());
		estadosSiguientes.add(new SancionNoRecibidaEstado());
		return estadosSiguientes;
	}

	@Override
	public void accept(IEstadoSancionVisitor visitor) {
		visitor.visit(this);
	}

}

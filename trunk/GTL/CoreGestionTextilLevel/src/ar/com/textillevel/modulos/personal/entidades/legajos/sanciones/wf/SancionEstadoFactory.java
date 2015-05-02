package ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.wf;

import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.EEStadoCartaDocumento;

public class SancionEstadoFactory {

	private static SancionEstadoFactory instance = new SancionEstadoFactory();
	
	private SancionEstadoFactory() {
	}

	public static SancionEstadoFactory getInstance() {
		return instance;
	}

	@SuppressWarnings("incomplete-switch")
	public EstadoSancion getEstadoSancion(EEStadoCartaDocumento enumEstado) {
		switch (enumEstado) {
		case CREADA:
			return new SancionCreadaEstado();

		case IMPRESA:
			return new SancionImpresaEstado();

		case ENVIADA:
			return new SancionEnviadaEstado();

		case RECIBIDA:
			return new SancionRecibidaEstado();
		
		case JUSTIFICADA:
			return new SancionJustificadaEstado();
		}
		return null;
	}

}

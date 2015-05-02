package ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.wf;

import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.EEStadoValeEnfermedad;

public class ValeAtencionEstadoFactory {

	private static ValeAtencionEstadoFactory instance = new ValeAtencionEstadoFactory();
	
	private ValeAtencionEstadoFactory() {
	}

	public static ValeAtencionEstadoFactory getInstance() {
		return instance;
	}

	public EstadoValeAtencion getEstadoValeAtencion(EEStadoValeEnfermedad enumEstado) {
		switch (enumEstado) {
		case ABIERTO:
			return new ValeAbiertoEstado();

		case JUSTIFICADO_Y_ALTA:
			return new ValeJustificadoAltaEstado();

		case JUSTIFICADO_Y_CONTROL:
			return new ValeJustificadoControlEstado();

		case NO_JUSTIFICADO:
			return new ValeNoJustificadoAltaEstado();
		
		}
		return null;
	}

}

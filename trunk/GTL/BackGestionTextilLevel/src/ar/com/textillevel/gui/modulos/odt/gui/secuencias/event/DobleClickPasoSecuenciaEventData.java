package ar.com.textillevel.gui.modulos.odt.gui.secuencias.event;

import ar.com.textillevel.modulos.odt.entidades.secuencia.generica.PasoSecuencia;

public class DobleClickPasoSecuenciaEventData {

	private PasoSecuencia pasoSecuencia;

	public DobleClickPasoSecuenciaEventData(PasoSecuencia pasoSecuencia) {
		this.pasoSecuencia = pasoSecuencia;
	}

	public PasoSecuencia getPasoSecuencia() {
		return pasoSecuencia;
	}

	public void setPasoSecuencia(PasoSecuencia pasoSecuencia) {
		this.pasoSecuencia = pasoSecuencia;
	}
}

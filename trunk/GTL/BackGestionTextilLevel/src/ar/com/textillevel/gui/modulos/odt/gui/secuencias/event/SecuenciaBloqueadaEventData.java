package ar.com.textillevel.gui.modulos.odt.gui.secuencias.event;

import ar.com.textillevel.modulos.odt.entidades.secuencia.generica.SecuenciaTipoProducto;

public class SecuenciaBloqueadaEventData {

	public SecuenciaTipoProducto secuencia;

	public SecuenciaBloqueadaEventData(SecuenciaTipoProducto secuencia) {
		this.secuencia = secuencia;
	}

	public SecuenciaTipoProducto getSecuencia() {
		return secuencia;
	}

	public void setSecuencia(SecuenciaTipoProducto secuencia) {
		this.secuencia = secuencia;
	}

}

package ar.com.textillevel.gui.modulos.dibujos.cabecera;

import ar.com.textillevel.entidades.ventas.articulos.EEstadoDibujo;

public class ModeloCabeceraDibjuos {

	private Integer nroCliente;
	private EEstadoDibujo estadoDibujo;

	public Integer getNroCliente() {
		return nroCliente;
	}

	public void setNroCliente(Integer nroCliente) {
		this.nroCliente = nroCliente;
	}

	public EEstadoDibujo getEstadoDibujo() {
		return estadoDibujo;
	}

	public void setEstadoDibujo(EEstadoDibujo estadoDibujo) {
		this.estadoDibujo = estadoDibujo;
	}

}

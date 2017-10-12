package ar.com.textillevel.gui.modulos.dibujos.cabecera;

import ar.com.textillevel.entidades.ventas.articulos.EEstadoDibujo;

public class ModeloCabeceraDibjuos {

	private Integer nroCliente;
	private boolean incluir01;
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

	public boolean isIncluir01() {
		return incluir01;
	}

	public void setIncluir01(boolean incluir01) {
		this.incluir01 = incluir01;
	}

}

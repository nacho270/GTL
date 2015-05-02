package ar.com.textillevel.gui.modulos.odt.gui.validacion;

import java.awt.Component;

public abstract class Validacion {

	private Component owner;

	public Validacion(Component owner) {
		this.owner = owner;
	}

	protected Component getOwner() {
		return owner;
	}

	public abstract boolean validate();

}

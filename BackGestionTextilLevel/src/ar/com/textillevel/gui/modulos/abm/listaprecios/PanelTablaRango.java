package ar.com.textillevel.gui.modulos.abm.listaprecios;

import java.awt.Dialog;

import ar.clarin.fwjava.componentes.PanelTabla;

public abstract class PanelTablaRango <T> extends PanelTabla<T>{

	private static final long serialVersionUID = 7325870283327165165L;

	protected Dialog parent;
	
	public PanelTablaRango(Dialog parent) {
		this.parent = parent;
	}
}

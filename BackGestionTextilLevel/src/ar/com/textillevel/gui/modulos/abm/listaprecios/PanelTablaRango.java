package ar.com.textillevel.gui.modulos.abm.listaprecios;

import ar.clarin.fwjava.componentes.PanelTabla;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAncho;

public abstract class PanelTablaRango <T extends RangoAncho> extends PanelTabla<T>{

	private static final long serialVersionUID = 7325870283327165165L;

	public abstract boolean validar();
}

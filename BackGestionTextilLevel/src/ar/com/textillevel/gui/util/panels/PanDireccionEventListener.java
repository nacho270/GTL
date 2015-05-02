package ar.com.textillevel.gui.util.panels;

import java.util.EventListener;

public interface PanDireccionEventListener extends EventListener {

	public abstract void newInfoLocalidadAdded(PanDireccionEvent evt);

	public abstract void newInfoLocalidadSelected(PanDireccionEvent evt);

}

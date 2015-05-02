package ar.com.textillevel.gui.util.panels;

import java.util.EventListener;

public interface PanelSeleccionarElementoEventListener<T> extends EventListener {

	public abstract void elementsSelected(PanelSeleccionarElementoEvent<T> evt);

}

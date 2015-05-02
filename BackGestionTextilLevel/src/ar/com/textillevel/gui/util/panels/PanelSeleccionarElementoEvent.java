package ar.com.textillevel.gui.util.panels;

import java.util.EventObject;
import java.util.List;

public class PanelSeleccionarElementoEvent<T> extends EventObject {

	private static final long serialVersionUID = -5771755194567873015L;

	private List<T> elements;

	public PanelSeleccionarElementoEvent(Object source, List<T> elements) {
		super(source);
		this.elements = elements;
	}

	public List<T> getElements() {
		return elements;
	}

}

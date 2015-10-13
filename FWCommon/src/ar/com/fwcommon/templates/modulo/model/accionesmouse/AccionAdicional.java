package ar.com.fwcommon.templates.modulo.model.accionesmouse;

import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;


public abstract class AccionAdicional<T> {
	
	public AccionAdicional() {
		super();
	}

	protected abstract void update(AccionEvent<T> e);
	
}

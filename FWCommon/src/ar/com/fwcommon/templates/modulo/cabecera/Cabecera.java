package ar.com.fwcommon.templates.modulo.cabecera;

import ar.com.fwcommon.componentes.GuiPanelObservable;

/**
 * Panel base para las cabeceras que utilizan el módulo template
 * 
 * 
 * 
 * 
 * @param MC Modelo de la cabecera que contendrá todos los datos de la misma
 */
public abstract class Cabecera<MC> extends GuiPanelObservable {

	private static final long serialVersionUID = -1534617932718678180L;

	/**
	 * Notifica a todos los observers que se ha producido un cambio en la cabecera.<br>
	 * Debe llamarse a esta función cada vez que se ha producido un cambio en los datos de la cabecera y se desea recargar la tabla utilizando dichos datos
	 */
	protected void notificar() {
		setChanged();
		notifyObservers();
	}

	/**
	 * Devuelve el modelo de la cabecera. Dicho modelo debe contener todos los
	 * datos que se encuentran seleccionados en la misma
	 * 
	 * @return Modelo de la cabecera
	 */
	public abstract MC getModel();
}
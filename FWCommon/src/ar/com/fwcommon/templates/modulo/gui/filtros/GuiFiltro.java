package ar.com.fwcommon.templates.modulo.gui.filtros;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.model.filtros.Filtro;
import ar.com.fwcommon.templates.modulo.model.listeners.FilterChangeEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.FilterChangeListener;
import ar.com.fwcommon.templates.modulo.model.listeners.ModelChangeEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.ModelChangeListener;

/**
 * Clase base para todas las GUI de los filtros
 * 
 * 
 *
 * @param <T> Tipo de datos que va a filtrar
 * @param <U> Elementos que va a utilizar el filtro
 */
public abstract class GuiFiltro<T, U> extends JPanel {

	private static final long serialVersionUID = -3980610008006542606L;
	
	private final ModuloTemplate<T, ?> owner;
	private Filtro<T, U> filtro;

	/**
	 * Crea un filtro.
	 */
	GuiFiltro(ModuloTemplate<T, ?> owner, Filtro<T, U> filtro) {
		super();
		this.owner = owner;
		setFiltro(filtro);
		setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
	}

	/**
	 * Devuelve el Módulo Template al que pertenece esta GUI
	 * @return Modulo Template al que pertenece esta GUI
	 */
	protected ModuloTemplate<T, ?> getOwner() {
		return owner;
	}
	
	/**
	 * Devuelve el modelo de la GUI
	 * @return Modelo de la GUI
	 */
	public Filtro<T, U> getFiltro() {
		return filtro;
	}

	/**
	 * Establece el modelo de la GUI
	 * @param filtro Modelo de la GUI
	 */
	public void setFiltro(Filtro<T, U> filtro) {
		this.filtro = filtro;
		this.filtro.addModelChangeListener(new FiltroModelChangeListener());
	}
	
	/**
	 * Dice si la GUI puede crecer en dirección horizontal
	 * 
	 * @return <code>true</code> si puede crecer, <code>false</code> en caso
	 *         contrario
	 */
	abstract boolean canGrow();
	
	/**
	 * Agrega un listener que notifica cuando se produce un cambio en el valor
	 * del filtro
	 * 
	 * @param listener listener a agregar
	 */
	public void addFilterChangeListener(FilterChangeListener listener) {
		listenerList.add(FilterChangeListener.class, listener);
	}

	/**
	 * Quita un listener que notifica cuando se produce un cambio en el valor
	 * del filtro
	 * 
	 * @param listener listener a quitar
	 */
	public void removeFilterChangeListener(FilterChangeListener listener) {
		listenerList.add(FilterChangeListener.class, listener);
	}

	/**
	 * Notifica a los listeners que se produjo un cambio en el filtro.
	 * @param value Valor que toma el filtro
	 */
	protected void fireFilterChangeListener(U value) {
		this.filtro.setInternalValue(value);
		final FilterChangeEvent e = new FilterChangeEvent(this, value);
		final FilterChangeListener[] listeners = listenerList.getListeners(FilterChangeListener.class);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for (int i = 0; i < listeners.length; i++) {
					listeners[i].filterChange(e);
				}
			}			
		});
	}
	
	/**
	 * Esta función es llamada cuando el modelo cambió y en consecuencia es
	 * necesario sincronizar la información con este. <br>
	 * Esta función debe ser implementada de tal manera de que nos se pierda el
	 * valor actual (en caso que el mismo siga existiendo)
	 */
	protected abstract void refreshData();
	
	/**
	 * Esta función es llamada cuando el modelo cambió el nombre del filtro y en
	 * consecuencia es necesacio actualizar dicho nombre en la gui
	 */
	protected abstract void refreshName();
	
	/**
	 * Establece el valor al filtro
	 * @param value Valor a establecer
	 */
	protected abstract void setValue(U value);
	
	private class FiltroModelChangeListener implements ModelChangeListener {
		public void stateChanged(ModelChangeEvent e) {
			if (e.getSource() == filtro) {
				switch (e.getEventType()) {
				case Filtro.EVENT_TYPE_VALUES_CHANGE:
					refreshData();
					break;
					
				case Filtro.EVENT_TYPE_NAME_CHANGE:
					refreshName();
					break;
					
				case Filtro.EVENT_TYPE_VALUE_CHANGE:
					//No hago nada :-)
					break;
				
				case Filtro.EVENT_TYPE_GUI_VALUE_MUST_UPDATE:
					setValue(getFiltro().getValue());
					break;
				}
			}
		}		
	}
}
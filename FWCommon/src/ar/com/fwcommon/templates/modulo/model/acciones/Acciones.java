package ar.com.fwcommon.templates.modulo.model.acciones;

import javax.swing.SwingUtilities;

import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.fwcommon.templates.modulo.model.meta.GroupModel;
import ar.com.fwcommon.templates.modulo.model.meta.Model;
import ar.com.fwcommon.templates.modulo.model.meta.ModelSet;
import ar.com.fwcommon.templates.modulo.model.meta.SingleModel;

/**
 * Clase que agrupa a todas las acciones
 * 
 * 
 * 
 * @param <T> Tipo de datos sobre el cual son aplicables las acciones
 */
public final class Acciones<T> extends ModelSet<Accion<T>> {
	private static final int DEFAULT_CANTIDAD_FILAS = 1;
	private int cantidadFilas = DEFAULT_CANTIDAD_FILAS;
	
	public Acciones() {
		super();
	}
	
	/**
	 * Devuelve la cantidad de filas que se utilizarán para distribuir las
	 * acciones. El valor default es 1.
	 * 
	 * @return Cantidad de filas en las que se colocarán las acciones
	 */
	public int getCantidadFilas() {
		return cantidadFilas;
	}

	/**
	 * Establece la cantidad de filas en las que se van a distribuir las
	 * acciones.
	 * 
	 * @param cantidadFilas Cantidad de filas en las que se van a colocar las
	 *            acciones
	 * @throws IllegalArgumentException Si cantidadFilas es menor o igual a cero
	 */
	public void setCantidadFilas(int cantidadFilas) {
		if (cantidadFilas < 1) 
			throw new IllegalArgumentException("La cantidad de filas debe ser mayor o igual a uno");
		this.cantidadFilas = cantidadFilas;
	}

	/**
	 * Habilita/Deshabilita las acciones, teniendo en cuenta los datos que se
	 * encuentran seleccionados en la tabla
	 * 
	 * @param e Evento que contiene los elementos seleccionados y el
	 *            {@link ModuloTemplate} que origina el mismo
	 */
	public void habilitarAcciones(final AccionEvent<T> e) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final int total = getElementCount();
				for(int i=0; i<total; i++) {
					Model<Accion<T>> element = getElement(i);
					if (element.isSingleModel()) {
						habilitarAccion(e, ((SingleModel<Accion<T>>)element).getModel());
					} else {
						final GroupModel<Accion<T>> groupElement = (GroupModel<Accion<T>>)element;
						for (Accion<T> accion : groupElement.getModels()) {
							habilitarAccion(e, accion);
						}
					}

				}
			}
		});
	}
	
	private void habilitarAccion(final AccionEvent<T> e, Accion<T> accion) {
		accion.setValida(accion.isIndependienteSeleccion() || accion.esValida(e));
	}
}

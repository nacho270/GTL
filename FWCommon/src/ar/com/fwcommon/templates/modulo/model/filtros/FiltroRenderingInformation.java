package ar.com.fwcommon.templates.modulo.model.filtros;

import java.awt.Dimension;

/**
 * Clase que agrupa toda la información acerca de como debe mostrarse en
 * pantalla un filtro determinado
 * 
 * 
 */
public class FiltroRenderingInformation {
	private int fila;
	private Dimension minimumSize = null;
	private Dimension maximumSize = null;
	private Boolean ajustable = null;
	
	public FiltroRenderingInformation() {
		super();
	}

	/**
	 * Dice si el componente puede variar su tamaño o no
	 * 
	 * @return Si el componente puede variar su tamaño o no. <code>null</code>
	 *         es la opción predeterminada para dicho componente
	 */
	public Boolean isAjustable() {
		return ajustable;
	}


	/**
	 * Establece si el componente puede variar su tamaño o no
	 * 
	 * @param ajustable Si el componente puede variar su tamaño o no. <code>null</code>
	 *         es la opción predeterminada para dicho componente
	 */
	public void setAjustable(Boolean ajustable) {
		this.ajustable = ajustable;
	}

	/**
	 * Devuelve la fila en la que se va a ubicar el filtro
	 * 
	 * @return Fila en la que se va a ubicar el filtro
	 */
	public int getFila() {
		return fila;
	}

	/**
	 * Establece la fila en la que se va a ubicar el filtro
	 * 
	 * @param fila Fila en la que se va a ubicar el filtro
	 */
	public void setFila(int fila) {
		this.fila = fila;
	}

	/**
	 * Devuelve el tamaño mínimo que deberá que tener el componente
	 * 
	 * @return Tamaño mínimo que deberá tener el componente. <code>null</code>
	 *         si no se especifica ningún tamaño
	 */
	public Dimension getMinimumSize() {
		return minimumSize;
	}

	/**
	 * Establece el tamaño mínimo que deberá que tener el componente
	 * 
	 * @param minimumSize Tamaño mínimo que deberá tener el componente. <code>null</code>
	 *         si no se especifica ningún tamaño y se utiliza el default
	 */
	public void setMinimumSize(Dimension minimumSize) {
		this.minimumSize = minimumSize;
	}

	/**
	 * Devuelve el tamaño máximo para el componente
	 * @return Tamaño máximo para el componente
	 */
	public Dimension getMaximumSize() {
		return maximumSize;
	}

	/**
	 * Establece el tamaño máximo para el componente
	 * 
	 * @param maximumSize Tamaño máximo para el componente. <code>null</code>
	 *            si no se especifica ningún tamaño y se utiliza el default
	 */
	public void setMaximumSize(Dimension maximumSize) {
		this.maximumSize = maximumSize;
	}
}

package ar.com.fwcommon.templates.modulo.model.tabla;

import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTableAnalisis;

/**
 * Columna de una tabla
 * 
 * 
 * 
 * @param <T> Tipo de datos que figura en la tabla
 */
public abstract class Columna<T> {
	public static final int TIPO_OBJECT = 0;
	public static final int TIPO_STRING = 1;
	public static final int TIPO_INT = 2;
	public static final int TIPO_DATE = 3;
	public static final int TIPO_FLOAT = 4;
	public static final int TIPO_TIME = 5;
	public static final int TIPO_BOOLEAN = 6;
	
	/**
	 * Indica que la columna se meramente de tipo informativa, y en
	 * consecuencia, no se le aplican operaciones a la misma
	 * 
	 * CLJTableAnalisis#setColumnaInfo(int, int, boolean)
	 */
	public static final int ANALISIS_MODE_INFORMATION = 0;
	
	/**
	 * Indica que la columna se puede agrupar
	 * 
	 * CLJTableAnalisis#setColumnaGrupo(int, int, String)
	 */
	public static final int ANALISIS_MODE_GROUP_BY = 1;
	
	/**
	 * Indica que la columna se encuentra en modo de operación.
	 * 
	 * CLJTableAnalisis#setColumnaDato(int, int, String, boolean)
	 */
	public static final int ANALISIS_MODE_AGGREGATE_FUNCTION = 2;
	
	private EventListenerList listeners = new EventListenerList();
	private int idColumna;
	private String nombre;
	private int tipo;
	
	private int ancho;
	/**
	 * Grupo de variables utilizadas para cuando la tabla esta en modo Analisis.
	 * 
	 * @#Tabla.isAnalisis(); 
	 */
	private boolean visible = true;
	private int analisisMode = ANALISIS_MODE_INFORMATION;
	private String aggregateFunction = FWJTableAnalisis.OPERACION_CONT;
	private int alineacion = FWJTable.LEFT_ALIGN;
	private int alineacionHeader = FWJTable.CENTER_ALIGN;
	
	private boolean multiLine = false;
	
	/**
	 * Crea una Columna según su tipo especificado.
	 * 
	 * <UL>
	 * 	<LI>Columna.TIPO_OBJECT Setea el tipo de dato para la columna un <code>Object</code>
	 * 	<LI>Columna.TIPO_STRING Setea el tipo de dato para la columna un <code>TIPO_STRING</code>
	 *  <LI>Columna.TIPO_INT Setea el tipo de dato para la columna un <code>TIPO_INT</code>
	 *  <LI>Columna.TIPO_DATE Setea el tipo de dato para la columna un <code>TIPO_DATE</code>
	 *  <LI>Columna.TIPO_FLOAT Setea el tipo de dato para la columna un <code>TIPO_FLOAT</code>
	 *  <LI>Columna.TIPO_TIME Setea el tipo de dato para la columna un <code>TIPO_TIME</code>
	 * </UL>
	 *  
	 * @param nombre el nombre de la columna que estará visible en el header de la misma.
	 * @param tipo El valor para el tipo de la columna. 
	 */
	public Columna(String nombre, int tipo) {
		super();
		setNombre(nombre);
		setTipo(tipo);
	}

	/**
	 * Si alguna vez es necesario persistir la columna el id de la misma es
	 * requerido por hibernate.
	 * 
	 * @return El valor para identificar a la columna.
	 */
	public int getIdColumna() {
		return idColumna;
	}

	/**
	 * Establece el valor para identificar unívocamente a la columna.
	 * @param id que identificará a la columna.
	 * #getIdColumna()
	 */
	public void setIdColumna(int id) {
		this.idColumna = id;
	}
	
	/**
	 * Devuelve el nombre para la columna.
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Establece el nombre para la columna.
	 * @param nombre The nombre to set.
	 */
	public void setNombre(String nombre) {
		if (this.nombre != nombre && nombre != null && !nombre.equals(this.nombre)) {
			this.nombre = nombre;
			fireChangeListener();
		}
	}

	/**
	 * Devuelve el tipo de dato o entidad que tiene la columna.
	 * @return el valor que identifica al tipo de datos que tiene asosiado la columna.
	 * #Columna(String, int)
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * Establece el tipo de dato o entidad que tiene la columna.
	 * @param tipo el valor constante para el tipo de datos que tiene asociada la columna.
	 * #Columna(String, int)
	 */
	private void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
	/**
	 * Indica en que modo se encuentra trabajando.  
	 * @return el valor para el modo actual.
	 * #setAnalisisMode(int)
	 */
	public int getAnalisisMode() {
		return analisisMode;
	}

	
	/**
	 * Establece el modo de análisis que tiene la grilla.
	 * <ul>
	 *  <li>{@link #ANALISIS_MODE_INFORMATION}
	 * 	<li>{@link #ANALISIS_MODE_GROUP_BY}
	 * 	<li>{@link #ANALISIS_MODE_AGGREGATE_FUNCTION}
	 * </ul>
	 * 
	 * @param analisisMode
	 * Columna 
	 */
	public void setAnalisisMode(int analisisMode) {
		if (this.analisisMode != analisisMode) {
			this.analisisMode = analisisMode;
			fireChangeListener();
		}
	}
	
	/**
	 * Devuelve el <code>Object</code> correspondiente a la columna.
	 * @param item El tipo de dato o entidad que se encuentra en la tabla. 
	 * @return el valor correspondiente a la columna.
	 */
	public abstract Object getValor(T item);

	/**
	 * Devuelve el ancho de la columna.
	 * @return Returns el valor para el ancho de la columna.
	 */
	public int getAncho() {
		return ancho;
	}

	/**
	 * Establece el ancho de la columna.
	 * @param ancho el valor para el ancho de la columna.
	 */
	public void setAncho(int ancho) {
		if (this.ancho != ancho) {
			this.ancho = ancho;
			fireChangeListener();
		}
	}

	/**
	 * Devuelve el tipo de operación aplicada sobre la columna.
	 * @return Returns el valor para la funcion de agregación utilizada actualmente.
	 * #setAggregateFunction(String)
	 */
	public String getAggregateFunction() {
		return aggregateFunction;
	}

	/**
	 * Establece la operación a aplicar en la columna.
	 * <UL>
	 * 	<LI>CLJTableAnalisis.OPERACION_SUM = "SUM"
	 *  <LI>CLJTableAnalisis.OPERACION_CONT = "CONT"
	 *  <LI>CLJTableAnalisis.OPERACION_PROM = "PROM"
	 *  <LI>CLJTableAnalisis.OPERACION_MIN = "MIN"
	 *  <LI>CLJTableAnalisis.OPERACION_MAX = "MAX"
	 *  <LI>CLJTableAnalisis.OPERACION_VAL = "VAL"
	 *  <LI>CLJTableAnalisis.OPERACION_PORC = "PORC"	  
	 * </UL>
	 * 
	 * @param aggregateFunction el valor para la funcion de agregación utilizada actualmente.
	 * 
	 * CLJTableAnalisis
	 */
	public void setAggregateFunction(String operacion) {
		if (this.aggregateFunction != operacion) {
			this.aggregateFunction = operacion;
			fireChangeListener();
		}
	}

	/**
	 * Devuelve la alineación que tiene la celda en la columna.
	 * @return valor para la alineación de la celda en la grilla.
	 * #setAlineacion(int)
	 */
	public int getAlineacion() {
		return alineacion;
	}

	/**
	 * Establece la alineación que tiene la celda en la columna.
	 * 
	 * <UL>
	 * 	<LI> CLJTable.LEFT_ALIGN
	 *  <LI> CLJTable.CENTER_ALIGN
	 *  <LI> CLJTable.RIGHT_ALIGN
	 * </UL>
	 *  
	 * @param alineacion el valor para la alineación
	 */
	public void setAlineacion(int alineacion) {
		if (this.alineacion != alineacion) {
			this.alineacion = alineacion;
			fireChangeListener();
		}
	}

	/**
	 * Devuelve la alineación para el título de la columna.
	 * @return valor para la alineación del título en la columna.
	 * 
	 * #setAlineacionHeader(int)
	 */
	public int getAlineacionHeader() {
		return alineacionHeader;
	}

	/**
	 * Establece la alineación para el título de la columna.
	 * <UL>
	 * 	<LI> CLJTable.LEFT_ALIGN
	 *  <LI> CLJTable.CENTER_ALIGN
	 *  <LI> CLJTable.RIGHT_ALIGN
	 * </UL>
	 * 
	 * @param alineacionHeader el valor para la alineación del título en la columna.
	 */
	public void setAlineacionHeader(int alineacionHeader) {
		if (this.alineacionHeader != alineacionHeader) {
			this.alineacionHeader = alineacionHeader;
			fireChangeListener();
		}
	}

	/**
	 * Indica si la columna se muestra en la grilla.
	 * 
	 * @return <code>True</code> si muestra la columna, caso contrario
	 *         <code>False</code>
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Establece si la columna se muestra o no en pantalla.
	 * @param visible el valor para setear la visibilidad de la columna
	 */
	public void setVisible(boolean visible) {
		if (this.visible != visible) {
			this.visible = visible;
			fireChangeListener();
		}
	}

	/**
	 * Agrega un listener que notifica cuando cambio la columna
	 * @param listener Listener a agregar
	 */
	public void addChangeListener(ChangeListener listener) {
		listeners.add(ChangeListener.class, listener);
	}
	
	/**
	 * Quita un listener que notifica cuando cambio la columna
	 * @param listener Listener a quitar
	 */
	public void removeChangeListener(ChangeListener listener) {
		listeners.remove(ChangeListener.class, listener);
	}
	
	/**
	 * Avisa que cambió la columna
	 */
	protected void fireChangeListener() {
		final ChangeEvent e = new ChangeEvent(this);
		final ChangeListener[] l = listeners.getListeners(ChangeListener.class);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for (int i = 0; i < l.length; i++) {
					l[i].stateChanged(e);
				}
			}			
		});
	}

	public boolean isMultiline() {
		return this.multiLine;
	}

	/** Permite que las celdas de la grilla sean multiline
	 * @param multiLine
	 */
	public void setMultiline(boolean multiLine) {
		this.multiLine= multiLine;
	}
	
}

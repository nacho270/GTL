package ar.com.fwcommon.templates.modulo.model.tabla;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import ar.com.fwcommon.templates.modulo.model.listeners.TableChangeEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.TableChangeListener;

/**
 * Modelo para la Tabla. Este modelo contiene las columnas que tendrá la tabla ({@link #addColumna(Columna)},
 * {@link #getColumna(int)}), la forma de selección de la misma ({@link #getModoSeleccion()},
 * {@link #setModoSeleccion(int)}).
 * <p>
 * Además, posee manejadores auxiliares que se encargan de diferentes aspectos.
 * Por ejemplo:
 * <ul>
 * <li> De colorear las filas de la tabla se encarga el
 * {@link TablaColorManager} ({@link #setTablaColorManager(TablaColorManager)},
 * {@link #getTablaColorManager()})
 * <li> De persistir el orden de las columas elegido por el usuario se encarga
 * {@link TablaColumnOrderPersister} ({@link #setColumnOrderPersister(TablaColumnOrderPersister)},
 * {@link #getColumnOrderPersister()})
 * </ul>
 * 
 * 
 * 
 * @param <T> Tipo de datos que va a tener la tabla
 */
public class Tabla<T> {
	private EventListenerList listeners = new EventListenerList();
	private List<Columna<T>> columnas;
	private List<T> items; 
	private List<GrupoColumnasModel<T>> gruposColumnas;
	private ColumnaObjeto<T> columnaObjeto = null;
	private TablaColorManager<T> tablaColorManager;
	private TablaColumnOrderPersister columnOrderPersister;
	private TablaColumnVisibilityPersister columnVisibilityPersister;
	private int modoSeleccion = SELECCION_MULTIPLES_FILAS;
	
	/**
	 * Para permitir seleccionar una única fila
	 */
    public static final int SELECCION_FILA = 0;
    /**
     * Para permitir seleccionar múltiples filas
     */
    public static final int SELECCION_MULTIPLES_FILAS = 1;
    
    /**
     * Para permitir seleccionar una única columna
     */
    public static final int SELECCION_COLUMNA = 2;
    /**
	 * Para permitir seleccionar múltiples columnas
	 */
    public static final int SELECCION_MULTIPLES_COLUMNAS = 3;
    
    /**
	 * Para permitir la selección de una única celda
	 */
    public static final int SELECCION_CELDA = 4;

	/**
	 * Indica que la tabla funciona en modo análisis
	 */
	private boolean analisis = false;
	/**
	 * Indica si previo a efectuar el modo análisis debe ordenar los datos  
	 */
	private boolean ordenar = true;
	/**
	 * Indica si al efectuar el modo análisis coloca los totales sumarizados.
	 * <br>
	 * Si es <code>TRUE</code> se agregan las filas de totales. Si es
	 * <code>FALSE</code> solo agrupa los datos.
	 */
	private boolean operar = true;
	
	/**
	 * Construye una tabla
	 */
	public Tabla() {
		super();
	}

	public Tabla(boolean analisis) {
		super();
		setAnalisis(analisis);
	}
	
	/**
	 * Devuelve la lista con las columnas
	 * @return Returns the columnas.
	 */
	//private List<Columna<T>> getColumnas() {
	public List<Columna<T>> getColumnas() {
		if (columnas == null) {
			columnas = new ArrayList<Columna<T>>();
			fixColumnaObjeto();
		}
		return columnas;
	}

	/**
	 * Devuelve la cantidad de columnas
	 * @return Cantidad de columnas
	 */
	public synchronized int getColumnasCount() {
		return getColumnas().size();
	}
	
	/**
	 * Devuelve una columna determinada
	 * @param index indice de la columna
	 * @return Columna
	 * @throws IndexOutOfBoundsException Si el índice se encuentra fuera de rango
	 */
	public synchronized Columna<T> getColumna(int index) {
		return getColumnas().get(index);
	}

	/**
	 * Devuelve la columna cuyo id es <code>id</code>
	 * @param id El id de la columna a devolver
	 * @return La columna cuyo id es <code>id</code>
	 * @throws IllegalArgumentException Si el id no es válido
	 */
	public synchronized Columna<T> getColumnaById(int id) {
		for(Columna<T> columna: getColumnas()) {
			if(columna.getIdColumna() == id) {
				return columna;
			}
		}
		throw new IllegalArgumentException("No existe una columna con id " + id);
	}

	/**
	 * Devuelve <code>true</code> si la tabla contiene la columna cuyo id es <code>id</code> 
	 * @param id El id de la columna a comprobar su existencia
	 * @return <code>true</code> si la tabla contiene la columna cuyo id es <code>id</code>
	 */
	public synchronized boolean containsColumna(int id) {
		for(Columna<T> columna: getColumnas()) {
			if(columna.getIdColumna() == id) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Agrega una columna a la tabla
	 * @param columna Columna que se quiere agregar
	 */
	public synchronized void addColumna(Columna<T> columna) {
		if (needsColumnaObjeto()) {
			getColumnas().add(getColumnas().size()-1, columna);
		} else {
			getColumnas().add(columna);
		}
		columna.addChangeListener(getColumnChangeListener());
		fireTableStructureChangeListener(TableChangeEvent.TYPE_COLUMN_CHANGE);
	}
	
	/**
	 * Quita una columna de la tabla
	 * @param columna Columna que se desea quitar
	 * @throws IllegalArgumentException si es una columna especial que no puede quitarse
	 */
	public synchronized void removeColumna(Columna<T> columna) {
		if (columna == getColumnaObjeto()) throw new IllegalArgumentException("No se puede quitar la columna objeto");
		if (getColumnas().remove(columna)) {
			columna.removeChangeListener(getColumnChangeListener());
			fireTableStructureChangeListener(TableChangeEvent.TYPE_COLUMN_CHANGE);
		}
	}

	public void agregarGrupoColumnas(String titulo,Columna<T> columnaDesde, Columna<T> columnaHasta){
		getGruposColumnas().add(new GrupoColumnasModel<T>(titulo,columnaDesde,columnaHasta ));
		fireTableStructureChangeListener(TableChangeEvent.TYPE_COLUMN_GROUP_CHANGE);
	}

	public List<GrupoColumnasModel<T>> getGruposColumnas() {
		if (gruposColumnas==null){
			gruposColumnas = new ArrayList<GrupoColumnasModel<T>>();
		}
		return gruposColumnas;
	}
	
	/**
	 * Devuelve una lista con los datos de la tabla<br>
	 * 
	 * @return Lista inmutable con los datos de la tabla
	 */
	public List<T> getItems() {
		if (items == null) {
			items = Collections.emptyList();
		}
		return items;
	}

	/**
	 * Establece los datos de la tabla
	 * @param items Datos de la tabla
	 */
	public void setItems(List<T> items) {
		this.items = Collections.unmodifiableList(new ArrayList<T>(items));
		fireTableDataChangeListener(null);
	}

	/**
	 * Establece los datos de la tabla
	 * 
	 * @param items Datos de la tabla
	 * @param selectedItems Elementos que deben quedar seleccionados.
	 *            <code>null</code> si se desea mantener la selección actual
	 */
	public void setItems(List<T> items, List<T> selectedItems) {
		this.items = Collections.unmodifiableList(new ArrayList<T>(items));
		fireTableDataChangeListener(selectedItems);
	}

	/**
	 * Toma un <code>item</code> y devuelve el contenido para cada columna
	 * 
	 * @param item Item del que se desea obtener la información para las
	 *            columnas
	 * @param permutacion Permutación actual de las columnas
	 * @return Array con la información para las columnas
	 */
	public Object[] getFilaItem(T item, final int[] permutacion) {
		if (permutacion == null || getColumnas().size() != permutacion.length)
			throw new IllegalArgumentException("La permutación de columnas no puede ser null y debe tener la misma longitud que la cantidad de columnas");
		Object[] fila = new Object[getColumnas().size()];
		int col = 0;
    	for (Columna<T> columna : getColumnas()) {
    		fila[permutacion[col]] = columna.getValor(item);
    		col++;
    	}
		return fila;
	}
	
	/**
	 * Dice si la tabla funciona en modo análisis
	 * @return <code>true</code> si está en modo análisis. <code>false</code> en caso contrario.
	 */
	public boolean isAnalisis() {
		return analisis;
	}

	/**
	 * Establece si la tabla funciona en modo análisis
	 * @param analisis <code>true</code> si está en modo análisis. <code>false</code> en caso contrario.
	 */
	public void setAnalisis(boolean analisis) {
		if (this.analisis != analisis) {
			this.analisis = analisis;
			fixColumnaObjeto();
			fireTableStructureChangeListener(TableChangeEvent.TYPE_ANALISIS_CHANGE);
		}
	}
	
	/**
	 * Indica si previo a efectuar el modo análisis debe ordenar los datos<br>
	 * 
	 * @return <code>true</code> si agrega las filas sumarizadas. <code>false</code> si solo agrupa los datos.
	 */
	public boolean isOperar() {
		return operar;
	}

	/**
	 * Establece si previo a efectuar el modo análisis debe ordenar los datos
	 * 
	 * @param operar <code>TRUE</code> si agrega las filas sumarizadas.
	 *            <code>FALSE</code> si solo agrupa los datos.
	 */
	public void setOperar(boolean operar) {
		if (this.operar != operar) {
			this.operar = operar;
			fixColumnaObjeto();
			fireTableStructureChangeListener(TableChangeEvent.TYPE_ANALISIS_CHANGE);
		}
	}

	/**
	 * Indica si previo a efectuar el modo análisis debe ordenar los datos
	 * 
	 * @return <code>TRUE</code> si ordena antes de efectuar la agupación.
	 *         <code>FALSE</code> en caso contrario.
	 */
	public boolean isOrdenar() {
		return ordenar;
	}

	/**
	 * Establece si previo a efectuar el modo análisis debe ordenar los datos
	 * 
	 * @param ordenar <code>TRUE</code> si ordena antes de efectuar la
	 *            agupación. <code>FALSE</code> en caso contrario.
	 */
	public void setOrdenar(boolean ordenar) {
		if (this.ordenar != ordenar) {
			this.ordenar = ordenar;
			fixColumnaObjeto();
			fireTableStructureChangeListener(TableChangeEvent.TYPE_ANALISIS_CHANGE);
		}
	}

	/**
	 * Verifica que si se necesita una columna objeto, que la misma esté
	 * presente y que si no se necesita que no exista
	 * #needsColumnaObjeto()
	 * #getColumnaObjeto()
	 */
	private synchronized void fixColumnaObjeto() {
		if (needsColumnaObjeto() && getColumnaObjeto() == null) {
			setColumnaObjeto(new ColumnaObjeto<T>());
			getColumnas().add(getColumnaObjeto());
		} else if (!needsColumnaObjeto() && getColumnaObjeto() != null) {
			getColumnas().remove(getColumnaObjeto());
			setColumnaObjeto(null);
		}
	}
	
	/**
	 * Devuelve el índice de la columna en el modelo
	 * 
	 * @param columna Columna de la que se quiere obtener el índice
	 * @return Indice de la columna. <code>-1</code> si la columna no existe
	 */
	public int getColumnaIndex(Columna<T> columna) {
		if (columna == null) return -1;
		return getColumnas().indexOf(columna);
	}
	
	/**
	 * Devuelve el índice de la columna que contiene el objeto de negocio
	 * 
	 * @return Indice de la columna que contiene el objeto de negocio.
	 *         <code>-1</code> si no existe dicha columna
	 */
	public int getColumnaObjetoIndex() {
		return getColumnaIndex(getColumnaObjeto());
	}

	/**
	 * Devuelve la columna que contiene los datos del objeto
	 * 
	 * @return Columna que contiene la entidad en cuestión. <code>null</code>
	 *         si no existe tal columna
	 * #needsColumnaObjeto()
	 */
	private ColumnaObjeto<T> getColumnaObjeto() {
		return columnaObjeto;
	}

	/**
	 * Establece la columna que se va a utilizar para guardar los elementos de
	 * negocio
	 * 
	 * @param columnaObjeto Columna que tendrá los elementos de negocio
	 * #needsColumnaObjeto()
	 */
	private void setColumnaObjeto(ColumnaObjeto<T> columnaObjeto) {
		this.columnaObjeto = columnaObjeto;
		if (columnaObjeto != null)
			this.columnaObjeto.setVisible(false);
	}

	/**
	 * En función de los parámetros de análisis, dice si el modelo debe
	 * incorporar o no una columna especial para guardar los objetos
	 * 
	 * @return <code>true</code> si se debe utilizar una columna para guardar
	 *         los objetos y <code>false</code> en caso contrario
	 */
	private boolean needsColumnaObjeto() {
		return (!isAnalisis() || (isAnalisis() && !isOperar()));
	}

	/**
	 * Devuelve el modo de selección de la tabla.
	 * <p>
	 * Los modos de selección pueden ser:
	 * 
	 * <ul>
	 * <li> <code>Tabla.SELECCION_FILA</code> : Para permitir seleccionar una
	 * única fila
	 * <li> <code>Tabla.SELECCION_MULTIPLES_FILAS</code> : Para permitir
	 * seleccionar múltiples filas
	 * <li> <code>Tabla.SELECCION_COLUMNA</code> : Para permitir seleccionar
	 * una única columna
	 * <li> <code>Tabla.SELECCION_MULTIPLES_COLUMNAS</code> : Para permitir
	 * seleccionar múltiples columnas
	 * <li> <code>Tabla.SELECCION_CELDA</code> : Para permitir la selección de
	 * una única celda
	 * </ul>
	 * 
	 * @return Modo de selección de la tabla
	 */
	public int getModoSeleccion() {
		return modoSeleccion;
	}

	/**
	 * Establece el modo de selección de la tabla.
	 * <p>
	 * Los modos de selección pueden ser:
	 * 
	 * <ul>
	 * <li> <code>Tabla.SELECCION_FILA</code> : Para permitir seleccionar una
	 * única fila
	 * <li> <code>Tabla.SELECCION_MULTIPLES_FILAS</code> : Para permitir
	 * seleccionar múltiples filas
	 * <li> <code>Tabla.SELECCION_COLUMNA</code> : Para permitir seleccionar
	 * una única columna
	 * <li> <code>Tabla.SELECCION_MULTIPLES_COLUMNAS</code> : Para permitir
	 * seleccionar múltiples columnas
	 * <li> <code>Tabla.SELECCION_CELDA</code> : Para permitir la selección de
	 * una única celda
	 * </ul>
	 * 
	 * @param modoSeleccion Modo de selección de la tabla
	 */
	public void setModoSeleccion(int modoSeleccion) {
		this.modoSeleccion = modoSeleccion;
		fireTableStructureChangeListener(TableChangeEvent.TYPE_SELECTION_MODE_CHANGE);
	}
   

	/**
	 * Devuelve el manejador de colores de fondo de la tabla
	 * 
	 * @return Manejador de colores de fondo
	 */
	public TablaColorManager<T> getTablaColorManager() {
		if (tablaColorManager == null) {
			tablaColorManager = new NullTableColorManager<T>();
		}
		return tablaColorManager;
	}

	/**
	 * Establece el manejador de colores de fondo de la tabla
	 * 
	 * @param tablaColorManager Manejador de colores de fondo
	 */
	public void setTablaColorManager(TablaColorManager<T> tablaColorManager) {
		this.tablaColorManager = tablaColorManager;
	}
	

	/**
	 * Devuelve el encargado de manejar la persistencia del orden de las
	 * columnas
	 * 
	 * @return Encargado de manejar la persistencia del orden de las columnas
	 */
	public TablaColumnOrderPersister getColumnOrderPersister() {
		if (columnOrderPersister == null) {
			columnOrderPersister = new NullTableColumnOrderPersister();
		}
		return columnOrderPersister;
	}

	/**
	 * Establece el encargado de manejar la persistencia del orden de las
	 * columnas
	 * 
	 * @param columnOrderPersister Encargado de manejar la persistencia del orden de las columnas
	 */
	public void setColumnOrderPersister(
			TablaColumnOrderPersister columnOrderPersister) {
		this.columnOrderPersister = columnOrderPersister;
	}
	
	/**
	 * Listener que se utiliza para ver si alguna de las columnas cambió sus propiedades
	 */
	private ChangeListener columnChangeListener;
	private ChangeListener getColumnChangeListener() {
		if (columnChangeListener == null) {
			columnChangeListener = new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					fireTableStructureChangeListener(TableChangeEvent.TYPE_COLUMN_CHANGE);					
				}				
			};
		}
		return columnChangeListener;
	}
	
	/**
	 * Agrega un listener que notifica cuando cambió la tabla
	 * @param listener Listener a agregar
	 */
	public void addTableChangeListener(TableChangeListener listener) {
		listeners.add(TableChangeListener.class, listener);
	}
	
	/**
	 * Quita un listener que notifica cuando cambió la tabla
	 * @param listener Listener a quitar
	 */
	public void removeTableChangeListener(TableChangeListener listener) {
		listeners.remove(TableChangeListener.class, listener);
	}
	
	/**
	 * Avisa que cambió la información de la tabla
	 * 
	 * @param Elementos que deben quedar seleccionados. <code>null</code> si
	 *            se desea mantener la selección actual
	 */
	protected void fireTableDataChangeListener(List<T> selectedObjects) {
		final TableChangeEvent e = new TableChangeEvent(this, TableChangeEvent.TYPE_DATA_CHANGE, selectedObjects);
		final TableChangeListener[] l = listeners.getListeners(TableChangeListener.class);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for (int i = 0; i < l.length; i++) {
					l[i].dataChange(e);
				}
			}			
		});
	}
	
	/**
	 * Avisa que cambió la estructura de la tabla.<br>
	 * Recibe el tipo de evento a disparar (ver
	 * {@link TableChangeEvent#getEventType()}
	 * 
	 * @param eventType Tipo de evento.
	 */
	protected void fireTableStructureChangeListener(int eventType) {
		final TableChangeEvent e = new TableChangeEvent(this, eventType);
		final TableChangeListener[] l = listeners.getListeners(TableChangeListener.class);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for (int i = 0; i < l.length; i++) {
					l[i].structureChange(e);
				}
			}			
		});
	}

	/**
	 * Manejador de fondo default. Esto permite evitar el IF de cuando el
	 * manejador es null
	 * 
	 * 
	 * 
	 * @param <T>
	 */
	private static class NullTableColorManager<T> implements TablaColorManager<T> {
		public Color getBackgroundColor(T element) {
			return null;
		}		
	}

	/**
	 * Persistidor de orden de columnas default. Esto permite evitar el IF de
	 * cuando el manejador es null
	 * 
	 * 
	 * 
	 * @param <T>
	 */
	private static class NullTableColumnOrderPersister implements TablaColumnOrderPersister {
		public int[] loadColumnPermutation() {
			return null;
		}

		public void saveColumnPermutation(int[] columnPermutation) {}
	}
	

	/**
	 * Persistidor de orden de columnas ocultas. Esto permite evitar el IF de
	 * cuando el manejador es null
	 * 
	 * @param <T>
	 */

	private static class NullTableColumnVisibilityPersister implements TablaColumnVisibilityPersister  {

		public int[] loadColumnHidden() {
			return null;
		}
		public void saveColumnHidden(int[] columnHidden) {}
	}
	
	
	/**
	 * Devuelve el encargado de manejar la persistencia de las
	 * columnas ocultas
	 * 
	 * @return Encargado de manejar la persistencia las columnas ocultas
	 */
	public TablaColumnVisibilityPersister getColumnVisibilityPersister() {
		if (columnVisibilityPersister  == null) {
			columnVisibilityPersister = new NullTableColumnVisibilityPersister();
		}
		return columnVisibilityPersister;
	}
	
	/**
	 * Establece el encargado de manejar la persistencia de las
	 * columnas ocultas
	 * 
	 * @param columnVisibilityPersister Encargado de manejar la persistencia de las columnas ocultas
	 */
	public void setColumnVisibilityPersister(
			TablaColumnVisibilityPersister columnVisibilityPersister) {
		this.columnVisibilityPersister = columnVisibilityPersister;
	}
	
}
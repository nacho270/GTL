package ar.com.fwcommon.templates.modulo.gui.tabla;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTableAnalisis;
import ar.com.fwcommon.entidades.EqualsTabla;
import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.model.listeners.TableChangeEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.TableChangeListener;
import ar.com.fwcommon.templates.modulo.model.tabla.Columna;
import ar.com.fwcommon.templates.modulo.model.tabla.GrupoColumnasModel;
import ar.com.fwcommon.templates.modulo.model.tabla.Tabla;
import ar.com.fwcommon.templates.modulo.model.tabla.TablaColorManager;

/**
 * GUI que contiene la tabla
 * 
 * 
 * 
 *
 * @param <T> Elementos de la tabla
 */
@SuppressWarnings("serial")
public class GuiTabla<T> extends JPanel implements IGuiTabla<T> {

	private final ModuloTemplate<T, ?> owner;
	private TableChangeListener tableListener;
	private FWJTableAnalisis jTable;
	private Tabla<T> model;
	private int[] columnPermutation;
	private int[] inversePermutation;
	private int[] columnHidden;
	
	public GuiTabla(ModuloTemplate<T, ?> owner, Tabla<T> model) {
		super();
		this.owner = owner;
		createCLJTable();
		setModel(model);
		construct();
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
	 * @return Modelo de la Tabla
	 */
	public Tabla<T> getModel() {
		return model;
	}

	/**
	 * Establece el modelo de la GUI
	 * @param model Modelo de la tabla
	 */
	public void setModel(Tabla<T> model) {
		if (this.model != model) {
			if (this.model != null) this.model.removeTableChangeListener(getTableListener());
			if (model != null) model.addTableChangeListener(getTableListener());
			
			this.model = model;
			actualizarEstructuraColumnas();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.tabla.IGuiTabla#getComponent()
	 */
	public Component getComponent() {
		return this;
	}
	
	private void construct() {
		setLayout(new BorderLayout());
		JScrollPane panTabla = new JScrollPane(getJTable(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panTabla.addMouseListener(new PanTablaMouseListener());
		add(panTabla);
	}
	
	private void createCLJTable() {
		jTable = new FWJTableAnalisis() {
			@Override
			public void hideColumns() {
				super.hideColumns();
				if(getColumnChooser().isOkPressed()) {
					int[] objArray = new int[getIndicesColumnasOcultas().length];
					for(int i = 0; i < getIndicesColumnasOcultas().length; i++) {
						objArray[i] = columnPermutation[getIndicesColumnasOcultas()[i]];
					}
					columnHidden = new int[getIndicesColumnasOcultas().length];
					for(int i = 0; i < getIndicesColumnasOcultas().length; i++) {
						columnHidden[i] = columnPermutation[getIndicesColumnasOcultas()[i]];
					}
					GuiTabla.this.getModel().getColumnVisibilityPersister().saveColumnHidden(objArray);
				}
			}
		};
		columnHidden = new int[0];
		columnPermutation = new int[0];
		inversePermutation = new int[0];
		jTable.setAllowSorting(true);
		jTable.setAllowHidingColumns(true);
		jTable.setReorderingAllowed(true);
		jTable.setPermiteCopiar(true);
		jTable.setHeaderFontStyle(Font.BOLD);
		jTable.permiteHeaderMultiple(true);

		jTable.getColumnModel().addColumnModelListener(new TableColumnModelListener() {
			public void columnAdded(TableColumnModelEvent e) {}
			public void columnMarginChanged(ChangeEvent e) {}
			public void columnRemoved(TableColumnModelEvent e) {}
			public void columnSelectionChanged(ListSelectionEvent e) {}
			
			public void columnMoved(TableColumnModelEvent e) {
				if (e.getFromIndex() != e.getToIndex()) {
					final int col = columnPermutation[e.getFromIndex()]; 
					if (e.getFromIndex() < e.getToIndex()) {
						for (int i=e.getFromIndex(); i<e.getToIndex(); i++)
							columnPermutation[i] = columnPermutation[i+1];
					} else if (e.getFromIndex() > e.getToIndex()) {
						for (int i=e.getFromIndex(); i>e.getToIndex(); i--)
							columnPermutation[i] = columnPermutation[i-1];					
					}
					columnPermutation[e.getToIndex()] = col;
					inversePermutation = inversePermutation(columnPermutation);
					getModel().getColumnOrderPersister().saveColumnPermutation(columnPermutation);
				}
			}
		});
	}
	
	/**
	 * Agrega una columna a la tabla
	 * @param columnIndex índice de la columna a agregar
	 * @param columna Columna a agregar
	 */
	private void addColumna(int columnIndex, Columna<T> columna) {
		if(columna.getTipo() == Columna.TIPO_STRING) {
			if (columna.isMultiline()){
				getJTable().addMultilineColumn (columna.getNombre(), columna.getAncho());
			}else{
				getJTable().addStringColumn(columna.getNombre(), columna.getAncho());	
			}
		} else if(columna.getTipo() == Columna.TIPO_INT) {
			getJTable().addIntColumn(columna.getNombre(), columna.getAncho());
		} else if(columna.getTipo() == Columna.TIPO_FLOAT) {
			getJTable().addFloatColumn(columna.getNombre(), columna.getAncho());
			getJTable().setFloatFormatDefault(columnIndex);
		} else if(columna.getTipo() == Columna.TIPO_DATE) {
			getJTable().addDateColumn(columna.getNombre(), columna.getAncho(), true);
		} else if(columna.getTipo() == Columna.TIPO_TIME) {
			getJTable().addTimeColumn(columna.getNombre(), columna.getAncho(), true);
		} else if(columna.getTipo() == Columna.TIPO_BOOLEAN) {
			getJTable().addCheckColumn(columna.getNombre(), columna.getAncho(), true);			
		} else { // Columna objeto
			getJTable().addStringColumn(columna.getNombre(), columna.getAncho());
		}
		if(columnPermutation[columnIndex] == getModel().getColumnaObjetoIndex()) {
				getJTable().ocultarColumna(columnIndex, true);
		}
	}

	/**
	 * Agrega una fila de datos
	 * 
	 * @param fila Array de objetos con los datos de la fila. Debe tener el
	 *            tamaño de la cantidad de columnas.
	 * @param colorBG Color de fondo de la fila. <code>null</code> si se
	 *            utiliza el por defecto
	 */
	private void addFila(Object[] fila, Color colorBG) {
		if (fila == null || fila.length != getModel().getColumnasCount())
			throw new IllegalArgumentException("Tamaño del vector de datos de la fila inválido");
		
		getJTable().addRow(fila);
		if (colorBG != null) {
			int nroFila = getJTable().getRowCount() - 1;
			getJTable().setBackgroundRow(nroFila, colorBG);
		}
	}
	
	/**
	 * Coloca a la tabla el modo de selección correspondiente. Los valores
	 * posibles son:
	 * <li> <code>Tabla.SELECCION_FILA</code>
	 * <li> <code>Tabla.SELECCION_MULTIPLES_FILAS</code>
	 * <li> <code>Tabla.SELECCION_COLUMNA</code>
	 * <li> <code>Tabla.SELECCION_MULTIPLES_COLUMNAS</code>
	 * <li> <code>Tabla.SELECCION_CELDA</code>
	 * 
	 * @param modoSeleccion Modo de selección a establecer
	 * @throws IllegalArgumentException Si el modo de selección no es ninguno de
	 *             los mencionados anteriormente
	 */
	private void setModoSeleccion(int modoSeleccion) {
		switch (modoSeleccion) {
		case Tabla.SELECCION_FILA:
			getJTable().setColumnSelectionAllowed(false);
			getJTable().setRowSelectionAllowed(true);
			getJTable().setSelectionMode(FWJTable.SINGLE_SELECTION);
			break;
		case Tabla.SELECCION_MULTIPLES_FILAS:
			getJTable().setColumnSelectionAllowed(false);
			getJTable().setRowSelectionAllowed(true);
			getJTable().setSelectionMode(FWJTable.MULTIPLE_INTERVAL_SELECTION);
			break;
		case Tabla.SELECCION_COLUMNA:
			getJTable().setColumnSelectionAllowed(true);
			getJTable().setRowSelectionAllowed(false);
			getJTable().setSelectionMode(FWJTable.SINGLE_SELECTION);
			break;
		case Tabla.SELECCION_MULTIPLES_COLUMNAS:
			getJTable().setColumnSelectionAllowed(true);
			getJTable().setRowSelectionAllowed(false);
			getJTable().setSelectionMode(FWJTable.MULTIPLE_INTERVAL_SELECTION);
			break;
		case Tabla.SELECCION_CELDA:
			getJTable().setColumnSelectionAllowed(true);
			getJTable().setRowSelectionAllowed(true);
			getJTable().setSelectionMode(FWJTable.SINGLE_SELECTION);
			break;
		default:
			throw new IllegalArgumentException("Modo de selección " + modoSeleccion + " no válido");
		}
	}

	/**
	 * Devuelve el componente de la tabla utilizado para mostrar los datos.
	 * <p>
	 * <u><b>IMPORTANTE:</b></u><br>
	 * El componente puede utilizarse para consultar información, agregar
	 * listeners, etc., pero bajo <u>ninguna circunstancia</u> se debe
	 * modificar el estado del mismo (ya sean datos y/o propiedades del mismo). <br>
	 * Lo único que se permite modificar es la selección de la misma
	 * 
	 * @return Componente de la tabla
	 */
	public FWJTableAnalisis getJTable() {
		return jTable;
	}

	/**
	 * Dice si hau alguna fila seleccionada o no
	 * 
	 * @return <code>true</code> si hay al menos una fila seleccionada.
	 *         <code>false</code> en caso contrario
	 */
	public boolean hayFilaSeleccionada() {
		boolean seleccionada = false;
		if(getJTable().getSelectedRow() >= 0) {
			seleccionada = true;
		}
		return seleccionada;
	}

	/**
	 * Borra todos los datos de la tabla
	 */
	protected void inicializarTabla() {
		getJTable().removeAllRows();
	}


	/**
	 * Devuelve el objeto que se encuentra en una fila determinada
	 * @param fila Fila en la que se encuentra el objeto
	 * @return Objeto correspondiente a dicha fila
	 */
	@SuppressWarnings("unchecked")
	public T getObjeto(int fila) {
		T objeto = (T)getJTable().getValueAt(fila, inversePermutation[getModel().getColumnaObjetoIndex()]);
		return objeto;
	}

	/**
	 * Devuelve el item seleccionado en la tabla.
	 * @return El item seleccionado.
	 */
	public T getObjetoSeleccionado() {
		int[] filasSeleccionada = getJTable().getSelectedRows();
		return (filasSeleccionada.length > 0) ? getObjeto(filasSeleccionada[0]) : null;
	}

	/**
	 * Devuelve una lista con los items seleccionados en la tabla.
	 * @return itemsTabla La lista de items seleccionados.
	 */
	public List<T> getObjetosSeleccionados() {
		List<T> objetos;
		if(getModel().getModoSeleccion() == Tabla.SELECCION_COLUMNA || getModel().getModoSeleccion() == Tabla.SELECCION_MULTIPLES_COLUMNAS) {
			objetos = getModel().getItems();
			
		} else {
			int[] filasSeleccionadas = getJTable().getSelectedRows();
			objetos = new ArrayList<T>();
			if(filasSeleccionadas.length > 0) {
				for(int i = 0; i < filasSeleccionadas.length; i++)
					objetos.add(getObjeto(filasSeleccionadas[i]));
			}
		}
		return objetos;
	}

	/**
	 * Actualiza los valores de la tabla
	 * 
	 * @param selectedItems Elementos que deben quedar seleccionados.
	 *            <code>null</code> si se desea el comportamiento default
	 */
	protected void actualizarValores(final List<T> selectedItems) {
		if (selectedItems == null && model.getColumnaObjetoIndex() != -1) {
			getJTable().guardarSeleccion(getJTable().getIndiceActual(model.getColumnaObjetoIndex()));
		}
		if (getJTable().isModoAnalisis()) {
			getJTable().setModoAnalisis(false);
		}
		inicializarTabla();		
		final Tabla<T> model = getModel();
		final TablaColorManager<T> colorManager = model.getTablaColorManager();
		final List<T> items = model.getItems();
		for(T item : items) {
			Object[] fila = model.getFilaItem(item, inversePermutation);
			addFila(fila, colorManager.getBackgroundColor(item));
		}
		if (model.getColumnaObjetoIndex() != -1) {
			if (selectedItems == null) {
				getJTable().restaurarSeleccion();
			} else {
				seleccionarItems(selectedItems);
			}
		}
		recuperarOrdenamiento();
		setTableAnalisis();
		
	}

	
	/**
	 * Recupera el ordenamiento de las filas de la tabla que contiene el
	 * listado de items.
	 */
	protected void recuperarOrdenamiento() {
		int indiceCol = getJTable().getLastSortedCol();
		if(indiceCol != -1) {
			getJTable().sortAllRowsBy(indiceCol, getJTable().sortColsAscending());
		}
	}

	/**
	 * Marca en la tabla los elementos seleccionados
	 * @param selectedItems Elementos a marcar como seleccionados
	 */
	@SuppressWarnings("unchecked")
	protected void seleccionarItems(List<T> selectedItems) {
		Set<T> selectedItemsSet = new HashSet<T>(selectedItems);
		getJTable().clearSelection();
		for (int i=0; i<getJTable().getRowCount(); i++) {
			boolean seleccionar = false ;
			// if (getObjeto(i).getClass().isAssignableFrom(EqualsTabla.class)) {
			if (getObjeto(i) instanceof EqualsTabla) {
				for (T t : selectedItems) {
					EqualsTabla<T> tt = (EqualsTabla<T>) t ;
					if (tt.equalsTabla(getObjeto(i))) {
						seleccionar = true ;
					}
				}
			} else {
				seleccionar = selectedItemsSet.contains(getObjeto(i)) ;
			}
			if (seleccionar) {
				getJTable().addRowSelectionInterval(i, i);
			}
		}
	}
	/**
	 * Limpia la esctructura de la tabla y la vuelve a crear
	 */
	private void actualizarEstructuraColumnas() {
		//inicializarTabla();
		getJTable().setModoAnalisis(false);
		getJTable().removeAllRows();
		getJTable().removeAllColumns();
		getJTable().limpiarTiposDato();
		setModoSeleccion(getModel().getModoSeleccion());
		
		initializeColumnPermutation();
		initializeColumnHidden();
		for(int i=0; i<getModel().getColumnasCount(); i++) {
			Columna<T> columna = getModel().getColumna(columnPermutation[i]);
			if (!isColumnVisibility(columnPermutation[i])) {
				columna.setVisible(isColumnVisibility(columnPermutation[i]));
			}	
			addColumna(i, columna);
			switch (columna.getAnalisisMode()) {
			case Columna.ANALISIS_MODE_INFORMATION:
				//Columnas sobre las que no se hace ninguna operacion
				getJTable().setColumnaInfo(i, i, true);
				break;

//			case Columna.ANALISIS_MODE_JOINEABLE:
//				//Columnas sobre las que no se hace ninguna operacion
//				//pero que en caso que tengan el mismo valor, de juntan
//				getJTable().agruparColumna(i);
//				break;
				
			case Columna.ANALISIS_MODE_AGGREGATE_FUNCTION:
				//Columnas sobre las que se efectua una operación de totalización
				getJTable().setColumnaDato(i, i, columna.getAggregateFunction(), false);
				break;

			case Columna.ANALISIS_MODE_GROUP_BY:
				//Columnas de agrupamiento
				getJTable().setColumnaGrupo(i, i, columna.getAggregateFunction());
				break;

			default:
				throw new IllegalArgumentException(
						"Valor desconocido de Modo Análisis ("
								+ columna.getAnalisisMode()
								+ ") para la columna " + columna.toString());
			}

			getJTable().setAlignment(i, columna.getAlineacion());
			getJTable().setHeaderAlignment(i, columna.getAlineacionHeader());
			getJTable().showColumn(i, columna.isVisible());
		}
		for(GrupoColumnasModel<T> grupo : getModel().getGruposColumnas()) {
			int desde = getModel().getColumnaIndex(grupo.getColumnaDesde());
			int hasta = getModel().getColumnaIndex(grupo.getColumnaHasta());
			if (desde != -1 && hasta != -1) {
				getJTable().agregarGrupoColumnas(grupo.getNombre(), inversePermutation[desde], inversePermutation[hasta]);
			} else {
				throw new RuntimeException("No se encontaron las columnas del grupo: " + grupo.getNombre());
			}
		}
		setTableAnalisis();
	}

	private boolean isColumnVisibility(int columna) {
		if(columnHidden != null && columnHidden.length>0){
			for(int i=0; i < columnHidden.length; i++) {
				if (columnHidden[i] == columna) {
					return false;
				}
			}
		}
		return true;
	}

	private void initializeColumnHidden() {
		columnHidden = getModel().getColumnVisibilityPersister().loadColumnHidden();
	}
	
	/**
	 * Inicializa el vector de permutación de columnas
	 */
	private void initializeColumnPermutation() {
		columnPermutation = getModel().getColumnOrderPersister().loadColumnPermutation();

		/*
		 * Si no tengo una permutación de columnas o la que tengo no coincide
		 * con la cantidad de columnas, la inicializo sin permutar.
		 * 
		 * Si tengo un grupo de columnas tambien coloco la permutacion default
		 */
		if (columnPermutation == null || columnPermutation.length != getModel().getColumnasCount()
				|| getModel().getGruposColumnas().size() > 0) {
			columnPermutation = new int[getModel().getColumnasCount()];
			for (int i = 0; i < columnPermutation.length; i++) {
				columnPermutation[i] = i;				
			}
		}
		inversePermutation = inversePermutation(columnPermutation);
	}

	/**
	 * Calcula la inversa de una permutación
	 * @param permutation Permutación de la que se quiere calcular la inversa
	 * @return Inversa de la permutación
	 */
	private static int[] inversePermutation(int[] permutation) {
		if (permutation == null) return null;
		int[] inverse = new int[permutation.length];
		for (int i=0; i<permutation.length; i++)
			inverse[permutation[i]] = i;
		return inverse;
	}
	
	/**
	 * Setea los parámetros apropiados para el modo análisis
	 */
	private void setTableAnalisis() {
		if (getModel().isAnalisis()) {
			getJTable().setOrdenar(getModel().isOrdenar());
			getJTable().setOperar(getModel().isOperar());
			getJTable().setModoAnalisis(true);
			getJTable().setAllowSorting(false);
			getJTable().setReorderingAllowed(false);
		} else {
			getJTable().setModoAnalisis(false);
			getJTable().setAllowSorting(true);
			getJTable().setReorderingAllowed(true);			
		}
	}
	
	/**
	 * Devuelve el listener que se colocará en el modelo para ver cuando este
	 * cambió
	 * 
	 * @return Listener que se va a utilizar en el modelo
	 */
	@SuppressWarnings("unchecked")
	private TableChangeListener getTableListener() {
		if (tableListener == null) {
			tableListener = new TableChangeListener() {
				public void dataChange(TableChangeEvent e) {
					actualizarValores((List<T>)e.getSelectedObjects());
				}

				public void structureChange(TableChangeEvent e) {
					switch (e.getEventType()) {
					case TableChangeEvent.TYPE_COLUMN_CHANGE:
					case TableChangeEvent.TYPE_COLUMN_GROUP_CHANGE:
					case TableChangeEvent.TYPE_FILL_MODE_CHANGE:
						//TODO VER SI DEBE actualizarEstructuraColumnas()
						//actualizarEstructuraColumnas();
						actualizarValores((List<T>)e.getSelectedObjects());
						break;

					case TableChangeEvent.TYPE_ANALISIS_CHANGE:
						setTableAnalisis();
						break;
						
					case TableChangeEvent.TYPE_SELECTION_MODE_CHANGE:
						getJTable().setSelectionMode(getModel().getModoSeleccion());
						break;
						
					default:
						break;
					}
				}
			};
		}
		return tableListener;
	}
	
	private class PanTablaMouseListener extends MouseAdapter {
		public void mouseReleased(MouseEvent arg0) {
			getJTable().clearSelection();
		}
	}
	
}
package ar.com.fwcommon.componentes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;

import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.Diccionario;

/**
 * Componente que simplifica el uso del componente JTable de Java.
 */
public class FWJTable extends JTable implements ListSelectionListener, FWJTableEventListener, TableColumnModelListener {

	private static final long serialVersionUID = 3622735924123481329L;

	private static Logger logger = Logger.getLogger(FWJTable.class);
	
	public static final int COLUMN_DEFAULT_WIDTH = 100;
	public static final int STRING_COLUMN_DEFAULT_WIDTH = 150;
	public static final int INT_COLUMN_DEFAULT_WIDTH = 50;
	public static final int DATE_COLUMN_DEFAULT_WIDTH = 70;
	public static final int TIME_COLUMN_DEFAULT_WIDTH = 40;
	public static final int COMBO_COLUMN_DEFAULT_WIDTH = 100;
	public static final int IMAGE_COLUMN_DEFAULT_WIDTH = 100;
	public static final int CHECK_COLUMN_DEFAULT_WIDTH = 50;
	public static final int ROW_DEFAULT_HEIGHT = 20;
	public static final int SCROLL_WIDTH = 20;
	public static final int LEFT_ALIGN = JLabel.LEFT;
	public static final int CENTER_ALIGN = JLabel.CENTER;
	public static final int RIGHT_ALIGN = JLabel.RIGHT;
	public static final int STRING_CELL = 0;
	public static final int INT_CELL = 1;
	public static final int DATE_CELL = 2;
	public static final int TIME_CELL = 3;
	public static final int FLOAT_CELL = 4;
	public static final Color HEADER_BACKGROUND_DEFAULT_COLOR = new Color(243, 246, 251);
	public static final Color HEADER_FOREGROUND_DEFAULT_COLOR = Color.BLACK;
	public static final int SINGLE_SELECTION = 0;
	public static final int SINGLE_INTERVAL_SELECTION = 1;
	public static final int MULTIPLE_INTERVAL_SELECTION = 2;
	public final static int LLENADO_HORIZONTAL = 1;
	public final static int LLENADO_VERTICAL = 2;
	public final static int LLENADO_NINGUNO = 3;
	protected FWColumn[] tiposColumnas; //Vector que contiene los tipos especiales de columna asignados por el setXXXColumn
	private boolean tableLock; //Variable para control del lockeo total de la tabla
	private String tableDateMask; //String que contiene la máscara de formato de date
	private String tableTimeMask; //String que contiene la máscara de formato de time
	protected Color alternativeColor; //Objeto color. Si está definido marca el color de fondo de las filas salteadas
	private Color headerBackground = HEADER_BACKGROUND_DEFAULT_COLOR; //Color de fondo de los headers
	private Color headerForeground = HEADER_FOREGROUND_DEFAULT_COLOR; //Color de texto de los headers
	private FWJTableRenderer tableRenderer; //Variable privada del table renderer
	private DefaultTableModel tableModel; //Variable privada del modelo
	protected Hashtable<String, FWCell> lockCells; //Colección que contiene las celdas lockeadas
	protected Hashtable<String, FWCell> backgroundColorCells; //Colección que contiene las celdas con un color de fondo
	protected Hashtable<String, FWCell> foregroundColorCells; //Colección que contiene las celdas con un color de fuente
	protected Hashtable<Integer, FWRow> backgroundColorRows; //Colección que contiene las filas con un color de fondo
	protected Hashtable<String, FWCell> fontCells; //Colección que contiene las celdas con una fuente
	protected Hashtable<String, FWCell> alignmentCells; //Colección que contiene las celdas con una alineación
	protected Hashtable<String, FWCell> tooltipCells; //Colección que contiene las celdas con un tootltip
	protected Hashtable<String, FWCell> typesCells; //Colección que contiene las celdas con un tipo
	private ColumnHeaderTooltips columnHeaderToolTips; //Objeto que contiene los tooltips de los headers
	private int sepDateMask1, sepDateMask2, sepTimeMask1, sepTimeMask2; //Variables auxiliares para el control de la edición de un date y un time
	private int llenado = LLENADO_NINGUNO;
	private boolean allowHidingColumns; //Variable que contiene si se permite o no esconder columnas
	private boolean allowSorting; //Variable que contiene si se permite o no ordenar las filas
	private boolean permiteCopiar; //Variable que contiene si se permite o no copiar el contenido de la tabla
	private boolean permiteHeaderMultiple = false; //Variable que contiene si se permite o no utilizar multiples headers
	private boolean sortColsAscending = false; //Variable que contiene si las filas deben ordenarse ascendentemente
	private int lastSortedColIndex = -1; //Variable que contiene el índice de la última columna por la cual se ordeno el contenido de la tabla
	private int lastEditedRow = -1;
	private Object cellOldValue; //Variable que contiene el valor anterior de la celda que se está editando
	private static int fontSize = 11; //Variable que contiene el tamaño de la fuente
	private List gruposCeldas; //Colección que contiene los grupos de celdas
	private List gruposHeaders; //Colección que contiene los grupos de headers
	private int colClave; //Variable que indice en columna se encuentran las claves
	private List<Object> clavesSeleccionadas; //Variable que contiene las claves seleccionadas
	FWColumnChooser columnChooser = new FWColumnChooser("Mostrar columnas");
	private ColumnHeaderListener columnHeaderListener;

	/**
	 * Método constructor.
	 */
	public FWJTable() {
		this(0, 0);
	}

	/**
	 * Método constructor.
	 * @param rows La cantidad de filas.
	 * @param cols La cantidad de columnas.
	 */
	@SuppressWarnings("unchecked")
	public FWJTable(int rows, int cols) {
//		setSurrendersFocusOnKeystroke(true);
//		getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "selectNextColumnCell");     
//		getInputMap().put(KeyStroke.getKeyStroke("shift ENTER"), "selectPreviousColumnCell");
		//Setea la tipografía de la tabla
		setTableHeader(new FWJTableHeader(getColumnModel()));
		setFont(getFont().deriveFont((float)fontSize));
		getTableHeader().setFont(getTableHeader().getFont().deriveFont((float)fontSize));
		setRowHeight(ROW_DEFAULT_HEIGHT);
		tableRenderer = new FWJTableRenderer();
		setDateMask("dd/MM/yyyy");
		setTimeMask("HH:mm");
		tiposColumnas = new FWColumn[cols];
		tableModel = new FWJTableModel(rows, cols);
		setModel(tableModel);
		tableModel.addTableModelListener(new FWJTableModelListener());
		setDefaultRenderer(Object.class, tableRenderer);
		setDefaultRenderer(Integer.class, tableRenderer);
		addFocusListener(new FocusTable());
		getTableHeader().addMouseListener(getColumnHeaderListener());
		columnHeaderToolTips = new ColumnHeaderTooltips();
		getTableHeader().addMouseMotionListener(columnHeaderToolTips);
		setAutoResizeMode(FWJTable.AUTO_RESIZE_OFF);
		lockCells = new Hashtable();
		backgroundColorCells = new Hashtable();
		backgroundColorRows = new Hashtable();
		foregroundColorCells = new Hashtable();
		fontCells = new Hashtable();
		tooltipCells = new Hashtable();
		typesCells = new Hashtable();
		alignmentCells = new Hashtable();
		gruposCeldas = new ArrayList();
		gruposHeaders = new ArrayList();
		clavesSeleccionadas = new ArrayList();
		setReorderingAllowed(true);
		setUI(new FWJTableUI());
		getTableHeader().setDefaultRenderer(new FWJTableHeaderRenderer());
		//KeyListener que captura los eventos y los pasa al listener del editor
		//correspondiente a la celda que se está editando
		addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent evt) {
				int row = getSelectedRow();
				int col = getSelectedColumn();
				//Al presionar 'ESCAPE' cuando se esta editando una celda se vuelve al valor anterior
				if(evt.getKeyCode() == KeyEvent.VK_ESCAPE && isEditing()) {
					TableCellEditor editor = getCellEditor();
					if(editor != null)
						editor.stopCellEditing();
					setValueAt(cellOldValue, row, col);
				} else if(row != -1 && col != -1) {
					if(getCellEditor(row, col) instanceof KeyEditor) {
						KeyEditor editor = (KeyEditor)getCellEditor(row, col);
						KeyListener kl = editor.getKeyListener();
						kl.keyReleased(evt);
					}
				}
			}

			public void keyPressed(KeyEvent evt) {
				int row = getSelectedRow();
				int col = getSelectedColumn();
				if(row != -1 && col != -1) {
					if(getCellEditor(row, col) instanceof KeyEditor) {
						KeyEditor editor = (KeyEditor)getCellEditor(row, col);
						KeyListener kl = editor.getKeyListener();
						kl.keyReleased(evt);
					}
				}
			}

			public void keyTyped(KeyEvent evt) {
				int row = getSelectedRow();
				int col = getSelectedColumn();
				if(row != -1 && col != -1) {
					if(getCellEditor(row, col) instanceof KeyEditor) {
						KeyEditor editor = (KeyEditor)getCellEditor(row, col);
						KeyListener kl = editor.getKeyListener();
						kl.keyTyped(evt);
					}
				}
			}
		});
		addMouseListener(new TablaMouseAdapter());
		putClientProperty("terminateOnFocusLost", Boolean.TRUE);
	}

	/**
	 * Retorna el estado general de lockeo de la table. Ver isCellEditable del
	 * modelo.
	 * @return tableLock
	 */
	public boolean isTableLock() {
		return tableLock;
	}

	/**
	 * Establece el estado general de lockeo de la tabla. Ver isCellEditable del
	 * modelo.
	 * @param estado
	 */
	public void setTableLock(boolean estado) {
		tableLock = estado;
	}

	/**
	 * Retorna la máscara para la fecha. Por defecto es <b>dd/mm/yyyy</b>.
	 * @return tableDateMask
	 */
	public String getDateMask() {
		return tableDateMask;
	}

	/**
	 * Establece la máscara para la fecha. Por defecto es <b>dd/MM/yyyy</b>.
	 * Hace cálculos preliminares para utilizar más adelante.
	 * @param mask
	 */
	public void setDateMask(String mask) {
		tableDateMask = mask;
		sepDateMask1 = tableDateMask.indexOf("/");
		sepDateMask2 = tableDateMask.indexOf("/", sepDateMask1 + 1);
	}

	/**
	 * Retorna la máscara para la hora. Por defecto es <b>hh:mm:ss</b>.
	 * @return tableTimeMask
	 */
	public String getTimeMask() {
		return tableTimeMask;
	}

	/**
	 * Establece la máscara para la hora: por defecto es <b>hh:mm:ss</b>. Hace
	 * cálculos preliminares para utilizar más adelante.
	 * @param mask
	 */
	public void setTimeMask(String mask) {
		tableTimeMask = mask;
		sepTimeMask1 = tableTimeMask.indexOf(":");
		sepTimeMask2 = tableTimeMask.indexOf(":", sepTimeMask1 + 1);
	}

	/**
	 * Retorna si se le permitirá o no al usuario seleccionar las columnas visibles
	 * al hacer doble click en el header.
	 * @return <b>true</b> si se le permitirá al usuario seleccionar las columnas visibles.
	 */
	public boolean allowHidingColumns() {
		return allowHidingColumns;
	}

	/**
	 * Establece la propiedad de la tabla de permitir ocultar/mostrar sus
	 * columnas.
	 * @param allowHidingColumns
	 */
	public void setAllowHidingColumns(boolean allowHidingColumns) {
		this.allowHidingColumns = allowHidingColumns;
	}

	/**
	 * Devuelve <b>true</b> si la tabla permite ser ordenada.
	 * @return allowSorting
	 */
	public boolean allowSorting() {
		return allowSorting;
	}

	/**
	 * Establece la propiedad de la tabla de permitir ser ordenada.
	 * @param allowSorting
	 */
	public void setAllowSorting(boolean allowSorting) {
		this.allowSorting = allowSorting;
	}

	/**
	 * Estable si se puede o no copiar el contenido de la tabla.
	 * @param permiteCopiar
	 */
	public void setPermiteCopiar(boolean permiteCopiar) {
		this.permiteCopiar = permiteCopiar;
	}

	/**
	 * Retorna si se puede o no copiar el contenido de la tabla.
	 * @return
	 */
	public boolean permiteCopiar() {
		return permiteCopiar;
	}

	/**
	 * Retorna el color de fondo para las filas alternadas.
	 * @return alternativeColor
	 */
	public Color getAlternativeColor() {
		return alternativeColor;
	}
	
	public void pintarSubimagen(Graphics g, int x, int y, int ancho, int alto){
		g.setClip(x, y, ancho, alto);
		((FWJTableUI)this.getUI()).pintarPagina(g);
	}

	/**
	 * Establece el color de fondo para las filas alternadas. Si no se asigna se
	 * crea un color ligeramente inferior en tono al color default de
	 * background.
	 * @param color
	 */
	public void setAlternativeColor(Color color) {
		alternativeColor = color;
	}

	/**
	 * Retorna la cantidad de filas de la tabla.
	 * @return
	 */
	public int getRowCount() {
		if(tableModel != null) {
			return tableModel.getRowCount();
		} else {
			return 0;
		}
	}

	/**
	 * Establece la cantidad de filas máxima de la tabla. Si es necesario agrega
	 * o remueve filas del final.
	 * @param cantidad
	 */
	public void setNumRows(int cantidad) {
		tableModel.setNumRows(cantidad);
		if(cantidad == 0) {
			cellOldValue = null;
			lockCells.clear();
			backgroundColorCells.clear();
			foregroundColorCells.clear();
			backgroundColorRows.clear();
			fontCells.clear();
			tooltipCells.clear();
			alignmentCells.clear();
			typesCells.clear();
			if(tiposColumnas != null) {
				for(int i = 0; i < tiposColumnas.length; i++) {
					if(tiposColumnas[i] instanceof OperableTimeColumn) {
						((OperableTimeColumn)tiposColumnas[i]).getTipos().clear();
					}
				}
			}
		} else if(cantidad <= lastEditedRow) {
			cellOldValue = null;
		}
	}

	/**
	 * Establece la cantidad de filas máxima de la tabla. Si es necesario agrega
	 * o remueve filas del final.
	 * @param cantidad
	 * @param borrar
	 */
	public void setNumRows(int cantidad, boolean borrar) {
		if(borrar) {
			setNumRows(0);
		}
		setNumRows(cantidad);
	}

	/**
	 * Obtiene el combobox de una columna de tipo <code>ComboColumn</code>.
	 * @param col
	 * @return
	 */
	public JComboBox getComboColumn(int col) {
		if(tiposColumnas[col] instanceof ComboColumn) {
			return ((ComboColumn)tiposColumnas[col]).getCombo();
		} else {
			return null;
		}
	}

	/** Detección del evento de cambio de fila */
	public void valueChanged(ListSelectionEvent evt) {
		if(!evt.getValueIsAdjusting()) {
            if(evt.getSource() == getSelectionModel() && getRowSelectionAllowed()) {
                int newRow, oldRow;
                int first = evt.getFirstIndex();
                int last = evt.getLastIndex();
                if(this.getSelectedRow() == first) {
                    oldRow = last;
                } else {
                    oldRow = first;
                }
                newRow = this.getSelectedRow();
                newRowSelected(newRow, oldRow);
                repaint();
            }
        }
	}

	/**
	 * Elimina la fila indicada con row.
	 * @param row
	 */
	@SuppressWarnings("unchecked")
	public void removeRow(int row) {
		if(row == lastEditedRow) {
			cellOldValue = null;
		}
		lockCells = removerFilaHash(row, lockCells);
		backgroundColorCells = removerFilaHash(row, backgroundColorCells);
		foregroundColorCells = removerFilaHash(row, foregroundColorCells);
		fontCells = removerFilaHash(row, fontCells);
		tooltipCells = removerFilaHash(row, tooltipCells);
		alignmentCells = removerFilaHash(row, alignmentCells);
		typesCells = removerFilaHash(row, typesCells);
		Hashtable backgroundColorRowsTemp = (Hashtable)backgroundColorRows.clone();
		for(Iterator i = backgroundColorRowsTemp.values().iterator(); i.hasNext();) {
			FWRow fila = (FWRow)i.next();
			if(fila.getIndex() == row) {
				backgroundColorRows.remove(fila.getKey());
			}
		}
		for(int i = 0; i < tiposColumnas.length; i++) {
			if(tiposColumnas[i] instanceof OperableTimeColumn) {
				OperableTimeColumn columna = (OperableTimeColumn)tiposColumnas[i];
				Hashtable tiposTemp = (Hashtable)columna.getTipos().clone();
				for(Iterator j = tiposTemp.values().iterator(); j.hasNext();) {
					FWRow fila = (FWRow)j.next();
					if(fila.getIndex() == row)
						columna.getTipos().remove(fila.getKey());
				}
			}
		}
		tableModel.removeRow(row);
	}

	/**
	 * Remueve del hashtable especificado las celdas pertenecientes a una fila .
	 * @param row
	 * @param hashtable
	 * @return hashtable
	 */
	private Hashtable removerFilaHash(int row, Hashtable hashtable) {
		Hashtable hashtableTemp = (Hashtable)hashtable.clone();
		for(Iterator i = hashtableTemp.values().iterator(); i.hasNext();) {
			FWCell cell = (FWCell)i.next();
			if(row == cell.getRow()) {
				hashtable.remove(cell.getKey());
			}
		}
		return hashtable;
	}

	/**
	 * Elimina las filas indicadas en el array rows.
	 * @param rows
	 */
	public void removeRows(int[] rows) {
		List<Integer> rowList= new ArrayList<Integer>();
		for(int r : rows) {
			rowList.add(r);
		}
		Collections.sort(rowList);
		int cantFilasRemoved = 0;
		for(int r : rowList) {
			removeRow(r - cantFilasRemoved);
			cantFilasRemoved ++;
		}
//		for(int i = 0; i < rows.length; i++) {
//			removeRow(rows[i]);
//		}
	}

	/**
	 * Agrega una fila vacía a la tabla.
	 */
	public void addRow() {
		Object o[] = new Object[getColumnCount()];
		tableModel.addRow(o);
	}

	/**
	 * Agrega una fila a la tabla y la llena con el contenido especificado.
	 * @param o
	 */
	public void addRow(Object o[]) {
		addRow(o, false);
	}

	public void addRow(Object o[], boolean selectNewRow) {
//		if(o.length != getColumnCount()) throw new IllegalArgumentException("El array tiene dimensiones inválidas");
		tableModel.addRow(o);
		if(selectNewRow) {
			int row = getRowCount() - 1;
			selectAndScroll(row, row);
		}
	}

	/**
	 * Agrega una fila a la tabla y la llena con el contenido especificado.
	 * @param rowData
	 */
	public void addRow(Vector rowData) {
//		if(rowData.size() != getColumnCount()) throw new IllegalArgumentException("El vector tiene dimensiones inválidas");
		tableModel.addRow(rowData);
	}

	//Manejo de descriptores de columnas
	/**
	 * Retorna el array que contiene los tipos de columna asignado a cada
	 * columna. Son tipos descriptivos que sirven para validar y controlar.
	 * @return tipoColumnas
	 */
	public FWColumn[] getTiposColumnas() {
		return tiposColumnas;
	}

	/**
	 * Retorna el tipo de columna en la posición <code>col</code>.
	 * @param col
	 * @return
	 */
	public FWColumn getTipoColumna(int col) {
		return tiposColumnas[col];
	}

	/**
	 * Bloque completo de los tipos de columnas de la tabla.
	 * @param tiposColumnas
	 */
	public void setTiposColumnas(FWColumn[] tiposColumnas) {
		this.tiposColumnas = tiposColumnas;
	}

	/**
	 * Establece una columna del tipo <b>String</b>.
	 * @param col
	 * @param nombre
	 * @param ancho
	 * @param lock
	 * @param size
	 */
	public void setStringColumn(int col, String nombre, int size, int ancho, boolean lock) {
		StringColumn c = new StringColumn();
		c.setSize(size);
		tiposColumnas[col] = c;
		setCLColumnData(col, c, nombre, ancho, lock);
		c.setSize(size);
		getColumn(col).setCellEditor(new StringEditor(this, size));
	}

	/**
	 * Agrega una columna del tipo <b>String</b>.
	 * @param nombre
	 * @param size
	 * @param ancho
	 * @param lock
	 */
	public void addStringColumn(String nombre, int size, int ancho, boolean lock) {
		tableModel.addColumn(nombre);
		StringColumn c = new StringColumn();
		c.setSize(size);
		actualizarTiposColumnas(c);
		setCLColumnData(getColumnCount() - 1, c, nombre, ancho, lock);
		getColumn(getColumnCount() - 1).setCellEditor(new StringEditor(this, size));
		restablecerAtributos();
	}
	
	public void addCheckColumn(String nombre, int ancho, boolean lock) {
		tableModel.addColumn(nombre);
		CheckColumn c = new CheckColumn();
		actualizarTiposColumnas(c);
		setCLColumnData(getColumnCount() - 1, c, nombre, ancho, lock);
		getColumnModel().getColumn(getColumnCount() - 1).setCellRenderer(new CheckRenderer());
		JCheckBox check = new JCheckBox();
		check.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
					teclaEnterPresionada(getSelectedRow(), getSelectedColumn());
				}
			}
		});
		getColumn(getColumnCount() - 1).setCellEditor(new DefaultCellEditor(check));
	}

	/**
	 * Agrega una columna del tipo <b>String</b>.
	 * @param nombre
	 * @param size
	 */
	public void addStringColumn(String nombre, int size, int textAligment) {
		int ancho = (int)(size * 1.5);
		if(ancho > STRING_COLUMN_DEFAULT_WIDTH)
			ancho = STRING_COLUMN_DEFAULT_WIDTH;
		addStringColumn(nombre, size, ancho, true);
		setColumnAlignment(getColumnCount() - 1, textAligment);
	}

	/**
	 * Establece una columna del tipo <b>String</b>.
	 * @param col
	 * @param nombre
	 * @param size
	 */
	public void setStringColumn(int col, String nombre, int size) {
		int ancho = (int)(size * 1.5);
		if(ancho > STRING_COLUMN_DEFAULT_WIDTH) {
			ancho = STRING_COLUMN_DEFAULT_WIDTH;
		}
		setStringColumn(col, nombre, size, ancho, true);
	}

	/**
	 * Agrega una columna del tipo <b>String</b>.
	 * @param nombre
	 * @param size
	 */
	public void addStringColumn(String nombre, int ancho) {
		addStringColumn(nombre, 100, ancho, true);
	}

	/**
	 * Agrega una columna del tipo <b>Multiline</b>.
	 * @param nombre
	 * @param ancho
	 */
	public void addMultilineColumn(String nombre, int ancho) {
		addMultilineColumn(nombre, ancho, true);
	}

	/**
	 * Agrega una columna del tipo <b>Multiline</b>.
	 * @param nombre
	 * @param ancho
	 * @param lock
	 */
	public void addMultilineColumn(String nombre, int ancho, boolean lock) {
		MultiLineCellRenderer multiLineCellRenderer = new MultiLineCellRenderer();
		tableModel.addColumn(nombre);
		MultilineColumn c = new MultilineColumn();
		c.setMultiLineCellRenderer(multiLineCellRenderer);
		actualizarTiposColumnas(c);
		setCLColumnData(getColumnCount() - 1, c, nombre, ancho, lock);
		getColumnModel().getColumn(getColumnCount() - 1).setCellRenderer(new MultiLineCellRenderer());
		getColumn(getColumnCount() - 1).setCellEditor(new MultilineStringEditor());

	}

	/**
	 * Establece una columna del tipo <b>int</b>.
	 * @param col
	 * @param nombre
	 * @param min
	 * @param max
	 * @param ancho
	 * @param lock
	 * @param nulleable
	 */
	public void setIntColumn(int col, String nombre, int min, int max, int ancho, boolean lock, boolean nulleable) {
		IntColumn c = new IntColumn();
		tiposColumnas[col] = c;
		setCLColumnData(col, c, nombre, ancho, lock);
		c.setMinValue(min);
		c.setMaxValue(max);
		getColumn(col).setCellEditor(new IntegerEditor(this, min, max, nulleable));
	}

	/**
	 * Establece una columna del tipo <b>int</b>.
	 * @param col
	 * @param nombre
	 * @param min
	 * @param max
	 * @param ancho
	 * @param lock
	 */
	public void setIntColumn(int col, String nombre, int min, int max, int ancho, boolean lock) {
		IntColumn c = new IntColumn();
		tiposColumnas[col] = c;
		setCLColumnData(col, c, nombre, ancho, lock);
		c.setMinValue(min);
		c.setMaxValue(max);
		getColumn(col).setCellEditor(new IntegerEditor(this, min, max));
	}

	/**
	 * Establece una columna del tipo <b>int</b>.
	 * @param col
	 * @param nombre
	 * @param ancho
	 * @param lock
	 */
	public void setIntColumn(int col, String nombre, int ancho, boolean lock) {
		setIntColumn(col, nombre, Integer.MIN_VALUE, Integer.MAX_VALUE, ancho, lock);
	}

	/**
	 * Agrega una columna del tipo <b>int</b>.
	 * @param nombre
	 * @param min
	 * @param max
	 * @param ancho
	 * @param lock
	 */
	public void addIntColumn(String nombre, int min, int max, int ancho, boolean lock) {
		((DefaultTableModel)getModel()).addColumn(nombre);
		IntColumn c = new IntColumn();
		actualizarTiposColumnas(c);
		setCLColumnData(getColumnCount() - 1, c, nombre, ancho, lock);
		c.setMinValue(min);
		c.setMaxValue(max);
		getColumn(getColumnCount() - 1).setCellEditor(new IntegerEditor(this, min, max));
		restablecerAtributos();
	}

	/**
	 * Agrega una columna del tipo <b>int</b>.
	 * @param nombre
	 * @param ancho
	 */
	public void addIntColumn(String nombre, int ancho) {
		addIntColumn(nombre, -999999, 999999, ancho, true);
	}

	/**
	 * Agrega una columna del tipo <b>int</b>.
	 * @param nombre
	 */
	public void addIntColumn(String nombre) {
		addIntColumn(nombre, -999999, 999999, INT_COLUMN_DEFAULT_WIDTH, true);
	}

	/**
	 * Establece una columna del tipo <b>int</b>.
	 * @param col
	 * @param nombre
	 * @param min
	 * @param max
	 */
	public void setIntColumn(int col, String nombre, int min, int max) {
		setIntColumn(col, nombre, min, max, INT_COLUMN_DEFAULT_WIDTH, true);
	}

	/**
	 * Agrega una columna del tipo <b>int</b>.
	 * @param nombre
	 * @param min
	 * @param max
	 */
	public void addIntColumn(String nombre, int min, int max) {
		addIntColumn(nombre, min, max, INT_COLUMN_DEFAULT_WIDTH, true);
	}

	/**
	 * Establece una columna del tipo <b>Date</b>.
	 * @param col
	 * @param nombre
	 * @param ancho
	 * @param lock
	 */
	public void setDateColumn(int col, String nombre, int ancho, boolean lock) {
		//setDateColumn(col, nombre, Date.valueOf("1901-01-01"), Date.valueOf("2099-01-01"), ancho, lock);
		setDateColumn(col, nombre, DateUtil.getFecha(1901, 1, 1) , DateUtil.getFecha(2099, 1, 1), ancho, lock);
	}

	/**
	 * Agrega una columna del tipo <b>Date</b>.
	 * @param nombre
	 * @param ancho
	 * @param lock
	 */
	public void addDateColumn(String nombre, int ancho, boolean lock) {
		//addDateColumn(nombre, Date.valueOf("1901-01-01"), Date.valueOf("2099-01-01"), ancho, lock);
		addDateColumn(nombre, DateUtil.getFecha(1901,1,1), DateUtil.getFecha(2099, 1, 1), ancho, lock);
	}

	/**
	 * Establece una columna del tipo <b>Date</b>.
	 * @param col
	 * @param nombre
	 */
	public void setDateColumn(int col, String nombre) {
		setDateColumn(col, nombre, DATE_COLUMN_DEFAULT_WIDTH, true);
	}

	/**
	 * Agrega una columna del tipo <b>Date</b>.
	 * @param nombre
	 */
	public void addDateColumn(String nombre) {
		addDateColumn(nombre, DATE_COLUMN_DEFAULT_WIDTH, true);
	}

	/**
	 * Establece una columna del tipo <b>Date</b>.
	 * @param col
	 * @param nombre
	 * @param ancho
	 * @param lock
	 * @param maxDate
	 * @param minDate
	 */
	public void setDateColumn(int col, String nombre, Date maxDate, Date minDate, int ancho, boolean lock) {
		DateColumn c = new DateColumn();
		tiposColumnas[col] = c;
		setCLColumnData(col, c, nombre, ancho, lock);
		c.setMinDate(minDate);
		c.setMaxDate(maxDate);
		getColumn(col).setCellEditor(new DateEditor(this));
	}

	/**
	 * Agrega una columna del tipo <b>Date</b>.
	 * @param nombre
	 * @param maxDate
	 * @param minDate
	 * @param ancho
	 * @param lock
	 */
	public void addDateColumn(String nombre, Date maxDate, Date minDate, int ancho, boolean lock) {
		((DefaultTableModel)getModel()).addColumn(nombre);
		DateColumn c = new DateColumn();
		actualizarTiposColumnas(c);
		setCLColumnData(getColumnCount() - 1, c, nombre, ancho, lock);
		c.setMinDate(minDate);
		c.setMaxDate(maxDate);
		getColumn(getColumnCount() - 1).setCellEditor(new DateEditor(this));
		restablecerAtributos();
	}

	/**
	 * Establece una columna del tipo <b>Time</b>.
	 * @param col
	 * @param nombre
	 * @param ancho
	 * @param lock
	 */
	public void setTimeColumn(int col, String nombre, int ancho, boolean lock) {
		TimeColumn c = new TimeColumn();
		tiposColumnas[col] = c;
		setCLColumnData(col, c, nombre, ancho, lock);
		getColumn(col).setCellEditor(new TimeEditor(this));
	}

	/**
	 * Establece una columna del tipo <b>Time</b>.
	 * @param col
	 * @param nombre
	 */
	public void setTimeColumn(int col, String nombre) {
		setTimeColumn(col, nombre, TIME_COLUMN_DEFAULT_WIDTH, true);
	}

	/**
	 * Agrega una columna del tipo <b>Time</b>.
	 * @param nombre
	 * @param ancho
	 * @param lock
	 */
	public void addTimeColumn(String nombre, int ancho, boolean lock) {
		((DefaultTableModel)getModel()).addColumn(nombre);
		TimeColumn c = new TimeColumn();
		actualizarTiposColumnas(c);
		setCLColumnData(getColumnCount() - 1, c, nombre, ancho, lock);
		getColumn(getColumnCount() - 1).setCellEditor(new TimeEditor(this));
		restablecerAtributos();
	}

	/**
	 * Establece una columna del tipo <b>Time</b> sobre la cual se pueden
	 * realizar operaciones.
	 * @param col
	 * @param nombre
	 * @param ancho
	 * @param lock
	 */
	public void setOperableTimeColumn(int col, String nombre, int ancho, boolean lock) {
		OperableTimeColumn c = new OperableTimeColumn();
		tiposColumnas[col] = c;
		setCLColumnData(col, c, nombre, ancho, lock);
	}

	/**
	 * Establece una columna del tipo <b>Time</b> sobre la cual se pueden
	 * realizar operaciones.
	 * @param col
	 * @param nombre
	 */
	public void setOperableTimeColumn(int col, String nombre) {
		setOperableTimeColumn(col, nombre, TIME_COLUMN_DEFAULT_WIDTH, true);
	}

	/**
	 * Establece una columna del tipo <b>ComboBox</b>.
	 * @param col
	 * @param nombre
	 * @param ancho
	 * @param lock
	 * @param combo
	 */
	public void setComboColumn(int col, String nombre, JComboBox combo, int ancho, boolean lock) {
		ComboColumn c = new ComboColumn();
		tiposColumnas[col] = c;
		setCLColumnData(col, c, nombre, ancho, lock);
		//Listener para que cuando la tabla pierda el foco, el combo salga del
		//modo de edición
		combo.addFocusListener(new FocusListener() {

			public void focusLost(FocusEvent evt) {
				TableCellEditor editor = getCellEditor();
				if(editor != null)
					editor.stopCellEditing();
			}

			public void focusGained(FocusEvent evt) {
			}
		});
		c.setCombo(combo);
		//getColumn(col).setCellEditor(new DefaultCellEditor(combo));
		getColumn(col).setCellEditor(new ComboBoxEditor(combo));
	}

	/**
	 * Establece una columna del tipo <b>ComboBox</b>.
	 * @param col
	 * @param nombre
	 * @param combo
	 */
	public void setComboColumn(int col, String nombre, JComboBox combo) {
		setComboColumn(col, nombre, combo, COMBO_COLUMN_DEFAULT_WIDTH, true);
	}

	/**
	 * Establece una columna del tipo <b>Image</b>.
	 * @param col
	 * @param nombre
	 * @param ancho
	 * @param lock
	 * @param imagen
	 */
	public void setImagenColumn(int col, String nombre, JLabel imagen, int ancho, boolean lock) {
		ImageColumn c = new ImageColumn();
		tiposColumnas[col] = c;
		setCLColumnData(col, c, nombre, ancho, lock);
		c.setImagen(imagen);
		getColumnModel().getColumn(col).setCellRenderer(new ImageCellRenderer());
	}

	/**
	 * Establece una columna del tipo <b>Image</b>.
	 * @param col
	 * @param nombre
	 * @param imagen
	 */
	public void setImagenColumn(int col, String nombre, JLabel imagen) {
		setImagenColumn(col, nombre, imagen, IMAGE_COLUMN_DEFAULT_WIDTH, true);
	}

	/**
	 * Establece una columna del tipo <b>Check</b>.
	 * @param col
	 * @param nombre
	 * @param ancho
	 * @param lock
	 */
	public void setCheckColumn(int col, String nombre, int ancho, boolean lock) {
		CheckColumn c = new CheckColumn();
		tiposColumnas[col] = c;
		setCLColumnData(col, c, nombre, ancho, lock);
		getColumnModel().getColumn(col).setCellRenderer(new CheckRenderer());
		JCheckBox check = new JCheckBox();
		check.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if(evt.getStateChange() == ItemEvent.SELECTED) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							teclaEnterPresionada(getSelectedRow(), getSelectedColumn());							
						}						
					});
				}
			}
		});
		check.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
					teclaEnterPresionada(getSelectedRow(), getSelectedColumn());
				}
			}
		});
		check.setMargin(new Insets(0, 0, 0, 0));
		check.setHorizontalAlignment(CENTER_ALIGN);
		getColumn(col).setCellEditor(new DefaultCellEditor(check));
	}
	


	/**
	 * Establece una columna del tipo <b>Multiline</b>.
	 * @param col
	 * @param nombre
	 * @param ancho
	 * @param lock
	 */
	public void setMultilineColumn(int col, String nombre, int ancho, boolean lock) {
		setMultilineColumn(col, nombre, ancho, lock, false);
	}

	/**
	 * Establece una columna del tipo <b>Multiline</b>.
	 * @param col
	 * @param nombre
	 * @param ancho
	 * @param lock
	 * @param isHtml
	 */
	public void setMultilineColumn(int col, String nombre, int ancho, boolean lock, boolean isHtml) {
		MultilineColumn c = new MultilineColumn();
		c.setTextLeftAlign(true);
		tiposColumnas[col] = c;
		c.size = ancho;
		setCLColumnData(col, c, nombre, ancho, lock);
		if(isHtml) {
			getColumnModel().getColumn(col).setCellRenderer(new HtmlCellRenderer());
		} else {
			getColumnModel().getColumn(col).setCellRenderer(new MultiLineCellRenderer());
		}
		getColumn(col).setCellEditor(new MultilineStringEditor());
	}

	/**
	 * Establece una columna del tipo <b>Check</b>.
	 * @param col
	 * @param nombre
	 */
	public void setCheckColumn(int col, String nombre) {
		setCheckColumn(col, nombre, CHECK_COLUMN_DEFAULT_WIDTH, true);
	}

	/**
	 * Agrega una CLColumn al arreglo que contiene los distintos tipos de
	 * columnas.
	 * @param col
	 */
	private void actualizarTiposColumnas(FWColumn col) {
		if(tiposColumnas != null) {
			int cantAnterior = tiposColumnas.length;
			FWColumn[] tiposColumnasTemp = new FWColumn[cantAnterior + 1];
			System.arraycopy(tiposColumnas, 0, tiposColumnasTemp, 0, cantAnterior);
			tiposColumnasTemp[cantAnterior] = col;
			setTiposColumnas(tiposColumnasTemp);
		} else {
			tiposColumnas = new FWColumn[1];
			tiposColumnas[0] = col;
		}
	}

	protected void restablecerAtributos() {
		for(int i = 0; i < tiposColumnas.length; i++) {
			FWColumn col = tiposColumnas[i];
			int indice = col.getIndice();
			getColumn(indice).setHeaderValue(col.getNombre());
			setColumnWidth(indice, col.getAncho());
			if (col instanceof MultilineColumn) {
				getColumn(indice).setCellRenderer(((MultilineColumn)col).getMultiLineCellRenderer());
			}
		}
	}

	/**
	 * Intercambia el contenido de las filas manteniendo todas las propiedades
	 * que actualmente tienen esas filas (color, fuente, estado de lock, etc). 
	 * @param row1
	 * @param row2
	 */
	public void intercambiarFilas(int row1, int row2) {
		Object[] valuesTmp = new Object[getColumnCount()];
		for(int c = 0; c < getColumnCount(); c++) {
			valuesTmp[c] = getValueAt(row1, c);
			setValueAt(getValueAt(row2, c), row1, c);
			String key1 = row1 + "," + c;
			String key2 = row2 + "," + c;
			//intercambio la información de lockeo de celdas
			intercambiarEnHash(lockCells, key1, row1, key2, row2);
			//intercambio la información del color de fondo
			intercambiarEnHash(backgroundColorCells, key1, row1, key2, row2);
			//intercambio la información del color de la fuente
			intercambiarEnHash(foregroundColorCells, key1, row1, key2, row2);
			//intercambio la información de la fuente
			intercambiarEnHash(fontCells, key1, row1, key2, row2);
			//intercambio la información de la alineación
			intercambiarEnHash(alignmentCells, key1, row1, key2, row2);
			//intercambio la información de tooltips
			intercambiarEnHash(tooltipCells, key1, row1, key2, row2);
			//intercambio la información de los tipos de celdas
			intercambiarEnHash(typesCells, key1, row1, key2, row2);
		}
		for(int c = 0; c < valuesTmp.length; c++) {
			setValueAt(valuesTmp[c], row2, c);
		}
		//intercambio el color por filas
		FWRow clRow1 = backgroundColorRows.get(row1);
		FWRow clRow2 = backgroundColorRows.get(row2);
		if(clRow1 != null) {
			clRow1.setIndex(row2);
			backgroundColorRows.put(row2, clRow1);
		}
		if(clRow2 != null) {
			clRow2.setIndex(row1);
			backgroundColorRows.put(row1, clRow2);
		}
	}

	private void intercambiarEnHash(Hashtable<String, FWCell> hash, String key1, int row1, String key2, int row2) {
		FWCell cell1 = hash.get(key1);
		FWCell cell2 = hash.get(key2);
		if(cell1 != null) {
			cell1.setRow(row2);
			hash.put(cell1.getKey(), cell1);
		} 
		if(cell2 != null) {
			cell2.setRow(row1);
			hash.put(cell2.getKey(), cell2);
		}
	}

	/**
	 * Setea los valores de las columnas.
	 * @param nroCol
	 * @param col
	 * @param nombre
	 * @param ancho
	 * @param lock
	 */
	protected void setCLColumnData(int nroCol, FWColumn col, String nombre, int ancho, boolean lock) {
		col.setIndice(nroCol);
		col.setIndiceOriginal(nroCol);
		col.setNombre(nombre);
		col.setAnchoOriginal(ancho);
		col.setAncho(ancho);
		col.setLock(lock);
		setColumnWidth(nroCol, ancho);
		getColumn(nroCol).setHeaderValue(nombre);
	}

	/**
	 * Retorna el <b>ancho</b> de la columna en el índice pasado por parámetro.
	 * @param index
	 * @return
	 */
	public int getColumnWidth(int index) {
		return getColumnModel().getColumn(index).getWidth();
	}

	/**
	 * Establece una columna del tipo <b>float</b>.
	 * @param col
	 * @param nombre
	 * @param ancho
	 * @param lock
	 * @param max
	 * @param min
	 */
	public void setFloatColumn(int col, String nombre, float min, float max, int ancho, boolean lock) {
		FloatColumn c = new FloatColumn();
		tiposColumnas[col] = c;
		setCLColumnData(col, c, nombre, ancho, lock);
		c.setMinValue(min);
		c.setMaxValue(max);
		getColumn(col).setCellEditor(new FloatEditor(this, min, max));
	}

	/**
	 * Establece una columna del tipo <b>float</b>.
	 * @param col
	 * @param nombre
	 * @param ancho
	 * @param lock
	 */
	public void setFloatColumn(int col, String nombre, int ancho, boolean lock) {
		setFloatColumn(col, nombre, Float.MIN_VALUE, Float.MAX_VALUE, ancho, lock);
	}

	/**
	 * Agrega una columna del tipo <b>float</b>.
	 * @param nombre
	 * @param min
	 * @param max
	 * @param ancho
	 * @param lock
	 */
	public void addFloatColumn(String nombre, float min, float max, int ancho, boolean lock) {
		((DefaultTableModel)getModel()).addColumn(nombre);
		FloatColumn c = new FloatColumn();
		actualizarTiposColumnas(c);
		setCLColumnData(getColumnCount() - 1, c, nombre, ancho, lock);
		c.setMinValue(min);
		c.setMaxValue(max);
		getColumn(getColumnCount() - 1).setCellEditor(new FloatEditor(this, min, max));
		restablecerAtributos();
	}

	/**
	 * Agrega una columna del tipo <b>float</b>.
	 * @param nombre
	 * @param ancho
	 */
	public void addFloatColumn(String nombre, int ancho) {
		addFloatColumn(nombre,  -9999999, 999999, ancho, true);
	}

	/**
	 * Agrega una columna del tipo <b>float</b>.
	 * @param nombre
	 */
	public void addFloatColumn(String nombre) {
		addFloatColumn(nombre, -999999, 999999,  INT_COLUMN_DEFAULT_WIDTH, true);
	}

	/**
	 * Establece una columna del tipo <b>float</b>.
	 * @param col
	 * @param nombre
	 * @param min
	 * @param max
	 */
	public void setFloatColumn(int col, String nombre, float min, float max) {
		setFloatColumn(col, nombre, min, max, INT_COLUMN_DEFAULT_WIDTH, true);
	}

	/**
	 * Agrega una columna del tipo <b>float</b>.
	 * @param nombre
	 * @param min
	 * @param max
	 */
	public void addFloatColumn(String nombre, float min, float max) {
		addFloatColumn(nombre, min, max, INT_COLUMN_DEFAULT_WIDTH, true);
	}
	//FIN DAM
	
	/**
	 * Setea el <b>ancho</b> de la columna en el índice pasado por parámetro.
	 * @param index
	 * @param ancho
	 */
	public void setColumnWidth(int index, int ancho) {
		getColumnModel().getColumn(index).setMinWidth(0);
		getColumnModel().getColumn(index).setPreferredWidth(ancho);
		getColumnModel().getColumn(index).setWidth(ancho);
		getTableHeader().getColumnModel().getColumn(index).setMinWidth(0);
		getTableHeader().getColumnModel().getColumn(index).setPreferredWidth(ancho);
		getTableHeader().getColumnModel().getColumn(index).setWidth(ancho);
		
	}

	/** Manejo del evento de selección de filas */
	public void newRowSelected(int newRow, int oldRow) {
	}

	/** Manejo del evento de selección de una columna */
	public void newColumnSelected(int newCol, int oldCol) {
	}

	/** Manejo del evento para cuando se edita una columna */
	public void cellEdited(int cell, int row) {
	}

	/**
	 * Manejo del evento para cuando se hace doble click en el header de la
	 * tabla
	 */
	public void doubleClickHeader() {
	}

	/** Manejo del evento para cuando se hace un click en el header de la tabla */
	public void singleClickHeader() {
	}

	/** Manejo del evento para cuando se agrega una fila */
	public void newRowAdded(int valorNewRow) {
	}

	/** Manejo del evento para cuando se elimina una fila */
	public void newRowDeleted() {
	}

	/** Manejo del evento para cuando se actualiza una fila */
	public void newRowUpdated(int column, int row) {
	}

	/**
	 * Devuelve el valor de una celda del tipo correcto. Ej.: Si la celda
	 * pertenece a una columna del tipo <b>DateColumn</b> el método devolverá
	 * un objeto del tipo <b>java.sql.Date</b>.
	 * @param row
	 * @param col
	 * @return value
	 */
    public Object getTypedValueAt(int row, int col) {
        Object value = super.getValueAt(row, col);
        FWColumn tipoColumna = tiposColumnas[col];
        FWCell celda = typesCells.get(row + "," + col);
        if(value instanceof String) {
            if(tipoColumna instanceof DateColumn || (celda != null && celda.getType() == DATE_CELL)) {
                if(String.valueOf(value).compareTo("") != 0) {
                    Date d = stringToDate(String.valueOf(value));
                    return d;
                } else {
                    return null;
                }
            } else if(tipoColumna instanceof TimeColumn || (celda != null && celda.getType() == TIME_CELL)) {
                if(value.toString().trim().compareTo("") != 0) {
                    Timestamp ts = stringToTimestamp(String.valueOf(value));//TODO
                    return ts;
                } else {
                    return null;
                }
            } else if(tipoColumna instanceof IntColumn || (celda != null && celda.getType() == INT_CELL)) {
                if(value.toString().trim().compareTo("") != 0) {
                    return Integer.valueOf(value.toString());
                } else
                    return null;
            } else if(tipoColumna instanceof FloatColumn || (celda != null && celda.getType() == FLOAT_CELL)) {
                if(value.toString().trim().compareTo("") != 0) {
                    return Float.valueOf(value.toString());
                } else
                    return null;
            } else if(tipoColumna instanceof OperableTimeColumn) {
                if(((OperableTimeColumn)tipoColumna).isTime(row)) {
                    if(value != null && value.toString().trim().compareTo("") != 0) {
                        Time t = ((OperableTimeColumn)tipoColumna).stringToTime(value.toString());
                        return t;
                    } else
                        return null;
                } else {
                    if(value != null && value.toString().trim().compareTo("") != 0) {
                        Timestamp ts = ((OperableTimeColumn)tipoColumna).stringToTimestamp(value.toString());
                        return ts;
                    } else
                        return null;
                }
            } else if(tipoColumna instanceof ComboColumn) {
                if(((String)value).trim().compareTo("") == 0) {
                    return null;
                }
            }
            return value;
        } else {
            if(tipoColumna instanceof CheckColumn) {
                if(value != null)
                    return value;
                else
                    return Boolean.valueOf(false);
            }
        }
        return value;
    }

	private Date stringToDate(String value) {
		return DateUtil.stringToDate(value);
//		String temp;
//		temp = value.substring(6, 10) + "-" + value.substring(3, 5) + "-" + value.substring(0, 2);
//		
//		Date d = Date.valueOf(temp);
//		return d;
	}

	private Timestamp stringToTimestamp(String value) {//TODO Aca setea el valor de la celda al editar
		Timestamp ts = Timestamp.valueOf(String.valueOf("1970-01-01 " + value + ":00.000000000"));
		return ts;
	}

	public void setValueAt(Object aValue, int row, int column) {
		for(int i = 0; i < tiposColumnas.length; i++) {
			if(column == i && tiposColumnas[i] instanceof OperableTimeColumn)
				((OperableTimeColumn)tiposColumnas[i]).addRow(row, aValue instanceof Time);
		}
		getModel().setValueAt(aValue, row, column);
	}
	
	public void setValueAtSinNotificacionEventos(Object aValue, int row, int column) {
		if (getModel() instanceof FWJTableModel){
			FWJTableModel model = (FWJTableModel)getModel();
			model.setValueAtSinNotificaciones(aValue,row,column);	
		}else {
			throw new UnsupportedOperationException("La operación no es soportada por el modelo de tabla subyacente");
		}
	}
	

	public void setValueAt(int aValue, int row, int column) {
		setValueAt(new Integer(aValue), row, column);
	}

	/**
	 * Lockea una celda.
	 * @param row
	 * @param col
	 */
	public void lockCell(int row, int col) {
		FWCell cellLock = new FWCell(row, col);
		if(lockCells.get(cellLock.getKey()) == null) {
			lockCells.put(cellLock.getKey(), cellLock);
		}
	}
	
	public void lockColumn(int col, boolean locked) {
		tiposColumnas[col].setLock(locked);
	}

	/**
	 * Lockea una celda.
	 * @param row
	 * @param col
	 * @param color
	 */
	public void lockCell(int row, int col, Color color) {
		FWCell cellLock = new FWCell(row, col);
		setBackgroundCell(row, col, color);
		if(lockCells.get(cellLock.getKey()) == null) {
			lockCells.put(cellLock.getKey(), cellLock);
		}
	}

	/**
	 * Deslockea una celda.
	 * @param row
	 * @param col
	 */
	public void unlockCell(int row, int col) {
		String key = row + "," + col;
		if(lockCells.get(key) != null) {
			lockCells.remove(key);
			setBackgroundCell(row, col, getRowDefaultBackground(row));
		}
	}

	public boolean isCellLocked(int row, int col) {
		String key = row + "," + col;
		return (lockCells.get(key) != null);
	}

	/**
	 * Setea el color de fondo de una celda.
	 * @param row
	 * @param col
	 * @param color
	 */
	public void setBackgroundCell(int row, int col, Color color) {
		FWCell cell = new FWCell(row, col);
		cell.setBackgroundColor(color);
		if(backgroundColorCells.get(cell.getKey()) != null)
			backgroundColorCells.remove(cell.getKey());
		backgroundColorCells.put(cell.getKey(), cell);
	}

	/**
	 * Setea el color de la fuente de una celda.
	 * @param row
	 * @param col
	 * @param color
	 */
	public void setForegroundCell(int row, int col, Color color) {
		FWCell cell = new FWCell(row, col);
		cell.setForegroundColor(color);
		if(foregroundColorCells.get(cell.getKey()) != null)
			foregroundColorCells.remove(cell.getKey());
		foregroundColorCells.put(cell.getKey(), cell);
	}

	/**
	 * Setea el color de la fuente de una fila.
	 * @param row
	 * @param color
	 */
	public void setForegroundRow(int row, Color color) {
		for(int i = 0; i < getColumnCount(); i++) {
			setForegroundCell(row, i, color);
		}
	}

	/**
	 * Setea la fuente para una celda.
	 * @param row
	 * @param col
	 * @param fuente
	 */
	public void setFontCell(int row, int col, Font fuente) {
		FWCell cell = new FWCell(row, col);
		cell.setFuente(fuente);
		if(fontCells.get(cell.getKey()) != null)
			fontCells.remove(cell.getKey());
		fontCells.put(cell.getKey(), cell);
	}

	/**
	 * Setea la fuente para una fila.
	 * @param row
	 * @param fuente
	 */
	public void setFontRow(int row, Font fuente) {
		for(int col = 0; col < getColumnCount(); col++) {
			setFontCell(row, col, fuente);
		}
	}

	/**
	 * Setea la alineación de una celda.
	 * @param row
	 * @param col
	 * @param alignment
	 */
	public void setAlignmentCell(int row, int col, int alignment) {
		FWCell cell = new FWCell(row, col);
		cell.setAlignment(alignment);
		if(alignmentCells.get(cell.getKey()) != null)
			alignmentCells.remove(cell.getKey());
		alignmentCells.put(cell.getKey(), cell);
	}

	/**
	 * Setea el texto del tooltip para una celda.
	 * @param row
	 * @param col
	 * @param tooltip
	 */
	public void setTooltipCell(int row, int col, String tooltip) {
		FWCell cell = new FWCell(row, col);
		cell.setTooltip(tooltip);
		if(tooltipCells.get(cell.getKey()) != null)
			tooltipCells.remove(cell.getKey());
		tooltipCells.put(cell.getKey(), cell);
	}

	/**
	 * Setea el texto del tooltip para una fila.
	 * 
	 * @param row
	 * @param tooltip
	 */
	public void setTooltipRow(int row, String toolTip) {
		for(int i = 0; i < this.getColumnCount(); i++) {
			setTooltipCell(row, i, toolTip);
		}
	}

	/**
	 * Setea el tipo de una celda específica.
	 * @param row
	 * @param col
	 * @param type
	 * @param format
	 */
	public void setTypeCell(int row, int col, int type, String format) {
		FWCell cell = new FWCell(row, col);
		cell.setType(type);
		cell.setMask(format);
		if(typesCells.get(cell.getKey()) != null)
			typesCells.remove(cell.getKey());
		typesCells.put(cell.getKey(), cell);
	}

	/**
	 * Retorna el color de fondo de una celda.
	 * @param row
	 * @param column
	 * @return
	 */
	public Color getBackgroundCell(int row, int column) {
		String key = row + "," + column;
		FWCell cell = (FWCell)backgroundColorCells.get(key);
		if(cell != null)
			return cell.getBackgroundColor();
		return null;
	}

	/**
	 * Setea el color de fondo de una fila.
	 * @param index
	 * @param color
	 */
	public void setBackgroundRow(int index, Color color) {
		FWRow fila = new FWRow(index);
		fila.setBackgroundColor(color);
		if(backgroundColorRows.get(fila.getKey()) != null)
			backgroundColorRows.remove(fila.getKey());
		backgroundColorRows.put(fila.getKey(), fila);
	}

	/**
	 * Retorna el color de la fuente de una celda.
	 * @param row
	 * @param col
	 * @return
	 */
	public Color getForegroundCell(int row, int col) {
		String key = row + "," + col;
		FWCell cell = (FWCell)foregroundColorCells.get(key);
		if(cell != null)
			return cell.getForegroundColor();
		return getForeground();
	}

	/**
	 * Retorna el color de fondo de una fila.
	 * @param index
	 * @return
	 */
	public Color getBackgroundRow(int index) {
		Integer key = new Integer(index);
		FWRow row = (FWRow)backgroundColorRows.get(key);
		if(row != null)
			return row.getBackgroundColor();
		return null;
	}

	public Color getRowDefaultBackground(int index) {
		Color colorTable = getBackground();
		if((index % 2) == 0) {
			if(alternativeColor == null && colorTable.getRed() > 10 && colorTable.getGreen() > 10 && colorTable.getBlue() > 10) {
				return new Color(colorTable.getRed() - 10, colorTable.getGreen() - 10, colorTable.getBlue() - 10);
			} else if(alternativeColor != null) {
				return alternativeColor;
			}
		}
		return colorTable;
	}

	/**
	 * Retorna la fuente de una celda.
	 * @param row
	 * @param column
	 * @return
	 */
	public Font getFontCell(int row, int column) {
		String key = row + "," + column;
		FWCell cell = (FWCell)fontCells.get(key);
		if(cell != null)
			return cell.getFuente();
		return getFont();
	}

	/**
	 * Retorna la alineación de una celda.
	 * @param row
	 * @param col
	 * @return
	 */
	public int getAlignmentCell(int row, int col) {
		String key = row + "," + col;
		FWCell cell = (FWCell)alignmentCells.get(key);
		if(cell != null)
			return cell.getAlignment();
		if(tiposColumnas[col].isTextLeftAlign())
			return LEFT_ALIGN;
		else if(tiposColumnas[col].isTextCenterAlign())
			return CENTER_ALIGN;
		else
			return RIGHT_ALIGN;
	}

	/**
	 * Retorna el texto del tooltip de una celda.
	 * @param row
	 * @param column
	 * @return
	 */
	public String getTooltipCell(int row, int column) {
		String key = row + "," + column;
		FWCell cell = (FWCell)tooltipCells.get(key);
		if(cell != null)
			return cell.getTooltip();
		return null;
	}

	/**
	 * Retrona el tipo de una celda.
	 * @param row
	 * @param column
	 * @return
	 */
	public int getTypeCell(int row, int column) {
		String key = row + "," + column;
		FWCell cell = (FWCell)typesCells.get(key);
		if(cell != null)
			return cell.getType();
		return -1;
	}

	/**
	 * Selecciona una celda para el ingreso de datos.
	 * @param row
	 * @param column
	 */
	public void selectCell(int row, int column) {
		setColumnSelectionAllowed(true);
		setRowSelectionAllowed(true);
		if(editCellAt(row, column)) {
			//Selecciona la celda
			boolean toggle = false;
			boolean extend = false;
			changeSelection(row, column, toggle, extend);
			getEditorComponent().requestFocus();
		}
	}

	/** Valida el ingreso de la fecha HH:mm */
	public boolean inputTimeVerifier(char inputDateVerified, FWJFormattedTextField ftf) {
		//Validación de la hora.
		if(((String)("012")).indexOf(inputDateVerified) == -1 && ftf.getCaretPosition() == 0)
			return true;
		if(ftf.getCaretPosition() == 1 && ftf.getText().charAt(0) == '2') {
			if(((String)("0123")).indexOf(inputDateVerified) == -1)
				return true;
			else if(((String)("0123456789")).indexOf(inputDateVerified) == -1)
				return true;
		}
		if(ftf.getCaretPosition() == 2 && ((String)("012345")).indexOf(inputDateVerified) == -1 && ftf.getCaretPosition() == 2)
			return true;
		//Validación de los minutos.
		if(((String)("012345")).indexOf(inputDateVerified) == -1 && ftf.getCaretPosition() == 3)
			return true;
		if(((String)("0123456789")).indexOf(inputDateVerified) == -1 && ftf.getCaretPosition() == 4)
			return true;
		return false;
	}

	/** Devuelve la ultima columna ordenada */
	public int getLastSortedCol() {
		return lastSortedColIndex;
	}

	/** Devuelve <b>true</b> si las filas deben ordenarse en forma ascendente. */
	public boolean sortColsAscending() {
		return sortColsAscending;
	}

	/** Cancela la edición de una celda */
	public void unselectCell() {
		if(getEditingColumn() != -1 && getEditingRow() != -1)
			getCellEditor(getEditingRow(), getEditingColumn()).cancelCellEditing();
	}

	/**
	 * Guarda las filas seleccionadas.
	 * @param colClave La columna en la cual se encuentran las claves que identifican unívocamente a cada fila.
	 */
	public void guardarSeleccion(int colClave) {
		this.colClave = colClave;
		int[] filasSeleccionadas = getSelectedRows();
		clavesSeleccionadas.clear();
		for(int i = 0; i < filasSeleccionadas.length; i++)
			clavesSeleccionadas.add(getValueAt(filasSeleccionadas[i], colClave));
	}

	/**
	 * Selecciona las filas que se encontraban seleccionadas al invocar el
	 * metodo guardarSeleccion.
	 */
	public void restaurarSeleccion() {
		for(Iterator i = clavesSeleccionadas.iterator(); i.hasNext();) {
			int fila = getFirstRowWithValue(colClave, i.next());
			if(fila != -1)
				addRowSelectionInterval(fila, fila);
		}
	}

	public class FWJTableModel extends DefaultTableModel {
		public FWJTableModel() {
			super();
		}

		public FWJTableModel(int rowCount, int columnCount) {
			//Se inicializan los vectores vacíos y con el tamaño especificado
			super(new Object[rowCount][columnCount], new Object[columnCount]);
		}

		
		
		/**
		 * Sets the object value for the cell at <code>column</code> and
		 * <code>row</code>.<code>aValue</code> is the new value. This
		 * method will generate a <code>tableChanged</code> notification.
		 * @param value
		 * @param row
		 * @param col
		 */
		@SuppressWarnings("unchecked")
		public void setValueAt(Object value, int row, int col) {
			if(row >= 0 && row < ((Vector)dataVector).size()) {
				Vector<Object> rowVector = (Vector)dataVector.elementAt(row);
				if(validarValorColumna(value, col)) {
					rowVector.setElementAt(value, col);
					fireTableCellUpdated(row, col);
				} else
					rowVector.setElementAt(value, col);
					//rowVector.setElementAt(rowVector.get(col), col);
			}
		}
		
		/**
		 * Sets the object value for the cell at <code>column</code> and
		 * <code>row</code>.<code>aValue</code> is the new value. This
		 * method will NOT generate a <code>tableChanged</code> notification.
		 * @param value
		 * @param row
		 * @param col
		 */
		@SuppressWarnings("unchecked")
		public void setValueAtSinNotificaciones(Object value, int row, int col) {
			if(row >= 0 && row < ((Vector)dataVector).size()) {
				Vector<Object> rowVector = (Vector)dataVector.elementAt(row);
				if(validarValorColumna(value, col)) {
					rowVector.setElementAt(value, col);
					//fireTableCellUpdated(row, col);
				} else
					rowVector.setElementAt(value, col);
					//rowVector.setElementAt(rowVector.get(col), col);
			}
		}
		

		/**
		 * Devuelve <b>true</b> si la celda es editable.
		 * @param row
		 * @param col
		 * @return
		 */
		public boolean isCellEditable(int row, int col) {
			if(tiposColumnas == null || col >= tiposColumnas.length)
				return false;
			else if(tableLock)
				return false;
			else if(tiposColumnas[col].isLock())
				return false;
			else if(lockCells.get(row + "," + col) == null)
				return true;
			else
				return false;
		}

		public Vector getDataVector() {
			return dataVector;
		}

		public void clear() {
			dataVector.removeAllElements();
			fireTableDataChanged();
		}

		public void removeRow(int i) {
			dataVector.removeElementAt(i);
			fireTableRowsDeleted(i, i);
		}
	}

	/**
	 * Valida el valor de una columna.
	 * @param value
	 * @param col
	 * @return
	 */
	private boolean validarValorColumna(Object value, int col) {
		FWColumn c;
		StringColumn sc;
		if(tiposColumnas[col] == null) {
			sc = new StringColumn();
			sc.setNombre(getColumn(col).getHeaderValue().toString());
			sc.setAncho(getColumn(col).getPreferredWidth());
			sc.setLock(false);
			tiposColumnas[col] = sc;
		}
		c = tiposColumnas[col];
		if(c instanceof StringColumn) {
			sc = (StringColumn)c;
			if(value == null || value.toString().length() > sc.getSize())
				return false;
			else
				return true;
		}
		return true;
	}

	//Métodos para accesos más rápidos a determinados valores */
	protected TableColumn getColumn(int col) {
		if(col < 0)
			return null;
		if(col > getColumnModel().getColumnCount())
			return null;
		if(col == getColumnModel().getColumnCount())
			getColumnModel().addColumn(new TableColumn());
		return getColumnModel().getColumn(col);
	}

	protected Vector getDataVector() {
		return tableModel.getDataVector();
	}

	/** Clase StringEditor */
	private class StringEditor extends KeyEditor {
		FWJFormattedTextField ftf;
		StringListener stringListener;

		public StringEditor(FWJTable t, int size) {
			super(t);
			ftf = (FWJFormattedTextField)getComponent();
			stringListener = new StringListener(ftf, size);
			ftf.addKeyListener(stringListener);
		}

		//Retorna el KeyListener del editor
		public KeyListener getKeyListener() {
			return stringListener;
		}
	}

	/** Clase MultilineStringEditor */
	protected class MultilineStringEditor extends AbstractCellEditor implements TableCellEditor {
		JTextArea textArea;

		public MultilineStringEditor() {
			textArea = new JTextArea();
		}

		public Object getCellEditorValue() {
			return textArea.getText();
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			textArea.setText((String)value);
			return textArea;
		}
	}

	/** Clase IntegerEditor */
	protected class IntegerEditor extends KeyEditor {
		protected FWJFormattedTextField ftf;
		NumberListener numberListener;
		boolean nulleable = false;
		public IntegerEditor(FWJTable t, int min, int max) {
			super(t);
			ftf = (FWJFormattedTextField)getComponent();
			numberListener = new NumberListener(ftf, min, max);
			ftf.addKeyListener(numberListener);
		}

		public IntegerEditor(FWJTable t, int min, int max, boolean nulleable) {
			super(t);
			this.nulleable = nulleable;
			ftf = (FWJFormattedTextField)getComponent();
			numberListener = new NumberListener(ftf, min, max);
			ftf.addKeyListener(numberListener);
		}

		public boolean stopCellEditing() {
			if(getCellEditorValue() != null) {
				String s = getCellEditorValue().toString();
				if(s.equals("")) {
					if(!nulleable)
						ftf.setText("0");
				} else {
					String temp = sacarCerosIzquierda(s);
					if(temp.equals(""))
						ftf.setText("0");
					else
						ftf.setText(temp);
				}
			}
			return super.stopCellEditing();
		}

		//Retorna el KeyListener del editor
		public KeyListener getKeyListener() {
			return numberListener;
		}
	}

	/** Clase IntegerEditor Float*/
	private class FloatEditor extends KeyEditor {
		FWJFormattedTextField ftf;
		FloatListener numberListener;

		public FloatEditor(FWJTable t, float min, float max) {
			super(t);
			ftf = (FWJFormattedTextField)getComponent();
			numberListener = new FloatListener(ftf, min, max);
			ftf.addKeyListener(numberListener);
		}


		public boolean stopCellEditing() {
			if(getCellEditorValue() != null) {
				String s = getCellEditorValue().toString();
				if(s.equals(""))
					ftf.setText("0");
				else {
					String temp = sacarCerosIzquierda(s);
					if(temp.equals(""))
						ftf.setText("0");
					else
						ftf.setText(temp);
				}
			}
			return super.stopCellEditing();
		}

		//Retorna el KeyListener del editor
		public KeyListener getKeyListener() {
			return numberListener;
		}
	}

	
	/** Clase DateEditor */
	private class DateEditor extends KeyEditor {
		FWJFormattedTextField ftf;
		DateListener dateListener;

		public DateEditor(FWJTable t) {
			super(t);
			ftf = (FWJFormattedTextField)getComponent();
			dateListener = new DateListener(ftf);
			ftf.addKeyListener(dateListener);
			delegate = new EditorDelegate() {
				public void setValue(Object valor) {
					if(valor instanceof Date) {
						Date fecha = (Date)valor;
						if(fecha != null) {
							//SimpleDateFormat sdf = new SimpleDateFormat(tableDateMask);
							SimpleDateFormat sdf = DateUtil.getSimpleDateFormat(tableDateMask);
							String fechaFormateada = sdf.format(fecha);
							ftf.setValue(fechaFormateada);
						}
					}
				}

				public Object getCellEditorValue() {
					try {
						//SimpleDateFormat sdf = new SimpleDateFormat(tableDateMask);
						SimpleDateFormat sdf = DateUtil.getSimpleDateFormat(tableDateMask);
						if(ftf.getText().trim().compareTo("") == 0)
							return null;
						String fechaFormateada = ftf.getText();
						Date fecha = new Date(sdf.parse(fechaFormateada).getTime());
						 return fecha;
					} catch(ParseException  e) {
						return cellOldValue;
					}
				}
			};
		}

		public boolean stopCellEditing() {
			String s = ftf.getText();
			if(!isValid(s)) {
				FWJOptionPane.showWarningMessage(null, "El campo admite una fecha en formato '" + getDateMask() + "'.", "Error");
				return false;
			}
			return super.stopCellEditing();
		}

		boolean isValid(String s) {
			if(s.equals(""))
				return true;
			try {
				//SimpleDateFormat sdf = new SimpleDateFormat(tableDateMask);
				SimpleDateFormat sdf = DateUtil.getSimpleDateFormat(tableDateMask);
				sdf.parse(s).getTime();
				return true;
			} catch(ParseException e) {
				return false;
			}
		}

		//Retorna el KeyListener del editor
		public KeyListener getKeyListener() {
			return dateListener;
		}
	}

	/** Clase TimeEditor */
	private class TimeEditor extends KeyEditor {
		FWJFormattedTextField ftf;
		TimeListener timeListener;

		public TimeEditor(FWJTable t) {
			super(t);
			ftf = (FWJFormattedTextField)getComponent();
			timeListener = new TimeListener(ftf);
			ftf.addKeyListener(timeListener);
		}

		public boolean stopCellEditing() {
			String s = (String)getCellEditorValue();
			if(!isValid(s)) {
//				CLJOptionPane.showWarningMessage(null, "El campo admite una hora en formato 'HH:MM'.", "Error");
				return false;
			}
			return super.stopCellEditing();
		}

		boolean isValid(String s) {//TODO
			if(s.compareTo("") == 0)
				return true;
			try {
				//DateFormat formatter = new SimpleDateFormat("HH:mm");
				DateFormat formatter = DateUtil.getSimpleDateFormat("HH:mm");
				formatter.parse(s);
				return true;
			} catch(ParseException e) {
				return false;
			}
		}

		//Retorna el KeyListener del editor.
		public KeyListener getKeyListener() {
			return timeListener;
		}
	}

	/** Clase listener del StringEditor */
	private class StringListener implements KeyListener {
		private FWJFormattedTextField ftf;
		private int size;

		public StringListener(FWJFormattedTextField ftf, int size) {
			this.ftf = ftf;
			this.size = size;
		}

		public void keyPressed(KeyEvent evt) {
		}

		public void keyReleased(KeyEvent evt) {
			if(ftf.getText().length() > size)
				ftf.setText(ftf.getText().substring(0, size));
		}

		public void keyTyped(KeyEvent evt) {
			if(ftf.getText().length() == size)
				evt.consume();
		}
	}

	/** Clase listener del IntegerEditor */
	private class NumberListener implements KeyListener {
		private FWJFormattedTextField ftf;
		private int max, min, maxLength;

		public NumberListener(FWJFormattedTextField ftf, int min, int max) {
			this.ftf = ftf;
			this.max = max;
			this.min = min;
			maxLength = new Integer(max).toString().length();
		}

		public void keyPressed(KeyEvent evt) {
		}

		public void keyReleased(KeyEvent evt) {
			int i;
			String valor = sacarCerosIzquierda(ftf.getText());
			if(ftf.getText().length() != 0) {
				if(!valor.trim().equals("")) {
					i = Integer.parseInt(valor);
					if(valor.length() > maxLength)
						ftf.setText(ftf.getText().substring(0, ftf.getText().length() - 1));
					else if(i > max)
						ftf.setText(String.valueOf(max));
					else if(i < min)
						ftf.setText(String.valueOf(min));
				} else {
					if(0 > max)
						ftf.setText(String.valueOf(max));
					else if(0 < min)
						ftf.setText(String.valueOf(min));
				}
			} 
		}

		public void keyTyped(KeyEvent evt) {
			if(evt.getKeyChar() != '\b') {
				if(sacarCerosIzquierda(ftf.getText()).length() > maxLength)
					evt.consume();
				if(((String)("0123456789")).indexOf(evt.getKeyChar()) == -1)
					evt.consume();
			}
		}
	}

	/** Clase listener del IntegerEditor */
	private class FloatListener implements KeyListener {
		private FWJFormattedTextField ftf;
		private int maxLength;
		private float maxf, minf;

		public FloatListener(FWJFormattedTextField ftf, float min, float max) {
			this.ftf = ftf;
			this.maxf = max;
			this.minf = min;
			maxLength = new Float(max).toString().length();
		}

		public void keyPressed(KeyEvent evt) {
		}

		public void keyReleased(KeyEvent evt) {
			float i;
			String valor = sacarCerosIzquierda(ftf.getText());
			if(ftf.getText().length() != 0) {
				if(!valor.equals("")) {
					try {
						i = Float.parseFloat(valor);
						if(valor.length() > maxLength) {
							ftf.setText(ftf.getText().substring(0, ftf.getText().length() - 1));							
						} else if(i > maxf) {
							ftf.setText(ftf.getText().substring(0, ftf.getText().length() - 1));							
						} else if(i < minf) {
							ftf.setText(String.valueOf(minf));							
						}
					} catch(NumberFormatException e) {
						ftf.setText(valor.substring(0, valor.length() - 1));
					}
				}
			}
		}

		public void keyTyped(KeyEvent evt) {
			/*
			if(evt.getKeyChar() != '\b') {
				if(sacarCerosIzquierda(ftf.getText()).length() > maxLength)
					evt.consume();
				if(((String)("0123456789")).indexOf(evt.getKeyChar()) == -1)
					evt.consume();
			}
			*/
		}
	}

	/** Clase listener del DateEditor */
	private class DateListener implements KeyListener {
		private FWJFormattedTextField ftf;

		public DateListener(FWJFormattedTextField ftf) {
			this.ftf = ftf;
		}

		public void keyReleased(KeyEvent evt) {
			if(ftf.getText().length() == sepDateMask1 || ftf.getText().length() == sepDateMask2)
				ftf.setText(ftf.getText().concat("/"));
			if(ftf.getText().length() > tableDateMask.length())
				ftf.setText(ftf.getText().substring(0, tableDateMask.length()));
		}

		public void keyTyped(KeyEvent evt) {
			if(evt.getKeyChar() != '\b') {
				if(ftf.getText().length() == tableDateMask.length()) {
					evt.consume();
				}
				if(((String)("0123456789/")).indexOf(evt.getKeyChar()) == -1) {
					evt.consume();
				}
				if(((String)("/")).indexOf(evt.getKeyChar()) != -1) {
					if(!(ftf.getText().length() == sepDateMask1 || ftf.getText().length() == sepDateMask2)) {
						evt.consume();
					}
				} else {
					if((ftf.getText().length() == sepDateMask1 || ftf.getText().length() == sepDateMask2)) {
						evt.consume();
					}
				}
			}
		}

		public void keyPressed(KeyEvent evt) {
		}
	}

	/** Clase listener del TimeEditor */
	private class TimeListener implements KeyListener {
		private FWJFormattedTextField ftf;

		public TimeListener(FWJFormattedTextField ftf) {
			this.ftf = ftf;
		}

		public void keyReleased(KeyEvent evt) {
			if(ftf.getText().length() == sepTimeMask1 || ftf.getText().length() == sepTimeMask2)
				if(ftf.getText().indexOf(':') == -1 && evt.getKeyChar()!= '\b' && evt.getKeyCode()!= KeyEvent.VK_LEFT) {
					ftf.setText(ftf.getText().concat(":"));
				}
			if(ftf.getText().length() > tableTimeMask.length())
				ftf.setText(ftf.getText().substring(0, tableTimeMask.length()));
		}

		public void keyTyped(KeyEvent evt) {
			if(evt.getKeyChar() != '\b') {
				if(inputTimeVerifier(evt.getKeyChar(), ftf))
					evt.consume();
				if(ftf.getText().length() == tableTimeMask.length() && ftf.getSelectedText() != null && ftf.getSelectedText().length() == tableTimeMask.length()){
					ftf.setText("");
					if(!inputTimeVerifier(evt.getKeyChar(), ftf)){
						ftf.setText(evt.getKeyChar() + "");
					}
					evt.consume();
				}
				if(((String)("0123456789:")).indexOf(evt.getKeyChar()) == -1){	
					evt.consume();
				}
				
				if(ftf.getText().length() == 2 && !inputTimeVerifier(evt.getKeyChar(), ftf)){
					ftf.setText(ftf.getText().concat(":" + evt.getKeyChar()));
					evt.consume();
				}
				if(((String)(":")).indexOf(evt.getKeyChar()) != -1)
					if(!(ftf.getText().length() == sepTimeMask1 || ftf.getText().length() == sepTimeMask2))
						evt.consume();
			}
		}

		public void keyPressed(KeyEvent evt) {
		}
	}

	/** Clase utilizada para almacenar una celda */
	private class FWCell {
		private int row;
		private int column;
		private Font fuente;
		private Color backgroundColor;
		private Color foregroundColor;
		private String tooltip;
		private int alignment;
		private int type;
		private String mask;

		public FWCell(int row, int column) {
			this.row = row;
			this.column = column;
		}

		public int getRow() {
			return row;
		}

		public int getColumn() {
			return column;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public void setColumn(int column) {
			this.column = column;
		}

		public String getKey() {
			return row + "," + column;
		}

		public Color getBackgroundColor() {
			return backgroundColor;
		}

		public void setBackgroundColor(Color backgroundColor) {
			this.backgroundColor = backgroundColor;
		}

		public String getTooltip() {
			return tooltip;
		}

		public void setTooltip(String tooltip) {
			this.tooltip = tooltip;
		}

		public Font getFuente() {
			return fuente;
		}

		public void setFuente(Font fuente) {
			this.fuente = fuente;
		}

		public int getAlignment() {
			return alignment;
		}

		public void setAlignment(int alignment) {
			this.alignment = alignment;
		}

		public Color getForegroundColor() {
			return foregroundColor;
		}

		public void setForegroundColor(Color foregroundColor) {
			this.foregroundColor = foregroundColor;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public String getMask() {
			return mask;
		}

		public void setMask(String mask) {
			this.mask = mask;
		}
	}

	/** Clase utilizada para almacenar una fila */
	private class FWRow {
		private int index;
		private Color backgroundColor;
		private boolean time;

		public FWRow(int index) {
			this.index = index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public void setBackgroundColor(Color color) {
			this.backgroundColor = color;
		}

		public Color getBackgroundColor() {
			return backgroundColor;
		}

		public boolean isTime() {
			return time;
		}

		public void setTime(boolean time) {
			this.time = time;
		}

		public Integer getKey() {
			return new Integer(index);
		}
	}

	private class ComboBoxEditor extends DefaultCellEditor {
		public ComboBoxEditor(JComboBox combo) {
            super(combo);
            combo.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent evt) {
					if(evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
						TableCellEditor editor = getCellEditor(getSelectedRow(), getSelectedColumn());
						if(editor != null)
							editor.stopCellEditing();
						((JComboBox)evt.getSource()).setSelectedItem(cellOldValue);
					} else if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
						if(getSelectedRow() != -1 && getSelectedColumn() != -1) {
							TableCellEditor editor = getCellEditor(getSelectedRow(), getSelectedColumn());
							if(editor != null)
								editor.stopCellEditing();
							teclaEnterPresionada(getSelectedRow(), getSelectedColumn());
						}		
					} else if(evt.getKeyCode() == KeyEvent.VK_LEFT || evt.getKeyCode() == KeyEvent.VK_RIGHT || evt.getKeyCode() == KeyEvent.VK_UP) {
						teclaFlechaPresionada(evt.getKeyCode(), getSelectedRow(), getSelectedColumn());
					}
				}
			});          
		}
    }

	/** Clase editor que se habilita al presionar una tecla */
	private abstract class KeyEditor extends DefaultCellEditor {
		int row;
		int col;

		public KeyEditor(FWJTable t) {
			super(new FWJFormattedTextField(t));
			clickCountToStart = 1;
		}

		public boolean isCellEditable(EventObject evt) {
			if(evt != null && evt instanceof MouseEvent) {
				row = rowAtPoint(((MouseEvent)evt).getPoint());
				col = columnAtPoint(((MouseEvent)evt).getPoint());
				cellOldValue = getValueAt(row, col);
				lastEditedRow = row;
				((FWJFormattedTextField)getComponent()).setText(null);
				return true;
			}
//			row = getSelectedRow();
//			col = getSelectedColumn();
//			cellOldValue = getValueAt(row, col);
//			lastEditedRow = row;
//			setValueAt(null, row, col);
			return true;
		}

		public abstract KeyListener getKeyListener();
	}

	protected void teclaEnterPresionada(int fila, int col) {
		final int columnaActual = col;
		int columnaSiguiente = -1;
		final int filaActual = fila;
		int filaSiguiente = -1;
		int filaTemp;
		int colTemp;
		if(llenado == LLENADO_HORIZONTAL) {
			if(columnaActual < getColumnCount() - 1) {
				colTemp = columnaActual + 1;
				filaTemp = filaActual;
			} else {
				colTemp = 0;
				filaTemp = filaActual + 1;
			}
			if(filaTemp < getRowCount()) {
				for(int i = filaTemp; i < getRowCount(); i++) {
					for(int j = colTemp; j < getColumnCount(); j++) {
						if(getColumnWidth(j) > 0 && lockCells.get(i + "," + j) == null && !tiposColumnas[j].isLock()) {
						//if(getColumnWidth(j) > 0 && lockCells.get(i + "," + j) == null && !tiposColumnas[j].isLock() && !(((DefaultCellEditor)getCellEditor(i, j)).getComponent() instanceof JComboBox)) {
							filaSiguiente = i;
							columnaSiguiente = j;
							break;
						}
					}
					colTemp = 0;
					if(filaSiguiente != -1 && columnaSiguiente != -1) {
						break;
					}
				}
			}
		} else if(llenado == LLENADO_VERTICAL) {
			if(filaActual < getRowCount() - 1) {
				colTemp = columnaActual;
				filaTemp = filaActual + 1;
			} else {
				colTemp = columnaActual + 1;
				filaTemp = 0;
			}
			if(colTemp < getColumnCount()) {
				for(int i = colTemp; i < getColumnCount(); i++) {
					for(int j = filaTemp; j < getRowCount(); j++) {
						//if(getColumnWidth(i) > 0 && lockCells.get(j + "," + i) == null && !tiposColumnas[i].isLock() && !(((DefaultCellEditor)getCellEditor(j, i)).getComponent() instanceof JComboBox)) {
						if(getColumnWidth(i) > 0 && lockCells.get(j + "," + i) == null && !tiposColumnas[i].isLock()) {
							filaSiguiente = j;
							columnaSiguiente = i;
							break;
						}
					}
					filaTemp = 0;
					if(filaSiguiente != -1 && columnaSiguiente != -1) {
						break;
					}
				}
			}
		}
		if(llenado != LLENADO_NINGUNO) {
			if(filaSiguiente != -1 && columnaSiguiente != -1) {
				final int filaASeleccionar = filaSiguiente;
				final int columnaASeleccionar = columnaSiguiente;
				final Component componenteSiguiente = ((DefaultCellEditor)getCellEditor(filaASeleccionar, columnaASeleccionar)).getComponent();
				final Component componenteActual = ((DefaultCellEditor)getCellEditor(filaActual, columnaActual)).getComponent();
				
				if (componenteActual instanceof JComboBox){
					((DefaultCellEditor)getCellEditor(filaActual, columnaActual)).stopCellEditing();
					editCellAt(filaASeleccionar, columnaASeleccionar);
					setValueAt(((JComboBox)componenteActual).getSelectedItem(), filaActual, columnaActual);					
				} else{
					//if (!(componenteActual instanceof JCheckBox))
						editCellAt(filaASeleccionar, columnaASeleccionar);
				}
				SwingUtilities.invokeLater(new Runnable(){ 
					public void run(){
						Rectangle cellRect = getCellRect(filaASeleccionar, columnaASeleccionar, true);
						if(cellRect != null) {
							scrollRectToVisible(cellRect);
						}
						//if(componenteActual instanceof JCheckBox) {
						//	logger.debug("valorActual:" + CLJTable.this.getValueAt(filaActual, columnaActual));
						//	return;
						//}
						editCellAt(filaASeleccionar, columnaASeleccionar);
						setRowSelectionInterval(filaASeleccionar, filaASeleccionar);
						setColumnSelectionInterval(columnaASeleccionar, columnaASeleccionar);
						componenteSiguiente.requestFocus();
						if(componenteActual instanceof JComboBox) {
							setValueAt(((JComboBox)componenteActual).getSelectedItem(), filaActual, columnaActual);
						}
					}
				});					
			}
		} 
	}

	private void teclaFlechaPresionada(int codTecla, final int fila, final int col) {
		int filaASeleccionar = -1;
		int columnaASeleccionar = -1;
		if(codTecla == KeyEvent.VK_UP) {
			if(fila > 0) {
				for(int i = fila - 1; i >= 0; i--) {
					if(lockCells.get(i + "," + col) == null) {
						filaASeleccionar = i;
						columnaASeleccionar = col;
						break;
					}
				}
			}
		} else if(codTecla == KeyEvent.VK_DOWN) {
			if(fila < getRowCount() - 1) {
				for(int i = fila + 1; i < getRowCount(); i++) {
					if(lockCells.get(i + "," + col) == null) {
						filaASeleccionar = i;
						columnaASeleccionar = col;
						break;
					}
				}
			}
		} else if(codTecla == KeyEvent.VK_LEFT) {
			if(col > 0) {
				for(int i = col - 1; i >= 0; i--) {
					if(lockCells.get(fila + "," + i) == null && getColumnWidth(i) != 0) {
						filaASeleccionar = fila;
						columnaASeleccionar = i;
						break;
					}
				}
			}
		} else if(codTecla == KeyEvent.VK_RIGHT) {
			if(col < getColumnCount() - 1) {
				for(int i = col + 1; i < getColumnCount(); i++) {
					if(lockCells.get(fila + "," + i) == null && getColumnWidth(i) != 0) {
						filaASeleccionar = fila;
						columnaASeleccionar = i;
						break;
					}
				}
			}
		}
		final int f = filaASeleccionar;
		final int c = columnaASeleccionar;
		if(filaASeleccionar != -1 && columnaASeleccionar != -1) {
			final Component componenteSiguiente = ((DefaultCellEditor)getCellEditor(filaASeleccionar, columnaASeleccionar)).getComponent();
			final Component componenteActual = ((DefaultCellEditor)getCellEditor(fila, col)).getComponent();
			if(!(componenteActual instanceof JComboBox)) {
				editCellAt(filaASeleccionar, columnaASeleccionar);
			} else {
				((DefaultCellEditor)getCellEditor(fila, col)).stopCellEditing();
				editCellAt(filaASeleccionar, columnaASeleccionar);
				setValueAt(((JComboBox)componenteActual).getSelectedItem(), fila, col);
			}
			SwingUtilities.invokeLater(new Runnable(){ 
				public void run(){
					editCellAt(f, c);
					setRowSelectionInterval(f, f);
					setColumnSelectionInterval(c, c);
					componenteSiguiente.requestFocus();
					if(componenteActual instanceof JComboBox) {
						setValueAt(((JComboBox)componenteActual).getSelectedItem(), fila, col);
					}
				}
			});
		}
	}

	public abstract class FWColumn {
		
		private int indice;
		private int indiceOriginal;
		private String nombre;
		private int ancho;
		private boolean lock;
		private int anchoOriginal;
		private boolean oculta;
		private boolean textLeftAlign;
		private boolean textRightAlign;
		private boolean textCenterAlign;
		private boolean headerLeftAlign;
		private boolean headerRightAlign;
		private boolean headerCenterAlign;

		public abstract String getMask();
		
		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public int getAncho() {
			return ancho;
		}

		public void setAncho(int ancho) {
			if(!oculta)
				this.ancho = ancho;
			else
				this.ancho = 0;
		}

		public int getAnchoOriginal() {
			return anchoOriginal;
		}

		public void setAnchoOriginal(int anchoOriginal) {
			this.anchoOriginal = anchoOriginal;
		}

		/**
		 * Retorna el estado de lockeo de la columna.
		 * @return lock
		 */
		public boolean isLock() {
			return lock;
		}

		/**
		 * Establece el estado de lockeo de la columna.
		 * @param estado
		 */
		public void setLock(boolean estado) {
			lock = estado;
		}

		/**
		 * Retorna <b>true</b> si la columna está visible.
		 * @return
		 */
		public boolean isVisible() {
			return ancho != 0;
		}

		/**
		 * Establece el estado de la columna en visible/oculta.
		 * @param visible
		 */
		public void setVisible(boolean visible) {
			if(visible) {
				ancho = anchoOriginal;
			} else {
				ancho = 0;
			}
			setColumnWidth(indice, ancho);
		}

		/**
		 * Retorna el índice.
		 * @return indice
		 */
		public int getIndice() {
			return indice;
		}

		/**
		 * Setea el índice.
		 * @param indice
		 */
		public void setIndice(int indice) {
			this.indice = indice;
		}

		public void setOculta(boolean oculta) {
			this.oculta = oculta;
			if(oculta) {				
				ancho = 0;
			} else {
				ancho = anchoOriginal;	
			}
			getColumnModel().getColumn(indice).setMaxWidth(ancho);
			getTableHeader().getColumnModel().getColumn(indice).setMaxWidth(ancho);			
			setColumnWidth(indice, ancho);
		}

		public boolean isOculta() {
			return oculta;
		}

		public boolean isTextLeftAlign() {
			return textLeftAlign;
		}

		public void setTextLeftAlign(boolean leftAlign) {
			this.textLeftAlign = leftAlign;
			if(leftAlign) {
				setTextRightAlign(false);
				setTextCenterAlign(false);
			}
		}

		public boolean isTextRightAlign() {
			return textRightAlign;
		}

		public void setTextRightAlign(boolean rightAlign) {
			this.textRightAlign = rightAlign;
			if(rightAlign) {
				setTextLeftAlign(false);
				setTextCenterAlign(false);
			}
		}

		public boolean isTextCenterAlign() {
			return textCenterAlign;
		}

		public void setTextCenterAlign(boolean centerAlign) {
			this.textCenterAlign = centerAlign;
			if(centerAlign) {
				setTextLeftAlign(false);
				setTextRightAlign(false);
			}
		}

		public boolean isHeaderLeftAlign() {
			return headerLeftAlign;
		}

		public void setHeaderLeftAlign(boolean headerLeftAlign) {
			this.headerLeftAlign = headerLeftAlign;
			if(this.headerLeftAlign) {
				setHeaderCenterAlign(false);
				setHeaderRightAlign(false);
			}
		}

		public boolean isHeaderRightAlign() {
			return headerRightAlign;
		}

		public void setHeaderRightAlign(boolean headerRightAlign) {
			this.headerRightAlign = headerRightAlign;
			if(headerRightAlign) {
				setHeaderLeftAlign(false);
				setHeaderCenterAlign(false);
			}
		}

		public boolean isHeaderCenterAlign() {
			return headerCenterAlign;
		}

		public void setHeaderCenterAlign(boolean hederCenterAlign) {
			this.headerCenterAlign = hederCenterAlign;
			if(headerCenterAlign) {
				setHeaderLeftAlign(false);
				setHeaderRightAlign(false);
			}
		}

		public int getHeaderAlign() {
			if(headerRightAlign)
				return RIGHT_ALIGN;
			else if(headerCenterAlign)
				return CENTER_ALIGN;
			return LEFT_ALIGN;
		}

		
		public int getIndiceOriginal() {
			return indiceOriginal;
		}

		
		public void setIndiceOriginal(int indiceOriginal) {
			this.indiceOriginal = indiceOriginal;
		}
	}

	/** Clase StringColumn */
	public final class StringColumn extends FWColumn {
		private int size;
		private String filtro;

		public StringColumn() {
			setTextLeftAlign(true);
		}

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public String getFiltro() {
			return filtro;
		}

		public void setFiltro(String filtro) {
			this.filtro = filtro;
		}

		public String getMask() {
			return null;
		}
	}

	/** Clase MultilineColumn */
	public final class MultilineColumn extends FWColumn {
		private int size;
		private String filtro;
		private MultiLineCellRenderer multiLineCellRenderer;
		
		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public String getFiltro() {
			return filtro;
		}

		public void setFiltro(String filtro) {
			this.filtro = filtro;
		}

		public String getMask() {
			return null;
		}
		
		public void setMultiLineCellRenderer(MultiLineCellRenderer multiLineCellRenderer) {
			this.multiLineCellRenderer = multiLineCellRenderer;
		}
		
		public MultiLineCellRenderer getMultiLineCellRenderer() {
			return multiLineCellRenderer;
		}
	}

	/** Clase ComboColumn */
	public final class ComboColumn extends FWColumn {
		private JComboBox combo;

		public JComboBox getCombo() {
			return combo;
		}

		public void setCombo(JComboBox combo) {
			this.combo = combo;
		}

		public String getMask() {
			return null;
		}
	}

	/** Clase CheckColumn */
	public final class CheckColumn extends FWColumn {
		private JCheckBox check;

		public JCheckBox getCheck() {
			return check;
		}

		public void setCheck(JCheckBox check) {
			this.check = check;
		}

		public String getMask() {
			return null;
		}
	}

	/** Clase IntColumn */
	public final class IntColumn extends FWColumn {
		private int minValue;
		private int maxValue;
		private String mask;

		public IntColumn() {
			setTextRightAlign(true);
		}

		public int getMinValue() {
			return minValue;
		}

		public void setMinValue(int minValue) {
			this.minValue = minValue;
		}

		public int getMaxValue() {
			return maxValue;
		}

		public void setMaxValue(int maxValue) {
			this.maxValue = maxValue;
		}

		public String getMask() {
			return mask;
		}

		public void setMask(String mask) {
			this.mask = mask;
		}
	}

	/** Clase DateColumn */
	public final class DateColumn extends FWColumn {
		private Date minDate;
		private Date maxDate;
		private String mask;

		public DateColumn() {
			setTextCenterAlign(true);
		}

		public Date getMinDate() {
			return minDate;
		}

		public void setMinDate(Date minDate) {
			this.minDate = minDate;
		}

		public Date getMaxDate() {
			return maxDate;
		}

		public void setMaxDate(Date maxDate) {
			this.maxDate = maxDate;
		}

		public String getMask() {
			return mask;
		}

		public void setMask(String mask) {
			this.mask = mask;
		}
	}

	/** Clase TimeColumn */
	public final class TimeColumn extends FWColumn {
		private String mask;

		public String getMask() {
			return mask;
		}

		public void setMak(String mask) {
			this.mask = mask;
		}
	}

	/** Clase FloatColumn */
	public final class FloatColumn extends FWColumn {
		private float minValue;
		private float maxValue;
		private String mask;

		public FloatColumn() {
			setTextRightAlign(true);
		}

		public float getMinValue() {
			return minValue;
		}

		public void setMinValue(float minValue) {
			this.minValue = minValue;
		}

		public float getMaxValue() {
			return maxValue;
		}

		public void setMaxValue(float maxValue) {
			this.maxValue = maxValue;
		}

		public String getMask() {
			return mask;
		}

		public void setMask(String mask) {
			this.mask = mask;
		}
	}
	
	
	public final class OperableTimeColumn extends FWColumn {
		private Hashtable<Integer, FWRow> tipos = new Hashtable<Integer, FWRow>();
		private String mask;
		private boolean difFechas;

		public OperableTimeColumn() {
			setTextCenterAlign(true);
		}

		public void addRow(int row, boolean time) {
			FWRow clRow = new FWRow(row);
			clRow.setTime(time);
			tipos.put(clRow.getKey(), clRow);
		}

		public boolean isTime(int row) {
			for(Iterator i = tipos.values().iterator(); i.hasNext();) {
				FWRow clRow = (FWRow)i.next();
				if(clRow.getIndex() == row)
					return clRow.isTime();
			}
			return false;
		}

		public String getMask() {
			return mask;
		}

		public void setMask(String mask) {
			this.mask = mask;
		}

		public boolean isDifFechas() {
			return difFechas;
		}

		public void setDifFechas(boolean difFechas) {
			this.difFechas = difFechas;
		}

		public Timestamp stringToTimestamp(String value) {
			Timestamp ts;
			if(value.trim().length() <= 5)
				ts = Timestamp.valueOf(String.valueOf("1970-01-01 " + value + ":00.000000000"));
			else {
				String hours = value.substring(11, value.length());
				ts = Timestamp.valueOf(dateToSqlDate(value.substring(0, 10)) + " " + hours + ":00.000000000");
			}
			return ts;
		}

		private String dateToSqlDate(String date) {
			String sqlDate = date.substring(6, date.length());
			sqlDate += "-" + date.substring(3, 5);
			sqlDate += "-" + date.substring(0, 2);
			return sqlDate;
		}

		public Time stringToTime(String value) {
			Time t = Time.valueOf(value + ":00");
			return t;
		}

		public Hashtable getTipos() {
			return tipos;
		}

		public void setTipos(Hashtable<Integer, FWRow> tipos) {
			this.tipos = tipos;
		}
	}

	/** Clase ImageColumn */
	public final class ImageColumn extends FWColumn {
		private JLabel imagen;

		public JLabel getImagen() {
			return imagen;
		}

		public void setImagen(JLabel imagen) {
			this.imagen = imagen;
		}

		public String getMask() {
			return null;
		}
	}

	/** Renderer de la tabla */
	protected class FWJTableRenderer extends DefaultTableCellRenderer {
		//Para mostrar las filas alteradas de distinto color. No funciona en modo edición
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
			Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
			setOpaque(true);
			Font fuente = ((FWJTable)table).getFontCell(row, col);
			if(fuente != null)
				setFont(fuente);
			if(!isSelected) {
				if(!isCeldaGrupo(row, col)) {
					Color cellColor = getBackgroundCell(row, col);
					if(cellColor != null)
						setBackground(cellColor);
					else {
						Color rowColor = getBackgroundRow(row);
						if(rowColor != null)
							setBackground(rowColor);
						else if(gruposCeldas.isEmpty()) {
							Color background = table.getBackground();
							if((row % 2) == 0) {
								if(alternativeColor == null)
									if(background.getRed() > 10 && background.getGreen() > 10 && background.getBlue() > 10)
										setBackground(new Color(background.getRed() - 10, background.getGreen() - 10, background.getBlue() - 10));
									else
										setBackground(background);
								else
									setBackground(alternativeColor);
							} else
								setBackground(background);
						} else
							setBackground(table.getBackground());
					}
				} else {
					Color rowColor = getBackgroundRow(row);
					if(rowColor != null)
						setBackground(rowColor);
					else {
						Color cellColor = getBackgroundCell(row, col);
						if(cellColor != null)
							setBackground(cellColor);
					}
				}
			}
			setForeground(getForegroundCell(row, col));
			if(value != null) {
				FWCell cell = (FWCell)typesCells.get(row + "," + col);
				if(cell != null) {
					int type = cell.getType();
					if(type == INT_CELL) {
						String format = cell.getMask();
						if(format != null) {
							NumberFormat nf = new DecimalFormat(format);
							try {
								String valor = nf.format(value);
								setValue(valor);
							} catch(Exception e) {
								setValue(value);
							}
						} else
							setValue(value);
					} else if(type == DATE_CELL) {
						DateFormat df;
						if(cell.getMask() != null) {
							df = DateUtil.getSimpleDateFormat(cell.getMask());
						}else{
							df = DateUtil.getSimpleDateFormat(tableDateMask);
						}
						try {
							String valor = df.format(value);
							setValue(valor);
						} catch(Exception e) {
							setValue(value);
						}
					} else if(type == TIME_CELL) {
						DateFormat df;
						if(cell.getMask() != null) {
							df = DateUtil.getSimpleDateFormat(cell.getMask());
						}
						else {
							df = DateUtil.getSimpleDateFormat(tableTimeMask);
						}
						try {
							String valor = df.format(value);
							setValue(valor);
						} catch(Exception e) {
							setValue(value);
						}
					} else
						setValue(value);
				} else {
					FWColumn tipoColumna = tiposColumnas[col];
					if(tipoColumna instanceof IntColumn) {
						String mask = ((IntColumn)tipoColumna).getMask();
						if(mask != null) {
							NumberFormat nf = new DecimalFormat(mask);
							try {
								String valor = nf.format(value);
								setValue(valor);
							} catch(Exception e) {
								setValue(value);
							}
						}
					} if(tipoColumna instanceof FloatColumn) {
						String mask = ((FloatColumn)tipoColumna).getMask();
						if(mask != null) {
							NumberFormat nf = new DecimalFormat(mask);
							try {
								String valor = nf.format(value);
								setValue(valor);
							} catch(Exception e) {
								setValue(value);
							}
						}
					} else if(tipoColumna instanceof DateColumn) {
						String mask = ((DateColumn)tipoColumna).getMask();
						if(mask != null) {
							DateFormat df = DateUtil.getSimpleDateFormat(mask);
							try {
								String valor = df.format(value);
								setValue(valor);
							} catch(Exception e) {
								setValue(value);
							}
						} else if(tableDateMask != null) {
							DateFormat df = DateUtil.getSimpleDateFormat(tableDateMask);
							try {
								String valor = df.format(value);
								setValue(valor);
							} catch(Exception e) {
								setValue(value);
							}
						}
					} else if(tipoColumna instanceof TimeColumn) {
						String mask = ((TimeColumn)tipoColumna).getMask();
						if(mask != null) {
							//DateFormat df = new SimpleDateFormat(mask);
							DateFormat df = DateUtil.getSimpleDateFormat(mask);
							try {
								String valor = df.format(value);
								setValue(valor);
							} catch(Exception e) {
								setValue(value);
							}
						}
						if(tableTimeMask != null) {
							//DateFormat df = new SimpleDateFormat(tableTimeMask);
							DateFormat df = DateUtil.getSimpleDateFormat(tableTimeMask);
							try {
								String valor = df.format(value);
								setValue(valor);
							} catch(Exception e) {
								setValue(value);
							}
						}
					} else if(tipoColumna instanceof OperableTimeColumn) {
						if(((OperableTimeColumn)tipoColumna).isDifFechas()) {
							if(value instanceof java.util.Date)
								setValue(DateUtil.getHorasMinutos((java.util.Date)value));
							else
								setValue(value);
						} else {
							String mask = ((OperableTimeColumn)tipoColumna).getMask();
							if(mask != null) {
								//DateFormat df = new SimpleDateFormat(mask);
								DateFormat df = DateUtil.getSimpleDateFormat(mask);
								try {
									String valor = df.format(value);
									setValue(valor);
								} catch(Exception e) {
									setValue(value);
								}
							}
						}
					}
				}
			}
			if(!isCeldaGrupo(row, col)) {
				if(tiposColumnas == null || col >= tiposColumnas.length)
					return null;
				if(tiposColumnas[col] == null)
					return null;
				setHorizontalAlignment(getAlignmentCell(row, col));
				setFont(getFontCell(row, col));
			} else {
				int filaInicioGrupo = getFilaInicioGrupoCeldas(row, col);
				int colInicioGrupo = getColInicioGrupoCeldas(row, col);
				String contenidoInicio = getFormattedValue(filaInicioGrupo, colInicioGrupo);
				setValue(contenidoInicio);
				/* setHorizontalAlignment(CENTER_ALIGN); */
				setHorizontalAlignment(getAlignmentCell(filaInicioGrupo, colInicioGrupo));
				setFont(getFontCell(filaInicioGrupo, colInicioGrupo));
				setVerticalAlignment(CENTER_ALIGN);
			}
			
			RowHeightCalculator.getInstance().setRowHeight(table, row, col);
			return component;
		}


	}

	/**
	 * Clase a utilizar en los renderers para determinar el alto de la fila
	 */
	private static class RowHeightCalculator  {
		private final static RowHeightCalculator instance = new RowHeightCalculator();

		public static RowHeightCalculator getInstance() {
			return instance;
		}

		private final Map<JTable, Map<Integer, Map<Integer, Integer>>> cellSizes = new HashMap<JTable, Map<Integer, Map<Integer, Integer>>>();

		/**
		 * Setea el alto de la celda
		 * @param table
		 * @param row
		 * @param col
		 * @param height
		 */
		private void setCellHeight(JTable table, int row, int col, int height) {
			addSize(table, row, col, height);
		}

		private void setRowHeight (JTable table, int row, int col) {
			for (int c = col+1; c < table.getColumnCount(); c++) {
				TableCellRenderer tcr = table.getCellRenderer(row, c);
				if(tcr instanceof MultiLineCellRenderer) {
					tcr.getTableCellRendererComponent(table, table.getValueAt(row, c), false /*isSelected*/, false /*hasFocus*/, row, c);
				}
			}
			int height_wanted = findTotalMaximumRowSize(table, row);
			if(height_wanted > table.getRowHeight(row)) {
				table.setRowHeight(row, height_wanted);
			}
		}
	
		private void addSize(JTable table, int row, int column, int height) {
			Map<Integer, Map<Integer, Integer>> rows = cellSizes.get(table);
			if(rows == null) {
				cellSizes.put(table, rows = new HashMap<Integer, Map<Integer, Integer>>());
			}
			Map<Integer, Integer> rowheights = rows.get(new Integer(row));
			if(rowheights == null) {
				rows.put(new Integer(row), rowheights = new HashMap<Integer, Integer>());
			}
			rowheights.put(new Integer(column), new Integer(height));
		}

		public int findTotalMaximumRowSize(JTable table, int row) {
			int maximum_height = 0;
			Enumeration<TableColumn> columns = table.getColumnModel().getColumns();
			while(columns.hasMoreElements()) {
				TableColumn tc = columns.nextElement();
				TableCellRenderer cellRenderer = tc.getCellRenderer();
				if(cellRenderer instanceof MultiLineCellRenderer) {
					maximum_height = Math.max(maximum_height, findMaximumRowSize(table, row));
				}
			}
			return maximum_height;
		}

		private int findMaximumRowSize(JTable table, int row) {
			Map<Integer, Map<Integer, Integer>> rows = cellSizes.get(table);
			if(rows == null)
				return 0;
			Map<Integer, Integer> rowheights = rows.get(new Integer(row));
			if(rowheights == null)
				return 0;
			int maximum_height = 0;
			for(Iterator<Map.Entry<Integer,Integer>> it = rowheights.entrySet().iterator(); it.hasNext();) {
				Map.Entry<Integer, Integer> entry = it.next();
				int cellHeight = (entry.getValue()).intValue();
				maximum_height = Math.max(maximum_height, cellHeight);
			}
			return maximum_height;
		}

	}

	/** Renderer de las celdas de tipo 'Imágen' */
	private class ImageCellRenderer extends JLabel implements TableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component component = (Component)value;
			Color cellColor = getBackgroundCell(row, column);
			if(cellColor != null)
				setBackground(cellColor);
			else {
				Color rowColor = getBackgroundRow(row);
				if(rowColor != null)
					setBackground(rowColor);
				else {
					Color colorTable = table.getBackground();
					if((row % 2) == 0) {
						if(alternativeColor == null)
							if(colorTable.getRed() > 10 && colorTable.getGreen() > 10 && colorTable.getBlue() > 10)
								setBackground(new Color(colorTable.getRed() - 10, colorTable.getGreen() - 10, colorTable.getBlue() - 10));
							else
								setBackground(colorTable);
						else
							setBackground(alternativeColor);
					} else
						setBackground(colorTable);
				}
			}
			return component;
		}
	}

	public CheckRenderer createCheckRenderer (){
		return new CheckRenderer();
	}
	
	/** Renderer de las celdas de tipo 'Check' */
	public class CheckRenderer extends DefaultTableCellRenderer {
		private JCheckBox check = new JCheckBox();

		public CheckRenderer() {
			super();
			check.setMargin(new Insets(0, 0, 0, 0));
			check.setHorizontalAlignment(CENTER_ALIGN);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
			if(isSelected) {
				setForeground(table.getSelectionForeground());
				super.setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(table.getBackground());
			}
			//if (row == 5 && col > 4)
			//	logger.debug("getTableCellRendererComponent: (row=" + row + ",col=" + col+ ")." + value);
			setValue(value);
			return check;
		}

		public void setValue(Object value) {
			if(value != null && ((Boolean)value).booleanValue())
				check.setSelected(true);
			else
				check.setSelected(false);
		}
	}

	/** Renderer de las celdas de tipo 'Multiline' */
	public class MultiLineCellRenderer extends JTextArea implements TableCellRenderer {
		private final DefaultTableCellRenderer adaptee = new DefaultTableCellRenderer();
		public MultiLineCellRenderer() {
			setLineWrap(true);
			setWrapStyleWord(true);
		}
		public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
			adaptee.getTableCellRendererComponent(table, obj, isSelected, hasFocus, row, column);
			setForeground(getForegroundCell(row, column));
			if(!isSelected) {
				Color rowColor = getBackgroundRow(row);
				if(rowColor != null)
					setBackground(rowColor);
				else {
					Color cellColor = getBackgroundCell(row, column);
					if(cellColor != null) {
						setBackground(cellColor);
					} else {
						Color colorTable = table.getBackground();
						if((row % 2) == 0) {
							if(alternativeColor == null)
								if(colorTable.getRed() > 10 && colorTable.getGreen() > 10 && colorTable.getBlue() > 10)
									setBackground(new Color(colorTable.getRed() - 10, colorTable.getGreen() - 10, colorTable.getBlue() - 10));
								else
									setBackground(colorTable);
							else
								setBackground(alternativeColor);
						} else
							setBackground(colorTable);
					}
				}
			} else {
				Color rowColor = adaptee.getBackground(); 
				setBackground(rowColor);
			}
			setBorder(adaptee.getBorder());
			setFont(adaptee.getFont());
			setText(adaptee.getText());
			TableColumnModel columnModel = table.getColumnModel();
			setSize(columnModel.getColumn(column).getWidth(), 100000);
			RowHeightCalculator.getInstance().setCellHeight(table, row, column, (int)getPreferredSize().getHeight());
			return this;
		}
	}

	/** Renderer de las celdas de tipo 'Multiline' con el flag isHtml en true */
	public class HtmlCellRenderer extends JLabel implements TableCellRenderer {
		public HtmlCellRenderer() {
			setOpaque(true);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			setText((value == null) ? "" : value.toString());
			int width = table.getColumnModel().getColumn(column).getWidth();
			setSize(width, 1000);
			int preferredHeight = (int)getPreferredSize().getHeight();
			int rowHeight = table.getRowHeight(row);
			Color background = table.getBackground();
			Color foreground = table.getForeground();
			Color selectionBackground = table.getSelectionBackground();
			Color selectionForeground = table.getSelectionForeground();
			if(preferredHeight != rowHeight) {
				table.setRowHeight(row, preferredHeight);
			}
			if(isSelected) {
				setBackground(selectionBackground);
				setForeground(selectionForeground);
			} else {
				Color rowColor = getBackgroundRow(row);
				if(rowColor != null)
					setBackground(rowColor);
				else {
					Color cellColor = getBackgroundCell(row, column);
					if(cellColor != null)
						setBackground(cellColor);
					else {
						if((row % 2) == 0) {
							if(alternativeColor == null)
								if(background.getRed() > 10 && background.getGreen() > 10 && background.getBlue() > 10)
									setBackground(new Color(background.getRed() - 10, background.getGreen() - 10, background.getBlue() - 10));
								else
									setBackground(background);
							else
								setBackground(alternativeColor);
						} else
							setBackground(background);
					}
				}
				setForeground(foreground);
			}
			if(hasFocus) {
				setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
				if(table.isCellEditable(row, column)) {
					setForeground(UIManager.getColor("Table.focusCellForeground"));
					setBackground(UIManager.getColor("Table.focusCellBackground"));
				}
			} else {
				setBorder(new EmptyBorder(1, 2, 1, 2));
			}
			return this;
		}
	}

	public void sincronizarModeloVista() {
		FWColumn[] tiposColumnasTemp = new FWColumn[getTiposColumnas().length];
		System.arraycopy(getTiposColumnas(), 0, tiposColumnasTemp, 0, getTiposColumnas().length);
		for(int i = 0; i < getColumnCount(); i++) {
			if(getColumnModel().getColumn(i).getModelIndex() != i) {
				FWColumn col = getTipoColumna(getColumnModel().getColumn(i).getModelIndex());
				col.setIndice(i);
				tiposColumnasTemp[i] = col;
				getColumnModel().getColumn(i).setModelIndex(i);
			}
		}
		setTiposColumnas(tiposColumnasTemp);
	}

	/**
	 * Muestra una lista de checkboxes con las columnas de la tabla a
	 * ocultar/mostrar.
	 */
	public void hideColumns() {
		columnChooser.updateColumns();
		columnChooser.setVisible(true);
	}

	@SuppressWarnings("unchecked")
	public void sortAllRowsBy(int col, boolean ascending, Comparator comparator) {
		int filas = this.getRowCount(); //Cantidad de filas de la tabla
		int columnas = this.getColumnCount(); //Cantidad de columnas de la
		//tabla
//		Hashtable tempCells = new Hashtable(); //Colección temporal que
		//almacena las celdas con su
		//color de fondo
		Hashtable tempRows = new Hashtable(); //Colección temporal que
		//almacena
		//las filas con su color de fondo
		Hashtable tempTipos = new Hashtable();
//		Hashtable tempLocks = new Hashtable(); //Colección temporal que
		//almacena las celdas que estan
		//lockeadas
//		Hashtable tempTooltip = new Hashtable(); //Colección temporal que
		//almacena las celdas con su
		//tooltip
//		Hashtable tempFonts = new Hashtable();
		Vector row; //Vector que representa una fila de la tabla.
		Vector data = tableModel.getDataVector(); //Vector que representa los
		//datos contenidos en la
		//tabla
		int[] seleccion = getSelectedRows();
		setAutoCreateColumnsFromModel(false);
		//Se agrega una columna con el número de fila
		for(int i = 0; i < filas; i++) {
			row = (Vector)data.elementAt(i);
			row.addElement(new Integer(i));
		}
		//Se ordenan las filas
		Collections.sort(data, comparator);
		lockCells = ordenarHash(data, lockCells);
		backgroundColorCells = ordenarHash(data, backgroundColorCells);
		foregroundColorCells = ordenarHash(data, foregroundColorCells);
		tooltipCells = ordenarHash(data, tooltipCells);
		typesCells = ordenarHash(data, typesCells);
		fontCells = ordenarHash(data, fontCells);
		alignmentCells = ordenarHash(data, alignmentCells);
		for(Iterator i = backgroundColorRows.values().iterator(); i.hasNext();) {
			FWRow fila = (FWRow)i.next();
			int indexFila = fila.getIndex();
			for(int j = 0; j < filas; j++) {
				int indexData = ((Integer)((Vector)data.elementAt(j)).elementAt(columnas)).intValue();
				if(indexFila == indexData) {
					fila.setIndex(j);
					Integer key = fila.getKey();
					tempRows.put(key, fila);
					break;
				}
			}
		}
		for(int i = 0; i < tiposColumnas.length; i++) {
			FWColumn tipoColumna = tiposColumnas[i];
			if(tipoColumna instanceof OperableTimeColumn) {
				Hashtable tipos = ((OperableTimeColumn)tipoColumna).getTipos();
				for(Iterator j = tipos.values().iterator(); j.hasNext();) {
					FWRow fila = (FWRow)j.next();
					for(int k = 0; k < filas; k++) {
//						int indexData = ((Integer)((Vector)data.elementAt(k)).elementAt(columnas)).intValue();
						fila.setIndex(k);
						Integer key = fila.getKey();
						tempTipos.put(key, fila);
						break;
					}
				}
				((OperableTimeColumn)tipoColumna).setTipos(tempTipos);
			}
		}
		int[] seleccionTemp = new int[seleccion.length];
		int ind = 0;
		for(int j = 0; j < filas; j++) {
			int indexData = ((Integer)((Vector)data.elementAt(j)).elementAt(columnas)).intValue();
			for(int i = 0; i < seleccion.length; i++) {
				if(indexData == seleccion[i] && ind < seleccion.length)
					seleccionTemp[ind++] = j;
			}
		}
		for(int i = 0; i < filas; i++) {
			row = (Vector)data.elementAt(i);
			row.removeElementAt(columnas);
		}
		//backgroundColorCells = tempCells;
		backgroundColorRows = tempRows;
		/*
		 * lockCells = tempLocks; tooltipCells = tempTooltip; fontCells = tempFonts;
		 */
		tableModel.fireTableStructureChanged();
		for(int i = 0; i < seleccion.length; i++)
			addRowSelectionInterval(seleccionTemp[i], seleccionTemp[i]);
	}

	/**
	 * Ordena una columna.
	 * @param col
	 * @param ascending
	 */
	@SuppressWarnings("unchecked")
	public void sortAllRowsBy(int col, boolean ascending) {
		sortAllRowsBy(col, ascending, new ColumnSorter(col, ascending));
	}

	@SuppressWarnings("unchecked")
	private Hashtable ordenarHash(Vector data, Hashtable hashtable) {
		int filas = getRowCount();
		int columnas = getColumnCount();
		Hashtable hashOrdenado = new Hashtable();
		for(Iterator i = hashtable.values().iterator(); i.hasNext();) {
			FWCell cell = (FWCell)i.next();
			int filaCelda = cell.getRow();
			for(int j = 0; j < filas; j++) {
				int filaData = ((Integer)((Vector)data.elementAt(j)).elementAt(columnas)).intValue();
				if(filaCelda == filaData) {
					cell.setRow(j);
					String key = cell.getKey();
					hashOrdenado.put(key, cell);
					break;
				}
			}
		}
		return hashOrdenado;
	}

	/** Clase para ordenar los datos en la tabla */
	public class ColumnSorter implements Comparator {
		int col;
		boolean ascending;

		public ColumnSorter(int col, boolean ascending) {
			this.col = col;
			this.ascending = ascending;
		}

		@SuppressWarnings("unchecked")
		public int compare(Object a, Object b) {
			Vector v1 = (Vector)a;
			Vector v2 = (Vector)b;
			Object o1 = v1.get(col);
			Object o2 = v2.get(col);
			//Trata las cadenas vacías como nulls
			if(o1 instanceof String && ((String)o1).length() == 0)
				o1 = null;
			if(o2 instanceof String && ((String)o2).length() == 0)
				o2 = null;
			//Ordena los nulls y los pone últimos
			if(o1 == null && o2 == null)
				return 0;
			else if(o1 == null)
				return 1;
			else if(o2 == null)
				return -1;
			else if(o1 instanceof Comparable) {
				if(o1 instanceof String) {
					if(ascending)
						return ((String)o1).compareToIgnoreCase((String)o2);
					else
						return ((String)o2).compareToIgnoreCase((String)o1);
				}
				if(ascending)
					return ((Comparable)o1).compareTo(o2);
				else
					return ((Comparable)o2).compareTo(o1);
			} else if(ascending) {
				return o1.toString().compareToIgnoreCase(o2.toString());
			} else {
				return o2.toString().compareToIgnoreCase(o1.toString());				
			}
		}
	}
	
	/** Clase para manejar la edición de las columnas */
	public class FWJTableModelListener implements TableModelListener {
		public void tableChanged(TableModelEvent evt) {
			int firstRow = evt.getFirstRow();
			int lastRow = evt.getLastRow();
			int mColIndex = evt.getColumn();
			switch(evt.getType()) {
				case TableModelEvent.INSERT:
				for(int i = firstRow; i <= lastRow; i++)
					newRowAdded(getRowCount());
				break;
				case TableModelEvent.DELETE:
				for(int i = firstRow; i <= lastRow; i++)
					newRowDeleted();
				break;
				case TableModelEvent.UPDATE:
				for(int i = firstRow; i <= lastRow; i++)
					newRowUpdated(mColIndex, i);
				break;
			}
			cellEdited(evt.getColumn(), evt.getFirstRow());
		}
	}
	
	public ColumnHeaderListener getColumnHeaderListener(){
		if (columnHeaderListener == null) {
			columnHeaderListener = new ColumnHeaderListener();
		}
		return columnHeaderListener ;
	}
	/** Clase para manejar el evento de doble click en la cabecera de las tablas */
	public class ColumnHeaderListener extends MouseAdapter {
		int xColumna;
		Timer timer;
		TableColumn columnaActual;

		public ColumnHeaderListener() {
			timer = new Timer(200, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					timer.stop();
					accionClick();
				}
			});
		}

		public void mouseClicked(MouseEvent evt) {
			int clickCount = evt.getClickCount();
			xColumna = evt.getX();
			if(clickCount == 2) {
				timer.stop();
				accionDobleClick();
			} else if(clickCount == 1)
				timer.start();
		}

		private void accionClick() {
			if(allowSorting()) {
				int newColIndex = getColumnModel().getColumnIndexAtX(xColumna);
				if(lastSortedColIndex == newColIndex)
					sortColsAscending = !sortColsAscending;
				else
					sortColsAscending = true;
				lastSortedColIndex = newColIndex;
				sortAllRowsBy(lastSortedColIndex, sortColsAscending);
			}
			singleClickHeader();
		}

		private void accionDobleClick() {
			if(allowHidingColumns())
				hideColumns();
			doubleClickHeader();
		}
	}

	@SuppressWarnings("unused")
	private void ordenar(MouseEvent evt) {
		int newColIndex = getColumnModel().getColumnIndexAtX(evt.getX());
		if(lastSortedColIndex == newColIndex)
			sortColsAscending = !sortColsAscending;
		else
			sortColsAscending = true;
		lastSortedColIndex = newColIndex;
		sortAllRowsBy(lastSortedColIndex, sortColsAscending);
	}

	public class FWColumnChooser extends JDialog implements ActionListener {
		private JButton btnAceptar = new JButton("Aceptar");
		private JButton btnCancelar = new JButton("Cancelar");
		private FWCheckBoxList listaHeader = new FWCheckBoxList();
		private JScrollPane sp = new JScrollPane(listaHeader);
		private boolean okPressed;

		public FWColumnChooser(String titulo) {
			super(new JDialog(), titulo, true);
			setResizable(false);
			construct();
			int x = ((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2);
			int y = ((Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2);
			setLocation(x, y);
		}

		public void updateColumns() {
			String columnas[] = obtenerColumnas();
			listaHeader.setValues(columnas);
			for(int i = 0; i < columnas.length; i++) {
				FWColumn clColumn = getCLColumn(columnas[i]);
				if(!clColumn.isOculta() && clColumn.isVisible()) {
					listaHeader.setSelectedIndex(i);
				}
			}
			sp.setPreferredSize(new Dimension(200, 250));
			pack();
		}

		private FWColumn getCLColumn(String header) {
			for(int i = 0; i < tiposColumnas.length; i++) {
				FWColumn clColumn = tiposColumnas[i];
				if(clColumn.getNombre().compareTo(header) == 0) {
					return clColumn;
				}
			}
			return null;
		}

		/** Llamado cuando se presionan los botones */
		public void actionPerformed(ActionEvent evt) {
			//Botón 'Aceptar'
			if(evt.getSource() == btnAceptar) {
				//Actualiza las columnas de las tablas según las elegidas
				int cantidadColumnasOcultas = 0;
				int j = 0;
				for(int i = 0; i < tiposColumnas.length; i++) {
					if(((FWColumn)tiposColumnas[i]).isOculta()) {
						cantidadColumnasOcultas++;
						continue;
					}
					if(!listaHeader.isSelectedIndex(j))
						((FWColumn)tiposColumnas[j + cantidadColumnasOcultas]).setVisible(false);
					else
						((FWColumn)tiposColumnas[j + cantidadColumnasOcultas]).setVisible(true);
					j++;
				}
				okPressed = true;
			} else {
				okPressed = false;
			}
			setVisible(false);
		}

		/** Crea los botones y ubica los datos en el ColumnChooser */
		public void construct() {
			getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
			sp.setPreferredSize(new Dimension(200, 250));
			JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
			panel.add(btnAceptar);
			btnAceptar.addActionListener(this);
			panel.add(btnCancelar);
			btnCancelar.addActionListener(this);
			getContentPane().add(BorderLayout.NORTH, sp);
			getContentPane().add(BorderLayout.SOUTH, panel);
			pack();
		}

		/**
		 * Obtiene los nombres del header de la tabla y devuelve un String[] con
		 * los datos.
		 */
		@SuppressWarnings("unchecked")
		public String[] obtenerColumnas() {
			java.util.List headers = new ArrayList();
			for(int i = 0; i < getColumnCount(); i++) {
				FWColumn columna = tiposColumnas[i];
				String header = columna.getNombre();
				if(!columna.isOculta() && header != null && header.trim().length() != 0) {
					headers.add(header);
				}
			}
			return (String[])headers.toArray(new String[0]);
		}

		/**
		 * Devuelve la lista con los nombres de las columnas de la tabla.
		 * @return listaHeader
		 */
		public FWCheckBoxList getListaHeader() {
			return listaHeader;
		}

		public JButton getBtnAceptar() {
			return btnAceptar;
		}

		public boolean isOkPressed() {
			return okPressed;
		}
	}

	/**
	 * Retorna el <b>ancho sobrante</b> entre el ancho total de las columnas y
	 * el tamaño total de la tabla.
	 * @return widthUnused
	 */
	public int getWidthUnused() {
		int widthUnused = COLUMN_DEFAULT_WIDTH;
		int totalColWidth = 0;
		JScrollPane scrollPane = getScrollPane();
		if(scrollPane != null) {
			int containerWidth = scrollPane.getWidth();
			if(containerWidth != 0) {
				for(int i = 0; i < tiposColumnas.length; i++) {
					if((FWColumn)tiposColumnas[i] != null) {
						totalColWidth += ((FWColumn)tiposColumnas[i]).getAncho();
					}
				}
				//Ancho del contenedor - ancho total de las columnas - ancho del scroll
				widthUnused = containerWidth - totalColWidth - FWJTable.SCROLL_WIDTH;
			}
		}
		return widthUnused;
	}

	/**
	 * Retorna el <b>ScrollPane</b> que contiene a la tabla.
	 * @return scrollPane
	 */
	public JScrollPane getScrollPane() {
		if(getParent() != null && ((Container)getParent()).getParent() != null && ((Container)getParent()).getParent() instanceof JScrollPane)
			return (JScrollPane)((Container)getParent()).getParent();
		return null;
	}

	/**
	 * Devuelve <b>true</b> si ya existe <code>data</code> en la columna
	 * especificada por <code>col</code>.
	 * @param col
	 * @param data
	 * @return
	 */
	public boolean foundData(int col, Object data) {
		if(data != null)
			for(int i = 0; i < getRowCount(); i++)
				if(getValueAt(i, col) != null && getValueAt(i, col).equals(data))
					return true;
		return false;
	}

	/**
	 * Chequea que no existan valores duplicados en una columna de la tabla.
	 * @param col
	 * @return
	 */
	public boolean checkDuplicateData(int col) {
		Object data, data2;
		for(int i = 0; i < getRowCount(); i++) {
			data = getTypedValueAt(i, col);
			for(int j = i + 1; j < getRowCount(); j++) {
				data2 = getTypedValueAt(j, col);
				if(data.equals(data2))
					return true;
			}
		}
		return false;
	}

	/**
	 * Devuelve el índice de la primera fila cuyo valor en la columna pasada
	 * como parámetro es vacía. Si no existe ninguna devuelve <b>-1</b>.
	 * @param col
	 * @return el indice de la primer fila vacía en la columna <b>col</b>. Si
	 *         no existe ninguna devuelve <b>-1</b>.
	 */
	public int getFirstEmptyRow(int col) {
		return getFirstEmptyRow(0, col);
	}

	/**
	 * Devuelve el índice de la primera fila cuyo valor en la columna pasada
	 * como parámetro es vacio. Empieza a comparar a partir de la fila
	 * <b>filaDesde</b>.
	 * @param filaDesde
	 * @param col
	 * @return el indice de la primer fila vacía en la columna <b>col</b> y
	 *         partir de la fila <b>filaDesde</b>. Si no existe ninguna
	 *         devuelve <b>-1</b>.
	 */
	public int getFirstEmptyRow(int filaDesde, int col) {
		for(int i = filaDesde; i < getRowCount(); i++)
			if(getValueAt(i, col) == null || (tiposColumnas[col] instanceof StringColumn && getValueAt(i, col).toString().trim().length() == 0)) {
				return i;
			}
		return -1;
	}

	/**
	 * Devuelve <b>true</b> si la fila de la tabla está completa con todos los
	 * datos. Se comienza a verificar a partir de <b>desde</b>.
	 * @param colDesde
	 * @param fila
	 * @return resultado
	 */
	public boolean rowCompleted(int colDesde, int fila) {
		return rowCompleted(colDesde, getColumnCount() - 1, fila);
	}

	/**
	 * Devuelve <b>true</b> si la fila de la tabla está completa con todos los
	 * datos. Se comienza a verificar a partir de <b>desde</b>.
	 * @param colDesde
	 * @param fila
	 * @return
	 */
	public boolean rowCompleted(int colDesde, int colHasta, int fila) {
		if(getRowCount() != 0) {
			for(int i = colDesde; i <= colHasta; i++) {
				if(getValueAt(fila, i) != null) {
					if((tiposColumnas[i] instanceof StringColumn && getValueAt(fila, i).toString().trim().length() == 0)) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Devuelve el índice de la primera fila cuyo valor coindice con
	 * <code>obj</code>.
	 * @param col
	 * @param obj
	 * @return el índice de la primera fila. Si no existe devuelve <b>-1</b>.
	 */
	public int getFirstRowWithValue(int col, Object obj) {
		return getFirstRowWithValue(0, col, obj);
	}

	/**
	 * Devuelve el índice de la primera fila cuyo valor coindice con
	 * <code>obj</code>. Comienza a comparar a partir de la fila <b>filaDesde</b>.
	 * @param filaDesde
	 * @param col
	 * @param obj
	 * @return el índice de la primera fila. Si no existe devuelve <b>-1</b>.
	 */
	public int getFirstRowWithValue(int filaDesde, int col, Object obj) {
		for(int i = filaDesde; i < getRowCount(); i++) {
			if(getValueAt(i, col) != null && getValueAt(i, col).equals(obj)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Devuelve el índice de la primera fila cuyo valor coindice con
	 * <code>str</code>. Comienza a comparar a partir de la fila <b>filaDesde</b>.
	 * La columna DEBE ser de tipo String.
	 * @param filaDesde
	 * @param col
	 * @param str
	 * @param ignoreCase
	 * @return
	 */
	public int getFirstRowWithValue(int filaDesde, int col, String str, boolean ignoreCase) {
		for(int i = filaDesde; i < getRowCount(); i++) {
			if(getValueAt(i, col) != null) {
				String tableStringValue = (String)getValueAt(i, col);
				boolean equals;
				if(ignoreCase) {
					equals = (tableStringValue.compareToIgnoreCase(str) == 0);
				} else {
					equals = (tableStringValue.compareTo(str) == 0);
				}
				if(equals) {
					return i;
				}
			}
		}
		return -1;
	}

    /**
     * Devuelve una lista de filas cuyos valores coindicen con
     * <code>str</code>. Se comparan en todas las filas de la tabla excepto en <code>rowToIgnore</code>.
     * La columna DEBE ser de tipo String.
     * @param rowToIgnore Fila a ignorar en la comparación
     * @param col Columna en donde buscar el valor a comparar
     * @param str El String a comparar
     * @param ignoreCase Si se desea comparar ignorando mayúsculas y minúsculas
     */
    public List<Integer> getRowsWithStringValue(int rowToIgnore, int col, String str, boolean ignoreCase) {
        List<Integer> rowsWithValue = new ArrayList<Integer>();
        for(int i = 0; i < getRowCount(); i++) {
            if(i != rowToIgnore) {
                if(valoresTablaIguales(str, getValueAt(i, col), ignoreCase)) {
                    rowsWithValue.add(i);
                }
            }
        }
        return rowsWithValue;
    }

    /**
     * Devuelve una lista de filas cuyos valores coindicen con
     * <code>value</code>. Se comparan en todas las filas de la tabla excepto en <code>rowToIgnore</code>.
     * @param rowToIgnore
     * @param col
     * @param value
     */
    public List<Integer> getRowsWithValue(int rowToIgnore, int col, Object value) {
        List<Integer> rowsWithValue = new ArrayList<Integer>();
        for(int i = 0; i < getRowCount(); i++) {
            if(i != rowToIgnore) {
                Object currentValue = getValueAt(i, col);
                if(equalsOrNulls(value, currentValue)) {
                    rowsWithValue.add(i);
                }
            }
        }
        return rowsWithValue;
    }

    /**
     * Devuelve <code>true</code> si ambos valores son distintos de <code>null</code>
     * y son iguales o ambos son <code>null</code>.  
     * @param value1
     * @param value2
     */
    private boolean equalsOrNulls(Object value1, Object value2) {
        return (value1 != null && value2 != null && value1.equals(value2)) ||
               (value1 == null && value2 == null);
    }

    /**
	 * Devuelve <b>true</b> cuando existe alguna combinación de valores
	 * repetidos en las columnas pasada como parámetro.
	 * @param columns
	 * @return <b>true</b> si existen valores repetidos
	 */
	public boolean existRepeatedValues(List<Integer> columns) {
		if(columns != null && !columns.isEmpty()) {
			int totalRows = getRowCount();
			for(int row = 0; row < totalRows; row++) {
				if(row < totalRows - 1) {
					List<Object> currentValues = getValuesInColumns(row, columns);
					for(int nextRow = row + 1; nextRow < totalRows; nextRow++) {
						List<Object> nextValues = getValuesInColumns(nextRow, columns);
						if(currentValues.containsAll(nextValues) && nextValues.containsAll(currentValues)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Devuelve <b>true</b> cuando existe alguna combinación de valores
	 * repetidos en las columnas pasada como parámetro.
	 * @param columns
	 * @return <b>true</b> si existen valores repetidos
	 */
	public boolean existRepeatedValues(int[] columns) {
		List<Integer> l = new ArrayList<Integer>();
		for(Integer c : columns) {
			l.add(c);
		}
		return existRepeatedValues(l);
	}

	   /**
     * Devuelve todos los grupos de filas cuyos valores en las columnas
     * especificado por <code>columns</code> son iguales. 
     * @param columns Las columnas donde se realizan las comparaciones
     * @param ignoreCase Ignora mayúsculas para las columnas que son de tipo String.
     *                   Si no hay columnas de ese tipo entonces no tiene sentido.   
     * @return Un diccionario donde la clave es una fila y el significado
     *         es una lista de otras filas cuyos valores en las columnas
     *         <code>columns</code> son iguales a los valores en esas mismas
     *         columnas pero de la clave.    
     */
    public Diccionario<Integer, Integer> getAllRowsWithRepeatedValues(List<Integer> columns, boolean ignoreCase) {
        Diccionario<Integer, Integer> rowsWithRepeatedValues = new Diccionario<Integer, Integer>();
        for(int row = 0; row < getRowCount(); row++) {
            if(!rowsWithRepeatedValues.existeValor(row)) {
                List<Integer> rowsWithSameValueInColumns = getRowsWithSameValueInColumns(row, columns, ignoreCase);
                if(!rowsWithSameValueInColumns.isEmpty()) {
                    rowsWithRepeatedValues.agregarValores(row, rowsWithSameValueInColumns);
                }
            }
        }
        return rowsWithRepeatedValues;
    }

	/**
	 * Devuelve una lista de filas cuyos valores en las columnas <code>columns</code>
	 * son iguales a los valores en esas mismas columnas pero para la fila <code>row</code>.
	 * Obs: La búsqueda se realiza en forma descendente, o sea que las filas devueltas
	 * siempre son mayores a <code>row</code>.  
	 * @param row La fila de donde sacar los valores a comparar
	 * @param columns Las columnas donde se realizan las comparaciones
	 * @param ignoreCase Ignora mayúsculas para las columnas que son de tipo String.
	 *                   Si no hay columnas de ese tipo entonces no tiene sentido.
	 * @return
	 */
	public List<Integer> getRowsWithSameValueInColumns(int row, List<Integer> columns, boolean ignoreCase) {
		List<Integer> rowsRepetadValuesByRow = new ArrayList<Integer>();
		List<Object> valuesInRowColumns = getValuesInColumns(row, columns);
		for(int i = row + 1; i < getRowCount(); i++) {
			List<Object> valuesInNextRowColumns = getValuesInColumns(i, columns);
			if(equals(valuesInRowColumns, valuesInNextRowColumns, columns, ignoreCase)) {
				rowsRepetadValuesByRow.add(i);
			}
		}
		return rowsRepetadValuesByRow;
	}

	private boolean equals(List<Object> valuesInRow, List<Object> valuesInOtherRow, List<Integer> columns, boolean ignoreCase) {
		boolean equals = true;
		for(int i = 0; (i < valuesInRow.size()) && equals; i++) {
			Object value1 = valuesInRow.get(i);
			Object value2 = valuesInOtherRow.get(i);
			if(tiposColumnas[columns.get(i)] instanceof StringColumn) {
				equals = valoresTablaIguales(value1, value2, ignoreCase);
			} else if((value1 != null) && (value2 != null)) {
				equals = value1.equals(value2);
			} else {
				equals = false;
			}
		}
		return equals;
	}

	private boolean valoresTablaIguales(Object v1, Object v2, boolean ignoreCase) {
		if(v1 == null && v2 != null) {
			return (((String)v2).trim().compareTo("") == 0);
		} else if(v1 != null && v2 == null) {
			return (((String)v1).trim().compareTo("") == 0);
		} else if(v1 != null && v2 != null) {
			if(ignoreCase) {
				return (((String)v1).trim().compareToIgnoreCase(((String)v2).trim()) == 0);
			} else {
				return (((String)v1).trim().compareTo(((String)v2).trim()) == 0);
			}
		}
		return true;
	}

	/**
	 * Para una fila devuelve una lista con los valores que existen en las
	 * columnas pasado como parámetro.
	 * @param row
	 * @param columns
	 * @return values Una lista de valores
	 */
	private List<Object> getValuesInColumns(int row, List<Integer> columns) {
		List<Object> values = new ArrayList<Object>();
		for(Integer col : columns) {
			values.add(getValueAt(row, col));
		}
		return values;
	}

	/**
	 * Permite mostrar/esconder una columna.
	 * @param index El índice de la columna.
	 * @param b <b>true</b> para mostrar una columna. <b>false</b> para esconderla.
	 */
	public void showColumn(int index, boolean b) {
		if(tiposColumnas[index] != null)
			((FWColumn)tiposColumnas[index]).setVisible(b);
	}

	/**
	 * Clase para administrar las selecciones y ediciones de las tablas.
	 */
	public class FocusTable extends FocusAdapter {
		public void focusLost(FocusEvent evt) {
			/*if(!getSelectionModel().getValueIsAdjusting())
				unselectCell();*/
		}
	}
	
	/**
	 * Al setear una columna como oculta, la misma no aparecerá en el diálogo que
	 * permie al usuario seleccionar las columnas visibles. Tampoco se la mostrara
	 * en tabla.
	 * @param index El índice de la columna.
	 * @param b <b>true</b> para setear la columna como oculta.
	 */
	public void ocultarColumna(int index, boolean b) {
		if(tiposColumnas[index] != null)
			((FWColumn)tiposColumnas[index]).setOculta(b);
	}

	public void setColumnAlignment(int col, int alignment) {
		if(tiposColumnas[col] != null) {
			switch(alignment) {
				case LEFT_ALIGN:
				tiposColumnas[col].setTextLeftAlign(true);
				break;
				case RIGHT_ALIGN:
				tiposColumnas[col].setTextRightAlign(true);
				break;
				case CENTER_ALIGN:
				tiposColumnas[col].setTextCenterAlign(true);
				break;
			}
		}
	}

	public void setHeaderAlignment(int col, int alignment) {
		if(tiposColumnas[col] != null) {
			switch(alignment) {
				case LEFT_ALIGN:
				tiposColumnas[col].setHeaderLeftAlign(true);
				break;
				case RIGHT_ALIGN:
				tiposColumnas[col].setHeaderRightAlign(true);
				break;
				case CENTER_ALIGN:
				tiposColumnas[col].setHeaderCenterAlign(true);
				break;
			}
		}
	}

	public void setAlignment(int col, int alignment) {
		if(tiposColumnas[col] != null) {
			switch(alignment) {
				case LEFT_ALIGN:
				tiposColumnas[col].setHeaderLeftAlign(true);
				tiposColumnas[col].setTextLeftAlign(true);
				break;
				case RIGHT_ALIGN:
				tiposColumnas[col].setHeaderRightAlign(true);
				tiposColumnas[col].setTextRightAlign(true);
				break;
				case CENTER_ALIGN:
				tiposColumnas[col].setHeaderCenterAlign(true);
				tiposColumnas[col].setTextCenterAlign(true);
				break;
			}
		}
	}

	/**
	 * Setea el formato con el que se muestran los valores numéricos de la
	 * columna <code>col</code>.
	 * @param format
	 * @param col
	 */
	public void setNumberFormat(String format, int col) {
		if(tiposColumnas[col] instanceof IntColumn)
			((IntColumn)tiposColumnas[col]).setMask(format);
	}

	/**
	 * Setea el formato con el que se muestran los valores numéricos de la
	 * columna <code>col</code>.
	 * @param format
	 * @param col
	 */
	public void setFloatFormatDefault(int col) {
		if(tiposColumnas[col] instanceof FloatColumn)
			((FloatColumn)tiposColumnas[col]).setMask("#,##0.00;(#,##0.00)");
	}

	/**
	 * Setea el formato con el que se muestran las fechas de la columna
	 * <code>col</code>.
	 * @param format
	 * @param col
	 */
	public void setDateFormat(String format, int col) {
		if(tiposColumnas[col] instanceof StringColumn) {
			((DateColumn)tiposColumnas[col]).setMask(format);
		}
	}

	/**
	 * Setea el formato con el que se muestran los valores de la columna
	 * <code>col</code>.
	 * @param format
	 * @param col
	 */
	public void setTimeFormat(String format, int col) {
		if(tiposColumnas[col] instanceof TimeColumn) {
			((TimeColumn)tiposColumnas[col]).setMak(format);
		}
	}

	/**
	 * Setea el formato con el que se muestran los valores de la columna
	 * <code>col</code>.
	 * @param mask
	 * @param col
	 */
	public void setOperableTimeFormat(String mask, int col) {
		if(tiposColumnas[col] instanceof OperableTimeColumn)
			((OperableTimeColumn)tiposColumnas[col]).setMask(mask);
	}

	/**
	 * Setea si una columna del tipo OperableTimeColumn muestra el resultado de
	 * una operación entre fechas.
	 * @param col
	 * @param difFechas
	 */
	public void setDifFechas(int col, boolean difFechas) {
		if(tiposColumnas[col] instanceof OperableTimeColumn)
			((OperableTimeColumn)tiposColumnas[col]).setDifFechas(difFechas);
	}

	/**
	 * Setea el encabezado de la columna <code>col</code>.
	 * @param col
	 * @param value
	 */
	public void setHeaderValue(int col, String value) {
		getColumn(col).setHeaderValue(value);
	}

	/**
	 * Devuelve el encabezado de la columna <code>col</code>.
	 * @param col
	 * @return
	 */
	public String getHeaderValue(int col) {
		return (String)getColumn(col).getHeaderValue();
	}

	/**
	 * Devuelve el <b>estilo de la fuente</b> del header de la tabla.
	 * @return
	 */
	public int getHeaderFontStyle() {
		return getTableHeader().getFont().getStyle();
	}
	
	/**
	 * Setea el <b>estilo de la fuente</b> del header de la tabla.
	 * @param style
	 */
	public void setHeaderFontStyle(int style) {
		getTableHeader().setFont(getTableHeader().getFont().deriveFont(style));
	}

	/**
	 * Devuelve el <b>color de fondo</b> del header de la tabla.
	 * @return
	 */
	public Color getHeaderBackground() {
		return headerBackground;
	}

	/**
	 * Setea el <b>color de fondo</b> del header de la tabla.
	 * @param c
	 */
	public void setHeaderBackground(Color c) {
		this.headerBackground = c;
	}

	/**
	 * Devuelve el <b>color de frente</b> del header de la tabla.
	 * @return
	 */
	public Color getHeaderForeground() {
		return headerForeground;
	}

	/**
	 * Setea el <b>color de frente</b> del header de la tabla.
	 * @return
	 */
	public void setHeaderForeground(Color c) {
		this.headerForeground = c;
	}

	public boolean tieneFocus() {
		return hasFocus();
	}

	/** Clase para el manejo de los distintos tipos de celdas */
	public class FWJFormattedTextField extends JFormattedTextField {
		private int col;
		private int row;
		private final FWJTable tabla;

		public FWJFormattedTextField(FWJTable t) {
			super();
			this.tabla = t;
			addFocusListener(new FocusListener() {
				public void focusLost(FocusEvent evt) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							TableCellEditor tce = getCellEditor(row, col);
							if(!tabla.hasFocus() && tce != null) {
								tce.stopCellEditing();
							}
						}
					});
				}

				public void focusGained(FocusEvent evt) {
					row = rowAtPoint(((JComponent)evt.getSource()).getLocation());
					col = columnAtPoint(((JComponent)evt.getSource()).getLocation());
					selectAll();
				}
			});
			addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent evt) {
					if(evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
						TableCellEditor editor = getCellEditor();
						if(editor != null)
							editor.stopCellEditing();
						setValue(cellOldValue);
					} else if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
						TableCellEditor editor = getCellEditor();
						if(editor != null)
							editor.stopCellEditing();
						teclaEnterPresionada(row, col);
					} else {
						if(evt.getKeyCode() == KeyEvent.VK_LEFT || evt.getKeyCode() == KeyEvent.VK_RIGHT) {
							if(FWJFormattedTextField.this.getText().length() == 0) {
								teclaFlechaPresionada(evt.getKeyCode(), row, col);
							}
						} else if(evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_UP){
							teclaFlechaPresionada(evt.getKeyCode(), row, col);
						}
					}
				} 
			});		
		}
	}

	/**
	 * Setea el texto del tooltip para la columna cuyo indice se recibe como
	 * parámetro.
	 * @param col
	 * @param tooltipText
	 */
	public void setColumnHeaderTooltip(int col, String tooltipText) {
		columnHeaderToolTips.setColumnHeaderTooltip(col, tooltipText);
	}

	/**
	 * Setea el texto del tooltip a <b>todas</b> las columnas de la tabla.
	 * Este método deberá utilizarse después de haber seteado la cantidad
	 * de columnas que contendrá la tabla.
	 * @param tooltipText
	 */
	public void setHeaderTooltip(String tooltipText) {
		for(int i = 0; i < getColumnCount(); i++) {
			setColumnHeaderTooltip(i, tooltipText);
		}
	}

	/** Clase para el manejo de los tooltips de los header de las columnas */
	public class ColumnHeaderTooltips extends MouseMotionAdapter {
		HashMap tooltips = new HashMap();
		TableColumn columnaActual;

		@SuppressWarnings("unchecked")
		public void setColumnHeaderTooltip(int col, String tooltipText) {
			TableColumn columna = getColumnModel().getColumn(col);
			if(tooltipText != null)
				tooltips.put(columna, tooltipText);
			else
				tooltips.remove(columna);
		}

		public void mouseMoved(MouseEvent evt) {
			TableColumn columna = null;
			JTableHeader header = (JTableHeader)evt.getSource();
			int index = getColumnModel().getColumnIndexAtX(evt.getX());
			if(index >= 0)
				columna = getColumnModel().getColumn(index);
			if(columna != null && !columna.equals(columnaActual)) {
				header.setToolTipText((String)tooltips.get(columna));
				columnaActual = columna;
			}
		}
	}

	/**
	 * Elimina los 0 no significativos de <code>numero<code>.
	 * @param numero
	 * @return
	 */
	private String sacarCerosIzquierda(String numero) {
		int i;
		char[] temp = numero.toCharArray();
		for(i = 0; i < temp.length; i++) {
			if(temp[i] != '0' || (i < temp.length - 1 && temp[i] == '0' && temp[i + 1] == '.'))
				break;
		}
		return numero.substring(i);
	}

	public static int getFontSize() {
		return fontSize;
	}

	public static void setFontSize(int fontSize) {
		FWJTable.fontSize = fontSize;
	}

	public boolean getReorderingAllowed() {
		return getTableHeader().getReorderingAllowed();
	}

	public void setReorderingAllowed(boolean reorderingAllowed) {
		getTableHeader().setReorderingAllowed(reorderingAllowed);
	}

	/**
	 * Selecciona un rango de filas y escrolea hasta ellas.
	 * @param filaDesde
	 * @param filaHasta
	 */
	public void selectAndScroll(int filaDesde, int filaHasta) {
		Rectangle cellRect = getCellRect(filaDesde, 0, true);
		if(cellRect != null && (filaDesde <= getRowCount() && filaHasta <= getRowCount())) {
			scrollRectToVisible(cellRect);
			setRowSelectionInterval(filaDesde, filaHasta);
		}
	}

	/**
	 * Escrolea hasta la fila indicada.
	 * @param fila
	 */
	public void scroll(int fila) {
		Rectangle cellRect = getCellRect(fila, 0, true);
		if(cellRect != null) {
			scrollRectToVisible(cellRect);
		}
	}

	/**
	 * Manejo del evento de drag & drop de cols.
	 * @param evt
	 */
	public void columnMoved(TableColumnModelEvent evt) {
		super.columnMoved(evt);
		int colDesde = evt.getFromIndex();
		int colHasta = evt.getToIndex();
		sincronizarModeloVista();
		if(colDesde != colHasta) {
			FWColumn tipoColumnaDesde = tiposColumnas[colDesde];
			FWColumn tipoColumnaHasta = tiposColumnas[colHasta];
			tipoColumnaDesde.setIndice(colHasta);
			tiposColumnas[colHasta] = tipoColumnaDesde;
			tipoColumnaHasta.setIndice(colDesde);
			tiposColumnas[colDesde] = tipoColumnaHasta;
			TableColumn columnaDesdeHeader = getTableHeader().getColumnModel().getColumn(colDesde);
			TableColumn columnaHastaHeader = getTableHeader().getColumnModel().getColumn(colHasta);
			columnaDesdeHeader.setModelIndex(colHasta);
			columnaHastaHeader.setModelIndex(colDesde);
			TableColumn columnaDesdeTabla = getColumnModel().getColumn(colDesde);
			TableColumn columnaHastaTabla = getColumnModel().getColumn(colHasta);
			columnaDesdeTabla.setModelIndex(colHasta);
			columnaHastaTabla.setModelIndex(colDesde);
			Object[][] dataColumnaDesde = getDataVector(0, getRowCount() - 1, colDesde, colDesde);
			Object[][] dataColumnaHasta = getDataVector(0, getRowCount() - 1, colHasta, colHasta);
			for(int i = 0; i < dataColumnaDesde.length; i++) {
				Object nuevoValor = dataColumnaDesde[i][0];
				if(tiposColumnas[colHasta] instanceof StringColumn && nuevoValor == null) {
					setValueAt("", i, colHasta);
				} else {
					setValueAt(nuevoValor, i, colHasta);
				}
			}
			for(int i = 0; i < dataColumnaHasta.length; i++) {
				Object nuevoValor = dataColumnaHasta[i][0];
				if(tiposColumnas[colDesde] instanceof StringColumn && nuevoValor == null) {
					setValueAt("", i, colDesde);
				} else {
					setValueAt(nuevoValor, i, colDesde);
				}
			}
			colDragged(colDesde, colHasta);
		}
	}

	/**
	 * Manejo del evento de Drag & Drop de cols.
	 * @param colDesde
	 * @param colHasta
	 */
	public void colDragged(int colDesde, int colHasta) {
	}

	/**
	 * Elimina todas las filas de la tabla.
	 */
	public void removeAllRows() {
		setNumRows(0);
	}

	/**
	 * Elimina la columna recbida como parámetro.
	 */
	@SuppressWarnings("unchecked")
	public void removeColumn(TableColumn col) {
		super.removeColumn(col);
		FWColumn[] cols = new FWColumn[tiposColumnas.length - 1];
		int pos = 0;
		for(int i = 0; i < tiposColumnas.length; i++) {
			FWColumn c = tiposColumnas[i];
			if(c != null && c.getIndice() != col.getModelIndex()) {
				c.setIndice(pos);
				cols[pos] = c;
				pos++;
			}
		}
		tiposColumnas = cols;
		tableModel.setColumnCount(tableModel.getColumnCount() - 1);
		tableModel.fireTableStructureChanged();
		restablecerAtributos();
		getTableHeader().resizeAndRepaint();
		int index = col.getModelIndex();
		lockCells = removerColumnaHash(index, lockCells);
		backgroundColorCells = removerColumnaHash(index, backgroundColorCells);
		foregroundColorCells = removerColumnaHash(index, foregroundColorCells);
		fontCells = removerColumnaHash(index, fontCells);
		tooltipCells = removerColumnaHash(index, tooltipCells);
		typesCells = removerColumnaHash(index, typesCells);
		alignmentCells = removerColumnaHash(index, alignmentCells);
	}

	private Hashtable removerColumnaHash(int col, Hashtable hashtable) {
		for(Iterator i = hashtable.values().iterator(); i.hasNext();) {
			FWCell cell = (FWCell)i.next();
			if(col == cell.getColumn())
				hashtable.remove(cell.getKey());
		}
		return hashtable;
	}

	/**
	 * Elimina una columna de la tabla.
	 */
	public void removeColumn(int index) {
		removeColumn(getColumnModel().getColumn(index));
	}

	/**
	 * Elimina todas las columnas de la tabla.
	 */
	public void removeAllColumns() {
		setAutoCreateColumnsFromModel(true);
		while(getColumnModel().getColumns().hasMoreElements()) {
			TableColumn col = (TableColumn)getColumnModel().getColumns().nextElement();
			removeColumn(col);
		}
	}

	/**
	 * Devuelve el compenente con su tooltip.
	 */
	public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
		Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
		if(c instanceof JComponent) {
			JComponent jc = (JComponent)c;
			String tooltip = getTooltipCell(rowIndex, vColIndex);
			jc.setToolTipText(tooltip);
		}
		return c;
	}

	/**
	 * Agrupa celdas a partir de la celda ubicada en la posición
	 * <code>fila</code>,<code>col</code>.<code>alto</code> y
	 * <code>ancho</code> definen el tamaño del grupo el filas y columnas.
	 * @param fila
	 * @param col
	 * @param alto
	 * @param ancho
	 */
	@SuppressWarnings("unchecked")
	public void agruparCeldas(int fila, int col, int alto, int ancho) {
		GrupoCeldas grupo = new GrupoCeldas(fila, col, alto, ancho);
		gruposCeldas.add(grupo);
	}

	/**
	 * Desagrupa todas las celdas.
	 */
	public void desagruparCeldas() {
		gruposCeldas.clear();
		repaint();
	}

	/**
	 * Agrupa los headers a partir de la columna <code>col</code>.
	 * <code>ancho</code> define la cantidad de columnas a agrupar.
	 * @param col
	 * @param ancho
	 */
	@SuppressWarnings("unchecked")
	public void agruparHeaders(int col, int ancho) {
		GrupoHeaders grupo = new GrupoHeaders(col, ancho);
		gruposHeaders.add(grupo);
	}

	/**
	 * Desagrupa todos los headers.
	 */
	public void desagruparHeaders() {
		gruposHeaders.clear();
		repaint();
	}

	/**
	 * Devuelve <b>true</b> si la celda ubicada en la posción <code>fila</code>,
	 * <code>col</code> es la primera de un grupo.
	 * @param fila
	 * @param col
	 * @return
	 */
	public boolean isCeldaInicioGrupoCeldas(int fila, int col) {
		for(Iterator i = gruposCeldas.iterator(); i.hasNext();) {
			GrupoCeldas grupo = (GrupoCeldas)i.next();
			if(fila == grupo.getFila() && col == grupo.getCol())
				return true;
		}
		return false;
	}

	/**
	 * Devuelve <b>true</b> si la columna es la primera columna de un grupo de
	 * headers.
	 * @param col
	 * @return
	 */
	public boolean isColInicionGrupoHeader(int col) {
		for(Iterator i = gruposHeaders.iterator(); i.hasNext();) {
			GrupoHeaders grupo = (GrupoHeaders)i.next();
			if(col == grupo.getCol())
				return true;
		}
		return false;
	}

	/**
	 * Devuelve el grupo que comienza en la celda ubicada en la posción fila,
	 * col.
	 * @param fila
	 * @param col
	 * @return
	 */
	public GrupoCeldas getGrupoCeldas(int fila, int col) {
		for(Iterator i = gruposCeldas.iterator(); i.hasNext();) {
			GrupoCeldas grupo = (GrupoCeldas)i.next();
			if(fila == grupo.getFila() && col == grupo.getCol())
				return grupo;
		}
		return null;
	}

	/**
	 * Devuelve el grupo de headers que comienza en la columna indicada.
	 * @param col
	 * @return
	 */
	public GrupoHeaders getGrupoHeader(int col) {
		for(Iterator i = gruposHeaders.iterator(); i.hasNext();) {
			GrupoHeaders grupo = (GrupoHeaders)i.next();
			if(col == grupo.getCol())
				return grupo;
		}
		return null;
	}

	/**
	 * Devuelve el ancho del grupo al que pertenece la celda ubicada en la
	 * posción fila, col.
	 * @param fila
	 * @param col
	 * @return
	 */
	public int getAnchoGrupoCeldas(int fila, int col) {
		GrupoCeldas grupo = getGrupoCeldas(fila, col);
		int ancho = 0;
		if(grupo != null) {
			if(grupo.getAncho() != 1) {
				for(int i = col; i < col + grupo.getAncho(); i++)
					ancho += getColumnModel().getColumn(i).getWidth();
			} else
				ancho = getColumnModel().getColumn(col).getWidth();
		}
		return ancho;
	}

	/**
	 * Devuelve el ancho del grupo al que pertenece la columna indicada.
	 * @param col
	 * @return
	 */
	public int getAnchoGrupoHeader(int col) {
		GrupoHeaders grupo = getGrupoHeader(col);
		int ancho = 0;
		if(grupo != null) {
			if(grupo.getAncho() != 1) {
				for(int i = col; i < col + grupo.getAncho(); i++)
					ancho += getColumnModel().getColumn(i).getPreferredWidth();
			} else
				ancho = getColumnModel().getColumn(col).getPreferredWidth();
		}
		return ancho;
	}

	/**
	 * Retorna el ancho del grupo al que pertenece la celda ubicada en la
	 * posción fila, col.
	 * @param fila
	 * @param col
	 * @return
	 */
	public int getAltoGrupoCeldas(int fila, int col) {
		GrupoCeldas grupo = getGrupoCeldas(fila, col);
		int alto = 0;
		if(grupo != null) {
			if(grupo.getAlto() != 1) {
				for(int i = fila; i < fila + grupo.getAlto(); i++)
					alto += getRowHeight(i);
			} else {
				alto = getRowHeight(fila);
			}
		}
		return alto;
	}

	/**
	 * Retorna la posición x del grupo al que pertenece la celda ubicada en la
	 * posción fila, col.
	 * @param fila
	 * @param col
	 * @return
	 */
	public int getXGrupoCeldas(int fila, int col) {
		GrupoCeldas grupo = getGrupoCeldas(fila, col);
		int x = 0;
		if(grupo != null) {
			for(int i = 0; i < col; i++) {
				x += getColumnModel().getColumn(i).getWidth();
			}
		}
		return x;
	}

	/**
	 * Retorna la posición x del grupo al que pertenece la columna indicada.
	 * @param col
	 * @return
	 */
	public int getXGrupoHeader(int col) {
		GrupoHeaders grupo = getGrupoHeader(col);
		int x = 0;
		if(grupo != null) {
			for(int i = 0; i < col; i++) {
				x += getColumnModel().getColumn(i).getWidth();
			}
		}
		return x;
	}

	/**
	 * Retorna la posición y del grupo al que pertenece la celda ubicada en la
	 * posción fila, col.
	 * @param fila
	 * @param col
	 * @return
	 */
	public int getYGrupoCeldas(int fila, int col) {
		GrupoCeldas grupo = getGrupoCeldas(fila, col);
		int y = 0;
		if(grupo != null) {
			for(int i = 0; i < fila; i++) {
				y += getRowHeight(i);
			}
		}
		return y;
	}

	/**
	 * Devuelve <b>true</b> si la celda ubicada en la posción fila, col
	 * pertenece a algún grupo.
	 * @param fila
	 * @param col
	 * @return
	 */
	public boolean isCeldaGrupo(int fila, int col) {
		for(Iterator i = gruposCeldas.iterator(); i.hasNext();) {
			GrupoCeldas grupo = (GrupoCeldas)i.next();
			int filaDesde = grupo.getFila();
			int filaHasta = grupo.getFila() + grupo.getAlto() - 1;
			int colDesde = grupo.getCol();
			int colHasta = grupo.getCol() + grupo.getAncho() - 1;
			if(fila >= filaDesde && fila <= filaHasta && col >= colDesde && col <= colHasta) {
				return true;
			}
		}
		return false;
	}

	private boolean isCeldasGrupo(int fila, int col, int fila2, int col2) {
		for(Iterator i = gruposCeldas.iterator(); i.hasNext();) {
			GrupoCeldas grupo = (GrupoCeldas)i.next();
			int filaDesde = grupo.getFila();
			int filaHasta = grupo.getFila() + grupo.getAlto() - 1;
			int colDesde = grupo.getCol();
			int colHasta = grupo.getCol() + grupo.getAncho() - 1;
			if(
				(fila >= filaDesde && fila <= filaHasta && col >= colDesde && col <= colHasta) &&
				(fila2 >= filaDesde && fila2 <= filaHasta && col2 >= colDesde && col2 <= colHasta)
			) {
				return true;
			}
		}
		return false;		
	}
	
	private boolean drawLeftBorder(int row, int col) {
		if(col == 0)
			return true;
		if(!isCeldasGrupo(row, col, row, col - 1))
			return true;
		return false;
	}

	@SuppressWarnings("unused")
	private boolean drawRightBorder(int row, int col) {
		if(col == getColumnCount() - 1)
			return true;
		if(!isCeldasGrupo(row, col, row, col - 1))
			return true;
		return false;
	}

	@SuppressWarnings("unused")
	private boolean drawTopBorder(int row, int col) {
		if(row == 0)
			return true;
		if(!isCeldasGrupo(row, col, row - 1, col))
			return true;
		return false;
	}

	private boolean drawBottomBorder(int row, int col) {
		if(row == getRowCount() - 1)
			return true;
		if(!isCeldasGrupo(row, col, row + 1, col))
			return true;
		return false;
	}

	/**
	 * Devuelve la lista que contiene los grupos de celdas.
	 * @return gruposCeldas
	 */
	public List getGruposCeldas() {
		return gruposCeldas;
	}

	/**
	 * Devuelve la lista que contiene los grupos de headers.
	 * @return gruposHeaders
	 */
	public List getGruposHeaders() {
		return gruposHeaders;
	}

	/**
	 * Devuelve <b>true</b> si la columna pertenece a algún grupo.
	 * @param col
	 * @return
	 */
	public boolean isColGrupoHeader(int col) {
		for(Iterator i = gruposHeaders.iterator(); i.hasNext();) {
			GrupoHeaders grupo = (GrupoHeaders)i.next();
			int colDesde = grupo.getCol();
			int colHasta = grupo.getCol() + grupo.getAncho() - 1;
			if(col >= colDesde && col <= colHasta) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Devuelve el indice de la primera fila del grupo al que pertenece la celda
	 * ubicada en la posición fila, col.
	 * @param fila
	 * @param col
	 * @return
	 */
	public int getFilaInicioGrupoCeldas(int fila, int col) {
		for(Iterator i = gruposCeldas.iterator(); i.hasNext();) {
			GrupoCeldas grupo = (GrupoCeldas)i.next();
			int filaDesde = grupo.getFila();
			int filaHasta = grupo.getFila() + grupo.getAlto() - 1;
			int colDesde = grupo.getCol();
			int colHasta = grupo.getCol() + grupo.getAncho() - 1;
			if(fila >= filaDesde && fila <= filaHasta && col >= colDesde && col <= colHasta) {
				return filaDesde;
			}
		}
		return -1;
	}

	/**
	 * Devuelve el indice de la primera columna del grupo al que pertenece la
	 * celda ubicada en la posición fila, col.
	 * @param fila
	 * @param col
	 * @return
	 */
	public int getColInicioGrupoCeldas(int fila, int col) {
		for(Iterator i = gruposCeldas.iterator(); i.hasNext();) {
			GrupoCeldas grupo = (GrupoCeldas)i.next();
			int filaDesde = grupo.getFila();
			int filaHasta = grupo.getFila() + grupo.getAlto() - 1;
			int colDesde = grupo.getCol();
			int colHasta = grupo.getCol() + grupo.getAncho() - 1;
			if(fila >= filaDesde && fila <= filaHasta && col >= colDesde && col <= colHasta) {
				return colDesde;
			}
		}
		return -1;
	}

	/**
	 * Devuelve el índice de la primera columna del grupo al que pertenece la
	 * columna indicada.
	 * @param col
	 * @return
	 */
	public int getColInicioGrupoHeader(int col) {
		for(Iterator i = gruposHeaders.iterator(); i.hasNext();) {
			GrupoHeaders grupo = (GrupoHeaders)i.next();
			int colDesde = grupo.getCol();
			int colHasta = grupo.getCol() + grupo.getAncho() - 1;
			if(col >= colDesde && col <= colHasta) {
				return colDesde;
			}
		}
		return -1;
	}

	/**
	 * Agrupa desde la columna 0 hasta las columna <code>colHasta</code>. Al
	 * finalizar cada agrupación inserta <code>cantFilas</code> filas y
	 * devuelve un arreglo del tipo <b>int</b> con los índices de las filas
	 * insertadas.
	 * @param colHasta
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int[] agruparHastaCol(int colHasta, int cantFilas) {
		gruposCeldas.clear();
		setRowSelectionAllowed(false);
		setColumnSelectionAllowed(false);
		setCellSelectionEnabled(true);
		int[] filasDesde = inicializarArray(colHasta);
		List filasInsertadas = new ArrayList();
		for(int i = colHasta; i >= 0; i--) {
			sortAllRowsBy(i, true);
		}
		Vector valoresAnteriores = getValoresFila(0, colHasta);
		int i = 0;
		while(i < getRowCount()) {
			int colCorte = getColCorte(i, colHasta, valoresAnteriores);
			if(colCorte != -1) {
				valoresAnteriores = getValoresFila(i, colHasta);
				for(int j = colHasta; j >= colCorte; j--) {
					int alto = i - filasDesde[j];
					agruparCeldas(filasDesde[j], j, alto, 1);
				}
				for(int j = 0; j < cantFilas; j++) {
					if(i != getRowCount() - 1) {
						insertRow(i, new Vector());
						filasInsertadas.add(new Integer(i));
						i++;
					}
				}
				for(int j = colHasta; j >= colCorte; j--)
					filasDesde[j] = i;
			}
			i++;
		}
		for(int j = 0; j <= colHasta; j++) {
			int alto = i - filasDesde[j];
			agruparCeldas(filasDesde[j], j, alto, 1);
		}
		for(int j = 0; j < cantFilas; j++) {
			addRow();
			filasInsertadas.add(new Integer(getRowCount() - 1));
		}
		return getFilasInsertadas(filasInsertadas);
	}

	/**
	 * Genera una arreglo del tipo <b>int</b> a partir de una lista que
	 * contiene objetos del tipo <b>Integer</b>.
	 * @param filasInsertadas
	 * @return filas
	 */
	private int[] getFilasInsertadas(List filasInsertadas) {
		int[] filas = new int[filasInsertadas.size()];
		for(int i = 0; i < filasInsertadas.size(); i++) {
			Integer fila = (Integer)filasInsertadas.get(i);
			filas[i] = fila.intValue();
		}
		return filas;
	}

	/**
	 * Inicializa un array de colHasta + 1 posiciones en 0.
	 * @param colHasta
	 * @return array
	 */
	private int[] inicializarArray(int colHasta) {
		int[] array = new int[colHasta + 1];
		for(int i = 0; i < array.length; i++)
			array[i] = 0;
		return array;
	}

	/**
	 * Retrona los primeros colHasta + 1 elementos de la fila fila.
	 * @param fila
	 * @param colHasta
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Vector getValoresFila(int fila, int colHasta) {
		Vector valores = new Vector();
		for(int i = 0; i <= colHasta; i++) {
			valores.add(getValueAt(fila, i));
		}
		return valores;
	}

	/**
	 * Devuelve la menor columna en la que se cierra un grupo.
	 * @param fila
	 * @param colHasta
	 * @param valoresAnteriores
	 * @return
	 */
	private int getColCorte(int fila, int colHasta, Vector valoresAnteriores) {
		Vector v = getValoresFila(fila, colHasta);
		int corteControl = colHasta;
		boolean hayCorte = false;
		for(int i = valoresAnteriores.size() - 1; i >= 0; i--) {
			if(valoresAnteriores.get(i) != null && !valoresAnteriores.get(i).equals(v.get(i))) {
				corteControl = Math.min(i, corteControl);
				hayCorte = true;
			}
		}
		if(hayCorte) {
			return corteControl;
		}
		return -1;
	}

	/**
	 * Inserta una fila en la posición <code>row</code> con el contenido de
	 * <code>rowData</code>.
	 * @param row
	 * @param rowData
	 */
	public void insertRow(int row, Object[] rowData) {
		actualizarHash(row);
		tableModel.insertRow(row, rowData);
	}

	/**
	 * Inserta una fila en la posición <code>row</code> con el contenido de
	 * <code>rowData</code>.
	 * @param row
	 * @param rowData
	 */
	public void insertRow(int row, Vector rowData) {
		actualizarHash(row);
		tableModel.insertRow(row, rowData);
	}

	/**
	 * Actualiza las distintas colecciones que contienen información sobre las
	 * celdas.
	 * @param row
	 */
	@SuppressWarnings("unchecked")
	private void actualizarHash(int row) {
		Hashtable backgroundColorRowsTemp = new Hashtable();
		lockCells = insertarFilaHash(row, lockCells);
		backgroundColorCells = insertarFilaHash(row, backgroundColorCells);
		foregroundColorCells = insertarFilaHash(row, foregroundColorCells);
		fontCells = insertarFilaHash(row, fontCells);
		tooltipCells = insertarFilaHash(row, tooltipCells);
		typesCells = insertarFilaHash(row, typesCells);
		alignmentCells = insertarFilaHash(row, alignmentCells);
		for(Iterator i = backgroundColorRows.values().iterator(); i.hasNext();) {
			FWRow fila = (FWRow)i.next();
			int indexFila = fila.getIndex();
			if(indexFila >= row)
				fila.setIndex(indexFila + 1);
			Integer key = fila.getKey();
			backgroundColorRowsTemp.put(key, fila);
		}
		backgroundColorRows = backgroundColorRowsTemp;
	}

	@SuppressWarnings("unchecked")
	private Hashtable insertarFilaHash(int row, Hashtable hashtable) {
		Hashtable hashtableTemp = new Hashtable();
		for(Iterator i = hashtable.values().iterator(); i.hasNext();) {
			FWCell cell = (FWCell)i.next();
			int filaCelda = cell.getRow();
			if(filaCelda >= row)
				cell.setRow(filaCelda + 1);
			String key = cell.getKey();
			hashtableTemp.put(key, cell);
		}
		return hashtableTemp;
	}

	/**
	 * Clase utilizada para la agrupación de celdas.
	 */
	public class GrupoCeldas {
		private int fila;
		private int col;
		private int alto;
		private int ancho;

		public GrupoCeldas(int fila, int col, int alto, int ancho) {
			this.fila = fila;
			this.col = col;
			this.alto = alto;
			this.ancho = ancho;
		}

		public int getAlto() {
			return alto;
		}

		public void setAlto(int alto) {
			this.alto = alto;
		}

		public int getAncho() {
			return ancho;
		}

		public void setAncho(int ancho) {
			this.ancho = ancho;
		}

		public int getCol() {
			return col;
		}

		public void setCol(int col) {
			this.col = col;
		}

		public int getFila() {
			return fila;
		}

		public void setFila(int fila) {
			this.fila = fila;
		}
	}

	/**
	 * Clase utilizada para la agrupación de headers.
	 */
	public class GrupoHeaders {
		int col;
		int ancho;

		public GrupoHeaders(int col, int ancho) {
			this.col = col;
			this.ancho = ancho;
		}

		public int getAncho() {
			return ancho;
		}

		public int getCol() {
			return col;
		}
	}

	/**
	 * UI adaptada para la agrupación de celdas.
	 */
	public class FWJTableUI extends BasicTableUI {
		/**
		 * Método copiado de BasicTableUI.
		 * @param g
		 * @param c
		 */
		
		public void pintarPagina(Graphics g) {
			if(table.getRowCount() <= 0 || table.getColumnCount() <= 0)
				return;
			Rectangle clip = g.getClipBounds();
			Point upperLeft = clip.getLocation();
			Point lowerRight = new Point(clip.x + clip.width - 1, clip.y + clip.height - 1);
			int rMin = table.rowAtPoint(upperLeft);
			int rMax = table.rowAtPoint(lowerRight);
			if(rMin == -1)
				rMin = 0;
			if(rMax == -1)
				rMax = table.getRowCount() - 1;
			boolean ltr = table.getComponentOrientation().isLeftToRight();
			int cMin = table.columnAtPoint(ltr ? upperLeft : lowerRight);
			int cMax = table.columnAtPoint(ltr ? lowerRight : upperLeft);
			if(cMin == -1)
				cMin = 0;
			if(cMax == -1)
				cMax = table.getColumnCount() - 1;
			g.setClip(clip.x,0,clip.width,clip.height);
			pintarGrillaPagina(g, rMin, rMax, cMin, cMax);
			pintarCeldasPagina(g, rMin, rMax, cMin, cMax);
		}
		
		private void pintarCeldasPagina(Graphics g, int rMin, int rMax, int cMin, int cMax) {
			JTableHeader header = table.getTableHeader();
			TableColumn draggedColumn = (header == null) ? null : header.getDraggedColumn();
			TableColumnModel cm = table.getColumnModel();
			int columnMargin = cm.getColumnMargin();
			Rectangle cellRect;
			TableColumn aColumn;
			int columnWidth;
			if(table.getComponentOrientation().isLeftToRight()) {
				int deltaY = table.getCellRect(rMin, cMin, true).y;
				for(int row = rMin; row <= rMax; row++) {
					cellRect = table.getCellRect(row, cMin, false);
					cellRect.y = cellRect.y - deltaY;
					for(int column = cMin; column <= cMax; column++) {
						aColumn = cm.getColumn(column);
						columnWidth = aColumn.getWidth();
						cellRect.width = columnWidth - columnMargin;
						if(aColumn != draggedColumn) {
							paintCell(g, cellRect, row, column);
						}
						cellRect.x += columnWidth;
					}
				}
			} else {
				for(int row = rMin; row <= rMax; row++) {
					cellRect = table.getCellRect(row, cMin, false);
					aColumn = cm.getColumn(cMin);
					if(aColumn != draggedColumn) {
						columnWidth = aColumn.getWidth();
						cellRect.width = columnWidth - columnMargin;
						paintCell(g, cellRect, row, cMin);
					}
					for(int column = cMin + 1; column <= cMax; column++) {
						aColumn = cm.getColumn(column);
						columnWidth = aColumn.getWidth();
						cellRect.width = columnWidth - columnMargin;
						cellRect.x -= columnWidth;
						if(aColumn != draggedColumn)
							paintCell(g, cellRect, row, column);
					}
				}
			}
			if(draggedColumn != null) {
				paintDraggedArea(g, rMin, rMax, draggedColumn, header.getDraggedDistance());
			}
			rendererPane.removeAll();
		}

		private void pintarGrillaPagina(Graphics g, int rMin, int rMax, int cMin, int cMax) {
			g.setColor(table.getGridColor());
			Rectangle minCell = table.getCellRect(rMin, cMin, true);
			Rectangle maxCell = table.getCellRect(rMax, cMax, true);
			Rectangle damagedArea = minCell.union(maxCell);
			if(table.getShowHorizontalLines()) {
				int tableWidth = damagedArea.x + damagedArea.width;
				//int y = damagedArea.y;
				int y = 0;
				for(int row = rMin; row <= rMax; row++) {
					y += table.getRowHeight(row);
					g.drawLine(damagedArea.x, y - 1, tableWidth - 1, y - 1);
				}
			}
			if(table.getShowVerticalLines()) {
				TableColumnModel cm = table.getColumnModel();
				//int tableHeight = damagedArea.y + damagedArea.height;
				int tableHeight = damagedArea.height;
				int x;
				if(table.getComponentOrientation().isLeftToRight()) {
					x = damagedArea.x;
					for(int column = cMin; column <= cMax; column++) {
						int w = cm.getColumn(column).getWidth();
						x += w;
						g.drawLine(x - 1, 0, x - 1, tableHeight - 1);
					}
				} else {
					x = damagedArea.x + damagedArea.width;
					for(int column = cMin; column < cMax; column++) {
						int w = cm.getColumn(column).getWidth();
						x -= w;
						g.drawLine(x - 1, 0, x - 1, tableHeight - 1);
					}
					x -= cm.getColumn(cMax).getWidth();
					g.drawLine(x, 0, x, tableHeight - 1);
				}
			}
		}

		public void paint(Graphics g, JComponent c) {
			if(table.getRowCount() <= 0 || table.getColumnCount() <= 0)
				return;
			Rectangle clip = g.getClipBounds();
			Point upperLeft = clip.getLocation();
			Point lowerRight = new Point(clip.x + clip.width - 1, clip.y + clip.height - 1);
			int rMin = table.rowAtPoint(upperLeft);
			int rMax = table.rowAtPoint(lowerRight);
			if(rMin == -1)
				rMin = 0;
			if(rMax == -1)
				rMax = table.getRowCount() - 1;
			boolean ltr = table.getComponentOrientation().isLeftToRight();
			int cMin = table.columnAtPoint(ltr ? upperLeft : lowerRight);
			int cMax = table.columnAtPoint(ltr ? lowerRight : upperLeft);
			if(cMin == -1)
				cMin = 0;
			if(cMax == -1)
				cMax = table.getColumnCount() - 1;
			paintGrid(g, rMin, rMax, cMin, cMax);
			paintCells(g, rMin, rMax, cMin, cMax);
		}

		/**
		 * Método copiado de BasicTableUI.
		 * @param g
		 * @param rMin
		 * @param rMax
		 * @param cMin
		 * @param cMax
		 */
		private void paintGrid(Graphics g, int rMin, int rMax, int cMin, int cMax) {
			g.setColor(table.getGridColor());
			Rectangle minCell = table.getCellRect(rMin, cMin, true);
			Rectangle maxCell = table.getCellRect(rMax, cMax, true);
			Rectangle damagedArea = minCell.union(maxCell);
			if(table.getShowHorizontalLines()) {
				int tableWidth = damagedArea.x + damagedArea.width;
				int y = damagedArea.y;
				for(int row = rMin; row <= rMax; row++) {
					y += table.getRowHeight(row);
					g.drawLine(damagedArea.x, y - 1, tableWidth - 1, y - 1);
				}
			}
			if(table.getShowVerticalLines()) {
				TableColumnModel cm = table.getColumnModel();
				int tableHeight = damagedArea.y + damagedArea.height;
				int x;
				if(table.getComponentOrientation().isLeftToRight()) {
					x = damagedArea.x;
					for(int column = cMin; column <= cMax; column++) {
						int w = cm.getColumn(column).getWidth();
						x += w;
						g.drawLine(x - 1, 0, x - 1, tableHeight - 1);
					}
				} else {
					x = damagedArea.x + damagedArea.width;
					for(int column = cMin; column < cMax; column++) {
						int w = cm.getColumn(column).getWidth();
						x -= w;
						g.drawLine(x - 1, 0, x - 1, tableHeight - 1);
					}
					x -= cm.getColumn(cMax).getWidth();
					g.drawLine(x, 0, x, tableHeight - 1);
				}
			}
		}

		/**
		 * Método adaptado para que soporte la agrupación celdas.
		 * @param g
		 * @param cellRect
		 * @param row
		 * @param column
		 */
		private void paintCell(Graphics g, Rectangle cellRect, int row, int column) {
			FWJTable tabla = (FWJTable)table;
			if(tabla.isCeldaGrupo(row, column)) {
				int filaInicioGrupo = tabla.getFilaInicioGrupoCeldas(row, column);
				int colInicioGrupo = tabla.getColInicioGrupoCeldas(row, column);
				int anchoGrupo = tabla.getAnchoGrupoCeldas(filaInicioGrupo, colInicioGrupo);
				int altoGrupo = tabla.getAltoGrupoCeldas(filaInicioGrupo, colInicioGrupo);
				Color colorTemp;
				if(tabla.getBackgroundCell(filaInicioGrupo, colInicioGrupo) != null)
					colorTemp = tabla.getBackgroundCell(filaInicioGrupo, colInicioGrupo);
				else if(tabla.getBackgroundRow(filaInicioGrupo) != null)
					colorTemp = tabla.getBackgroundRow(filaInicioGrupo);
				else
					colorTemp = tabla.getBackground();
				g.setPaintMode();
				int x1 = cellRect.x;
				int y1 = cellRect.y;
				int w1 = cellRect.width;
				int h1 = cellRect.height;
				int x2 = cellRect.x;
				int y2 = cellRect.y;
				int w2 = cellRect.width;
				int h2 = cellRect.height;
				if(!drawLeftBorder(row, column)) {
					x2--;
					w2++;
				}
				if(!drawBottomBorder(row, column)) {
					h2++;
				}
				//Pintar el borde
				g.setColor(tabla.getGridColor());
				g.fillRect(x1, y1, w1, h1);
				//Pintar el background
				g.setColor(colorTemp);
				g.fillRect(x2, y2, w2, h2);
				//
				Rectangle cellRectTemp = new Rectangle(cellRect);
				cellRectTemp.height = altoGrupo - 1;
				cellRectTemp.y = tabla.getYGrupoCeldas(filaInicioGrupo, colInicioGrupo);
				cellRectTemp.width = anchoGrupo - 1;
				cellRectTemp.x = tabla.getXGrupoCeldas(filaInicioGrupo, colInicioGrupo);
				TableCellRenderer renderer = table.getCellRenderer(row, column);
				Component component = table.prepareRenderer(renderer, row, column);
				component.setBackground(colorTemp);
				pintar(g, cellRectTemp, row, column);
			} else {
				pintar(g, cellRect, row, column);
			}
		}

		/**
		 * Método que pinta las celdas de acuerdo a si pertenecen a una columna
		 * de grupo o a una fila de corte.
		 * @param g
		 * @param cellRect
		 * @param row
		 * @param column
		 */
		private void pintar(Graphics g, Rectangle cellRect, int row, int column) {
			if(table.isEditing() && table.getEditingRow() == row && table.getEditingColumn() == column) {
				Component component = table.getEditorComponent();
				component.setBounds(cellRect);
				component.validate();
			} else {
				TableCellRenderer renderer = table.getCellRenderer(row, column);
				Component component = table.prepareRenderer(renderer, row, column);
				rendererPane.paintComponent(g, component, table, cellRect.x, cellRect.y, cellRect.width, cellRect.height, true);
			}
		}

		/**
		 * Método copiado de BasicTableUI.
		 * @param g
		 * @param rMin
		 * @param rMax
		 * @param cMin
		 * @param cMax
		 */
		public void paintCells(Graphics g, int rMin, int rMax, int cMin, int cMax) {
			JTableHeader header = table.getTableHeader();
			TableColumn draggedColumn = (header == null) ? null : header.getDraggedColumn();
			TableColumnModel cm = table.getColumnModel();
			int columnMargin = cm.getColumnMargin();
			Rectangle cellRect;
			TableColumn aColumn;
			int columnWidth;
			if(table.getComponentOrientation().isLeftToRight()) {
				for(int row = rMin; row <= rMax; row++) {
					cellRect = table.getCellRect(row, cMin, false);
					for(int column = cMin; column <= cMax; column++) {
						aColumn = cm.getColumn(column);
						columnWidth = aColumn.getWidth();
						cellRect.width = columnWidth - columnMargin;
						if(aColumn != draggedColumn) {
							paintCell(g, cellRect, row, column);
						}
						cellRect.x += columnWidth;
					}
				}
			} else {
				for(int row = rMin; row <= rMax; row++) {
					cellRect = table.getCellRect(row, cMin, false);
					aColumn = cm.getColumn(cMin);
					if(aColumn != draggedColumn) {
						columnWidth = aColumn.getWidth();
						cellRect.width = columnWidth - columnMargin;
						paintCell(g, cellRect, row, cMin);
					}
					for(int column = cMin + 1; column <= cMax; column++) {
						aColumn = cm.getColumn(column);
						columnWidth = aColumn.getWidth();
						cellRect.width = columnWidth - columnMargin;
						cellRect.x -= columnWidth;
						if(aColumn != draggedColumn)
							paintCell(g, cellRect, row, column);
					}
				}
			}
			if(draggedColumn != null) {
				paintDraggedArea(g, rMin, rMax, draggedColumn, header.getDraggedDistance());
			}
			rendererPane.removeAll();
		}

		/**
		 * Método copiado de BasicTableUI.
		 * @param aColumn
		 * @return
		 */
		private int viewIndexForColumn(TableColumn aColumn) {
			TableColumnModel cm = table.getColumnModel();
			for(int column = 0; column < cm.getColumnCount(); column++) {
				if(cm.getColumn(column) == aColumn) {
					return column;
				}
			}
			return -1;
		}

		/**
		 * Método copiado de BasicTableUI.
		 * @param g
		 * @param rMin
		 * @param rMax
		 * @param draggedColumn
		 * @param distance
		 */
		private void paintDraggedArea(Graphics g, int rMin, int rMax, TableColumn draggedColumn, int distance) {
			int draggedColumnIndex = viewIndexForColumn(draggedColumn);
			Rectangle minCell = table.getCellRect(rMin, draggedColumnIndex, true);
			Rectangle maxCell = table.getCellRect(rMax, draggedColumnIndex, true);
			Rectangle vacatedColumnRect = minCell.union(maxCell);
			g.setColor(table.getParent().getBackground());
			g.fillRect(vacatedColumnRect.x, vacatedColumnRect.y, vacatedColumnRect.width, vacatedColumnRect.height);
			vacatedColumnRect.x += distance;
			g.setColor(table.getBackground());
			g.fillRect(vacatedColumnRect.x, vacatedColumnRect.y, vacatedColumnRect.width, vacatedColumnRect.height);
			if(table.getShowVerticalLines()) {
				g.setColor(table.getGridColor());
				int x1 = vacatedColumnRect.x;
				int y1 = vacatedColumnRect.y;
				int x2 = x1 + vacatedColumnRect.width - 1;
				int y2 = y1 + vacatedColumnRect.height - 1;
				g.drawLine(x1 - 1, y1, x1 - 1, y2);
				g.drawLine(x2, y1, x2, y2);
			}
			for(int row = rMin; row <= rMax; row++) {
				Rectangle r = table.getCellRect(row, draggedColumnIndex, false);
				r.x += distance;
				paintCell(g, r, row, draggedColumnIndex);
				if(table.getShowHorizontalLines()) {
					g.setColor(table.getGridColor());
					Rectangle rcr = table.getCellRect(row, draggedColumnIndex, true);
					rcr.x += distance;
					int x1 = rcr.x;
					int y1 = rcr.y;
					int x2 = x1 + rcr.width - 1;
					int y2 = y1 + rcr.height - 1;
					g.drawLine(x1, y2, x2, y2);
				}
			}
		}
	}

	/**
	 * Renderer de los headers.
	 */
	public class FWJTableHeaderRenderer extends JLabel implements TableCellRenderer {

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {
			JLabel header = (JLabel)this;
			header.setBorder(BorderFactory.createLineBorder(getGridColor()));
			header.setOpaque(true);
			header.setBackground(headerBackground);
			header.setForeground(headerForeground);
			header.setText(value.toString());
			header.setFont(getTableHeader().getFont());
			if(isColInicionGrupoHeader(vColIndex) || vColIndex == -1)
				header.setHorizontalAlignment(CENTER_ALIGN);
			else if(tiposColumnas[vColIndex] != null)
				header.setHorizontalAlignment(tiposColumnas[vColIndex].getHeaderAlign());
			return header;
		}
	}

	/**
	 * Devuelve un vector con los datos necesarios para generar un gráfico de
	 * torta.
	 * @param filaDesde
	 * @param filaHasta
	 * @param colCategoria
	 * @param colData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Vector getDataPieChart(int filaDesde, int filaHasta, int colCategoria, int colData) {
		Vector categorias = new Vector();
		Vector categoriasTemp = new Vector();
		Vector dataChart = new Vector();
		Vector valores = new Vector();
		Vector dataVector = getDataVector();
		for(int i = filaDesde; i <= filaHasta; i++) {
			Object categoria = ((Vector)dataVector.get(i)).get(colCategoria);
			if(categoria != null) {
				if(!categoriasTemp.contains(categoria))
					categoriasTemp.add(categoria);
			}
		}
		for(Iterator i = categoriasTemp.iterator(); i.hasNext();) {
			int total = 0;
			Object categoria = i.next();
			for(int j = filaDesde; j <= filaHasta; j++) {
				Object categoriaTemp = ((Vector)dataVector.get(j)).get(colCategoria);
				if(categoriaTemp != null) {
					if(categoria.equals(categoriaTemp)) {
						int valor = ((Integer)((Vector)dataVector.get(j)).get(colData)).intValue();
						total += valor;
					}
				}
			}
			categorias.add(categoria);
			valores.add(new Integer(total));
		}
		dataChart.add(categorias);
		dataChart.add(valores);
		return dataChart;
	}

	/**
	 * Devuelve un vector con los datos necesarios para generar un gráfico de
	 * barras.
	 * @param filaDesde
	 * @param filaHasta
	 * @param colCategorias
	 * @param colSeries
	 * @param colData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Vector getDataBarChart(int filaDesde, int filaHasta, int colCategorias, int colSeries, int colData) {
		Vector dataCategorias = new Vector();
		Vector dataChart = new Vector();
		Vector nombresCategorias = new Vector();
		Vector nombresSeries = new Vector();
		Vector dataVector = getDataVector();
		for(int i = filaDesde; i <= filaHasta; i++) {
			Object nombreCategoria = ((Vector)dataVector.get(i)).get(colCategorias);
			if(nombreCategoria != null) {
				if(!nombresCategorias.contains(nombreCategoria))
					nombresCategorias.add(nombreCategoria);
				Object nombreSerie = ((Vector)dataVector.get(i)).get(colSeries);
				if(!nombresSeries.contains(nombreSerie))
					nombresSeries.add(nombreSerie);
			}
		}
		for(Iterator i = nombresSeries.iterator(); i.hasNext();) {
			Vector dataSerie = new Vector();
			Object nombreSerie = i.next();
			for(int j = filaDesde; j <= filaHasta; j++) {
				Object nombreSerieTemp = ((Vector)dataVector.get(j)).get(colSeries);
				if(nombreSerieTemp != null) {
					if(nombreSerieTemp.equals(nombreSerie)) {
						Object data = ((Vector)dataVector.get(j)).get(colData);
						dataSerie.add(data);
					}
				}
			}
			dataCategorias.add(dataSerie);
		}
		dataChart.add(nombresCategorias);
		dataChart.add(nombresSeries);
		dataChart.add(dataCategorias);
		return dataChart;
	}

	public Object[][] getDataVector(int filaDesde, int filaHasta, int colDesde, int colHasta) {
		Vector dataVectorOriginal = getDataVector();
		Object[][] dataVectorResultado = new Object[filaHasta - filaDesde + 1][colHasta - colDesde + 1];
		int contFilas = 0;
		for(int i = filaDesde; i <= filaHasta; i++) {
			int contCols = 0;
			for(int j = colDesde; j <= colHasta; j++) {
				dataVectorResultado[contFilas][contCols] = ((Vector)dataVectorOriginal.get(i)).get(j);
				contCols++;
			}
			contFilas++;
		}
		return dataVectorResultado;
	}

	public Object getValueForExcelAt (int fila,int col){
		return getValueAt(fila, col);
	}
	
	/**
	 * Retorna el valor formateado de una celda.
	 * @param fila
	 * @param col
	 * @return
	 */
	public String getFormattedValue(int fila, int col) {
		Object value = getValueAt(fila, col);
		FWColumn tipoColumna = tiposColumnas[col];
		FWCell celda = (FWCell)typesCells.get(fila + "," + col);
		if(value != null) {
			if(tipoColumna instanceof IntColumn || (celda != null && celda.getType() == INT_CELL)) {
				String mask;
				if(celda != null)
					mask = celda.getMask();
				else
					mask = ((IntColumn)tipoColumna).getMask();
				if(mask != null) {
					NumberFormat nf = new DecimalFormat(mask);
					try {
						String valor = nf.format(value);
						return valor;
					} catch(Exception e) {
						return value.toString();
					}
				} else
					return value.toString();
			} else if(tipoColumna instanceof FloatColumn || (celda != null && celda.getType() == FLOAT_CELL)) {
					String mask;
					if(celda != null)
						mask = celda.getMask();
					else
						mask = ((FloatColumn)tipoColumna).getMask();
					if(mask != null) {
						NumberFormat nf = new DecimalFormat(mask);
						try {
							String valor = nf.format(value);
							return valor;
						} catch(Exception e) {
							return value.toString();
						}
					} else
						return value.toString();
			} else if(tipoColumna instanceof DateColumn || (celda != null && celda.getType() == DATE_CELL)) {
				String mask;
				if(celda != null)
					mask = celda.getMask();
				else
					mask = ((DateColumn)tipoColumna).getMask();
				if(mask != null) {
					DateFormat df = DateUtil.getSimpleDateFormat(mask);
					try {
						String valor = df.format(value);
						return valor;
					} catch(Exception e) {
						return value.toString();
					}
				} else if(tableDateMask != null) {
					DateFormat df = DateUtil.getSimpleDateFormat(tableDateMask);
					try {
						String valor = df.format(value);
						return valor;
					} catch(Exception e) {
						return value.toString();
					}
				} else
					return value.toString();
			} else if(tipoColumna instanceof TimeColumn || (celda != null && celda.getType() == TIME_CELL)) {
				String mask;
				if(celda != null)
					mask = celda.getMask();
				else
					mask = ((TimeColumn)tipoColumna).getMask();
				if(mask != null) {
					DateFormat df = DateUtil.getSimpleDateFormat(mask);
					try {
						String valor = df.format(value);
						return valor;
					} catch(Exception e) {
						return value.toString();
					}
				} else if(tableTimeMask != null) {
					DateFormat df = DateUtil.getSimpleDateFormat(tableTimeMask);
					try {
						String valor = df.format(value);
						return valor;
					} catch(Exception e) {
						return value.toString();
					}
				} else
					return value.toString();
			} else if(tipoColumna instanceof OperableTimeColumn) {
				if(((OperableTimeColumn)tipoColumna).isDifFechas()) {
					if(value instanceof java.util.Date)
						return DateUtil.getHorasMinutos((java.util.Date)value);
					else
						return value.toString();
				} else {
					String mask = ((OperableTimeColumn)tipoColumna).getMask();
					if(mask != null) {
						DateFormat df = DateUtil.getSimpleDateFormat(mask);
						try {
							String valor = df.format(value);
							return valor;
						} catch(Exception e) {
							return value.toString();
						}
					}
				}
			} else
				return value.toString();
		}
		return "";
	}
	
	
	/**
	 * Crea un header múltiple para las columnas especificadas.
	 * @param nombre
	 * @param colInicio
	 * @param colFin
	 */
	public void agregarGrupoColumnas(String nombre, int colInicio, int colFin) {
		GrupoColumnas grupoColumnas = new GrupoColumnas(getTableHeader().getDefaultRenderer(), nombre);
		TableColumnModel columnModel = getColumnModel();
		for(int i = colInicio; i <= colFin; i++)
			grupoColumnas.add(columnModel.getColumn(i));
		((FWJTableHeader)getTableHeader()).agregarGrupoColumnas(grupoColumnas);
	}

	/**
	 * Retorna si se permite o no el uso de headers múltiples.
	 * @return permiteHeaderMultiple
	 */
	public boolean permiteHeaderMultiple() {
		return permiteHeaderMultiple;
	}

	/**
	 * Retorna si se permite o no el uso de headers múltiples.
	 * @param permiteHeaderMultiple
	 */
	public void permiteHeaderMultiple(boolean permiteHeaderMultiple) {
		if(this.permiteHeaderMultiple != permiteHeaderMultiple) {
			this.permiteHeaderMultiple = permiteHeaderMultiple;
			if(!permiteHeaderMultiple)
				((FWJTableHeader)getTableHeader()).cleanColumnGroups();			
		}
	}
	
	public boolean isMultilineColumn(int col) {
		return tiposColumnas[col] instanceof MultilineColumn;
	}

	/**
	 * Clase que permite copiar y pegar en Excel los valores de las filas y
	 * columnas seleccionadas y los headers de las columnas.
	 */
	@SuppressWarnings("unused")
	private class AdaptadorExcel implements ActionListener {
		String contenidoFila;
		String valor;
		Clipboard clipBoard;
		StringSelection stringSelection;

		public AdaptadorExcel() {
			KeyStroke copiar = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK, false);
			registerKeyboardAction(this, "copiar", copiar, JComponent.WHEN_FOCUSED);
			clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
		}

		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().compareTo("copiar") == 0) {
				StringBuffer contenidoFilasTemp = new StringBuffer();
				int cantFilas = getSelectedRowCount();
				int cantCols = getSelectedColumnCount();
				int[] filasSeleccionadas = getSelectedRows();
				int[] colsSeleccionadas = getSelectedColumns();
				if((cantFilas - 1 == filasSeleccionadas[filasSeleccionadas.length - 1] - filasSeleccionadas[0] && cantFilas == filasSeleccionadas.length)
						&& (cantCols - 1 == colsSeleccionadas[colsSeleccionadas.length - 1] - colsSeleccionadas[0] && cantCols == colsSeleccionadas.length)) {
					for(int i = 0; i < cantCols; i++) {
						String nombreCol = (String)(getColumnModel().getColumn(colsSeleccionadas[i]).getHeaderValue());
						contenidoFilasTemp.append(nombreCol);
						if(i < cantCols - 1)
							contenidoFilasTemp.append("\t");
						else
							contenidoFilasTemp.append("\n");
					}
					for(int i = 0; i < cantFilas; i++) {
						for(int j = 0; j < cantCols; j++) {
							String contenidoCelda = getFormattedValue(filasSeleccionadas[i], colsSeleccionadas[j]);
							contenidoFilasTemp.append(contenidoCelda);
							if(j < cantCols - 1)
								contenidoFilasTemp.append("\t");
							else
								contenidoFilasTemp.append("\n");
						}
					}
					stringSelection = new StringSelection(contenidoFilasTemp.toString());
					clipBoard.setContents(stringSelection, stringSelection);
				}
			}
		}
	}

	/** Clase para el menú emergente de la tabla */
	protected class PopupMenu extends JPopupMenu {
		public PopupMenu() {
			if(permiteCopiar) {
				JMenuItem copiarTodo = new JMenuItem("Copiar todo");
				copiarTodo.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent evt) {
						copiarSeleccion(getIndicesFilas(), getIndicesColumnasVisibles());
					}
				});
				add(copiarTodo);
				JMenuItem copiarSeleccion = new JMenuItem("Copiar selección");
				copiarSeleccion.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent evt) {
						if(getRowSelectionAllowed())
							copiarSeleccion(getSelectedRows(), getIndicesColumnasVisibles());
						else
							copiarSeleccion(getSelectedRows(), getSelectedColumns());
					}
				});
				add(copiarSeleccion);
			}
		}
	}

	public int[] getIndicesFilas() {
		int[] indicesFilas = new int[getRowCount()];
		for(int i = 0; i < getRowCount(); i++)
			indicesFilas[i] = i;
		return indicesFilas;
	}

	@SuppressWarnings("unchecked")
	public int[] getIndicesColumnasVisibles() {
		List columnas = new ArrayList();
		for(int i = 0; i < getColumnCount(); i++) {
			if(columnModel.getColumn(i).getWidth() != 0)
				columnas.add(tiposColumnas[i]);
		}
		int indicesColumnas[] = new int[columnas.size()];
		for(int i = 0; i < columnas.size(); i++)
			indicesColumnas[i] = ((FWColumn)columnas.get(i)).getIndice();
		return indicesColumnas;
	}

	public boolean isColumnaVisible(int col) {
		if(columnModel.getColumn(col).getWidth() != 0)
			return true;
		return false;
	}

	protected void copiarSeleccion(int filasSeleccionadas[], int colsSeleccionadas[]) {
//		String contenidoFila;
//		String valor;
		Clipboard clipBoard;
		StringSelection stringSelection;
		clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringBuffer contenidoFilasTemp = new StringBuffer();
		int cantFilas = filasSeleccionadas.length;
		int cantCols = colsSeleccionadas.length;
		if((cantFilas - 1 == filasSeleccionadas[filasSeleccionadas.length - 1] - filasSeleccionadas[0] && cantFilas == filasSeleccionadas.length)
				&& (cantCols - 1 == colsSeleccionadas[colsSeleccionadas.length - 1] - colsSeleccionadas[0] && cantCols == colsSeleccionadas.length)) {
			for(int i = 0; i < cantCols; i++) {
				String nombreCol = (String)(getColumnModel().getColumn(colsSeleccionadas[i]).getHeaderValue());
				contenidoFilasTemp.append(nombreCol);
				if(i < cantCols - 1)
					contenidoFilasTemp.append("\t");
				else
					contenidoFilasTemp.append("\n");
			}
			for(int i = 0; i < cantFilas; i++) {
				for(int j = 0; j < cantCols; j++) {
					String contenidoCelda = getFormattedValue(filasSeleccionadas[i], colsSeleccionadas[j]);
					contenidoFilasTemp.append(contenidoCelda);
					if(j < cantCols - 1)
						contenidoFilasTemp.append("\t");
					else
						contenidoFilasTemp.append("\n");
				}
			}
			stringSelection = new StringSelection(contenidoFilasTemp.toString());
			clipBoard.setContents(stringSelection, stringSelection);
		}
	}

	/** Clase para el manejo de los eventos del mouse */
	private class TablaMouseAdapter extends MouseAdapter {
		public void mousePressed(MouseEvent evt) {
			if(evt.isPopupTrigger()) {
				PopupMenu popup = new PopupMenu();
				popup.show(evt.getComponent(), evt.getX(), evt.getY());
			}
		}

		public void mouseReleased(MouseEvent evt) {
			if(evt.isPopupTrigger()) {
				PopupMenu popup = new PopupMenu();
				popup.show(evt.getComponent(), evt.getX(), evt.getY());
			}
		}
	}
	/** Clase para poder utilizar headers múltiples */
	public class GrupoColumnas {
		protected TableCellRenderer renderer;
		protected Vector columnas;
		protected String nombre;

		public GrupoColumnas(String nombre) {
			this(null, nombre);
		}

		public GrupoColumnas(TableCellRenderer renderer, String nombre) {
			if(renderer == null) {
				this.renderer = new DefaultTableCellRenderer() {
					public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
						JTableHeader header = table.getTableHeader();
						if(header != null) {
							setForeground(header.getForeground());
							setBackground(header.getBackground());
							setFont(header.getFont());
						}
						setHorizontalAlignment(JLabel.CENTER);
						setText((value == null) ? "" : value.toString());
						setBorder(UIManager.getBorder("TableHeader.cellBorder"));
						return this;
					}
				};
			} else
				this.renderer = renderer;
			this.nombre = nombre;
			columnas = new Vector();
		}

		@SuppressWarnings("unchecked")
		public void add(Object obj) {
			if(obj != null) {
				columnas.addElement(obj);
			}
		}

		@SuppressWarnings("unchecked")
		public Vector getColumnGroups(TableColumn columna, Vector columnasTemp) {
			columnasTemp.addElement(this);
			if(columnas.contains(columna)) {
				return columnasTemp;
			}
			Enumeration e = columnas.elements();
			while(e.hasMoreElements()) {
				Object obj = e.nextElement();
				if(obj instanceof GrupoColumnas) {
					Vector columnasTemp2 = (Vector)((GrupoColumnas)obj).getColumnGroups(columna, (Vector)columnasTemp.clone());
					if(columnasTemp2 != null)
						return columnasTemp2;
				}
			}
			return null;
		}

		public TableCellRenderer getHeaderRenderer() {
			return renderer;
		}

		public void setHeaderRenderer(TableCellRenderer renderer) {
			if(renderer != null)
				this.renderer = renderer;
		}

		public Object getHeaderValue() {
			return nombre;
		}

		public Dimension getSize(JTable table) {
			Component comp = renderer.getTableCellRendererComponent(table, getHeaderValue(), false, false, -1, -1);
			int alto = comp.getPreferredSize().height;
			int ancho = 0;
			Enumeration e = columnas.elements();
			while(e.hasMoreElements()) {
				Object obj = e.nextElement();
				if(obj instanceof TableColumn) {
					TableColumn aColumn = (TableColumn)obj;
					ancho += aColumn.getWidth();
				} else
					ancho += ((GrupoColumnas)obj).getSize(table).width;
			}
			return new Dimension(ancho, alto);
		}

		public Vector getColumnas() {
			return columnas;
		}
	}

	/** JTableHeader adaptado para soportar headers múltiples */
	public class FWJTableHeader extends JTableHeader {
		@SuppressWarnings("unused")
		private static final String uiClassID = "GroupableTableHeaderUI";
		protected Vector gruposColumnas = null;

		public FWJTableHeader(TableColumnModel model) {
			super(model);
			setUI(new FWJTableHeaderUI());
			setReorderingAllowed(false);
		}

		public void setReorderingAllowed(boolean b) {
			reorderingAllowed = b;
//			if(permiteHeaderMultiple)
//				reorderingAllowed = false;
//			else
//				reorderingAllowed = b;
		}
		
		public boolean getReorderingAllowed() {
			if(gruposColumnas != null && !gruposColumnas.isEmpty()) {
				return false;
			}
			return reorderingAllowed;
		}

		@SuppressWarnings("unchecked")
		public void agregarGrupoColumnas(GrupoColumnas grupoColumnas) {
			setReorderingAllowed(false);
			if(gruposColumnas == null) {
				gruposColumnas = new Vector();
			}
			gruposColumnas.addElement(grupoColumnas);
		}

		public Enumeration getGruposColumnas(TableColumn col) {
			if(gruposColumnas != null) {
				Enumeration e = gruposColumnas.elements();
				while(e.hasMoreElements()) {
					GrupoColumnas grupoColumnas = (GrupoColumnas)e.nextElement();
					Vector gruposColumnasTemp = (Vector)grupoColumnas.getColumnGroups(col, new Vector());
					if(gruposColumnasTemp != null)
						return gruposColumnasTemp.elements();
				}
			}
			return null;
		}

		public void cleanColumnGroups() {
			gruposColumnas.clear();
		}

		public Vector getGruposColumnas() {
			return gruposColumnas;
		}
	}

	/**
	 * BasicTableHeaderUI desarrollada para soportar agrupación de headers y
	 * header múltiples.
	 */
	private class FWJTableHeaderUI extends BasicTableHeaderUI {
		/**
		 * Método copiado de BasicTableHeaderUI.
		 * @param columnIndex
		 * @return
		 */
		private Component getHeaderRenderer(int columnIndex) {
			TableColumn aColumn = header.getColumnModel().getColumn(columnIndex);
			TableCellRenderer renderer = aColumn.getHeaderRenderer();
			if(renderer == null)
				renderer = header.getDefaultRenderer();
			return renderer.getTableCellRendererComponent(header.getTable(), aColumn.getHeaderValue(), false, false, -1, columnIndex);
		}

		/**
		 * Método adaptado para soportar headers múltiples.
		 */
		@SuppressWarnings("unchecked")
		public void paint(Graphics g, JComponent c) {
			if(((FWJTable)header.getTable()).permiteHeaderMultiple) {
				Rectangle clipBounds = g.getClipBounds();
				if(header.getColumnModel() == null)
					return;
				int column = 0;
				Dimension size = header.getSize();
				Rectangle cellRect = new Rectangle(0, 0, size.width, size.height);
				Hashtable h = new Hashtable();
//				int columnMargin = header.getColumnModel().getColumnMargin();
				Enumeration enumeration = header.getColumnModel().getColumns();
				while(enumeration.hasMoreElements()) {
					cellRect.height = size.height;
					cellRect.y = 0;
					TableColumn aColumn = (TableColumn)enumeration.nextElement();
					Enumeration cGroups = ((FWJTableHeader)header).getGruposColumnas(aColumn);
					if(cGroups != null) {
						int groupHeight = 0;
						while(cGroups.hasMoreElements()) {
							GrupoColumnas cGroup = (GrupoColumnas)cGroups.nextElement();
							Rectangle groupRect = (Rectangle)h.get(cGroup);
							if(groupRect == null) {
								groupRect = new Rectangle(cellRect);
								Dimension d = cGroup.getSize(header.getTable());
								groupRect.width = d.width;
								groupRect.height = d.height;
								h.put(cGroup, groupRect);
							}
							paintCell(g, groupRect, cGroup);
							groupHeight += groupRect.height;
							cellRect.height = size.height - groupHeight;
							cellRect.y = groupHeight;
						}
					}
					cellRect.width = aColumn.getWidth();
					if(cellRect.intersects(clipBounds)) {
						paintCell(g, cellRect, column);
					}
					cellRect.x += cellRect.width;
					column++;
				}
			} else {
				if(header.getColumnModel().getColumnCount() <= 0)
					return;
				boolean ltr = header.getComponentOrientation().isLeftToRight();
				Rectangle clip = g.getClipBounds();
				Point left = clip.getLocation();
				Point right = new Point(clip.x + clip.width - 1, clip.y);
				TableColumnModel cm = header.getColumnModel();
				int cMin = header.columnAtPoint(ltr ? left : right);
				int cMax = header.columnAtPoint(ltr ? right : left);
				if(cMin == -1)
					cMin = 0;
				if(cMax == -1)
					cMax = cm.getColumnCount() - 1;
				TableColumn draggedColumn = header.getDraggedColumn();
				int columnWidth;
				int columnMargin = cm.getColumnMargin();
				Rectangle cellRect = header.getHeaderRect(cMin);
				TableColumn aColumn;
				if(ltr) {
					for(int column = cMin; column <= cMax; column++) {
						aColumn = cm.getColumn(column);
						columnWidth = aColumn.getWidth();
						cellRect.width = columnWidth - columnMargin;
						if(aColumn != draggedColumn)
							paintCell(g, cellRect, column);
						cellRect.x += columnWidth;
					}
				} else {
					aColumn = cm.getColumn(cMin);
					if(aColumn != draggedColumn) {
						columnWidth = aColumn.getWidth();
						cellRect.width = columnWidth - columnMargin;
						cellRect.x += columnMargin;
						paintCell(g, cellRect, cMin);
					}
					for(int column = cMin + 1; column <= cMax; column++) {
						aColumn = cm.getColumn(column);
						columnWidth = aColumn.getWidth();
						cellRect.width = columnWidth - columnMargin;
						cellRect.x -= columnWidth;
						if(aColumn != draggedColumn)
							paintCell(g, cellRect, column);
					}
				}
				if(draggedColumn != null) {
					int draggedColumnIndex = viewIndexForColumn(draggedColumn);
					Rectangle draggedCellRect = header.getHeaderRect(draggedColumnIndex);
					g.setColor(header.getParent().getBackground());
					g.fillRect(draggedCellRect.x, draggedCellRect.y, draggedCellRect.width, draggedCellRect.height);
					draggedCellRect.x += header.getDraggedDistance();
					g.setColor(header.getBackground());
					g.fillRect(draggedCellRect.x, draggedCellRect.y, draggedCellRect.width, draggedCellRect.height);
					paintCell(g, draggedCellRect, draggedColumnIndex);
				}
				rendererPane.removeAll();
			}
		}

		/**
		 * Método adaptado para soportar la agrupación de headers y headers
		 * múltiples.
		 * @param g
		 * @param cellRect
		 * @param columnIndex
		 */
		private void paintCell(Graphics g, Rectangle cellRect, int columnIndex) {
			FWJTable tabla = (FWJTable)header.getTable();
			if(tabla.permiteHeaderMultiple) {
				TableColumn aColumn = header.getColumnModel().getColumn(columnIndex);
				TableCellRenderer renderer = getTableHeader().getDefaultRenderer();
				Component component = renderer.getTableCellRendererComponent(header.getTable(), aColumn.getHeaderValue(), false, false, -1, columnIndex);
				rendererPane.add(component);
				rendererPane.paintComponent(g, component, header, cellRect.x, cellRect.y, cellRect.width, cellRect.height, true);
			} else {
				if(tabla.isColInicionGrupoHeader(columnIndex)) {
					int ancho = tabla.getAnchoGrupoHeader(columnIndex);
					TableColumn columna = header.getColumnModel().getColumn(columnIndex);
					TableCellRenderer renderer = columna.getHeaderRenderer();
					if(renderer == null)
						renderer = header.getDefaultRenderer();
					Component component = renderer.getTableCellRendererComponent(tabla, columna.getHeaderValue(), false, false, -1, columnIndex);
					g.setColor(header.getBackground());
					g.fillRect(cellRect.x + 1, cellRect.y, ancho - 1, cellRect.height);
					rendererPane.paintComponent(g, component, header, cellRect.x, cellRect.y, ancho, cellRect.height, true);
				} else if(tabla.isColGrupoHeader(columnIndex)) {
					int ancho = tabla.getAnchoGrupoHeader(columnIndex);
					g.setColor(header.getBackground());
					g.fillRect(cellRect.x + 1, cellRect.y, ancho - 1, cellRect.height);
					Rectangle cellRectTemp = new Rectangle(cellRect);
					cellRectTemp.width = ancho - 1;
					pintar(g, cellRectTemp, columnIndex);
				} else
					pintar(g, cellRect, columnIndex);
			}
		}

		/**
		 * Método para pintar header múltiples.
		 * @param g
		 * @param cellRect
		 * @param cGroup
		 */
		private void paintCell(Graphics g, Rectangle cellRect, GrupoColumnas cGroup) {
			TableCellRenderer renderer = cGroup.getHeaderRenderer();
			Component component = renderer.getTableCellRendererComponent(header.getTable(), cGroup.getHeaderValue(), false, false, -1, -1);
			rendererPane.add(component);
			rendererPane.paintComponent(g, component, header, cellRect.x, cellRect.y, cellRect.width, cellRect.height, true);
		}

		/**
		 * Método paintCell de BasicTableHeaderUI.
		 * @param g
		 * @param cellRect
		 * @param columnIndex
		 */
		private void pintar(Graphics g, Rectangle cellRect, int columnIndex) {
			Component component = getHeaderRenderer(columnIndex);
			rendererPane.paintComponent(g, component, header, cellRect.x, cellRect.y, cellRect.width, cellRect.height, true);
		}

		/**
		 * Método copiado de BasicTableHeaderUI.
		 * @param aColumn
		 * @return
		 */
		private int viewIndexForColumn(TableColumn aColumn) {
			TableColumnModel cm = header.getColumnModel();
			for(int column = 0; column < cm.getColumnCount(); column++) {
				if(cm.getColumn(column) == aColumn)
					return column;
			}
			return -1;
		}

		/**
		 * Método adaptado para soportar mulptiples headers.
		 * @return
		 */
		private int getHeaderHeight() {
			int height = 0;
			TableColumnModel columnModel = header.getColumnModel();
			for(int column = 0; column < columnModel.getColumnCount(); column++) {
				TableColumn aColumn = columnModel.getColumn(column);
				TableCellRenderer renderer = getTableHeader().getDefaultRenderer();
				if(renderer != null) {
					Component comp = renderer.getTableCellRendererComponent(header.getTable(), aColumn.getHeaderValue(), false, false, -1, column);
					int cHeight = comp.getPreferredSize().height;
					if(((FWJTable)header.getTable()).permiteHeaderMultiple) {
						Enumeration e = ((FWJTableHeader)header).getGruposColumnas(aColumn);
						if(e != null) {
							while(e.hasMoreElements()) {
								GrupoColumnas grupoColumnas = (GrupoColumnas)e.nextElement();
								cHeight += grupoColumnas.getSize(header.getTable()).height;
							}
						}
						height = Math.max(height, cHeight);
					} else {
//						Component component = getHeaderRenderer(column);
						int rendererHeight = comp.getPreferredSize().height;
						height = Math.max(height, rendererHeight);
					}
				}
			}
			return height;
		}

		/**
		 * Método copiado de BasicTableHeaderUI.
		 * @param width
		 * @return
		 */
		private Dimension createHeaderSize(long width) {
			TableColumnModel columnModel = header.getColumnModel();
			width += columnModel.getColumnMargin() * columnModel.getColumnCount();
			if(width > Integer.MAX_VALUE) {
				width = Integer.MAX_VALUE;
			}
			return new Dimension((int)width, getHeaderHeight());
		}

		/**
		 * Método copiado de BasicTableHeaderUI.
		 */
		public Dimension getPreferredSize(JComponent c) {
			long width = 0;
			Enumeration columns = header.getColumnModel().getColumns();
			while(columns.hasMoreElements()) {
				TableColumn aColumn = (TableColumn)columns.nextElement();
				width = width + aColumn.getPreferredWidth();
			}
			return createHeaderSize(width);
		}
	}

	public Vector getGruposColumnas() {
		return ((FWJTableHeader)getTableHeader()).getGruposColumnas();
	}

	public FWColumnChooser getColumnChooser() {
		return columnChooser;
	}

	public String getMask(int fila, int columna) {
		FWCell cell = (FWCell)typesCells.get(fila + "," + columna);
		if(cell != null) {
			return cell.getMask();
		} else {
			return tiposColumnas[columna].getMask();
		}
	}

	public int getIndiceActual(int indiceOriginal) {
		for(FWColumn columna : tiposColumnas) {
			if(columna.getIndiceOriginal() == indiceOriginal) {
				return columna.getIndice();
			}
		}
		return -1;
	}
	
	public void setTipoLlenado(int llenado) {
		this.llenado = llenado;
	}
	
	public void distribuirEspacioLibre() {
		int cantColumnas = getColumnCount();
		int cantColumnasVisibles = 0;
		int ultimaColVisible = 0;
		for(int i = 0; i < cantColumnas; i++) {
			if(getColumnModel().getColumn(i).getWidth() != 0) {
				cantColumnasVisibles++;
				ultimaColVisible = i;
			}
		}
		if(cantColumnasVisibles != 0) {
			int espacioColumna = (int)Math.ceil(getWidthUnused() / cantColumnasVisibles);
			for(int i = 0; i < cantColumnas; i++) {
				TableColumn columna = getColumnModel().getColumn(i);
				if(columna.getWidth() != 0) {
					columna.setPreferredWidth(columna.getPreferredWidth() + espacioColumna + (i != ultimaColVisible ? 0 : 1));					
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public int[] getIndicesColumnasOcultas() {
		List columnas = new ArrayList();
		for(int i = 0; i < getColumnCount(); i++) {
			if(columnModel.getColumn(i).getWidth() == 0)
				columnas.add(tiposColumnas[i]);
		}
		int indicesColumnas[] = new int[columnas.size()];
		for(int i = 0; i < columnas.size(); i++)
			indicesColumnas[i] = ((FWColumn)columnas.get(i)).getIndice();
		return indicesColumnas;
	}
	
}
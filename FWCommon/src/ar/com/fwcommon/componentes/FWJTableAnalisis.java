package ar.com.fwcommon.componentes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import ar.com.fwcommon.util.DecorateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.ImageUtil;

@SuppressWarnings({"rawtypes", "unchecked"})
public class FWJTableAnalisis extends FWJTable {

	private static final long serialVersionUID = 1575612557733986294L;

	public static Font fuente;
    public static final int TIPO_GRUPO = 1;
    public static final int TIPO_INFO = 2;
    public static final int TIPO_DATO = 3;
    public static final String OPERACION_SUM = "SUM";
    public static final String OPERACION_CONT = "CONT";
    public static final String OPERACION_PROM = "PROM";
    public static final String OPERACION_MIN = "MIN";
    public static final String OPERACION_MAX = "MAX";
    public static final String OPERACION_VAL = "VAL";
    public static final String OPERACION_PORC = "PORC";
    public static final String MENSAJE_NO_CALCULABLE = "No Calculable";
    public static final Color COLOR_GRUPO_DEFAULT = new Color(248, 247, 245);
    public static final Color COLOR_CORTE_DEFAULT = new Color(220, 227, 229);
    public static final Color COLOR_CORTE_PRINC_DEFAULT = new Color(221, 222, 211);
    public static final Color COLOR_TOTAL_DEFAULT = new Color(191, 192, 176);
    private ColeccionTipoDato coleccion;
    private Vector cortesControl;
    private boolean analisis;
    private boolean sumarizarGrupos;
    private FWColumn[] tiposColumnasBackUp;
    private int[] anchosBackUp;
    private TableCellRenderer[] renderersBackUp;
    private TableCellEditor[] editorsBackUp;
    private Object[] headersBackUp;
    private Object[][] datosBackUp;
    private Hashtable backgroundColorCellsBackUp;
    private Hashtable backgroundColorRowsBackUp;
    private boolean rowSelectionAllowed;
    private boolean colSelectionAllowed;
    private boolean cellSelectionEnabled;
    private boolean sortingAllowed;
    private boolean reorderingAllowed;
    private boolean ordenar = true;
    private boolean operar = true;
    private boolean seleccionarBloqueCeldas = true;
    private boolean seleccionEnBloque = false;
    //private Hashtable tooltipCellsBackUp;
    private Hashtable lockCellsBackUp;
    private boolean permiteSelector = false;
    private FWJTableAnalisisRenderer tableRenderer;
    private Color colorGrupo = COLOR_GRUPO_DEFAULT;
    private Color colorCorte = COLOR_CORTE_DEFAULT;
    private Color colorCortePrincipal = COLOR_CORTE_PRINC_DEFAULT;
    private Color colorTotal = COLOR_TOTAL_DEFAULT;
    private boolean analisisReducidoGral;
    
    public FWJTableAnalisis(int filas, int cols) {
        super(filas, cols);
        getTableHeader().setReorderingAllowed(false);
        addMouseListener(new TablaMouseAdapter());
        coleccion = new ColeccionTipoDato();
        cortesControl = new Vector();
        tableRenderer = new FWJTableAnalisisRenderer();
        setDefaultRenderer(Object.class, tableRenderer);
        setDefaultRenderer(Integer.class, tableRenderer);
        setSelectionForeground(Color.BLACK);
    }

    public FWJTableAnalisis() {
        super();
        getTableHeader().setReorderingAllowed(false);
        addMouseListener(new TablaMouseAdapter());
        coleccion = new ColeccionTipoDato();
        cortesControl = new Vector();
        tableRenderer = new FWJTableAnalisisRenderer();
        setDefaultRenderer(Object.class, tableRenderer);
        setDefaultRenderer(Integer.class, tableRenderer);
        setSelectionForeground(Color.BLACK);
    }

    /**
     * Devuelve <b>true </b> si la tabla permite desplegar el selector.
     * @return
     */
    public boolean isPermiteSelector() {
        return permiteSelector;
    }

    /**
     * Setea si la tabla permite o no desplegar el selector.
     * @param permiteSelector
     */
    public void setPermiteSelector(boolean permiteSelector) {
        this.permiteSelector = permiteSelector;
    }

    /**
     * Setea el modo de la tabla.
     * @param analisis
     */
    public void setModoAnalisis(boolean analisis) {
        setModoAnalisis(analisis, ordenar, operar);
    }
    
    /**
     * Setea el modo de la tabla.
     * @param analisis
     */
    public void setModoAnalisis(boolean analisis, boolean ordenar, boolean operar) {
        if(this.analisis && analisis && !isRedefinir()) {
        	reprocesar();        	
        } else {
            if(analisis) {
            	this.analisis = analisis;
            	this.ordenar = ordenar;
            	this.operar = operar;
            	procesarAnalisis();
            } else if(this.analisis && !analisis){
            	this.analisis = analisis;
            	procesarDefault();
            }
        }
    }

    /**
     * Devuelve <b>true </b> si la tabla se encuentra en modo de análisis.
     * @return
     */
    public boolean isModoAnalisis() {
        return analisis;
    }

    /**
     * Devuelve el color de las celdas de las columnas grupo.
     * @return
     */
    public Color getColorGrupo() {
        return colorGrupo;
    }

    /**
     * Setea el color de las celdas de las columnas grupo.
     * @param colorGrupo
     */
    public void setColorGrupo(Color colorGrupo) {
        this.colorGrupo = colorGrupo;
    }

    /**
     * Devuelve el color de las celdas de los cortes de control.
     * @return
     */
    public Color getColorCorte() {
        return colorCorte;
    }

    /**
     * Setea el color de las celdas de los cortes de control.
     * @param colorCorte
     */
    public void setColorCorte(Color colorCorte) {
        this.colorCorte = colorCorte;
    }

    /**
     * Devuelve el color de las celdas de los cortes de control de mayor
     * prioridad.
     * @return
     */
    public Color getColorCortePrincipal() {
        return colorCortePrincipal;
    }

    /**
     * Setea el color de las celdas de los cortes de control de mayor prioridad.
     * @param colorCortePrincipal
     */
    public void setColorCortePrincipal(Color colorCortePrincipal) {
        this.colorCortePrincipal = colorCortePrincipal;
    }

    /**
     * Devuelve el color de fondo de las celdas de la última fila de totales.
     * @return
     */
    public Color getColorTotal() {
        return colorTotal;
    }

    /**
     * Setea el color de fondo de las celdas de la última fila de totales.
     * @param colorTotal
     */
    public void setColorTotal(Color colorTotal) {
        this.colorTotal = colorTotal;
    }

    /**
     * Devuelve <b>true </b> si se calcula la cantidad de elementos en un grupo.
     * @return
     */
    public boolean getSumarizarGrupos() {
        return sumarizarGrupos;
    }

    /**
     * Setea si se calcula la cantidad de elementos en un grupo.
     * @param sumarizarGrupos
     */
    public void setSumarizarGrupos(boolean sumarizarGrupos) {
        this.sumarizarGrupos = sumarizarGrupos;
    }

    /**
     * Reprocesa los datos de la tabla en modo análisis.
     */
    public void reprocesar() {
        analisis = true;
        cortesControl.clear();
        desagruparCeldas();
        //ordenamiento de los datos
        ordenar();
        //armado de los grupos
        if(coleccion.getGrupo().size() != 0 && getRowCount() > 0)
            generarCortesControl();
        //realizar operaciones
        addRow();
		if(coleccion.getDato().size() != 0 && getRowCount() > 0)
            operar();
    }

    /**
     * Procesa y genera la tabla de análisis.
     */ 
    private void procesarAnalisis() {
        boolean aceptar = true;
        if(permiteSelector) {
            if(coleccion.isEmpty()) {
                for(int i = 0; i < tiposColumnas.length; i++) {
                    if(!tiposColumnas[i].isOculta()) {
                        String campo = tiposColumnas[i].getNombre();
                        int posicion = tiposColumnas[i].getIndice();
                        //boolean isIntColumn = tiposColumnas[i] instanceof IntColumn;
                        //TipoDatoAnalisis tipoDato = new TipoDatoAnalisis(campo, posicion, isIntColumn);
                        TipoDatoAnalisis tipoDato = new TipoDatoAnalisis(campo, posicion);
                        coleccion.add(tipoDato);
                    }
                }
            }
            SelectorAnalisis selector = new SelectorAnalisis(null, coleccion);
            selector.setVisible(true);
            aceptar = selector.isAceptar();
        }
        if(aceptar) {
        	if(!isRedefinir()) {
        		rowSelectionAllowed = getRowSelectionAllowed();
                colSelectionAllowed = getColumnSelectionAllowed();
                cellSelectionEnabled = getCellSelectionEnabled();
                sortingAllowed = allowSorting();
                reorderingAllowed = getReorderingAllowed();
                setRowSelectionAllowed(false);
                setColumnSelectionAllowed(false);
                setCellSelectionEnabled(true);
                setAutoCreateColumnsFromModel(false);
        		setAllowSorting(false);
        		setReorderingAllowed(false);
        	} else {
        		procesarDefault();
        		this.analisis = true;
        	}
            //back ups
            copiar();
            //limpiar atributos de las celdas
            limpiar();
            //redistribución de columnas y datos
            distribuir();
            //ordenamiento de los datos
            if(ordenar) {
            	ordenar();            	
            }
            //armado de los grupos
            if(coleccion.getGrupo().size() != 0 && getRowCount() > 0)
                generarCortesControl();
            if(operar) {
            	if(getRowCount() > 0) {
            		calcularTotales();
            		//realizar operaciones
            		operar();
            	}            	
            }
            //análisis reducido
            if(analisisReducidoGral)
                procesarAnalisisReducido();
        } else
            analisis = false;
    }

    /**
     * Genera el back up de los tipos de columnas, los identificadores de las
     * columnas y del data vector.
     */
    private void copiar() {
        //tipos columnas
        FWColumn[] tiposColumnasOriginal = getTiposColumnas();
        tiposColumnasBackUp = new FWColumn[tiposColumnasOriginal.length];
        System.arraycopy(tiposColumnasOriginal, 0, tiposColumnasBackUp, 0, tiposColumnasOriginal.length);
        //anchos columnas, headers, renderers y editors
        int cantColumnas = getColumnModel().getColumnCount();
        anchosBackUp = new int[cantColumnas];
        headersBackUp = new Object[cantColumnas];
        renderersBackUp = new TableCellRenderer[cantColumnas];
        editorsBackUp = new TableCellEditor[cantColumnas];
        for(int i = 0; i < cantColumnas; i++) {
            TableColumn tableColumn = getColumnModel().getColumn(i);
            anchosBackUp[i] = tableColumn.getPreferredWidth();
            headersBackUp[i] = tableColumn.getHeaderValue();
            renderersBackUp[i] = tableColumn.getCellRenderer();
            editorsBackUp[i] = tableColumn.getCellEditor();
        }
        //celdas lockeadas
        lockCellsBackUp = (Hashtable)lockCells.clone();
        //colores celdas y filas.
        backgroundColorCellsBackUp = (Hashtable)backgroundColorCells.clone();
        backgroundColorRowsBackUp = (Hashtable)backgroundColorRows.clone();
        //tooltips celdas
        //tooltipCellsBackUp = (Hashtable)tooltipCells.clone();
        //datos
        datosBackUp = getDatosBackUp();
    }

    /**
     * Crea un arreglo bidimensional con los datos contenidos en la tabla.
     * @return datos
     */
    private Object[][] getDatosBackUp() {
        int filas = getRowCount();
        int cols = getColumnCount();
        Object[][] datos = new Object[filas][cols];
        Vector dataVector = ((DefaultTableModel)getModel()).getDataVector();
        for(int i = 0; i < dataVector.size(); i++) {
            for(int j = 0; j < ((Vector)dataVector.get(i)).size(); j++)
                datos[i][j] = ((Vector)dataVector.get(i)).get(j);
        }
        return datos;
    }

    /**
     * Elimina los atributos de las celdas (colores, tooltips y locks).
     */
    private void limpiar() {
        backgroundColorCells.clear();
        backgroundColorRows.clear();
        lockCells.clear();
        tooltipCells.clear();
    }

    /**
     * Reordena las columnas y los datos de la tabla.
     */
	private void distribuir() {
        Vector secciones = coleccion.getSecciones();
        Vector dataVector = ((DefaultTableModel)getModel()).getDataVector();
        Object contenido[][] = new Object[getRowCount()][getColumnCount()];
        FWColumn[] tiposColumnasTemp = new FWColumn[tiposColumnas.length];
        TableCellRenderer[] renderersTemp = new TableCellRenderer[secciones.size()];
        TableCellEditor[] editorsTemp = new TableCellEditor[secciones.size()];
        int[] anchosColumnas = new int[secciones.size()];
        List columnasOcultas = new ArrayList();
        for(int i = 0; i < tiposColumnas.length; i++) {
            FWColumn clColumn = tiposColumnas[i];
            if(permiteSelector) {
            	clColumn.setVisible(true);            	
            }
            if(clColumn.isOculta())
                columnasOcultas.add(clColumn);
        }
        int ultimoIndice = -1;
        for(Iterator i = secciones.iterator(); i.hasNext();) {
            TipoDatoAnalisis tipoDato = (TipoDatoAnalisis)i.next();
            int colOriginal = tipoDato.getPosicionOriginal();
            int colAnalisis = tipoDato.getPosicionAnalisis();
            ultimoIndice = Math.max(ultimoIndice, colAnalisis);
            //Headers
            getColumnModel().getColumn(colAnalisis).setHeaderValue(tipoDato.getCampo());
            getTableHeader().resizeAndRepaint();
            //Tipos
            FWColumn clColumn = getTipoColumna(colOriginal);
            clColumn.setIndice(colAnalisis);
            tiposColumnasTemp[colAnalisis] = clColumn;
            //Ancho
            /*if(!permiteSelector && tiposColumnas[colOriginal].isOculta()) {
            	 anchosColumnas[colAnalisis] = 0;
            } else {
            	anchosColumnas[colAnalisis] = getColumnModel().getColumn(colOriginal).getPreferredWidth();            	
            }*/
            anchosColumnas[colAnalisis] = tiposColumnas[colOriginal].getAncho();
            //Renderer
            renderersTemp[colAnalisis] = getColumnModel().getColumn(colOriginal).getCellRenderer();
            //Editor
            editorsTemp[colAnalisis] = getColumnModel().getColumn(colOriginal).getCellEditor();
            //Datos de la fila
            for(int j = 0; j < dataVector.size(); j++) {
                Object obj = ((Vector)dataVector.get(j)).get(colOriginal);
                contenido[j][colAnalisis] = obj;
            }
        }
        //ultimoIndice++;
        for(Iterator i = columnasOcultas.iterator(); i.hasNext();) {
            FWColumn clColumn = (FWColumn)i.next();
            clColumn.setIndice(ultimoIndice);
            tiposColumnasTemp[ultimoIndice] = clColumn;
            ultimoIndice++;
        }
        setTiposColumnas(tiposColumnasTemp);
        for(int i = 0; i < anchosColumnas.length; i++) {
        	getColumnModel().getColumn(i).setWidth(anchosColumnas[i]);
        	getColumnModel().getColumn(i).setPreferredWidth(anchosColumnas[i]);   	
        }
        getTableHeader().resizeAndRepaint();
        for(int i = 0; i < renderersTemp.length; i++)
            getColumnModel().getColumn(i).setCellRenderer(renderersTemp[i]);
        for(int i = 0; i < editorsTemp.length; i++)
            getColumnModel().getColumn(i).setCellEditor(editorsTemp[i]);
        //Reubicación de los datos de toda la tabla
        llenarTabla(contenido);
    }

    /**
     * Llena la tabla con los datos contenidos en el arreglo bidimensional
     * recibido como parámetro.
     * @param datos
     */
    private void llenarTabla(Object[][] datos) {
        setNumRows(datos.length);
        for(int i = 0; i < datos.length; i++) {
            for(int j = 0; j < datos[i].length; j++)
                setValueAt(datos[i][j], i, j);
        }
    }

    /**
     * Ordena los datos de la tabla de acuerdo a las columnas grupo.
     */
    private void ordenar() {
    	Vector grupo = coleccion.getGrupo();
    	if(!coleccion.getInfo().isEmpty()) {
    		sortAllRowsBy(grupo.size(), true);
    	}
        for(int i = grupo.size() - 1; i >= 0; i--) {
        	sortAllRowsBy(i, true);        	
        }
    }

    /**
     * Genera e inserta los cortes de control.
     */
	private void generarCortesControl() {
        int row = 0;
        int colCorte = 0;
        int cantGrupo = 0;
        int cantFilasAnteriores = 0;
        int[] filasDesde = inicializarArray();
        if(getRowCount() > 0) {
        	Vector valoresAnteriores = getValoresFila(row);
        	while(row < getRowCount()) {
        		colCorte = getCorteControl(row, valoresAnteriores);
        		if(colCorte != -1) {
        			cantGrupo = row - cantFilasAnteriores;
        			cantFilasAnteriores = row;
        			Vector valoresAnterioresTemp = (Vector)valoresAnteriores.clone();
        			valoresAnteriores = getValoresFila(row);
        			for(int j = coleccion.getGrupo().size() - 1; j >= colCorte; j--) {
        				if(j == coleccion.getGrupo().size() - 1)
        					cortesControl.add(new CorteControl(row, j, filasDesde[j], cantGrupo));
        				else
        					cortesControl.add(new CorteControl(row, j, filasDesde[j]));
        				agruparCeldas(filasDesde[j], j, row - filasDesde[j], 1);
        				if(operar) {
        					insertRow(row, getVectorRelleno(valoresAnterioresTemp, j));
        					row++;
        					cantFilasAnteriores++;                    	
        				}
        			}
        			for(int j = coleccion.getGrupo().size() - 1; j >= colCorte; j--)
        				filasDesde[j] = row;
        		}
        		row++;
        	}
        	//TODO: incluir el ultimo corte dentro de getCorteControl
        	valoresAnteriores = getValoresFila(getRowCount() - 1);
        	for(int i = coleccion.getGrupo().size() - 1; i >= 0; i--) {
        		if(operar) {
        			addRow(getVectorRelleno(valoresAnteriores, i));        		
        		}
        		if(i == coleccion.getGrupo().size() - 1)
        			cortesControl.add(new CorteControl(getRowCount() - 1, i, filasDesde[i], row - cantFilasAnteriores));
        		else
        			cortesControl.add(new CorteControl(getRowCount() - 1, i, filasDesde[i]));
        		if(operar) {
        			agruparCeldas(filasDesde[i], i, getRowCount() - 1 - filasDesde[i], 1);            	
        		} else {
        			agruparCeldas(filasDesde[i], i, getRowCount() - filasDesde[i], 1);
        		}
        	}
        }
    }
    
    public void agruparColumna(int col) {
    	if(getRowCount() > 0) {
    		int row = 1;
        	int inicioGrupo = 0;
        	Vector valoresAnteriores = (Vector)getDataVector().get(0);
        	while(row < getRowCount()) {
        		Vector valoresActuales = (Vector)getDataVector().get(row);
        		Object valorActual = valoresActuales.get(col);
        		Object valorAnterior = valoresAnteriores.get(col);
        		if(valorAnterior != null && valorActual != null) {
        			if(!valorActual.equals(valorAnterior)) {
        				if(inicioGrupo != row - 1) {
        					agruparCeldas(inicioGrupo, col, row - inicioGrupo, 1);
        				}
        				inicioGrupo = row;
        			}
        		}
        		valoresAnteriores = (Vector)getDataVector().get(row);
        		row++;
        	}
        	if(inicioGrupo != row - 1) {
				agruparCeldas(inicioGrupo, col, row - inicioGrupo, 1);
			}
    	}  	
    }

    
    @Override
    /*
     * override de setNumRows en Tabla de analisis para eliminar los grupos de celdas. 
     * Pasar a CLJTable cuando veamos que no provoca efectos laterales en las aplicaciones.
     * (Una aplicacion que falle por este cambio estaría mal programada) 
     */
    public void setNumRows(int cantidad) {
    	if (cantidad == 0){
    		this.desagruparCeldas();
    	}
    	super.setNumRows(cantidad);
    }
    
	

	/**
	 * Agrupa las celdas de una columna que tienen el mismo valor siempre y cuando las celdas a la izquierda
	 * también tenga el mismo valor. 
	 * @param col
	 */
	public void agruparColumnaExtendido(int col) {
		if(getRowCount() > 0) {
			int row = 1;
			int inicioGrupo = 0;
			Vector valoresAnteriores = (Vector)getDataVector().get(0);
			while(row < getRowCount()) {
				Vector valoresActuales = (Vector)getDataVector().get(row);
				
				// Si hay que cortar
				if (finGrupo(valoresAnteriores, valoresActuales, col)) {
					if(inicioGrupo != row - 1) {
						agruparCeldas(inicioGrupo, col, row - inicioGrupo, 1);
					}
					inicioGrupo = row;
				}
				valoresAnteriores = (Vector)getDataVector().get(row);
				row++;
			}
			if(inicioGrupo != row - 1) {
				agruparCeldas(inicioGrupo, col, row - inicioGrupo, 1);
			}
		}
	}

	private boolean finGrupo(Vector<?> valoresAnteriores, Vector<?> valoresActuales, int col) {
		for (int i = col ; i >= 0 ; i--) {
			Object valorActual = valoresActuales.get(i);
			Object valorAnterior = valoresAnteriores.get(i);
			if (valorAnterior == null && valorActual== null) {
				continue ;
			}
			if (valorAnterior == null || valorActual == null) {
				return true ;
			}
			if (!valorActual.equals(valorAnterior)) {
				return true ;
			}
		}
		return false;
	}

	/**
     * Inicaliza un vector de enteros con 0, de acuerdo a la cantidad de
     * columnas grupo.
     * @return array;
     */
    private int[] inicializarArray() {
        int[] array = new int[coleccion.getGrupo().size()];
        for(int i = 0; i < array.length; i++)
            array[i] = 0;
        return array;
    }

    /**
     * Devuelve un vector con los datos de la fila recibida como parámetro.
     * @param fila
     * @return valores
     */
	private Vector getValoresFila(int fila) {
        Vector valores = new Vector();
        for(int i = 0; i < coleccion.getGrupo().size(); i++) {
    		valores.add(getValueAt(fila, i));        		
        }
        return valores;
    }

    /**
     * Devuelve el índice de la columna en la que se produce un corte de control
     * para la fila recibida como parámetro. Devuelve -1 en caso de que no se
     * porduzca ningún corte de control.
     * @param fila
     * @param valoresAnteriores
     * @return
     */
    private int getCorteControl(int fila, Vector valoresAnteriores) {
        Vector v = getValoresFila(fila);
        int corteControl = coleccion.getGrupo().size();
        boolean hayCorte = false;
        for(int i = valoresAnteriores.size() - 1; i >= 0; i--) {
            if(valoresAnteriores.get(i) != null && !valoresAnteriores.get(i).equals(v.get(i))) {
                corteControl = Math.min(i, corteControl);
                hayCorte = true;
            }
        }
        if(hayCorte)
            return corteControl;
        return -1;
    }

    /**
     * Rellena un vector con los valores de la fila anterior en las columnas
     * menores a la columna de corte.
     * @param valoresAnteriores
     * @param colHasta
     * @return
     */
	private Vector getVectorRelleno(Vector valoresAnteriores, int colHasta) {
        Vector vectorRelleno = new Vector(coleccion.getSize());
        for(int i = 0; i < colHasta; i++)
            vectorRelleno.add(valoresAnteriores.get(i));
        return vectorRelleno;
    }

    /**
     * Calcula los totales de los grupos.
     */
    private void calcularTotales() {
        for(int i = coleccion.getGrupo().size() - 2; i >= 0; i--) {
            Vector cortesCol = getCortesCol(cortesControl, i);
            Vector cortesColAnt = getCortesCol(cortesControl, i + 1);
            for(Iterator j = cortesCol.iterator(); j.hasNext();) {
                int total = 0;
                CorteControl corteControl = (CorteControl)j.next();
                for(Iterator k = cortesColAnt.iterator(); k.hasNext();) {
                    CorteControl corteControlTemp = (CorteControl)k.next();
                    if(corteControlTemp.getFilaCorte() < corteControl.getFilaCorte() && corteControlTemp.getFilaDesde() >= corteControl.getFilaDesde())
                        total += corteControlTemp.getTotal();
                }
                corteControl.setTotal(total);
            }
        }
        if(sumarizarGrupos) {
            for(Iterator i = cortesControl.iterator(); i.hasNext();) {
                CorteControl corteControl = (CorteControl)i.next();
                setValueAt(Integer.toString(corteControl.getTotal()), corteControl.getFilaCorte(), corteControl.getColCorte());
            }
        }
        addRow();
    }

    /**
     * Realiza las operaciones de las columnas grupo y dato.
     */
    public void operar() {
        Vector dato = coleccion.getDato();
        for(Iterator i = dato.iterator(); i.hasNext();) {
            TipoDatoAnalisis tipoDato = (TipoDatoAnalisis)i.next();
            boolean valor = (tipoDato.getAccion().equals(OPERACION_VAL));
            int colAnalisis = tipoDato.getPosicionAnalisis();
            for(int j = coleccion.getGrupo().size() - 1; j >= 0; j--) {
                TipoDatoAnalisis tipoDatoTemp = (TipoDatoAnalisis)coleccion.getGrupo().get(j);
                String operacion = (String)tipoDatoTemp.getAccion();
                Vector cortesCol = getCortesCol(cortesControl, j);
                for(Iterator k = cortesCol.iterator(); k.hasNext();) {
                    CorteControl corteControl = (CorteControl)k.next();
                    int filaDesde = corteControl.getFilaDesde();
                    int filaCorte = corteControl.getFilaCorte();
                    if(operacion.compareTo(OPERACION_SUM) == 0) {
                        if(!(tipoDato.isPorcentaje() && corteControl.getColCorte() != coleccion.getGrupo().size() - 1))
                            setValueAt(getSum(colAnalisis, filaDesde, filaCorte, valor), filaCorte, colAnalisis);
                    } else if(operacion.compareTo(OPERACION_CONT) == 0)
                        setValueAt(getCont(filaDesde, filaCorte, valor), filaCorte, colAnalisis);
                    else if(operacion.compareTo(OPERACION_PROM) == 0)
                        setValueAt(getProm(colAnalisis, filaDesde, filaCorte), filaCorte, colAnalisis);
                    else if(operacion.compareTo(OPERACION_MIN) == 0)
                        setValueAt(getMin(colAnalisis, filaDesde, filaCorte), filaCorte, colAnalisis);
                    else if(operacion.compareTo(OPERACION_MAX) == 0)
                        setValueAt(getMax(colAnalisis, filaDesde, filaCorte), filaCorte, colAnalisis);
                }
            }
        }
        int total = 0;
        for(Iterator i = getCortesCol(cortesControl, 0).iterator(); i.hasNext();) {
            CorteControl corteControl = (CorteControl)i.next();
            total += corteControl.getTotal();
        }
        if(sumarizarGrupos)
            setValueAt(total, getRowCount() - 1, 0);
        Object operacion = null;
        for(Iterator i = coleccion.getGrupo().iterator(); i.hasNext();) {
            TipoDatoAnalisis tipoDato = (TipoDatoAnalisis)i.next();
            if(tipoDato.getPosicionAnalisis() == 0) {
                operacion = tipoDato.getAccion();
                break;
            }
        }
        if(operacion != null) {
            for(Iterator i = coleccion.getDato().iterator(); i.hasNext();) {
                total = 0;
                TipoDatoAnalisis tipoDato = (TipoDatoAnalisis)i.next();
                int col = tipoDato.getPosicionAnalisis();
                if(!tipoDato.isPorcentaje()) {
                    if(operacion.equals(OPERACION_SUM))
                        setValueAt(getSum(col, 0, getRowCount() - 1, tipoDato.getAccion().equals(OPERACION_VAL)), getRowCount() - 1, col);
                    else if(operacion.equals(OPERACION_CONT))
                        setValueAt(getCont(0, getRowCount() - 1, tipoDato.getAccion().equals(OPERACION_VAL)), getRowCount() - 1, col);
                    else if(operacion.equals(OPERACION_PROM))
                        setValueAt(getProm(col, 0, getRowCount() - 1), getRowCount() - 1, col);
                    else if(operacion.equals(OPERACION_MIN))
                        setValueAt(getMin(col, 0, getRowCount() - 1), getRowCount() - 1, col);
                    else if(operacion.equals(OPERACION_MAX))
                        setValueAt(getMin(col, 0, getRowCount() - 1), getRowCount() - 1, col);
                }
            }
        }
        Vector info = coleccion.getInfo();
        for(Iterator i = info.iterator(); i.hasNext();) {
        	TipoDatoAnalisis tipoDatoAnalisis = (TipoDatoAnalisis)i.next();    	
        	boolean ver = tipoDatoAnalisis.getAccion() != null ? ((Boolean)tipoDatoAnalisis.getAccion()).booleanValue() : false;
            int colAnalisis = tipoDatoAnalisis.getPosicionAnalisis();
            if(!ver)
                setColumnWidth(colAnalisis, 0);
        }
        getTableHeader().resizeAndRepaint();
    }

    /**
     * Realiza la operación SUM de valores numéricos y fechas para el grupo
     * delimitado para la columna <code>col</code> y para el grupo delimitado
     * por las filas <code>filaDesde</code> y <code>filaCorte</code>.
     * @param col
     * @param filaDesde
     * @param filaCorte
     * @return
     */
    private Object getSum(int col, int filaDesde, int filaCorte, boolean valor) {
        if(tiposColumnas[col] instanceof IntColumn) {
            int sum = 0;
            for(int i = filaDesde; i < filaCorte; i++) {
                if(!isFilaCorte(i) && getValueAt(i, col) != null) {
                    if(getValueAt(i, col) instanceof Integer)
                        sum += ((Integer)getValueAt(i, col)).intValue();
                }
            }
            if(valor)
                return new Integer(sum);
            else {
                int total = ((Integer)getSum(col, 0, getRowCount() - 2, true)).intValue();
                return new Float((float)sum * 100 / (float)total);
            }
        } else if(tiposColumnas[col] instanceof FloatColumn) {
        	float sum = 0;
        	for(int i = filaDesde; i < filaCorte; i++) {
        		if(!isFilaCorte(i) && getValueAt(i, col) != null) {
        			if(getValueAt(i, col) instanceof Float)
        				sum += ((Float)getValueAt(i, col)).floatValue();
        		}
        	}
        	if(valor)
        		return new Float(sum);
        	else {
        		float total = ((Float)getSum(col, 0, getRowCount() - 2, true)).floatValue();
                return new Float((float)sum * 100 / (float)total);
        	}
        } else if(tiposColumnas[col] instanceof OperableTimeColumn) {
            long sum = 0;
            for(int i = filaDesde; i < filaCorte; i++) {
                if(!isFilaCorte(i) && getValueAt(i, col) != null) {
                    if(getValueAt(i, col) instanceof java.util.Date)
                        sum += ((java.util.Date)getValueAt(i, col)).getTime();
                }
            }
            if(valor)
                return new Date(sum);
            else {
                long total = ((Date)getSum(col, 0, getRowCount() - 2, true)).getTime();
                return new Date((long)(sum * 100 / total));
            }
        } else
            return MENSAJE_NO_CALCULABLE;
    }

    /**
     * Realiza la operación CONT para el grupo delimitado para la columna
     * <code>col</code> y para el grupo delimitado por las filas
     * <code>filaDesde</code> y <code>filaCorte</code>.
     * @param filaDesde
     * @param filaCorte
     * @return
     */
    private Object getCont(int filaDesde, int filaCorte, boolean valor) {
        int cont = 0;
        for(int i = filaDesde; i < filaCorte; i++) {
            if(!isFilaCorte(i))
                cont++;
        }
        if(valor)
            return new Integer(cont);
        else {
            int total = ((Integer)getCont(0, getRowCount() - 2, true)).intValue();
            return new Float((float)cont * 100 / (float)total);
        }
    }

    /**
     * Realiza la operación PROM para el grupo delimitado para la columna
     * <code>col</code> y para el grupo delimitado por las filas
     * <code>filaDesde</code> y <code>filaCorte</code>.
     * @param col
     * @param filaDesde
     * @param filaCorte
     * @return
     */
    private Object getProm(int col, int filaDesde, int filaCorte) {
        if(tiposColumnas[col] instanceof IntColumn) {
            int sum = ((Integer)getSum(col, filaDesde, filaCorte, true)).intValue();
            int cont = ((Integer)getCont(filaDesde, filaCorte, true)).intValue();
            return new Float(sum / (float)cont);
        } else
            return MENSAJE_NO_CALCULABLE;
    }

    /**
     * Realiza la operación MAX para el grupo delimitado para la columna
     * <code>col</code> y para el grupo delimitado por las filas
     * <code>filaDesde</code> y <code>filaCorte</code>.
     * @param col
     * @param filaDesde
     * @param filaCorte
     * @return
     */
    private Object getMax(int col, int filaDesde, int filaCorte) {
        if(tiposColumnas[col] instanceof IntColumn) {
            int max = 0;
            for(int i = filaDesde; i < filaCorte; i++) {
                if(!isFilaCorte(i) && getValueAt(i, col) != null) {
                    int val = ((Integer)getValueAt(i, col)).intValue();
                    if(i == filaDesde)
                        max = val;
                    else
                        max = Math.max(max, val);
                }
            }
            return new Integer(max);
        } else if(tiposColumnas[col] instanceof DateColumn || tiposColumnas[col] instanceof TimeColumn) {
            long max = 0;
            for(int i = filaDesde; i < filaCorte; i++) {
                if(!isFilaCorte(i) && getValueAt(i, col) != null) {
                    long val = ((Date)getValueAt(i, col)).getTime();
                    if(i == filaDesde)
                        max = val;
                    else
                        max = Math.max(max, val);
                }
            }
            return new Date(max);
        } else
            return MENSAJE_NO_CALCULABLE;
    }

    /**
     * Realiza la operación MIN para el grupo delimitado para la columna
     * <code>col</code> y para el grupo delimitado por las filas
     * <code>filaDesde</code> y <code>filaCorte</code>.
     * @param col
     * @param filaDesde
     * @param filaCorte
     * @return
     */
    private Object getMin(int col, int filaDesde, int filaCorte) {
        if(tiposColumnas[col] instanceof IntColumn) {
            int min = 0;
            for(int i = filaDesde; i < filaCorte; i++) {
                if(!isFilaCorte(i) && getValueAt(i, col) != null) {
                    int val = ((Integer)getValueAt(i, col)).intValue();
                    if(i == filaDesde)
                        min = val;
                    else
                        min = Math.min(min, val);
                }
            }
            return new Integer(min);
        } else if(tiposColumnas[col] instanceof DateColumn || tiposColumnas[col] instanceof TimeColumn) {
            long min = 0;
            for(int i = filaDesde; i < filaCorte; i++) {
                if(!isFilaCorte(i) && getValueAt(i, col) != null) {
                    long val = ((Date)getValueAt(i, col)).getTime();
                    if(i == filaDesde)
                        min = val;
                    else
                        min = Math.min(min, val);
                }
            }
            return new Date(min);
        } else
            return MENSAJE_NO_CALCULABLE;
    }

    /**
     * Procesa y genera la tabla de análisis reducido**/
    private void procesarAnalisisReducido() {
        desagruparCeldas();
        llenarFilasCorte();
        ocultarColumnas();
        ocultarFilas();
        int totales[] = getTotalesGrupos();
//      Object totalesGrupos = null;
//      if(coleccion.getDato().size() > 0)
//          totalesGrupos = getTotalesGrupos();
        cortesControl.clear();
        generarCortesControl();
        setTotalesGrupos(totales);
        if(coleccion.getDato().size() > 0)
            setTotalesGrupos(totales);
        calcularTotales();
        operar();
    }

    /**
     * Rellena las filas corte con los datos de la fila anterior para poder
     * procesar el modo de análisis reducido.
     */
    public void llenarFilasCorte() {
        int ultimaColGrupo = coleccion.getGrupo().size() - 1;
        for(Iterator i = getCortesCol(cortesControl, ultimaColGrupo).iterator(); i.hasNext();) {
            CorteControl corteControl = (CorteControl)i.next();
            int filaCorte = corteControl.getFilaCorte();
            setValueAt(getValueAt(filaCorte - 1, ultimaColGrupo), filaCorte, ultimaColGrupo);
        }
    }

    /**
     * Oculta las columnas info que no deben verse en el modo de análisis
     * reducido.
     */
    private void ocultarColumnas() {
        for(Iterator i = coleccion.getInfo().iterator(); i.hasNext();) {
            TipoDatoAnalisis tipoDato = (TipoDatoAnalisis)i.next();
            tipoDato.setAccion(Boolean.valueOf(false));
        }
    }

    /**
     * Oculta las filas que no deben verse en el modo de análisis reducido.
     */
    private void ocultarFilas() {
        int filasBorradas = 0;
        int cantFilas = getRowCount();
        for(int i = 0; i < cantFilas; i++) {
            if(!isFilaVisible(i, filasBorradas)) {
                removeRow(i - filasBorradas);
                filasBorradas++;
            }
        }
    }

    /**
     * Devuelve <b>true </b> si la fila recibida como parámetro permanecerá
     * visible en el modo de análisis reducido.
     * @param fila
     * @return
     */
    private boolean isFilaVisible(int fila, int filasBorradas) {
        Vector cortesCol = getCortesCol(cortesControl, coleccion.getGrupo().size() - 1);
        int filaReal = fila - filasBorradas;
        for(Iterator i = cortesCol.iterator(); i.hasNext();) {
            CorteControl corteControl = (CorteControl)i.next();
            int filaCorteReal = corteControl.getFilaCorte() - filasBorradas;
            if(filaCorteReal == filaReal)
                return true;
        }
        return false;
    }

    /**
     * Devuelve un arreglo de enteros con los totales de los grupos.
     * @return
     */
    private int[] getTotalesGrupos() {
        Vector cortesCol = getCortesCol(cortesControl, coleccion.getGrupo().size() - 1);
        int totales[] = new int[cortesCol.size()];
        for(int i = 0; i < cortesCol.size(); i++) {
            CorteControl corteControl = (CorteControl)cortesCol.get(i);
            totales[i] = corteControl.getTotal();
        }
        return totales;
    }

    /**
     * Setea los totales contenidos en <code>totales</code> a los cortes de
     * control correspondientes.
     * @param totales
     */
    private void setTotalesGrupos(int[] totales) {
        Vector cortesCol = getCortesCol(cortesControl, coleccion.getGrupo().size() - 1);
        for(int i = 0; i < cortesCol.size(); i++) {
            CorteControl corteControl = (CorteControl)cortesCol.get(i);
            corteControl.setTotal(totales[i]);
        }
    }

    /**
     * Deja a la tabla en el mismo estado que se encontraba antes de ponerla en
     * modo de análisis.
     */
	private void procesarDefault() {
        desagruparCeldas();
        analisis = false;
        clearSelection();
        setCellSelectionEnabled(cellSelectionEnabled);
        setRowSelectionAllowed(rowSelectionAllowed);
        setColumnSelectionAllowed(colSelectionAllowed);
        setAllowSorting(sortingAllowed);
        setReorderingAllowed(reorderingAllowed);
        int filasBorradas = 0;
        if(operar) {
        	for(Iterator i = cortesControl.iterator(); i.hasNext();) {
        		CorteControl corteControl = (CorteControl)i.next();
        		removeRow(corteControl.getFilaCorte() - filasBorradas);
        		filasBorradas++;
        	}
        	for(int i = 0; i < coleccion.getGrupo().size() + 1; i++) {
        		if(getRowCount() != 0) {
        			removeRow(getRowCount() - 1);        		
        		}
        	}
        }
        cortesControl.clear();
        setTiposColumnas(tiposColumnasBackUp);
        if(anchosBackUp != null) {
        	for(int i = 0; i < anchosBackUp.length; i++) {
        		getColumnModel().getColumn(i).setPreferredWidth(anchosBackUp[i]);        		
        	}
        }
        if(headersBackUp != null) {
        	for(int i = 0; i < headersBackUp.length; i++) {
        		getColumnModel().getColumn(i).setHeaderValue(headersBackUp[i]);        		
        	}
        }
        if(renderersBackUp != null) {
        	for(int i = 0; i < renderersBackUp.length; i++) {
        		getColumnModel().getColumn(i).setCellRenderer(renderersBackUp[i]);        		
        	}
        }
        if(editorsBackUp != null) {
        	for(int i = 0; i < editorsBackUp.length; i++) {
        		getColumnModel().getColumn(i).setCellEditor(editorsBackUp[i]);        		
        	}
        }
        getTableHeader().resizeAndRepaint();
        lockCells = lockCellsBackUp;
        if(backgroundColorCellsBackUp != null) {
        	backgroundColorCells = (Hashtable)backgroundColorCellsBackUp.clone();        	
        }
        if(backgroundColorRowsBackUp != null) {
        	backgroundColorRows = (Hashtable)backgroundColorRowsBackUp.clone();        	
        }
        //tooltipCells = tooltipCellsBackUp;
        llenarTabla(datosBackUp);
    }

    /**
     * Devuelvo los cortes de control para la columna recibida como parámetro.
     * @param cortesControl
     * @param col
     * @return cortesColS
     */
	private Vector getCortesCol(Vector cortesControl, int col) {
        Vector cortesCol = new Vector();
        for(Iterator i = cortesControl.iterator(); i.hasNext();) {
            CorteControl corteControl = (CorteControl)i.next();
            if(corteControl.getColCorte() == col)
                cortesCol.add(corteControl);
        }
        return cortesCol;
    }

    /**
     * Devuelve <b>true </b> si la fila pasada como parámetro pertenece a un
     * corte de control.
     * @param fila
     * @return
     */
    public boolean isFilaCorte(int fila) {
        for(Iterator i = cortesControl.iterator(); i.hasNext();) {
            CorteControl corteControl = (CorteControl)i.next();
            if(corteControl.getFilaCorte() == fila)
                return true;
        }
        return false;
    }

    /**
     * Devuelve <b>true </b> si la fila pasada como parámetro pertenece a un
     * corte de control de la columna grupo de mayor prioridad.
     * @param fila
     * @return
     */
    public boolean isFilaCortePrincipal(int fila) {
        for(Iterator i = cortesControl.iterator(); i.hasNext();) {
            CorteControl corteControl = (CorteControl)i.next();
            if(corteControl.getFilaCorte() == fila && corteControl.getColCorte() == 0)
                return true;
        }
        return false;
    }

    /**
     * Devuelve el índice de la columna que genera el corte de control para la
     * fila recibida como parámetro.
     * @param fila
     * @return
     */
    public int getColCorte(int fila) {
        if(!isFilaCorte(fila))
            return -1;
        for(Iterator i = cortesControl.iterator(); i.hasNext();) {
            CorteControl corteControl = (CorteControl)i.next();
            if(corteControl.getFilaCorte() == fila)
                return corteControl.getColCorte();
        }
        return -1;
    }

    /**
     * Devuelve <b>true </b> si la columna pasada como parámetro es una columna
     * grupo.
     * @param col
     * @return
     */
    public boolean isColGrupo(int col) {
        int colFin = coleccion.getGrupo().size() - 1;
        if(col >= 0 && col <= colFin)
            return true;
        return false;
    }

    /**
     * Devuelve <b>true </b> si el campo pasado como parámetro es una columna
     * grupo.
     * @param campo
     * @return
     */
    public boolean isColGrupo(String campo) {
        TipoDatoAnalisis tipoDato = (TipoDatoAnalisis)coleccion.get(campo);
        if(tipoDato != null)
            return (tipoDato.getTipo() == TipoDatoAnalisis.TIPO_GRUPO);
        return false;
    }

    /**
     * Devuelve <b>true </b> si la columna pasada como parámetro es una columna
     * info.
     * @param col
     * @return
     */
    public boolean isColInfo(int col) {
        int colInicio = coleccion.getGrupo().size();
        int colFin = colInicio + coleccion.getInfo().size() - 1;
        if(col >= colInicio && col <= colFin)
            return true;
        return false;
    }

    /**
     * Devuelve <b>true </b> si el campo pasado como parámetro es una columna
     * info.
     * @param campo
     * @return
     */
    public boolean isColInfo(String campo) {
        TipoDatoAnalisis tipoDato = (TipoDatoAnalisis)coleccion.get(campo);
        if(tipoDato != null)
            return (tipoDato.getTipo() == TipoDatoAnalisis.TIPO_INFO);
        return false;
    }

    /**
     * Devuelve <b>true </b> si la columna pasada como parámetro es una columna
     * dato.
     * @param col
     * @return
     */
    public boolean isColDato(int col) {
        int colInicio = coleccion.getGrupo().size() + coleccion.getInfo().size();
        int colFin = colInicio + coleccion.getDato().size() - 1;
        if(col >= colInicio && col <= colFin)
            return true;
        return false;
    }

    /**
     * Devuelve <b>true </b> si el campo pasado como parámetro es una columna
     * dato.
     * @param campo
     * @return
     */
    public boolean isColDato(String campo) {
        TipoDatoAnalisis tipoDato = (TipoDatoAnalisis)coleccion.get(campo);
        if(tipoDato != null)
            return (tipoDato.getTipo() == TipoDatoAnalisis.TIPO_DATO);
        return false;
    }

    /**
     * Retorna todo los cortes de control que se producen en la columna grupo de
     * mayor prioridad.
     * @return cortesPrincipales
     */
	public Vector getCortesPrincipales() {
        Vector cortesPrincipales = new Vector();
        for(Iterator i = cortesControl.iterator(); i.hasNext();) {
            CorteControl corteControl = (CorteControl)i.next();
            if(corteControl.getColCorte() == 0)
                cortesPrincipales.add(corteControl);
        }
        return cortesPrincipales;
    }

    /**
     * Retorna todo los cortes de control que no se producen en la columna grupo
     * de mayor prioridad.
     * @return cortesSecundarios
     */
	public Vector getCortesSecundarios() {
        Vector cortesSecundarios = new Vector();
        for(Iterator i = cortesControl.iterator(); i.hasNext();) {
            CorteControl corteControl = (CorteControl)i.next();
            if(corteControl.getColCorte() != 0)
                cortesSecundarios.add(corteControl);
        }
        return cortesSecundarios;
    }

    /**
     * Devuelve el corte de control para el elmento ubicado en la posición
     * <code>fila</code>,<code>col</code>.
     * @param fila
     * @param col
     * @return
     */
    public CorteControl getCorteControl(int fila, int col) {
        Vector cortesCol = getCortesCol(cortesControl, col);
        for(Iterator i = cortesCol.iterator(); i.hasNext();) {
            CorteControl corteControl = (CorteControl)i.next();
            if(fila >= corteControl.getFilaDesde() && fila <= corteControl.getFilaCorte() && corteControl.getColCorte() == col)
                return corteControl;
        }
        return null;
    }

    /**
     * Devuelve <b>true </b> si la tabla se encuentra en modo de análisis.
     * @return analisi.
     */
    public boolean isAnalisis() {
        return analisis;
    }

    /**
     * Devuelve la ColeccionTipoDato.
     * @return coleccion
     */
    public ColeccionTipoDato getColeccionTipoDato() {
        return coleccion;
    }
    
    public void limpiarTiposDato() {
    	coleccion.clear();
    }

    public void setColumnaGrupo(int posOriginal, int posAnalisis, String operacion) {
        String campo = tiposColumnas[posOriginal].getNombre();
        TipoDatoAnalisis tipoDatoAnalisis = new TipoDatoAnalisis();
        tipoDatoAnalisis.setAccion(operacion);
        tipoDatoAnalisis.setCampo(campo);
        tipoDatoAnalisis.setPosicionAnalisis(posAnalisis);
        tipoDatoAnalisis.setPosicionOriginal(posOriginal);
        tipoDatoAnalisis.setTipo(TIPO_GRUPO);
        coleccion.add(tipoDatoAnalisis);
    }

    public void setColumnaGrupo(String campo, int posAnalisis, String operacion) {
        for(int i = 0; i < tiposColumnas.length; i++) {
            String campoTemp = tiposColumnas[i].getNombre();
            if(campo.compareTo(campoTemp) == 0)
                setColumnaGrupo(i, posAnalisis, operacion);
        }
    }

    public void setColumnaInfo(int posOriginal, int posAnalisis, boolean visible) {
        String campo = tiposColumnas[posOriginal].getNombre();
        TipoDatoAnalisis tipoDatoAnalisis = new TipoDatoAnalisis();
        tipoDatoAnalisis.setAccion(Boolean.valueOf(visible));
        tipoDatoAnalisis.setCampo(campo);
        tipoDatoAnalisis.setPosicionAnalisis(posAnalisis);
        tipoDatoAnalisis.setPosicionOriginal(posOriginal);
        tipoDatoAnalisis.setTipo(TIPO_INFO);
        coleccion.add(tipoDatoAnalisis);
    }

    public void setColumnaInfo(String campo, int posAnalisis, boolean visible) {
        for(int i = 0; i < tiposColumnas.length; i++) {
            String campoTemp = tiposColumnas[i].getNombre();
            if(campo.compareTo(campoTemp) == 0)
                setColumnaInfo(i, posAnalisis, visible);
        }
    }

    public void setColumnaDato(int posOriginal, int posAnalisis, String operacion, boolean porcentaje) {
        String campo = tiposColumnas[posOriginal].getNombre();
        TipoDatoAnalisis tipoDatoAnalisis = new TipoDatoAnalisis();
        tipoDatoAnalisis.setAccion(operacion);
        tipoDatoAnalisis.setCampo(campo);
        tipoDatoAnalisis.setPosicionAnalisis(posAnalisis);
        tipoDatoAnalisis.setPosicionOriginal(posOriginal);
        tipoDatoAnalisis.setTipo(TIPO_DATO);
        tipoDatoAnalisis.setPorcentaje(porcentaje);
        coleccion.add(tipoDatoAnalisis);
    }

    public void setColumnaDato(String campo, int posAnalisis, String operacion, boolean porcentaje) {
        for(int i = 0; i < tiposColumnas.length; i++) {
            String campoTemp = tiposColumnas[i].getNombre();
            if(campo.compareTo(campoTemp) == 0)
                setColumnaDato(i, posAnalisis, operacion, porcentaje);
        }
    }

    /**
     * Devuelve la suma del ancho de todas las columnas de la tabla.
     * @return
     */
    public int getAnchoTotal() {
        return getColumnModel().getTotalColumnWidth();
    }

    public class TipoDatoAnalisis {

        private String campo;
        private Object accion;
        private boolean porcentaje;
        private int posicionOriginal;
        private int posicionAnalisis;
        private int prioridad;
        private int tipo;
        public static final int TIPO_GRUPO = 1;
        public static final int TIPO_INFO = 2;
        public static final int TIPO_DATO = 3;

        public TipoDatoAnalisis() {
        }

//        public TipoDatoAnalisis(String campo, int posicion, boolean intColumn) {
//            this.campo = campo;
//            this.posicionOriginal = posicion;
//            this.tipo = TIPO_INFO;
//            //this.intColumn = intColumn;
//        }

        public TipoDatoAnalisis(String campo, int posicion) {
            this.campo = campo;
            this.posicionOriginal = posicion;
            this.tipo = TIPO_INFO;
        }

        public Object getAccion() {
            return accion;
        }

        public void setAccion(Object accion) {
            this.accion = accion;
        }

        public String getCampo() {
            return campo;
        }

        public void setCampo(String campo) {
            this.campo = campo;
        }

        public int getPosicionOriginal() {
            return posicionOriginal;
        }

        public void setPosicionOriginal(int posicion) {
            this.posicionOriginal = posicion;
        }

        public int getTipo() {
            return tipo;
        }

        public void setTipo(int tipo) {
            this.tipo = tipo;
        }

        public int getPosicionAnalisis() {
            return posicionAnalisis;
        }

        public void setPosicionAnalisis(int posicionAnalisi) {
            this.posicionAnalisis = posicionAnalisi;
        }

        public int getPrioridad() {
            return prioridad;
        }

        public void setPrioridad(int prioridad) {
            this.prioridad = prioridad;
        }

        public boolean isIntColumn() {
            return isIntColumn();
        }

        public void setPorcentaje(boolean porcentaje) {
            this.porcentaje = porcentaje;
        }

        public boolean isPorcentaje() {
            return porcentaje;
        }

//		public int compareTo(Object o) {
//			TipoDatoAnalisis tda = (TipoDatoAnalisis)o;
//			return getPosicionOriginal() - tda.getPosicionOriginal();
//		}
    }

    public class ColeccionTipoDato {

        private Vector coleccion;

        public ColeccionTipoDato() {
            coleccion = new Vector();
        }

        public Vector getColeccion() {
            return coleccion;
        }

        public void setColeccion(Vector tipoDato) {
            this.coleccion = tipoDato;
        }

		public void add(TipoDatoAnalisis tipoDatoAnalisis) {
            coleccion.add(tipoDatoAnalisis);
        }

        public int getSize() {
            return coleccion.size();
        }

        public Object get(int index) {
            return coleccion.get(index);
        }

        public Object get(String s) {
            for(Iterator i = coleccion.iterator(); i.hasNext();) {
                TipoDatoAnalisis tipoDatoAnalisis = (TipoDatoAnalisis)i.next();
                if(tipoDatoAnalisis.getCampo().equals(s))
                    return tipoDatoAnalisis;
            }
            return null;
        }

		public Vector getGrupo() {
            Vector grupo = new Vector();
            for(Iterator i = coleccion.iterator(); i.hasNext();) {
                TipoDatoAnalisis tipoDatoAnalisis = (TipoDatoAnalisis)i.next();
                if(tipoDatoAnalisis.getTipo() == TipoDatoAnalisis.TIPO_GRUPO)
                    grupo.add(tipoDatoAnalisis);
            }
            return grupo;
        }

		public Vector getInfo() {
            Vector info = new Vector();
            for(Iterator i = coleccion.iterator(); i.hasNext();) {
                TipoDatoAnalisis tipoDatoAnalisis = (TipoDatoAnalisis)i.next();
                if(tipoDatoAnalisis.getTipo() == TipoDatoAnalisis.TIPO_INFO)
                    info.add(tipoDatoAnalisis);
            }
            return info;
        }

		public Vector getDato() {
            Vector dato = new Vector();
            for(Iterator i = coleccion.iterator(); i.hasNext();) {
                TipoDatoAnalisis tipoDatoAnalisis = (TipoDatoAnalisis)i.next();
                if(tipoDatoAnalisis.getTipo() == TipoDatoAnalisis.TIPO_DATO)
                    dato.add(tipoDatoAnalisis);
            }
            return dato;
        }

		public Vector getSecciones() {
            Vector v = new Vector();
            v.addAll(getGrupo());
            v.addAll(getInfo());
            v.addAll(getDato());
            return v;
        }

        public boolean isEmpty() {
            return (getSize() == 0);
        }

        public void clear() {
            coleccion.clear();
        }
    }

    //Clase Selector
    public class SelectorAnalisis extends JDialog {

    	private static final long serialVersionUID = 8203464942551730878L;
		
    	private FWJTable tablaGrupo;
        private FWJTable tablaInfo;
        private FWJTable tablaDato;
        private JButton btnDerecha;
        private JButton btnIzquierda;
        private JButton btnArriba;
        private JButton btnAbajo;
        private JButton btnAceptar;
        private JButton btnCancelar;
        private JCheckBox chkModoReducido;
        private JComboBox cmbOperacionesGrupo;
        private JComboBox cmbOperacionesDato;
        private FWJTable tablaOrigen;
        private ColeccionTipoDato coleccion;
        private boolean analisisReducido;
        private boolean aceptar;
        private final String OPERACIONES_GRUPO[] = { FWJTableAnalisis.OPERACION_SUM, FWJTableAnalisis.OPERACION_CONT, FWJTableAnalisis.OPERACION_PROM, FWJTableAnalisis.OPERACION_MIN, FWJTableAnalisis.OPERACION_MAX };
        private final String OPERACIONES_DATO[] = { FWJTableAnalisis.OPERACION_VAL, FWJTableAnalisis.OPERACION_PORC };
        private static final int ACCION_IZQUIERDA = 1;
        private static final int ACCION_DERECHA = 2;
        private static final int ACCION_ARRIBA = 3;
        private static final int ACCION_ABAJO = 4;
        private static final String DEFAULT_IMAGEN_FLECHA_DERECHA = "ar/com/fwcommon/imagenes/flecha_derecha.png";
        private static final String DEFAULT_IMAGEN_FLECHA_IZQUIERDA = "ar/com/fwcommon/imagenes/flecha_izquierda.png";
        private static final String DEFAULT_IMAGEN_FLECHA_ARRIBA = "ar/com/fwcommon/imagenes/flecha_arriba.png";
        private static final String DEFAULT_IMAGEN_FLECHA_ABAJO = "ar/com/fwcommon/imagenes/flecha_abajo.png";
        private static final String DEFAULT_IMAGEN_FLECHA_DERECHA_DES = "ar/com/fwcommon/imagenes/flecha_derecha_des.png";
        private static final String DEFAULT_IMAGEN_FLECHA_IZQUIERDA_DES = "ar/com/fwcommon/imagenes/flecha_izquierda_des.png";
        private static final String DEFAULT_IMAGEN_FLECHA_ARRIBA_DES = "ar/com/fwcommon/imagenes/flecha_arriba_des.png";
        private static final String DEFAULT_IMAGEN_FLECHA_ABAJO_DES = "ar/com/fwcommon/imagenes/flecha_abajo_des.png";
        private static final String DEFAULT_IMAGEN_GAP = "ar/com/fwcommon/imagenes/gap.png";

        /**
         * Método constructor.
         * @param owner
         */
        public SelectorAnalisis(Frame owner, ColeccionTipoDato coleccion) {
            super(owner, true);
            this.coleccion = coleccion;
            construct();
            inicializar();
        }

        //Construye gráficamente el componente
        private void construct() {
            setTitle("Análisis - Selector");
            setSize(new Dimension(500, 410));
            setResizable(false);
            getContentPane().setLayout(new FlowLayout());
            //Listas
            JPanel panListas = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            getContentPane().add(panListas);
            //Grupo
            JPanel panGrupo = new JPanel(new BorderLayout(0, 0));
            panListas.add(panGrupo);
            JLabel lblGrupo = new JLabel("Grupo");
            lblGrupo.setFont(fuente);
            panGrupo.add(lblGrupo, BorderLayout.NORTH);
            tablaGrupo = new FWJTable(0, 2);
            JScrollPane panTablaGrupo = new JScrollPane(tablaGrupo);
            panTablaGrupo.setPreferredSize(new Dimension(150, 180));
            panTablaGrupo.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            panGrupo.add(panTablaGrupo, BorderLayout.CENTER);
            tablaGrupo.addFocusListener(new TablaFocusListener());
            tablaGrupo.getSelectionModel().addListSelectionListener(new TablaSelectionListener(tablaGrupo));
            //Info
            JPanel panInfo = new JPanel(new BorderLayout(0, 0));
            panListas.add(panInfo);
            JLabel lblInfo = new JLabel("Info.");
            lblInfo.setFont(fuente);
            panInfo.add(lblInfo, BorderLayout.NORTH);
            tablaInfo = new FWJTable(0, 2);
            JScrollPane panTablaInfo = new JScrollPane(tablaInfo);
            panTablaInfo.setPreferredSize(new Dimension(150, 180));
            panTablaInfo.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            panInfo.add(panTablaInfo, BorderLayout.CENTER);
            tablaInfo.addFocusListener(new TablaFocusListener());
            tablaInfo.getSelectionModel().addListSelectionListener(new TablaSelectionListener(tablaInfo));
            //Dato
            JPanel panDato = new JPanel(new BorderLayout(0, 0));
            panListas.add(panDato);
            JLabel lblDato = new JLabel("Dato");
            lblDato.setFont(fuente);
            panDato.add(lblDato, BorderLayout.NORTH);
            tablaDato = new FWJTable(0, 2);
            JScrollPane panTablaDato = new JScrollPane(tablaDato);
            panTablaDato.setPreferredSize(new Dimension(150, 180));
            panTablaDato.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            panDato.add(panTablaDato, BorderLayout.CENTER);
            tablaDato.addFocusListener(new TablaFocusListener());
            tablaDato.getSelectionModel().addListSelectionListener(new TablaSelectionListener(tablaDato));
            //Selector
            JPanel panSelector = new JPanel(new GridLayout(3, 3));
            getContentPane().add(panSelector);
            //gap
            panSelector.add(new JLabel(ImageUtil.loadIcon(DEFAULT_IMAGEN_GAP)));
            //Arriba
            btnArriba = new JButton();
            DecorateUtil.decorateButton(btnArriba, DEFAULT_IMAGEN_FLECHA_ARRIBA, DEFAULT_IMAGEN_FLECHA_ARRIBA_DES);
            panSelector.add(btnArriba);
            btnArriba.addActionListener(new BotonArribaSelector());
            //gap
            panSelector.add(new JLabel(ImageUtil.loadIcon(DEFAULT_IMAGEN_GAP)));
            //Izquierda
            btnIzquierda = new JButton();
            DecorateUtil.decorateButton(btnIzquierda, DEFAULT_IMAGEN_FLECHA_IZQUIERDA, DEFAULT_IMAGEN_FLECHA_IZQUIERDA_DES);
            panSelector.add(btnIzquierda);
            btnIzquierda.addActionListener(new BotonIzquierdaSelector());
            //gap
            panSelector.add(new JLabel(ImageUtil.loadIcon(DEFAULT_IMAGEN_GAP)));
            //Derecha
            btnDerecha = new JButton();
            DecorateUtil.decorateButton(btnDerecha, DEFAULT_IMAGEN_FLECHA_DERECHA, DEFAULT_IMAGEN_FLECHA_DERECHA_DES);
            panSelector.add(btnDerecha);
            btnDerecha.addActionListener(new BotonDerechaSelector());
            //gap
            panSelector.add(new JLabel(ImageUtil.loadIcon(DEFAULT_IMAGEN_GAP)));
            //Abajo
            btnAbajo = new JButton();
            DecorateUtil.decorateButton(btnAbajo, DEFAULT_IMAGEN_FLECHA_ABAJO, DEFAULT_IMAGEN_FLECHA_ABAJO_DES);
            panSelector.add(btnAbajo);
            btnAbajo.addActionListener(new BotonAbajoSelector());
            //gap
            panSelector.add(new JLabel(ImageUtil.loadIcon(DEFAULT_IMAGEN_GAP)));
            //Panel Pie
            JPanel panPie = new JPanel(new BorderLayout());
            getContentPane().add(panPie);
            //Modo Reducido
            chkModoReducido = new JCheckBox("Modo Reducido");
            DecorateUtil.decorateCheckBox(chkModoReducido, false);
            panPie.add(chkModoReducido, BorderLayout.NORTH);
            //Botones
            JPanel panBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            panBotones.setPreferredSize(new Dimension(this.getWidth() - 20, 50));
            panPie.add(panBotones, BorderLayout.CENTER);
            //Aceptar
            btnAceptar = new JButton("Aceptar");
            btnAceptar.setPreferredSize(new Dimension(80, 20));
            DecorateUtil.decorateButton(btnAceptar);
            panBotones.add(btnAceptar);
            btnAceptar.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    if(validar()) {
                        setearAccionesTipos();
                        aceptar = true;
                        analisisReducidoGral = chkModoReducido.isSelected();                        
                        dispose();
                    }
                }
            });
            //Cancelar
            btnCancelar = new JButton("Cancelar");
            btnCancelar.setPreferredSize(new Dimension(80, 20));
            DecorateUtil.decorateButton(btnCancelar);
            panBotones.add(btnCancelar);
            btnCancelar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    dispose();
                }
            });
            constuirTablas();
            GuiUtil.centrar(this);
        }

        private void constuirTablas() {
            //Grupo
            tablaGrupo.setStringColumn(0, "Grupo", 60);
            cmbOperacionesGrupo = new JComboBox(OPERACIONES_GRUPO);
            tablaGrupo.setComboColumn(1, "Oper.", cmbOperacionesGrupo, 40, false);
            //Info
            tablaInfo.setStringColumn(0, "Campo", 90, 100, true);
            tablaInfo.setCheckColumn(1, "Ver", 30, false);
            //Dato
            tablaDato.setStringColumn(0, "Campo", 60);
            cmbOperacionesDato = new JComboBox(OPERACIONES_DATO);
            tablaDato.setComboColumn(1, "Oper.", cmbOperacionesDato, 40, false);
        }

        /**
         * Inicializa el componente con los datos de las cols. de la tabla. (Se
         * carga la tabla 'Info.' con los headers de tabla).
         * @param cols
         */
		public void inicializar() {
            Collections.sort(coleccion.getColeccion(), new ComparadorTipoDatoAnalisis());
        	for(int i = 0; i < coleccion.getSize(); i++) {
                TipoDatoAnalisis tipoDatoAnalisis = (TipoDatoAnalisis)coleccion.get(i);
                switch(tipoDatoAnalisis.getTipo()) {
                    case TipoDatoAnalisis.TIPO_GRUPO:
                    tablaGrupo.addRow();
                    tablaGrupo.setValueAt(tipoDatoAnalisis.getCampo(), tablaGrupo.getRowCount() - 1, 0);
                    tablaGrupo.setValueAt(tipoDatoAnalisis.getAccion(), tablaGrupo.getRowCount() - 1, 1);
                    break;
                    case TipoDatoAnalisis.TIPO_INFO:
                    tablaInfo.addRow();
                    tablaInfo.setValueAt(tipoDatoAnalisis.getCampo(), tablaInfo.getRowCount() - 1, 0);
                    tablaInfo.setValueAt(Boolean.valueOf(true), tablaInfo.getRowCount() - 1, 1);
                    break;
                    case TipoDatoAnalisis.TIPO_DATO:
                    tablaDato.addRow();
                    tablaDato.setValueAt(tipoDatoAnalisis.getCampo(), tablaDato.getRowCount() - 1, 0);
                    tablaDato.setValueAt(tipoDatoAnalisis.getAccion(), tablaDato.getRowCount() - 1, 1);
                    break;
                }
            }
        }
        
        /** Clase listener del evento de foco de las tablas */
        private class TablaFocusListener implements FocusListener {

            public void focusGained(FocusEvent evt) {
                tablaOrigen = (FWJTable)evt.getSource();
                if(tablaOrigen.equals(tablaGrupo))
                    btnIzquierda.setEnabled(false);
                else if(tablaOrigen.equals(tablaDato))
                    btnDerecha.setEnabled(false);
                habilitarSelector();
            }

            public void focusLost(FocusEvent evt) {
                FWJTable t = (FWJTable)evt.getSource();
                if(t.equals(tablaGrupo))
                    btnIzquierda.setEnabled(true);
                else if(t.equals(tablaDato))
                    btnDerecha.setEnabled(true);
                habilitarSelector();
            }
        }

        /** Clase listener del evento de selección de filas de las tablas */
        private class TablaSelectionListener implements ListSelectionListener {

            private FWJTable tabla;

            public TablaSelectionListener(FWJTable tabla) {
                this.tabla = tabla;
            }

            public void valueChanged(ListSelectionEvent evt) {
                if(tabla.getSelectedRow() == 0)
                    btnArriba.setEnabled(false);
                else if(tabla.getSelectedRow() == tabla.getRowCount() - 1)
                    btnAbajo.setEnabled(false);
                else {
                    btnArriba.setEnabled(true);
                    btnAbajo.setEnabled(true);
                }
            }
        }

        /** Clase listener del evento click del botón 'Arriba' del Selector */
        private class BotonArribaSelector implements ActionListener {

            public void actionPerformed(ActionEvent evt) {
                int filaSel = tablaOrigen.getSelectedRow();
                if(filaSel != -1 && filaSel != 0) {
                    ((DefaultTableModel)tablaOrigen.getModel()).moveRow(filaSel, filaSel, filaSel - 1);
                    tablaOrigen.setRowSelectionInterval(filaSel - 1, filaSel - 1);
                }
            }
        }

        /** Clase listener del evento click del botón 'Abajo' del Selector */
        private class BotonAbajoSelector implements ActionListener {

            public void actionPerformed(ActionEvent evt) {
                int filaSel = tablaOrigen.getSelectedRow();
                if(filaSel != -1 && filaSel != tablaOrigen.getRowCount() - 1) {
                    ((DefaultTableModel)tablaOrigen.getModel()).moveRow(filaSel, filaSel, filaSel + 1);
                    tablaOrigen.setRowSelectionInterval(filaSel + 1, filaSel + 1);
                }
            }
        }

        /** Clase listener del evento click del botón 'Izquierda' del Selector */
        private class BotonIzquierdaSelector implements ActionListener {

            public void actionPerformed(ActionEvent evt) {
                procesarAccion(getTablaDestino(ACCION_IZQUIERDA));
            }
        }

        /** Clase listener del evento click del botón 'Derecha' del Selector */
        private class BotonDerechaSelector implements ActionListener {

            public void actionPerformed(ActionEvent evt) {
                procesarAccion(getTablaDestino(ACCION_DERECHA));
            }
        }

        /**
         * Proceso de la acción del Selector.
         * @param tablaDestino
         */
        private void procesarAccion(FWJTable tablaDestino) {
            int filaSel = tablaOrigen.getSelectedRow();
            if(filaSel != -1) {
                tablaDestino.addRow();
                String valor = (String)tablaOrigen.getValueAt(filaSel, 0);
                tablaDestino.setValueAt(valor, tablaDestino.getRowCount() - 1, 0);
                if(tablaDestino.equals(tablaGrupo))
                    tablaDestino.setValueAt(FWJTableAnalisis.OPERACION_SUM, tablaDestino.getRowCount() - 1, 1);
                else if(tablaDestino.equals(tablaInfo))
                    tablaDestino.setValueAt(Boolean.valueOf(true), tablaDestino.getRowCount() - 1, 1);
                else if(tablaDestino.equals(tablaDato))
                    tablaDestino.setValueAt(FWJTableAnalisis.OPERACION_VAL, tablaDestino.getRowCount() - 1, 1);
                tablaOrigen.removeRow(filaSel);
                tablaDestino.selectAndScroll(tablaDestino.getRowCount() - 1, tablaDestino.getRowCount() - 1);
                tablaOrigen = tablaDestino;
                habilitarSelector();
            }
        }

        /**
         * Devuelve la tabla Destino a partir de la acción ejecutada.
         * @param accion
         * @return
         */
        private FWJTable getTablaDestino(int accion) {
            if(tablaOrigen != null) {
                switch(accion) {
                    case ACCION_IZQUIERDA:
                    if(tablaOrigen.equals(tablaDato))
                        return tablaInfo;
                    else if(tablaOrigen.equals(tablaInfo))
                        return tablaGrupo;
                    case ACCION_DERECHA:
                    if(tablaOrigen.equals(tablaGrupo))
                        return tablaInfo;
                    else if(tablaOrigen.equals(tablaInfo))
                        return tablaDato;
                    case ACCION_ARRIBA:
                    return tablaOrigen;
                    case ACCION_ABAJO:
                    return tablaOrigen;
                }
            }
            return null;
        }

        /** Habilita/Deshabilita el Selector */
        private void habilitarSelector() {
            //Según la tabla que tiene foco
            if(tablaOrigen.equals(tablaGrupo)) {
                btnIzquierda.setEnabled(false);
                btnDerecha.setEnabled(true);
            } else if(tablaOrigen.equals(tablaInfo)) {
                btnIzquierda.setEnabled(true);
                btnDerecha.setEnabled(true);
            } else if(tablaOrigen.equals(tablaDato)) {
                btnDerecha.setEnabled(false);
                btnIzquierda.setEnabled(true);
            }
            //Según la cantidad de filas y posición de la fila seleccionada
            if(tablaOrigen.getRowCount() == 1) {
                btnArriba.setEnabled(false);
                btnAbajo.setEnabled(false);
            } else if(tablaOrigen.getRowCount() == 0) {
                btnArriba.setEnabled(false);
                btnAbajo.setEnabled(false);
                btnIzquierda.setEnabled(false);
                btnDerecha.setEnabled(false);
            } else if(tablaOrigen.getSelectedRow() == 0) {
                btnArriba.setEnabled(false);
                btnAbajo.setEnabled(true);
            } else if(tablaOrigen.getSelectedRow() == tablaOrigen.getRowCount() - 1) {
                btnAbajo.setEnabled(false);
                btnArriba.setEnabled(true);
            }
        }

        private void setearAccionesTipos() {
            for(int i = 0; i < tablaGrupo.getRowCount(); i++) {
                TipoDatoAnalisis tipoDatoAnalisis = (TipoDatoAnalisis)coleccion.get((String)tablaGrupo.getValueAt(i, 0));
                tipoDatoAnalisis.setTipo(TipoDatoAnalisis.TIPO_GRUPO);
                tipoDatoAnalisis.setPosicionAnalisis(i);
                tipoDatoAnalisis.setPrioridad(i);
                tipoDatoAnalisis.setAccion(tablaGrupo.getValueAt(i, 1));
            }
            for(int i = 0; i < tablaInfo.getRowCount(); i++) {
                TipoDatoAnalisis tipoDatoAnalisis = (TipoDatoAnalisis)coleccion.get((String)tablaInfo.getValueAt(i, 0));
                tipoDatoAnalisis.setTipo(TipoDatoAnalisis.TIPO_INFO);
                tipoDatoAnalisis.setPosicionAnalisis(i + tablaGrupo.getRowCount());
                tipoDatoAnalisis.setPrioridad(i);
                tipoDatoAnalisis.setAccion((Boolean)tablaInfo.getTypedValueAt(i, 1));
            }
            for(int i = 0; i < tablaDato.getRowCount(); i++) {
                TipoDatoAnalisis tipoDatoAnalisis = (TipoDatoAnalisis)coleccion.get((String)tablaDato.getValueAt(i, 0));
                tipoDatoAnalisis.setTipo(TipoDatoAnalisis.TIPO_DATO);
                tipoDatoAnalisis.setPosicionAnalisis(i + tablaGrupo.getRowCount() + tablaInfo.getRowCount());
                tipoDatoAnalisis.setPrioridad(i);
                tipoDatoAnalisis.setAccion(tablaDato.getValueAt(i, 1));
            }
        }

        private boolean validar() {
            for(int i = 0; i < tablaGrupo.getRowCount(); i++) {
                if(tablaGrupo.getValueAt(i, 1) == null) {
                    String grupo = (String)tablaGrupo.getValueAt(i, 0);
                    FWJOptionPane.showErrorMessage(null, "Seleccione una operación para el grupo '" + grupo + "'", "Error");
                    tablaGrupo.changeSelection(i, 0, false, false);
                    tablaInfo.clearSelection();
                    tablaDato.clearSelection();
                    return false;
                }
            }
            for(int i = 0; i < tablaDato.getRowCount(); i++) {
                if(tablaDato.getValueAt(i, 1) == null) {
                    String dato = (String)tablaDato.getValueAt(i, 0);
                    FWJOptionPane.showErrorMessage(null, "Seleccione una operación para el dato '" + dato + "'", "Error");
                    tablaGrupo.clearSelection();
                    tablaInfo.clearSelection();
                    tablaDato.changeSelection(i, 0, false, false);
                    return false;
                }
            }
            return true;
        }

        public boolean procesarAnalisisReducido() {
            return analisisReducido;
        }

        public boolean isAceptar() {
            return aceptar;
        }
    }

    public class CorteControl {

        private int filaCorte;
        private int colCorte;
        private int total;
        private int filaDesde;

        public CorteControl(int filaCorte, int colCorte, int filaDesde,
                int total) {
            this.filaCorte = filaCorte;
            this.colCorte = colCorte;
            this.total = total;
            this.filaDesde = filaDesde;
        }

        public CorteControl(int filaCorte, int colCorte, int filaDesde) {
            this.filaCorte = filaCorte;
            this.colCorte = colCorte;
            this.filaDesde = filaDesde;
        }

        public int getColCorte() {
            return colCorte;
        }

        public int getFilaCorte() {
            return filaCorte;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getFilaDesde() {
            return filaDesde;
        }
    }

    /** Renderer de la tabla */
    private class FWJTableAnalisisRenderer extends FWJTableRenderer {

		private static final long serialVersionUID = 3147290356966113907L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            if(analisis /*&& !isSelected*/) {
                if(isColGrupo(col)) {
                    if(!operar || !isFilaCorte(row) && row != getRowCount() - 1)
                        setBackground(colorGrupo);
                    else if(isFilaCortePrincipal(row))
                        setBackground(colorCortePrincipal);
                    else if(isFilaCorte(row)) {
                        int colCorte = getColCorte(row);
                        if(col < colCorte)
                            setBackground(colorGrupo);
                        else
                            setBackground(colorCorte);
                    } else if(row == getRowCount() - 1) {
                    	setBackground(colorTotal);
                    	if(value != null) {
                    		setValue(value.toString());                    		
                    	}
                    }
                } else if(operar && isFilaCortePrincipal(row)) {
                    setBackground(colorCortePrincipal);
                    if(mostrarPorcentaje(row, col)) {
                        NumberFormat nf = new DecimalFormat("#.##'%'");
                        try {
                            String valorFormateado = nf.format(value);
                            setValue(valorFormateado);
                        } catch(Exception e) {
                            setValue(value);
                        }
                    } else if(mostrarDecimales(row)) {
                        NumberFormat nf = new DecimalFormat("#.##");
                        try {
                            String valorFormateado = nf.format(value);
                            setValue(valorFormateado);
                        } catch(Exception e) {
                            setValue(value);
                        }
                    }
                } else if(operar && isFilaCorte(row)) {
                    setBackground(colorCorte);
                    if(mostrarPorcentaje(row, col)) {
                        NumberFormat nf = new DecimalFormat("#.##'%'");
                        try {
                            String valorFormateado = nf.format(value);
                            setValue(valorFormateado);
                        } catch(Exception e) {
                            setValue(value);
                        }
                    } else if(mostrarDecimales(row)) {
                        NumberFormat nf = new DecimalFormat("#.##");
                        try {
                            String valorFormateado = nf.format(value);
                            setValue(valorFormateado);
                        } catch(Exception e) {
                            setValue(value);
                        }
                    }
                } else if(operar && row == getRowCount() - 1 && !isSelected)
                    setBackground(colorTotal);
                else if(getBackgroundCell(row, col) != null && !isSelected)
                    setBackground(getBackgroundCell(row, col));
                else if(getBackgroundRow(row) != null && !isSelected)
                    setBackground(getBackgroundRow(row));
                else if(!isSelected)
                    setBackground(table.getBackground());
                if(operar) {
                	if(isFilaCorte(row)) {
                		if((value != null && value.equals(MENSAJE_NO_CALCULABLE)) || tiposColumnas[col] instanceof OperableTimeColumn)
                			setHorizontalAlignment(CENTER_ALIGN);
                		else if(col >= getColCorte(row))
                			setHorizontalAlignment(RIGHT_ALIGN);
                	} else if(row == getRowCount() - 1) {
                		if(value != null && value.equals(MENSAJE_NO_CALCULABLE))
                			setHorizontalAlignment(CENTER_ALIGN);
                		else {
                			setHorizontalAlignment(RIGHT_ALIGN);
                		}
                	} else if(value != null && value.equals(MENSAJE_NO_CALCULABLE))
                		setHorizontalAlignment(CENTER_ALIGN);                	
                }
            }
            return component;
        }
    }

    /**
     * Devuelve <b>true </b> si es necesario formatear el total de
     * <code>fila</code>,<code>col</code> porque se trata de una operación
     * PORC.
     * @param fila
     * @param col
     * @return
     */
    private boolean mostrarPorcentaje(int fila, int col) {
        for(Iterator i = coleccion.getDato().iterator(); i.hasNext();) {
            TipoDatoAnalisis tipoDato = (TipoDatoAnalisis)i.next();
            if(tipoDato.getPosicionAnalisis() == col && tipoDato.getAccion().equals(OPERACION_PORC)) {
                int colCorte = getColCorte(fila);
                for(Iterator j = coleccion.getGrupo().iterator(); j.hasNext();) {
                    TipoDatoAnalisis tipoGrupo = (TipoDatoAnalisis)j.next();
                    if(tipoGrupo.getPosicionAnalisis() == colCorte) {
                        if(tipoGrupo.getAccion().equals(OPERACION_SUM) || tipoGrupo.getAccion().equals(OPERACION_CONT))
                            return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Devuelve <b>true </b> si es necesario formatear el total de
     * <code>fila</code> porque se trata de una operación PROM.
     * @param fila
     * @return
     */
    private boolean mostrarDecimales(int fila) {
        int colCorte = getColCorte(fila);
        for(Iterator i = coleccion.getGrupo().iterator(); i.hasNext();) {
            TipoDatoAnalisis tipoDato = (TipoDatoAnalisis)i.next();
            if(tipoDato.getPosicionAnalisis() == colCorte) {
                if(tipoDato.accion.equals(OPERACION_PROM))
                    return true;
            }
        }
        return false;
    }

    private class MultiLineCellRendererAnalisis extends MultiLineCellRenderer {

		private static final long serialVersionUID = -1978497457344313850L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            if(analisis) {
                //color de fondo
                if(isColGrupo(col)) {
                    if(!operar || !isFilaCorte(row) && row != getRowCount() - 1)
                        setBackground(colorGrupo);
                    else if(isFilaCortePrincipal(row))
                        setBackground(colorCortePrincipal);
                    else if(isFilaCorte(row)) {
                        int colCorte = getColCorte(row);
                        if(col < colCorte)
                            setBackground(colorGrupo);
                        else
                            setBackground(colorCorte);
                    } else if(row == getRowCount() - 1)
                        setBackground(colorTotal);
                } else if(operar && isFilaCortePrincipal(row))
                    setBackground(colorCortePrincipal);
                else if(operar && isFilaCorte(row))
                    setBackground(colorCorte);
                else if(operar && row == getRowCount() - 1)
                    setBackground(colorTotal);
                else if(getBackgroundCell(row, col) != null)
                    setBackground(getBackgroundCell(row, col));
                else if(getBackgroundRow(row) != null)
                    setBackground(getBackgroundRow(row));
                else
                    setBackground(table.getBackground());
                if(operar) {
                	if(isFilaCorte(row)) {
                		if(col >= getColCorte(row))
                			setAlignmentX(JLabel.RIGHT_ALIGNMENT);
                	} else if(row == getRowCount() - 1)
                		setAlignmentX(JLabel.RIGHT_ALIGNMENT);                	
                }
            }
            return component;
        }
    }
    
    /**
     * Establece una columna del tipo <b>Multiline </b>.
     * @param col
     * @param nombre
     * @param ancho
     * @param lock
     */
    public void setMultilineColumn(int col, String nombre, int ancho, boolean lock) {
        MultilineColumn c = new MultilineColumn();
        tiposColumnas[col] = c;
        c.setTextLeftAlign(true);
        c.setSize(ancho);
        setCLColumnData(col, c, nombre, ancho, lock);
        getColumnModel().getColumn(col).setCellRenderer(new MultiLineCellRendererAnalisis());
        getColumn(col).setCellEditor(new MultilineStringEditor());
    }

    /** Clase para el manejo de los eventos del mouse */
    private class TablaMouseAdapter extends MouseAdapter {

        public void mousePressed(MouseEvent evt) {
            if((permiteSelector || permiteCopiar()) && evt.isPopupTrigger()) {
                PopupMenu popup = new PopupMenu();
                popup.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }

        public void mouseReleased(MouseEvent evt) {
            if((permiteSelector || permiteCopiar()) && evt.isPopupTrigger()) {
                PopupMenu popup = new PopupMenu();
                popup.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }
    
    boolean redefinir = false;
    
    private void setRedefinir(boolean redefinir) {
    	this.redefinir = redefinir;
    }
    
    private boolean isRedefinir() {
    	return redefinir;
    }

    /** Clase para el menú emergente de la tabla */
    private class PopupMenu extends JPopupMenu {

		private static final long serialVersionUID = -1347808587070612393L;

		public PopupMenu() {
            if(permiteSelector) {
                JMenuItem modo = new JMenuItem();
                JMenuItem redefinir = new JMenuItem("Redefinir análisis");
                if(analisis) {                	
                	modo.setText("Normal");
                	add(redefinir);
                } else {
                	modo.setText("Análisis");                	
                }
                add(modo);
                redefinir.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						setRedefinir(true);
						setModoAnalisis(analisis);
					}                	
                });
                modo.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if(analisis) {
                        	setRedefinir(false);
                        }
                    	setModoAnalisis(!analisis);
                    }
                });
            }
            if(permiteSelector && permiteCopiar())
                addSeparator();
            if(permiteCopiar()) {
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

    /**
     * Devuelve un vector con los datos necesarios para generar un gráfico de
     * torta.
     * @param filaDesde
     * @param filaHasta
     * @param colCategoria
     * @param colData
     * @return
     */
	public Vector getDataPieChart(int filaDesde, int filaHasta, int colCategoria, int colData) {
        Vector categorias = new Vector();
        Vector categoriasTemp = new Vector();
        Vector dataChart = new Vector();
        Vector valores = new Vector();
        Vector dataVector = getDataVector();
        for(int i = filaDesde; i <= filaHasta; i++) {
            if(!isFilaCorte(i)) {
                Object categoria = ((Vector)dataVector.get(i)).get(colCategoria);
                if(categoria != null) {
                    if(!categoriasTemp.contains(categoria))
                        categoriasTemp.add(categoria);
                }
            }
        }
        for(Iterator i = categoriasTemp.iterator(); i.hasNext();) {
            int total = 0;
            Object categoria = i.next();
            for(int j = filaDesde; j <= filaHasta; j++) {
                if(!isFilaCorte(j)) {
                    Object categoriaTemp = ((Vector)dataVector.get(j)).get(colCategoria);
                    if(categoriaTemp != null) {
                        if(categoria.equals(categoriaTemp)) {
                            int valor = ((Integer)((Vector)dataVector.get(j)).get(colData)).intValue();
                            total += valor;
                        }
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
	public Vector getDataBarChart(int filaDesde, int filaHasta, int colCategorias, int colSeries, int colData) {
        Vector dataCategorias = new Vector();
        Vector dataChart = new Vector();
        Vector nombresCategorias = new Vector();
        Vector nombresSeries = new Vector();
        Vector dataVector = getDataVector();
        for(int i = filaDesde; i <= filaHasta; i++) {
            if(!isFilaCorte(i)) {
                Object nombreCategoria = ((Vector)dataVector.get(i)).get(colCategorias);
                if(nombreCategoria != null) {
                    if(!nombresCategorias.contains(nombreCategoria))
                        nombresCategorias.add(nombreCategoria);
                }
                Object nombreSerie = ((Vector)dataVector.get(i)).get(colSeries);
                if(nombreSerie != null) {
                    if(!nombresSeries.contains(nombreSerie))
                        nombresSeries.add(nombreSerie);
                }
            }
        }
        for(Iterator i = nombresSeries.iterator(); i.hasNext();) {
            Vector dataSerie = new Vector();
            Object nombreSerie = i.next();
            for(Iterator j = nombresCategorias.iterator(); j.hasNext();) {
                Object nombreCategoria = j.next();
                Object valor = null;
                for(int k = filaDesde; k <= filaHasta; k++) {
                    if(!isFilaCorte(k)) {
                        Object nombreCategoriaTemp = getValueAt(k, colCategorias);
                        Object nombreSerieTemp = getValueAt(k, colSeries);
                        if(nombreCategoria.equals(nombreCategoriaTemp) && nombreSerie.equals(nombreSerieTemp)) {
                            valor = getValueAt(k, colData);
                            break;
                        }
                    }
                }
                dataSerie.add(valor);
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
        	if(!isModoAnalisis() || (!isFilaCorte(i) && i != getRowCount() - 1)) {
                int contCols = 0;
                for(int j = colDesde; j <= colHasta; j++) {
                    dataVectorResultado[contFilas][contCols] = ((Vector)dataVectorOriginal.get(i)).get(j);
                    contCols++;
                }
                contFilas++;
            }
        }
        return dataVectorResultado;
    }
    
    public Object[][] getDataVector(int colDesde, int colHasta) {
        int contFilas = 0;
        for(int i = 0; i < getRowCount(); i++) {
        	if(!isModoAnalisis() || (!isFilaCorte(i) && i != getRowCount() - 1)) {
                contFilas++;
            }
        }
        Object[][] dataVectorResultado = new Object[contFilas][colHasta - colDesde + 1];
        contFilas = 0;
        Vector dataVectorOriginal = getDataVector();
        for(int i = 0; i < getRowCount(); i++) {
        	if(!isModoAnalisis() || (!isFilaCorte(i) && i != getRowCount() - 1)) {
                int contCols = 0;
                for(int j = colDesde; j <= colHasta; j++) {
                    dataVectorResultado[contFilas][contCols] = ((Vector)dataVectorOriginal.get(i)).get(j);
                    contCols++;
                }
                contFilas++;
            }
        }
        return dataVectorResultado;
    }
    
    public void ocultarColumnaInfo(int posicion, boolean ver) {
    	Vector<TipoDatoAnalisis> tiposDatoInfo = coleccion.getInfo();
    	for(TipoDatoAnalisis tipoDatoAnalisis : tiposDatoInfo) {
    		if(tipoDatoAnalisis.getPosicionAnalisis() == posicion) {
    			tipoDatoAnalisis.setAccion(ver);
    		}
    	}
    }

	
	public boolean isOperar() {
		return operar;
	}

	
	public void setOperar(boolean operar) {
		this.operar = operar;
	}

	
	public boolean isOrdenar() {
		return ordenar;
	}

	public void setOrdenar(boolean ordenar) {
		this.ordenar = ordenar;
	}

	public boolean isSeleccionEnBloque() {
		return seleccionEnBloque;
	}

	public void setSeleccionEnBloque(boolean seleccionEnBloque) {
		this.seleccionEnBloque = seleccionEnBloque;
	}

	public void valueChanged(ListSelectionEvent evt) {
		super.valueChanged(evt);
		if(seleccionEnBloque && isModoAnalisis() && seleccionarBloqueCeldas) {
			seleccionarBloqueCeldas = false;
			seleccionarCeldasGrupo(getGruposCeldas(getSelectedRow()));
			seleccionarBloqueCeldas = true;
		}
	}
	
	private List<GrupoCeldas> getGruposCeldas(int fila) {
		List<GrupoCeldas> gruposCeldasFila = new ArrayList<GrupoCeldas>();
		if(fila != -1) {
			List<GrupoCeldas> gruposCeldasTabla = getGruposCeldas();
			for(GrupoCeldas grupoCeldas : gruposCeldasTabla) {
				if(fila >= grupoCeldas.getFila() && fila <= grupoCeldas.getFila() + grupoCeldas.getAlto() - 1) {
					gruposCeldasFila.add(grupoCeldas);
				}
			}
		}
		return gruposCeldasFila;			
	}
	
	private void seleccionarCeldasGrupo(List<GrupoCeldas> gruposCeldas) {
		clearSelection();
		GrupoCeldas grupoCeldas = getGrupoCeldasGeneral(gruposCeldas);
		if(grupoCeldas != null) {
			int filaInicio = grupoCeldas.getFila();
			changeSelection(filaInicio + grupoCeldas.getAlto() - 1, getColumnCount() - 1, false, false);	
			changeSelection(filaInicio, grupoCeldas.getCol(), false, true);
		}
	}
	
	private GrupoCeldas getGrupoCeldasGeneral(List<GrupoCeldas> gruposCeldas) {
		GrupoCeldas grupoCeldasGeneral = null;
		for(GrupoCeldas grupoCeldas : gruposCeldas) {
			if(grupoCeldasGeneral == null || grupoCeldas.getCol() < grupoCeldasGeneral.getCol()) {
				grupoCeldasGeneral = grupoCeldas;
			}
		}
		return grupoCeldasGeneral;
	}
	
    private class ComparadorTipoDatoAnalisis implements Comparator<TipoDatoAnalisis> {

		public int compare(TipoDatoAnalisis o1, TipoDatoAnalisis o2) {
			return o1.getPosicionAnalisis() - o2.getPosicionAnalisis();
		}
    	
    }
    public boolean isAnalisisReducido() {
		return analisisReducidoGral;
	}

	
	public void setAnalisisReducido(boolean analisisReducidoGral) {
		this.analisisReducidoGral = analisisReducidoGral;
	}
}
package ar.com.fwcommon.templates.modulo;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

import org.apache.log4j.Logger;

import ar.com.fwcommon.boss.BossError;
import ar.com.fwcommon.componentes.FWCursor;
import ar.com.fwcommon.componentes.GuiPanelObservable;
import ar.com.fwcommon.componentes.GuiPanelObserver;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.entidades.Modulo;
import ar.com.fwcommon.templates.GuiForm;
import ar.com.fwcommon.templates.modulo.cabecera.Cabecera;
import ar.com.fwcommon.templates.modulo.gui.acciones.GuiAcciones;
import ar.com.fwcommon.templates.modulo.gui.acciones.IGuiAcciones;
import ar.com.fwcommon.templates.modulo.gui.filtros.GuiFiltros;
import ar.com.fwcommon.templates.modulo.gui.filtros.IGuiFiltros;
import ar.com.fwcommon.templates.modulo.gui.status.GuiStatuses;
import ar.com.fwcommon.templates.modulo.gui.status.IGuiStatuses;
import ar.com.fwcommon.templates.modulo.gui.tabla.GuiTabla;
import ar.com.fwcommon.templates.modulo.gui.tabla.IGuiTabla;
import ar.com.fwcommon.templates.modulo.gui.totales.GuiTotales;
import ar.com.fwcommon.templates.modulo.gui.totales.IGuiTotales;
import ar.com.fwcommon.templates.modulo.model.ModuloModel;
import ar.com.fwcommon.templates.modulo.model.acciones.Acciones;
import ar.com.fwcommon.templates.modulo.model.filtros.Filtros;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionExecutedEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionExecutedListener;
import ar.com.fwcommon.templates.modulo.model.listeners.ModelChangeEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.ModelChangeListener;
import ar.com.fwcommon.templates.modulo.model.status.Statuses;
import ar.com.fwcommon.templates.modulo.model.tabla.Tabla;
import ar.com.fwcommon.templates.modulo.model.totales.Totales;
import ar.com.fwcommon.templates.modulo.resources.InterModuleListener;
import ar.com.fwcommon.templates.modulo.resources.InterModuleMediator;

/**
 * Clase que consiste en un template para la implementación de módulos para una
 * aplicación. Dichos módulos se contruyen basicamente con una <b>cabecera</b>
 * en la parte superior, donde por ej. se muestran dos campos con las fechas
 * desde y hasta, un combobox para la selección del medio y otro combobox para
 * la selección del producto; un panel con una tabla en la parte media, donde se
 * muestra un listado de items; un panel con botones de acción para los items
 * por debajo de la tabla; y varios paneles con filtros en la parte inferior
 * para filtrar la lista de items en la tabla.
 * 
 * 
 * 
 * @param <T> Elemento con el que se llena la tabla
 * @param <MC> Modelo de la cabecera del módulo
 */
public abstract class ModuloTemplate<T, MC> extends GuiForm {

	private static final int TIEMPO_REFRESCO_DEFAULT = 240;
	private static final InterModuleMediator interModuleMediator = InterModuleMediator.getInstance();
	private int idModulo;
	private Cabecera<? extends MC> panCabecera = null;
	private JPanel encabezadoTabla = null;
	private JPanel pieTabla = null;
	private JPanel margenIzqTabla = null;
	private JPanel margenDerTabla = null;
	private JPanel panBotones = null;
	private List<ModuloModel<T, MC>> modulosModel;
	private IGuiTotales<T> guiTotales;
	private IGuiTabla<T> guiTabla;
	private IGuiFiltros<T> guiFiltros;
	private IGuiAcciones<T> guiAcciones;
	private IGuiStatuses<T> guiStatuses;
	private ModuloModel<T, MC> moduloModelActivo;
	private JComboBox cmbModelos;
	private JTabbedPane tabbedPane;
	private Timer timerRefresco;
	private EModelChangeType modelChangeType = EModelChangeType.TYPE_COMBOBOX;
	private int tiempoRefresco = TIEMPO_REFRESCO_DEFAULT;
	private FilaSelectionListener filaSelectionListener;
	private ColumnaSelectionListener columnaSelectionListener;
	private GuiPanelObserver observerGuiSet;
	private InterModuleListener interModuleListener;
	private ModelChangeListener modelChangeListener;
	private AccionExecutedListener accionExecutedListener;
	private FilaSelectionDobleClick mouseInputListener;

	/**
	 * Al implementar el constructor llamar al metodo actualizar() si se quiere
	 * cargar datos en la tabla al principio
	 * @param idModulo
	 * @throws FWException 
	 */
	protected ModuloTemplate(int idModulo) throws FWException {
		this(idModulo, EModelChangeType.TYPE_COMBOBOX);
	}

	protected ModuloTemplate(int idModulo, EModelChangeType modelChangeType) throws FWException {
		super();
		setIdModulo(idModulo);
		this.modelChangeType = modelChangeType;
		setModulosModel(createModulosModel());
		setModuloModelActivo(getModulosModel().get(0));
		interModuleMediator.addInterModuleListener(getInterModuleListener());
		construct();
//		addInternalFrameListener(new VentanaListener());
	}

	protected ModuloTemplate(Modulo modulo) throws FWException {
		this(modulo, EModelChangeType.TYPE_COMBOBOX);
	}

	protected ModuloTemplate(Modulo modulo, EModelChangeType modelChangeType) throws FWException {
		super(modulo);
		setIdModulo(modulo.getIdModulo());
		this.modelChangeType = modelChangeType;
		setModulosModel(createModulosModel());
		setModuloModelActivo(getModulosModel().get(0));
		interModuleMediator.addInterModuleListener(getInterModuleListener());
		construct();
//		addInternalFrameListener(new VentanaListener());
	}

	/**
	 * Devuelve el nro. de id del módulo.
	 * @return idModulo El nro. de id del módulo.
	 */
	public int getIdModulo() {
		return idModulo;
	}

	/**
	 * Setea el nro. de id del módulo.
	 * @param idModulo El nro. de id del módulo.
	 */
	protected void setIdModulo(int idModulo) {
		this.idModulo = idModulo;
	}

	private static Logger logger = Logger.getLogger(ModuloTemplate.class);

	/**
	 * Esta función actualiza los datos de la tabla. Para ello, le pide al
	 * ModuloModel activo que busque los datos en función de lo que haya en la
	 * cabecera
	 */
	protected void actualizar() {
		try {
			FWCursor.startWait(ModuloTemplate.this);
			List<T> items = getModuloModelActivo().buscarItems(getCabecera().getModel());
			getModuloModelActivo().setItems(items);
			habilitarAcciones();
			actualizarStatuses();
		} catch(RuntimeException e) {
			logger.error("Error inesperado actualizando la lista", e);
			BossError.gestionarError(e);
		} finally {
			FWCursor.endWait(ModuloTemplate.this);
		}
	}

	/**
	 * Esta función actualiza los datos de la tabla. Para ello, le pide al
	 * ModuloModel activo que busque los datos en función de lo que haya en la
	 * cabecera
	 * @param selectedItems Lista de elementos que deben quedar seleccionados
	 *            luego de la actualización. <code>null</code> efectua el
	 *            comportamiento default
	 */
	protected void actualizar(List<T> selectedItems) {
		try {
			FWCursor.startWait(ModuloTemplate.this);
			List<T> items = getModuloModelActivo().buscarItems(getCabecera().getModel());
			getModuloModelActivo().setItems(items,selectedItems);
			habilitarAcciones();
			actualizarStatuses();
			
		} finally {
			FWCursor.endWait(ModuloTemplate.this);
		}
	}

	/**
	 * Coloca todos los filtros en su valor por defecto
	 */
	public void resetFiltros() {
		getModuloModelActivo().getFiltros().resetFiltros();
	}

	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.GuiForm#botonSalirPresionado()
	 */
	public boolean botonSalirPresionado() {
			return true;
	}

	/**
	 * Esta función establece si las mismas están activas o no.<br>
	 * Función que es invocada cuando se agregan o quitan acciones del modelo.
	 */
	protected void changeAcciones() {
		try {
			FWCursor.startWait(ModuloTemplate.this);
			habilitarAcciones();
		} finally {
			FWCursor.endWait(ModuloTemplate.this);
		}
	}

	/**
	 * Función que es invocada cuando cambian los filtros (ya sea su valor o
	 * bien si se agregaron/quitaron filtros) del modelo.<br>
	 * 
	 */
	protected void changeFiltros() {
		try {
			FWCursor.startWait(ModuloTemplate.this);
			getModuloModelActivo().refreshData();
		} finally {
			FWCursor.endWait(ModuloTemplate.this);
		}
	}

	protected void changeTotales() {
		try {
			FWCursor.startWait(ModuloTemplate.this);
			getModuloModelActivo().getTotales().totalizar(getModuloModelActivo().getTabla().getItems());
		} finally {
			FWCursor.endWait(ModuloTemplate.this);
		}
	}

	protected void changeStatuses() {
		try {
			FWCursor.startWait(ModuloTemplate.this);
			actualizarStatuses();
		} finally {
			FWCursor.endWait(ModuloTemplate.this);
		}
	}

	/**
	 * Construye gráficamente la pantalla
	 */
	protected void construct() {
		setTitle(getModuloModelActivo().getTitulo());
		//Setea la propiedad 'Maximizable'
		setMaximizable(true);
		//Se remueve el boton para agregarlo en el lugar que corresponde
		getContentPane().remove(getBtnSalir());
		getContentPane().setLayout(new BorderLayout());
		//Se crean los componentes necesarios para el módulo
		setCabecera(createCabecera());
		//Cabecera
		getContentPane().add(getCabecera(), BorderLayout.NORTH);

		/*
		 * -------------------------------------------
		 * Se agrega el panel central (el de la tabla)
		 * -------------------------------------------
		 */
		getContentPane().add(constructCenterComponent(), BorderLayout.CENTER);
		getPieTabla().setLayout(new BorderLayout());
		getPieTabla().add(getGuiStatuses().getComponent(), BorderLayout.CENTER);

		/*
		 * --------------------------------------------------------
		 * Se agrega el panel inferior (Acciones, Filtros, Totales)
		 * --------------------------------------------------------
		 */
		JPanel pie = new JPanel(new BorderLayout());
		JPanel pieNorte = new JPanel(new BorderLayout());
		//Botones
		pieNorte.add(getPanBotones(), BorderLayout.WEST);
		//Acciones
		pieNorte.add(getGuiAcciones().getComponent(), BorderLayout.EAST);
		pie.add(pieNorte, BorderLayout.NORTH);
		//Filtros
		pie.add(getGuiFiltros().getComponent(), BorderLayout.CENTER);
		JPanel pieSur = new JPanel(new BorderLayout());
		pieSur.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 5));
		//Totales
		pieSur.add(getGuiTotales().getComponent(), BorderLayout.WEST);
		//Salir
		pieSur.add(getBtnSalir(), BorderLayout.EAST);
		pie.add(pieSur, BorderLayout.SOUTH);
		getContentPane().add(pie, BorderLayout.SOUTH);
		getCabecera().addObserver(new GuiPanelObserver() {
			public void update(GuiPanelObservable gui) {
				actualizar();
			}
		});
	}

	/**
	 * Esta método construye el panel cental (el de la tabla) y dependiendo de la
	 * cantidad de modelos y de la forma de cambiar el modelo acomoda los
	 * componentes de diferentes maneras.<p>
	 * @return Componente central que contiene la tabla
	 */
	protected JComponent constructCenterComponent() {
		final JPanel panelCentro = new JPanel(new BorderLayout());
		panelCentro.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
		// Encabezado de tabla
		panelCentro.add(getEncabezadoTabla(), BorderLayout.NORTH);
		// Pie de tabla
		panelCentro.add(getPieTabla(), BorderLayout.SOUTH);
		// Izquierda de la tabla
		panelCentro.add(getMargenIzqTabla(), BorderLayout.WEST);
		// Derecha de la tabla
		panelCentro.add(getMargenDerTabla(), BorderLayout.EAST);
		// Tabla
		panelCentro.add(getGuiTabla().getComponent(), BorderLayout.CENTER);
		// Componente a devolver
		JComponent centerComponent;
		// Si hay un solo modelo, devuelvo el panel central
		if(getModulosModel().size() <= 1) {
			centerComponent = panelCentro;
			// Si hay más de un modelo y la forma de cambiar es mediante un
			// combo
		} else if(modelChangeType == EModelChangeType.TYPE_COMBOBOX) {
			// Coloco el combo en el encabezado
			getEncabezadoTabla().add(getCmbModelos());
			// Agrego todos los modelos al combo
			for(ModuloModel moduloModel : getModulosModel()) {
				getCmbModelos().addItem(moduloModel);
			}
			getCmbModelos().setVisible(true);
			// Agrego al combo el listener de cambio
			getCmbModelos().addItemListener(new ItemListener() {
				@SuppressWarnings("unchecked")
				public void itemStateChanged(ItemEvent ie) {
					if(ie.getStateChange() == ItemEvent.SELECTED) {
						// Seteo el modelo
						setModuloModelActivo((ModuloModel<T, MC>)getCmbModelos().getSelectedItem());
						// Actualizo los datos desde la cabecera
						actualizar();
					}
				}
			});
			centerComponent = panelCentro;
			// Si es el tipo TAB
		} else {
			// Creo el tabbed pane
			// tabbedPane = new JTabbedPane();
			// Le agrego todos los tabs
			for(ModuloModel<T, MC> moduloModel : getModulosModel()) {
				getTabbedPane().addTab(moduloModel.toString(), moduloModel.getIcon(), new JPanel(new BorderLayout()));
			}
			// Coloco en el tab central el panel
			((JPanel)getTabbedPane().getSelectedComponent()).add(panelCentro, BorderLayout.CENTER);
			// Agrego el listener de cambio de modelo
			getTabbedPane().addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					if(getTabbedPane().getSelectedIndex() != -1) {
						// Coloco el panel al tab
						((JPanel)getTabbedPane().getSelectedComponent()).add(panelCentro, BorderLayout.CENTER);
						// Seteo el modelo
						setModuloModelActivo(getModulosModel().get(getTabbedPane().getSelectedIndex()));
						// Actualizo los datos desde la cabecera
						actualizar();
					}
				}
			});
			centerComponent = getTabbedPane();
		}
		return centerComponent;
	}

	/**
	 * Crea la cabecera que va a tener el módulo.
	 * @return Cabecera del módulo
	 */
	protected abstract Cabecera<MC> createCabecera();

	/**
	 * Crea los Modelos que va a utilizar esta pantalla y los devuelve
	 * @return Lista de modelos que va a utilizar la pantalla
	 * @throws FWException 
	 */
	protected abstract List<ModuloModel<T, MC>> createModulosModel() throws FWException;

	/*
	 * (non-Javadoc)
	 * javax.swing.JInternalFrame#dispose()
	 */
	public void dispose() {
		try {
			getTimerRefresco().stop();
			interModuleMediator.removeInterModuleListener(getInterModuleListener());
		} finally {
			super.dispose();
		}
	}

	/**
	 * Devuelve el listener que se utilizará para tomar las acciones pertinentes
	 * cuando se ejecute una acción
	 * 
	 * @return Listener de cuando se ejecuta una acción
	 */
	protected AccionExecutedListener getAccionExecutedListener() {
		if(accionExecutedListener == null) {
			accionExecutedListener = new AccionExecutedListener() {

				@SuppressWarnings("unchecked")
				public void accionExcecuted(AccionExecutedEvent e) {
					if(e.isNeedsRefreshTable()) {
						if(e.getSelectedObjects() != null) {
							actualizar((List<T>)e.getSelectedObjects());
						} else {
							actualizar();
						}
					}
				}
			};
		}
		return accionExecutedListener;
	}

	/**
	 * Retorna el panel de la cabecera.
	 * @return panCabecera El panel de la cabecera.
	 */
	public Cabecera<? extends MC> getCabecera() {
		return panCabecera;
	}

	/**
	 * Devuelve el combo utilizado para cambiar de modelos
	 * @return Combo utilizado para cambiar de modelos
	 */
	protected JComboBox getCmbModelos() {
		if(cmbModelos == null) {
			cmbModelos = new JComboBox();
		}
		return cmbModelos;
	}

	/**
	 * Devuelve el listener que se disparará cuando se selecciona una columna de
	 * la tabla. Este listener solo se utiliza si la tabla tiene modo de
	 * selección por celdas (ver {@link Tabla#setModoSeleccion(int)})
	 * @return Listener para la tabla
	 * Tabla#setModoSeleccion(int)
	 * Tabla#SELECCION_CELDA
	 */
	protected ColumnaSelectionListener getColumnaSelectionListener() {
		if(columnaSelectionListener == null) {
			columnaSelectionListener = new ColumnaSelectionListener();
		}
		return columnaSelectionListener;
	}

	/**
	 * Devuelve el panel que se posiciona por sobre la tabla<br>
	 * Si de desea agregarle a la misma un encabezado, se debe sobreescribir
	 * este método y devolver un panel con los componentes adecuados
	 * 
	 * @return Panel con el encabezado de la tabla
	 */
	protected JPanel getEncabezadoTabla() {
		if(encabezadoTabla == null) {
			encabezadoTabla = new JPanel();
		}
		return encabezadoTabla;
	}

	/**
	 * Devuelve el listener que se disparará cuando se selecciona una fila de la tabla
	 * @return Listener para la tabla
	 */
	protected FilaSelectionListener getFilaSelectionListener() {
		if(filaSelectionListener == null) {
			filaSelectionListener = new FilaSelectionListener();
		}
		return filaSelectionListener;
	}

	protected FilaSelectionDobleClick getFilaSelectionDobleClick() {
		if(mouseInputListener == null) {
			mouseInputListener = new FilaSelectionDobleClick();
		}
		return mouseInputListener;
	}

	/**
	 * Devuelve la GUI con las acciones
	 * @return GUI de acciones
	 */
	public final IGuiAcciones<T> getGuiAcciones() {
		if(guiAcciones == null) {
			guiAcciones = createGuiAcciones(getModuloModelActivo().getAcciones());
			guiAcciones.addAccionExecutedListener(getAccionExecutedListener());
		}
		return guiAcciones;
	}

	/**
	 * Crea la {@link IGuiAcciones} de acciones que se va a utilizar
	 * @param acciones Modelo de acciones
	 * @return Una instancia de {@link IGuiAcciones}
	 */
	protected IGuiAcciones<T> createGuiAcciones(Acciones<T> acciones) {
		return new GuiAcciones<T>(this, acciones);
	}

	/**
	 * Devuelve la GUI de los Filtros
	 * @return GUI de los filtros
	 */
	public final IGuiFiltros<T> getGuiFiltros() {
		if(guiFiltros == null) {
			guiFiltros = createGuiFiltros(getModuloModelActivo().getFiltros());
		}
		return guiFiltros;
	}

	/**
	 * Crea la {@link IGuiFiltros} de filtros que se va a utilizar
	 * @param filtros Modelo de filtros
	 * @return Una instancia de {@link IGuiFiltros}
	 */
	protected IGuiFiltros<T> createGuiFiltros(Filtros<T> filtros) {
		return new GuiFiltros<T>(this, filtros);
	}

	/**
	 * Devuelve la GUI de la tabla
	 * @return GUI con la tabla
	 */
	public final IGuiTabla<T> getGuiTabla() {
		if(guiTabla == null) {
			guiTabla = createGuiTabla(getModuloModelActivo().getTabla());
		}
		return guiTabla;
	}

	/**
	 * Crea la {@link IGuiTabla} que se va a utilizar
	 * @param tabla Modelo de la tabla
	 * @return Una instancia de {@link IGuiTabla}
	 */
	protected IGuiTabla<T> createGuiTabla(Tabla<T> tabla) {
		return new GuiTabla<T>(this, tabla);
	}

	/**
	 * Devuelve el panel con los totales que representan la cantidad de items
	 * con respecto a algún determinado criterio (ej. En el caso de órdenes de
	 * publicidad según su estado).
	 * @return panTotales El panel con los totales.
	 */
	public final IGuiTotales<T> getGuiTotales() {
		if(guiTotales == null) {
			guiTotales = createGuiTotales(getModuloModelActivo().getTotales());
		}
		return guiTotales;
	}

	/**
	 * Crea la {@link IGuiTotales} que se va a utilizar
	 * @param totales Modelo de totales
	 * @return Una instancia de {@link IGuiTotales}
	 */
	protected IGuiTotales<T> createGuiTotales(Totales<T> totales) {
		return new GuiTotales<T>(this, totales);
	}

	/**
	 * Devuelve la GUI para los status que muestran información adicional de los
	 * elementos seleccionados
	 * 
	 * @return GUI para los status
	 */
	public final IGuiStatuses<T> getGuiStatuses() {
		if(guiStatuses == null) {
			guiStatuses = createGuiStatuses(getModuloModelActivo().getStatuses());
		}
		return guiStatuses;
	}

	/**
	 * Crea la {@link IGuiStatuses} que se va a utilizar
	 * @param statuses Modelo de status
	 * @return Una instancia de {@link IGuiStatuses}
	 */
	protected IGuiStatuses<T> createGuiStatuses(Statuses<T> statuses) {
		return new GuiStatuses<T>(this, statuses);
	}

	/**
	 * Devuelve el panel que se posiciona a la derecha de la tabla<br>
	 * Si de desea agregarle a la misma un margen derecho, se debe sobreescribir
	 * este método y devolver un panel con los componentes adecuados
	 * @return Panel con el margen derecho de la tabla
	 */
	protected JPanel getMargenDerTabla() {
		if(margenDerTabla == null) {
			margenDerTabla = new JPanel();
			margenDerTabla.setVisible(false);
		}
		return margenDerTabla;
	}

	/**
	 * Devuelve el panel que se posiciona a la izquierda de la tabla<br>
	 * Si de desea agregarle a la misma un margen izquierdo, se debe sobreescribir
	 * este método y devolver un panel con los componentes adecuados
	 * @return Panel con el margen izquierdo de la tabla
	 */
	protected JPanel getMargenIzqTabla() {
		if(margenIzqTabla == null) {
			margenIzqTabla = new JPanel();
			margenIzqTabla.setVisible(false);
		}
		return margenIzqTabla;
	}

	/**
	 * Devuelve el listener que se utilizará para observar cuando cambia el modelo
	 * @return Listener para el modelo
	 */
	protected ModelChangeListener getModelChangeListener() {
		if(modelChangeListener == null) {
			modelChangeListener = new ModelChangeListener() {

				public void stateChanged(ModelChangeEvent e) {
					switch(e.getEventType()) {
						case ModuloModel.EVENT_TYPE_TITLE_CHANGE:
						ModuloTemplate.this.setTitle(getModuloModelActivo().getTitulo());
						break;
						case ModuloModel.EVENT_TYPE_ACCIONES_CHANGE:
						getGuiAcciones().setModel(getModuloModelActivo().getAcciones());
						break;
						case ModuloModel.EVENT_TYPE_FILTRO_CHANGE:
						getGuiFiltros().setModel(getModuloModelActivo().getFiltros());
						boolean hayFiltros = getModuloModelActivo().getFiltros().getElementCount() > 0;
						getGuiFiltros().getComponent().setVisible(hayFiltros);
						break;
						case ModuloModel.EVENT_TYPE_TOTALES_CHANGE:
						getGuiTotales().setModel(getModuloModelActivo().getTotales());
						break;
						case ModuloModel.EVENT_TYPE_STATUS_CHANGE:
						getGuiStatuses().setModel(getModuloModelActivo().getStatuses());
						boolean hayStatuses = getModuloModelActivo().getStatuses().getElementCount() > 0;
						getGuiStatuses().getComponent().setVisible(hayStatuses);
						break;
						case ModuloModel.EVENT_TYPE_TABLA_CHANGE:
						case ModuloModel.EVENT_TYPE_FULL_CHANGE:
						updateOtherModels();
						actualizar();
						break;
					}
				}
			};
		}
		return modelChangeListener;
	}

	/**
	 * Devuelve el modelo actual
	 * @return Modulo model actual
	 */
	public ModuloModel<T, MC> getModuloModelActivo() {
		return moduloModelActivo;
	}

	/**
	 * Devuelve todos los modelos que tiene disponibles este módulo
	 * @return Lista de modelos que tiene disponible el módulo
	 */
	public List<ModuloModel<T, MC>> getModulosModel() {
		return modulosModel;
	}

	/**
	 * Devuelve el observer que estará monitoreando las distintas GUI's
	 * @return Observer a utilizar con la {@link GuiAcciones}, {@link GuiFiltros} y {@link GuiTotales}
	 */
	private GuiPanelObserver getObserverGuiSet() {
		if(observerGuiSet == null) {
			observerGuiSet = new GuiPanelObserver() {

				public void update(final GuiPanelObservable gui) {
					if(gui == getGuiAcciones()) {
						changeAcciones();
					} else if(gui == getGuiFiltros()) {
						changeFiltros();
					} else if(gui == getGuiTotales()) {
						changeTotales();
					} else if(gui == getGuiStatuses()) {
						changeStatuses();
					}
				}
			};
		}
		return observerGuiSet;
	}

	/**
	 * Devuelve el panel que tiene los botones. Este panel se encuentra a la
	 * derecha del panel de las acciones.<br>
	 * Este panel NO es el que contiene las acciones.
	 * @return Panel de posibles botones adicionales
	 */
	protected JPanel getPanBotones() {
		if(panBotones == null) {
			panBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
		}
		return panBotones;
	}

	/**
	 * Devuelve el panel que se posiciona debajo de la tabla<br>
	 * Si de desea agregarle a la misma un pie, se debe sobreescribir
	 * este método y devolver un panel con los componentes adecuados
	 * @return Panel con el pie de la tabla
	 */
	protected JPanel getPieTabla() {
		if(pieTabla == null) {
			pieTabla = new JPanel(new FlowLayout(FlowLayout.LEFT));
		}
		return pieTabla;
	}

	/**
	 * Devuelve el tiempo en segundos de refresco.
	 * @return Tiempo en segundos de refresco. Si es <code><= 0</code> el
	 *         refresco se encuentra deshabilitado
	 */
	public int getTiempoRefresco() {
		return tiempoRefresco;
	}

	/**
	 * Inicializa el timer de refresco de las reservas.
	 * El valor 1000 representa a 1000 Milisegundos = 1 Segundo.
	 */
	protected Timer getTimerRefresco() {
		if(timerRefresco == null) {
			timerRefresco = new Timer(1000, new TimerActionListener());
			timerRefresco.setInitialDelay(getTiempoRefresco());
		}
		return timerRefresco;
	}

	/**
	 * Habilita/Deshabilita las acciones en función del estado actual del módulo
	 */
	protected void habilitarAcciones() {
		final AccionEvent<T> e = new AccionEvent<T>(this, 
				Collections.unmodifiableList(getGuiTabla().getObjetosSeleccionados()));
		getModuloModelActivo().getAcciones().habilitarAcciones(e);
	}

	//TODO Optimizar debe mejorse este esquema de obtencion de los seleccionados
	public void calcularFilasSeleccionadas(){
		getModuloModelActivo().getTotales().totalizarSeleccionadas(Collections.unmodifiableList(getGuiTabla().getObjetosSeleccionados()).size());
	}

	/**
	 * Actualiza la información de los statuses en función del estado actual del módulo
	 */
	protected void actualizarStatuses() {
		final AccionEvent<T> e = new AccionEvent<T>(this, 
				Collections.unmodifiableList(getGuiTabla().getObjetosSeleccionados()));
		getModuloModelActivo().getStatuses().update(e);
	}

	/**
	 * Setea el panel cabecera del módulo.
	 * @param cabecera El panel cabecera.
	 */
	protected final void setCabecera(Cabecera<? extends MC> cabecera) {
		this.panCabecera = cabecera;
	}

	/**
	 * Establece el modelo actual
	 * @param moduloModelActivo Modelo actual
	 */
	protected void setModuloModelActivo(ModuloModel<T, MC> moduloModelActivo) {
		if(this.moduloModelActivo != moduloModelActivo) {
			if(this.moduloModelActivo != null) {
				this.moduloModelActivo.removeModelChangeListener(getModelChangeListener());
			}
			this.moduloModelActivo = moduloModelActivo;
			this.moduloModelActivo.addModelChangeListener(getModelChangeListener());
			// Actualizo los modelos en las otras GUI
			updateOtherModels();
		}
	}

	/**
	 * Establece los modelos para este módulo
	 * @param modulosModel
	 *            Lista de modelos para el módulo
	 */
	private void setModulosModel(List<ModuloModel<T, MC>> modulosModel) {
		this.modulosModel = modulosModel;
	}

	/**
	 * Devuelve el tiempo en segundos de refresco.
	 * @param tiempoRefresco Tiempo en segundos de refresco. Si es
	 *            <code><= 0</code> el refresco se encuentra deshabilitado
	 */
	public void setTiempoRefresco(int tiempoRefresco) {
		this.tiempoRefresco = tiempoRefresco;
		if(tiempoRefresco <= 0 && getTimerRefresco().isRunning()) {
			getTimerRefresco().stop();
		} else {
			getTimerRefresco().start();
		}
	}

	/*
	 * (non-Javadoc)
	 * javax.swing.JComponent#setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if(visible) {
			getTimerRefresco().start();
		} else {
			getTimerRefresco().stop();
		}
	}

	/**
	 * Esta función se encarga de actualizar los modelos de
	 * <code>GuiAcciones</code>, <code>GuiTotales</code>,
	 * <code>GuiFiltros</code> y <code>GuiTabla</code>. Esto lo hace
	 * garantizando que no se disparen listeners innecesarios en el medio
	 */
	protected void updateOtherModels() {
		//Coloco el titulo al formulario
		setTitle(this.getModuloModelActivo().getTitulo());

		//Quito los observers
		getGuiAcciones().deleteObserver(getObserverGuiSet());
		getGuiTotales().deleteObserver(getObserverGuiSet());
		getGuiFiltros().deleteObserver(getObserverGuiSet());

		//Cambio los models
		getGuiAcciones().setModel(this.getModuloModelActivo().getAcciones());
		getGuiStatuses().setModel(this.getModuloModelActivo().getStatuses());
		getGuiTotales().setModel(this.getModuloModelActivo().getTotales());
		getGuiFiltros().setModel(this.getModuloModelActivo().getFiltros());
		getGuiTabla().setModel(this.getModuloModelActivo().getTabla());

		//Agrego los observers
		getGuiAcciones().addObserver(getObserverGuiSet());
		getGuiStatuses().addObserver(getObserverGuiSet());
		getGuiTotales().addObserver(getObserverGuiSet());
		getGuiFiltros().addObserver(getObserverGuiSet());

		//Se agregan los listener para la seleccion de celdas, filas o columnas
		getGuiTabla().getJTable().getColumnModel().removeColumnModelListener(getColumnaSelectionListener());
		getGuiTabla().getJTable().getSelectionModel().removeListSelectionListener(getFilaSelectionListener());
		if(getModuloModelActivo().getTabla().getModoSeleccion() == Tabla.SELECCION_CELDA) {
			getGuiTabla().getJTable().getColumnModel().addColumnModelListener(getColumnaSelectionListener());
			getGuiTabla().getJTable().getSelectionModel().addListSelectionListener(getFilaSelectionListener());
		} else {
			getGuiTabla().getJTable().getSelectionModel().addListSelectionListener(getFilaSelectionListener());
		}
		//Se agrega Listener para el dobleClick
		if(agregarFilaSelectionDobleClick()) {
			getGuiTabla().getJTable().addMouseListener(getFilaSelectionDobleClick());
		}
		//Actualizo la GUI de filtros 
		boolean hayFiltros = getModuloModelActivo().getFiltros().getElementCount()>0;
		getGuiFiltros().getComponent().setVisible(hayFiltros);

		//Actualizo la GUI de statuses
		boolean hayStatuses = getModuloModelActivo().getStatuses().getElementCount()>0;
		getGuiStatuses().getComponent().setVisible(hayStatuses);
	}

	private boolean agregarFilaSelectionDobleClick() {
		for(MouseListener mouseListener : getGuiTabla().getJTable().getMouseListeners()) {
			if(mouseListener.equals(getFilaSelectionDobleClick())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Listener de cuando cambia la selección de una columna
	 * 
	 */
	protected class ColumnaSelectionListener implements TableColumnModelListener {

		public void columnAdded(TableColumnModelEvent e) {
		}

		public void columnMarginChanged(ChangeEvent e) {
		}

		public void columnMoved(TableColumnModelEvent e) {
		}

		public void columnRemoved(TableColumnModelEvent e) {
		}

		public void columnSelectionChanged(ListSelectionEvent e) {
			if(!e.getValueIsAdjusting()) {
				calcularFilasSeleccionadas();
				habilitarAcciones();
				actualizarStatuses();
			}
		}
	}

	/**
	 * Listener de cuando se selecciona una fila de la tabla
	 * 
	 */
	protected class FilaSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if(!e.getValueIsAdjusting()) {
				calcularFilasSeleccionadas();
				habilitarAcciones();
				actualizarStatuses();
			}
		}
	}

	/**
	 * Listener para manejar el doble click 
	 */
	public class FilaSelectionDobleClick implements MouseInputListener {
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() ==2 ) {
				actualizarDobleClick();
			}
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseDragged(MouseEvent e) {}
		public void mouseMoved(MouseEvent e) {}
	}

	/**
	 * Listener del timer de refresco
	 * 
	 */
	protected class TimerActionListener implements ActionListener {
		private int t = 0;
		public void actionPerformed(ActionEvent e) {
			t++;
			if(t == getTiempoRefresco()) {
				getTimerRefresco().stop();
				actualizar();
				t = 0;
				getTimerRefresco().start();
			}
		}
	}

	/**
	 * Devuelve las clases para las cuales este módulo escuchará los eventos de
	 * otros módulos para actualizarse.
	 * <p>
	 * El módulo se actualizará si se satisface que
	 * <code>listenUpdateFor()[i].isAssignableFrom(claseDelEvento)</code> para
	 * algún <code>i</code><br>
	 * Donde <code>claseDelEvento</code> es la clase que otro módulo indicó
	 * para actualizar
	 * 
	 * @return Arreglo de clases para las que se va a actualizar el módulo
	 * #actualizarOtrosModulos(Class) Dispara el evento de actualización para otros módulos
	 */
	protected Class<?>[] listenUpdateFor() {
		return new Class<?>[0];
	}

	/**
	 * Dispara una actualización hacia otros módulos. Solo se actualizarán
	 * aquellos módulos que escuchen los eventos de <code>clazz</code> o se
	 * una superclase de esta
	 * @param clazz
	 * #listenUpdateFor() Clases que escucha este módulo
	 */
	public void actualizarOtrosModulos(Class<?> clazz) {
		interModuleMediator.fireInterModuleEvent(this, clazz);
	}

	private InterModuleListener getInterModuleListener() {
		if(interModuleListener == null) {
			final Class<?>[] clases = listenUpdateFor();
			interModuleListener = new InterModuleListener() {

				public boolean isAplicableFor(Class<?> clazz) {
					for(int i = 0; i < clases.length; i++) {
						if(clases[i].isAssignableFrom(clazz))
							return true;
					}
					return false;
				}

				public void actionPerformed(ActionEvent e) {
					if(e.getSource() != ModuloTemplate.this)
						actualizar();
				}
			};
		}
		return interModuleListener;
	}

	/**
	 * Constantes que especifican la forma de cambiar de modelo
	 * 
	 */
	public static enum EModelChangeType {
		/**
		 * Permite cambiar de modelo utilizando un ComboBox
		 */
		TYPE_COMBOBOX,
		/**
		 * Permite cambiar de modelo utilizando diferentes tabs
		 */
		TYPE_TAB;
	}

	/**
	 * Actualiza la información  en función del estado actual del módulo
	 */
	protected void actualizarDobleClick() {
		final AccionEvent<T> e = new AccionEvent<T>(this, 
				Collections.unmodifiableList(getGuiTabla().getObjetosSeleccionados()));
		getModuloModelActivo().getAccionesAdicionales().update(e); 
	}

	/**
	 * Devuelve el JTabbedPane utilizado para cambiar de modelos
	 * @return JTabbedPane utilizado para cambiar de modelos
	 */
	protected JTabbedPane getTabbedPane() {
		if(tabbedPane == null) {
			tabbedPane = new JTabbedPane();
		}
		return tabbedPane;
	}
}
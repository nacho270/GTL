package ar.com.fwcommon.templates.modulo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Icon;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Acciones;
import ar.com.fwcommon.templates.modulo.model.acciones.IBuilderAcciones;
import ar.com.fwcommon.templates.modulo.model.accionesmouse.AccionesAdicionales;
import ar.com.fwcommon.templates.modulo.model.accionesmouse.IBuilderAccionAdicional;
import ar.com.fwcommon.templates.modulo.model.filtros.Filtros;
import ar.com.fwcommon.templates.modulo.model.filtros.IBuilderFiltros;
import ar.com.fwcommon.templates.modulo.model.listeners.ModelChangeEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.ModelChangeListener;
import ar.com.fwcommon.templates.modulo.model.status.IBuilderStatuses;
import ar.com.fwcommon.templates.modulo.model.status.Statuses;
import ar.com.fwcommon.templates.modulo.model.tabla.IBuilderTabla;
import ar.com.fwcommon.templates.modulo.model.tabla.Tabla;
import ar.com.fwcommon.templates.modulo.model.totales.IBuilderTotales;
import ar.com.fwcommon.templates.modulo.model.totales.Totales;

/**
 * Modelo del ModuloTemplate.
 * 
 *
 * @param <T> Elemento con el que se llena la tabla
 * @param <MC> Modelo de la cabecera utilizada por el módulo
 */
public abstract class ModuloModel<T, MC> {
	/**
	 * Si cambió el título o subtitulo del modelo
	 */
	public static final int EVENT_TYPE_TITLE_CHANGE = 0;
	/**
	 * Si cambió el modelo de los totales
	 */
	public static final int EVENT_TYPE_TOTALES_CHANGE = 1;
	/**
	 * Si cambió el modelo de las acciones
	 */
	public static final int EVENT_TYPE_ACCIONES_CHANGE = 2;
	/**
	 * Si cambió el modelo de la tabla
	 */
	public static final int EVENT_TYPE_TABLA_CHANGE = 3;
	/**
	 * Si cambió el modelo del filtro
	 */
	public static final int EVENT_TYPE_FILTRO_CHANGE = 4;
	/**
	 * Si cambió el modelo del status
	 */
	public static final int EVENT_TYPE_STATUS_CHANGE = 5;
	/**
	 * Si el cambio es muy grande como para ser descripto con los eventos
	 * anteriores
	 */
	public static final int EVENT_TYPE_FULL_CHANGE = 6;
	
	private EventListenerList listeners = new EventListenerList();
	
	private int idModel;
	private List<T> items;	
	private Totales<T> totales;
	private Tabla<T> tabla;
	private Acciones<T> acciones;
	private Filtros<T> filtros;
	private Statuses<T> statuses;
	private String titulo;
	private String subtitulo;
	private Icon icon;
	private AccionesAdicionales<T> accionesAdicionales;
	private String nombrePropiedadConfig;
	
	public ModuloModel() {
		super();
	}

	public ModuloModel(int idModel) {
		super();
		setIdModel(idModel);
	}
	
	/**
	 * Construye un modelo utilizando Builders para las partes más importantes
	 * del mismo. El modelo no tiene status
	 * 
	 * @param idModel Identificador del modelo
	 * @param builderAcciones Objeto encargado de construir las acciones
	 * @param builderFiltros Objeto encargado de construir los filtros
	 * @param builderTabla Objeto encargado de construir la tabla
	 * @param builderTotales Objeto encargado de construir los totales
	 * @throws FWException 
	 */
	public ModuloModel(int idModel, IBuilderAcciones<T> builderAcciones, IBuilderFiltros<T> builderFiltros,
			IBuilderTabla<T> builderTabla, IBuilderTotales<T> builderTotales) throws FWException {
		super();
		setIdModel(idModel);
		setAcciones(builderAcciones.construirAcciones(idModel));
		setFiltros(builderFiltros.construirFiltros(idModel));
		setTabla(builderTabla.construirTabla(idModel));
		setTotales(builderTotales.construirTotales(idModel));
	}
	/**
	 * Construye un modelo utilizando Builders para las partes más importantes
	 * del mismo. El modelo no tiene status
	 * 
	 * @param idModel Identificador del modelo
	 * @param builderAcciones Objeto encargado de construir las acciones
	 * @param builderFiltros Objeto encargado de construir los filtros
	 * @param builderTabla Objeto encargado de construir la tabla
	 * @throws FWException 
	 */
	public ModuloModel(int idModel, IBuilderAcciones<T> builderAcciones, IBuilderFiltros<T> builderFiltros,
			IBuilderTabla<T> builderTabla) throws FWException {
		super();
		setIdModel(idModel);
		setAcciones(builderAcciones.construirAcciones(idModel));
		setFiltros(builderFiltros.construirFiltros(idModel));
		setTabla(builderTabla.construirTabla(idModel));
		
	}
	/**
	 * Construye un modelo utilizando Builders para las partes más importantes
	 * del mismo. El modelo no tiene status
	 * 
	 * @param idModel Identificador del modelo
	 * @param builderAcciones Objeto encargado de construir las acciones
	 * @param builderFiltros Objeto encargado de construir los filtros
	 * @param builderTabla Objeto encargado de construir la tabla
	 * @param builderAdicionales Objeto encargado de construir las acciones adicional
	 * @throws FWException 
	 */
	public ModuloModel(int idModel, IBuilderAcciones<T> builderAcciones, IBuilderFiltros<T> builderFiltros,
			IBuilderTabla<T> builderTabla, IBuilderAccionAdicional<T> builderAdicionales) throws FWException {
		super();
		setIdModel(idModel);
		setAcciones(builderAcciones.construirAcciones(idModel));
		setFiltros(builderFiltros.construirFiltros(idModel));
		setTabla(builderTabla.construirTabla(idModel));
		setAccionesAdiconales(builderAdicionales.construirAccionAdicional(idModel));
		
	}
	/**
	 * Construye un modelo utilizando Builders para las partes más importantes
	 * del mismo
	 * 
	 * @param idModel Identificador del modelo
	 * @param builderAcciones Objeto encargado de construir las acciones
	 * @param builderFiltros Objeto encargado de construir los filtros
	 * @param builderTabla Objeto encargado de construir la tabla
	 * @param builderTotales Objeto encargado de construir los totales
	 * @param builderStatuses Objeto encargado de contruir los statuses
	 * @throws FWException 
	 */
	public ModuloModel(int idModel, IBuilderAcciones<T> builderAcciones, IBuilderFiltros<T> builderFiltros,
			IBuilderTabla<T> builderTabla, IBuilderTotales<T> builderTotales, IBuilderStatuses<T> builderStatuses) throws FWException {
		super();
		setIdModel(idModel);
		setAcciones(builderAcciones.construirAcciones(idModel));
		setFiltros(builderFiltros.construirFiltros(idModel));
		setTabla(builderTabla.construirTabla(idModel));
		setTotales(builderTotales.construirTotales(idModel));
		setStatuses(builderStatuses.construirStatuses(idModel));
	}
	
	public ModuloModel(int idModel, IBuilderAcciones<T> builderAcciones, IBuilderFiltros<T> builderFiltros,
			IBuilderTabla<T> builderTabla, IBuilderTotales<T> builderTotales, IBuilderStatuses<T> builderStatuses, IBuilderAccionAdicional<T> builderAdicionales) throws FWException {
		super();
		setIdModel(idModel);
		setAcciones(builderAcciones.construirAcciones(idModel));
		setFiltros(builderFiltros.construirFiltros(idModel));
		setTabla(builderTabla.construirTabla(idModel));
		setTotales(builderTotales.construirTotales(idModel));
		setStatuses(builderStatuses.construirStatuses(idModel));
		setAccionesAdiconales(builderAdicionales.construirAccionAdicional(idModel));
	}
	
	public ModuloModel(int idModel, IBuilderAcciones<T> builderAcciones, IBuilderFiltros<T> builderFiltros,
			IBuilderTabla<T> builderTabla, IBuilderTotales<T> builderTotales, IBuilderAccionAdicional<T> builderAdicionales) throws FWException {
		super();
		setIdModel(idModel);
		setAcciones(builderAcciones.construirAcciones(idModel));
		setFiltros(builderFiltros.construirFiltros(idModel));
		setTabla(builderTabla.construirTabla(idModel));
		setTotales(builderTotales.construirTotales(idModel));
		setAccionesAdiconales(builderAdicionales.construirAccionAdicional(idModel));
	}

	public ModuloModel(int idModel, IBuilderAcciones<T> builderAcciones, IBuilderFiltros<T> builderFiltros,
			IBuilderTabla<T> builderTabla, IBuilderStatuses<T> builderStatuses) throws FWException {
		super();
		setIdModel(idModel);
		setAcciones(builderAcciones.construirAcciones(idModel));
		setFiltros(builderFiltros.construirFiltros(idModel));
		setTabla(builderTabla.construirTabla(idModel));
		setStatuses(builderStatuses.construirStatuses(idModel));
	}
	
	/**
	 * Devuelve el identificador del modelo<br>
	 * Este identificador no es utilizado para la lógica, pero tiene por
	 * objetivo permitir persistencia (del módulo, como de sus componentes) en
	 * frameworks inferiores
	 * 
	 * @return Identificador del modelo
	 */
	public int getIdModel() {
		return idModel;
	}

	/**
	 * Establece el identificador del modelo<br>
	 * Este identificador no es utilizado para la lógica, pero tiene por
	 * objetivo permitir persistencia (del módulo, como de sus componentes) en
	 * frameworks inferiores
	 * 
	 * @param idModel Identificador del modelo
	 */
	public void setIdModel(int idModel) {
		this.idModel = idModel;
	}

	/**
	 * Devuelve el modelo de las acciones
	 * @return Modelo de las acciones
	 */
	public Acciones<T> getAcciones() {
		if (acciones == null) {
			acciones = new Acciones<T>();
		}
		return acciones;
	}

	/**
	 * Establece el modelo de las acciones
	 * @param acciones Modelo de las acciones
	 */
	public void setAcciones(Acciones<T> acciones) {
		if (this.acciones != acciones) {
			this.acciones = acciones;
			fireModelChangeListener(EVENT_TYPE_ACCIONES_CHANGE);
		}
	}
	
	/**
	 * Establece el modelo de Acciones adicionales
	 * @param accionesAdicionales Modelo de Acciones Adicionales
	 */
	public void setAccionesAdiconales(AccionesAdicionales<T> accionesAdicinales) {
		if (this.accionesAdicionales != accionesAdicinales ) {
			this.accionesAdicionales = accionesAdicinales;
			fireModelChangeListener(EVENT_TYPE_ACCIONES_CHANGE);
		}
	}
	public AccionesAdicionales<T> getAccionesAdicionales() {
		if (accionesAdicionales == null) {
			accionesAdicionales = new AccionesAdicionales<T>();
		}
		return accionesAdicionales;
	}
	
	/**
	 * Devuelve el modelo de los totales
	 * @return Modelo de los totales
	 */
	public Totales<T> getTotales() {
		if (totales == null) {
			totales = new Totales<T>();
		}
		return totales;
	}

	/**
	 * Establece el modelo de los totales
	 * @param totales Modelo de los totales
	 */
	public void setTotales(Totales<T> totales) {
		if (this.totales != totales) {
			this.totales = totales;
			fireModelChangeListener(EVENT_TYPE_TOTALES_CHANGE);
		}
	}

	/**
	 * Devuelve el modelo de la tabla
	 * @return Modelo de la tabla
	 */
	public Tabla<T> getTabla() {
		if (tabla == null) {
			tabla = new Tabla<T>();
		}
		return tabla;
	}

	/**
	 * Establece el modelo de la tabla
	 * @param tabla Modelo de la tabla
	 */
	public void setTabla(Tabla<T> tabla) {
		if (this.tabla != tabla) {
			this.tabla = tabla;
			fireModelChangeListener(EVENT_TYPE_TABLA_CHANGE);
		}
	}

	/**
	 * Devuelve el modelo de los filtros
	 * @return Modelo de los filtros
	 */
	public Filtros<T> getFiltros() {
		if (filtros == null) {
			filtros = new Filtros<T>();
		}
		return filtros;
	}

	/**
	 * Establece el modelo de los filtros
	 * @param filtros Modelo de los filtros
	 */
	public void setFiltros(Filtros<T> filtros) {
		if (this.filtros != filtros) {
			this.filtros = filtros;
			fireModelChangeListener(EVENT_TYPE_FILTRO_CHANGE);
		}
	}

	/**
	 * Devuelve el modelo de los status
	 * @return Modelo de los status
	 */
	public Statuses<T> getStatuses() {
		if (statuses == null) {
			statuses = new Statuses<T>();
		}
		return statuses;
	}
	
	/**
	 * Establece el modelo de los status
	 * @param statuses Modelo de los status
	 */
	public void setStatuses(Statuses<T> statuses) {
		if (this.statuses != statuses) {
			this.statuses = statuses;
			fireModelChangeListener(EVENT_TYPE_STATUS_CHANGE);
		}
	}
	
	/**
	 * Devuelve el titulo del módulo
	 * @return Título del módulo
	 */
	public String getTitulo() {
		return titulo;
	}
	
	/**
	 * Establece el título de módulo
	 * @param titulo Título de módulo
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
		fireModelChangeListener(EVENT_TYPE_TITLE_CHANGE);
	}
	
	/**
	 * Devuelve el subtítulo del módulo
	 * @return Subtítulo del módulo
	 */
	public String getSubtitulo() {
		if (subtitulo == null) {
			subtitulo = "";
		}
		return subtitulo;
	}
	
	/**
	 * Establece el subtítulo del módulo
	 * @param subtitulo Subtítulo del módulo
	 */
	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
		fireModelChangeListener(EVENT_TYPE_TITLE_CHANGE);
	}
	
	/**
	 * Devuelve el ícono del model
	 * @return Ícono del model
	 */
	public Icon getIcon() {
		return icon;
	}

	/**
	 * Establece el ícono del model
	 * @param icon Ícono del model
	 */
	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	/**
	 * Devuelve la lista de items del modelo
	 * 
	 * @return Lista de items del modelo
	 */
	public List<T> getItems() {
		return items;
	}

	/**
	 * Establece la lista de ítems del modelo
	 * 
	 * @param items Lista de ítems del modelo
	 */ 
	public void setItems(List<T> items) {
		if (this.items != items) {
			this.items = Collections.unmodifiableList(new ArrayList<T>(items));
		}
		refreshData();
	}
	
	/**
	 * Establece la lista de items del modelo
	 * 
	 * @param items Lista de ítems del modelo
	 * @param selectedItems Elementos que deben quedar seleccionados.
	 *            <code>null</code> efectua el comportamiento default
	 */
	public void setItems(List<T> items, List<T> selectedItems) {
		this.items = Collections.unmodifiableList(new ArrayList<T>(items));
		refreshData(selectedItems);
	}
	
	private static Logger logger = Logger.getLogger(ModuloModel.class);
	
	/**
	 * Reprocesa los ítems, pasándolos nuevamente por los filtros, totales y
	 * colocándolos nuevamente el la tabla. <br>
	 * Esta función NO llama a {@link #buscarItems(Object)} sino que utiliza los
	 * elementos {@link #getItems()}
	 */
	public void refreshData() {
		List<T> filteredItems;
		try {
			filteredItems = getFiltros().filtrar(getItems());
			getTotales().totalizar(filteredItems);
			getTabla().setItems(filteredItems);		
		} catch(RuntimeException e) {
			logger.error("Error en el refresh data (filtros/totales/tabla)");
			throw e;
		}

	}
	
	/**
	 * Reprocesa los ítems, pasándolos nuevamente por los filtros, totales y
	 * colocándolos nuevamente el la tabla. <br>
	 * Esta función NO llama a {@link #buscarItems(Object)} sino que utiliza los
	 * elementos {@link #getItems()}
	 * 
	 * @param selectedItems Elementos que deben quedar seleccionados.
	 *            <code>null</code> efectua el comportamiento default
	 */
	public void refreshData(List<T> selectedItems) {
		List<T> filteredItems = getFiltros().filtrar(getItems());
		getTotales().totalizar(filteredItems);
		getTabla().setItems(filteredItems, selectedItems);
	}
	
	/**
	 * Obtiene los datos que serán volcados en la tabla en función de lo
	 * seleccionado en la cabecera
	 * 
	 * @param modeloCabecera Modelo de la cabecera
	 * @return Lista de elementos a cargar en la tabla
	 */
	public abstract List<T> buscarItems(MC modeloCabecera);

	/**
	 * Agrega un listener que notifica cuando cambió el modelo
	 * @param listener Listener a agregar
	 */
	public void addModelChangeListener(ModelChangeListener listener) {
		listeners.add(ModelChangeListener.class, listener);
	}
	
	/**
	 * Quita un listener que notifica cuando cambió el modelo
	 * @param listener Listener a quitar
	 */
	public void removeModelChangeListener(ModelChangeListener listener) {
		listeners.remove(ModelChangeListener.class, listener);
	}
	
	/**
	 * Avisa que cambió el modelo.<br>
	 * Recibe el tipo de evento a disparar (ver
	 * {@link ModelChangeEvent#getEventType()}
	 * 
	 * @param eventType Tipo de evento.
	 */
	protected void fireModelChangeListener(int eventType) {
		final ModelChangeListener[] l = listeners.getListeners(ModelChangeListener.class);
		final ModelChangeEvent e = new ModelChangeEvent(this, eventType);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for (int i = 0; i < l.length; i++) {
					l[i].stateChanged(e);
				}
			}			
		});
	}
	
	/* (non-Javadoc)
	 * java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getTitulo();
	}

	/**
	 * 
	 * @return nombre Propiedad a salvar
	 */
	public String getNombrePropiedad() {
		return nombrePropiedadConfig;
	}

	/**
	 * Setea el nombre de la propiedad  a ser salvada cuando se graban las columnas
	 * <b>IMPORTANTE</b>: debe coincidir con el nombre del modulo en el portal
	 * @param nombrePropiedad
	 */
	public void setNombrePropiedad(String nombrePropiedad) {
		this.nombrePropiedadConfig = nombrePropiedad;
	}
	
	
}

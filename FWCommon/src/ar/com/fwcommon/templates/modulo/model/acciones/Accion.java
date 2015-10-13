package ar.com.fwcommon.templates.modulo.model.acciones;

import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.model.filtros.Filtro;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.ModelChangeEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.ModelChangeListener;
import ar.com.fwcommon.templates.modulo.model.meta.GroupModel;
import ar.com.fwcommon.templates.modulo.model.meta.Model;
import ar.com.fwcommon.templates.modulo.model.meta.SingleModel;

/**
 * Clase que representa a las acciones que se pueden realizar en los distintos sistemas.
 */
public abstract class Accion<T> {

	/**
	 * Indica que cambió el valor del filtro
	 */
	public static final int EVENT_TYPE_ENABLED_CHANGE = 0;
	/**
	 * Indica que cambió el nombre del filtro
	 */
	public static final int EVENT_TYPE_NAME_CHANGE = 1;
	/**
	 * Indica que cambió el nombre del filtro
	 */
	public static final int EVENT_TYPE_DESCRIPCION_CHANGE = 2;
	/**
	 * Indica que cambió el tooltip del filtro
	 */
	public static final int EVENT_TYPE_TOOLTIP_CHANGE = 3;
	/**
	 * Indica que cambió el color del filtro
	 */
	public static final int EVENT_TYPE_IMAGE_CHANGE = 4;
	/**
	 * Indica que cambió la indicación de si muestra o no el nombre
	 */
	public static final int EVENT_TYPE_SHOW_NAME_CHANGE = 5;

	private EventListenerList listeners = new EventListenerList();

	private int idAccion;
	private String nombre = "";
	private String descripcion = "";
	private String tooltip = null;
	private String imagenActivo = "ar/com/fwcommon/imagenes/b_blank.png";
	private String imagenInactivo = "ar/com/fwcommon/imagenes/b_blank_des.png";
	private String imagenGrandeActivo = null;
	private String imagenGrandeInactivo = null;
	private boolean independienteSeleccion = false;
	private boolean valida;
	private boolean muestraNombre = false;
	private String titulo;
	private String subtitulo;
	private String filtros;
	private Integer orden;

	public Accion() {
		super();
	}

	public Accion(int idAccion) {
		super();
		setIdAccion(idAccion);
	}

	/**
	 * Devuelve el id de la acción. Este identificador no es utilizado por el template, pero se brinda a fin de
	 * poder utilizarse para persistencia
	 * 
	 * @return El id de la acción.
	 */
	public int getIdAccion() {
		return idAccion;
	}

	/**
	 * Setea el id de la acción. Este identificador no es utilizado por el template, pero se brinda a fin de poder
	 * utilizarse para persistencia
	 * 
	 * @param idAccion El id de la acción.
	 */
	public void setIdAccion(int idAccion) {
		this.idAccion = idAccion;
	}

	/**
	 * Devuelve el nombre de la acción
	 * 
	 * @return Nombre de la acción
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Establece el nombre de la acción
	 * 
	 * @param nombre Nombre de la acción
	 */
	public void setNombre(String nombre) {
		if(this.nombre != nombre) {
			this.nombre = nombre;
			fireModelChangeListener(EVENT_TYPE_NAME_CHANGE);
		}
	}

	/**
	 * Devuelve la descripción de la acción.
	 * 
	 * @return La descripción de la acción.
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Setea la descripción de la acción.
	 * 
	 * @param descripcion La descripción de la acción.
	 */
	public void setDescripcion(String descripcion) {
		if(this.descripcion != descripcion) {
			this.descripcion = descripcion;
			fireModelChangeListener(EVENT_TYPE_DESCRIPCION_CHANGE);
		}
	}

	/**
	 * Devuelve si la acción puede o no ser ejecutada sobre el elemento ejecutable recibido como parámetro.
	 * 
	 * @param e Evento que contiene los elementos seleccionados y el {@link ModuloTemplate} que origina el evento
	 * @return <b>true</b> si la acción puede ser ejecutada sobre dichos elementos
	 */
	public abstract boolean esValida(AccionEvent<T> e);

	/**
	 * Ejecuta la acción
	 * 
	 * @param e Evento que contiene los elementos seleccionados y el {@link ModuloTemplate} que origina el evento.
	 *            Notar que en caso que se modifique la lista de elementos seleccionados se modificará la selección
	 *            actual
	 * @return <code>true</code> si se debe refrescar la tabla. <code>false</code> en caso contrario
	 * @throws FWException
	 */
	public abstract boolean ejecutar(AccionEvent<T> e) throws FWException;

	/**
	 * Dice si los métodos {@link #esValida(List)} y {@link #ejecutar(List)} deben recibir los elementos
	 * seleccionados o todos los elementos de la tabla
	 * 
	 * @return <code>false</code> si deben recibir solo los elementos seleccionados. <code>true</code> si deben
	 *         recibir todos los elementos de la tabla
	 */
	public boolean isIndependienteSeleccion() {
		return independienteSeleccion;
	}

	/**
	 * Establece si los métodos {@link #esValida(List)} y {@link #ejecutar(List)} deben recibir los elementos
	 * seleccionados o todos los elementos de la tabla
	 * 
	 * @param independienteSeleccion <code>false</code> si deben recibir solo los elementos seleccionados.
	 *            <code>true</code> si deben recibir todos los elementos de la tabla
	 */
	public void setIndependienteSeleccion(boolean independienteSeleccion) {
		this.independienteSeleccion = independienteSeleccion;
	}

	/**
	 * Devuelve la imagen que aparece en ActionButton correspondiente a la acción cuando este se encuentra
	 * habilitado.
	 * 
	 * @return El path de la imagen.
	 */
	public String getImagenActivo() {
		return imagenActivo;
	}

	/**
	 * Setea la imagen que aparece en ActionButton correspondiente a la acción cuando este se encuentra habilitado.
	 * 
	 * @param imagenActivo El path de la imagen a setear.
	 */
	public void setImagenActivo(String imagenActivo) {
		if(this.imagenActivo != imagenActivo) {
			this.imagenActivo = imagenActivo;
			fireModelChangeListener(EVENT_TYPE_IMAGE_CHANGE);
		}
	}

	/**
	 * Devuelve la imagen que aparece en ActionButton correspondiente a la acción cuando este se encuentra
	 * deshabilitado.
	 * 
	 * @return Returns El path de la imagen.
	 */
	public String getImagenInactivo() {
		return imagenInactivo;
	}

	/**
	 * Setea la imagen que aparece en ActionButton correspondiente a la acción cuando este se encuentra
	 * deshabilitado.
	 * 
	 * @param imagenInactivo El path de la imagen a setear.
	 */
	public void setImagenInactivo(String imagenInactivo) {
		if(this.imagenInactivo != imagenInactivo) {
			this.imagenInactivo = imagenInactivo;
			fireModelChangeListener(EVENT_TYPE_IMAGE_CHANGE);
		}
	}

	/**
	 * Devuelve la imagen que aparece en ActionButton correspondiente a la acción cuando este se encuentra
	 * habilitado y la fuente se encuentra en tamaño grande
	 * 
	 * @return El path de la imagen. <code>null</code> si se utiliza la misma que {@link Accion#getImagenActivo()}
	 */
	public String getImagenGrandeActivo() {
		return imagenGrandeActivo;
	}

	/**
	 * Setea la imagen que aparece en ActionButton correspondiente a la acción cuando este se encuentra habilitado
	 * y la fuente se encuentra en tamaño grande
	 * 
	 * @param imagenActivo El path de la imagen a setear. <code>null</code> si se utiliza la misma que
	 *            {@link Accion#getImagenActivo()}
	 */
	public void setImagenGrandeActivo(String imagenGrandeActivo) {
		if(this.imagenGrandeActivo != imagenGrandeActivo) {
			this.imagenGrandeActivo = imagenGrandeActivo;
			fireModelChangeListener(EVENT_TYPE_IMAGE_CHANGE);
		}
	}

	/**
	 * Devuelve la imagen que aparece en ActionButton correspondiente a la acción cuando este se encuentra
	 * deshabilitado y la fuente se encuentra en tamaño grande
	 * 
	 * @return Returns El path de la imagen. <code>null</code> si se utiliza la misma que
	 *         {@link Accion#getImagenInactivo()}
	 */
	public String getImagenGrandeInactivo() {
		return imagenGrandeInactivo;
	}

	/**
	 * Setea la imagen que aparece en ActionButton correspondiente a la acción cuando este se encuentra
	 * deshabilitado y la fuente se encuentra en tamaño grande
	 * 
	 * @param imagenInactivo El path de la imagen a setear. <code>null</code> si se utiliza la misma que
	 *            {@link Accion#getImagenInactivo()}
	 */
	public void setImagenGrandeInactivo(String imagenGrandeInactivo) {
		if(this.imagenGrandeInactivo != imagenGrandeInactivo) {
			this.imagenGrandeInactivo = imagenGrandeInactivo;
			fireModelChangeListener(EVENT_TYPE_IMAGE_CHANGE);
		}
	}

	/**
	 * Retorna el tooltip del ActionButton correspondiente a la acción.
	 * 
	 * @return El tooltip de la acción.
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * Setea el tooltip del ActionButton correspondiente a la acción
	 * <p>
	 * El tooltip debiera setearse de la siguiente manera:
	 * 
	 * <pre>
	 * String nota = &quot;Nota&quot;; //Opcional. Puede ser null 
	 * setToolTip(crearToolTip(getNombre(), getDescripcion(), nota));
	 * </pre>
	 * 
	 * @param tooltip El tooltip a setear.
	 * #getNombre()
	 * #getDescripcion()
	 * #crearTooltip(String, String, String)
	 */
	public void setTooltip(String tooltip) {
		if(this.tooltip != tooltip) {
			this.tooltip = tooltip;
			fireModelChangeListener(EVENT_TYPE_TOOLTIP_CHANGE);
		}
	}

	public void setTooltipNombreDescripcion(String tooltip) {
		setTooltip(crearTooltip(getNombre(), getDescripcion(), tooltip));
	}

	/**
	 * Crea un tooltip con formato HTML.
	 * 
	 * @param titulo El título del tooltip.
	 * @param descripcion La descripción del tooltip.
	 * @param nota La nota del tooltip.
	 * @return El tooltip con formato HTML.
	 */
	public static String crearTooltip(String titulo, String descripcion, String nota) {
		StringBuilder tooltip = new StringBuilder(40 + titulo.length() + descripcion.length() + (nota != null ? nota.length() : 0));
		tooltip.append("<html><b>").append(titulo).append("</b><br>").append(descripcion);
		if(nota != null) {
			tooltip.append("<br><i>").append(nota).append("</i>");
		}
		return tooltip.append("</html>").toString();
	}

	/**
	 * Devuelve si debe mostrarse el nombre de la acción en pantalla
	 * 
	 * @return <code>true</code> si se muestra el nombre. <code>false</code> en caso contrario.
	 */
	public boolean isMuestraNombre() {
		return muestraNombre;
	}

	/**
	 * Establece si debe mostrarse el nombre de la acción en pantalla
	 * 
	 * @param muestraNombre <code>true</code> si se muestra el nombre. <code>false</code> en caso contrario.
	 */
	public void setMuestraNombre(boolean muestraNombre) {
		if(this.muestraNombre != muestraNombre) {
			this.muestraNombre = muestraNombre;
			fireModelChangeListener(EVENT_TYPE_SHOW_NAME_CHANGE);
		}
	}

	/**
	 * Dice si la acción está habilitada o no
	 * 
	 * @return <code>true</code> si la acción está habilitada. <code>false</code> en caso contrario
	 */
	public boolean isValida() {
		return valida;
	}

	/**
	 * Establece si la acción está habilitada o no
	 * 
	 * @param valida <code>true</code> si la acción está habilitada. <code>false</code> en caso contrario
	 */
	public void setValida(boolean valida) {
		if(this.valida != valida) {
			this.valida = valida;
			fireModelChangeListener(EVENT_TYPE_ENABLED_CHANGE);
		}
	}

	/**
	 * Agrega un listener que notifica cuando cambio la acción
	 * 
	 * @param listener Listener a agregar
	 */
	public void addModelChangeListener(ModelChangeListener listener) {
		listeners.add(ModelChangeListener.class, listener);
	}

	/**
	 * Quita un listener que notifica cuando cambio la acción
	 * 
	 * @param listener Listener a quitar
	 */
	public void removeModelChangeListener(ModelChangeListener listener) {
		listeners.remove(ModelChangeListener.class, listener);
	}

	/**
	 * Avisa que la acción cambió
	 */
	protected void fireModelChangeListener(final int eventType) {
		final ModelChangeEvent e = new ModelChangeEvent(this, eventType);
		final ModelChangeListener[] l = listeners.getListeners(ModelChangeListener.class);
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				for(int i = 0; i < l.length; i++) {
					l[i].stateChanged(e);
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * java.lang.Object#toString()
	 */
	public String toString() {
		return descripcion;
	}

	/*
	 * (non-Javadoc)
	 * java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if(o instanceof Accion) {
			Accion<?> accion = (Accion<?>)o;
			if(idAccion == accion.getIdAccion() && descripcion.equals(accion.getDescripcion())) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return (idAccion << 3) ^ (idAccion >> 1) + ((getDescripcion() == null) ? 0 : getDescripcion().hashCode());
	}

	/**
	 * Setea en el Modelo el string con los filtros seteados para que se visualizen en la impresión o exportación a
	 * Excel de los datos
	 * 
	 * @param e
	 */
	protected void makeFilterToPrinter(AccionEvent<T> e) {
		StringBuilder filtrosBuilder = new StringBuilder();
		for(int i = 0; i < e.getSource().getModuloModelActivo().getFiltros().getElementCount(); i++) {
			if(e.getSource().getModuloModelActivo().getFiltros().getElements().get(i) != null) {
				Model<Filtro<T, ?>> model = e.getSource().getModuloModelActivo().getFiltros().getElements().get(i);
				if(model.isGroupModel()) {
					final GroupModel<Filtro<T, ?>> groupElement = (GroupModel<Filtro<T, ?>>)model;
					for(Filtro<T, ?> filtro : groupElement.getModels()) {
						if(filtro.getValue() != null && !filtro.getValue().equals("")) {
							// filtros = filtros + "  " + filtro.getNombre() + ": " + filtro.getValue() + "  ";
							filtrosBuilder.append("  ");
							filtrosBuilder.append(filtro.getNombre());
							filtrosBuilder.append(": ");
							
							if(filtro.getValue() instanceof Boolean) {
								if(filtro.getValue().equals(false)) {
									filtrosBuilder.append("No");
								} else {
									filtrosBuilder.append("Si");
								}
							}else{
								filtrosBuilder.append(filtro.getValue());
							}
							
							filtrosBuilder.append("  ");
						} else {
							// filtros = filtros + "  " + filtro.getNombre() + ": " + "-- ";
							filtrosBuilder.append("  ");
							filtrosBuilder.append(filtro.getNombre());
							filtrosBuilder.append(": ");
							filtrosBuilder.append("-- ");
						}
					}
				} else {
					final SingleModel<Filtro<T, ?>> singleElement = (SingleModel<Filtro<T, ?>>)model;
					if(singleElement.getModel().getValue() != null && !singleElement.getModel().getValue().equals("")) {
						// filtros = filtros + "  " + singleElement.getModel().getNombre() + ": " +
						// singleElement.getModel().getValue() + "  ";

						filtrosBuilder.append("  ");
						filtrosBuilder.append(singleElement.getModel().getNombre());
						filtrosBuilder.append(": ");

						
						//Modifico True o False por Si o No, para los filtros que son Booleanos
						if(singleElement.getModel().getValue() instanceof Boolean) {
							if(singleElement.getModel().getValue().equals(false)) {
								filtrosBuilder.append("No");
							} else {
								filtrosBuilder.append("Si");
							}
						}else{
							filtrosBuilder.append(singleElement.getModel().getValue());
						}

						filtrosBuilder.append("  ");

					} else {
						// filtros = filtros + "  " + singleElement.getModel().getNombre() + ": " + "-- ";
						filtrosBuilder.append("  ");
						filtrosBuilder.append(singleElement.getModel().getNombre());
						filtrosBuilder.append(": ");
						filtrosBuilder.append("-- ");
					}
				}
			}
		}
		this.setFiltros(filtrosBuilder.toString());
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getSubtitulo() {
		return subtitulo;
	}

	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}

	public String getFiltros() {
		return filtros;
	}

	public void setFiltros(String filtros) {
		this.filtros = filtros;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

}
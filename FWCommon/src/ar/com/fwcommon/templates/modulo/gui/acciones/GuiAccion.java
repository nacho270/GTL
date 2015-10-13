package ar.com.fwcommon.templates.modulo.gui.acciones;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ar.com.fwcommon.boss.BossError;
import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionExecutedEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionExecutedListener;
import ar.com.fwcommon.templates.modulo.model.listeners.ModelChangeEvent;
import ar.com.fwcommon.templates.modulo.model.listeners.ModelChangeListener;
import ar.com.fwcommon.templates.modulo.resources.ModuleSettingsManager;

/**
 * Clase utilizada para representar los botones de acción de los módulos de las
 * aplicaciones.
 * 
 * 
 */
@SuppressWarnings("serial")
public class GuiAccion<T> extends JPanel {	
	public static final int DEFAULT_BUTTON_WIDTH = 20;
	public static final int DEFAULT_BUTTON_HEIGHT = 20;

	private final ModuloTemplate<T, ?> owner;
	private Accion<T> accion;
	private JButton jButton;
	private JLabel jLabel;
	private ActionListener buttonActionListener;
	
	public GuiAccion(ModuloTemplate<T, ?> owner, Accion<T> accion) {
		super();
		this.owner = owner;
		this.jButton = new JButton();
		this.jLabel = BossEstilos.createLabel("");
		setAccion(accion);
		construct();
	}

	//Construye gráficamente el componente
	private void construct() {
		this.setLayout(new GridBagLayout());
		
		jButton.addActionListener(getButtonActionListener());
		this.add(jButton, new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,
				GridBagConstraints.NONE,new Insets(0,0,0,0), 0, 0));
		this.add(jLabel, new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.WEST,
				GridBagConstraints.NONE,new Insets(0,2,0,0), 0, 0));
		this.validate();
	}

	/**
	 * Devuelve el Módulo Template al que pertenece esta GUI
	 * @return Modulo Template al que pertenece esta GUI
	 */
	protected ModuloTemplate<T, ?> getOwner() {
		return owner;
	}
	
	/**
	 * Devuelve el objeto <b>Accion</b> asociado al botón.
	 * @return accion La acción asociada al botón.
	 */
	public Accion<T> getAccion() {
		return accion;
	}

	/**
	 * Setea la acción asociada al botón.
	 * @param accion La acción a asociar al componente.
	 */
	public void setAccion(Accion<T> accion) {
		if (this.accion != accion) {
			if (this.accion != null) this.accion.removeModelChangeListener(getAccionChangeListener());
			
			this.accion = accion;
			if (this.accion != null) this.accion.addModelChangeListener(getAccionChangeListener());
			
			updateFromModel();
		}
	}

	/**
	 * Toma los valores del modelo y los aplica al botón
	 */
	private void updateFromModel() {
		//Enabled
		this.setEnabled(this.accion.isValida());
		jButton.setEnabled(this.accion.isValida());
		jLabel.setEnabled(this.accion.isValida());
		
		//Imagenes
		decorateButton(jButton, getAccion());
		
		//Nombre y label
		jLabel.setText(accion.getNombre());
		jLabel.setVisible(accion.isMuestraNombre());
		
		//Tooltip
		jLabel.setToolTipText(getAccion().getTooltip());
		jButton.setToolTipText(getAccion().getTooltip());
	}

	/**
	 * Agrega un listener que notifica cuando se ejecutó una acción
	 * @param listener Listener a agregar
	 */
	public void addAccionExecutedListener(AccionExecutedListener listener) {
		listenerList.add(AccionExecutedListener.class, listener);
	}
	
	/**
	 * Quita un listener que notifica cuando se ejecutó una acción
	 * @param listener Listener a quitar
	 */
	public void removeAccionExecutedListener(AccionExecutedListener listener) {
		listenerList.remove(AccionExecutedListener.class, listener);
	}
	
	/**
	 * Notifica que se ha ejecutado la acción
	 * 
	 * @param refreshTable Si se debe refrescar la tabla
	 * @param selectedItems La lista de items que deben quedar seleccionados
	 *            luego de refrescar la tabla. <code>null</code> hace la
	 *            opción por defecto (mantener la selección)
	 */
	protected void fireActionExecutedListener(final boolean refreshTable, final List<T> selectedItems) {
		final AccionExecutedEvent e = new AccionExecutedEvent(this, refreshTable, selectedItems);
		final AccionExecutedListener[] l = listenerList.getListeners(AccionExecutedListener.class);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for (int i = 0; i < l.length; i++) {
					l[i].accionExcecuted(e);
				}
			}			
		});
	}

	private static void decorateButton(JButton jButton, Accion<?> accion) {
		boolean bigFont = ModuleSettingsManager.getInstance().isBigFont();
		String imagenActivo = (bigFont && accion.getImagenGrandeActivo() != null) ? accion.getImagenGrandeActivo() : accion.getImagenActivo();
		String imagenInactivo = (bigFont && accion.getImagenGrandeInactivo() != null) ? accion.getImagenGrandeInactivo() : accion.getImagenInactivo();
		BossEstilos.decorateButton(jButton, imagenActivo, imagenInactivo);
	}
	
	
	/**
	 * Devuelve el {@link ActionListener} que se utiliza en este botón
	 * @return {@link ActionListener} que utiliza este botón
	 */
	protected ActionListener getButtonActionListener() {
		if (buttonActionListener == null) {
			buttonActionListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						List<T> selectedItems = null;
						boolean refreshTable;
						selectedItems = getOwner().getGuiTabla().getObjetosSeleccionados();
						AccionEvent<T> event = new AccionEvent<T>(getOwner(), selectedItems);
						refreshTable = accion.ejecutar(event);
						fireActionExecutedListener(refreshTable, selectedItems);
						
					} catch (RuntimeException ex) {
						ex.printStackTrace();
						BossError.gestionarError(new FWException(BossError.interpretarMensajeExcepcion(ex), ex));
						
					} catch (FWException ex) {
						ex.printStackTrace();
						BossError.gestionarError(ex);
					}
				}
			};
		}
		return buttonActionListener;
	}
	
	private ModelChangeListener accionChangeListener;
	/**
	 * Devuelve el listener que se coloca en la acción (modelo) para observar cuando
	 * cambió
	 * 
	 * @return {@link ModelChangeListener} que se coloca en el modelo
	 */
	protected ModelChangeListener getAccionChangeListener() {
		if (accionChangeListener == null) {
			accionChangeListener = new ModelChangeListener() {
				public void stateChanged(ModelChangeEvent e) {
					switch (e.getEventType()) {
					case Accion.EVENT_TYPE_ENABLED_CHANGE:
						updateFromModel();					
						break;

					case Accion.EVENT_TYPE_NAME_CHANGE:
						jLabel.setText(getAccion().getNombre());
						break;
						
					case Accion.EVENT_TYPE_DESCRIPCION_CHANGE:
						//ARI: Ver que se hace con la descripcion
						break;
						
					case Accion.EVENT_TYPE_TOOLTIP_CHANGE:
						updateFromModel();
						break;

					case Accion.EVENT_TYPE_IMAGE_CHANGE:
						decorateButton(jButton, getAccion());
						break;
						
					case Accion.EVENT_TYPE_SHOW_NAME_CHANGE:
						jLabel.setVisible(getAccion().isMuestraNombre());
						break;
					}		
				}				
			};
		}
		return accionChangeListener;
	}
}
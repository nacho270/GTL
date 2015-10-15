package ar.com.fwcommon.templates.main;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.Action;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;

import org.apache.log4j.Logger;

import ar.com.fwcommon.boss.BossError;
import ar.com.fwcommon.componentes.FWCursor;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.entidades.Modulo;
import ar.com.fwcommon.templates.main.config.IConfigClienteManager;
import ar.com.fwcommon.templates.main.login.FWLoginManager;
import ar.com.fwcommon.templates.main.menu.ManejadorMenues;
import ar.com.fwcommon.templates.main.menu.MenuModulosSimple;
import ar.com.fwcommon.templates.main.skin.AbstractSkin;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.MiscUtil;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class FWMainTemplate<T extends FWLoginManager, V extends IConfigClienteManager> extends AbstractMainTemplate<T> {
	private static final long serialVersionUID = 1L;
	
	protected IConfigClienteManager configClienteManager;


	/**
	 * Método constructor.
	 * @param idAplicacion El id de la aplicación.
	 * @param version La versión de la aplicación.
	 * @throws FWException 
	 */
	protected FWMainTemplate(int idAplicacion, String version) throws FWException {
		super(idAplicacion, version);
		construirMenues();
	}

	/**
	 * Método constructor.
	 * @param idAplicacion El id de la aplicación.
	 * @param titulo El título de la aplicación.
	 * @param version La versión de la aplicación.
	 * @throws FWException 
	 */
	protected FWMainTemplate(int idAplicacion, String titulo, String version) throws FWException {
		super(idAplicacion, titulo, version);
		construirMenues();
	}

	/**
	 * Crea el manager para el manejo de la configuración del cliente.
	 * @return IConfigClienteManager
	 * ar.com.fwcommon.templates.main.config.IConfigClienteManager
	 */
	protected abstract IConfigClienteManager crearConfigClienteManager();

	/** Método que se ejecuta <b>antes</b> de la construcción de la ventana */
	@Override
	protected void preConstruccion() {
		//Fix del FileChooser
		if(MiscUtil.getOSName().equalsIgnoreCase("Windows NT") && MiscUtil.getOSVersion().equals("4.0")) {
			System.setProperty("swing.disableFileChooserSpeedFix", "true");
		}
	}

	/** Método que se ejecuta <b>después</b> de la construcción de la ventana 
	 * @throws FWException */
	@Override
	protected void postConstruccion() throws FWException {
		//Carga la configuración del cliente
		cargarConfiguracionCliente();
	}

	/**
	 * Método llamado inmediatamente después de que se loguea el usuario.
	 * Una vez que el usuario se logueó se le piden al manager los módulos que tiene
	 * asignados y se llena el menú <b>Módulos</b>.
	 * @throws FWException 
	 */
	@Override
	protected void postLogin() throws FWException {
		try {
			//Se carga la lista de módulos de usuario
			List<Modulo> modulosUsuario = loginManager.getModulosUsuario();
			if(modulosUsuario == null || modulosUsuario.isEmpty()) {
				FWJOptionPane.showWarningMessage(this, "No tiene acceso a la aplicación.", getTitle());
				verDialogoLogin();
			} else {
				//Se llena el menú con los módulos
				agregarModulos();
			}
		} catch(FWException e) {
			BossError.gestionarError(e);
		}
	}

	/** Método llamado antes de que se cierre la ventana principal */
	@Override
	protected void preSalir() {
		//Guarda la configuración del cliente
		guardarConfiguracionCliente();
	}

	/**
	 * Devuelve el menú <b>Módulos</b>.
	 * @return menuModulos
	 * ar.com.fwcommon.templates.main.menu.MenuModulos
	 * @deprecated devuelve solo el primero. Reemplazar por getxxxxxxxxx.get(0)
	 */
	@Deprecated
	protected MenuModulosSimple getMenuModulos() {
		return ManejadorMenues.getManejadorMenues(this).getMenuDefault();
	}

	/**
	 * Aplica el skin a la aplicación.
	 * Deberá llamarse antes de hacer la llamada al método <b>iniciarAplicacion()</b> para
	 * que se aplique el skin en su totalidad.
	 * @param skin El skin a ser aplicado.
	 * @throws FWException En caso de que se produzca algún error en la inicialización
	 * del skin.
	 * ar.com.fwcommon.templates.main.skin.AbstractSkin
	 */
	public static void aplicarSkin(AbstractSkin skin) throws FWException {
		skin.init();
	}

	
	/* Construye el menú 'Módulos' */
	private void construirMenues() {
		ManejadorMenues.getManejadorMenues(this);
	}

	static Logger logger = Logger.getLogger(FWMainTemplate.class); 

	/* Agrega los módulos a partir de la lista de módulos obtenida del login manager */
	private void agregarModulos() throws FWException {
		List<Modulo> modulos = loginManager.getModulosUsuario();
		ManejadorMenues manejadorMenues = ManejadorMenues.getManejadorMenues(this);
		
		manejadorMenues.limpiarModulos();
		manejadorMenues.iniciarSubmenues();
		
		for(Modulo modulo : manejadorMenues.reordenamiento(modulos)) {
			if(modulos.contains(modulo)) {
				manejadorMenues.agregarModulo(modulo);
			}
		}
		manejadorMenues.mostrarIconos();
	}

	/**
	 * {@link AbstractMainTemplate#agregarMenu(JMenu, int)}
	 * 
	 * Solo las subclases y ManejadorMenues(this) lo llaman.
	 */
	@Override
	public void agregarMenu(JMenu menu, int pos) {
		super.agregarMenu(menu, pos);
	}
	
	/**
	 * {@link AbstractMainTemplate#agregarMenu(JMenu, int)}
	 * 
	 * Toma en cuenta el desplazamiento generado por los menues de modulos adicionales
	 * 
	 * 
	 */
	public void agregarMenuDespl(JMenu menu, int pos) {
		byte cantMArriba = ManejadorMenues.getManejadorMenues(this).getCantidadMenuesArriba();
		//desplaza segun los menues de modulos que se agregan
		//en conf. x defecto: (cantMArriba-1)+pos = pos
		super.agregarMenu(menu, (cantMArriba-1)+pos);
	}	
	
	

	/* Carga la configuración del cliente desde el manager */
	private void cargarConfiguracionCliente() {
		configClienteManager = crearConfigClienteManager();
		try {
			if(configClienteManager == null) {
				System.err.println("Advertencia: el Manager de configuración de cliente es nulo. La configuración no será cargada.");
			} else {
				configClienteManager.cargarConfiguracionCliente();
			}
		} catch(FWException e) {
			e.setMensajeContexto("No se pudo cargar la configuración del cliente.");
			BossError.gestionarError(e);
		}
	}

	/* Guarda la configuración del cliente desde el manager */
	private void guardarConfiguracionCliente() {
		try {
			if(configClienteManager == null) {
				System.err.println("Advertencia: el Manager de configuración de cliente es nulo. La configuración no será guardada.");
			} else {
				configClienteManager.guardarConfiguracionCliente();
			}
		} catch(FWException e) {
			e.setMensajeContexto("No se pudo guardar la configuración del cliente.");
			BossError.gestionarError(e);
		}
	}

	/* Clase listener de eventos de los módulos obtenidos del manager de login */
	public class ModuloListener implements ActionListener {
		Modulo modulo;
		Class clase;
		JInternalFrame form;

		public ModuloListener(Modulo modulo) throws ClassNotFoundException, NullPointerException {
			this.modulo = modulo;
			this.clase = Class.forName(modulo.getUbicacion()) ;
		}

		public void actionPerformed(ActionEvent evt) {
			if(form == null || form.isClosed()) {
				FWCursor.startWait(getContentPane());
				try {
					if(modulo.isAction()) {
						Action newInstance = (Action)clase.getConstructor(new Class[] { Frame.class }).newInstance(new Object[] { FWMainTemplate.getFrameInstance() });
						newInstance.actionPerformed(new ActionEvent(new Object(), 0, ""));
						return;
					}

					try {
						form = (JInternalFrame)clase.getConstructor(new Class[] { Modulo.class }).newInstance(new Object[] { modulo });
					} catch(NoSuchMethodException e) {
						form = (JInternalFrame)clase.getConstructor(new Class[] { Integer.class }).newInstance(new Object[] { modulo.getIdModulo() });				}

					if(!form.isVisible()) {
						desktop.add(form);
						form.setVisible(true);
						GuiUtil.centrarEnPadre(form);
					} else {
						if(form.isIcon()) {
							try {
								form.setIcon(false);
							} catch(PropertyVetoException e) {
							}
						}
					}
					try {
						form.setSelected(true);
					} catch(PropertyVetoException e) {
					}
				
				} catch(Exception e1) {
					if (e1 instanceof InvocationTargetException && e1.getCause() instanceof FWException) {
						BossError.gestionarError((FWException)e1.getCause());
					} else {						
						BossError.gestionarError(BossError.ERR_APLICACION, "Se produjo un error en la construcción del módulo" , "módulo " + modulo.getNombre(), e1, new String[] {"Comuníquese con Desarrollo de Sistemas."});
					}
				} finally {
					FWCursor.endWait(getContentPane());
				}
			}
		}
	}
	
	public void logout(){
		guardarConfiguracionCliente();
		StringBuffer sb = new StringBuffer("Textil Level - Sistema de Gestión");
		if(version != null) {
			sb.append(" v").append(version);
		}
		preLogout();
		setTitle(sb.toString());
		if(desktop.cerrarVentanas()) {
			verDialogoLogin();
		}
	}
	
	protected void preLogout(){}

	/* Clase listener de eventos de la opción 'Cambiar Usuario' en el menú 'Módulos' */
	public class CambiarUsuarioListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			logout();
		}
	}

	/* Clase listener de eventos de la opción 'Salir' del menú 'Módulos' */
	public class SalirListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			salir();
		}
	}

}
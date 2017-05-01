package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import main.acciones.chat.MenuChat;
import main.servicios.MessageListener;
import main.servicios.Servicio;
import main.servicios.ServiciosPool;
import main.servicios.alertas.gui.JDialogVerNotificaciones;
import main.statusbar.CompoTest;
import main.statusbar.ConfiguracionComponenteStatusBar;
import main.statusbar.StatusBar;
import main.triggers.Trigger;
import ar.com.fwcommon.boss.BossError;
import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.main.FWMainTemplate;
import ar.com.fwcommon.templates.main.config.IConfigClienteManager;
import ar.com.fwcommon.templates.main.menu.MenuAyuda;
import ar.com.fwcommon.templates.main.menu.MenuImpresion;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.MiscUtil;
import ar.com.textillevel.entidades.portal.Modulo;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.gui.modulos.chat.client.ChatClient;
import ar.com.textillevel.gui.util.ESkin;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.notificaciones.entidades.NotificacionUsuario;
import ar.com.textillevel.modulos.notificaciones.facade.api.remote.NotificacionUsuarioFacadeRemote;
import ar.com.textillevel.skin.SkinModerno;
import ar.com.textillevel.util.GTLBeanFactory;

public class GTLMainTemplate extends FWMainTemplate<GTLLoginManager, GTLConfigClienteManager> implements NotificableMainTemplate {

	private static final long serialVersionUID = -7589061723941536496L;
	protected MenuImpresion menuImpresion;
	protected MenuAyuda menuAyuda;
	//private static Logger logger = Logger.getLogger(GTLMainTemplate.class);

	private static final int MAX_NOTIFICACIONES = 20;
	private MessageListener messageListener;
	private JButton btnNotificaciones;
	
	
	static {
		initFlagTest();
		initSkin();
	}

	protected GTLMainTemplate(int idAplicacion, String version) throws FWException {
		super(idAplicacion, version);
		construirMenues();
		getDesktop().add(crearPanelNotificaciones());
	}

	@Override
	protected IConfigClienteManager crearConfigClienteManager() {
		return new GTLConfigClienteManager();
	}

	@Override
	protected GTLLoginManager crearLoginManager() {
		return new GTLLoginManager(idAplicacion);
	}

	private void construirMenues() {
		/** COMENTAR **/
		agregarMenuDespl(new MenuChat(), 1);
//		agregarMenuDespl(new MenuCambioSkin(GTLMainTemplate.getFrameInstance(),this), 2);
//		agregarMenuDespl(new MenuImpresion(), 3);
//		agregarMenuDespl(new MenuAyuda(), 4);
	}
	
	private static void initFlagTest(){
		if(System.getProperty("test")==null){
			System.getProperties().setProperty("test","0");
		}else{
			System.getProperties().setProperty("test","1");
		}
	}

	protected static final void initSkin() {
//		try{
//			if(isWindows()){
//				GuiUtil.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//			}else if(isUnix()){
//				GuiUtil.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
//			}else{
//				initSkintModerno();
//			}
//		}catch(Exception e){
//			try{
//				initSkintModerno();
//			}catch(CLException cle){
//				BossError.gestionarError(cle);
//			}
//		}
		
//		try{
//			if(isUnix()){
//				GuiUtil.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
//			}else{
//				initSkintModerno();
//			}
//		}catch(Exception e){
//			try{
//				initSkintModerno();
//			}catch(CLException cle){
//				BossError.gestionarError(cle);
//			}
//		}
		
		
		try {
//			if(System.getProperty("test")==null){
//				System.getProperties().setProperty("test","0");
//			}else{
//				System.getProperties().setProperty("test","1");
//			}
//			if(System.getProperties().getProperty("test")== null || System.getProperties().getProperty("test").equals("0")){
			if(!GenericUtils.isSistemaTest()){
				initSkintModerno(ESkin.ROJO);
			}else{
				try{
//					javax.swing.plaf.metal.MetalLookAndFeel
					GuiUtil.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				}catch(Exception e){
					initSkintModerno(ESkin.ROJO);
				}
			}
		} catch (FWException e) {
			e.printStackTrace();
		}
	}
	
	public void cambiarSkin(ESkin eskin) throws FWException{
		SkinModerno skinModerno = new SkinModerno(eskin);
		BossEstilos.setDefaultSkin(skinModerno);
		BossEstilos.setDefaultFont(skinModerno.getDecorator().getDefaultFont());
		BossEstilos.setSecondaryFont(skinModerno.getDecorator().getSecondaryFont());
		if(MiscUtil.isMacOS()) {
			BossEstilos.ajustarFuenteComponentes();
		}
		skinModerno.init();
		JInternalFrame[] allFrames = this.getDesktop().getAllFrames();
		for(int i = 0; i <allFrames.length;i++) {
			allFrames[i].updateUI();
		}
	}

	private static void initSkintModerno(ESkin eskin) throws FWException {
		SkinModerno skinModerno = new SkinModerno(eskin);
		BossEstilos.setDefaultSkin(skinModerno);
		BossEstilos.setDefaultFont(skinModerno.getDecorator().getDefaultFont());
		BossEstilos.setSecondaryFont(skinModerno.getDecorator().getSecondaryFont());
		if(MiscUtil.isMacOS()) {
			BossEstilos.ajustarFuenteComponentes();
		}
		skinModerno.init();
	}
	
	@Override
	protected void postConstruccion() throws FWException {
		crearTitulo();
	}

	@Override
	protected final void preConstruccion() {
		super.preConstruccion();
		//Configura todos los Boss
		configurarAplicacion();
	}

	@Override
	protected final void postLogin() throws FWException {
		super.postLogin();
		UsuarioSistema usuarioSistema = GTLGlobalCache.getInstance().getUsuarioSistema();
		if(usuarioSistema!=null){
			setTitle(getTitle()+ " - USUARIO: " + usuarioSistema.getUsrName().toUpperCase());
			messageListener = MessageListener.build(this, GTLGlobalCache.getInstance().getUsuarioSistema());
		}
		List<Modulo> modulosTrigger = GTLGlobalCache.getInstance().getModulosTriggerUsuarioLogueado();
		try {
			List<Trigger> triggers = getTriggers(modulosTrigger);
			if(!triggers.isEmpty()){
				for(Trigger t : triggers){
					if(t.esValido()){
						t.execute();
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			BossError.gestionarError(new FWException(ex.getMessage()));
		}
		
		List<Modulo> servicios = GTLGlobalCache.getInstance().getServiciosUsuarioLogueado();
		try {
			List<Servicio> servs = getServicios(servicios);
			if(!servs.isEmpty()){
				ServiciosPool.launch(servs);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			BossError.gestionarError(new FWException(ex.getMessage()));
		}
		
		//NotificationHelper.showNotification("Usuario conectado", "dfs",EnumNotificaciones.CONNECTED);
		try {
			ChatClient.getInstance().conectar();
		} catch (UnknownHostException e) {
			FWJOptionPane.showErrorMessage(this, "No se ha podido conectar a " + System.getProperty("textillevel.chat.server.ip") +":" +System.getProperty("textillevel.chat.server.port")+". Host desconocido", "Error");
		} catch (IOException e) {
			FWJOptionPane.showErrorMessage(this, "Ha ocurrido un error al intentar conectarse : " + System.getProperty("textillevel.chat.server.ip") +":" +System.getProperty("textillevel.chat.server.port")+".", "Error");
		} catch (Exception e){
			FWJOptionPane.showErrorMessage(this,"Error desconocido al iniciar el cliente de Chat.","Error");
			e.printStackTrace();
		}
		actualizarNotificaciones();
		messageListener.start();
	}

	private List<Servicio> getServicios(List<Modulo> servicios) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
		List<Servicio> servs = new ArrayList<Servicio>();
		if(!servicios.isEmpty()){
			for(Modulo m : servicios){
				servs.add((Servicio)Class.forName(m.getClassName()).getConstructors()[0].newInstance((Object[])null));
			}
		}
		return servs;
	}

	private List<Trigger> getTriggers(List<Modulo> modulosTrigger) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
		List<Trigger> triggers = new ArrayList<Trigger>();
		if(!modulosTrigger.isEmpty()){
			for(Modulo m : modulosTrigger){
				triggers.add((Trigger)Class.forName(m.getClassName()).getConstructors()[0].newInstance((Object[])null));
			}
		}
		return triggers;
	}

	private void crearTitulo() throws FWException {
		StringBuffer sb = new StringBuffer("Textil Level - Sistema de Gestión");
		if(version != null) {
			sb.append(" v").append(version);
		}
		setTitle(sb.toString());
	}

	private void configurarAplicacion() {

	}
	
	@Override
	protected void preSalir() {
		ChatClient.getInstance().desconectar();
		messageListener.stop();
		ServiciosPool.stopAll();
	}

	@Override
	protected void preLogout() {
		ChatClient.getInstance().desconectar();
		messageListener.stop();
		ServiciosPool.stopAll();
	}
	
	protected void crearTray(){
		//NO SE USA POR AHORA, PERO SE PODRIA USAR SI SE LE QUIERE AGREGAR UN POPUP MENU
	}
	
	protected void crearStatusBar() {
		StatusBar comp = new StatusBar(this);
		comp.addComponent(new CompoTest(new ConfiguracionComponenteStatusBar(true, 1000l)));
		add(comp, BorderLayout.SOUTH);
	}
	
	private JButton getBtnNotificaciones() {
		if (btnNotificaciones == null){
			btnNotificaciones = new JButton("0");
			btnNotificaciones.setVerticalTextPosition(SwingConstants.BOTTOM);
			btnNotificaciones.setHorizontalTextPosition(SwingConstants.CENTER);
			btnNotificaciones.setBounds(new Rectangle(new Point(55, 5), btnNotificaciones.getPreferredSize()));
			btnNotificaciones.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					List<NotificacionUsuario> notificaciones = GTLBeanFactory.getInstance().getBean2(NotificacionUsuarioFacadeRemote.class).getNotificacionesByUsuario(GTLGlobalCache.getInstance().getUsuarioSistema().getId(), MAX_NOTIFICACIONES);
					if (notificaciones == null || notificaciones.isEmpty()) {
						return;
					}
					new JDialogVerNotificaciones(GTLMainTemplate.frameInstance, notificaciones).setVisible(true);
				}
			});
		}
		return btnNotificaciones;
	}

	private JPanel crearPanelNotificaciones() {
		JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		pnl.setBackground(Color.WHITE);
		pnl.setBounds(new Rectangle(new Point(5, 5), new Dimension(120, 120)));
		JLabel label = new JLabel("Notificaciones");
		pnl.add(label);
		pnl.add(getBtnNotificaciones());
		return pnl;
	}

	@Override
	public void actualizarNotificaciones() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Integer cantidadNoLeidas = GTLBeanFactory.getInstance().getBean2(NotificacionUsuarioFacadeRemote.class).getCountNotificacionesNoLeidasByUsuario(GTLGlobalCache.getInstance().getUsuarioSistema().getId());
				getBtnNotificaciones().setText(String.valueOf(cantidadNoLeidas));
			}
		}).start();
	}

	@Override
	public void mostrarNotificacion(String text) {
		FWJOptionPane.showInformationMessage(null, text, "Notificacion");
	}

	@Override
	public void mostrarNotificacion(final NotificacionUsuario notifiacion) {
		// por ahora muestro el texto nomas, hay que buscar la forma de tener acciones especificas para cada tipo
		mostrarNotificacion(notifiacion.getTexto());
	}
}

package main;

import java.awt.BorderLayout;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JInternalFrame;

import main.acciones.cambioskin.MenuCambioSkin;
import main.acciones.chat.MenuChat;
import main.servicios.Servicio;
import main.servicios.ServiciosPool;
import main.statusbar.CompoTest;
import main.statusbar.ConfiguracionComponenteStatusBar;
import main.statusbar.StatusBar;
import main.triggers.Trigger;
import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.boss.BossEstilos;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.main.CLMainTemplate;
import ar.clarin.fwjava.templates.main.config.IConfigClienteManager;
import ar.clarin.fwjava.templates.main.menu.MenuAyuda;
import ar.clarin.fwjava.templates.main.menu.MenuImpresion;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.MiscUtil;
import ar.com.textillevel.entidades.portal.Modulo;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.gui.modulos.chat.client.ChatClient;
import ar.com.textillevel.gui.util.ESkin;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.skin.SkinModerno;

public class GTLMainTemplate extends CLMainTemplate<GTLLoginManager, GTLConfigClienteManager> {

	private static final long serialVersionUID = -7589061723941536496L;
	protected MenuImpresion menuImpresion;
	protected MenuAyuda menuAyuda;
	//private static Logger logger = Logger.getLogger(GTLMainTemplate.class);

	static {
		initFlagTest();
		initSkin();
	}

	protected GTLMainTemplate(int idAplicacion, String version) throws CLException {
		super(idAplicacion, version);
		construirMenues();		
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
		agregarMenuDespl(new MenuCambioSkin(GTLMainTemplate.getFrameInstance(),this), 2);
		agregarMenuDespl(new MenuImpresion(), 3);
		agregarMenuDespl(new MenuAyuda(), 4);
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
		} catch (CLException e) {
			e.printStackTrace();
		}
	}
	
	public void cambiarSkin(ESkin eskin) throws CLException{
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

	private static void initSkintModerno(ESkin eskin) throws CLException {
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
	protected void postConstruccion() throws CLException {
		crearTitulo();
	}

	@Override
	protected final void preConstruccion() {
		super.preConstruccion();
		//Configura todos los Boss
		configurarAplicacion();
	}

	@Override
	protected final void postLogin() throws CLException {
		super.postLogin();
		UsuarioSistema usuarioSistema = GTLGlobalCache.getInstance().getUsuarioSistema();
		if(usuarioSistema!=null){
			setTitle(getTitle()+ " - USUARIO: " + usuarioSistema.getUsrName().toUpperCase());
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
			BossError.gestionarError(new CLException(ex.getMessage()));
		}
		
		List<Modulo> servicios = GTLGlobalCache.getInstance().getServiciosUsuarioLogueado();
		try {
			List<Servicio> servs = getServicios(servicios);
			if(!servs.isEmpty()){
				ServiciosPool.launch(servs);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			BossError.gestionarError(new CLException(ex.getMessage()));
		}
		
		//NotificationHelper.showNotification("Usuario conectado", "dfs",EnumNotificaciones.CONNECTED);
		try {
			ChatClient.getInstance().conectar();
		} catch (UnknownHostException e) {
			CLJOptionPane.showErrorMessage(this, "No se ha podido conectar a " + System.getProperty("textillevel.chat.server.ip") +":" +System.getProperty("textillevel.chat.server.port")+". Host desconocido", "Error");
		} catch (IOException e) {
			CLJOptionPane.showErrorMessage(this, "Ha ocurrido un error al intentar conectarse : " + System.getProperty("textillevel.chat.server.ip") +":" +System.getProperty("textillevel.chat.server.port")+".", "Error");
		} catch (Exception e){
			CLJOptionPane.showErrorMessage(this,"Error desconocido al iniciar el cliente de Chat.","Error");
			e.printStackTrace();
		}
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

	private void crearTitulo() throws CLException {
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
		ServiciosPool.stopAll();
	}

	@Override
	protected void preLogout() {
		ChatClient.getInstance().desconectar();
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
}

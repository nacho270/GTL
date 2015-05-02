package main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.com.textillevel.gui.util.EventQueueProxy;
import ar.com.textillevel.gui.util.GenericUtils;

public class GuiBackTextilLevel extends GTLMainTemplate {

	private static final long serialVersionUID = -2538191821904835236L;
	private static final String VERSION = "3.0";

	protected GuiBackTextilLevel(int idAplicacion, String version) throws CLException {
		super(idAplicacion, version);
		getDesktop().setBackground(new Color(255,255,255));
		if(!GenericUtils.isSistemaTest()){
			setBackgroundImage("ar/com/textillevel/imagenes/logogtl.jpg");
		}else{
			setBackgroundImage("ar/com/textillevel/imagenes/logogtl-test.jpg");
		}
		crearTrayIcon("ar/com/textillevel/imagenes/logogtl-ventana.jpg", "Gestion TextilLevel");
		setIconoVentana("ar/com/textillevel/imagenes/logogtl-ventana.jpg");
	}

	public static void main(String[] args) {
		try {
			System.getProperties().setProperty("applicationName", "GTL");
			System.getProperties().setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
			System.getProperties().setProperty("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
			System.getProperties().setProperty("cltimezone","GMT-3");
			if(System.getProperty("java.naming.provider.url")==null){
				System.getProperties().setProperty("java.naming.provider.url", "localhost:1099");
			}
			if(System.getProperty("textillevel.chat.server.ip")==null){
				System.getProperties().setProperty("textillevel.chat.server.ip", "127.0.0.1");
			}
			if(System.getProperty("textillevel.chat.server.port")==null){
				System.getProperties().setProperty("textillevel.chat.server.port", "7777");
			}
			GuiBackTextilLevel guiBackTextilLevel = new GuiBackTextilLevel(-1, VERSION);
			EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
			queue.push(new EventQueueProxy(guiBackTextilLevel));
			guiBackTextilLevel.setExtendedState(MAXIMIZED_BOTH);
			guiBackTextilLevel.iniciarAplicacion();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
package ar.com.fwcommon.templates.main.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.swing.JMenuItem;

import ar.com.fwcommon.AnchorTrick;
import ar.com.fwcommon.componentes.FWAboutScreen;
import ar.com.fwcommon.componentes.error.FWException;

public class MenuAyuda extends FWJMenu {

	private static final long serialVersionUID = -4160016552497241971L;

	private JMenuItem menuAcercaDe;
	private JMenuItem menuContenidos;
	private FWAboutScreen aboutScreen;
	private static HelpSet helpSet;
	private static HelpBroker helpBroker;
	private String archivoAyuda;
	private String imagenAbout;
	private String version;
	private static final String COMPANY = "EMPRESA";

	public MenuAyuda() {
		super("Ayuda", 'A');
		add(getMenuAcercaDe());
	}

	private void addContenidos() {
		add(getMenuContenidos());
	}

	/** Clase listener del menú 'Ayuda' */
	class AyudaListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			//Contenidos
			if(evt.getSource().equals(getMenuContenidos())) {
				helpBroker.setDisplayed(true);
				//Acerca De...
			} else if(evt.getSource().equals(getMenuAcercaDe())) {
				verAboutScreen(getImagenAbout(), COMPANY, getVersion());
			}
		}
	}

	public JMenuItem getMenuAcercaDe() {
		if(menuAcercaDe == null) {
			menuAcercaDe = new JMenuItem("Acerca de...");
			menuAcercaDe.addActionListener(new AyudaListener());
			menuAcercaDe.setEnabled(false);
		}
		return menuAcercaDe;
	}

	public JMenuItem getMenuContenidos() {
		if(menuContenidos == null) {
			menuContenidos = new JMenuItem("Contenidos");
			menuContenidos.addActionListener(new AyudaListener());
		}
		return menuContenidos;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getArchivoAyuda() {
		return archivoAyuda;
	}

	public void setArchivoAyuda(String archivoAyuda) throws FWException {
		this.archivoAyuda = archivoAyuda;
		crearHelp();
	}

	public String getImagenAbout() {
		return imagenAbout;
	}

	public void setImagenAbout(String imagenAbout) {
		this.imagenAbout = imagenAbout;
		getMenuAcercaDe().setEnabled(true);
	}

	/* Crea la ayuda en pantalla */
	private void crearHelp() throws FWException {
		ClassLoader cl = AnchorTrick.class.getClassLoader();
		try {
			URL hsURL = new URL(getArchivoAyuda()) ;
			helpSet = new HelpSet(cl, hsURL);
			helpBroker = helpSet.createHelpBroker();
			addContenidos() ;
		} catch(HelpSetException e) {
			throw new FWException("No se puede inicializar la ayuda", e);
		} catch (MalformedURLException e) {
			throw new FWException("No se puede inicializar la ayuda", e);
		}
	}

	/*
	 * Muestra la ventana de <b>Acerca De...</b> de la aplicación.
	 * @param imagen La imágen a mostrar en la ventana.
	 * @param titulo El título de la ventana.
	 * @param version El nro. de versión de la aplicación.
	 * @param versionJava Si es true muestra la versión del JDK instalado.
	 */
	private void verAboutScreen(String imagen, String titulo, String version) {
		if(aboutScreen == null) {
			aboutScreen = new FWAboutScreen(imagen, titulo, "Versión " + version);
			aboutScreen.displayJavaVersion();
		}
		aboutScreen.setVisible(true);
	}

}
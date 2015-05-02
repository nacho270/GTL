package main.acciones.parametrosgenerales;

import java.awt.Frame;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;

import ar.clarin.fwjava.templates.main.menu.CLJMenu;

public class MenuParametrosGenerales extends CLJMenu {

	private static final long serialVersionUID = -7385942711273234287L;
	private JMenuItem menuParametrosGenerales;
	private Frame frame;

	public MenuParametrosGenerales(Frame frame) {
		super("Configuración", 'C');
		setMnemonic(KeyEvent.VK_O);
		this.frame = frame;
		add(getMenuParametrosGenerales());
	}

	public JMenuItem getMenuParametrosGenerales() {
		if(menuParametrosGenerales == null){
			menuParametrosGenerales = new JMenuItem(new ModificarParametrosGeneralesAction(getFrame()));
			menuParametrosGenerales.setText("Modificar parametros generales");
			menuParametrosGenerales.setEnabled(true);
		}
		return menuParametrosGenerales;
	}

	public void setMenuParametrosGenerales(JMenuItem menuParametrosGenerales) {
		this.menuParametrosGenerales = menuParametrosGenerales;
	}

	public Frame getFrame() {
		return frame;
	}

	public void setFrame(Frame frame) {
		this.frame = frame;
	}
}

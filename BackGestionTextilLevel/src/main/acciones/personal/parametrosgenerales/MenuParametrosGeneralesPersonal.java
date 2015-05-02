package main.acciones.personal.parametrosgenerales;

import java.awt.Frame;

import javax.swing.JMenuItem;

import ar.clarin.fwjava.templates.main.menu.CLJMenu;


public class MenuParametrosGeneralesPersonal extends CLJMenu {

	private static final long serialVersionUID = -7385942711273234287L;
	private JMenuItem menuParametrosGenerales;
	private Frame frame;

	public MenuParametrosGeneralesPersonal(Frame frame) {
		this.frame = frame;
		add(getMenuParametrosGenerales());
	}

	public JMenuItem getMenuParametrosGenerales() {
		if(menuParametrosGenerales == null){
			menuParametrosGenerales = new JMenuItem(new ModificarParametrosGeneralesPersonalAction(getFrame()));
			menuParametrosGenerales.setText("Modificar parametros generales");
			menuParametrosGenerales.setEnabled(true);
		}
		return menuParametrosGenerales;
	}

	public void setMenuParametrosGenerales(JMenuItem menuParametrosGenerales) {
		this.menuParametrosGenerales = menuParametrosGenerales;
	}

	@Override
	public Frame getFrame() {
		return frame;
	}

	public void setFrame(Frame frame) {
		this.frame = frame;
	}

}

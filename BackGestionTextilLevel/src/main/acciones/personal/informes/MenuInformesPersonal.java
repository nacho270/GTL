package main.acciones.personal.informes;

import java.awt.Frame;

import javax.swing.JMenuItem;

import ar.com.fwcommon.templates.main.menu.FWJMenu;


public class MenuInformesPersonal extends FWJMenu {

	private static final long serialVersionUID = -7385942711273234287L;
	private JMenuItem menuInformeVacaciones;
	private Frame frame;

	public MenuInformesPersonal(Frame frame) {
		this.frame = frame;
		add(getMenuInformeVacaciones());
	}

	public JMenuItem getMenuInformeVacaciones() {
		if(menuInformeVacaciones == null){
			menuInformeVacaciones = new JMenuItem(new InformesVacacionesPersonalAction(getFrame()));
			menuInformeVacaciones.setText("Vacaciones");
			menuInformeVacaciones.setEnabled(true);
		}
		return menuInformeVacaciones;
	}

	public void setMenuParametrosGenerales(JMenuItem menuParametrosGenerales) {
		this.menuInformeVacaciones = menuParametrosGenerales;
	}

	@Override
	public Frame getFrame() {
		return frame;
	}

	public void setFrame(Frame frame) {
		this.frame = frame;
	}

}

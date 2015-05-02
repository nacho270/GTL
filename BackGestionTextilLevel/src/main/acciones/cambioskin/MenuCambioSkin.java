package main.acciones.cambioskin;

import java.awt.Frame;

import javax.swing.JMenuItem;

import main.GTLMainTemplate;

import ar.clarin.fwjava.templates.main.menu.CLJMenu;

public class MenuCambioSkin extends CLJMenu {

	private static final long serialVersionUID = 1423099650817991471L;
	
	private JMenuItem menuItemSkinRojo;
	private JMenuItem menuItemSkinAzul;
	private Frame frame;
	private GTLMainTemplate mainTemplate;
	
	public MenuCambioSkin(Frame frame, GTLMainTemplate mainTemplate) {
		super("Cambiar colores", 'C');
		this.frame = frame;
		this.mainTemplate = mainTemplate;
		add(getMenuItemSkinRojo());
		add(getMenuItemSkinAzul());
	}
	
	public JMenuItem getMenuItemSkinRojo() {
		if(menuItemSkinRojo == null){
			menuItemSkinRojo = new JMenuItem(new CambioSkinRojoAction(frame, mainTemplate));
			menuItemSkinRojo.setText("Skin rojo");
		}
		return menuItemSkinRojo;
	}
	
	public JMenuItem getMenuItemSkinAzul() {
		if(menuItemSkinAzul == null){
			menuItemSkinAzul = new JMenuItem(new CambioSkinAzulAction(frame, mainTemplate));
			menuItemSkinAzul.setText("Skin azul");
		}
		return menuItemSkinAzul;
	}

}

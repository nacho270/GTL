package ar.com.fwcommon.templates.main.menu;

import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class MenuModulosSimple extends FWJMenu {

	public MenuModulosSimple() {
		super();
	}

	public MenuModulosSimple(String nombre) {
		super(nombre);
	}

	public MenuModulosSimple(String nombre, char mnemonic) {
		super(nombre, mnemonic);
	}

	public MenuModulosSimple(Action action) {
		super(action);
	}

	public void agregarModulo(String nombre, ActionListener listener) {
		agregarModulo(nombre, null, listener);
	}

	public void agregarModulo(String nombre, Icon icono, ActionListener listener) {
		JMenuItem item = new JMenuItem(nombre, icono);
		item.addActionListener(listener);
		int pos = getMenuComponentCount(); //3 --> Separador + Cambiar Usuario + Salir
		if(listener == null) {
			item.setEnabled(false);
			add(item, pos);
		} else {
			add(item, pos);
		}
	}

	public void limpiarModulos() {
		removeAll();
	}

	public boolean hasDecorator() {
		return getMenuDecorator() != null;
	}

	public void mostrarIconos() {
	}

	public void agregarSubmenu(MenuModulosSimple menuModulosGrupo) {
		int pos = getMenuComponentCount(); //3 --> Separador + Cambiar Usuario + Salir
		add(menuModulosGrupo, pos);
	}	

}
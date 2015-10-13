package ar.com.fwcommon.templates.main.menu;

import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JMenuItem;

import ar.com.fwcommon.util.ImageUtil;

@SuppressWarnings("serial")
public class MenuModulosPrincipal extends MenuModulosSimple {

	private JMenuItem menuSalir;
	private JMenuItem menuCambiarUsuario;
	static final String ICONO_CAMBIAR_USUARIO = "ar/com/fwcommon/templates/main/menu/decorator/cambiar_usuario.png";
	static final String ICONO_SALIR = "ar/com/fwcommon/templates/main/menu/decorator/blank_icon.png";

	public MenuModulosPrincipal() {
		super("Módulos", 'M');
		//Cambiar usuario
		addSeparator();
		add(getMenuItemCambiarUsuario());
		//Salir
		add(getMenuItemSalir());
	}

	public JMenuItem getMenuItemCambiarUsuario() {
		if(menuCambiarUsuario == null) {
			menuCambiarUsuario = new JMenuItem("Cambiar Usuario");
		}
		return menuCambiarUsuario;
	}

	public JMenuItem getMenuItemSalir() {
		if(menuSalir == null) {
			menuSalir = new JMenuItem("Salir");
		}
		return menuSalir;
	}
	
	public void limpiarModulos() {
		removeAll();
		//Cambiar usuario
		addSeparator();
		add(getMenuItemCambiarUsuario());
		//Salir
		add(getMenuItemSalir());
	}
	
	public void mostrarIconos() {
		getMenuItemCambiarUsuario().setIcon(ImageUtil.loadIcon(ICONO_CAMBIAR_USUARIO));
		getMenuItemSalir().setIcon(ImageUtil.loadIcon(ICONO_SALIR));
	}
	
	public void agregarModulo(String nombre, Icon icono, ActionListener listener) {
		JMenuItem item = new JMenuItem(nombre, icono);
		item.addActionListener(listener);
		int pos = getMenuComponentCount() -3; //3 --> Separador + Cambiar Usuario + Salir
		if(listener == null) {
			item.setEnabled(false);
			add(item, pos);
		} else {
			add(item, pos);
		}
	}
	
	public void agregarSubmenu(MenuModulosSimple menuModulosGrupo) {
		int pos = getMenuComponentCount()-3; //3 --> Separador + Cambiar Usuario + Salir
		add(menuModulosGrupo, pos);
	}		
	

}
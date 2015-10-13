package ar.com.fwcommon.templates.main.menu;

import java.awt.Frame;

import javax.swing.Action;
import javax.swing.JMenu;

import ar.com.fwcommon.templates.main.menu.decorator.MenuDecorator;
import ar.com.fwcommon.util.GuiUtil;

public class FWJMenu extends JMenu {

	private MenuDecorator decorator;

	public FWJMenu() {
		super();
	}

	public FWJMenu(String nombre) {
		super(nombre);
	}

	public FWJMenu(String nombre, char mnemonic) {
		super(nombre);
		setMnemonic(mnemonic);
	}

	public FWJMenu(Action action) {
		super(action);
	}

	public MenuDecorator getMenuDecorator() {
		return decorator;
	}

	public void setMenuDecorator(MenuDecorator decorator) {
		this.decorator = decorator;
	}

	public Frame getFrame() {
		return GuiUtil.getFrameForComponent(this);
	}

}
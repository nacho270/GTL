package main.acciones.chat;

import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;

import ar.com.fwcommon.templates.main.menu.FWJMenu;

public class MenuChat extends FWJMenu {

	private static final long serialVersionUID = 6347837601938238673L;

	private JMenuItem menuItemVerChat;

	public MenuChat() {
		super("Chat");
		setMnemonic(KeyEvent.VK_U);
		add(getMenuItemVerChat());
	}

	public JMenuItem getMenuItemVerChat() {
		if(menuItemVerChat == null){
			menuItemVerChat = new JMenuItem(new VerChatAccion());
			menuItemVerChat.setText("Ver chat");
		}
		return menuItemVerChat;
	}
}

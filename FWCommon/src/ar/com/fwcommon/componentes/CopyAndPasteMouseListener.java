package ar.com.fwcommon.componentes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;

import ar.com.fwcommon.boss.BossError;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.util.ImageUtil;
import ar.com.fwcommon.util.MiscUtil;

public class CopyAndPasteMouseListener extends MouseAdapter {

	JTextComponent textComp;
	JPopupMenu copyAndPasteMenu;
	JMenuItem cutItem;
	JMenuItem copyItem;
	JMenuItem pasteItem;
	static final String IMG_CUT = "ar/com/fwcommon/imagenes/clipboard_cut.png";
	static final String IMG_COPY = "ar/com/fwcommon/imagenes/clipboard_copy.png";
	static final String IMG_PASTE = "ar/com/fwcommon/imagenes/clipboard_paste.png";

	public CopyAndPasteMouseListener(JTextComponent textComp) {
		this.textComp = textComp;
		copyAndPasteMenu = getCopyAndPasteMenu();
	}

	public void mousePressed(MouseEvent evt) {
		showMenu(evt);
	}

	public void mouseReleased(MouseEvent evt) {
		showMenu(evt);
	}

	private void showMenu(MouseEvent evt) {
		if(evt.isPopupTrigger()) {
			// Si la caja de texto es editable
			if(textComp.isEditable()) {
				// Si hay texto seleccionado en la caja de texto habilita las opciones 'Cortar' y 'Copiar'
				if(textComp.getSelectedText() != null && textComp.getSelectedText().length() > 0) {
					cutItem.setEnabled(true);
					copyItem.setEnabled(true);
				} else {
					cutItem.setEnabled(false);
					copyItem.setEnabled(false);
				}
				String clipboardText = null;
				try {
					clipboardText = MiscUtil.getClipboardText();
					// Si hay texto en el portapapeles habilita la opción 'Pegar'
					if(clipboardText != null && clipboardText.length() > 0) {
						pasteItem.setEnabled(true);
					} else {
						pasteItem.setEnabled(false);
					}
				} catch(FWException e) {
					BossError.gestionarError(e);
				}
			} else { // Si la caja de texto es no editable
				cutItem.setEnabled(false);
				pasteItem.setEnabled(false);
				if(textComp.getSelectedText() != null && textComp.getSelectedText().length() > 0) {
					copyItem.setEnabled(true);
				} else {
					copyItem.setEnabled(false);
				}
			}
			// Muestra el menú
			copyAndPasteMenu.show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}

	private JPopupMenu getCopyAndPasteMenu() {
		if(copyAndPasteMenu == null) {
			copyAndPasteMenu = new JPopupMenu();
			//Cut
			cutItem = new JMenuItem("Cortar", ImageUtil.loadIcon(IMG_CUT));
			cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
			cutItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					textComp.cut();
				}
			});
			copyAndPasteMenu.add(cutItem);
			//Copy
			copyItem = new JMenuItem("Copiar", ImageUtil.loadIcon(IMG_COPY));
			copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
			copyItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					textComp.copy();
				}
			});
			copyAndPasteMenu.add(copyItem);
			//Paste
			pasteItem = new JMenuItem("Pegar", ImageUtil.loadIcon(IMG_PASTE));
			pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
			pasteItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					textComp.paste();
				}
			});
			copyAndPasteMenu.add(pasteItem);
		}
		return copyAndPasteMenu;
	}

}
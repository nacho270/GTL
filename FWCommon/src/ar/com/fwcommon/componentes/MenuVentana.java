package ar.com.fwcommon.componentes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import ar.com.fwcommon.templates.main.menu.FWJMenu;

/**
 * Componente que muestra un típico menú <b>Ventana</b> en el menú principal de una
 * aplicación MDI (o interfaz de múltiples documentos).
 */
public class MenuVentana extends FWJMenu {

	private static final long serialVersionUID = 5006152828200923863L;

	private MDIDesktopPane desktop;
	private JMenuItem cascadeItem;
	private JMenuItem tileItem;
	private JMenuItem minimizeAllItem;
	private JMenuItem restoreAllItem;

	public MenuVentana(MDIDesktopPane desktop) {
		super();
		this.desktop = desktop;
		createItems();
		cascadeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				MenuVentana.this.desktop.cascadeFrames();
			}
		});
		tileItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				MenuVentana.this.desktop.tileFrames();
			}
		});
		minimizeAllItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				MenuVentana.this.desktop.minimizeAllFrames();
			}
		});
		restoreAllItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				MenuVentana.this.desktop.restoreAllFrames();
			}
		});
		addMenuListener(new MenuListener() {
			public void menuCanceled(MenuEvent evt) {
			}

			public void menuDeselected(MenuEvent evt) {
				removeAll();
			}

			public void menuSelected(MenuEvent evt) {
				createChildItems();
			}
		});
	}

	private void createItems() {
		setText("Ventana");
		setMnemonic(KeyEvent.VK_V);
		cascadeItem = new JMenuItem("Cascada");
		tileItem = new JMenuItem("Mosaico");
		minimizeAllItem = new JMenuItem("Minimizar Todas");
		restoreAllItem = new JMenuItem("Restaurar Todas");
	}

	//Sets up the children menus depending on the current desktop state
	private void createChildItems() {
		JInternalFrame[] frames = desktop.getAllFrames();
		add(cascadeItem);
		add(tileItem);
		add(minimizeAllItem);
		add(restoreAllItem);
		boolean hasFrames = frames.length > 0;
		if(hasFrames) {
			addSeparator();
		}
		cascadeItem.setEnabled(hasFrames);
		tileItem.setEnabled(hasFrames);
		minimizeAllItem.setEnabled(desktop.deiconifiedFrames() > 0);
		restoreAllItem.setEnabled(desktop.iconifiedFrames() > 0);
		ChildMenuItem childItem;
		for(int i = 0; i < frames.length; i++) {
			childItem = new ChildMenuItem(frames[i]);
			childItem.setState(i == 0);
			childItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					JInternalFrame frame = ((ChildMenuItem)evt.getSource()).getFrame();
					frame.moveToFront();
					try {
						frame.setSelected(true);
					} catch(PropertyVetoException e) {
						e.printStackTrace();
					}
				}
			});
//			childItem.setIcon(frames[i].getFrameIcon());
			add(childItem);
		}
	}

	class ChildMenuItem extends JCheckBoxMenuItem {
		JInternalFrame frame;

		public ChildMenuItem(JInternalFrame frame) {
			super(frame.getTitle());
			this.frame = frame;
		}

		public JInternalFrame getFrame() {
			return frame;
		}
	}

}
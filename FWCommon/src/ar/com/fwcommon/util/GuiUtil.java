package ar.com.fwcommon.util;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.text.MaskFormatter;

import org.apache.commons.lang.StringEscapeUtils;
import org.jdesktop.jdic.tray.SystemTray;
import org.jdesktop.jdic.tray.TrayIcon;

import ar.com.fwcommon.Predeterminable;
import ar.com.fwcommon.componentes.FWBotonesTablaLight;

import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;

/**
 * Clase que contiene funciones gráficas útiles.
 */
public class GuiUtil {

	/**
	 * Habilita/deshabilita un panel, subpaneles y todos sus componentes en forma
	 * recursiva.
	 * @param panel El panel a deshabilitar.
	 * @param estado El flag habilitar/deshabilitar.
	 */
	public static void setEstadoPanel(JComponent panel, boolean estado) {
        Component componentes[] = panel.getComponents();
        for(int i = 0; i < componentes.length; i++) {
        	JComponent component = (JComponent)componentes[i];
            if((component instanceof JPanel && !(component instanceof FWBotonesTablaLight)) ||
               component instanceof JScrollPane ||
               component instanceof JViewport) {
                setEstadoPanel(component, estado);
            } else if(component instanceof JTable ||
               component instanceof JList ||
               !(component instanceof JLabel)) {
                component.setEnabled(estado);
            }
        }
        panel.setEnabled(estado);
    }

	/**
	 * Devuelve <b>true</b> si el componente que generó el evento pasado por parámetro
	 * está habilitado.
	 * @param evt El evento que generó el componente.
	 * @return
	 */
	public static boolean isControlEventoEnabled(EventObject evt) {
		try {
			Component c = (Component)evt.getSource();
			return c.isEnabled();
		} catch(Exception e) {
			return false;
		}
	}

	/**
	 * Agrega un item a una javax.swing.JList.
	 * @param lista El componente JList.
	 * @param item El objeto a agregar como item de la lista.
	 */
	public static void agregarItemLista(JList lista, Object item) {
		((DefaultListModel)lista.getModel()).addElement(item);
	}

	/**
	 * Devuelve <b>true</b> si el item existe en la lista.
	 * @param lista La lista que contiene o no el item.
	 * @param item El item a saber si está contenido en la lista.
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean existeItemLista(List lista, Object item) {
		for(int i = 0; i < lista.size(); i++)
			if(lista.get(i).equals(item))
				return true;
		return false;
	}

	/**
	 * Llena un combobox a partir de una lista de items.
	 * @param cmb El combobox a llenar.
	 * @param list La lista de items con los que se llenará el combobox.
	 * @param disable Flag que determina si se deshabilitará el combobox antes de llenarlo
	 *          para impedir que lance el evento 'itemStateChanged'.
	 */
	@SuppressWarnings("rawtypes")
	public static void llenarCombo(JComboBox c, List l, boolean disable) {
		Object elementoActual;
		int predeterminado = -1;
		boolean enabled = c.isEnabled();
		c.setEnabled(!disable);
		if(c.getItemCount() != 0)
			c.removeAllItems();
		if(l != null) {
			for(int i = 0; i < l.size(); i++) {
				elementoActual = l.get(i); 
				if(elementoActual instanceof Predeterminable)
					if(((Predeterminable)elementoActual).isPredeterminado())
						predeterminado = i;
				c.addItem(elementoActual);
			}
			if(predeterminado != -1)
				c.setSelectedIndex(predeterminado);
		}
		c.setEnabled(enabled);
	}

	/**
	 * Llena un combobox a partir de una lista de items, y para evitar que se disparen los eventos asociados a los listeners, éstos se 
	 * quitan mientras se cargan los items, y se vuelven a a agregar al final.
	 * @param cmb El combobox a llenar.
	 * @param list La lista de items con los que se llenará el combobox.
	 * @param disable Flag que determina si se deshabilitará el combobox antes de llenarlo
	 *          para impedir que lance el evento 'itemStateChanged'.
	 */
	public static <T extends Object> void llenarComboWithoutListener(JComboBox c, List<T> l, boolean disable) {
		Object elementoActual;
		int predeterminado = -1;

		boolean enabled = c.isEnabled();
		c.setEnabled(!disable);

		/* Saco los ItemListener */
		List<ItemListener> itemListenerList = Arrays.asList(c.getItemListeners()) ;
		for (ItemListener itemListener : itemListenerList) {
			c.removeItemListener(itemListener) ;
		}
		/* Saco los ActionListener */
		List<ActionListener> actionListenerList = Arrays.asList(c.getActionListeners()) ;
		for (ActionListener actionListener : actionListenerList) {
			c.removeActionListener(actionListener);
		}

		/* Agrego los items al combo */
		if(c.getItemCount() != 0)
			c.removeAllItems();

		for(int i = 0; i < l.size(); i++) {
			elementoActual = l.get(i); 
			if(elementoActual instanceof Predeterminable)
				if(((Predeterminable)elementoActual).isPredeterminado())
					predeterminado = i;
			c.addItem(elementoActual);
		}
		if(predeterminado != -1)
			c.setSelectedIndex(predeterminado);

		// Agrego los ItemListener
		for (ItemListener itemListener : itemListenerList) {
			c.addItemListener(itemListener) ;
		}
		// Agrego los ActionListener
		for (ActionListener actionListener : actionListenerList) {
			c.addActionListener(actionListener) ;
		}
		c.setEnabled(enabled);
	}

	
	/**
	 * Elimina <b>todos los items</b> de un combobox.
	 * @param cmb El combobox del cual se eliminarán todos los items.
	 */
	public static void vaciarCombo(JComboBox cmb) {
		if(cmb.getItemCount() != 0)
			cmb.removeAllItems();
	}

	/**
	 * Devuelve <b>true</b> si existe un determinado item en el combobox.
	 * @param cmb El combobox que podría contener el item.
	 * @param obj El objeto que representa al item.
	 * @return
	 */
	public static boolean existeItemCombo(JComboBox cmb, Object obj) {
		for(int i = 0; i < cmb.getItemCount(); i++)
			if(((Object)cmb.getItemAt(i)).equals(obj))
				return true;
		return false;
	}

    /**
     * Devuelve el <b>índice</b> en el combobox del objeto obj.
     * @param obj El objeto a buscar.
     * @param cmb El combobox donde buscar.
     * @return El índice del objeto en el combobox.
     */
    public static int getIndiceCombo(Object obj, JComboBox cmb) {
        return ((DefaultComboBoxModel)cmb.getModel()).getIndexOf(obj) + 1;
    }

	/** 
	 * Verifica si el usuario se encuentra en el tab especificado.
	 * Si no es así lo cambia.
	 * @param tabbedPane El contenedor de los tabs.
	 * @param tab El tab en el que se posicionará.
	 */
	public static void verificarTab(JTabbedPane tabbedPane, Component tab) {
		tabbedPane.setSelectedIndex(tabbedPane.indexOfComponent(tab));
	}

	/**
	 * Devuelve el componente <b>divider</b> de un splitPane.
	 * @param splitPane
	 * @return
	 */
	public static BasicSplitPaneDivider getSplitPaneDivider(JSplitPane splitPane) {
		return ((BasicSplitPaneUI)splitPane.getUI()).getDivider();
	}

	/**
	 * Setea el cursor por <b>defecto</b>.
	 * @param c El componente dueño del cursor.
	 */
	public static void setDefaultCursor(Component c) {
		c.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	/**
	 * Setea el cursor de <b>espera</b>.
	 * @param c El componente dueño del cursor.
	 */
	public static void setWaitCursor(Component c) {
		c.setCursor(new Cursor(Cursor.WAIT_CURSOR));
	}

	/**
	 * Centra una ventana en la pantalla.
	 * @param window La ventana a centrar en la pantalla.
	 */
	public static void centrar(Window window) {
		int x = ((Toolkit.getDefaultToolkit().getScreenSize().width - window.getWidth()) / 2);
		int y = ((Toolkit.getDefaultToolkit().getScreenSize().height - window.getHeight()) / 2);
		window.setLocation(x, y);
	}
	
	/**
	 * Posiciona el componente en el centro del padre.  
	 * @param component el componente a centrar.
	 */
	public static void centrarEnPadre(Component component) {
		int x = component.getParent().getX() + (component.getParent().getWidth() - component.getWidth()) / 2;
		int y = component.getParent().getY() + (component.getParent().getHeight() - component.getHeight()) / 2;
		component.setLocation(x,y);
	}

	/**
	 * Posiciona el componente en el centro del padre.  
	 * @param component el componente a centrar.
	 */
	public static void centrarEnFramePadre(Component component) {
		Frame framePadre = (Frame)SwingUtilities.getAncestorOfClass(Frame.class, component) ;
		int x = framePadre.getX() + (framePadre.getWidth() - component.getWidth()) / 2;
		int y = framePadre.getY() + (framePadre.getHeight() - component.getHeight()) / 2;
		component.setLocation(x,y);
	}

    /**
     * Crea un tooltip HTML con un texto y un ícono.
     * @param text El texto del tooltip.
     * @param icon El ícono del tooltip.
     * @return tooltip El tooltip HTML generado.
     */
    public static String crearTooltipHTML(String text, String icon) {
    	// para evitar XSS
    	text = StringEscapeUtils.escapeHtml(text);
    	icon = StringEscapeUtils.escapeHtml(icon);
    	//
    	URL url = FileUtil.getResource(icon);
        String s = text.replaceAll(StringUtil.RETORNO_CARRO, "<BR>");
        String tooltip = "<html><table border=0 cellspacing=0>" +
        				 "<tr><td valign='middle' align='center'>" +
        				 "<img border='0' src='" + url.toString() + "'>" +
        				 "</td><td>" + s + "</td></tr></table></html>";
        return tooltip;
    }

    /**
     * Crea un tooltip HTML con un título y un texto. Opcional una nota adicional.
     * @param title El título del tooltip.
     * @param text El texto del tooltip.
     * @param note Una nota adicional
     * @return tooltip El tooltip HTML generado.
     */
    public static String crearTooltipHTML(String title, String text, String note) {
        //evitar XSS
    	title = StringEscapeUtils.escapeHtml(title);
        text = StringEscapeUtils.escapeHtml(text);
        note = StringEscapeUtils.escapeHtml(note);
    	//
        
    	StringBuffer tooltip = new StringBuffer("<html><b>" + title + "</b><br>" + text);
        if(note != null) {
            tooltip.append("<br><i>" + note + "</i>");
        }
        return tooltip.append("</html>").toString();
    }

	/**
	 * Setea el tamaño <code>d</code> a los tamaños mínimo, máximo, y preferido
	 * del componente <code>c</code>.
	 * @param c
	 * @param d
	 */
	public static void setAllSizes(JComponent c, Dimension d) {
		c.setMinimumSize(d);
		c.setMaximumSize(d);
		c.setPreferredSize(d);
	}

    /**
     * Setea el <b>ícono</b> de un frame.
     * @param frame la ventana a la que se le setea el ícono.
     * @param icon el ícono a setear a la ventana.
     */
    public static void setFrameIcon(Frame frame, String icon) {
//    	frame.setIconImage(Toolkit.getDefaultToolkit().getImage(icon));
    	frame.setIconImage(new ImageIcon(FileUtil.getResource(icon)).getImage());
    }

    /**
     * Aplica el <b>look & feel</b> pasado por parámetro.
     * @param laf el look & feel.
     * @throws UnsupportedLookAndFeelException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws ClassNotFoundException 
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws SecurityException 
     * @throws IllegalArgumentException 
     */
    public static void setLookAndFeel(String laf) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
        UIManager.setLookAndFeel(laf);
        decorateFramesAndDialogs();
    }

    /**
     * Aplica el <b>tema</b> de SkinLF pasado por parámetro.
     * @param themepack El tema a aplicar.
     * @throws Exception
     */
    public static void setSkinLookAndFeelThemepack(String themepack) throws Exception {
    	SkinLookAndFeel.setSkin(SkinLookAndFeel.loadThemePack(FileUtil.getResource(themepack)));
    	UIManager.setLookAndFeel(new SkinLookAndFeel());
    	decorateFramesAndDialogs();
    }

    /**
     * Decora los frames y los dialogs 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     * @throws NoSuchMethodException 
     * @throws SecurityException
     */
    private static void decorateFramesAndDialogs() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
        java.lang.reflect.Method method = JFrame.class.getMethod("setDefaultLookAndFeelDecorated", new Class[] { boolean.class });
        method.invoke(null, new Object[] { Boolean.TRUE });
        method = JDialog.class.getMethod("setDefaultLookAndFeelDecorated", new Class[] { boolean.class });
        method.invoke(null, new Object[] { Boolean.TRUE });
    }

    /**
     * Cambia el <b>look & feel</b>.
     * @param f La ventana a la que se le cambia el look & feel.
     * @param lookAndFeel El look & feel.
     * @throws UnsupportedLookAndFeelException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws ClassNotFoundException 
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws SecurityException 
     * @throws IllegalArgumentException 
     */
    public static void changeLookAndFeel(Frame f, String lookAndFeel) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
    	setLookAndFeel(lookAndFeel);
    	refreshAllFrames(f);
    }

    /**
     * Cambia el <b>themepack</b> de SkinLF.
     * @param f La ventana a la que se le cambia el themepack.
     * @param themepack El nuevo themepack a aplicar.
     * @throws Exception
     */
    public static void changeSkinLookAndFeel(Frame f, String themepack) throws Exception {
    	setSkinLookAndFeelThemepack(themepack);
    	refreshAllFrames(f);
    }

    /**
     * Refresca el skin en todas las ventanas.
     * @param frame
     */
    public static void refreshAllFrames(Frame frame) {
    	Frame[] frames = Frame.getFrames();
    	for(Frame f : frames) {
    		SwingUtilities.updateComponentTreeUI(f);
    		Window[] windows = f.getOwnedWindows();
    		for(Window w : windows) {
    			SwingUtilities.updateComponentTreeUI(w);
    		}
    	}
    }

	/**
	 * Devuelve el textfield del spinner.
	 * @param spinner
	 * @return
	 */
	public static JFormattedTextField getSpinnerTextField(JSpinner spinner) {
		return ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField();
	}

    /**
     * Agrega una máscara de entrada al textfield pasado por parámetro.
     * @param tf El textfield que contendrá la máscara de entrada.
     * @param mask La máscara de entrada.
     * @throws ParseException
     */
	public static void agregarMascara(JFormattedTextField tf, String mask) throws ParseException {
		MaskFormatter formatter = new MaskFormatter(mask);
		formatter.install(tf);
	}

    /**
     * Agrega una máscara de entrada al textfield pasado por parámetro.
     * @param tf El textfield que contendrá la máscara de entrada.
     * @param mask La máscara de entrada.
     * @param placeholder
     * @throws ParseException
     */
	public static void agregarMascara(JFormattedTextField tf, String mask, char placeholder) throws ParseException {
		MaskFormatter formatter = new MaskFormatter(mask);
		formatter.setPlaceholderCharacter(placeholder);
		formatter.install(tf);
	}

	/**
	 * Crea un <b>tray icon</b> para el frame pasado por parámetro.
	 * @param frame El frame.
	 * @param icon El ícono que se mostrará en el área de notificaciones.
	 * @param descr La descripción que se mostrará cuando se pase el puntero del mouse por encima del ícono.
	 * @return trayIcon El ícono creado.
	 */
	public static TrayIcon createTrayIcon(final Frame frame, Icon icon, String descr) {
		TrayIcon trayIcon = null;
		if(MiscUtil.isWindowsXP()) {
			trayIcon = new TrayIcon(icon, descr);
			trayIcon.setIconAutoSize(true);
			trayIcon.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame.toBack();
				}
			});
			SystemTray.getDefaultSystemTray().addTrayIcon(trayIcon);
		}
		return trayIcon;
	}

	/**
	 * Devuelve el <b>frame</b> al que pertenece el componente pasado por parámetro.
	 * @param c El componente.
	 * @return
	 */
	public static Frame getFrameForComponent(Component c) {
		if(c instanceof Frame || c == null) {
			return c == null ? null : (Frame)c;
		}
		return getFrameForComponent(c.getParent());
	}

	/**
	 * Cambia la tipografía de todas las ventanas.
	 * @param font La nueva tipografía.
	 */
	public static void changeAllFonts(Font font) {
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		while(keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if(value instanceof javax.swing.plaf.FontUIResource) {
				UIManager.put(key, font);
			}
		}
	}

	/**
	 * Devuelve el <b>ButtonGroup</b> de un botón de radio.
	 * @param button El botón del que se obtiene el grupo.
	 * @return
	 */
	public static ButtonGroup getButtonGroup(AbstractButton button) {
		ButtonModel bm = button.getModel();
		if(bm instanceof DefaultButtonModel) {
			DefaultButtonModel dbm = (DefaultButtonModel)bm;
			return dbm.getGroup();
		}
		return null;
	}

	/**
	 * Muestra un <b>tooltip</b> con un texto para el componente pasados por parámetro.
	 * Luego de mostrar el tooltip con el texto pasado por parámetro se restablece el tooltip con
	 * el texto original.
	 * @param c El componente que mostrará el tooltip.
	 * @param text El texto del tooltip.
	 */
	public static void showTooltipText(JComponent c, String text) {
		showTooltipText(c, text, true);
	}

	/**
	 * Muestra un <b>tooltip</b> con un texto para el componente pasados por parámetro.
	 * Luego de mostrar el tooltip con el texto pasado por parámetro se restablece el tooltip con
	 * el texto original.
	 * @param c El componente que mostrará el tooltip.
	 * @param text El texto del tooltip.
	 * @param beep Si es <b>true</b> reproduce una alerta sonora.
	 */
	public static void showTooltipText(JComponent c, String text, boolean beep) {
		String oldText = c.getToolTipText();
		c.setToolTipText(text);
		if(beep) {
			Toolkit.getDefaultToolkit().beep();
		}
		c.dispatchEvent(new KeyEvent(c, KeyEvent.KEY_PRESSED, 0, KeyEvent.CTRL_MASK, KeyEvent.VK_F1, KeyEvent.CHAR_UNDEFINED));
		c.setToolTipText(oldText);
	}

	/**
	 * Registra el listener pasado por parámetro a la tecla ESC en el diálogo.
	 * @param dialog
	 * @param listener
	 */
	public static void addEscKeyAction(JDialog dialog, ActionListener listener) {
		dialog.getRootPane().registerKeyboardAction(listener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}

	/**
	 * Setea el texto de un text area dejando visible el comienzo del texto.
	 * @param textArea
	 * @param text
	 */
	public static void setText(JTextArea textArea, String text) {
		textArea.setText(text) ;
		textArea.setCaretPosition(0) ;
	}
}
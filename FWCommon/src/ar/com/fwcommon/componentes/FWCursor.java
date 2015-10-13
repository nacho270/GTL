package ar.com.fwcommon.componentes;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Window;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

public class FWCursor {

	private static int waiting = 0;
	private static Window window;

	public static void startWait(Component componentInWindow) {
		if(componentInWindow instanceof RootPaneContainer) {
			//Esto soluciona parte del problema Ej: que funcione el cursor wait en un text field
			RootPaneContainer pane = (RootPaneContainer)componentInWindow;
			pane.getGlassPane().setVisible(true);
			pane.getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			//Y desde aqui es para otra parte del problema Ej: que funcione el cursor wait en un combo box
			//Window window = getEnclosingWindow((RootPaneContainer)componentInWindow);
			Window window = getWindow();
			if(window == null) {
				return;
			}
			waiting++;
			//Only wait if we are not already
			if(waiting == 1) {
				window.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			}
		} else {
			componentInWindow.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			//Y desde aqui es para otra parte del problema Ej: que funcione el cursor wait en un combo box
			//Window window = getEnclosingWindow(componentInWindow);
			Window window = getWindow();
			if(window == null) {
				return;
			}
			waiting++;
			//Only wait if we are not already
			if(waiting == 1) {
				window.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			}
		}
	}

	public static void endWait(Component componentInWindow) {
		if(componentInWindow instanceof RootPaneContainer) {
			//Esto soluciona parte del problema Ej: que funcione el cursor wait en un text field
			RootPaneContainer pane = (RootPaneContainer)componentInWindow;
			pane.getGlassPane().setVisible(false);
			//Y desde aqui es para otra parte del problema Ej: que funcione el cursor wait en un combo box
			//Window window = getEnclosingWindow(componentInWindow);
			Window window = getWindow();
			if(window == null) {
				return;
			}
			if(waiting > 0) {
				waiting--;
				//Only stop when all waiting is done
				if(waiting == 0) {
					window.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		} else {
			componentInWindow.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			//Y desde aqui es para otra parte del problema Ej: que funcione el cursor wait en un combo box
			//Window window = getEnclosingWindow(componentInWindow);
			Window window = getWindow();
			if(window == null) {
				return;
			}
			if(waiting > 0) {
				waiting--;
				//Only stop when all waiting is done
				if(waiting == 0) {
					window.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		}
	}

//	public static void fullEndWait(Window window) {
//		if (waiting > 0) {
//			waiting = 1;
//			endWait(window);
//		}
//	}

	public static Window getEnclosingWindow(RootPaneContainer componentInWindow) {
		if(componentInWindow instanceof Window) {
			return (Window)componentInWindow;
		} else if(componentInWindow != null) {
			return SwingUtilities.windowForComponent((Component)componentInWindow);
		} else {
			return null;
		}
	}

	public static Window getEnclosingWindow(Component componentInWindow) {
		if(componentInWindow instanceof Window) {
			return (Window)componentInWindow;
		} else if(componentInWindow != null) {
			return SwingUtilities.windowForComponent((Component)componentInWindow);
		} else {
			return null;
		}
	}

	public static Window getWindow() {
		return window;
	}

	public static void setWindow(Window window) {
		FWCursor.window = window;
	}

}
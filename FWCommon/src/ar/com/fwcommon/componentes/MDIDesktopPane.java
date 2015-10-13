package ar.com.fwcommon.componentes;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.beans.PropertyVetoException;
import javax.swing.DefaultDesktopManager;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

public class MDIDesktopPane extends JDesktopPane {

	private static final long serialVersionUID = 4995066473471272385L;
	private MDIDesktopManager manager;
	private static final int FRAME_OFFSET = 20;

	public MDIDesktopPane() {
		manager = new MDIDesktopManager(this);
		setDesktopManager(manager);
		setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
	}

	public void setBounds(int x, int y, int w, int h) {
		super.setBounds(x, y, w, h);
		checkDesktopSize();
	}

	public Component add(JInternalFrame frame) {
		JInternalFrame[] frames = getAllFrames();
		Component c = super.add(frame);
		checkDesktopSize();
		Point p;
		if(frames.length > 0) {
			p = frames[0].getLocation();
			p.x = p.x + FRAME_OFFSET;
			p.y = p.y + FRAME_OFFSET;
		} else {
			p = new Point(0, 0);
		}
		frame.setLocation(p.x, p.y);
		moveToFront(frame);
		frame.setVisible(true);
		try {
			frame.setSelected(true);
		} catch(PropertyVetoException e) {
			frame.toBack();
		}
		return c;
	}

	public void remove(Component c) {
		super.remove(c);
		checkDesktopSize();
	}

	/** Cascade all internal frames uniconfying any minimized first */
	public void cascadeFrames() {
		restoreAllFrames();
		JInternalFrame frames[] = getAllFrames();
		manager.setNormalSize();
		int frameHeight = (getBounds().height - 5) - frames.length * FRAME_OFFSET;
		int frameWidth = (getBounds().width - 5) - frames.length * FRAME_OFFSET;
		int x = 0;
		int y = 0;
		for(int i = frames.length - 1; i >= 0; i--) {
			frames[i].setSize(frameWidth, frameHeight);
			frames[i].setLocation(x, y);
			x = x + FRAME_OFFSET;
			y = y + FRAME_OFFSET;
		}
	}

	/** Tile all internal frames uniconifying any minimized first */
	public void tileFrames() {
		restoreAllFrames();
		Component frames[] = getAllFrames();
		manager.setNormalSize();
		int frameHeight = getBounds().height / frames.length;
		int y = 0;
		for(Component frame : frames) {
			frame.setSize(getBounds().width, frameHeight);
			frame.setLocation(0, y);
			y = y + frameHeight;
		}
	}

	public void minimizeAllFrames() {
		JInternalFrame[] frames = getAllFrames();
		for(JInternalFrame frame : frames) {
			try {
				frame.setIcon(true);
			} catch(PropertyVetoException e) {
			}
		}
	}

	public void restoreAllFrames() {
		JInternalFrame[] frames = getAllFrames();
		for(JInternalFrame frame : frames) {
			try {
				frame.setIcon(false);
			} catch(PropertyVetoException e) {
			}
		}
	}

	public void closeAllFrames() {
		JInternalFrame[] frames = getAllFrames();
		for(JInternalFrame frame : frames) {
			frame.dispose();
		}
	}

	public int deiconifiedFrames() {
		int count = 0;
		JInternalFrame[] frames = getAllFrames();
		for(JInternalFrame frame : frames) {
			if(!frame.isIcon()) {
				count++;
			}
		}
		return count;
	}

	public int iconifiedFrames() {
		int count = 0;
		JInternalFrame[] frames = getAllFrames();
		for(JInternalFrame frame : frames) {
			if(frame.isIcon()) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Sets all component size properties (maximum/minimum/preferred)
	 * to the given dimension.
	 */
	public void setAllSize(Dimension d) {
		setMinimumSize(d);
		setMaximumSize(d);
		setPreferredSize(d);
	}

	/**
	 * Sets all component size properties (maximum/minimum/preferred)
	 * to the given width and height.
	 */
	public void setAllSize(int width, int height) {
		setAllSize(new Dimension(width, height));
	}

	private void checkDesktopSize() {
		if(getParent() != null && isVisible()) {
			manager.resizeDesktop();
		}
	}

	/**
	 * Private class used to replace the standard DesktopManager for JDesktopPane.
	 * Used to provide scrollbar functionality.
	 */
	class MDIDesktopManager extends DefaultDesktopManager {
		static final long serialVersionUID = -6283429494666829046L;
		MDIDesktopPane desktop;

		public MDIDesktopManager(MDIDesktopPane desktop) {
			this.desktop = desktop;
		}

		public void endResizingFrame(JComponent c) {
			super.endResizingFrame(c);
			resizeDesktop();
		}

		public void endDraggingFrame(JComponent c) {
			super.endDraggingFrame(c);
			resizeDesktop();
		}

		public void setNormalSize() {
			JScrollPane scrollpane = getScrollPane();
			int x = 0;
			int y = 0;
			Insets spInsets = getScrollPaneInsets();
			if(scrollpane != null) {
				Dimension d = scrollpane.getVisibleRect().getSize();
				if(scrollpane.getBorder() != null) {
					d.setSize(d.getWidth() - spInsets.left - spInsets.right, d.getHeight() - spInsets.top - spInsets.bottom);
				}
				d.setSize(d.getWidth() - 20, d.getHeight() - 20);
				desktop.setAllSize(x, y);
				scrollpane.revalidate();
			}
		}

		private Insets getScrollPaneInsets() {
			JScrollPane scrollpane = getScrollPane();
			if(scrollpane == null) {
				return new Insets(0, 0, 0, 0);
			} else {
				return getScrollPane().getBorder().getBorderInsets(scrollpane);
			}
		}

		private JScrollPane getScrollPane() {
			if(desktop.getParent() instanceof JViewport) {
				JViewport viewport = (JViewport)desktop.getParent();
				if(viewport.getParent() instanceof JScrollPane) {
					return (JScrollPane)viewport.getParent();
				}
			}
			return null;
		}

		protected void resizeDesktop() {
			int x = 0;
			int y = 0;
			JScrollPane scrollpane = getScrollPane();
			Insets spInsets = getScrollPaneInsets();
			if(scrollpane != null) {
				JInternalFrame frames[] = desktop.getAllFrames();
				for(JInternalFrame frame : frames) {
					if(frame.getX() + frame.getWidth() > x) {
						x = frame.getX() + frame.getWidth();
					}
					if(frame.getY() + frame.getHeight() > y) {
						y = frame.getY() + frame.getHeight();
					}
				}
				Dimension d = scrollpane.getVisibleRect().getSize();
				if(scrollpane.getBorder() != null) {
					d.setSize(d.getWidth() - spInsets.left - spInsets.right, d.getHeight() - spInsets.top - spInsets.bottom);
				}
				if(x <= d.getWidth()) {
					x = ((int)d.getWidth()) - 20;
				}
				if(y <= d.getHeight()) {
					y = ((int)d.getHeight()) - 20;
				}
				desktop.setAllSize(x, y);
				scrollpane.revalidate();
			}
		}
	}

}
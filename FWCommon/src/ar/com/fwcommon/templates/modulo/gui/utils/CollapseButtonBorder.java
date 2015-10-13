package ar.com.fwcommon.templates.modulo.gui.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.EventListenerList;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

/**
 * Borde de que tiene el botón de expandir y contraer un frame
 * 
 */
class CollapseButtonBorder implements Border {

	private static final String BUTTON_BORDER_COLOR = "CollapseButtonBorder.color";
	private static final String BACKGROUND_OVER_COLOR = "CollapseButtonBorder.backgroundOverColor";
	private final EventListenerList listeners = new EventListenerList();
	static {
		if(UIManager.getColor(BUTTON_BORDER_COLOR) == null) {
			UIManager.put(BUTTON_BORDER_COLOR, new Color(184, 207, 229));
		}
		if(UIManager.getColor(BACKGROUND_OVER_COLOR) == null) {
			UIManager.put(BACKGROUND_OVER_COLOR, Color.WHITE);
		}
	}
	private Border border;
	private ButtonIcon icon;
	private Rectangle iconBounds;
	private Color background = null;
	private boolean listenerInstaled = false;

	public CollapseButtonBorder(Border border) {
		super();
		this.iconBounds = new Rectangle();
		this.border = border;
		this.icon = new ButtonIcon();
		this.iconBounds.height = icon.getIconHeight();
		this.iconBounds.width = icon.getIconWidth();
	}

	public void addActionListener(ActionListener l) {
		listeners.add(ActionListener.class, l);
	}

	public void removeActionListener(ActionListener l) {
		listeners.remove(ActionListener.class, l);
	}

	public Border getBorder() {
		return border;
	}

	public void setBorder(Border border) {
		this.border = border;
	}

	public ButtonIcon getIcon() {
		return icon;
	}

	protected final void fireActionListener() {
		final ActionListener[] l = listeners.getListeners(ActionListener.class);
		final ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for(int i = 0; i < l.length; i++) {
					try {
						l[i].actionPerformed(e);
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private MouseInputListener mouseInputListener;

	private MouseInputListener getMouseInputListener() {
		if(mouseInputListener == null) {
			mouseInputListener = new MouseInputAdapter() {
				@Override
				public void mouseExited(MouseEvent e) {
					background = null;
					((Component)e.getSource()).repaint();
				}

				@Override
				public void mouseMoved(MouseEvent e) {
					if(background == null && iconBounds.contains(e.getX(), e.getY())) {
						background = UIManager.getColor(BACKGROUND_OVER_COLOR);
						((Component)e.getSource()).repaint();
					} else if(background != null && !iconBounds.contains(e.getX(), e.getY())) {
						background = null;
						((Component)e.getSource()).repaint();
					}
				}

				public void mouseClicked(MouseEvent e) {
					if(e.getButton() == MouseEvent.BUTTON1 && iconBounds.contains(e.getX(), e.getY())) {
						icon.setUp(!icon.isUp());
						fireActionListener();
						((Component)e.getSource()).repaint();
					}
				}
			};
		}
		return mouseInputListener;
	}

	private synchronized void checkAndInstallMouseListener(Component c) {
		if(!listenerInstaled) {
			listenerInstaled = true;
			c.addMouseListener(getMouseInputListener());
			c.addMouseMotionListener(getMouseInputListener());
		}
	}

	public Insets getBorderInsets(Component c) {
		Insets insets = border.getBorderInsets(c);
		insets = (Insets)insets.clone();
		insets.top = Math.max(insets.top, icon.getIconHeight() + 2);
		return insets;
	}

	public boolean isBorderOpaque() {
		return border.isBorderOpaque();
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		checkAndInstallMouseListener(c);
		Insets insets = getBorderInsets(c);
		Insets bInsets = border.getBorderInsets(c);
		border.paintBorder(c, g, x, y + (insets.top - bInsets.top) / 2, width, height);
		this.iconBounds.x = x + width - insets.right - 4 - icon.getIconWidth();
		this.iconBounds.y = y + (insets.top - icon.getIconHeight()) / 2;
		icon.paintIcon(c, g, this.iconBounds.x, this.iconBounds.y);
	}

	/**
	 * Icono con de la flecha de expasion y contracción del frame
	 * 
	 */
	final class ButtonIcon implements Icon {
		private boolean up = false;

		public ButtonIcon() {
			super();
		}

		public boolean isUp() {
			return up;
		}

		public void setUp(boolean up) {
			this.up = up;
		}

		public int getIconHeight() {
			return 17;
		}

		public int getIconWidth() {
			return 17;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D g2d = (Graphics2D)g;
			g2d.setColor((background == null) ? c.getBackground() : background);
			g2d.fillOval(x, y, 16, 16);
			g2d.setColor(UIManager.getColor(BUTTON_BORDER_COLOR));
			g2d.drawOval(x, y, 16, 16);
			g2d.setColor(c.getForeground());
			g2d.setStroke(new BasicStroke(1));
			if(isUp()) {
				g2d.drawLine(x + 5, y + 7, x + 8, y + 4);
				g2d.drawLine(x + 5, y + 11, x + 8, y + 8);
				g2d.drawLine(x + 8, y + 4, x + 11, y + 7);
				g2d.drawLine(x + 8, y + 8, x + 11, y + 11);
			} else {
				g2d.drawLine(x + 5, y + 5, x + 8, y + 8);
				g2d.drawLine(x + 5, y + 9, x + 8, y + 12);
				g2d.drawLine(x + 8, y + 8, x + 11, y + 5);
				g2d.drawLine(x + 8, y + 12, x + 11, y + 9);
			}
		}
	}

}
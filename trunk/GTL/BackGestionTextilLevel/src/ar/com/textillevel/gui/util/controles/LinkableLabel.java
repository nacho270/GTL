package ar.com.textillevel.gui.util.controles;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

public abstract class LinkableLabel extends JLabel {

	private static final long serialVersionUID = -3653601234798922238L;

	private String texto;
	private String color;
	private String colorHover;
	
	public LinkableLabel() {

	}

	public LinkableLabel(String text) {
		this(text,"#000099","#ff0000");
	}
	
	public LinkableLabel(String text, String hexaColor, String colorHover) {
		super("<html><font color='"+hexaColor+"'><u>" + text + "</u></font></html>");
		this.color = hexaColor;
		this.colorHover = colorHover;
		setTexto(text);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				if(isEnabled()){
					labelClickeada(e);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				setText("<html><font color='"+getColorHover()+"'><u>" + getTexto() + "</u></font></html>");
				repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setText("<html><font color='"+getColor()+"'><u>" + getTexto() + "</u></font></html>");
				repaint();
			}
		});
	}

	protected void refreshLabel() {
		setText("<html><font color='"+getColor()+"'><u>" + getTexto() + "</u></font></html>");
		repaint();
	}

	public abstract void labelClickeada(MouseEvent e);

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getColorHover() {
		return colorHover;
	}

	public void setColorHover(String colorHover) {
		this.colorHover = colorHover;
	}
}

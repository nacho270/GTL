package ar.com.textillevel.skin;

import java.awt.Color;
import java.awt.Font;
import javax.swing.UIManager;
import ar.clarin.fwjava.componentes.CLCalendar;
import ar.clarin.fwjava.templates.main.skin.ISkinDecorator;

public class SkinDecoratorModerno implements ISkinDecorator {

	//Imágenes
	private static final String ICONO_VENTANA = "ar/clarin/fwsgp/imagenes/ico_logo_trans.png";
	private static final String ICONO_LOGIN = "ar/clarin/fwsgp/imagenes/logo_login_trans.png";
	//Colores
	private static final Color COLOR_AZUL = new Color(40, 100, 180);
	private static final Color COLOR_VERDE = new Color(98, 158, 98);
	private static final Color COLOR_CELESTE_FONDO_VENTANA = new Color(224, 229, 233);
	private static final Color COLOR_CELESTE_FONDO_PANEL = new Color(209, 214, 223);
	private static final Color COLOR_CELESTE_BOTON_ROLLOVER = new Color(118, 151, 220);
	private static final Color COLOR_GRIS_BARRA_TITULO = new Color(119, 126, 138);
	private static final Color COLOR_GRIS_HEADER_TABLA = new Color(196, 202, 210);

	public void init() {
		//Button
		UIManager.put("Button.foreground", Color.WHITE);
		//TitledBorder
		UIManager.put("TitledBorder.titleColor", COLOR_AZUL);
		//TabbedPane
		UIManager.put("TabbedPane.foreground", Color.WHITE);
		//TaskPane
		UIManager.put("TaskPane.useGradient", Boolean.TRUE);
		UIManager.put("TaskPane.backgroundGradientStart", COLOR_AZUL);
		UIManager.put("TaskPane.backgroundGradientEnd", COLOR_AZUL);
		//Calendario
        CLCalendar.weekDaysForeground = COLOR_AZUL;
	}

	public Font getDefaultFont() {
		return new Font("Tahoma", Font.PLAIN, 11);
	}

	public Font getSecondaryFont() {
		return new Font("Tahoma", Font.PLAIN, 11);
	}

	public String getIconoVentana() {
		return ICONO_VENTANA;
	}

	public String getIconoLogin() {
		return ICONO_LOGIN;
	}

	public Color getColorBarraTituloVentana() {
		return COLOR_AZUL;
	}

	public Color getColorFondoVentana() {
		return COLOR_CELESTE_FONDO_VENTANA;
	}

	public Color getColorFondoPanel() {
		return COLOR_CELESTE_FONDO_PANEL;
	}

	public Color getColorComponenteNormal() {
		return COLOR_AZUL;
	}

	public Color getColorComponenteRollover() {
		return COLOR_CELESTE_BOTON_ROLLOVER;
	}

	public Color getColorComponenteDeshabilitado() {
		return COLOR_GRIS_BARRA_TITULO;
	}

	public Color getColorComponenteSeleccionado() {
		return COLOR_VERDE;
	}

	public Color getColorHeaderTabla() {
		return COLOR_GRIS_HEADER_TABLA;
	}

	public Color getColorItemNormal() {
		return Color.BLACK;
	}

	public Color getColorItemResaltado() {
		return COLOR_AZUL;
	}

	public Color getColorCajaTexto() {
		return Color.WHITE;
	}

}
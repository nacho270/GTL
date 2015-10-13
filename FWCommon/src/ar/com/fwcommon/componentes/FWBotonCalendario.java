package ar.com.fwcommon.componentes;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

import javax.swing.Icon;
import javax.swing.JButton;

import ar.com.fwcommon.util.DecorateUtil;
import ar.com.fwcommon.util.ImageUtil;

/**
 * Componente que muestra un botón que al ser presionado muestra un CLPopupCalendar.
 * ar.com.fwcommon.componentes.CLPopupCalendar
 */
public class FWBotonCalendario extends JButton {

	private static final long serialVersionUID = 8500398313752041799L;

	public static String iconoCalendario = null;
	public static String iconoCalendarioDeshab = null;
	private FWPopupCalendar calendario;
	private static final String DEFAULT_ICONO_CALENDARIO = "ar/com/fwcommon/imagenes/b_calendario.png";
	private static final String DEFAULT_ICONO_CALENDARIO_DESHAB = "ar/com/fwcommon/imagenes/b_calendario_des.png";
	private static final String DEFAULT_TITLE = "Calendario";
	public final int DEFAULT_BUTTON_WIDTH = 20;
	public final int DEFAULT_BUTTON_HEIGHT = 20;

	/** Método constructor */
	public FWBotonCalendario() {
		super();
		construct();
		calendario = new FWPopupCalendar(null, DEFAULT_TITLE);
		getCalendario().select();
	}

	public FWBotonCalendario(Date fechaDefaultSeleccionada) {
		super();
		construct();
		calendario = new FWPopupCalendar(null, DEFAULT_TITLE, fechaDefaultSeleccionada);
		//getCalendario().select();Importante: No descomentar. Esto selecciona la fecha de hoy, justo lo que no quiero hacer.
	}

	public FWBotonCalendario(Frame owner) {
		super();
		construct();
		calendario = new FWPopupCalendar(owner, DEFAULT_TITLE);
		getCalendario().select();
	}

	public FWBotonCalendario(Frame owner, Date fechaMinima, Date fechaMaxima) {
		super();
		construct();
		calendario = new FWPopupCalendar(owner, DEFAULT_TITLE, fechaMinima, fechaMaxima);
		getCalendario().select();
	}

	public FWBotonCalendario(Date fechaMinima, Date fechaMaxima) {
		super();
		construct();
		calendario = new FWPopupCalendar(null, DEFAULT_TITLE, fechaMinima, fechaMaxima);
		getCalendario().select();
	}

	/**
	 * Método constructor.
	 * @param iconoCalendario El icono del botón.
	 * @param iconoCalendarioDeshab El icono del botón en estado deshabilitado.
	 */
	public FWBotonCalendario(String iconoCalendario, String iconoCalendarioDeshab) {
		this();
		setIconoCalendario(iconoCalendario);
		setIconoCalendarioDeshabilitado(iconoCalendarioDeshab);
	}

	/**
	 * Método constructor.
	 * @param calendario El popup calendar.
	 */
	public FWBotonCalendario(FWPopupCalendar calendario) {
		super();
		construct();
		this.calendario = calendario;
		getCalendario().select();
	}

	//Construye gráficamente el componente
	private void construct() {
		String icono = (iconoCalendario == null ? DEFAULT_ICONO_CALENDARIO : iconoCalendario);
		String iconoDeshab = (iconoCalendarioDeshab == null ? DEFAULT_ICONO_CALENDARIO_DESHAB : iconoCalendarioDeshab);
		DecorateUtil.decorateButton(this, icono, iconoDeshab);
		setPreferredSize(new Dimension(DEFAULT_BUTTON_WIDTH, DEFAULT_BUTTON_HEIGHT));
		setToolTipText("Mostrar calendario");
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				calendario.setVisible(true);
				if(!calendario.isCanceled()) {
					botonCalendarioPresionado();
				}
			}
		});
	}

	/**
	 * Devuelve el calendario asociado al cuadro de diálogo.
	 * @return Una instancia de CLCalendar.
	 * ar.com.fwcommon.componentes.CLCalendar
	 */
	public FWCalendar getCalendario() {
		return calendario.getCalendar();
	}

	/**
	 * Devuelve el icono del botón.
	 * @return iconoCalendario
	 */
	public String getIconoCalendario() {
		return iconoCalendario;
	}

	/**
	 * Setea el icono del botón.
	 * @param iconoCalendario
	 */
	public void setIconoCalendario(String iconoCalendario) {
		FWBotonCalendario.iconoCalendario = iconoCalendario;
		Icon icon = ImageUtil.loadIcon(iconoCalendario);
		super.setIcon(icon);
		super.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
	}

	/**
	 * Devuelve el icono del botón en estado deshabilitado.
	 * @return iconoCalendarioDeshab
	 */
	public String getIconoCalendarioDeshabilitado() {
		return iconoCalendarioDeshab;
	}

	/**
	 * Setea el icono del botón en estado deshabilitado.
	 * @param iconoCalendarioDeshab
	 */
	public void setIconoCalendarioDeshabilitado(String iconoCalendarioDeshab) {
		FWBotonCalendario.iconoCalendarioDeshab = iconoCalendarioDeshab;
		super.setDisabledIcon(ImageUtil.loadIcon(iconoCalendarioDeshab));
		super.setIcon(ImageUtil.loadIcon(iconoCalendarioDeshab));
	}

	/**
	 * Método invocado cuando se produce el evento de click del botón.
	 * Método vacío para ser sobreescrito.
	 */
	public void botonCalendarioPresionado() {
	}

}
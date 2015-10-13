package ar.com.fwcommon.componentes;

import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

import ar.com.fwcommon.util.DateUtil;

/**
 * Componente que representa una casilla de texto para el ingreso de fechas.
 * Valida que la fecha ingresada sea una fecha válida.
 */
public class FWDateField extends JFormattedTextField {

	private static final long serialVersionUID = 5266294999542465756L;

	private DateMaskFormatter formatter;
	private DateFormat dateFormat;
	private static final String DEFAULT_DATE_MASK = "##/##/####";
	private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

	/** Método constructor */
	public FWDateField() {
		this(DEFAULT_DATE_MASK, DEFAULT_DATE_FORMAT);
	}

	/**
	 * Método constructor.
	 * @param fecha La fecha inicial.
	 */
	public FWDateField(Date fecha) {
		this(DEFAULT_DATE_MASK, DEFAULT_DATE_FORMAT);
		setFecha(fecha);
	}

	/**
	 * Método constructor.
	 * @param mask La máscara de entrada.
	 * @param format El formato de la fecha.
	 */
	public FWDateField(String mask, String format) {
		this(mask, format, false);
	}

	/**
	 * Método constructor.
	 * @param mask La máscara de entrada.
	 * @param format El formato de la fecha.
	 */
	public FWDateField(String mask, String format, boolean usarDateFormatter) {
		formatter = new DateMaskFormatter(mask);
		formatter.install(this);
//		dateFormat = new SimpleDateFormat(format);
		dateFormat = DateUtil.getSimpleDateFormat(format);
		dateFormat.setLenient(false);
		if(usarDateFormatter) {
			DateFormatter dateFormatter = new DateFormatter(this.dateFormat);
			DefaultFormatterFactory dff = new DefaultFormatterFactory(dateFormatter);
			this.setFormatterFactory(dff);
		}
		construct();
	}

	/**
	 * Solo aplica formato, no mascara. Son validos setValue y getValue.
	 * Método constructor.
	 * @param format El formato de la fecha.
	 */
	public FWDateField(boolean usarDateFormatter) {
		this(DEFAULT_DATE_MASK, DEFAULT_DATE_FORMAT, usarDateFormatter);
	}

	/**
	 * Cambia dinámicamente la máscara de entrada y el formato de la fecha.
	 * @param mask La máscara de entrada.
	 * @param format El formato de la fecha.
	 */
	public void changeMaskAndFormat(String mask, String format) {
		Date fecha = getFecha();
		setFecha(null);
		formatter.uninstall();
		formatter = new DateMaskFormatter(mask);
		formatter.install(this);
//		dateFormat = new SimpleDateFormat(format);
		dateFormat = DateUtil.getSimpleDateFormat(format);
		dateFormat.setLenient(false);
//		setColumns(formatter.getMask().length());
		setFecha(fecha);
	}

	private void construct() {
		setColumns(formatter.getMask().length());
		setFocusLostBehavior(JFormattedTextField.COMMIT);
		addKeyListener(new DateFieldKeyListener());
		addFocusListener(new DateFieldFocusListener());
	}

	public void commit() {
	}

	public void invalid() {
	}

	/**
	 * Devuelve la fecha ingresada. Si la fecha es inválida retorna <b>null</b>.
	 * @return La fecha ingresada o null.
	 */
	public Date getFecha() {
		try {
			Date fecha = new Date(dateFormat.parse(getText()).getTime());
			return fecha;
		} catch(ParseException e) {
			return null;
		}
	}

	/**
	 * Setea la fecha.
	 * @param fecha La fecha a setear.
	 */
	public void setFecha(Date fecha) {
		setText(fecha == null ? "" : dateFormat.format(fecha));
	}

	/**
	 * @return <b>true</b> si el textfield está vacío.
	 */
	public boolean isBlank() {
		return getText().equals(blankValue());
	}

	private String blankValue() {
		String mask = formatter.getMask();
		return mask.replace('#', ' ');
	}

	class DateMaskFormatter extends MaskFormatter {
		private static final long serialVersionUID = 1L;

		public DateMaskFormatter(String mask) {
			super();
			try {
				setMask(mask);
			} catch(ParseException e) {
				e.printStackTrace();
			}
		}
	}

	class DateFieldKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent evt) {
			if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
				if(getFecha() == null && !getText().equals(blankValue())) {
					Toolkit.getDefaultToolkit().beep();
					selectAll();
					invalid();
				} else {
					commit();
				}
			}
		}
	}

	class DateFieldFocusListener extends FocusAdapter {
		public void focusGained(FocusEvent evt) {
			setCaretPosition(0);
		}
	}
}
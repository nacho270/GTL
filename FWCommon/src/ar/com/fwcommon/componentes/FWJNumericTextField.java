package ar.com.fwcommon.componentes;

import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class FWJNumericTextField extends JTextField {

	private static final long serialVersionUID = 1L;
	private FWJNumberDocument numberDocument;
	private Long maximo = null;
	private Long minimo = null;
	private NumberFormat integerFormatter;
	private String formatoInteger = "#";
	private Toolkit toolkit;
	
	public FWJNumericTextField() {
		super();
		toolkit = Toolkit.getDefaultToolkit();
		integerFormatter = new DecimalFormat(formatoInteger);
		integerFormatter.setParseIntegerOnly(true);
		super.addFocusListener(new NumericFocusListener());
	}

	public FWJNumericTextField(long minimo, long maximo) {
		this();
		setMinimo(minimo);
		setMaximo(maximo);
		super.addFocusListener(new NumericFocusListener());
	}

	public FWJNumericTextField(long minimo, long maximo, long valor) {
		this(minimo, maximo);
		setValue(valor);
		super.addFocusListener(new NumericFocusListener());
	}

	/**
	 * Establece la cantidad máxima posible de caracteres en el componente.
	 * @param maxlength
	 */
	public void setMaxLength(int maxlength) {
		((AbstractDocument)getDocument()).setDocumentFilter(new FixedSizeFilter(maxlength));
	}
	
	/**
	 * Retorna el valor mínimo que puede ingresarse.
	 * @return
	 */
	public Long getMinimo() {
		return minimo;
	}

	/**
	 * Setea el valor mínimo que puede ingresarse.
	 * @param minimo
	 */
	public void setMinimo(long minimo) {
		this.minimo = minimo;
	}

	/**
	 * Retorna el valor máximo que puede ingresarse.
	 * @return
	 */
	public Long getMaximo() {
		return maximo;
	}

	/**
	 * Setea el valor máximo que puede ingresarse.
	 * @param maximo
	 */
	public void setMaximo(long maximo) {
		this.maximo = maximo;
	}

	/**
	 * Setea el formato a aplicar al valor ingresado.
	 * @param formato
	 */
	public void setFormato(String formato) {
		integerFormatter = new DecimalFormat(formato);
	}

	/**
	 * Retorna el formato a aplicar al valor ingresado.
	 * @return
	 */
	public String getFormato() {
		return formatoInteger;
	}

	/**
	 * @return El valor ingresado, o cero si no se puede parsear el valor.
	 */
	public Integer getValue() {
		try {
			return new Integer(getText());
		} catch(NumberFormatException e) {
			return new Integer(0);
		}
	}
	
	/**
	 * @return El valor ingresado, o cero si no se puede parsear el valor.
	 */
	public Long getLongValue() {
		try {
			return new Long(getText());
		} catch(NumberFormatException e) {
			return new Long(0);
		}
	}

	/**
	 * Setea un valor al componente.
	 * @param valor
	 */
	public void setValue(Long valor) {
		numberDocument.setTextoComponente("");
		if(valor != null) {
			setText(integerFormatter.format(valor));
		} else {
			setText("");
		}
	}

	protected Document createDefaultModel() {
		numberDocument = new FWJNumberDocument();
		addKeyListener(new ListenerCLJNumericTextField(numberDocument));
		return numberDocument;
	}

	private class FWJNumberDocument extends PlainDocument {
		private static final long serialVersionUID = 1L;
		private String textoComponente = "";

		public void setTextoComponente(String textoComponente) {
			this.textoComponente = textoComponente;
		}

		public String getTextoComponente() {
			return textoComponente;
		}

		public void insertString(int posicion, String insercion, AttributeSet atributos) throws BadLocationException {
			boolean insercionValida = true;
			char[] charsInsercion = insercion.toCharArray();
			char[] resultado = new char[charsInsercion.length];
			Number valor = null;
			int j = 0;
			for(int i = 0; i < resultado.length; i++) {
				if(Character.isDigit(charsInsercion[i]) || ((posicion == 0) && (charsInsercion[i] == '-') && (minimo == null || minimo < 0))) {
					resultado[j++] = charsInsercion[i];
				} else {
					insercionValida = false;
					break;
				}
			}
			textoComponente += insercion;
			if(insercionValida) {
				try {
					if(textoComponente.trim().compareTo("-") != 0) {
						valor = integerFormatter.parse(textoComponente);
						if(valor instanceof Long) {
							valor = new Integer(((Long)valor).intValue());
						}
					}
				} catch(ParseException e) {
					e.printStackTrace();
					insercionValida = false;
				}
				if(valor != null) {
					if(maximo != null) {
						if(((Comparable<Long>)valor.longValue()).compareTo(maximo) > 0) {
							insercionValida = false;
							j--;
						}
					}
					if(minimo != null && insercionValida) {
						 if(((Comparable<Long>)valor.longValue()).compareTo(minimo) < 0) { insercionValida = false; j--; }
					}
				}
			}
			if(insercionValida)
				super.insertString(posicion, new String(resultado, 0, j), atributos);
			else {
				textoComponente = new String(textoComponente.substring(0, textoComponente.length() - insercion.length()));
				toolkit.beep();
			}
		}

		@Override
		public void replace(int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
			writeLock();
			try {
				if (length > 0) {
					remove(offset, length);
				}
				if (text != null && text.length() > 0) {
					insertString(offset, text, attrs);
				}
			} finally {
				writeUnlock();
			}
		}

	}

	private class ListenerCLJNumericTextField implements KeyListener {
		FWJNumberDocument numberDocument;

		public ListenerCLJNumericTextField(FWJNumberDocument numberDocument) {
			this.numberDocument = numberDocument;
		}

		public void keyTyped(KeyEvent evt) {
		}

		public void keyPressed(KeyEvent evt) {
			String textoSeleccionado = getSelectedText();
			if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE || evt.getKeyCode() == KeyEvent.VK_DELETE
					|| (textoSeleccionado != null && textoSeleccionado.compareTo(getText()) == 0)) {
				if(textoSeleccionado == null || textoSeleccionado.length() == 0) {
					if(getText().length() == 1) {
						numberDocument.setTextoComponente("");
					} else if(getText().length() > 1) {
						int hasta = numberDocument.getTextoComponente().length() - 1;
						if(hasta >= 0) {
							numberDocument.setTextoComponente(numberDocument.getTextoComponente().substring(0, hasta));
						}
					}
				} else {
					int seleccionDesde = getSelectionStart();
					int seleccionHasta = getSelectionEnd() - 1;
					char[] temp = getText().toCharArray();
					StringBuffer textoComponente = new StringBuffer();
					for(int i = 0; i < temp.length; i++) {
						if(i < seleccionDesde || i > seleccionHasta) {
							textoComponente.append(temp[i]);
						}
					}
					numberDocument.setTextoComponente(textoComponente.toString());
				}
			}
		}

		public void keyReleased(KeyEvent evt) {
		}
	}

	private class NumericFocusListener implements FocusListener {
		public void focusGained(FocusEvent evt) {
		}

		public void focusLost(FocusEvent evt) {
			if(minimo != null) {
				if(getText().length() > 0) {
					if(getText().equals("-")) {
						setValue(null);
						return;
					}
					try {
						Long.valueOf(getText());
					} catch(NumberFormatException nfe) {
						setValue(null);
						return;
					}
					if(Long.valueOf(getText()) < minimo) {
						setText(minimo.toString());
					}
				}
			}
		}
	}

	public Integer getValueWithNull() {
		return getValue();
	}

}
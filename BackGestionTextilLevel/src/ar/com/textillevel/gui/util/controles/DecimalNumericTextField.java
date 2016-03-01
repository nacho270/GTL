package ar.com.textillevel.gui.util.controles;

import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import ar.com.fwcommon.componentes.FixedSizeFilter;

public class DecimalNumericTextField extends JTextField {

	private static final long serialVersionUID = 1333323126147248188L;

	private FWJNumberDocument numberDocument;
	private Double maximo = null;
	private Double minimo = null;
	private DecimalFormat decimalFormat;
	private Toolkit toolkit;

	public DecimalNumericTextField() {
		super();
		toolkit = Toolkit.getDefaultToolkit();
		decimalFormat = (DecimalFormat) DecimalFormat.getNumberInstance(new Locale("es_AR"));
		decimalFormat.setMaximumFractionDigits(3);
		decimalFormat.setMinimumFractionDigits(3);
		decimalFormat.setMinimumIntegerDigits(1);
		super.addFocusListener(new NumericFocusListener());
	}

	public DecimalNumericTextField(Integer maximumFractionDigits, Integer minimumFractionDigits) {
		super();
		toolkit = Toolkit.getDefaultToolkit();
		decimalFormat = (DecimalFormat) DecimalFormat.getNumberInstance(new Locale("es_AR"));
		decimalFormat.setMaximumFractionDigits(maximumFractionDigits);
		decimalFormat.setMinimumFractionDigits(minimumFractionDigits);
		decimalFormat.setMinimumIntegerDigits(1);
		decimalFormat.setGroupingUsed(false);
		super.addFocusListener(new NumericFocusListener());
	}

	public DecimalNumericTextField(double minimo, double maximo) {
		this();
		setMinimo(minimo);
		setMaximo(maximo);
		super.addFocusListener(new NumericFocusListener());
	}

	public DecimalNumericTextField(double minimo, double maximo, double valor) {
		this(minimo, maximo);
		setValue(valor);
		super.addFocusListener(new NumericFocusListener());
	}

	/**
	 * Establece la cantidad máxima posible de caracteres en el componente.
	 * 
	 * @param maxlength
	 */
	public void setMaxLength(int maxlength) {
		((AbstractDocument) getDocument())
				.setDocumentFilter(new FixedSizeFilter(maxlength));
	}

	/**
	 * Retorna el valor mínimo que puede ingresarse.
	 * 
	 * @return
	 */
	public Double getMinimo() {
		return minimo;
	}

	/**
	 * Setea el valor mínimo que puede ingresarse.
	 * 
	 * @param minimo
	 */
	public void setMinimo(double minimo) {
		this.minimo = minimo;
	}

	/**
	 * Retorna el valor máximo que puede ingresarse.
	 * 
	 * @return
	 */
	public Double getMaximo() {
		return maximo;
	}

	/**
	 * Setea el valor máximo que puede ingresarse.
	 * 
	 * @param maximo
	 */
	public void setMaximo(double maximo) {
		this.maximo = maximo;
	}

	/**
	 * Setea el formato a aplicar al valor ingresado.
	 * 
	 * @param formato
	 */
	public void setFormato(String formato) {
		decimalFormat = new DecimalFormat(formato);
	}

	/**
	 * @return El valor ingresado, o cero si no se puede parsear el valor.
	 */
	public Float getValue() {
		try {
			if(decimalFormat != null) {
				return new Float(decimalFormat.format(new Float(getText().replace(",", "."))).replace(",", "."));
			} else {
				return new Float(getText().replace(",", "."));
			}
		} catch (NumberFormatException e) {
			return new Float(0f);
		}
	}

	/**
	 * @return El valor ingresado, o cero si no se puede parsear el valor.
	 */
	public Double getDoubleValue() {
		try {
			return new Double(getText().replace(",", "."));
		} catch (NumberFormatException e) {
			return new Double(0d);
		}
	}

	/**
	 * Setea un valor al componente.
	 * 
	 * @param valor
	 */
	public void setValue(Double valor) {
		numberDocument.setTextoComponente("");
		if (valor != null) {
			setText(decimalFormat.format(valor));
		} else {
			setText("");
		}
	}

	protected Document createDefaultModel() {
		numberDocument = new FWJNumberDocument();
		addKeyListener(new ListenerDecimalNumericTextField(numberDocument));
		return numberDocument;
	}

	private class FWJNumberDocument extends PlainDocument {

		private static final long serialVersionUID = -7162184430057542069L;

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
			char ultimoSeparador = '1';
			boolean ingresoSeparador = DecimalNumericTextField.this.getText().indexOf(",") != -1 || DecimalNumericTextField.this.getText().indexOf(".") != -1;
			int j = 0;
			for (int i = 0; i < resultado.length; i++) {
				if (Character.isDigit(charsInsercion[i]) || ((posicion == 0) && (charsInsercion[i] == '-') && (minimo == null || minimo < 0))) {
					resultado[j++] = charsInsercion[i];
				} else {
					if (!ingresoSeparador) {
						if (charsInsercion[i] == '.' || charsInsercion[i] == ',') {
							resultado[j++] = charsInsercion[i];
							ingresoSeparador = true;
							ultimoSeparador = charsInsercion[i];
						}
					} else if(decimalFormat.isGroupingUsed() && ultimoSeparador != '1'
							&& ultimoSeparador != charsInsercion[i]) {
						resultado[j++] = charsInsercion[i];
						ingresoSeparador = true;
						ultimoSeparador = charsInsercion[i];
					} else {
						insercionValida = false;
						break;
					}
				}
			}
			textoComponente += insercion;
			if (insercionValida) {
				try {
					if (textoComponente.trim().compareTo("-") != 0) {
						if(textoComponente.trim().matches("0\\d+")){
							throw new ParseException(textoComponente, 1);
						}
						
						valor = decimalFormat.parse(textoComponente);
						if (valor instanceof Double) {
							valor = new Float(((Double) valor).floatValue());
						}
					}
				} catch (ParseException e) {
					// e.printStackTrace();
					insercionValida = false;
				}
				if (valor != null) {
					if (maximo != null) {
						if (((Comparable<Double>) valor.doubleValue()).compareTo(maximo) > 0) {
							insercionValida = false;
							j--;
						}
					}
					if (minimo != null && insercionValida) {
						if (((Comparable<Double>) valor.doubleValue()).compareTo(minimo) < 0) {
							insercionValida = false;
							j--;
						}
					}
				}
			}
			if (insercionValida)
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

	private class ListenerDecimalNumericTextField implements KeyListener {
		FWJNumberDocument numberDocument;

		public ListenerDecimalNumericTextField(FWJNumberDocument numberDocument) {
			this.numberDocument = numberDocument;
		}

		public void keyTyped(KeyEvent evt) {
		}

		public void keyPressed(KeyEvent evt) {
			String textoSeleccionado = getSelectedText();
			if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE|| evt.getKeyCode() == KeyEvent.VK_DELETE || (textoSeleccionado != null && textoSeleccionado.compareTo(getText()) == 0)) {
				if (textoSeleccionado == null || textoSeleccionado.length() == 0) {
					if (getText().length() == 1) {
						numberDocument.setTextoComponente("");
					} else if (getText().length() > 1) {
						int hasta = numberDocument.getTextoComponente().length() - 1;
						if (hasta >= 0) {
							numberDocument.setTextoComponente(numberDocument.getTextoComponente().substring(0, hasta));
						}
					}
				} else {
					int seleccionDesde = getSelectionStart();
					int seleccionHasta = getSelectionEnd() - 1;
					char[] temp = getText().toCharArray();
					StringBuffer textoComponente = new StringBuffer();
					for (int i = 0; i < temp.length; i++) {
						if (i < seleccionDesde || i > seleccionHasta) {
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
			if (minimo != null) {
				if (getText().length() > 0) {
					if (getText().equals("-")) {
						setValue(null);
						return;
					}
					try {
						Double.valueOf(getText());
					} catch (NumberFormatException nfe) {
						setValue(null);
						return;
					}
					if (Double.valueOf(getText()) < minimo) {
						setText(minimo.toString());
					}
				}
			}
		}
	}

	public Float getValueWithNull() {
		return getValue();
	}
}
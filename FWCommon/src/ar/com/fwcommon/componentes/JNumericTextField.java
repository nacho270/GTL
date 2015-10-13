package ar.com.fwcommon.componentes;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Text field para el manejo de números
 * <p>
 * Site: <a href="http://www.java2s.com/Code/Java/Swing-JFC/NumericTextField.htm">http://www.java2s.com/Code/Java/Swing-JFC/NumericTextField.htm</a>
 */
public class JNumericTextField extends JTextField {
	private static final long serialVersionUID = 8507929743268018358L;
	private static final int MAX_DECIMAL_DIGITS = 5;
	private static final int MIN_FRACTION_DIGITS = 2;
	public static final DecimalFormat INTEGER_DECIMAL_FORMAT = (DecimalFormat)NumberFormat.getIntegerInstance();
	public static final DecimalFormat CURRENCY_DECIMAL_FORMAT = (DecimalFormat)NumberFormat.getCurrencyInstance();
	public static final DecimalFormat PERCENT_DECIMAL_FORMAT = (DecimalFormat)NumberFormat.getPercentInstance();
	public static final DecimalFormat DOUBLE_DECIMAL_FORMAT;
	public static final DecimalFormat BIGDECIMAL_DECIMAL_FORMAT;
	
	static {
		DOUBLE_DECIMAL_FORMAT = new DecimalFormat();
		DOUBLE_DECIMAL_FORMAT.setParseIntegerOnly(false);
		DOUBLE_DECIMAL_FORMAT.setParseBigDecimal(false);
		DOUBLE_DECIMAL_FORMAT.setGroupingUsed(true);
		DOUBLE_DECIMAL_FORMAT.setMaximumFractionDigits(MAX_DECIMAL_DIGITS);
		DOUBLE_DECIMAL_FORMAT.setMinimumFractionDigits(MIN_FRACTION_DIGITS);

		BIGDECIMAL_DECIMAL_FORMAT = new DecimalFormat();
		BIGDECIMAL_DECIMAL_FORMAT.setParseIntegerOnly(false);
		BIGDECIMAL_DECIMAL_FORMAT.setParseBigDecimal(true);
		BIGDECIMAL_DECIMAL_FORMAT.setGroupingUsed(false);
		BIGDECIMAL_DECIMAL_FORMAT.setMaximumFractionDigits(MAX_DECIMAL_DIGITS);
	}
	
	private final EventListenerList events = new EventListenerList();
	private boolean nullAllowed = true;
	
	public JNumericTextField() {
		this(null, 0, INTEGER_DECIMAL_FORMAT);
	}

	public JNumericTextField(DecimalFormat format) {
		this(null, 0, format);
	}
	
	public JNumericTextField(int columns, DecimalFormat format) {
		this(null, columns, format);
	}

	public JNumericTextField(String text) {
		this(text, 0, null);
	}

	public JNumericTextField(String text, int columns) {
		this(text, columns, null);
	}

	public JNumericTextField(String text, int columns, DecimalFormat format) {
		super(null, text, columns);
		
		NumericPlainDocument numericDoc = getNumericDocument();
		if (format != null) {
			numericDoc.setFormat(format);
		}
		initialize();
	}
	
	private void initialize() {
		this.setHorizontalAlignment(JTextField.RIGHT);
		
		/*
		 * Agrego que al perder el foco verifique el valor ingresado. Si el
		 * mismo es válido, lo normaliza. Sino, dispara el evento
		 */
		this.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				try {
					if (getNumberValue() == null && !isNullAllowed())
						fireInvalidFormatEvent();
					else
						normalize();
				} catch (ParseException e1) {
					fireInvalidFormatEvent();
				}
			}		
		});
		
		this.addKeyListener(new KeyAdapter() {
			
			public void keyPressed(KeyEvent evt) {
				getNumericDocument().setLastPressedKey(evt.getKeyCode());
			}
			public void keyReleased(KeyEvent evt) {}
		});
	}

	protected final void fireInvalidFormatEvent() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				InvalidTextFormatEvent e = new InvalidTextFormatEvent(JNumericTextField.this);
				InvalidTextFormatListener listeners[] = events.getListeners(InvalidTextFormatListener.class);
				for (int i=0; i<listeners.length; i++)
					listeners[i].invalidTextFormat(e);
			}			
		});
	}
	
	/**
	 * Agrega un listener que se acciona cuando se deja incompleto el texto
	 * 
	 * @param listener Listener a agregar
	 */
	public void addInvalidTextFormatListener(InvalidTextFormatListener listener) {
		events.add(InvalidTextFormatListener.class, listener);
	}
	
	/**
	 * Quita un listener que se acciona cuando se deja incompleto el texto
	 * 
	 * @param listener Listener a quitar
	 */
	public void removeInvalidTextFormatListener(InvalidTextFormatListener listener) {
		events.remove(InvalidTextFormatListener.class, listener);
	}
	
	public final BigDecimal getMaximumValue() {
		return getNumericPlainDocument().getMaximumValue();
	}

	public void setMaximumValue(BigDecimal maximumValue) {
		getNumericPlainDocument().setMaximumValue(maximumValue);
	}

	public final BigDecimal getMinimumValue() {
		return getNumericPlainDocument().getMinimumValue();
	}

	public void setMinimumValue(BigDecimal minimumValue) {
		getNumericPlainDocument().setMinimumValue(minimumValue);
	}

	// Method to create default model
	protected NumericPlainDocument createDefaultModel() {
		return new NumericPlainDocument();
	}
 
	private NumericPlainDocument getNumericPlainDocument() {
		return (NumericPlainDocument)super.getDocument();
	}
	
	/**
	 * @return Devuelve el formato
	 */
	public DecimalFormat getFormat() {
		return getNumericDocument().getFormat();
	}

	// Methods to get the field value
	/**
	 * @return Devuelve el valor Long
	 * @throws ParseException
	 */
	public Long getLongValue() throws ParseException {
		return getNumericDocument().getLongValue();
	}
	
	/**
	 * @return Devuelve el valor double
	 * @throws ParseException
	 */
	public Double getDoubleValue() throws ParseException {
		return getNumericDocument().getDoubleValue();
	}

	/**
	 * @return Devuelve el valor double
	 * @throws ParseException
	 */
	public Number getNumberValue() throws ParseException {
		return getNumericDocument().getNumberValue();
	}

	public void normalize() throws ParseException {
		// format the value according to the format string
		Number number = getNumberValue();
		if (number != null)
			setText(getFormat().format(number));
	}

	public void setFormat(DecimalFormat format) {
		getNumericDocument().setFormat(format);
	}

	public void setValue(double d) {
		setText(getFormat().format(d));
	}

	public void setValue(long l) {
		setText(getFormat().format(l));
	}

	// Methods to install numeric values
	public void setValue(Number number) {
		setText(getFormat().format(number));
	}

	/**
	 * @return the nullAllowed
	 */
	public boolean isNullAllowed() {
		return nullAllowed;
	}

	/**
	 * @param nullAllowed the nullAllowed to set
	 */
	public void setNullAllowed(boolean nullAllowed) {
		this.nullAllowed = nullAllowed;
	}

	/* (non-Javadoc)
	 * javax.swing.text.JTextComponent#getDocument()
	 */
	protected NumericPlainDocument getNumericDocument() {
		return (NumericPlainDocument)super.getDocument();
	}
}

@SuppressWarnings("serial")
class NumericPlainDocument extends PlainDocument {
	protected static DecimalFormat defaultFormat = new DecimalFormat();
	protected char decimalSeparator;
	protected DecimalFormat format;
	protected char groupingSeparator;
	protected String negativePrefix;
	protected int negativePrefixLen;
	protected String negativeSuffix;
	protected int negativeSuffixLen;
	protected ParsePosition parsePos = new ParsePosition(0);
	protected String positivePrefix;
	protected int positivePrefixLen;
	protected String positiveSuffix;
	protected int positiveSuffixLen;
	protected int lastPressedKey;
	private BigDecimal minimumValue;
	private BigDecimal maximumValue;


	public NumericPlainDocument() {
		setFormat(null);
	}

	public NumericPlainDocument(AbstractDocument.Content content, DecimalFormat format) {
		super(content);
		setFormat(format);

		try {
			format.parseObject(content.getString(0, content.length()), parsePos);
		} catch (Exception e) {
			throw new IllegalArgumentException("Initial content not a valid number");
		}

		if (parsePos.getIndex() != content.length() - 1) {
			throw new IllegalArgumentException("Initial content not a valid number");
		}
	}

	public NumericPlainDocument(DecimalFormat format) {
		setFormat(format);
	}
	
	public Double getDoubleValue() throws ParseException {
		Number result = getNumberValue();
		if (result == null) return null;
		if ((result instanceof Long) == false && (result instanceof Double) == false) {
			throw new ParseException("Not a valid double", 0);
		}

		if (result instanceof Long) {
			result = new Double(result.doubleValue());
		}

		return (Double) result;
	}

	public DecimalFormat getFormat() {
		return format;
	}

	public Long getLongValue() throws ParseException {
		Number result = getNumberValue();
		if (result == null) return null;
		if ((result instanceof Long) == false) {
			throw new ParseException("Not a valid long", 0);
		}

		return (Long) result;
	}

	public Number getNumberValue() throws ParseException {
		try {
			if (getLength() == 0) return null;
			String content = getText(0, getLength());
			parsePos.setIndex(0);
			Number result = format.parse(content, parsePos);
			if (parsePos.getIndex() != getLength()) {
				throw new ParseException("Not a valid number: " + content, 0);
			}

			return result;
		} catch (BadLocationException e) {
			throw new ParseException("Not a valid number", 0);
		}
	}

	public final BigDecimal getMaximumValue() {
		return maximumValue;
	}

	public void setMaximumValue(BigDecimal maximumValue) {
		this.maximumValue = maximumValue;
	}

	public final BigDecimal getMinimumValue() {
		return minimumValue;
	}

	public void setMinimumValue(BigDecimal minimumValue) {
		this.minimumValue = minimumValue;
	}
	
	public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
		if (str == null || str.length() == 0) {
			return;
		} else if(str.length() == 1 && lastPressedKey == KeyEvent.VK_DECIMAL) {
			//By Pola :-)
			str = new String(new char[] { format.getDecimalFormatSymbols().getDecimalSeparator()} );
		}
		
		Content content = getContent();
		int length = content.length();
		int originalLength = length;

		parsePos.setIndex(0);

		boolean completeNumber = false;
		
		// Create the result of inserting the new data,
		// but ignore the trailing newline
		String targetString = content.getString(0, offset) + str
				+ content.getString(offset, length - offset - 1);

		// Parse the input string and check for errors
		do {
			boolean gotPositive = targetString.startsWith(positivePrefix);
			boolean gotNegative = targetString.startsWith(negativePrefix);

			length = targetString.length();

			// If we have a valid prefix, the parse fails if the
			// suffix is not present and the error is reported
			// at index 0. So, we need to add the appropriate
			// suffix if it is not present at this point.
			if (gotPositive == true || gotNegative == true) {
				String suffix;
				int suffixLength;
				int prefixLength;

				if (gotPositive == true && gotNegative == true) {
					// This happens if one is the leading part of
					// the other - e.g. if one is "(" and the other "(("
					if (positivePrefixLen > negativePrefixLen) {
						gotNegative = false;
					} else {
						gotPositive = false;
					}
				}

				if (gotPositive == true) {
					suffix = positiveSuffix;
					suffixLength = positiveSuffixLen;
					prefixLength = positivePrefixLen;
				} else {
					// Must have the negative prefix
					suffix = negativeSuffix;
					suffixLength = negativeSuffixLen;
					prefixLength = negativePrefixLen;
				}

				// If the string consists of the prefix alone,
				// do nothing, or the result won't parse.
				if (length == prefixLength) {
					break;
				}

				// We can't just add the suffix, because part of it
				// may already be there. For example, suppose the
				// negative prefix is "(" and the negative suffix is
				// "$)". If the user has typed "(345$", then it is not
				// correct to add "$)". Instead, only the missing part
				// should be added, in this case ")".
				if (targetString.endsWith(suffix) == false) {
					int i;
					for (i = suffixLength - 1; i > 0; i--) {
						if (targetString.regionMatches(length - i, suffix, 0, i)) {
							targetString += suffix.substring(i);
							break;
						}
					}

					if (i == 0) {
						// None of the suffix was present
						targetString += suffix;
					}

					length = targetString.length();
				}
			}

			format.parse(targetString, parsePos);

			int endIndex = parsePos.getIndex();
			if (endIndex == length) {
				completeNumber = true;
				break; // Number is acceptable
			}

			// Parse ended early
			// Since incomplete numbers don't always parse, try
			// to work out what went wrong.
			// First check for an incomplete positive prefix
			if (positivePrefixLen > 0 && endIndex < positivePrefixLen
					&& length <= positivePrefixLen
					&& targetString.regionMatches(0, positivePrefix, 0, length)) {
				break; // Accept for now
			}

			// Next check for an incomplete negative prefix
			if (negativePrefixLen > 0 && endIndex < negativePrefixLen
					&& length <= negativePrefixLen
					&& targetString.regionMatches(0, negativePrefix, 0, length)) {
				break; // Accept for now
			}

			// Allow a number that ends with the group
			// or decimal separator, if these are in use
			char lastChar = targetString.charAt(originalLength - 1);
			int decimalIndex = targetString.indexOf(decimalSeparator);
			if (format.isGroupingUsed() && lastChar == groupingSeparator && decimalIndex == -1) {
				// Allow a "," but only in integer part
				break;
			}

			if (format.isParseIntegerOnly() == false && lastChar == decimalSeparator
					&& decimalIndex == originalLength - 1) {
				// Allow a ".", but only one
				break;
			}
			return;
		} while (true == false);

		if (completeNumber) {
			BigDecimal current = null;
			String[] split = targetString.split("\\.");
			if (split.length > 2) {
				StringBuffer bufferTargetString = new StringBuffer();
				int ultimo = split.length - 1;
				for (int i = 0; i < ultimo; i++) {
					bufferTargetString.append(split[i]);
				}
				bufferTargetString.append("." + split[ultimo]);
				current = new BigDecimal(bufferTargetString.toString());
			} else {
				current = new BigDecimal(targetString);
			}
			BigDecimal maximum = getMaximumValue();
			BigDecimal minimum = getMinimumValue();
			if ((minimum == null || minimum.compareTo(current) <= 0) &&
				(maximum == null || current.compareTo(maximum) <= 0)) {
					// Finally, add to the model
					super.insertString(offset, str, a);
			}
		} else {
			// Finally, add to the model
			super.insertString(offset, str, a);
		}
	}

	public void setFormat(DecimalFormat fmt) {
		this.format = fmt != null ? fmt : (DecimalFormat) defaultFormat.clone();
		decimalSeparator = format.getDecimalFormatSymbols().getDecimalSeparator();
		groupingSeparator = format.getDecimalFormatSymbols().getGroupingSeparator();
		positivePrefix = format.getPositivePrefix();
		positivePrefixLen = positivePrefix.length();
		negativePrefix = format.getNegativePrefix();
		negativePrefixLen = negativePrefix.length();
		positiveSuffix = format.getPositiveSuffix();
		positiveSuffixLen = positiveSuffix.length();
		negativeSuffix = format.getNegativeSuffix();
		negativeSuffixLen = negativeSuffix.length();
	}
	
	public void setLastPressedKey(int keyCode) {
		this.lastPressedKey = keyCode;
	}
}

package ar.com.fwcommon.componentes;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/** Clase para limitar la capacidad del TextField o TextArea */
public class FixedSizeFilter extends DocumentFilter {

	int maxSize;

	/**
	 * Limita el máximo número posible de caracteres permitidos.
	 * @param limite
	 */
	public FixedSizeFilter(int limite) {
		maxSize = limite;
	}

	/**
	 * Este método es llamado cuando son insertados caracteres en el componente.
	 * @param fb
	 * @param offset
	 * @param str
	 * @param attr
	 * @throws BadLocationException
	 */
	public void insertString(DocumentFilter.FilterBypass fb, int offset, String str, AttributeSet attr) throws BadLocationException {
		replace(fb, offset, 0, str, attr);
	}

	/**
	 * Este método es llamado cuando son reemplazados los caracteres en el componente.
	 * @param fb
	 * @param offset
	 * @param length
	 * @param str
	 * @param attrs
	 * @throws BadLocationException
	 */
	public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String str, AttributeSet attrs) throws BadLocationException {
		int strlen = 0;
		if(str != null) {
			strlen = str.length();
		}
		int newLength = fb.getDocument().getLength() - length + strlen;
		if(newLength <= maxSize) {
			fb.replace(offset, length, str, attrs);
		} else {
			throw new BadLocationException("Cantidad de caracteres excedida en el componente.", offset);
		}
	}

}
package ar.com.fwcommon.componentes;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;

import ar.com.fwcommon.util.StringUtil;

/**
 * Componente que simplifica el uso del componente JTextField de Java.
 */
public class FWJTextField extends JTextField {

	private static final long serialVersionUID = -3619221968640177392L;

	private Integer longitud;
	private CopyAndPasteMouseListener copyAndPasteMouseListener;

	/** Método constructor */
	public FWJTextField() {
		this("");
	}

	/**
	 * Método constructor.
	 * @param texto El texto inicial del componente.
	 */
	public FWJTextField(String texto) {
		super(texto);
		getDocument().addDocumentListener(new TextChangedListener());
		addMouseListener(getCopyAndPasteMouseListener());
	}

	/**
	 * Método constructor.
	 * @param longitud La longitud (cantidad de caracteres) que admite el componente.
	 */
	public FWJTextField(int longitud) {
		super();
		this.longitud = longitud;
		AbstractDocument doc = (AbstractDocument)getDocument();
		doc.setDocumentFilter(new FixedSizeFilter(longitud));
		addMouseListener(getCopyAndPasteMouseListener());
	}

	/**
	 * Setea la longitud (cantidad de caracteres) que admitirá el componente.
	 * @param longitud La cantidad de caracteres que admitirá el componente.
	 */
	public void setLongitud(Integer longitud) {
		if(longitud == null) {
			((AbstractDocument)getDocument()).setDocumentFilter(null);
		} else {
			((AbstractDocument)getDocument()).setDocumentFilter(new FixedSizeFilter(longitud));
		}
		this.longitud = longitud;
	}

	/**
	 * @return Devuelve la longitud (cantidad de caracteres) que admite el componente.
	 */
	public Integer getLongitud() {
		return longitud;
	}

	/** Remueve el listener que muestra el menú contextual de Cortar/Copiar/Pegar */
	public void removeCopyAndPasteMouseListener() {
		removeMouseListener(getCopyAndPasteMouseListener());
	}

	/**
	 * Método para el manejo del evento de cambio del texto del componente.
	 * Implementado vacío para ser sobreescrito.
	 */
	public void txtChanged() {
	}

	/* Devuelve el listener del mouse que despliega el menú contextual de Cortar/Copiar/Pegar */
	private CopyAndPasteMouseListener getCopyAndPasteMouseListener() {
		if(copyAndPasteMouseListener == null) {
			copyAndPasteMouseListener = new CopyAndPasteMouseListener(this);
		}
		return copyAndPasteMouseListener;
	}

	public class TextChangedListener implements DocumentListener {
		public void removeUpdate(DocumentEvent evt) {
		}

		public void insertUpdate(DocumentEvent evt) {
			txtChanged();
		}

		public void changedUpdate(DocumentEvent evt) {
		}
	}

	/**
	 * Reemplaza el texto retornado por el getText pero trimeado con el trim del StringUtil.
	 * @return
	 */
	public String getTrimmedText() {
		return StringUtil.trim(getText());
	}
}
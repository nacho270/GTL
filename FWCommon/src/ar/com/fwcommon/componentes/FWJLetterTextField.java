package ar.com.fwcommon.componentes;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class FWJLetterTextField extends JTextField {

	private FWJLetterDocument letterDocument;
	private Toolkit toolkit;

	public FWJLetterTextField() {
		super();
		this.toolkit = Toolkit.getDefaultToolkit();
	}

	public FWJLetterTextField(int columnas) {
		super(columnas);
		this.toolkit = Toolkit.getDefaultToolkit();
	}

	protected Document createDefaultModel() {
		letterDocument = new FWJLetterDocument();
		addKeyListener(new ListenerCLJLetterTextField(letterDocument));
		return letterDocument;
    }

    private class FWJLetterDocument extends PlainDocument {
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
            int j = 0;
			for(int i = 0; i < resultado.length; i++) {
                if(!Character.isDigit(charsInsercion[i])) {
					resultado[j++] = charsInsercion[i];
                } else {
					insercionValida = false;
					break;
                }
            }
			textoComponente += insercion;
            if(insercionValida) {
            	super.insertString(posicion, new String(resultado, 0, j), atributos);            	
            } else {
				textoComponente = new String(textoComponente.substring(0, textoComponente.length() - insercion.length()));
				toolkit.beep();
            }
        }
    }

	private class ListenerCLJLetterTextField implements KeyListener {
		FWJLetterDocument letterDocument;

		public ListenerCLJLetterTextField(FWJLetterDocument letterDocument) {
			this.letterDocument = letterDocument;
		}

		public void keyTyped(KeyEvent evt) {
		}

		public void keyPressed(KeyEvent evt) {
			String textoSeleccionado = getSelectedText();
			if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE || evt.getKeyCode() == KeyEvent.VK_DELETE || (textoSeleccionado != null && textoSeleccionado.compareTo(getText()) == 0)) {
				if(textoSeleccionado == null || textoSeleccionado.length() == 0) {
					if(getText().length() == 1) {
						letterDocument.setTextoComponente("");					
					} else if(getText().length() > 1) {
						letterDocument.setTextoComponente(letterDocument.getTextoComponente().substring(0, letterDocument.getTextoComponente().length() - 1));					
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
					letterDocument.setTextoComponente(textoComponente.toString());
				}
			}
		}

		public void keyReleased(KeyEvent evt) {
		}
	}

}
package ar.com.textillevel.gui.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class UppercaseDocumentFilterSoloLetras extends DocumentFilter {

	private String textoComponente = "";

	public void setTextoComponente(String textoComponente) {
		this.textoComponente = textoComponente;
	}

	public String getTextoComponente() {
		return textoComponente;
	}
	
	@Override
	public void insertString(DocumentFilter.FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
		boolean insercionValida = true;
		char[] charsInsercion = text.toCharArray();
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
		textoComponente += text;
        if(insercionValida) {
        	fb.insertString(offset, new String(resultado, 0, j).toUpperCase(), attr);            	
        } else {
			textoComponente = new String(textoComponente.substring(0, textoComponente.length() - text.length()));
        }
		//fb.insertString(offset, text.toUpperCase(), attr);
	}

	@Override
	public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
		boolean insercionValida = true;
		char[] charsInsercion = text.toCharArray();
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
		textoComponente += text;
        if(insercionValida) {
        	fb.replace(offset,length, new String(resultado, 0, j).toUpperCase(), attrs);            	
        } else {
			textoComponente = new String(textoComponente.substring(0, textoComponente.length() - text.length()));
        }
//		fb.replace(offset, length, text.toUpperCase(), attrs);
	}

}

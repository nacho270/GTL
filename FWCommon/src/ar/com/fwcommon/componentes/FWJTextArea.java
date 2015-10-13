package ar.com.fwcommon.componentes;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

import ar.com.fwcommon.util.StringUtil;

public class FWJTextArea extends JTextArea {

	public FWJTextArea() {
		super();
		setLineWrap(true);
	}

	public FWJTextArea(int lenght) {
		super();
		setLineWrap(true);
		AbstractDocument doc = (AbstractDocument)getDocument();
		doc.setDocumentFilter(new ExtendedFixedSizeFilter(lenght));
		this.addKeyListener(new TextAreaKeyListener(this));
	}

	/**
	 * Reemplaza el texto retornado por el getText pero trimeado con el trim del StringUtil.
	 * @return
	 */
	public String getTrimmedText() {
		return StringUtil.trim(getText());
	}

	static class TextAreaKeyListener extends KeyAdapter {
		private JComponent component;

		public TextAreaKeyListener(JComponent component) {
			super();
			this.component = component;
		}

		@Override
		public void keyPressed(KeyEvent evt) {
			if(evt.getKeyCode() == KeyEvent.VK_TAB)
				component.transferFocus();
		}
	}

	static class ExtendedFixedSizeFilter extends FixedSizeFilter {
		public ExtendedFixedSizeFilter(int size) {
			super(size);
		}

		@Override
		public void insertString(FilterBypass fb, int offset, String str, AttributeSet attr) throws BadLocationException {
			if(str == null || !str.equals("\t"))
				super.insertString(fb, offset, str, attr);
		}

		@Override
		public void replace(FilterBypass fb, int offset, int length, String str, AttributeSet attrs) throws BadLocationException {
			if(str == null || !str.equals("\t"))
				super.replace(fb, offset, length, str, attrs);
		}
	}

}
package ar.com.textillevel.gui.util.componentes;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import ar.com.fwcommon.util.GuiUtil;

public abstract class AutocompleteJComboBox<E> extends JComboBox {

	private static final long serialVersionUID = -2974862177535535230L;

	private List<E> elements;

	public AutocompleteJComboBox(List<E> elements) {
		super();
		this.elements = elements;
		putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE); //hace que la selección con las teclas cursoras sea igual que con el mouse
		setEditable(true);

		GuiUtil.llenarCombo(this, elements, true);
		setSelectedIndex(-1);

		final Component c = getEditor().getEditorComponent();

		if (c instanceof JTextComponent) {
			final JTextComponent tc = (JTextComponent) c;
			tc.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void changedUpdate(DocumentEvent arg0) {
				}

				@Override
				public void insertUpdate(DocumentEvent arg0) {
					update();
				}

				@Override
				public void removeUpdate(DocumentEvent arg0) {
					update();
				}

				public void update() {
					// perform separately, as listener conflicts between the editing component and JComboBox will result in an IllegalStateException due to editing the component when it is locked.
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							List<E> founds = getMatchedFounds(tc.getText());
							setEditable(false);
							removeAllItems();

							if(!completeMatch(tc.getText())) {
								addItem(tc.getText());
							}

							for(E s : founds) {
								addItem(s);
							}

							setEditable(true);
							setPopupVisible(true);
							c.requestFocus();
						}

					});

				}

			});

			// When the text component changes, focus is gained and the menu disappears. To account for this, whenever the focus is gained by the JTextComponent and it has searchable values, we show the popup.
			tc.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) {
					if (tc.getText().length() > 0) {
						setPopupVisible(true);
					}
				}

				@Override
				public void focusLost(FocusEvent arg0) {
				}

			});

		} else {
			throw new IllegalStateException("Editing component is not a JTextComponent!");
		}
	}

	private List<E> getMatchedFounds(String param) {
		List<E> founds = new ArrayList<E>();
		for(E elem : elements) {
			if(match(param, elem)) {
				founds.add(elem);
			}
		}
		return founds;
	}

	private boolean completeMatch(String param) {
		for(E elem : elements) {
			if(elem.toString().trim().compareToIgnoreCase(param.trim()) == 0) {
				return true;
			}
		}
		return false;
	}

	public abstract boolean match(String param, E value);

}
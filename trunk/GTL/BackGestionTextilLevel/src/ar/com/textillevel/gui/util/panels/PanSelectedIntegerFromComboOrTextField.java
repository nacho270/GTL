package ar.com.textillevel.gui.util.panels;

import java.util.List;

import javax.swing.JTextField;

import ar.clarin.fwjava.componentes.CLJNumericTextField;

public class PanSelectedIntegerFromComboOrTextField extends PanSelectElementFromComboOrTextField<Integer> {

	private static final long serialVersionUID = 3130860643580526953L;

	public PanSelectedIntegerFromComboOrTextField(String titleCombo, List<Integer> items) {
		super(titleCombo, items);
	}

	@Override
	protected JTextField createTextField() {
		return new CLJNumericTextField();
	}

	@Override
	protected Integer extractValueFromTextField() {
		return ((CLJNumericTextField)getTextField()).getValue();
	}

	@Override
	protected void setValueInTextField(Integer selectedItem) {
		((CLJNumericTextField)getTextField()).setValue(new Long(selectedItem));
	}

}

package ar.com.textillevel.gui.util.panels;

import java.util.List;

import javax.swing.JTextField;

import ar.com.fwcommon.componentes.FWJNumericTextField;

public class PanSelectedIntegerFromComboOrTextField extends PanSelectElementFromComboOrTextField<Integer> {

	private static final long serialVersionUID = 3130860643580526953L;

	public PanSelectedIntegerFromComboOrTextField(String titleCombo, List<Integer> items) {
		super(titleCombo, items);
	}

	@Override
	protected JTextField createTextField() {
		return new FWJNumericTextField();
	}

	@Override
	protected Integer extractValueFromTextField() {
		return ((FWJNumericTextField)getTextField()).getValue();
	}

	@Override
	protected void setValueInTextField(Integer selectedItem) {
		((FWJNumericTextField)getTextField()).setValue(new Long(selectedItem));
	}

}

package ar.com.textillevel.gui.util.panels;

import java.util.List;

import javax.swing.JTextField;

import ar.com.textillevel.gui.util.controles.DecimalNumericTextField;

public class PanSelectedFloatFromComboOrTextField extends PanSelectElementFromComboOrTextField<Float> {

	private static final long serialVersionUID = -2556677628133015664L;

	public PanSelectedFloatFromComboOrTextField(String titleCombo, List<Float> items) {
		super(titleCombo, items);
	}

	@Override
	protected JTextField createTextField() {
		return new DecimalNumericTextField();
	}

	@Override
	protected Float extractValueFromTextField() {
		return ((DecimalNumericTextField)getTextField()).getValue();
	}

	@Override
	protected void setValueInTextField(Float selectedItem) {
		((DecimalNumericTextField)getTextField()).setValue(new Double(selectedItem));
	}

}

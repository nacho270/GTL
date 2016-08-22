package ar.com.textillevel.gui.modulos.odt.gui.validacion;

import java.awt.Component;

import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.textillevel.gui.util.controles.DecimalNumericTextField;

public class ValidacionMayorQueCero extends ValidacionSobreTextField {

	public ValidacionMayorQueCero(Component owner, DecimalNumericTextField textField, String label) {
		super(owner, textField, label);
	}

	public ValidacionMayorQueCero(Component owner, FWJNumericTextField textFieldInteger, String label) {
		super(owner, textFieldInteger, label);
	}

	@Override
	public boolean validate() {
		if(getTextField() != null && getTextField().getValue() <= 0f) {
			FWJOptionPane.showErrorMessage(getOwner(), "El campo '" + getLabel() + "' debe ser mayor que cero.", "Error");
			getTextField().requestFocus();
			return false;
		}
		if(getTextFieldInteger() != null && getTextFieldInteger().getValue() <= 0) {
			FWJOptionPane.showErrorMessage(getOwner(), "El campo '" + getLabel() + "' debe ser mayor que cero.", "Error");
			getTextFieldInteger().requestFocus();
			return false;
		}
		return true;
	}

}

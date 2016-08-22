package ar.com.textillevel.gui.modulos.odt.gui.validacion;

import java.awt.Component;

import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.gui.util.controles.DecimalNumericTextField;

public class ValidacionCompletitud extends ValidacionSobreTextField {

	public ValidacionCompletitud(Component owner, FWJNumericTextField textFieldInteger, String label) {
		super(owner, textFieldInteger, label);
	}

	public ValidacionCompletitud(Component owner, DecimalNumericTextField textField, String label) {
		super(owner, textField, label);
	}

	@Override
	public boolean validate() {
		if(getTextField() != null && StringUtil.isNullOrEmpty(getTextField().getText())) {
			FWJOptionPane.showErrorMessage(getOwner(), "Falta completar el campo '" + getLabel() + "'.", "Error");
			getTextField().requestFocus();
			return false;
		}
		if(getTextFieldInteger() != null && StringUtil.isNullOrEmpty(getTextFieldInteger().getText())) {
			FWJOptionPane.showErrorMessage(getOwner(), "El campo '" + getLabel() + "' debe ser mayor que cero.", "Error");
			getTextFieldInteger().requestFocus();
			return false;
		}
		return true;
	}

}
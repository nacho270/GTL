package ar.com.textillevel.gui.modulos.odt.gui.validacion;

import java.awt.Component;

import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.gui.util.controles.DecimalNumericTextField;

public class ValidacionCompletitud extends ValidacionSobreTextField {

	public ValidacionCompletitud(Component owner, CLJNumericTextField textFieldInteger, String label) {
		super(owner, textFieldInteger, label);
	}

	public ValidacionCompletitud(Component owner, DecimalNumericTextField textField, String label) {
		super(owner, textField, label);
	}

	@Override
	public boolean validate() {
		if(getTextField() != null && StringUtil.isNullOrEmpty(getTextField().getText())) {
			CLJOptionPane.showErrorMessage(getOwner(), "Falta completar el campo '" + getLabel() + "'.", "Error");
			getTextField().requestFocus();
			return false;
		}
		if(getTextFieldInteger() != null && StringUtil.isNullOrEmpty(getTextFieldInteger().getText())) {
			CLJOptionPane.showErrorMessage(getOwner(), "El campo '" + getLabel() + "' debe ser mayor que cero.", "Error");
			getTextFieldInteger().requestFocus();
			return false;
		}
		return true;
	}

}
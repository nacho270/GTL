package ar.com.textillevel.gui.modulos.odt.gui.validacion;

import java.awt.Component;

import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.com.textillevel.gui.util.controles.DecimalNumericTextField;

public abstract class ValidacionSobreTextField extends Validacion {

	private DecimalNumericTextField textField;
	private CLJNumericTextField textFieldInteger;
	private String label;

	public ValidacionSobreTextField(Component owner, DecimalNumericTextField textField, String label) {
		super(owner);
		this.textField = textField;
		this.label = label;
	}

	public ValidacionSobreTextField(Component owner, CLJNumericTextField textFieldInteger, String label) {
		super(owner);
		this.textFieldInteger = textFieldInteger;
		this.label = label;
	}

	protected String getLabel() {
		return label;
	}

	protected DecimalNumericTextField getTextField() {
		return textField;
	}
	
	protected CLJNumericTextField getTextFieldInteger() {
		return textFieldInteger;
	}

}

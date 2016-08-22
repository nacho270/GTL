package ar.com.textillevel.gui.modulos.odt.gui.validacion;

import java.awt.Component;

import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.textillevel.gui.util.controles.DecimalNumericTextField;

public class ValidacionRangoTextFields extends Validacion {

	private DecimalNumericTextField textFieldMin;
	private String labelMin;
	private DecimalNumericTextField textFieldMax;
	private String labelMax;
	private FWJNumericTextField textNumericFieldMin;
	private FWJNumericTextField textNumericFieldMax;
	
	public ValidacionRangoTextFields(Component owner, DecimalNumericTextField textFieldMin, String labelMin, DecimalNumericTextField textFieldMax, String labelMax) {
		super(owner);
		this.textFieldMin = textFieldMin;
		this.labelMin = labelMin;
		this.textFieldMax = textFieldMax;
		this.labelMax = labelMax;
	}

	public ValidacionRangoTextFields(Component owner, FWJNumericTextField textNumericFieldMin, String labelMin, FWJNumericTextField textNumericFieldMax, String labelMax) {
		super(owner);
		this.textNumericFieldMin = textNumericFieldMin;
		this.labelMin = labelMin;
		this.textNumericFieldMax = textNumericFieldMax;
		this.labelMax = labelMax;
	}

	private DecimalNumericTextField getTextFieldMin() {
		return textFieldMin;
	}

	private String getLabelMin() {
		return labelMin;
	}

	private DecimalNumericTextField getTextFieldMax() {
		return textFieldMax;
	}

	private String getLabelMax() {
		return labelMax;
	}

	private FWJNumericTextField getTextNumericFieldMax() {
		return textNumericFieldMax;
	}

	private FWJNumericTextField getTextNumericFieldMin() {
		return textNumericFieldMin;
	}

	@Override
	public boolean validate() {
		if(getTextFieldMin() != null && getTextFieldMin().getValue() > getTextFieldMax().getValue()) {
			FWJOptionPane.showErrorMessage(getOwner(), "El campo '" + getLabelMin() + "' debe ser menor o igual que el campo '" + getLabelMax() + "'", "Error");
			getTextFieldMin().requestFocus();
			return false;
		}
		if(getTextNumericFieldMin() != null && getTextNumericFieldMin().getValue() > getTextNumericFieldMax().getValue()) {
			FWJOptionPane.showErrorMessage(getOwner(), "El campo '" + getLabelMin() + "' debe ser menor o igual que el campo '" + getLabelMax() + "'", "Error");
			getTextNumericFieldMin().requestFocus();
			return false;
		}
		return true;
	}

}
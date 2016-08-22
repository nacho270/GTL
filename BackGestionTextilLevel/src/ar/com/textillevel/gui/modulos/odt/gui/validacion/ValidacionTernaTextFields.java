package ar.com.textillevel.gui.modulos.odt.gui.validacion;

import java.awt.Component;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.textillevel.gui.util.controles.DecimalNumericTextField;

public class ValidacionTernaTextFields extends Validacion {

	private DecimalNumericTextField textFieldMin;
	private String labelMin;
	private DecimalNumericTextField textFieldProm;
	private String labelProm;
	private DecimalNumericTextField textFieldMax;
	private String labelMax;

	public ValidacionTernaTextFields(Component owner, DecimalNumericTextField textFieldMin, String labelMin, DecimalNumericTextField textFieldProm, String labelProm, DecimalNumericTextField textFieldMax, String labelMax) {
		super(owner);
		this.textFieldMin = textFieldMin;
		this.labelMin = labelMin;
		this.textFieldProm = textFieldProm;
		this.labelProm = labelProm;
		this.textFieldMax = textFieldMax;
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

	private DecimalNumericTextField getTextFieldProm() {
		return textFieldProm;
	}

	private String getLabelProm() {
		return labelProm;
	}

	@Override
	public boolean validate() {
		if(getTextFieldMin().getValue() > getTextFieldMax().getValue()) {
			FWJOptionPane.showErrorMessage(getOwner(), "El campo '" + getLabelMin() + "' debe ser menor o igual que el campo '" + getLabelMax() + "'", "Error");
			getTextFieldMin().requestFocus();
			return false;
		}
		if(getTextFieldProm().getValue() < getTextFieldMin().getValue()) {
			FWJOptionPane.showErrorMessage(getOwner(), "El campo '" + getLabelProm() + "' debe ser mayor o igual que el campo '" + getLabelMin() + "'", "Error");
			getTextFieldProm().requestFocus();
			return false;
		}
		if(getTextFieldProm().getValue() > getTextFieldMax().getValue()) {
			FWJOptionPane.showErrorMessage(getOwner(), "El campo '" + getLabelProm() + "' debe ser menor o igual que el campo '" + getLabelMax() + "'", "Error");
			getTextFieldProm().requestFocus();
			return false;
		}
		return true;
	}

}
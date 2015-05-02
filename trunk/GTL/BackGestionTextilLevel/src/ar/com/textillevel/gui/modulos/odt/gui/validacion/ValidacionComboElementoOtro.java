package ar.com.textillevel.gui.modulos.odt.gui.validacion;

import java.awt.Component;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.com.textillevel.gui.util.panels.PanComboConElementoOtro;

public class ValidacionComboElementoOtro<T> extends Validacion {

	private PanComboConElementoOtro<T> panelCombo;
	
	public ValidacionComboElementoOtro(Component owner, PanComboConElementoOtro<T> panelCombo) {
		super(owner);
		this.panelCombo = panelCombo;
	}

	@Override
	public boolean validate() {
		if(panelCombo.getSelectedItem() == null) {
			CLJOptionPane.showErrorMessage(getOwner(), "Falta seleccionar un item del '" + panelCombo.getLblCombo() + "'.", "Error");
			return false;
		}
		return true;
	}

}

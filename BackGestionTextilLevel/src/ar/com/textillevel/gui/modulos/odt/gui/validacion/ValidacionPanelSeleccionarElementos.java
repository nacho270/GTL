package ar.com.textillevel.gui.modulos.odt.gui.validacion;

import java.awt.Component;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.textillevel.gui.util.panels.PanelSeleccionarElementos;

public class ValidacionPanelSeleccionarElementos<T> extends Validacion {

	private PanelSeleccionarElementos<T> panelSel;
	
	public ValidacionPanelSeleccionarElementos(Component owner, PanelSeleccionarElementos<T> panelSel) {
		super(owner);
		this.panelSel = panelSel;
	}

	@Override
	public boolean validate() {
		if(panelSel.getSelectedElements().isEmpty()) {
			FWJOptionPane.showErrorMessage(getOwner(), "Debe seleccionar al menos un elemento de '" + panelSel.getTitleLabel() + "'.", "Error");
			return false;
		}
		return true;
	}

}

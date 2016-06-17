package ar.com.textillevel.gui.modulos.odt.gui;

import java.awt.Dialog;
import java.util.List;

import ar.com.textillevel.entidades.ventas.materiaprima.Pigmento;
import ar.com.textillevel.gui.modulos.odt.gui.estampado.TabPaneAnilinaPigmentoCantidad;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.estampado.PigmentoCantidad;

public class PanTablaPigmentoCantidad extends PanelTablaMateriaPrimaCantidad<Pigmento, PigmentoCantidad> {

	private static final long serialVersionUID = -5278662193977929538L;

	public PanTablaPigmentoCantidad(TabPaneAnilinaPigmentoCantidad tabPaneAnilinaPigmentoCantidad, Dialog owner, String descripcionTipoMateriaPrima, List<Pigmento> allFormulablesPosibles) {
		super(owner, descripcionTipoMateriaPrima, allFormulablesPosibles);
	}

	@Override
	public PigmentoCantidad createMateriaPrimaCantidad() {
		PigmentoCantidad pigmentoCantidad = new PigmentoCantidad();
		pigmentoCantidad.setCantidad(0f);
		return pigmentoCantidad;
	}

	@Override
	public String createLabelTipoMateriaPrima() {
		return "Pigmento";
	}

}
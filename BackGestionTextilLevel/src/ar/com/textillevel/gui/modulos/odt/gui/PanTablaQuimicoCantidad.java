package ar.com.textillevel.gui.modulos.odt.gui;

import java.awt.Dialog;
import java.util.List;

import ar.com.textillevel.entidades.ventas.materiaprima.Quimico;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.QuimicoCantidad;

public class PanTablaQuimicoCantidad extends PanelTablaMateriaPrimaCantidad<Quimico, QuimicoCantidad> {

	private static final long serialVersionUID = -5278662193977929538L;

	public PanTablaQuimicoCantidad(Dialog owner, String descripcionTipoMateriaPrima, List<Quimico> allFormulablesPosibles) {
		super(owner, descripcionTipoMateriaPrima, allFormulablesPosibles);
	}

	@Override
	public QuimicoCantidad createMateriaPrimaCantidad() {
		QuimicoCantidad quimicoCantidad = new QuimicoCantidad();
		quimicoCantidad.setCantidad(0f);
		return quimicoCantidad;
	}

	@Override
	public String createLabelTipoMateriaPrima() {
		return "Químico";
	}

}

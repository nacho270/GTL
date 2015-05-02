package ar.com.textillevel.gui.modulos.odt.gui;

import java.awt.Dialog;
import java.util.List;
import ar.com.textillevel.entidades.ventas.materiaprima.Reactivo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.ReactivoCantidad;

public class PanTablaReactivoCantidad extends PanelTablaMateriaPrimaCantidad<Reactivo, ReactivoCantidad> {

	private static final long serialVersionUID = 6413672431114825956L;

	public PanTablaReactivoCantidad(Dialog owner, String descripcionTipoMateriaPrima, List<Reactivo> allFormulablesPosibles) {
		super(owner, descripcionTipoMateriaPrima, allFormulablesPosibles);
	}

	@Override
	public ReactivoCantidad createMateriaPrimaCantidad() {
		ReactivoCantidad reactivoCantidad = new ReactivoCantidad();
		reactivoCantidad.setCantidad(0f);
		return reactivoCantidad;
	}

	@Override
	public String createLabelTipoMateriaPrima() {
		return "Reactivo";
	}

}
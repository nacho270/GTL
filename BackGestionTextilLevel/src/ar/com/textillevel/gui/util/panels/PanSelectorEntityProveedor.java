package ar.com.textillevel.gui.util.panels;

import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarProveedor;

public abstract class PanSelectorEntityProveedor extends PanSelectorEntity<Proveedor> {

	private static final long serialVersionUID = 4212958169131161130L;

	public PanSelectorEntityProveedor() {
		super("Proveedor");
	}

	@Override
	public String toStringEntity(Proveedor entity) {
		return String.valueOf(entity.getNombreCorto());
	}

	@Override
	public Proveedor handleHowToSelectEntity() {
		JDialogSeleccionarProveedor dialogSeleccionarProveedor = new JDialogSeleccionarProveedor(null);
		GuiUtil.centrar(dialogSeleccionarProveedor);
		dialogSeleccionarProveedor.setVisible(true);
		return dialogSeleccionarProveedor.getProveedor();
	}

}
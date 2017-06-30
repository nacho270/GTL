package ar.com.textillevel.gui.util.panels;

import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente.EModoDialogo;

public abstract class PanSelectorEntityCliente extends PanSelectorEntity<Cliente> {

	private static final long serialVersionUID = 8764855529338798612L;

	public PanSelectorEntityCliente() {
		super("Cliente");
	}

	@Override
	public String toStringEntity(Cliente entity) {
		return String.valueOf(entity.getNroCliente());
	}

	@Override
	public Cliente handleHowToSelectEntity() {
		JDialogSeleccionarCliente dialogSeleccionarCliente = new JDialogSeleccionarCliente(null, EModoDialogo.MODO_ID);
		GuiUtil.centrar(dialogSeleccionarCliente);
		dialogSeleccionarCliente.setVisible(true);
		return dialogSeleccionarCliente.getCliente();
	}

}
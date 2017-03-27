package main.acciones.facturacion;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.Action;

import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.gui.acciones.JDialogAgregarRemitoEntradaStock;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente.EModoDialogo;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;

public class AgregarRemitoEntradaStockAction implements Action {

	private final Frame frame;

	public AgregarRemitoEntradaStockAction(Frame frame) {
		this.frame = frame;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
	}

	public Object getValue(String key) {
		return null;
	}

	public boolean isEnabled() {
		return true;
	}

	public void putValue(String key, Object value) {
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
	}

	public void setEnabled(boolean b) {
	}

	public void actionPerformed(ActionEvent e) {
		JDialogSeleccionarCliente dialogSeleccionarCliente = new JDialogSeleccionarCliente(frame, EModoDialogo.MODO_ID);
		GuiUtil.centrar(dialogSeleccionarCliente);
		dialogSeleccionarCliente.setVisible(true);
		Cliente clienteElegido = dialogSeleccionarCliente.getCliente();
		if(clienteElegido != null) {
			RemitoEntrada remitoEntrada = new RemitoEntrada();
			remitoEntrada.setCliente(clienteElegido);
			JDialogAgregarRemitoEntradaStock dialogAgregarRemitoEntrada = new JDialogAgregarRemitoEntradaStock(frame, remitoEntrada, new ArrayList<OrdenDeTrabajo>(), false);
			GuiUtil.centrar(dialogAgregarRemitoEntrada);		
			dialogAgregarRemitoEntrada.setVisible(true);
		}
	}

}
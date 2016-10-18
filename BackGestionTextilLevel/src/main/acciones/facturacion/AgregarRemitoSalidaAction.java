package main.acciones.facturacion;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.Action;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.gui.acciones.JDialogSeleccionarRemitoEntrada;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;

public class AgregarRemitoSalidaAction implements Action {

	private final Frame frame;

	public AgregarRemitoSalidaAction(Frame frame) {
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
		JDialogSeleccionarCliente dialogSeleccionarCliente = new JDialogSeleccionarCliente(frame);
		GuiUtil.centrar(dialogSeleccionarCliente);
		dialogSeleccionarCliente.setVisible(true);
		Cliente clienteElegido = dialogSeleccionarCliente.getCliente();
		if (clienteElegido != null) {
			JDialogSeleccionarRemitoEntrada dialogSeleccionarODTs = new JDialogSeleccionarRemitoEntrada(frame, clienteElegido);
			GuiUtil.centrar(dialogSeleccionarODTs);
			dialogSeleccionarODTs.setVisible(true);
			List<OrdenDeTrabajo> odtList = dialogSeleccionarODTs.getOdtList();
			if (odtList == null || odtList.isEmpty()) {
				return;
			} else {
				IngresoRemitoSalidaNormalHandler handler = new IngresoRemitoSalidaNormalHandler(frame, clienteElegido, odtList);
				handler.gestionarIngresoRemitoSalida();
			}
		}
	}

}
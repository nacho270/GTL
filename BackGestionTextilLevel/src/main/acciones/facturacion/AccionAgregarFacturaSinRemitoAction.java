package main.acciones.facturacion;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.facade.api.remote.DocumentoContableFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogCargaFactura;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente.EModoDialogo;
import ar.com.textillevel.util.GTLBeanFactory;

public class AccionAgregarFacturaSinRemitoAction implements Action {

	private final Frame frame;

	public AccionAgregarFacturaSinRemitoAction(Frame frame) {
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
		if (clienteElegido != null) {
			Integer proxNroFactura = GTLBeanFactory.getInstance().getBean2(DocumentoContableFacadeRemote.class).getProximoNroDocumentoContable(clienteElegido.getPosicionIva(), ETipoDocumento.FACTURA);
			JDialogCargaFactura jdcf = new JDialogCargaFactura(frame, proxNroFactura, clienteElegido);
			jdcf.setVisible(true);
		}
	}
}

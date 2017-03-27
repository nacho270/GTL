package main.acciones.facturacion;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.facade.api.remote.RemitoEntradaFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogCompletarPiezasConODTRemitoEntrada;
import ar.com.textillevel.gui.acciones.JDialogSelRemitoEntradaClienteConPiezasSinODT;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente.EModoDialogo;
import ar.com.textillevel.util.GTLBeanFactory;

public class CompletarPiezasRemitoEntradaStockAction implements Action {

	private final Frame frame;
	private RemitoEntradaFacadeRemote remitoEntradaFacade;

	public CompletarPiezasRemitoEntradaStockAction(Frame frame) {
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
			JDialogSelRemitoEntradaClienteConPiezasSinODT dialogo = new JDialogSelRemitoEntradaClienteConPiezasSinODT(frame, clienteElegido);
			GuiUtil.centrar(dialogo);		
			dialogo.setVisible(true);
			RemitoEntrada re = dialogo.getRemitoEntrada();
			if(re != null) {
				JDialogCompletarPiezasConODTRemitoEntrada dialogo2 = new JDialogCompletarPiezasConODTRemitoEntrada(frame, re);
				GuiUtil.centrar(dialogo2);
				dialogo2.setVisible(true);
			}
		}
	}

	public RemitoEntradaFacadeRemote getRemitoEntradaFacade() {
		if(remitoEntradaFacade == null) {
			remitoEntradaFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaFacadeRemote.class);
		}
		return remitoEntradaFacade;
	}

}
package main.acciones.facturacion;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.recibo.Recibo;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.facade.api.remote.ReciboFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogCargaRecibo;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente.EModoDialogo;
import ar.com.textillevel.util.GTLBeanFactory;

public class AgregarReciboAction implements Action {

	private Frame frame;

	public AgregarReciboAction(Frame frame) {
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
			Recibo recibo = new Recibo();
			recibo.setCliente(clienteElegido);
			recibo.setNroRecibo(getProximoNroRecibo());
			JDialogCargaRecibo dialogCargarRecibo = new JDialogCargaRecibo(frame, recibo, false);
			GuiUtil.centrar(dialogCargarRecibo);		
			dialogCargarRecibo.setVisible(true);
		}
	}

	private Integer getProximoNroRecibo() {
		ReciboFacadeRemote reciboFacade = GTLBeanFactory.getInstance().getBean2(ReciboFacadeRemote.class);
		Integer lastNroRecibo = reciboFacade.getLastNroRecibo();
		if(lastNroRecibo == null) {
			ParametrosGeneralesFacadeRemote paramGeneralesFacade = GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class);
			Integer nroComienzoRecibo = paramGeneralesFacade.getParametrosGenerales().getNroComienzoRecibo();
			if(nroComienzoRecibo == null) {
				throw new RuntimeException("Falta configurar el número de comienzo de recibo en los parámetros generales");
			}
			lastNroRecibo = nroComienzoRecibo;
		} else {
			lastNroRecibo++;
		}
		return lastNroRecibo;
	}

}
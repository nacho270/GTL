package main.acciones.facturacion;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JOptionPane;

import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.recibo.Recibo;
import ar.com.textillevel.facade.api.remote.ReciboFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogCargaRecibo;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

public class ConsultarReciboAction implements Action{

	private Frame frame;
	
	public ConsultarReciboAction(Frame frame){
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
		try {
			ReciboFacadeRemote rfr = GTLBeanFactory.getInstance().getBean(ReciboFacadeRemote.class);
			Recibo recibo = null;
			boolean okRecibo = false;
			String inputRecibo = null;
			do {
				if(!okRecibo) {
					inputRecibo = JOptionPane.showInputDialog(frame, "Ingrese el número de recibo: ", "Buscar Recibo", JOptionPane.INFORMATION_MESSAGE);
					if(inputRecibo == null){
						break;
					}
					if(inputRecibo.trim().length()==0 || !GenericUtils.esNumerico(inputRecibo)) {
						CLJOptionPane.showErrorMessage(frame, "Ingreso incorrecto", "error");
					} else {
						recibo = rfr.getByNroReciboEager(Integer.valueOf(inputRecibo.trim()));
						if(recibo == null){
							CLJOptionPane.showErrorMessage(frame, "Recibo no encontrado", "Error");
						}else{
							okRecibo = true;
							JDialogCargaRecibo dialogCargaRecibo = new JDialogCargaRecibo(frame, recibo, true);
							GuiUtil.centrar(dialogCargaRecibo);
							dialogCargaRecibo.setVisible(true);
							break;
						}
					}
				}
			} while (!okRecibo);

		} catch (CLException e1) {
			BossError.gestionarError(e1);
		}
	}
}

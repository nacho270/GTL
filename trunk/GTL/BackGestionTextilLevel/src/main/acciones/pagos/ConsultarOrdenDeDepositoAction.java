package main.acciones.pagos;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JOptionPane;

import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.com.textillevel.entidades.documentos.ordendedeposito.OrdenDeDeposito;
import ar.com.textillevel.facade.api.remote.OrdenDeDepositoFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogCargaOrdenDeposito;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

public class ConsultarOrdenDeDepositoAction implements Action{

	private final Frame padre;
	
	public ConsultarOrdenDeDepositoAction(Frame padre){
		this.padre = padre;
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
			OrdenDeDepositoFacadeRemote oddfr = GTLBeanFactory.getInstance().getBean(OrdenDeDepositoFacadeRemote.class);
			OrdenDeDeposito orden = null;
			boolean ok = false;
			do {
				String input = JOptionPane.showInputDialog(padre, "Ingrese el n�mero de �rden: ", "Buscar �rden de dep�sito", JOptionPane.INFORMATION_MESSAGE);
				if(input == null){
					break;
				}
				if (input.trim().length()==0 || !GenericUtils.esNumerico(input)) {
					CLJOptionPane.showErrorMessage(padre, "Ingreso incorrecto", "error");
				} else {
					orden = oddfr.getOrdenByNro(Integer.valueOf(input.trim()));
					if(orden == null){
						CLJOptionPane.showErrorMessage(padre, "Orden no encontrada", "Error");
					}else{
						ok = true;
						new JDialogCargaOrdenDeposito(padre, orden).setVisible(true);	
					}
				}
			} while (!ok);

		} catch (CLException e1) {
			BossError.gestionarError(e1);
		}
	}
}

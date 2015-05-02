package main.acciones.pagos;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JOptionPane;

import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.com.textillevel.entidades.documentos.ordendepago.OrdenDePago;
import ar.com.textillevel.facade.api.remote.OrdenDePagoFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogCargaOrdenDePago;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

public class EditarOrdenDePagoAction implements Action{

	private final Frame padre;
	
	public EditarOrdenDePagoAction(Frame padre){
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
			OrdenDePagoFacadeRemote opfr = GTLBeanFactory.getInstance().getBean(OrdenDePagoFacadeRemote.class);
			OrdenDePago orden = null;
			boolean ok = false;
			do {
				String input = JOptionPane.showInputDialog(padre, "Ingrese el n�mero de la �rden: ", "Buscar Orden de pago", JOptionPane.INFORMATION_MESSAGE);
				if(input == null){
					break;
				}
				if (input.trim().length()==0 || !GenericUtils.esNumerico(input)) {
					CLJOptionPane.showErrorMessage(padre, "Ingreso incorrecto", "error");
				} else {
					orden = opfr.getOrdenDePagoByNroOrdenEager(Integer.valueOf(input.trim()));
					if(orden == null){
						CLJOptionPane.showErrorMessage(padre, "Orden de pago no encontrada", "Error");
					}else{
						ok = true;
						JDialogCargaOrdenDePago dialog = new JDialogCargaOrdenDePago(padre, orden,false);
						dialog.setVisible(true);
					}
				}
			} while (!ok);

		} catch (CLException e1) {
			BossError.gestionarError(e1);
		}
	}

}

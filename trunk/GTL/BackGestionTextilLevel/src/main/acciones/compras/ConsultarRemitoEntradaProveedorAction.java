package main.acciones.compras;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

public class ConsultarRemitoEntradaProveedorAction implements Action{

	private Frame frame;
	
	public ConsultarRemitoEntradaProveedorAction(Frame padre){
		this.frame = padre;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		
	}

	public Object getValue(String key) {
		return null;
	}

	public boolean isEnabled() {
		return false;
	}

	public void putValue(String key, Object value) {
		
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		
	}

	public void setEnabled(boolean b) {

	}

	public void actionPerformed(ActionEvent e) {
		JDialogConsultarRemitosEntradaProveedor dialogo = new JDialogConsultarRemitosEntradaProveedor(frame);
		dialogo.setVisible(true);
	}

}

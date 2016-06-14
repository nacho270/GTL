package main.acciones.facturacion;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

public class RetornarRemitoEntradaAction implements Action {

	private final Frame frame;

	public RetornarRemitoEntradaAction(Frame frame) {
		this.frame = frame;
	}

	public Object getValue(String key) {
		return null;
	}

	public void putValue(String key, Object value) {
		
	}

	public void setEnabled(boolean b) {
		
	}
	
	public boolean isEnabled() {
//		return GenericUtils.isSistemaTest();
		return true;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		
	}

	public void actionPerformed(ActionEvent e) {
		new JDialogRetornarRemitoDeEntrada(frame).setVisible(true);
	}
}

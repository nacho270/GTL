package main.acciones.cuentas;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import ar.com.textillevel.gui.acciones.cuentaarticulo.JFrameVerMovimientosCuentaArticulo;

public class VerMovimientosCuentaArticuloAction implements Action {

	private final Frame frame;
	
	public VerMovimientosCuentaArticuloAction(Frame frame){
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
		new JFrameVerMovimientosCuentaArticulo(frame).setVisible(true);
	}

}

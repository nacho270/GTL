package ar.com.textillevel.gui.modulos.odt.gui.tenido;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

public class VerAdministrarFormulasAction implements Action {
	
	public VerAdministrarFormulasAction(Frame frame){

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
		JFrameAdministrarFormulas.getInstance().mostrarVentana();
	}
}

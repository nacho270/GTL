package main.acciones.modulos.productos;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import ar.com.textillevel.gui.modulos.abm.listaprecios.JDialogAumentadorDePrecios;

public class AumentarPreciosAction  implements Action {

	private final Frame frame;
	
	public AumentarPreciosAction(Frame frame){
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
		new JDialogAumentadorDePrecios(frame).setVisible(true);
	}
}
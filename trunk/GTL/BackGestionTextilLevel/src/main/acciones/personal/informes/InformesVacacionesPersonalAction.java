package main.acciones.personal.informes;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import ar.com.textillevel.gui.modulos.personal.informes.vacaciones.JDialogParametrosInformeVacaciones;

public class InformesVacacionesPersonalAction implements Action {

	private final Frame frame;

	public InformesVacacionesPersonalAction(Frame frame) {
		this.frame = frame;
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
		new JDialogParametrosInformeVacaciones(frame).setVisible(true);
	}
}

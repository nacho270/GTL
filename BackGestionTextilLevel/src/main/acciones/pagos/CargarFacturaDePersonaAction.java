package main.acciones.pagos;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.gente.Persona;
import ar.com.textillevel.gui.acciones.JDialogAgregarFacturaPersona;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarPersona;

public class CargarFacturaDePersonaAction implements Action {
	
	private final Frame padre;
	
	public CargarFacturaDePersonaAction(Frame padre){
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
		JDialogSeleccionarPersona jd = new JDialogSeleccionarPersona(padre);
		GuiUtil.centrar(jd);
		jd.setVisible(true);
		Persona p = jd.getPersona();
		if(p!=null){
			new JDialogAgregarFacturaPersona(padre,p).setVisible(true);
		}
	}
}


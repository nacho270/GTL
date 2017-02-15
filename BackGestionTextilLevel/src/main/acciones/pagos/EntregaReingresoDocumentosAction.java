package main.acciones.pagos;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import ar.com.fwcommon.util.GuiUtil;
import main.acciones.pagos.gui.JDialogEntregaReingresoDocumentos;

public class EntregaReingresoDocumentosAction implements Action {
	
	private final Frame padre;
	
	public EntregaReingresoDocumentosAction(Frame padre){
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
		JDialogEntregaReingresoDocumentos jd = new JDialogEntregaReingresoDocumentos(padre);
		GuiUtil.centrar(jd);
		jd.setVisible(true);
	}
}


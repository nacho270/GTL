package main.acciones.facturacion;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import main.acciones.facturacion.gui.JDialogParamBusquedaFactura;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.gui.acciones.JDialogCargaFactura;

public class ConsultarFacturaAction implements Action{
	
	private final Frame frame;
	
	public ConsultarFacturaAction(Frame frame){
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
		JDialogParamBusquedaFactura dialogo = new JDialogParamBusquedaFactura(frame);
		dialogo.setVisible(true);
		Factura factura = dialogo.getFactura();
		if(factura != null) {
			JDialogCargaFactura dialogCargaFactura = new JDialogCargaFactura(frame,factura, true);						
			dialogCargaFactura.setVisible(true);
		}
	}

}
 
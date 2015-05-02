package main.acciones.compras;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.gui.acciones.proveedor.JDialogSeleccionarFacturaProveedor;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarProveedor;

public class ConsultarFacturaProveedorAction implements Action{

	private Frame frame;
	
	public ConsultarFacturaProveedorAction(Frame padre){
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
		JDialogSeleccionarProveedor dialogSeleccionarProveedor = new JDialogSeleccionarProveedor(frame);
		GuiUtil.centrar(dialogSeleccionarProveedor);
		dialogSeleccionarProveedor.setVisible(true);
		Proveedor proveedorElegido = dialogSeleccionarProveedor.getProveedor();
		if(proveedorElegido != null) {
			JDialogSeleccionarFacturaProveedor dialogo = new JDialogSeleccionarFacturaProveedor(frame, proveedorElegido);
			GuiUtil.centrar(dialogo);
			dialogo.setVisible(true);
		}
	}

}

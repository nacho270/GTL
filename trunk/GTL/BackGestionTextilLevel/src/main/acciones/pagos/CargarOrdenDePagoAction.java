package main.acciones.pagos;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.gui.acciones.JDialogCargaOrdenDePago;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarProveedor;

public class CargarOrdenDePagoAction implements Action{

	private final Frame padre;
	
	public CargarOrdenDePagoAction(Frame padre){
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
		JDialogSeleccionarProveedor dialogSeleccionarProveedor = new JDialogSeleccionarProveedor(padre);
		GuiUtil.centrar(dialogSeleccionarProveedor);
		dialogSeleccionarProveedor.setVisible(true);
		Proveedor proveedorElegido = dialogSeleccionarProveedor.getProveedor();
		if(proveedorElegido != null) {
			new JDialogCargaOrdenDePago(padre, proveedorElegido).setVisible(true);
		}
	}
}

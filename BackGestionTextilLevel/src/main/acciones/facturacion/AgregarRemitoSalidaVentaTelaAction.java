package main.acciones.facturacion;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import ar.com.textillevel.entidades.enums.ETipoRemitoSalida;

public class AgregarRemitoSalidaVentaTelaAction implements Action {

	private final Frame frame;

	public AgregarRemitoSalidaVentaTelaAction(Frame frame) {
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
		IngresoRemitoSalidaVentaDeTelaHandler rsHandler = new IngresoRemitoSalidaVentaDeTelaHandler(frame, ETipoRemitoSalida.CLIENTE_VENTA_DE_TELA, false, null);
		rsHandler.gestionarIngresoRemitoSalida();
	}

}
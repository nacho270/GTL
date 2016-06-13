package main.acciones.facturacion;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

public class RetornarRemitoEntradaAction implements Action {

	private final Frame frame;

	public RetornarRemitoEntradaAction(Frame frame) {
		this.frame = frame;
	}

	@Override
	public Object getValue(String key) {
		return null;
	}

	@Override
	public void putValue(String key, Object value) {
		
	}

	@Override
	public void setEnabled(boolean b) {
		
	}

	@Override
	public boolean isEnabled() {
//		return GenericUtils.isSistemaTest();
		return true;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new JDialogRetornarRemitoDeEntrada(frame).setVisible(true);
	}
}

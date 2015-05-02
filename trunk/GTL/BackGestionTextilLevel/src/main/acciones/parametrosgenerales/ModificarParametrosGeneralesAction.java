package main.acciones.parametrosgenerales;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.com.textillevel.entidades.config.ParametrosGenerales;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogParametrosGenerales;
import ar.com.textillevel.util.GTLBeanFactory;

public class ModificarParametrosGeneralesAction implements Action {

	private Frame frame;
	
	public ModificarParametrosGeneralesAction(Frame frame) {
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
		try {
			ParametrosGeneralesFacadeRemote pgfr = GTLBeanFactory.getInstance().getBean(ParametrosGeneralesFacadeRemote.class);
			ParametrosGenerales pg = pgfr.getParametrosGenerales();
			new JDialogParametrosGenerales(frame,pg).setVisible(true);
		} catch (CLException e1) {
			BossError.gestionarError(e1);
		}
	}
}

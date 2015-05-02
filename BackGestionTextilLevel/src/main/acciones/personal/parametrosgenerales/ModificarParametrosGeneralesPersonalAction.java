package main.acciones.personal.parametrosgenerales;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.com.textillevel.gui.modulos.personal.abm.configuracion.JDialogParametrosGenerales;
import ar.com.textillevel.modulos.personal.entidades.configuracion.ParametrosGeneralesPersonal;
import ar.com.textillevel.modulos.personal.facade.api.remote.ParametrosGeneralesPersonalFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class ModificarParametrosGeneralesPersonalAction implements Action {

	private final Frame frame;

	public ModificarParametrosGeneralesPersonalAction(Frame frame) {
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
			ParametrosGeneralesPersonalFacadeRemote pgfr = GTLPersonalBeanFactory.getInstance().getBean(ParametrosGeneralesPersonalFacadeRemote.class);
			ParametrosGeneralesPersonal pg = pgfr.getParametrosGenerales();
			new JDialogParametrosGenerales(frame, pg).setVisible(true);
		} catch (CLException e1) {
			BossError.gestionarError(e1);
		}
	}
}

package main.acciones.cambioskin;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import ar.com.fwcommon.boss.BossError;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.textillevel.gui.util.ESkin;
import main.GTLMainTemplate;

public class CambioSkinAzulAction implements Action{

	private Frame frame;
	private GTLMainTemplate mainTemplate;
	
	public CambioSkinAzulAction(Frame frame, GTLMainTemplate mainTemplate){
		this.frame = frame;
		this.mainTemplate = mainTemplate;
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
		try {
			mainTemplate.cambiarSkin(ESkin.AZUL);
		} catch (FWException e1) {
			BossError.gestionarError(e1);
		}
	}
	
	public Frame getFrame() {
		return frame;
	}

	
	public void setFrame(Frame frame) {
		this.frame = frame;
	}

	
	public GTLMainTemplate getMainTemplate() {
		return mainTemplate;
	}

	
	public void setMainTemplate(GTLMainTemplate mainTemplate) {
		this.mainTemplate = mainTemplate;
	}
}

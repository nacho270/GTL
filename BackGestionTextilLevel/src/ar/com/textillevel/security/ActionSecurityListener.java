package ar.com.textillevel.security;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;

import main.GTLGlobalCache;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.textillevel.entidades.portal.AccionesModulo;

public abstract class ActionSecurityListener implements ActionListener {

	private JButton owner;
	private EAccion accion;

	public ActionSecurityListener(JButton owner, EAccion accion) {
		this.owner = owner;
		this.accion = accion;
		if(!checkPermisos()) {
			this.owner.setVisible(false);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if(checkPermisos()) {
			execute(e);
		} else {
			FWJOptionPane.showInformationMessage(null, "No tiene permisos para ejecutar la accion", "Información");
		}
	}

	private boolean checkPermisos() {
		List<AccionesModulo> accionesModulo = GTLGlobalCache.getInstance().getUsuarioSistema().getPerfil().getAccionesModulo();
		for(AccionesModulo am : accionesModulo) {
			if(am.getAccion().getId().equals(accion.getId())) {
				return true;
			}
		}
		return false;
	}

	public abstract void execute(ActionEvent e);

}
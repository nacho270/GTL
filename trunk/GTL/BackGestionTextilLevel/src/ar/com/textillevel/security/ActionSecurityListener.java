package ar.com.textillevel.security;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;

import main.GTLGlobalCache;
import ar.clarin.fwjava.componentes.CLJOptionPane;
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
			CLJOptionPane.showInformationMessage(null, "No tiene permisos para ejecutar la accion", "Informaci�n");
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
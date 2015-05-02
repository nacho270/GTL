package ar.com.textillevel.modulos.alertas.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.entidades.portal.Perfil;
import ar.com.textillevel.modulos.alertas.dao.api.local.AlertaDAOLocal;
import ar.com.textillevel.modulos.alertas.entidades.Alerta;
import ar.com.textillevel.modulos.alertas.facade.api.remote.AlertaFacadeRemote;

@Stateless
public class AlertaFacade implements AlertaFacadeRemote{

	@EJB
	private AlertaDAOLocal alertaDAO;
	
	public void crearAlerta(Alerta alerta) {
		if(!alertaDAO.existeAlerta(alerta)){
			alertaDAO.save(alerta);
		}
	}

	public void save(Alerta alerta) {
		alertaDAO.save(alerta);		
	}

	public List<Alerta> getAlertasVigentesByPerfil(Perfil perfil) {
		return alertaDAO.getAlertasVigentesByPerfil(perfil);
	}
}

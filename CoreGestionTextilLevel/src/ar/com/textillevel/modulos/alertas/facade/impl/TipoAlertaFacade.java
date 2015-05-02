package ar.com.textillevel.modulos.alertas.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.modulos.alertas.dao.api.local.TipoAlertaDAOLocal;
import ar.com.textillevel.modulos.alertas.entidades.TipoAlerta;
import ar.com.textillevel.modulos.alertas.facade.api.remote.TipoAlertaFacadeRemote;

@Stateless
public class TipoAlertaFacade implements TipoAlertaFacadeRemote{

	@EJB 
	private TipoAlertaDAOLocal tipoAlertaDAO;
	
	public TipoAlerta getById(Integer id) {
		return tipoAlertaDAO.getById(id);
	}

	public TipoAlerta getReferenceById(Integer id) {
		return tipoAlertaDAO.getReferenceById(id);
	}

	public List<TipoAlerta> getAllOrderByName() {
		return tipoAlertaDAO.getAllOrderBy("descripcion");
	}
}

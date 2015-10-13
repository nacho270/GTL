package ar.com.textillevel.modulos.personal.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.entidades.Mes;
import ar.com.textillevel.modulos.personal.dao.api.MesDAOLocal;
import ar.com.textillevel.modulos.personal.facade.api.remote.MesFacadeRemote;

@Stateless
public class MesFacade implements MesFacadeRemote {

	@EJB
	private MesDAOLocal mesDAO;
	
	public List<Mes> getAllMeses() {
		return mesDAO.getAllOrderBy("nroMes");
	}

}

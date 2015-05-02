package ar.com.textillevel.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.facade.api.remote.QuincenaFacadeRemote;
import ar.com.textillevel.modulos.personal.dao.api.QuincenaDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.Quincena;

@Stateless
public class QuincenaFacade implements QuincenaFacadeRemote {

	@EJB
	private QuincenaDAOLocal quincenaDao;
	
	public Quincena getById(Integer idQuincena) {
		return quincenaDao.getById(idQuincena);
	}

	public List<Quincena> getAllOrderByName() {
		return quincenaDao.getAllOrderBy("nombre");
	}

}

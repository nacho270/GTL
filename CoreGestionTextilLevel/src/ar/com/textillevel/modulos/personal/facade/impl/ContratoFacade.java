package ar.com.textillevel.modulos.personal.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.modulos.personal.dao.api.ContratoDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.contratos.Contrato;
import ar.com.textillevel.modulos.personal.facade.api.remote.ContratoFacadeRemote;

@Stateless
public class ContratoFacade implements ContratoFacadeRemote{

	@EJB
	private ContratoDAOLocal contratoDao;
	
	public List<Contrato> getAll() {
		return contratoDao.getAll();
	}

}

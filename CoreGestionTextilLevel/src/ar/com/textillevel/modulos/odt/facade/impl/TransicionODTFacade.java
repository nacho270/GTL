package ar.com.textillevel.modulos.odt.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.modulos.odt.dao.api.local.TransicionODTDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.workflow.TransicionODT;
import ar.com.textillevel.modulos.odt.facade.api.remote.TransicionODTFacadeRemote;

@Stateless
public class TransicionODTFacade implements TransicionODTFacadeRemote {

	@EJB
	private TransicionODTDAOLocal transicionDao;

	public List<TransicionODT> getAllEagerByODT(Integer idODT) {
		return transicionDao.getAllEagerByODT(idODT);
	}
}

package ar.com.textillevel.modulos.personal.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.modulos.personal.dao.api.EmpresaSegurosDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.commons.EmpresaSeguros;
import ar.com.textillevel.modulos.personal.facade.api.remote.EmpresaSegurosFacadeRemote;

@Stateless
public class EmpresaSegurosFacade implements EmpresaSegurosFacadeRemote{

	@EJB
	private EmpresaSegurosDAOLocal empresaSegurosDao;
	
	public EmpresaSeguros save(EmpresaSeguros empresaSeguros) {
		return empresaSegurosDao.save(empresaSeguros);
	}

	public void remove(EmpresaSeguros empresaSeguros) {
		empresaSegurosDao.removeById(empresaSeguros.getId());
	}

	public List<EmpresaSeguros> getAllOrderByName() {
		return empresaSegurosDao.getAllOrderBy("nombre");
	}
}

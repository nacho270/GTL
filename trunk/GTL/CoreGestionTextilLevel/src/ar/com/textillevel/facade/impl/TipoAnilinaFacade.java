package ar.com.textillevel.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.dao.api.local.TipoAnilinaDAOLocal;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.TipoAnilina;
import ar.com.textillevel.facade.api.remote.TipoAnilinaFacadeRemote;

@Stateless
public class TipoAnilinaFacade implements TipoAnilinaFacadeRemote{
	@EJB
	private TipoAnilinaDAOLocal tipoAnilinaDao;
	
	public List<TipoAnilina> getAllOrderByName() {
		return tipoAnilinaDao.getAllOrderBy("descripcion");
	}

	public void remove(TipoAnilina tipoAnilina) {
		tipoAnilinaDao.removeById(tipoAnilina.getId());
	}

	public TipoAnilina save(TipoAnilina tipoAnilina) {
		return tipoAnilinaDao.save(tipoAnilina);
	}

	public TipoAnilina getByIdEager(Integer id) {
		return tipoAnilinaDao.getByIdEager(id);
	}
}

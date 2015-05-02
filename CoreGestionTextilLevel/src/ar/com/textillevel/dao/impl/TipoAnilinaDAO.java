package ar.com.textillevel.dao.impl;

import javax.ejb.Stateless;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.TipoAnilinaDAOLocal;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.TipoAnilina;

@Stateless
public class TipoAnilinaDAO extends GenericDAO<TipoAnilina, Integer> implements TipoAnilinaDAOLocal {

	public TipoAnilina getByIdEager(Integer id) {
		TipoAnilina ta = getById(id);
		ta.getTiposArticulosSoportados().size();
		return ta;
	}

}

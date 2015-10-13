package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.ModuloDAOLocal;
import ar.com.textillevel.entidades.portal.Modulo;

@Stateless
public class ModuloDAO extends GenericDAO<Modulo, Integer> implements ModuloDAOLocal {

	public List<Modulo> getAllWithActions() {
		// TODO Auto-generated method stub
		return null;
	}

}

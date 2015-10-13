package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.ColorDAOLocal;
import ar.com.textillevel.entidades.ventas.articulos.Color;

@Stateless
public class ColorDAO extends GenericDAO<Color, Integer> implements ColorDAOLocal{

	@SuppressWarnings("unchecked")
	public List<Color> getAllOrderByNameGamaEager() {
		return getEntityManager().createQuery("SELECT c FROM Color c LEFT JOIN FETCH c.gama ORDER BY c.nombre").getResultList();
	}
}

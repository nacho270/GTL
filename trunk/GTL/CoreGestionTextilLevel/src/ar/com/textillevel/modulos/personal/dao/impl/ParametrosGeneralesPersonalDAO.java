package ar.com.textillevel.modulos.personal.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.ParametrosGeneralesPersonalDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.configuracion.ParametrosGeneralesPersonal;

@Stateless
public class ParametrosGeneralesPersonalDAO extends GenericDAO<ParametrosGeneralesPersonal, Integer> implements ParametrosGeneralesPersonalDAOLocal{

	@SuppressWarnings("unchecked")
	public ParametrosGeneralesPersonal getParametrosGenerales() {
		Query query = getEntityManager().createQuery("FROM ParametrosGeneralesPersonal");
		List<ParametrosGeneralesPersonal> resultList = query.getResultList();
		if(resultList.isEmpty()) {
			//throw new RuntimeException("Falta agregar la configuraci�n de Par�metros Generales!!");
			return null;
		}
		if(resultList.size()>1) {
			throw new RuntimeException("Hay m�s de una configuraci�n cargada!!!");
		}
		return resultList.get(0);
	}
}

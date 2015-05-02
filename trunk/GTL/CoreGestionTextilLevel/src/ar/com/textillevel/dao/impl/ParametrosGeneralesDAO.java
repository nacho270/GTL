package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.ParametrosGeneralesDAOLocal;
import ar.com.textillevel.entidades.config.ConfiguracionNumeracionFactura;
import ar.com.textillevel.entidades.config.ParametrosGenerales;

@Stateless
public class ParametrosGeneralesDAO extends GenericDAO<ParametrosGenerales, Integer> implements ParametrosGeneralesDAOLocal {

	@SuppressWarnings("unchecked")
	public ParametrosGenerales getParametrosGenerales() {
		Query query = getEntityManager().createQuery("FROM ParametrosGenerales");
		List<ParametrosGenerales> resultList = query.getResultList();
		if(resultList.isEmpty()) {
			//throw new RuntimeException("Falta agregar la configuraci�n de Par�metros Generales!!");
			return null;
		}
		if(resultList.size()>1) {
			throw new RuntimeException("Hay m�s de una configuraci�n cargada!!!");
		}
		ParametrosGenerales parametrosGenerales = resultList.get(0);
		ConfiguracionNumeracionFactura configuracionFacturaA = parametrosGenerales.getConfiguracionFacturaA();
		if(configuracionFacturaA!=null){
			configuracionFacturaA.getTipoFactura();
		}
		ConfiguracionNumeracionFactura configuracionFacturaB = parametrosGenerales.getConfiguracionFacturaB();
		if(configuracionFacturaB!=null){
			configuracionFacturaB.getTipoFactura();
		}
		return parametrosGenerales;
	}

}

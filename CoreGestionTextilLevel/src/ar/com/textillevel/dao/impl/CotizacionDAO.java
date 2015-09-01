package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.CotizacionDAOLocal;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.cotizacion.Cotizacion;

@Stateless
public class CotizacionDAO extends GenericDAO<Cotizacion, Integer> implements CotizacionDAOLocal {

	@SuppressWarnings("unchecked")
	public Cotizacion getUltimaCotizacion(Cliente cliente) {
		Query query = getEntityManager().createQuery("SELECT cot " +
				 									"FROM Cotizacion AS cot " +
													"WHERE cot.cliente.id = :idCliente " + 
													"ORDER BY cot.fechaInicio DESC, cot.id DESC");
		query.setParameter("idCliente", cliente.getId());
		List<Cotizacion> resultList = query.getResultList();
		if(resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0);
		}
	}

}

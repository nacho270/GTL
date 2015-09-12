package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.ListaDePreciosDAOLocal;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.cotizacion.ListaDePrecios;

@Stateless
public class ListaDePreciosDAO extends GenericDAO<ListaDePrecios, Integer> implements ListaDePreciosDAOLocal {

	@SuppressWarnings("unchecked")
	public ListaDePrecios getListaByIdCliente(Integer idCliente) {
		Query query = getEntityManager().createQuery("SELECT lc " +
													 "FROM ListaDePrecios AS lc " +
													 "WHERE lc.cliente.id = :idCliente ");
		query.setParameter("idCliente", idCliente);
		List<ListaDePrecios> resultList = query.getResultList();
		if(resultList.size()> 1) {
			throw new RuntimeException("Existe más de una lista de precios para el cliente con ID " + idCliente);
		} else if(resultList.size() == 1) {
			return resultList.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Cliente> getClientesConListaDePrecios() {
		Query q = getEntityManager().createQuery(" SELECT lc.cliente FROM ListaDePrecios AS lc ORDER BY lc.cliente.razonSocial ASC ");
		return q.getResultList();
	}

}

package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.ListaDePreciosDAOLocal;
import ar.com.textillevel.entidades.ventas.productos.ListaDePrecios;

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
			throw new RuntimeException("Existe m�s de una lista de precios para el cliente con ID " + idCliente);
		} else if(resultList.size() == 1) {
			ListaDePrecios listaDePrecios = resultList.get(0);
			listaDePrecios.getPrecios().size();
			return listaDePrecios;
		}
		return null;
	}

}

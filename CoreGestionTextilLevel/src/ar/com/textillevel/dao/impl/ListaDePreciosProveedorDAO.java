package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.ListaDePreciosProveedorDAOLocal;
import ar.com.textillevel.entidades.ventas.materiaprima.ListaDePreciosProveedor;

@Stateless
@SuppressWarnings("unchecked")
public class ListaDePreciosProveedorDAO extends GenericDAO<ListaDePreciosProveedor, Integer> implements ListaDePreciosProveedorDAOLocal {

	public ListaDePreciosProveedor getListaByIdProveedor(Integer idProveedor) {
		Query query = getEntityManager().createQuery("SELECT lpp " + 
													 "FROM ListaDePreciosProveedor AS lpp " + 
													 "WHERE lpp.proveedor.id = :idProveedor ");
		query.setParameter("idProveedor", idProveedor);
		List<ListaDePreciosProveedor> resultList = query.getResultList();
		if (resultList.size() > 1) {
			throw new RuntimeException("Existe más de una lista de precios para el proveedor con ID " + idProveedor);
		} else if (resultList.size() == 1) {
			ListaDePreciosProveedor listaDePrecios = resultList.get(0);
			listaDePrecios.getPrecios().size();
			return listaDePrecios;
		}
		return null;
	}

}

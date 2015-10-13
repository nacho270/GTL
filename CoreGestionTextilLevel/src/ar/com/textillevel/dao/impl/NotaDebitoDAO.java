package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.NotaDebitoDAOLocal;
import ar.com.textillevel.entidades.documentos.factura.NotaDebito;

@Stateless
@SuppressWarnings("unchecked")
public class NotaDebitoDAO extends GenericDAO<NotaDebito, Integer> implements NotaDebitoDAOLocal {

	public List<NotaDebito> getNotaDebitoPendientePagarList(Integer idCliente) {
		Query query = getEntityManager().createQuery("FROM NotaDebito nd " +
													 "WHERE nd.cliente.id = :idCliente AND " +
													 "		nd.montoFaltantePorPagar > 0 " +
													 "ORDER BY nd.fechaEmision ");
		query.setParameter("idCliente", idCliente);
		return query.getResultList();
	}

	public List<NotaDebito> getAllByIdCliente(Integer idCliente) {
		Query query = getEntityManager().createQuery("SELECT nd FROM NotaDebito nd " +
				 									 "WHERE nd.cliente.id = :idCliente AND (nd.anulada is null OR nd.anulada = false) " +
				 									 "ORDER BY nd.fechaEmision ");
		query.setParameter("idCliente", idCliente);
		return query.getResultList();
	}

}

package ar.com.textillevel.dao.impl;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.FormaPagoOrdenDePagoDAOLocal;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaCreditoProveedor;
import ar.com.textillevel.entidades.documentos.ordendepago.formapago.FormaPagoOrdenDePago;

@Stateless
public class FormaPagoOrdenDePagoDAO extends GenericDAO<FormaPagoOrdenDePago, Integer> implements FormaPagoOrdenDePagoDAOLocal {

	public boolean existsNotaCreditoProvEnFormaPagoOrdenDePago(NotaCreditoProveedor ncp) {
		String hql = "SELECT f FROM FormaPagoOrdenDePagoNotaCredito f WHERE  f.notaCredito = :notaCredito";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("notaCredito", ncp);
		return !q.getResultList().isEmpty();
	}

}

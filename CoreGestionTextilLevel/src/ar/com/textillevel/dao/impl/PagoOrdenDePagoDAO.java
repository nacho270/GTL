package ar.com.textillevel.dao.impl;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.PagoOrdenDePagoDAOLocal;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaDebitoProveedor;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePago;

@Stateless
public class PagoOrdenDePagoDAO extends GenericDAO<PagoOrdenDePago, Integer> implements PagoOrdenDePagoDAOLocal {

	public boolean existsFacturaEnPagoOrdenDePago(FacturaProveedor factura) {
		String hql = " SELECT p FROM PagoOrdenDePagoFactura p WHERE  p.factura = :factura";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("factura",factura);
		return !q.getResultList().isEmpty();
	}

	public boolean existsNotaDebitoProvEnPagoOrdenDePago(NotaDebitoProveedor ndp) {
		String hql = " SELECT p FROM PagoOrdenDePagoNotaDebito p WHERE  p.notaDebito = :notaDebito";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("notaDebito", ndp);
		return !q.getResultList().isEmpty();
	}

}

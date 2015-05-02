package ar.com.textillevel.dao.impl;

import java.sql.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.clarin.fwjava.util.NumUtil;
import ar.com.textillevel.dao.api.local.CorreccionDAOLocal;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.documentos.factura.CorreccionFactura;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.NotaCredito;
import ar.com.textillevel.entidades.documentos.factura.NotaDebito;
import ar.com.textillevel.entidades.gente.Cliente;

@Stateless
@SuppressWarnings("unchecked")
public class CorreccionFacturaDAO extends GenericDAO<CorreccionFactura, Integer> implements CorreccionDAOLocal {

	public CorreccionFactura getCorreccionByNumero(Integer idNumero) {
		String hql = " SELECT c FROM CorreccionFactura c WHERE c.nroFactura = :nroCorreccion ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("nroCorreccion", idNumero);
		List<CorreccionFactura> cors = q.getResultList();
		if(cors == null || cors.isEmpty()){
			return null;
		}
		CorreccionFactura c = cors.get(0);
		c.getCliente().getRazonSocial();
		if(c instanceof NotaCredito){
			((NotaCredito)c).getFacturasRelacionadas().size();
		}
		return c;
	}

	public CorreccionFactura getCorreccionById(Integer idCorreccion) {
		String hql = " SELECT c FROM CorreccionFactura c WHERE c.id = :idCorreccion ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idCorreccion", idCorreccion);
		List<CorreccionFactura> cors = q.getResultList();
		if(cors == null || cors.isEmpty()){
			return null;
		}
		CorreccionFactura c = cors.get(0);
		c.getCliente().getRazonSocial();
		if(c instanceof NotaCredito){
			((NotaCredito)c).getFacturasRelacionadas().size();
		}
		return c;
	}
	
	public List<CorreccionFactura> getCorreccionesByFecha(Date fechaDesde, Date fechaHasta, Cliente cliente) {
		String hql = " SELECT c FROM CorreccionFactura c WHERE c.fechaEmision BETWEEN :fechaDesde AND :fechaHasta "+
					 (cliente !=null?" AND c.cliente.id = :idCliente ": " ");
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("fechaDesde", fechaDesde);
		q.setParameter("fechaHasta", fechaHasta);
		if(cliente!=null){
			q.setParameter("idCliente", cliente.getId());
		}
		List<CorreccionFactura> correcciones = q.getResultList();
		if (correcciones != null && correcciones.size() > 0) {
			return correcciones;
		}
		return null;
	}

	public List<NotaCredito> getNotaCreditoPendienteUsarList(Integer idCliente) {
		String hql = " SELECT nc FROM NotaCredito nc WHERE nc.cliente.id = :idCliente AND nc.montoSobrante > 0 ORDER BY nc.fechaEmision";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idCliente", idCliente);
		return q.getResultList();
	}

	public NotaCredito getNotaDeCreditoByFacturaRelacionada(Factura factura) {
		String hql = " SELECT nc FROM NotaCredito nc WHERE :factura in elements (nc.facturasRelacionadas) ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("factura", factura);
		List<NotaCredito> lista = q.getResultList();
		if(lista != null && !lista.isEmpty()){
			return lista.get(0);
		}
		return null;
	}

	public boolean notaCreditoSeUsaEnRecibo(NotaCredito nc) {
		String hql = " SELECT COUNT(*) FROM  FormaPagoNotaCredito fpnc WHERE  fpnc.notaCredito.id = :id ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", nc.getId());
		return NumUtil.toInteger(q.getSingleResult())>0;
	}

	public boolean notaDebitoSeUsaEnRecibo(NotaDebito nd) {
		String hql = " SELECT COUNT(*) FROM  PagoReciboNotaDebito prnd WHERE  prnd.notaDebito.id = :id ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", nd.getId());
		return NumUtil.toInteger(q.getSingleResult())>0;
	}

	public List<NotaCredito> getAllNotaCreditoList(Integer idCliente) {
		String hql = " SELECT nc FROM NotaCredito nc WHERE nc.cliente.id = :idCliente AND nc.anulada <> true ORDER BY nc.fechaEmision";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idCliente", idCliente);
		return q.getResultList();
	}

	public NotaDebito getNotaDebitoByCheque(Cheque cheque) {
		String hql = " SELECT nd FROM NotaDebito nd WHERE nd.chequeRechazado.id = :idCheque  ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idCheque", cheque.getId());
		NotaDebito nota = (NotaDebito) q.getSingleResult();
		nota.getChequeRechazado().getNombreProveedorSalida();
		return nota;
	}

}
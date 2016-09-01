package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.OrdenDePagoDAOLocal;
import ar.com.textillevel.entidades.documentos.factura.to.InfoCuentaTO;
import ar.com.textillevel.entidades.documentos.ordendepago.OrdenDePago;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePago;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePagoACuenta;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePagoFactura;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePagoNotaDebito;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.visitor.IPagoOrdenPagoVisitor;

@Stateless
public class OrdenDePagoDAO extends GenericDAO<OrdenDePago, Integer> implements OrdenDePagoDAOLocal{

	public Integer getNewNumeroOrdenDePago() {
		String hql = " SELECT MAX(o.nroOrden) FROM OrdenDePago o ";
		Query q = getEntityManager().createQuery(hql);
		Number n = (Number)q.getSingleResult();
		if(n == null){
			return null;
		}
		return n.intValue()+1;
	}

	@SuppressWarnings("unchecked")
	public OrdenDePago getOrdenDePagoByNroOrdenEager(Integer nroOrden) {
		String hql = "SELECT o FROM OrdenDePago o WHERE o.nroOrden = :nroOrden ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("nroOrden", nroOrden);
		List<OrdenDePago> l = q.getResultList();
		if(l==null || l.isEmpty()){
			return null;
		}
		return doEager(l.get(0));
	}
	
	@SuppressWarnings("unchecked")
	public OrdenDePago getByIdEager(Integer idODP) {
		String hql = "SELECT o FROM OrdenDePago o WHERE o.id = :id ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", idODP);
		List<OrdenDePago> l = q.getResultList();
		if(l==null || l.isEmpty()){
			return null;
		}
		return doEager(l.get(0));
	}

	private OrdenDePago doEager(OrdenDePago orden) {
		orden.getProveedor().getCelular();
		orden.getFormasDePago().size();
		orden.getPagos().size();
		PagoOrdenDePagoLazyInitializer initializer = new PagoOrdenDePagoLazyInitializer();
		for(PagoOrdenDePago p : orden.getPagos()){
			p.accept(initializer);
		}
		
		return orden;
	}

	@SuppressWarnings("unchecked")
	public InfoCuentaTO getInfoOrdenDePagoYPagosRecibidos(Integer idProveedor) {
		InfoCuentaTO infoCuentaTO = new InfoCuentaTO();
		Query query = getEntityManager().createNativeQuery("SELECT PODP.F_NOTA_DEBITO_P_ID, PODP.F_FACTURA_PROV_P_ID,ODP.P_ID " +
														   "FROM T_PAGO_ORDEN_DE_PAGO PODP " +
														   "INNER JOIN T_ORDEN_PAGO ODP ON ODP.P_ID = PODP.F_ORDEN_PAGO_P_ID " +
														   "WHERE (PODP.TIPO='PODPND' OR PODP.TIPO='PODPF') AND ODP.F_PROV_P_ID = :idProveedor");
		query.setParameter("idProveedor", idProveedor);
		List<Object[]> resultList = query.getResultList();
		for(Object[] tupla : resultList) {
			Integer idNotDeb = (Integer)tupla[0];
			Integer idFactura = (Integer)tupla[1];
			Integer idOrdenDePago = (Integer)tupla[2];
			if(idNotDeb != null) {
				infoCuentaTO.addInfoNotDeb(idNotDeb, idOrdenDePago);
			}
			if(idFactura != null) {
				infoCuentaTO.addInfoFactura(idFactura, idOrdenDePago);
			}
		}
		return infoCuentaTO;
	}

	private class PagoOrdenDePagoLazyInitializer implements IPagoOrdenPagoVisitor{

		public void visit(PagoOrdenDePagoACuenta popac) {

		}

		public void visit(PagoOrdenDePagoFactura popf) {
			popf.getFactura().getDescuento();
		}

		public void visit(PagoOrdenDePagoNotaDebito popnd) {
			popnd.getNotaDebito().getMontoTotal();
		}
	}

	@SuppressWarnings("unchecked")
	public List<OrdenDePago> getAllByIdProveedor(Integer idProveedor) {
		String hql = "SELECT o FROM OrdenDePago o WHERE o.proveedor.id = :idProveedor ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idProveedor", idProveedor);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public OrdenDePago getByNumero(String numero) {
		String hql = "SELECT o FROM OrdenDePago o WHERE o.nroOrden = :nroOrden ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("nroOrden", Integer.valueOf(numero));
		List<OrdenDePago> l = q.getResultList();
		if(l==null || l.isEmpty()){
			return null;
		}
		return l.get(0);
	}
}
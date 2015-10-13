package ar.com.textillevel.dao.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.ReciboDAOLocal;
import ar.com.textillevel.entidades.documentos.factura.to.InfoCuentaTO;
import ar.com.textillevel.entidades.documentos.recibo.Recibo;
import ar.com.textillevel.entidades.documentos.recibo.pagos.PagoRecibo;
import ar.com.textillevel.entidades.documentos.recibo.pagos.PagoReciboACuenta;
import ar.com.textillevel.entidades.documentos.recibo.pagos.PagoReciboFactura;
import ar.com.textillevel.entidades.documentos.recibo.pagos.PagoReciboNotaDebito;
import ar.com.textillevel.entidades.documentos.recibo.pagos.visitor.IPagoReciboVisitor;
import ar.com.textillevel.entidades.documentos.recibo.to.ResumenReciboTO;
import ar.com.textillevel.entidades.enums.EEstadoRecibo;

@Stateless
public class ReciboDAO extends GenericDAO<Recibo, Integer> implements ReciboDAOLocal {

	public Integer getLastNroRecibo() {
		Query query = getEntityManager().createQuery("SELECT MAX(r.nroRecibo) FROM Recibo AS r");
		Number singleResult = (Number) query.getSingleResult();
		if (singleResult == null) {
			return null;
		} else {
			return singleResult.intValue();
		}
	}

	public Integer getLastNroReciboByCliente(Integer idCliente) {
		Query query = getEntityManager().createQuery("SELECT MAX(r.nroRecibo) FROM Recibo AS r WHERE r.idEstadoRecibo != :idRechazado AND r.cliente.id = :idCliente ");
		query.setParameter("idRechazado", EEstadoRecibo.RECHAZADO.getId());
		query.setParameter("idCliente", idCliente);
		Number singleResult = (Number) query.getSingleResult();
		if (singleResult == null) {
			return null;
		} else {
			return singleResult.intValue();
		}
	}

	public Date getUltimaFechaReciboGrabado() {
		Query query = getEntityManager().createQuery("SELECT MAX(fecha) FROM Recibo");
		return (Date) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public Recibo getByNroReciboEager(Integer nroRecibo) {
		Query query = getEntityManager().createQuery("SELECT r " + "FROM Recibo r " + "WHERE r.nroRecibo = :nroRecibo");
		query.setParameter("nroRecibo", nroRecibo);
		List<Recibo> reciboList = query.getResultList();
		if (reciboList.size() == 0) {
			return null;
		} else if (reciboList.size() == 1) {
			Recibo recibo = reciboList.get(0);
			PagosReciboLazyInitializer prei = new PagosReciboLazyInitializer(); 
			for(PagoRecibo pr : recibo.getPagoReciboList()) {
				pr.accept(prei);
			}
			recibo.getPagos().size();
			return recibo;
		} else {
			throw new RuntimeException("Inconsistencia en la DB - Existe más de un Recibo con el mismo número " + nroRecibo);
		}
	}

	@SuppressWarnings("unchecked")
	public Recibo getByIdEager(Integer id) {
		Query query = getEntityManager().createQuery("SELECT r " + "FROM Recibo r " + "WHERE r.id = :id");
		query.setParameter("id", id);
		List<Recibo> reciboList = query.getResultList();
		if (reciboList.size() == 0) {
			return null;
		} else if (reciboList.size() == 1) {
			Recibo recibo = reciboList.get(0);
			PagosReciboLazyInitializer prei = new PagosReciboLazyInitializer(); 
			for(PagoRecibo pr : recibo.getPagoReciboList()) {
				pr.accept(prei);
			}
			recibo.getPagos().size();
			return recibo;
		} 
		return null;
	}

	@SuppressWarnings("unchecked")
	public Map<Integer, List<Integer>> getMapaRecibosYPagosRecibos() {
		Map<Integer, List<Integer>> ret = new HashMap<Integer, List<Integer>>();
		String hql = "SELECT r FROM Recibo r JOIN FETCH r.pagoReciboList ";
		Query q = getEntityManager().createQuery(hql);
		List<Recibo> recibos = q.getResultList();
		if(recibos != null && recibos.size()>0){
			for(Recibo r : recibos){
				r.getPagoReciboList().size();
				List<Integer> idsPagos = new ArrayList<Integer>();
				for(PagoRecibo pr : r.getPagoReciboList()){
					idsPagos.add(pr.getId());
				}
				ret.put(r.getId(), idsPagos);
			}
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public InfoCuentaTO getInfoReciboYPagosRecibidos(Integer nroCliente) {
		InfoCuentaTO infoCuentaTO = new InfoCuentaTO();
		Query query = getEntityManager().createNativeQuery("SELECT PR.F_NOTA_DEBITO_P_ID, PR.F_FACTURA_P_ID,R.P_ID " +
														   "FROM T_PAGO_RECIBO PR " +
														   "INNER JOIN T_RECIBO R ON R.P_ID = PR.F_RECIBO_P_ID " +
														   "INNER JOIN T_CLIENTE CL ON CL.P_ID = R.F_CLIENTE_P_ID " +
														   "WHERE (PR.TIPO='PRF' OR PR.TIPO='PRND') AND CL.A_NROCLIENTE = :nroCliente");
		query.setParameter("nroCliente", nroCliente);
		List<Object[]> resultList = query.getResultList();
		for(Object[] tupla : resultList) {
			Integer idNotDeb = (Integer)tupla[0];
			Integer idFactura = (Integer)tupla[1];
			Integer idRecibo = (Integer)tupla[2];
			if(idNotDeb != null) {
				infoCuentaTO.addInfoNotDeb(idNotDeb, idRecibo);
			}
			if(idFactura != null) {
				infoCuentaTO.addInfoFactura(idFactura, idRecibo);
			}
		}
		return infoCuentaTO;
	}

	private static class PagosReciboLazyInitializer implements IPagoReciboVisitor {

		public void visit(PagoReciboACuenta prac) {
		}

		public void visit(PagoReciboFactura prf) {
			prf.getFactura().getNroFactura();
		}

		public void visit(PagoReciboNotaDebito prnd) {
			prnd.getNotaDebito().getNroFactura();
		}
	}

	public void rollBackPagosFacturaYNotasDeDebito(Recibo recibo) throws FWException {
		recibo = getByIdEager(recibo.getId());
		RollBackPagoReciboVisitor rbprv = new RollBackPagoReciboVisitor();
		for(PagoRecibo pr : recibo.getPagoReciboList()){
			pr.accept(rbprv);
		}
	}

	private class RollBackPagoReciboVisitor implements IPagoReciboVisitor{

		public void visit(PagoReciboACuenta prac) {	}

		public void visit(PagoReciboFactura prf) {
			String hql = " UPDATE Factura f set f.montoFaltantePorPagar = f.montoTotal, f.idEstado = 1 WHERE f.id = :idFactura ";
			Query q = getEntityManager().createQuery(hql);
			q.setParameter("idFactura", prf.getFactura().getId());
			q.executeUpdate();
		}

		public void visit(PagoReciboNotaDebito prnd) {
			String hql = " UPDATE NotaDebito nd set nd.montoFaltantePorPagar = nd.monto, nd.idEstado = 1 WHERE nd.id = :idNota ";
			Query q = getEntityManager().createQuery(hql);
			q.setParameter("idNota", prnd.getNotaDebito().getId());
			q.executeUpdate();
		}
	}

	@SuppressWarnings("unchecked")
	public List<ResumenReciboTO> getResumenReciboList(Integer idCliente, Date fechaDesde, Date fechaHasta) {
		Map<Integer, ResumenReciboTO> resumenReciboMap = new HashMap<Integer, ResumenReciboTO>();
		Query query = getEntityManager().createNativeQuery(
								  "SELECT rec.P_ID,"
								+ "rec.A_FECHA, "
								+ "rec.A_NRO_RECIBO, "
								+ "fp.TIPO, "
								+ "SUM(fp.A_IMPORTE_PAGO_SIMPLE) as TOT_PAGO_SIMPLE, "
								+ "SUM(fp.A_IMPORTE_NC) as TOT_NC, "
								+ "SUM(ch.A_IMPORTE_CHEQUE) as TOT_CHEQUES "
								+ "FROM T_RECIBO rec " 
								+ "INNER JOIN t_forma_pago fp on rec.p_id = fp.f_recibo_p_id "
								+ "LEFT JOIN t_cheque ch on ch.p_id = fp.f_cheque_p_id "
								+ "WHERE 1=1 "
								+ (idCliente == null ? "" : " AND rec.F_CLIENTE_P_ID = :idCliente ")
								+ (fechaDesde == null ? "" : " AND rec.A_FECHA >= :fechaDesde ")
								+ (fechaHasta == null ? "" : " AND rec.A_FECHA < :fechaHasta ")
								+ "GROUP BY rec.P_ID, rec.A_FECHA, rec.A_NRO_RECIBO, fp.TIPO "
								+ "ORDER BY rec.P_ID ");
		if(idCliente != null) {
			query.setParameter("idCliente", idCliente);
		}
		if(fechaDesde != null) {
			query.setParameter("fechaDesde", fechaDesde);
		}
		if(fechaHasta != null) {
			query.setParameter("fechaHasta", fechaHasta);
		}
		List<Object[]> tmpList = query.getResultList();
		for (Object[] tupla : tmpList) {
			Integer idRecibo = (Integer) tupla[0];
			ResumenReciboTO resumenReciboTO = resumenReciboMap.get(idRecibo);
			Date fecha = (Date) tupla[1];
			Integer nroRecibo = (Integer) tupla[2];
			String tipoPago = (String) tupla[3];
			double totPagoSimple = ((Number) tupla[4]) == null ? 0 : (((Number) tupla[4])).doubleValue();
			double totPagoNC = ((Number) tupla[5]) == null ? 0 : (((Number) tupla[5])).doubleValue();
			double totPagoCh = ((Number) tupla[6]) == null ? 0 : (((Number) tupla[6])).doubleValue();
			if(resumenReciboTO == null) {
				resumenReciboTO = new ResumenReciboTO();
				resumenReciboTO.setIdRecibo(idRecibo);
				resumenReciboTO.setFecha(fecha);
				resumenReciboTO.setNroRecibo(nroRecibo);
				updateResumenReciboTO(resumenReciboTO, tipoPago, totPagoSimple, totPagoNC, totPagoCh);
				resumenReciboMap.put(idRecibo, resumenReciboTO);
			} else {
				updateResumenReciboTO(resumenReciboTO, tipoPago, totPagoSimple, totPagoNC, totPagoCh);
			}
		}
		List<ResumenReciboTO> resultList = new ArrayList<ResumenReciboTO>(resumenReciboMap.values());
		Collections.sort(resultList);
		return resultList;
	}

	private void updateResumenReciboTO(ResumenReciboTO resumenReciboTO, String tipoPago, double totPagoSimple, double totPagoNC, double totPagoCh) {
		resumenReciboTO.setTotalCheques(resumenReciboTO.getTotalCheques() + totPagoCh);
		resumenReciboTO.setTotal(resumenReciboTO.getTotal() + totPagoCh);
		resumenReciboTO.setTotalNC(resumenReciboTO.getTotalNC() + totPagoNC);
		resumenReciboTO.setTotal(resumenReciboTO.getTotal() + totPagoNC);
		if(tipoPago.equals("FPRI")) {
			resumenReciboTO.setTotalIVA(resumenReciboTO.getTotalIVA() + totPagoSimple);
			resumenReciboTO.setTotal(resumenReciboTO.getTotal() + totPagoSimple);
		}
		if(tipoPago.equals("FPE")) {
			resumenReciboTO.setTotalEfectivo(resumenReciboTO.getTotalEfectivo() + totPagoSimple);
			resumenReciboTO.setTotal(resumenReciboTO.getTotal() + totPagoSimple);
		}
		if(tipoPago.equals("FPRIB")) {
			resumenReciboTO.setTotalIIBB(resumenReciboTO.getTotalIIBB() + totPagoSimple);
			resumenReciboTO.setTotal(resumenReciboTO.getTotal() + totPagoSimple);
		}
		if(tipoPago.equals("FPTX")) {
			resumenReciboTO.setTotalTransfBancarias(resumenReciboTO.getTotalTransfBancarias() + totPagoSimple);
			resumenReciboTO.setTotal(resumenReciboTO.getTotal() + totPagoSimple);
		}
	}

	public boolean existsNroRecibo(Integer idRecibo, Integer nroRecibo) {
		Query query = getEntityManager().createQuery("SELECT 1 FROM Recibo AS r WHERE r.id != :idRecibo AND r.nroRecibo = :nroRecibo ");
		query.setParameter("idRecibo", idRecibo);
		query.setParameter("nroRecibo", nroRecibo);
		return !query.getResultList().isEmpty();
	}

	@SuppressWarnings("unchecked")
	public List<Recibo> getAllNoAnuladosByIdCliente(Integer idCliente) {
		Query query = getEntityManager().createQuery("FROM Recibo AS r WHERE r.idEstadoRecibo != :idRechazado AND r.cliente.id = :idCliente ORDER BY r.nroRecibo");
		query.setParameter("idCliente", idCliente);
		query.setParameter("idRechazado", EEstadoRecibo.RECHAZADO.getId());		
		return query.getResultList();
	}

}
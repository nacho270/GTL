package ar.com.textillevel.dao.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.google.common.collect.Lists;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.fwcommon.util.NumUtil;
import ar.com.textillevel.dao.api.local.MovimientoCuentaDAOLocal;
import ar.com.textillevel.entidades.cuenta.Cuenta;
import ar.com.textillevel.entidades.cuenta.CuentaCliente;
import ar.com.textillevel.entidades.cuenta.CuentaProveedor;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoCuenta;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoDebe;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoDebeBanco;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoDebePersona;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoDebeProveedor;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoHaber;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoHaberBanco;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoHaberPersona;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoHaberProveedor;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoInternoCuenta;
import ar.com.textillevel.entidades.cuenta.movimientos.visitor.IFilaMovimientoVisitor;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.pagopersona.FacturaPersona;
import ar.com.textillevel.entidades.documentos.pagopersona.OrdenDePagoAPersona;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;

@Stateless
public class MovimientoCuentaDAO extends GenericDAO<MovimientoCuenta, Integer> implements MovimientoCuentaDAOLocal {

	public Integer getCantMovimientosByIdClienteYFecha(Integer idCuenta, Date fechaDesde, Date fechaHasta) {
		String query = " SELECT COUNT(*) From MovimientoCuenta mov " +
					   " WHERE mov.cuenta.id = :idCuenta "+
					   (fechaDesde!=null? " AND :fechaDesde <= mov.fechaHora " : "")+
					   (fechaHasta!=null? " AND :fechaHasta >= mov.fechaHora " : "");
		Query q = getEntityManager().createQuery(query);
		q.setParameter("idCuenta", idCuenta);
		if(fechaDesde!=null){
			q.setParameter("fechaDesde", fechaDesde);
		}
		
		if(fechaHasta != null){
			q.setParameter("fechaHasta", fechaHasta);
		}
		
		return NumUtil.toInteger(q.getSingleResult());
	}

	private static final int CANT_MAX_MOVS = 20;
	
	public List<MovimientoCuenta> getMovimientosByIdClienteYFecha(Integer idCuenta, Date fechaDesde, Date fechaHasta, 
																boolean ultimosMovimientos/*, boolean masAntiguoPrimero*/,
																ETipoDocumento filtroTipoDocumento) {
		
		List<MovimientoCuenta> movsRet = new ArrayList<MovimientoCuenta>();
		
		List<MovimientoCuenta> movs = getMovimientosPorCuentaYFecha(idCuenta, fechaDesde, fechaHasta);
		MovimientosLazyInitializer mli = new MovimientosLazyInitializer();
		if(ultimosMovimientos){
			if(movs.size()>CANT_MAX_MOVS){
//				int index = movs.size()-1;
//				for(;index>0;index--){
//					if((index-1)>0){
//						MovimientoCuenta m1 = movs.get(index-1);
//						if(m1 instanceof MovimientoHaber && ((MovimientoHaber)m1).getRecibo()!=null && (movs.size()-1-index)>=CANT_MAX_MOVS){
//							break;
//						}
//					}else{
//						index=0;
//						break;
//					}
//				}
//				for(int i = index; i<movs.size();i++){
//					MovimientoCuenta m = movs.get(i);
//					m.aceptarVisitor(mli);
//					movsRet.add(m);
//
//				}
				List<MovimientoCuenta> subList = movs.subList(movs.size()-CANT_MAX_MOVS, movs.size());
				for(MovimientoCuenta mov : subList){
					mov.aceptarVisitor(mli);
					movsRet.add(mov);
				}
			}else{
				for(int i = 0;i<movs.size();i++){
					MovimientoCuenta m = movs.get(i);
					m.aceptarVisitor(mli);
					movsRet.add(m);
				}
			}
		}else{
			for(MovimientoCuenta m : movs){
				m.aceptarVisitor(mli);
				movsRet.add(m);
			}
		}
		
//		ESTO ES LO VIEJO, LO COMENTO POR LAS DUDAS
//		for(MovimientoCuenta m : movs){
//			m.aceptarVisitor(mli);
//			if(ultimosMovimientos && (m instanceof MovimientoDebe)){
//				MovimientoDebe md = (MovimientoDebe) m;
//				if( (md.getFactura()!=null && md.getFactura().getMontoFaltantePorPagar().doubleValue()>0) ||
//					(md.getNotaDebito()!=null && md.getNotaDebito().getMontoFaltantePorPagar().doubleValue()>0)	){
//					movsRet.add(md);
//				}
//			}else if(ultimosMovimientos && (m instanceof MovimientoHaber)){
//				MovimientoHaber mh = (MovimientoHaber)m;
//				if(mh.getRecibo()!=null && mh.getRecibo().getMonto().doubleValue()>0){
//					movsRet.add(m);
//				}
//			}else{
//				movsRet.add(m);
//			}
//		}
		if(filtroTipoDocumento == null) {
			return movsRet;
		}
		MovimientosFilterVisitor mfv = new MovimientosFilterVisitor(filtroTipoDocumento);
		for (MovimientoCuenta mc : movsRet) {
			mc.aceptarVisitor(mfv);
		}
		return mfv.movimientosFiltrados;
	}

	@SuppressWarnings("unchecked")
	private List<MovimientoCuenta> getMovimientosPorCuentaYFecha(Integer idCuenta, Date fechaDesde, Date fechaHasta) {
		String query = " SELECT mov From MovimientoCuenta mov " +
					   " WHERE mov.cuenta.id = :idCuenta "+
					   (fechaDesde!=null? " AND :fechaDesde <= mov.fechaHora " : "")+
					   (fechaHasta!=null? " AND :fechaHasta >= mov.fechaHora " : "") +
					   " ORDER BY mov.fechaHora ASC " /*+ (masAntiguoPrimero?" ASC ": " DESC ")*/;
		Query q = getEntityManager().createQuery(query);
		q.setParameter("idCuenta", idCuenta);
		if(fechaDesde!=null){
			q.setParameter("fechaDesde", fechaDesde);
		}
		
		if(fechaHasta != null){
			q.setParameter("fechaHasta", fechaHasta);
		}
		List<MovimientoCuenta> movs = q.getResultList();
		if(movs == null || movs.isEmpty()){
			return Collections.emptyList();
		}
		return movs;
	}

	public void borrarMovimientoFactura(Integer id) {
		String hql = " DELETE FROM MovimientoCuenta m WHERE m.factura.id = :id ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", id);
		q.executeUpdate();
	}

	public int borrarMovimientoFacturaProveedor(Integer id) {
		String hql = " DELETE FROM MovimientoDebeProveedor m WHERE m.facturaProveedor.id = :id ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", id);
		return q.executeUpdate();
	}
	
	private static class MovimientosFilterVisitor implements IFilaMovimientoVisitor {

		private final ETipoDocumento tipoDocumento;
		private final List<MovimientoCuenta> movimientosFiltrados = Lists.newArrayList();
		
		public MovimientosFilterVisitor(ETipoDocumento tipoDocumento) {
			this.tipoDocumento = tipoDocumento;
		}

		@Override
		public void visit(MovimientoHaber movimiento) {
			if ( (tipoDocumento == ETipoDocumento.RECIBO && movimiento.getRecibo() != null) || 
				 (tipoDocumento == ETipoDocumento.NOTA_CREDITO && movimiento.getNotaCredito() != null)) {
				movimientosFiltrados.add(movimiento);
			}
		}

		@Override
		public void visit(MovimientoDebe movimiento) {
			if ( (tipoDocumento == ETipoDocumento.FACTURA && movimiento.getFactura() != null) ||
				 (tipoDocumento == ETipoDocumento.NOTA_DEBITO && movimiento.getNotaDebito() != null) ) {
				movimientosFiltrados.add(movimiento);
			}
		}

		@Override
		public void visit(MovimientoInternoCuenta movimiento) {
			
		}

		@Override
		public void visit(MovimientoHaberProveedor movimiento) {
			
		}

		@Override
		public void visit(MovimientoDebeProveedor movimiento) {
			
		}

		@Override
		public void visit(MovimientoHaberBanco movimiento) {
			
		}

		@Override
		public void visit(MovimientoDebeBanco movimiento) {
			
		}

		@Override
		public void visit(MovimientoDebePersona movimientoDebePersona) {
			
		}

		@Override
		public void visit(MovimientoHaberPersona movimientoHaberPersona) {
			
		}
	}

	private static class MovimientosLazyInitializer implements IFilaMovimientoVisitor {

		public void visit(MovimientoHaber movimiento) {
			if(movimiento.getRecibo()!=null){
				movimiento.getRecibo().getNroRecibo();
			}else{
				movimiento.getNotaCredito().getNroFactura();
			}
		}

		public void visit(MovimientoDebe movimiento) {
			if(movimiento.getFactura()!=null){
				movimiento.getFactura().getNroFactura();
				if(movimiento.getFactura().getRemitos()!=null){
					movimiento.getFactura().getRemitos().size();
					for(RemitoSalida r : movimiento.getFactura().getRemitos()){
						r.getCliente().getCelular();
						if (r.getOdts() != null) {
							r.getOdts().size();
						}
					}
					if(movimiento.getFactura().getCliente()!=null){
						movimiento.getFactura().getCliente().getCelular();
					}
				}else if(movimiento.getFactura().getCliente()!=null){
					movimiento.getFactura().getCliente().getCelular();
				}
			}else if (movimiento.getNotaDebito() != null){
				movimiento.getNotaDebito().getNroFactura();
			} else {
				movimiento.getRemitoSalida().getCantidadPiezas();
				movimiento.getRemitoSalida().getOdts().size();
			}
		}

		public void visit(MovimientoInternoCuenta movimiento) {
			
		}

		public void visit(MovimientoHaberProveedor movimiento) {
			if(movimiento.getOrdenDePago()!=null){
				movimiento.getOrdenDePago().getMonto();
			}else if(movimiento.getNotaCredito()!=null){
				movimiento.getNotaCredito().getMontoTotal();
			}
		}

		public void visit(MovimientoDebeProveedor movimiento) {
			if(movimiento.getFacturaProveedor() != null) {
				movimiento.getFacturaProveedor().getMontoSubtotal();
			}
			if(movimiento.getNotaDebitoProveedor()!=null) {
				movimiento.getNotaDebitoProveedor().getMontoFaltantePorPagar();
			}
		}

		public void visit(MovimientoHaberBanco movimiento) {
			movimiento.getOrdenDeDeposito().getFecha();
		}

		public void visit(MovimientoDebeBanco movimiento) {
			
		}

		public void visit(MovimientoDebePersona movimientoDebePersona) {
			if(movimientoDebePersona.getFacturaPersona() != null) {
				movimientoDebePersona.getFacturaPersona().getImpuestos().size();
			}
			if(movimientoDebePersona.getNotaDebitoPersona() != null) {
				movimientoDebePersona.getNotaDebitoPersona().getItemsCorreccion().size();
			}
		}

		public void visit(MovimientoHaberPersona movimientoHaberPersona) {
			movimientoHaberPersona.getOrdenDePago().getFecha();
		}
	}

	public List<MovimientoCuenta> getMovimientosProveedorByIdCuentaYFecha(Integer idCuenta, Date fechaDesde, Date fechaHasta
		/*, boolean masAntiguoPrimero*/, boolean ultimosMovimientos) {
		List<MovimientoCuenta> movsRet = new ArrayList<MovimientoCuenta>();
		List<MovimientoCuenta> movs = getMovimientosPorCuentaYFecha(idCuenta, fechaDesde, fechaHasta);
		MovimientosLazyInitializer mli = new MovimientosLazyInitializer();
		if(ultimosMovimientos){
			if(movs.size()>CANT_MAX_MOVS){
				List<MovimientoCuenta> subList = movs.subList(movs.size()-CANT_MAX_MOVS, movs.size());
				for(MovimientoCuenta mov : subList){
					mov.aceptarVisitor(mli);
					movsRet.add(mov);
				}
			}else{
				for(int i = 0;i<movs.size();i++){
					MovimientoCuenta m = movs.get(i);
					m.aceptarVisitor(mli);
					movsRet.add(m);
				}
			}
		}else{
			for(MovimientoCuenta m : movs){
				m.aceptarVisitor(mli);
				movsRet.add(m);
			}
		}
		return movsRet;
	}

	@SuppressWarnings("unchecked")
	public MovimientoHaber getMovimientoHaberByRecibo(Integer idRecibo) {
		String hql = " SELECT m FROM MovimientoHaber m WHERE m.recibo.id = :idRecibo ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idRecibo", idRecibo);
		List<MovimientoHaber> resultList = q.getResultList();
		if(resultList.size() == 0 || resultList.size() > 1) {
			throw new RuntimeException("Datos inconsistentes. El recibo con id " + idRecibo + " no tiene un movimiento haber registrado o tiene más de uno.");
		} else {
			return resultList.get(0);
		}
	}

	@SuppressWarnings("unchecked")
	public MovimientoHaber getMovimientoHaberByNC(Integer idNC) {
		String hql = " SELECT m FROM MovimientoHaber m WHERE m.notaCredito.id = :idNC ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idNC", idNC);
		List<MovimientoHaber> resultList = q.getResultList();
		if(resultList.size() == 0 || resultList.size() > 1) {
			throw new RuntimeException("Datos inconsistentes. La NC con id " + idNC + " no tiene un movimiento haber registrado o tiene más de uno.");
		} else {
			return resultList.get(0);
		}
	}

	@SuppressWarnings("unchecked")
	public MovimientoDebe getMovimientoDebeByND(Integer idND) {
		String hql = " SELECT m FROM MovimientoDebe m WHERE m.notaDebito.id = :idND ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idND", idND);
		List<MovimientoDebe> resultList = q.getResultList();
		if(resultList.size() == 0 || resultList.size() > 1) {
			throw new RuntimeException("Datos inconsistentes. La ND con id " + idND + " no tiene un movimiento debe registrado o tiene más de uno.");
		} else {
			return resultList.get(0);
		}
	}
	
	@SuppressWarnings("unchecked")
	public MovimientoHaberProveedor getMovimientoHPByNC(Integer idNC) {
		String hql = " SELECT m FROM MovimientoHaberProveedor m WHERE m.notaCredito.id = :idNC ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idNC", idNC);
		List<MovimientoHaberProveedor> resultList = q.getResultList();
		if(resultList.size() == 0 || resultList.size() > 1) {
			throw new RuntimeException("Datos inconsistentes. La nota de crédito con id " + idNC + " no tiene un movimiento haber registrado o tiene más de uno.");
		} else {
			return resultList.get(0);
		}
	}

	@SuppressWarnings("unchecked")
	public MovimientoDebeProveedor getMovimientoDPByND(Integer idND) {
		String hql = " SELECT m FROM MovimientoDebeProveedor m WHERE m.notaDebitoProveedor.id = :idND ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idND", idND);
		List<MovimientoDebeProveedor> resultList = q.getResultList();
		if(resultList.size() == 0 || resultList.size() > 1) {
			throw new RuntimeException("Datos inconsistentes. La nota de débito con id " + idND + " no tiene un movimiento haber registrado o tiene más de uno.");
		} else {
			return resultList.get(0);
		}
	}

	public List<MovimientoCuenta> getMovimientosBancoByIdCuentaYFecha(Integer idCuenta, Date fechaDesde, Date fechaHasta) {
		List<MovimientoCuenta> movsRet = new ArrayList<MovimientoCuenta>();
		List<MovimientoCuenta> movs = getMovimientosPorCuentaYFecha(idCuenta, fechaDesde, fechaHasta);
		MovimientosLazyInitializer mli = new MovimientosLazyInitializer();
		for(MovimientoCuenta m : movs){
			m.aceptarVisitor(mli);
			movsRet.add(m);
		}
		return movsRet;
	}
	
	public List<MovimientoCuenta> getMovimientosPersonaByIdCuentaYFecha(Integer idCuenta, Date fechaDesde, Date fechaHasta) {
		List<MovimientoCuenta> movsRet = new ArrayList<MovimientoCuenta>();
		List<MovimientoCuenta> movs = getMovimientosPorCuentaYFecha(idCuenta, fechaDesde, fechaHasta);
		MovimientosLazyInitializer mli = new MovimientosLazyInitializer();
		for(MovimientoCuenta m : movs){
			m.aceptarVisitor(mli);
			movsRet.add(m);
		}
		return movsRet;
	}

	public int borrarMovimientoOrdenDePago(Integer id) {
		String hql = " DELETE FROM MovimientoHaberProveedor m WHERE m.ordenDePago.id = :id ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", id);
		return q.executeUpdate();
	}
	public int borrarMovimientoNotaDebitoProveedor(Integer id) {
		String hql = " DELETE FROM MovimientoDebeProveedor m WHERE m.notaDebitoProveedor.id = :id ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", id);
		return q.executeUpdate();
	}

	public int borrarMovimientoNotaCreditoProveedor(Integer id) {
		String hql = " DELETE FROM MovimientoHaberProveedor m WHERE m.notaCredito.id = :id ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", id);
		return q.executeUpdate();
	}

	public void borrarMovimientoNotaCreditoCliente(Integer id) {
		String hql = " DELETE FROM MovimientoHaber m WHERE m.notaCredito.id = :id ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", id);
		q.executeUpdate();
	}

	public void borrarMovimientoNotaDebitoCliente(Integer id) {
		String hql = " DELETE FROM MovimientoDebe m WHERE m.notaDebito.id = :id ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", id);
		q.executeUpdate();
	}

	public void borrarMovimientoOrdenDePagoPersona(Integer id) {
		String hql = " DELETE FROM MovimientoHaberPersona m WHERE m.ordenDePago.id = :id ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", id);
		q.executeUpdate();
	}

	public void borrarMovimientoFacturaPersona(Integer id) {
		String hql = " DELETE FROM MovimientoDebePersona m WHERE m.facturaPersona.id = :id ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", id);
		q.executeUpdate();		
	}

	public MovimientoCuenta getMovimientoDebePersonaByFactura(FacturaPersona factura) {
		String hql = " SELECT m FROM MovimientoDebePersona m WHERE m.facturaPersona.id = :idFactura";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idFactura", factura.getId());
		return (MovimientoCuenta) q.getResultList().get(0);
	}

	public MovimientoCuenta getMovimientoHaberPersonaByOrdenDePago(OrdenDePagoAPersona orden) {
		String hql = " SELECT m FROM MovimientoHaberPersona m WHERE m.ordenDePago.id = :idOrden ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idOrden", orden.getId());
		return (MovimientoCuenta) q.getResultList().get(0);
	}

	public MovimientoCuenta getMovimientoDebeByFactura(Factura factura) {
		String hql = " SELECT m FROM MovimientoDebe m WHERE m.factura.id = :idFactura ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idFactura", factura.getId());
		return (MovimientoCuenta) q.getResultList().get(0);
	}

	@SuppressWarnings("unchecked")
	public List<MovimientoCuenta> getAllMovimientosByIdCuentaCliente(Integer idCta) {
		String hql = " SELECT m FROM MovimientoCuenta m WHERE m.cuenta.id = :idCuenta";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idCuenta", idCta);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public BigDecimal getSaldoCuentaHastaFecha(Cuenta cc, Date fechaTope) {
		String hql = " SELECT m FROM MovimientoCuenta m WHERE m.cuenta.id = :idCuenta AND m.fechaHora <= :fechaTope ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idCuenta", cc.getId());
		q.setParameter("fechaTope", fechaTope);
		List<MovimientoCuenta> lista = q.getResultList();
		BigDecimal saldo = new BigDecimal(0d);
		for(MovimientoCuenta m : lista){
			saldo = saldo.add(m.getMonto());
		}
		return saldo.negate();
	}
	
	@SuppressWarnings("unchecked")
	public BigDecimal getSaldoCuentaHastaFechaByIdMovimiento(Cuenta cc, Date fechaTope, Integer idMovimiento) {
		String hql = " SELECT m FROM MovimientoCuenta m WHERE m.cuenta.id = :idCuenta AND m.fechaHora <= :fechaTope ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idCuenta", cc.getId());
		q.setParameter("fechaTope", fechaTope);
		List<MovimientoCuenta> lista = q.getResultList();
		BigDecimal saldo = new BigDecimal(0d);
		for(MovimientoCuenta m : lista){
			if(!m.getId().equals(idMovimiento)){
				saldo = saldo.add(m.getMonto());
			}
		}
		return saldo.negate();
	}
	
	@SuppressWarnings("unchecked")
	public List<MovimientoCuenta> getMovimientosDeTransporte(Cuenta cc, Date fechaTope) {
		String hql = " SELECT m FROM MovimientoCuenta m WHERE m.cuenta.id = :idCuenta AND m.fechaHora <= :fechaTope ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idCuenta", cc.getId());
		q.setParameter("fechaTope", fechaTope);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public BigDecimal getSaldoCuentaHastaFecha2(CuentaCliente cc, Date fechaTope, boolean menorEstricto) {
		String hql = " SELECT m FROM MovimientoCuenta m WHERE m.cuenta.id = :idCuenta AND m.fechaHora " + (menorEstricto? " < " : " <= ") + " :fechaTope ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idCuenta", cc.getId());
		q.setParameter("fechaTope", fechaTope);
		List<MovimientoCuenta> lista = q.getResultList();
		BigDecimal saldo = new BigDecimal(0d);
		for(MovimientoCuenta m : lista){
			saldo = saldo.add(m.getMonto());
		} 
		return saldo.negate();
	}

	@SuppressWarnings("unchecked")
	public BigDecimal getSaldoCuentaHastaFechaSinNegate(CuentaProveedor cp, Date fechaTope) {
		String hql = " SELECT m FROM MovimientoCuenta m WHERE m.cuenta.id = :idCuenta AND m.fechaHora < :fechaTope ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idCuenta", cp.getId());
		q.setParameter("fechaTope", fechaTope);
		List<MovimientoCuenta> lista = q.getResultList();
		BigDecimal saldo = new BigDecimal(0d);
		for(MovimientoCuenta m : lista){
			saldo = saldo.add(m.getMonto());
		}
		return saldo;
	}

	@SuppressWarnings("unchecked")
	public MovimientoHaberProveedor getMovimientoHaberProveedorByNC(Integer idNC) {
		String hql = " SELECT m FROM MovimientoHaberProveedor m WHERE m.notaCredito.id = :idNC ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idNC", idNC);
		List<MovimientoHaberProveedor> resultList = q.getResultList();
		if(resultList.size() == 0 || resultList.size() > 1) {
			throw new RuntimeException("Datos inconsistentes. La NC con id " + idNC + " no tiene un movimiento haber registrado o tiene más de uno.");
		} else {
			return resultList.get(0);
		}
	}

	@SuppressWarnings("unchecked")
	public MovimientoCuenta getMovimientoDebeProveedorByFactura(FacturaProveedor factura) {
		String hql = " SELECT m FROM MovimientoDebeProveedor m WHERE m.facturaProveedor.id = :idFact ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idFact", factura.getId());
		List<MovimientoDebeProveedor> resultList = q.getResultList();
		if(resultList.size() == 0 || resultList.size() > 1) {
			throw new RuntimeException("Datos inconsistentes. La Factura con id " + factura.getId() + " no tiene un movimiento debe registrado o tiene más de uno.");
		} else {
			return resultList.get(0);
		}
	}
	
	@SuppressWarnings("unchecked")
	public MovimientoHaberProveedor getMovimientoHaberProveedorByODP(Integer idOdp) {
		String hql = " SELECT m FROM MovimientoHaberProveedor m WHERE m.ordenDePago.id = :idOdp ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idOdp", idOdp);
		List<MovimientoHaberProveedor> resultList = q.getResultList();
		if(resultList.size() == 0 || resultList.size() > 1) {
			throw new RuntimeException("Datos inconsistentes. La orden de pago con id " + idOdp + " no tiene un movimiento haber registrado o tiene más de uno.");
		} else {
			return resultList.get(0);
		}
	}

	@SuppressWarnings("unchecked")
	public List<MovimientoCuenta> getAllMovimientosByIdCliente(Integer idCuenta) {
		String hql = " SELECT m FROM MovimientoCuenta m WHERE m.cuenta.id = :idCuenta ORDER BY m.fechaHora";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idCuenta", idCuenta);
		return q.getResultList();
	}

	public int borrarMovimientoNotaDebitoPersona(Integer id) {
		String hql = " DELETE FROM MovimientoDebePersona m WHERE m.notaDebitoPersona.id = :id ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", id);
		return q.executeUpdate();
	}

}
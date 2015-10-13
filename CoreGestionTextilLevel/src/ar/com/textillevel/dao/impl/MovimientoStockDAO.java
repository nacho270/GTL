package ar.com.textillevel.dao.impl;

import java.sql.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.fwcommon.util.NumUtil;
import ar.com.textillevel.dao.api.local.MovimientoStockDAOLocal;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.stock.MovimientoStock;
import ar.com.textillevel.entidades.stock.MovimientoStockResta;
import ar.com.textillevel.entidades.stock.MovimientoStockSuma;
import ar.com.textillevel.entidades.stock.visitor.IMovimientoStockVisitor;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;

@Stateless
public class MovimientoStockDAO extends GenericDAO<MovimientoStock, Integer> implements MovimientoStockDAOLocal{

	@SuppressWarnings("unchecked")
	public List<MovimientoStock> getAllMovimientosByPrecioMateriaPrimaPorFechaYPaginado(PrecioMateriaPrima pm, Date fechaDesde, Date fechaHasta, Integer paginActual, Integer cantidadPorPagina) {
		String hql = " SELECT ms FROM MovimientoStock ms WHERE ms.precioMateriaPrima.id = :idPrecioMateriaPrima "+
					(fechaDesde !=null? " AND ms.fechaMovimiento >= :fechaDesde ":"  ")+
					(fechaHasta !=null? " AND ms.fechaMovimiento <= :fechaHasta ":"  ");
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idPrecioMateriaPrima", pm.getId());
		if(fechaDesde!=null){
			q.setParameter("fechaDesde", fechaDesde);
		}
		if(fechaHasta!=null){
			q.setParameter("fechaHasta", fechaHasta);
		}
		q.setMaxResults(cantidadPorPagina);
		q.setFirstResult(cantidadPorPagina * (paginActual-1));
		List<MovimientoStock> resultList = q.getResultList();
		MovimientoStockVisitorLazyInitializer mli = new MovimientoStockVisitorLazyInitializer();
		for(MovimientoStock ms : resultList){
			ms.aceptarVisitor(mli);
		}
		return resultList;
	}

	private class MovimientoStockVisitorLazyInitializer implements IMovimientoStockVisitor{
		public void visit(MovimientoStockSuma movimientoSuma) {
			if(movimientoSuma.getRemito() != null) {
				movimientoSuma.getRemito().getFechaEmision();
				movimientoSuma.getRemito().getPiezas().size();
			}
		}

		public void visit(MovimientoStockResta movimientoResta) {
			if(movimientoResta.getFactura()!=null){
				movimientoResta.getFactura().getFechaEmision();
				movimientoResta.getFactura().getCliente().getCelular();
			}else if(movimientoResta.getRemitoSalida()!=null){
				movimientoResta.getRemitoSalida().getCantidadPiezas();
				movimientoResta.getRemitoSalida().getCliente().getCelular();
			}else if(movimientoResta.getOdt() !=null){
				movimientoResta.getOdt().getCodigo();
				movimientoResta.getOdt().getRemito().getFechaEmision();
				movimientoResta.getOdt().getRemito().getCliente().getCelular();
			}
		}
	}
	
	public Integer getCantidadMovimientosByPrecioMateriaPrimaPorFecha(PrecioMateriaPrima pm, Date fechaDesde, Date fechaHasta) {
		String hql = " SELECT COUNT(*) FROM MovimientoStock ms WHERE ms.precioMateriaPrima.id = :idPrecioMateriaPrima " + 
					(fechaDesde != null ? " AND ms.fechaMovimiento >= :fechaDesde " : "  ")
					+ (fechaHasta != null ? " AND ms.fechaMovimiento <= :fechaHasta " : "  ");
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idPrecioMateriaPrima", pm.getId());
		if (fechaDesde != null) {
			q.setParameter("fechaDesde", fechaDesde);
		}
		if (fechaHasta != null) {
			q.setParameter("fechaHasta", fechaHasta);
		}
		return NumUtil.toInteger(q.getSingleResult());
	}

	public void borrarMovimientosStockByFactura(Factura factura) {
		String hql = " DELETE FROM MovimientoStock m WHERE m.factura.id = :id ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", factura.getId());
		q.executeUpdate();		
	}

	public void borrarMovimientosStockByRemitoSalida(RemitoSalida remitoSalida) {
		String hql = " DELETE FROM MovimientoStock m WHERE m.remitoSalida.id = :id ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", remitoSalida.getId());
		q.executeUpdate();		
	}

	@SuppressWarnings("unchecked")
	public List<MovimientoStockSuma> getMovimientosSumaByRemito(Integer idRemitoEntradaProveedor) {
		String hql = "SELECT ms " +
					  "FROM MovimientoStockSuma ms " +
					  "WHERE ms.remito.id = :idRemitoEntradaProveedor";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idRemitoEntradaProveedor", idRemitoEntradaProveedor);
		List<MovimientoStockSuma> resultList = q.getResultList();
		return resultList;
	}

	@SuppressWarnings("unchecked")
	public List<MovimientoStockResta> getMovimientosRestaByRemitoSalida(Integer idRemitoSalida) {
		String hql = "SELECT mr " +
					 "FROM MovimientoStockResta mr " + 
					 "WHERE mr.remitoSalida.id = :idRemitoSalida";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idRemitoSalida", idRemitoSalida);
		List<MovimientoStockResta> resultList = q.getResultList();
		return resultList;
	}

	@SuppressWarnings("unchecked")
	public List<MovimientoStockResta> getMovimientosRestaByOdt(Integer idODT) {
		String hql = " SELECT mr FROM MovimientoStockResta mr " + 
					 " WHERE mr.odt.id = :idODT ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idODT", idODT);
		List<MovimientoStockResta> resultList = q.getResultList();
		return resultList;
	}


}

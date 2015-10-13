package ar.com.textillevel.facade.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.dao.api.local.MovimientoStockDAOLocal;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFactura;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaPrecioMateriaPrima;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RelacionContenedorPrecioMatPrima;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;
import ar.com.textillevel.entidades.stock.MovimientoStock;
import ar.com.textillevel.entidades.stock.MovimientoStockResta;
import ar.com.textillevel.entidades.stock.MovimientoStockSuma;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.facade.api.local.MovimientoStockFacadeLocal;
import ar.com.textillevel.facade.api.local.PrecioMateriaPrimaFacadeLocal;
import ar.com.textillevel.facade.api.remote.MovimientoStockFacadeRemote;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;

@Stateless
public class MovimientoStockFacade implements MovimientoStockFacadeLocal, MovimientoStockFacadeRemote{

	@EJB
	private MovimientoStockDAOLocal movimientoStockDao;
	
	@EJB
	private PrecioMateriaPrimaFacadeLocal precioMateriaPrimaFacade;
	
	public MovimientoStock crearMovimientoSuma(PrecioMateriaPrima pm, BigDecimal cantidad, RemitoEntradaProveedor remitoNuevo, BigDecimal stockAnterior) {
		if(stockAnterior == null) {
			stockAnterior = new BigDecimal(0);
		}
		
		MovimientoStockSuma mss = new MovimientoStockSuma();
		mss.setCantidad(cantidad);
		mss.setRemito(remitoNuevo);
		mss.setPrecioMateriaPrima(pm);
		mss.setFechaMovimiento(remitoNuevo.getFechaEmision());
		return movimientoStockDao.save(mss);
	}

	public MovimientoStock crearMovimientoSuma(RelacionContenedorPrecioMatPrima rcpmp, BigDecimal cantidad, RemitoEntradaProveedor remitoNuevo, BigDecimal stockAnterior) {
		if(stockAnterior == null) {
			stockAnterior = new BigDecimal(0);
		}
		
		MovimientoStockSuma mss = new MovimientoStockSuma();
		mss.setCantidad(cantidad);
		mss.setRemito(remitoNuevo);
		mss.setRelContPrecioMatPrima(rcpmp);
		mss.setFechaMovimiento(remitoNuevo.getFechaEmision());
		return movimientoStockDao.save(mss);
	}

	
	public List<MovimientoStock> getAllMovimientosByPrecioMateriaPrimaPorFechaYPaginado(PrecioMateriaPrima pm, Date fechaDesde, Date fechaHasta, Integer paginActual, Integer cantidadPorPagina) {
		return movimientoStockDao.getAllMovimientosByPrecioMateriaPrimaPorFechaYPaginado(pm,fechaDesde,fechaHasta,paginActual,cantidadPorPagina);
	}

	public Integer getCantidadMovimientosByPrecioMateriaPrimaPorFecha(PrecioMateriaPrima pm, Date fechaDesde, Date fechaHasta) {
		return movimientoStockDao.getCantidadMovimientosByPrecioMateriaPrimaPorFecha(pm,fechaDesde,fechaHasta);
	}

	public MovimientoStock crearMovimientoResta(PrecioMateriaPrima pm, BigDecimal cantidad, Factura factura, BigDecimal stockAnterior) {
		if(stockAnterior == null) {
			stockAnterior = new BigDecimal(0);
		}
		
		MovimientoStockResta msr = new MovimientoStockResta();
		msr.setFactura(factura);
		msr.setFechaMovimiento(new java.sql.Date(factura.getFechaEmision().getTime()));
		msr.setCantidad(cantidad);
		msr.setPrecioMateriaPrima(pm);
		return movimientoStockDao.save(msr);
	}

	public MovimientoStock crearMovimientoResta(RelacionContenedorPrecioMatPrima rcpmp, BigDecimal cantidad, RemitoSalida rsp, BigDecimal stockAnterior) {
		if(stockAnterior == null) {
			stockAnterior = new BigDecimal(0);
		}
		MovimientoStockResta msr = new MovimientoStockResta();
		msr.setRemitoSalida(rsp);
		msr.setFechaMovimiento(new java.sql.Date(rsp.getFechaEmision().getTime()));
		msr.setCantidad(cantidad);
		msr.setRelContPrecioMatPrima(rcpmp);
		return movimientoStockDao.save(msr);
	}

	public MovimientoStock crearMovimientoResta(PrecioMateriaPrima precioMateriaPrima, BigDecimal cantidad, RemitoSalida remitoSalidaProveedor, BigDecimal stockAnterior) {
		if(stockAnterior == null) {
			stockAnterior = new BigDecimal(0);
		}
		MovimientoStockResta msr = new MovimientoStockResta();
		msr.setRemitoSalida(remitoSalidaProveedor);
		msr.setFechaMovimiento(new java.sql.Date(remitoSalidaProveedor.getFechaEmision().getTime()));
		msr.setCantidad(cantidad);
		msr.setPrecioMateriaPrima(precioMateriaPrima);
		return movimientoStockDao.save(msr);
	}
	
	public MovimientoStock crearMovimientoResta(OrdenDeTrabajo odt, PrecioMateriaPrima precioMateriaPrima, Float cantidadADescontar) {
		MovimientoStockResta msr = new MovimientoStockResta();
		msr.setFechaMovimiento(DateUtil.getHoy());
		msr.setCantidad(new BigDecimal(cantidadADescontar.doubleValue()));
		msr.setPrecioMateriaPrima(precioMateriaPrima);
		msr.setOdt(odt);
		return movimientoStockDao.save(msr);
	}
	
	/** UTLIZADO CUANDO SE DA DE BAJA STOCK MANUALMENTE */
	public MovimientoStock crearMovimientoResta(PrecioMateriaPrima precioMateriaPrima, BigDecimal cantidad, BigDecimal stockAnterior) {
		if(stockAnterior == null) {
			stockAnterior = new BigDecimal(0);
		}
		MovimientoStockResta msr = new MovimientoStockResta();
		msr.setFechaMovimiento(DateUtil.getHoy());
		msr.setCantidad(cantidad);
		msr.setPrecioMateriaPrima(precioMateriaPrima);
		return movimientoStockDao.save(msr);
	}

	public void borrarMovientoStock(Factura factura) {
		movimientoStockDao.borrarMovimientosStockByFactura(factura);
		for(ItemFactura it : factura.getItems()){
			if(it instanceof ItemFacturaPrecioMateriaPrima){
				precioMateriaPrimaFacade.actualizarStockPrecioMateriaPrima(it.getCantidad(), ((ItemFacturaPrecioMateriaPrima)it).getPrecioMateriaPrima().getId());
			}
		}
	}

	public void borrarMovientosStockODT(Integer idODT) {
		List<MovimientoStockResta> movimientosRestaList = getMovimientosRestaByOdt(idODT);
		for(MovimientoStockResta msr : movimientosRestaList) {
			movimientoStockDao.removeById(msr.getId());
			precioMateriaPrimaFacade.actualizarStockPrecioMateriaPrima(msr.getCantidad(), msr.getPrecioMateriaPrima().getId());
		}
	}
	
	public void borrarMovientosStock(Integer idRemitoSalida) {
		List<MovimientoStockResta> movimientosRestaList = getMovimientosRestaByRemitoSalida(idRemitoSalida);
		for(MovimientoStockResta msr : movimientosRestaList) {
			movimientoStockDao.removeById(msr.getId());
			precioMateriaPrimaFacade.actualizarStockPrecioMateriaPrima(msr.getCantidad(), msr.getPrecioMateriaPrima().getId());
		}
	}

	public List<MovimientoStockSuma> getMovimientosSumaByRemito(Integer idRemitoEntradaProveedor) {
		return movimientoStockDao.getMovimientosSumaByRemito(idRemitoEntradaProveedor);
	}

	public void borrarMovimientoById(Integer idMovimiento) {
		movimientoStockDao.removeById(idMovimiento);
	}

	public List<MovimientoStockResta> getMovimientosRestaByRemitoSalida(Integer idRemitoSalida) {
		return movimientoStockDao.getMovimientosRestaByRemitoSalida(idRemitoSalida);
	}
	
	public List<MovimientoStockResta> getMovimientosRestaByOdt(Integer idODT) {
		return movimientoStockDao.getMovimientosRestaByOdt(idODT);
	}
}

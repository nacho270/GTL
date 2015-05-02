package ar.com.textillevel.facade.api.remote;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.stock.MovimientoStock;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;

@Remote
public interface MovimientoStockFacadeRemote {
	
	public List<MovimientoStock> getAllMovimientosByPrecioMateriaPrimaPorFechaYPaginado(PrecioMateriaPrima pm,
									Date fechaDesde, Date fechaHasta, Integer paginActual, Integer cantidadPorPagina);
	
	public Integer getCantidadMovimientosByPrecioMateriaPrimaPorFecha(PrecioMateriaPrima pm, Date fechaDesde, Date fechaHasta);
	public MovimientoStock crearMovimientoResta(PrecioMateriaPrima precioMateriaPrima, BigDecimal cantidad, BigDecimal stockAnterior);
}

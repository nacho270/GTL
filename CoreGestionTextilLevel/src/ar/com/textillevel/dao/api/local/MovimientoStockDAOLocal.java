package ar.com.textillevel.dao.api.local;

import java.sql.Date;
import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.stock.MovimientoStock;
import ar.com.textillevel.entidades.stock.MovimientoStockResta;
import ar.com.textillevel.entidades.stock.MovimientoStockSuma;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;

@Local
public interface MovimientoStockDAOLocal extends DAOLocal<MovimientoStock, Integer>{

	public List<MovimientoStock> getAllMovimientosByPrecioMateriaPrimaPorFechaYPaginado(PrecioMateriaPrima pm, Date fechaDesde, Date fechaHasta, Integer paginActual, Integer cantidadPorPagina);

	public Integer getCantidadMovimientosByPrecioMateriaPrimaPorFecha(PrecioMateriaPrima pm, Date fechaDesde, Date fechaHasta);

	public void borrarMovimientosStockByFactura(Factura factura);

	public void borrarMovimientosStockByRemitoSalida(RemitoSalida remitoSalida);

	public List<MovimientoStockSuma> getMovimientosSumaByRemito(Integer idRemitoEntradaProveedor);

	public List<MovimientoStockResta> getMovimientosRestaByRemitoSalida(Integer idRemitoSalida);

	public List<MovimientoStockResta> getMovimientosRestaByOdt(Integer idODT);


}

package ar.com.textillevel.facade.api.local;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Local;

import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RelacionContenedorPrecioMatPrima;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;
import ar.com.textillevel.entidades.stock.MovimientoStock;
import ar.com.textillevel.entidades.stock.MovimientoStockResta;
import ar.com.textillevel.entidades.stock.MovimientoStockSuma;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;

@Local
public interface MovimientoStockFacadeLocal {
	public MovimientoStock crearMovimientoSuma(PrecioMateriaPrima pm, BigDecimal cantidad, RemitoEntradaProveedor remitoNuevo, BigDecimal stockAnterior);
	public MovimientoStock crearMovimientoSuma(RelacionContenedorPrecioMatPrima rcpmp, BigDecimal cantidad, RemitoEntradaProveedor remitoNuevo, BigDecimal stockAnterior);
	public MovimientoStock crearMovimientoResta(PrecioMateriaPrima pm, BigDecimal cantidad, Factura factura, BigDecimal stockAnterior);
	public MovimientoStock crearMovimientoResta(RelacionContenedorPrecioMatPrima rcpmp, BigDecimal cantidad, RemitoSalida rsp, BigDecimal stockAnterior);	
	public MovimientoStock crearMovimientoResta(OrdenDeTrabajo odt, PrecioMateriaPrima precioMateriaPrima, Float cantidadADescontar);
	public void borrarMovientoStock(Factura f);
	public void borrarMovientosStock(Integer idRemitoSalida);
	public MovimientoStock crearMovimientoResta(PrecioMateriaPrima precioMateriaPrima, BigDecimal cantSalida, RemitoSalida remitoSalidaProveedor, BigDecimal stockActual);
	public List<MovimientoStockSuma> getMovimientosSumaByRemito(Integer idRemitoEntradaProveedor);
	public void borrarMovimientoById(Integer idMovimiento);
	public List<MovimientoStockResta> getMovimientosRestaByRemitoSalida(Integer idRemitoSalida);
	public void borrarMovientosStockODT(Integer idODT);
}

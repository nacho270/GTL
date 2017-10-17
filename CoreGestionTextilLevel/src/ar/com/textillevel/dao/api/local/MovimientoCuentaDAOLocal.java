package ar.com.textillevel.dao.api.local;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.cuenta.Cuenta;
import ar.com.textillevel.entidades.cuenta.CuentaCliente;
import ar.com.textillevel.entidades.cuenta.CuentaProveedor;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoCuenta;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoDebe;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoDebeProveedor;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoHaber;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoHaberProveedor;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.pagopersona.FacturaPersona;
import ar.com.textillevel.entidades.documentos.pagopersona.OrdenDePagoAPersona;

@Local
public interface MovimientoCuentaDAOLocal extends DAOLocal<MovimientoCuenta, Integer> {

	public List<MovimientoCuenta> getMovimientosByIdClienteYFecha(Integer idCuenta, Date fechaDesde, Date fechaHasta, 
																	boolean ultimosMovimientos/*, boolean masAntiguoPrimero*/, ETipoDocumento filtroTipoDocumento);
	public void borrarMovimientoFactura(Integer id);
	public MovimientoHaber getMovimientoHaberByRecibo(Integer idRecibo);
	public MovimientoHaber getMovimientoHaberByNC(Integer idNC);	
	public int borrarMovimientoFacturaProveedor(Integer id);
	public int borrarMovimientoNotaDebitoProveedor(Integer id);
	public BigDecimal getSaldoCuentaHastaFecha(Cuenta cc, Date fechaTope);
	public List<MovimientoCuenta> getMovimientosProveedorByIdCuentaYFecha(Integer idCuenta, Date fechaDesde, Date fechaHasta
																				/*, boolean masAntiguoPrimero*/, boolean ultimosMovimientos);

	public MovimientoHaberProveedor getMovimientoHPByNC(Integer idNC);
	public MovimientoDebeProveedor getMovimientoDPByND(Integer idND);
	public int borrarMovimientoNotaCreditoProveedor(Integer id);
	public List<MovimientoCuenta> getMovimientosBancoByIdCuentaYFecha(Integer id, Date fechaDesde, Date fechaHasta);
	public int borrarMovimientoOrdenDePago(Integer id);
	public void borrarMovimientoNotaCreditoCliente(Integer id);
	public void borrarMovimientoNotaDebitoCliente(Integer id);
	public List<MovimientoCuenta> getMovimientosPersonaByIdCuentaYFecha(Integer idCuenta, Date fechaDesde, Date fechaHasta);
	public void borrarMovimientoOrdenDePagoPersona(Integer id);
	public void borrarMovimientoFacturaPersona(Integer id);
	public MovimientoCuenta getMovimientoDebePersonaByFactura(FacturaPersona factura);
	public MovimientoCuenta getMovimientoHaberPersonaByOrdenDePago(OrdenDePagoAPersona orden);
	public MovimientoCuenta getMovimientoDebeByFactura(Factura factura);
	public MovimientoDebe getMovimientoDebeByND(Integer idND);
	public List<MovimientoCuenta> getAllMovimientosByIdCuentaCliente(Integer idCta);
	public BigDecimal getSaldoCuentaHastaFecha2(CuentaCliente cc, Date fechaTope, boolean menorEstricto);
	public BigDecimal getSaldoCuentaHastaFechaSinNegate(CuentaProveedor cp, Date fechaTope);
	public BigDecimal getSaldoCuentaHastaFechaByIdMovimiento(Cuenta cc, Date fechaTope, Integer idMovimiento);
	public List<MovimientoCuenta> getMovimientosDeTransporte(Cuenta cc, Date fechaTope);
	public MovimientoHaberProveedor getMovimientoHaberProveedorByNC(Integer idNC);
	public MovimientoCuenta getMovimientoDebeProveedorByFactura(FacturaProveedor factura);
	public MovimientoHaberProveedor getMovimientoHaberProveedorByODP(Integer idOdp);
	public List<MovimientoCuenta> getAllMovimientosByIdCliente(Integer idCuenta);
	public int borrarMovimientoNotaDebitoPersona(Integer id);
	
}

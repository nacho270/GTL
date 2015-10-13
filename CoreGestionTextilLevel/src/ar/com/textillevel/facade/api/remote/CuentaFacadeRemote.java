package ar.com.textillevel.facade.api.remote;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.entidades.cuenta.CuentaBanco;
import ar.com.textillevel.entidades.cuenta.CuentaCliente;
import ar.com.textillevel.entidades.cuenta.CuentaPersona;
import ar.com.textillevel.entidades.cuenta.CuentaProveedor;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoCuenta;
import ar.com.textillevel.entidades.documentos.factura.to.InfoCuentaTO;

@Remote
public interface CuentaFacadeRemote {

	public CuentaCliente getCuentaClienteByNroCliente(Integer nroCliente);
	public CuentaCliente getCuentaClienteByIdCliente(Integer idCliente);
	public List<MovimientoCuenta> getMovimientosByIdClienteYFecha(Integer nroCliente, Date fechaDesde, Date fechaHasta, boolean incluirFacturasPagadas/*, boolean masAntiguoPrimero*/);
	public Map<Integer, List<Integer>> getMapaRecibosYPagosRecibos();

	public InfoCuentaTO getInfoReciboYPagosRecibidos(Integer nroCliente);
	public InfoCuentaTO getInfoOrdenDePagoYPagosRecibidos(Integer idProveedor);

	public BigDecimal getTransporteCuenta(Integer idCliente, Date fechaTope, Integer idMovimiento) throws ValidacionException;
	public BigDecimal getTransporteCuenta(Integer idCliente, Date fechaTope, boolean menorEstricto) throws ValidacionException;
	
	public BigDecimal getTransporteCuentaProveedor(Integer idProveedor, Date fechaTope);
	public List<MovimientoCuenta> getMovimientosByIdProveedorYFecha(Integer id, Date fechaDesde, Date fechaHasta, boolean ultimosMovimientos);
	public CuentaProveedor getCuentaProveedorByIdProveedor(Integer idProveedor);
	
	public CuentaBanco getCuentaBancoByIdBanco(Integer idBanco);
	public BigDecimal getTransporteCuentaBanco(Integer idBanco, Date fechaTope);
	public List<MovimientoCuenta> getMovimientosByIdBancoYFecha(Integer id, Date fechaDesde, Date fechaHasta);

	public CuentaPersona getCuentaPersonaByIdPersona(Integer idPersona);
	public BigDecimal getTransporteCuentaPersona(Integer idPersona, Date fechaTope);
	public List<MovimientoCuenta> getMovimientosByIdPersonaYFecha(Integer idPersona, Date fechaDesde, Date fechaHasta);
	
	public MovimientoCuenta actualizarMovimiento(MovimientoCuenta mov);

	/* NUEVAS FORMAS DE CALCULAR EL TRANSPORDE DE CUENTA	 */
	public List<MovimientoCuenta> getMovimientosTransporteCuentaCliente(Integer idCliente, Date fechaTope) throws ValidacionException;
	public List<MovimientoCuenta> getMovimientosTransporteCuentaProveedor(Integer idProveedor, Date fechaTope) throws ValidacionException;
	public List<MovimientoCuenta> getMovimientosTransporteCuentaPersona(Integer idPersona, Date fechaTope) throws ValidacionException;
	public List<MovimientoCuenta> getMovimientosTransporteCuentaBanco(Integer idBanco, Date fechaTope) throws ValidacionException;
}

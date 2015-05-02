package ar.com.textillevel.facade.api.local;

import java.math.BigDecimal;

import javax.ejb.Local;

import ar.com.textillevel.entidades.cuenta.CuentaCliente;
import ar.com.textillevel.entidades.cuenta.to.CuentaTO;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.NotaCredito;
import ar.com.textillevel.entidades.documentos.factura.NotaDebito;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaCreditoProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaDebitoProveedor;
import ar.com.textillevel.entidades.documentos.ordendedeposito.OrdenDeDeposito;
import ar.com.textillevel.entidades.documentos.ordendepago.OrdenDePago;
import ar.com.textillevel.entidades.documentos.pagopersona.FacturaPersona;
import ar.com.textillevel.entidades.documentos.pagopersona.OrdenDePagoAPersona;
import ar.com.textillevel.entidades.documentos.recibo.Recibo;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.gente.Proveedor;

@Local
public interface CuentaFacadeLocal {
	public CuentaCliente getCuentaClienteByIdCliente(Integer idCliente);
	public void crearMovimientoDebe(Factura f);
	public NotaDebito crearMovimientoDebe(NotaDebito nd);
	public void crearMovimientoHaber(NotaCredito nc);
	public void crearMovimientoHaber(Recibo r);
	public void borrarMovimientoRecibo(Recibo recibo);
	public void borrarMovimientoNotaCreditoCliente(NotaCredito nc);
	public void borrarMovimientoNotaDebitoCliente(NotaDebito nd);
	
	/**
	 * Borra el movimiento y actualiza el saldo de la cuenta
	 * @param factura
	 */
	public void borrarMovimientoFactura(Factura factura);

	public void crearMovimientoHaberProveedor(OrdenDePago orden);
	public void crearMovimientoHaberProveedor(NotaCreditoProveedor notaCredito, String obsMovimiento);
	public void crearMovimientoDebeProveedor(FacturaProveedor f);
	public void crearMovimientoDebeProveedor(NotaDebitoProveedor notaDebito);
	
	/**
	 * Borra el movimiento y actualiza el saldo de la cuenta
	 * @param orden
	 */
	public void borrarMovimientoOrdenDePago(OrdenDePago orden);
	
	public void crearMovimientoHaberBanco(OrdenDeDeposito orden);
	public void asignarFechaMovimientoHaberSegunNCP(NotaCreditoProveedor ncp);
	public void asignarFechaMovimientoDebeSegunNDP(NotaDebitoProveedor ndp);
	public void borrarMovimientoFacturaProveedor(FacturaProveedor factura);
	public void borrarMovimientoNotaDebitoProveedor(NotaDebitoProveedor ndp);
	public void borrarMovimientoNotaCreditoProveedor(NotaCreditoProveedor ncp);
	
	public void crearMovimientoHaberPersona(OrdenDePagoAPersona orden);
	public void crearMovimientoDebePersona(FacturaPersona factura);
	
	public void borrarMovimientoOrdenDePagoPersona(OrdenDePagoAPersona orden);
	public void borrarMovimientoFacturaPersona(FacturaPersona factura);
	public void actualizarMovimientoFacturaPersona(FacturaPersona factura, BigDecimal montoAnterior);
	public void actualizarMovimientoOrdenDePagoPersona(OrdenDePagoAPersona orden, BigDecimal montoAnterior);
	public void actualizarMovimientoFacturaCliente(Factura factura, BigDecimal montoTotal);

	public CuentaTO getCuentaTO(Cliente cliente, int cantMovimientos);
	public CuentaTO getCuentaTO(Proveedor proveedor, int cantidadMovimientos);

}

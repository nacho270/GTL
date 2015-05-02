package ar.com.textillevel.entidades.cuenta.movimientos.visitor;

import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoDebe;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoDebeBanco;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoDebePersona;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoDebeProveedor;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoHaber;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoHaberBanco;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoHaberPersona;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoHaberProveedor;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoInternoCuenta;

public interface IFilaMovimientoVisitor {
	
	public void visit(MovimientoHaber movimiento);
	public void visit(MovimientoDebe movimiento);
	public void visit(MovimientoInternoCuenta movimiento);
	
	/************PROVEEDORES**********************************/
	
	public void visit(MovimientoHaberProveedor movimiento);
	public void visit(MovimientoDebeProveedor movimiento);
	
	/************BANCO**********************************/
	public void visit(MovimientoHaberBanco movimiento);
	public void visit(MovimientoDebeBanco movimiento);
	
	/************PERSONA**********************************/
	public void visit(MovimientoDebePersona movimientoDebePersona);
	public void visit(MovimientoHaberPersona movimientoHaberPersona);
}

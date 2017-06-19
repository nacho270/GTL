package ar.com.textillevel.gui.acciones.visitor.cuenta;

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
import ar.com.textillevel.gui.acciones.JFrameVerMovimientos;

public class AccionEliminarFacturaCuentaVisitor implements IFilaMovimientoVisitor {

private final JFrameVerMovimientos frameMovimientos;
	
	public AccionEliminarFacturaCuentaVisitor(JFrameVerMovimientos frameMovimientos) {
		this.frameMovimientos = frameMovimientos;
	}
	
	public void visit(MovimientoHaber movimiento) {
		if(movimiento.getRecibo()!=null){
			frameMovimientos.eliminarRecibo(movimiento.getRecibo());
		}else{
			frameMovimientos.eliminarCorreccion(movimiento.getNotaCredito());
		}
	}

	public void visit(MovimientoDebe movimiento) {
		if(movimiento.getFactura()!=null){
			frameMovimientos.eliminarFactura(movimiento.getFactura());
		}else if(movimiento.getNotaDebito() != null){
			frameMovimientos.eliminarCorreccion(movimiento.getNotaDebito());
		}
	}

	public void visit(MovimientoInternoCuenta movimiento) {
		
	}

	public void visit(MovimientoHaberProveedor movimiento) {
		
	}

	public void visit(MovimientoDebeProveedor movimiento) {
	
	}

	public void visit(MovimientoHaberBanco movimiento) {
		
	}

	public void visit(MovimientoDebeBanco movimiento) {
		
	}

	public void visit(MovimientoDebePersona movimientoDebePersona) {
		
	}

	public void visit(MovimientoHaberPersona movimientoHaberPersona) {
		
	}
}

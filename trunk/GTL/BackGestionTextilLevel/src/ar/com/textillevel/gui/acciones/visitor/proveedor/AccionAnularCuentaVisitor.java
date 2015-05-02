package ar.com.textillevel.gui.acciones.visitor.proveedor;

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
import ar.com.textillevel.gui.acciones.JFrameVerMovimientosProveedor;

public class AccionAnularCuentaVisitor implements IFilaMovimientoVisitor {
	
	private JFrameVerMovimientosProveedor frameMovimientos;
	
	public AccionAnularCuentaVisitor(JFrameVerMovimientosProveedor frameMovimientos) {
		this.frameMovimientos = frameMovimientos;
	}

	public void visit(MovimientoHaber movimiento) {
		if(movimiento.getRecibo()!=null){
			getFrameMovimientos().anularRecibo(movimiento.getRecibo());
		}
	}

	public void visit(MovimientoDebe movimiento) {
		
	}

	public void visit(MovimientoInternoCuenta movimiento) {
		
	}

	public void visit(MovimientoHaberProveedor movimiento) {
		
	}
	
	public JFrameVerMovimientosProveedor getFrameMovimientos() {
		return frameMovimientos;
	}

	
	public void setFrameMovimientos(JFrameVerMovimientosProveedor frameMovimientos) {
		this.frameMovimientos = frameMovimientos;
	}

	public void visit(MovimientoDebeProveedor movimiento) {
		if(movimiento.getFacturaProveedor()!=null){
			getFrameMovimientos().anularFactura(movimiento.getFacturaProveedor());
		}
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

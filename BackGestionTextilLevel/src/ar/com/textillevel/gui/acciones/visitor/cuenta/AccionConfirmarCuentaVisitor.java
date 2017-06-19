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

public class AccionConfirmarCuentaVisitor implements IFilaMovimientoVisitor{

	private JFrameVerMovimientos frameMovimientos;
	
	public AccionConfirmarCuentaVisitor(JFrameVerMovimientos frameMovimientos) {
		this.frameMovimientos = frameMovimientos;
	}

	public void visit(MovimientoHaber movimiento) {
		if(movimiento.getRecibo()!=null){
			getFrameMovimientos().confirmarRecibo(movimiento.getRecibo());
		}else{
			getFrameMovimientos().confirmarCredito(movimiento.getNotaCredito());
		}
	}

	public void visit(MovimientoDebe movimiento) {
		if(movimiento.getFactura()!=null){
			getFrameMovimientos().confirmarFactura(movimiento.getFactura());
		}else if (movimiento.getNotaDebito() != null){
			getFrameMovimientos().confirmarNotaDebito(movimiento.getNotaDebito());
		}
	}

	public void visit(MovimientoInternoCuenta movimiento) {
		
	}

	public JFrameVerMovimientos getFrameMovimientos() {
		return frameMovimientos;
	}

	
	public void setFrameMovimientos(JFrameVerMovimientos frameMovimientos) {
		this.frameMovimientos = frameMovimientos;
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

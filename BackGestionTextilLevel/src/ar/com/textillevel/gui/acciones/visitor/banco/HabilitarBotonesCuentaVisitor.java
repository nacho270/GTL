package ar.com.textillevel.gui.acciones.visitor.banco;

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
import ar.com.textillevel.gui.acciones.JFrameVerMovimientosBanco;

public class HabilitarBotonesCuentaVisitor implements IFilaMovimientoVisitor {

	private JFrameVerMovimientosBanco frameMovimientos;

	public HabilitarBotonesCuentaVisitor(JFrameVerMovimientosBanco jFrameVerMovimientos) {
		this.frameMovimientos = jFrameVerMovimientos;
	}

	public void visit(MovimientoHaber movimiento) {

	}

	public void visit(MovimientoDebe movimiento) {

	}

	public void visit(MovimientoInternoCuenta movimiento) {
		
	}
	
	public JFrameVerMovimientosBanco getFrameMovimientos() {
		return frameMovimientos;
	}

	public void setFrameMovimientos(JFrameVerMovimientosBanco frameMovimientos) {
		this.frameMovimientos = frameMovimientos;
	}

	public void visit(MovimientoDebeProveedor movimiento) {
		
	}
	
	public void visit(MovimientoHaberProveedor movimiento) {
		
	}
	
	public void visit(MovimientoHaberBanco movimiento) {
		frameMovimientos.getBtnAgregarObservaciones().setEnabled(true);
	}

	public void visit(MovimientoDebeBanco movimiento) {
		frameMovimientos.getBtnAgregarObservaciones().setEnabled(true);
	}
	
	public void visit(MovimientoDebePersona movimientoDebePersona) {
		
	}

	public void visit(MovimientoHaberPersona movimientoHaberPersona) {
		
	}
}
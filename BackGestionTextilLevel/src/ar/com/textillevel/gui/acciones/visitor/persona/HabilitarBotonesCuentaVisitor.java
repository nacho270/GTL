package ar.com.textillevel.gui.acciones.visitor.persona;

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
import ar.com.textillevel.gui.acciones.JFrameVerMovimientosPersona;

public class HabilitarBotonesCuentaVisitor implements IFilaMovimientoVisitor {

	private JFrameVerMovimientosPersona frameMovimientos;

	public HabilitarBotonesCuentaVisitor(JFrameVerMovimientosPersona jFrameVerMovimientos) {
		this.frameMovimientos = jFrameVerMovimientos;
	}

	public void visit(MovimientoHaber movimiento) {

	}

	public void visit(MovimientoDebe movimiento) {

	}

	public void visit(MovimientoInternoCuenta movimiento) {
		
	}
	
	public JFrameVerMovimientosPersona getFrameMovimientos() {
		return frameMovimientos;
	}

	public void setFrameMovimientos(JFrameVerMovimientosPersona frameMovimientos) {
		this.frameMovimientos = frameMovimientos;
	}

	public void visit(MovimientoDebeProveedor movimiento) {
		
	}
	
	public void visit(MovimientoHaberProveedor movimiento) {
		
	}
	
	public void visit(MovimientoHaberBanco movimiento) {

	}

	public void visit(MovimientoDebeBanco movimiento) {
	
	}
	
	public void visit(MovimientoDebePersona movimientoDebePersona) {
		frameMovimientos.getBtnAgregarObservaciones().setEnabled(true);
		frameMovimientos.getBtnEditar().setEnabled(true);
		frameMovimientos.getBtnConfirmar().setEnabled(true);
		frameMovimientos.getBtnEliminar().setEnabled(true);
	}

	public void visit(MovimientoHaberPersona movimientoHaberPersona) {
		frameMovimientos.getBtnAgregarObservaciones().setEnabled(true);
		frameMovimientos.getBtnEditar().setEnabled(true);
		frameMovimientos.getBtnConfirmar().setEnabled(movimientoHaberPersona.getOrdenDePago().getUsuarioVerificador()==null);
		frameMovimientos.getBtnEliminar().setEnabled(true);
	}
}

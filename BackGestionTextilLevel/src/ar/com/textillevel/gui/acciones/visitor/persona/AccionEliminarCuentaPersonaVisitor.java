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

public class AccionEliminarCuentaPersonaVisitor implements IFilaMovimientoVisitor {

	private final JFrameVerMovimientosPersona frameMovimientos;

	public AccionEliminarCuentaPersonaVisitor(JFrameVerMovimientosPersona frameMovimientos) {
		this.frameMovimientos = frameMovimientos;
	}

	public void visit(MovimientoHaber movimiento) {

	}

	public void visit(MovimientoDebe movimiento) {
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
		frameMovimientos.eliminarFactura(movimientoDebePersona.getFacturaPersona());
	}

	public void visit(MovimientoHaberPersona movimientoHaberPersona) {
		frameMovimientos.eliminarOrdenDePago(movimientoHaberPersona.getOrdenDePago());
	}
}

package ar.com.textillevel.gui.acciones.visitor.persona;

import java.awt.Frame;

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

public class AccionConfirmarCuentaVisitor implements IFilaMovimientoVisitor{

	private final Frame padre;

	public AccionConfirmarCuentaVisitor(JFrameVerMovimientosPersona jFrameVerMovimientos) {
		this.padre = jFrameVerMovimientos;
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
		if(movimientoDebePersona.getFacturaPersona() != null) {
			((JFrameVerMovimientosPersona)padre).confirmarFactura(movimientoDebePersona.getFacturaPersona());
		}
		if(movimientoDebePersona.getNotaDebitoPersona() != null) {
			((JFrameVerMovimientosPersona)padre).confirmarND(movimientoDebePersona.getNotaDebitoPersona());
		}
	}

	public void visit(MovimientoHaberPersona movimientoHaberPersona) {
		((JFrameVerMovimientosPersona)padre).confirmarOrden(movimientoHaberPersona.getOrdenDePago());
	}
}

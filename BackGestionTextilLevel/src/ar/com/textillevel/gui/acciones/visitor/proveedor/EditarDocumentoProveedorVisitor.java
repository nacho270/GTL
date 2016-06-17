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

public class EditarDocumentoProveedorVisitor implements IFilaMovimientoVisitor {

	private final JFrameVerMovimientosProveedor frameMovimientos;

	public EditarDocumentoProveedorVisitor(JFrameVerMovimientosProveedor jFrameVerMovimientos) {
		this.frameMovimientos = jFrameVerMovimientos;
	}
	
	public void visit(MovimientoHaber movimiento) {
	}

	public void visit(MovimientoDebe movimiento) {
	}

	public void visit(MovimientoInternoCuenta movimiento) {
		
	}

	public void visit(MovimientoHaberProveedor movimiento) {
		if(movimiento.getOrdenDePago()!=null){
			frameMovimientos.editarOrdenDePago(movimiento.getOrdenDePago());
		}
	}

	public void visit(MovimientoDebeProveedor movimiento) {
		if(movimiento.getFacturaProveedor() != null) {
			frameMovimientos.editarFactura(movimiento.getFacturaProveedor());
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

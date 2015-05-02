package ar.com.textillevel.entidades.cuenta.movimientos.visitor;

import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoHaberProveedor;

public interface IFilaMovmientoProveedorVisitor {
	public void visit(MovimientoHaberProveedor movimiento);
}

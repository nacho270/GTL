package ar.com.textillevel.entidades.cuentaarticulo.movimientos.visitor;

import ar.com.textillevel.entidades.cuentaarticulo.movimientos.MovimientoDebeCuentaArticulo;
import ar.com.textillevel.entidades.cuentaarticulo.movimientos.MovimientoHaberCuentaArticulo;

public interface IMovimientoCuentaArticuloVisitor {

	public void visit(MovimientoDebeCuentaArticulo movimientoDebeCuentaArticulo);

	public void visit(MovimientoHaberCuentaArticulo movimientoHaberCuentaArticulo);

}

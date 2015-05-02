package ar.com.textillevel.entidades.stock.visitor;

import ar.com.textillevel.entidades.stock.MovimientoStockResta;
import ar.com.textillevel.entidades.stock.MovimientoStockSuma;

public interface IMovimientoStockVisitor {
	public void visit(MovimientoStockSuma movimientoSuma);
	public void visit(MovimientoStockResta movimientoResta);
}

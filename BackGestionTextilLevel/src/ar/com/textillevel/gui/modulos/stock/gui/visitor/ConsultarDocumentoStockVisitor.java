package ar.com.textillevel.gui.modulos.stock.gui.visitor;

import java.awt.Frame;

import ar.com.textillevel.entidades.stock.MovimientoStockResta;
import ar.com.textillevel.entidades.stock.MovimientoStockSuma;
import ar.com.textillevel.entidades.stock.visitor.IMovimientoStockVisitor;
import ar.com.textillevel.gui.acciones.JDialogAgregarRemitoEntradaProveedor;

public class ConsultarDocumentoStockVisitor implements IMovimientoStockVisitor{

	private final Frame padre;
	
	public ConsultarDocumentoStockVisitor(Frame padre) {
		this.padre = padre;
	}

	public void visit(MovimientoStockSuma movimientoSuma) {
		if(movimientoSuma.getRemito() != null) {
			JDialogAgregarRemitoEntradaProveedor jd = new JDialogAgregarRemitoEntradaProveedor(padre, true, movimientoSuma.getRemito());
			jd.setVisible(true);
		}
	}

	public void visit(MovimientoStockResta movimientoResta) {
		
	}
}

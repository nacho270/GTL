package ar.com.textillevel.gui.modulos.stock.gui.visitor;

import java.math.BigDecimal;

import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.stock.MovimientoStockResta;
import ar.com.textillevel.entidades.stock.MovimientoStockSuma;
import ar.com.textillevel.entidades.stock.visitor.IMovimientoStockVisitor;

public class GenerarFilaMovimientoStockVisitor implements IMovimientoStockVisitor{

	private final CLJTable tabla;
	private final Integer cantCols;
	
	public GenerarFilaMovimientoStockVisitor(CLJTable tabla, Integer cantCols){
		this.tabla = tabla;
		this.cantCols = cantCols;
	}
	
	public void visit(MovimientoStockSuma movimientoSuma) {
		generarFilaMovimientoSuma(movimientoSuma);
	}

	private void generarFilaMovimientoSuma(MovimientoStockSuma movimientoSuma) {
		Proveedor p = null;
		String nroRemito = null;
		p = movimientoSuma.getRemito().getProveedor();
		nroRemito = movimientoSuma.getRemito().getNroRemito();
		Object[] row = new Object[cantCols];
		row[0] = p.getNombreCorto();
		row[1] = "Remito entrada N�: " + nroRemito;
		row[2] = movimientoSuma.getCantidad();
		row[3] = DateUtil.dateToString(movimientoSuma.getFechaMovimiento(),DateUtil.SHORT_DATE);
		row[4] = movimientoSuma;
		tabla.addRow(row);
	}

	public void visit(MovimientoStockResta movimientoResta) {
		Object[] row = new Object[cantCols];
		if(movimientoResta.getFactura()!=null){
			row[0] = movimientoResta.getFactura().getCliente().getRazonSocial();
			row[1] = "Factura N�: " + movimientoResta.getFactura().getNroFactura();
		}else if(movimientoResta.getRemitoSalida()!=null){
			row[0] = movimientoResta.getRemitoSalida().getCliente().getRazonSocial();
			row[1] = "Remito de salida N�: " + movimientoResta.getRemitoSalida().getNroRemito();
		}else if(movimientoResta.getOdt() !=null){
			row[0] = movimientoResta.getOdt().getRemito().getCliente().getRazonSocial();
			row[1] = "ODT N�: " + movimientoResta.getOdt().getCodigo();
		}else{
			row[0] = "Resta de stock";
			row[1] = "Resta de stock";
		}
		row[2] = movimientoResta.getCantidad().multiply(new BigDecimal(-1d));
		row[3] = DateUtil.dateToString(movimientoResta.getFechaMovimiento(),DateUtil.SHORT_DATE);
		row[4] = movimientoResta;
		tabla.addRow(row);
	}
}

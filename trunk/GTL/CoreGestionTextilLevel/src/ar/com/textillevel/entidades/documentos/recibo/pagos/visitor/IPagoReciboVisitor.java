package ar.com.textillevel.entidades.documentos.recibo.pagos.visitor;

import ar.com.textillevel.entidades.documentos.recibo.pagos.PagoReciboACuenta;
import ar.com.textillevel.entidades.documentos.recibo.pagos.PagoReciboFactura;
import ar.com.textillevel.entidades.documentos.recibo.pagos.PagoReciboNotaDebito;

public interface IPagoReciboVisitor {
	
	public void visit(PagoReciboACuenta prac);
	public void visit(PagoReciboFactura prf);
	public void visit(PagoReciboNotaDebito prnd);

}

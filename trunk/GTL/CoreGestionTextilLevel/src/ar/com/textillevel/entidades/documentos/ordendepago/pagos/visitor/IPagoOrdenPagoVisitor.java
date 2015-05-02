package ar.com.textillevel.entidades.documentos.ordendepago.pagos.visitor;

import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePagoACuenta;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePagoFactura;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePagoNotaDebito;

public interface IPagoOrdenPagoVisitor {
	public void visit(PagoOrdenDePagoACuenta popac);
	public void visit(PagoOrdenDePagoFactura popf);
	public void visit(PagoOrdenDePagoNotaDebito popnd);
}

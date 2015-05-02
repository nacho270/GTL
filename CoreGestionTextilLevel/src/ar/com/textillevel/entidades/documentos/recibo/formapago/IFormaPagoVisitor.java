package ar.com.textillevel.entidades.documentos.recibo.formapago;

public interface IFormaPagoVisitor {

	public abstract void visit(FormaPagoCheque formaPago);
	public abstract void visit(FormaPagoEfectivo formaPagoEfectivo);
	public abstract void visit(FormaPagoRetencionIngresosBrutos formaPagoRetencionIngresosBrutos);
	public abstract void visit(FormaPagoRetencionIVA formaPagoRetencionIVA);
	public abstract void visit(FormaPagoTransferencia formaPagoTransferencia);
	public abstract void visit(FormaPagoNotaCredito formaPagoNotaCredito);
	public abstract void visit(FormaPagoRetencionGanancias formaPagoRetGanancias);

}

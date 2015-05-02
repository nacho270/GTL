package ar.com.textillevel.entidades.documentos.ordendepago.formapago;

public interface IFormaPagoVisitor {

	public abstract void visit(FormaPagoOrdenDePagoCheque formaPago);
	public abstract void visit(FormaPagoOrdenDePagoEfectivo formaPagoEfectivo);
	public abstract void visit(FormaPagoOrdenDePagoRetencionIngresosBrutos formaPagoRetencionIngresosBrutos);
	public abstract void visit(FormaPagoOrdenDePagoRetencionIVA formaPagoRetencionIVA);
	public abstract void visit(FormaPagoOrdenDePagoTransferencia formaPagoTransferencia);
	public abstract void visit(FormaPagoOrdenDePagoNotaCredito formaPagoNotaCredito);
	public abstract void visit(FormaPagoOrdenDePagoRetencionesGanancias formaPagoGanancias);

}

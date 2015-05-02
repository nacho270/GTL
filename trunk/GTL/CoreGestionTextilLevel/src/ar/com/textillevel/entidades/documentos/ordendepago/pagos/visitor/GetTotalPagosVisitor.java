package ar.com.textillevel.entidades.documentos.ordendepago.pagos.visitor;

import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.ordendepago.OrdenDePago;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePagoACuenta;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePagoFactura;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePagoNotaDebito;
import ar.com.textillevel.entidades.documentos.recibo.to.PagoTO;
import ar.com.textillevel.entidades.enums.EDescripcionPagoFactura;
import ar.com.textillevel.util.Utils;

public class GetTotalPagosVisitor implements IPagoOrdenPagoVisitor {

	private PagoTO pagoTO;
	private final OrdenDePago orden;

	public GetTotalPagosVisitor(OrdenDePago orden2) {
		pagoTO = new PagoTO();
		this.orden = orden2;
	}

	public void visit(PagoOrdenDePagoACuenta popac) {
		pagoTO = new PagoTO();
		pagoTO.setConcepto("A Cuenta");
		pagoTO.setFecha(DateUtil.dateToString(orden.getFechaEmision(), DateUtil.SHORT_DATE));
		pagoTO.setImportePagado(Utils.getDecimalFormat().format(popac.getMontoPagado().floatValue()));
	}

	public void visit(PagoOrdenDePagoFactura popf) {
		pagoTO = new PagoTO();
		FacturaProveedor factura = popf.getFactura();
		pagoTO.setConcepto("Factura - Nro.: " + factura.getNroFactura() + getDescrPago(popf));
		pagoTO.setFecha(DateUtil.dateToString(factura.getFechaIngreso()));
		pagoTO.setImportePagado(Utils.getDecimalFormat().format(popf.getMontoPagado().floatValue()));
		pagoTO.setIdDocumento(popf.getFactura().getId());
		pagoTO.setIdTipoDocumento(ETipoDocumento.FACTURA_PROV.getId());
	}

	public void visit(PagoOrdenDePagoNotaDebito popnd) {
		pagoTO = new PagoTO();
		pagoTO.setConcepto("Nota de Débito - Nro. :" + popnd.getNotaDebito().getNroCorreccion() + getDescrPago(popnd));
		pagoTO.setFecha(DateUtil.dateToString(popnd.getNotaDebito().getFechaIngreso()));
		pagoTO.setImportePagado(Utils.getDecimalFormat().format(popnd.getMontoPagado().floatValue()));
		pagoTO.setIdDocumento(popnd.getNotaDebito().getId());
		pagoTO.setIdTipoDocumento(ETipoDocumento.NOTA_DEBITO_PROV.getId());
	}

	public PagoTO getPagoTO() {
		return pagoTO;
	}

	public void setPagoTO(PagoTO pagoTO) {
		this.pagoTO = pagoTO;
	}

	private String getDescrPago(PagoOrdenDePagoFactura prf) {
		return getDecripcionPago(prf.getDescrPagoFactura());
	}

	private String getDecripcionPago(EDescripcionPagoFactura descrPagoFactura) {
		if (descrPagoFactura == null) {
			return "";
		}
		if (descrPagoFactura == EDescripcionPagoFactura.FACTURA) {
			return "";
		} else {
			return " (" + descrPagoFactura.getDescripcion() + ") ";
		}
	}

	private String getDescrPago(PagoOrdenDePagoNotaDebito prf) {
		return getDecripcionPago(prf.getDescrPagoFactura());
	}
}

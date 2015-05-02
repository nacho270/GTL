package ar.com.textillevel.entidades.documentos.recibo.pagos.visitor;

import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.NotaDebito;
import ar.com.textillevel.entidades.documentos.recibo.Recibo;
import ar.com.textillevel.entidades.documentos.recibo.pagos.PagoReciboACuenta;
import ar.com.textillevel.entidades.documentos.recibo.pagos.PagoReciboFactura;
import ar.com.textillevel.entidades.documentos.recibo.pagos.PagoReciboNotaDebito;
import ar.com.textillevel.entidades.documentos.recibo.to.PagoTO;
import ar.com.textillevel.entidades.enums.EDescripcionPagoFactura;
import ar.com.textillevel.util.Utils;

public class ItemImpresionReciboVisitor implements IPagoReciboVisitor {

	private final PagoTO pagoTO;
	private final Recibo recibo;
	
	public ItemImpresionReciboVisitor(Recibo recibo) {
		this.recibo = recibo;
		this.pagoTO = new PagoTO();
	}

	public void visit(PagoReciboACuenta prac) {
		pagoTO.setConcepto("A Cuenta");
		pagoTO.setFecha(DateUtil.dateToString(recibo.getFecha()));
		pagoTO.setImportePagado(Utils.getDecimalFormat().format(prac.getMontoPagado().floatValue()));
	}

	public void visit(PagoReciboFactura prf) {
		Factura factura = prf.getFactura();
		pagoTO.setConcepto("Factura - Nro.: " + factura.getNroFactura() + getDescrPagoFactura(prf));
		pagoTO.setFecha(DateUtil.dateToString(factura.getFechaEmision()));
		pagoTO.setImportePagado(Utils.getDecimalFormat().format(prf.getMontoPagado().floatValue()));
		pagoTO.setIdTipoDocumento(ETipoDocumento.FACTURA.getId());
		pagoTO.setIdDocumento(prf.getFactura().getId());
	}

	public void visit(PagoReciboNotaDebito prnd) {
		NotaDebito nd = prnd.getNotaDebito(); 
		pagoTO.setConcepto("Nota de Débito");
		pagoTO.setFecha(DateUtil.dateToString(nd.getFechaEmision()));
		pagoTO.setImportePagado(Utils.getDecimalFormat().format(prnd.getMontoPagado().floatValue()));
		pagoTO.setIdTipoDocumento(ETipoDocumento.NOTA_DEBITO.getId());
		pagoTO.setIdDocumento(prnd.getNotaDebito().getId());
	}

	public PagoTO getPagoTO() {
		return pagoTO;
	}

	private String getDescrPagoFactura(PagoReciboFactura prf) {
		if(prf.getDescrPagoFactura() == null) {
			return "";
		}
		if(prf.getDescrPagoFactura() == EDescripcionPagoFactura.FACTURA) {
			return "";
		} else {
			return " (" + prf.getDescrPagoFactura().getDescripcion() + ") ";
		}
	}

}

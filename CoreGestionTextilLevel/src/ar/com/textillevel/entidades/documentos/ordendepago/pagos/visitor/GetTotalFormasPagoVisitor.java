package ar.com.textillevel.entidades.documentos.ordendepago.pagos.visitor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.ordendepago.formapago.FormaPagoOrdenDePagoCheque;
import ar.com.textillevel.entidades.documentos.ordendepago.formapago.FormaPagoOrdenDePagoEfectivo;
import ar.com.textillevel.entidades.documentos.ordendepago.formapago.FormaPagoOrdenDePagoNotaCredito;
import ar.com.textillevel.entidades.documentos.ordendepago.formapago.FormaPagoOrdenDePagoRetencionIVA;
import ar.com.textillevel.entidades.documentos.ordendepago.formapago.FormaPagoOrdenDePagoRetencionIngresosBrutos;
import ar.com.textillevel.entidades.documentos.ordendepago.formapago.FormaPagoOrdenDePagoRetencionesGanancias;
import ar.com.textillevel.entidades.documentos.ordendepago.formapago.FormaPagoOrdenDePagoTransferencia;
import ar.com.textillevel.entidades.documentos.ordendepago.formapago.IFormaPagoVisitor;
import ar.com.textillevel.entidades.documentos.recibo.to.ChequeTO;
import ar.com.textillevel.entidades.documentos.recibo.to.NotaCreditoTO;
import ar.com.textillevel.entidades.documentos.recibo.to.TransferenciaBancariaTO;
import ar.com.textillevel.util.Utils;

public class GetTotalFormasPagoVisitor implements IFormaPagoVisitor {

	private List<ChequeTO> cheques;
	private final List<NotaCreditoTO> notasCredito;
	private Double totalNotasCredito;
	private TransferenciaBancariaTO infoTransf;
	private BigDecimal total;
	private BigDecimal totalCheques;
	private BigDecimal totEfectivo;
	private BigDecimal totGanancias;
	private BigDecimal totIngBrut;
	private BigDecimal totIva;
	private BigDecimal importeTransfBancaria;

	public GetTotalFormasPagoVisitor() {
		cheques = new ArrayList<ChequeTO>();
		notasCredito = new ArrayList<NotaCreditoTO>();
		total = BigDecimal.ZERO;
		totalCheques = BigDecimal.ZERO;
		totEfectivo = BigDecimal.ZERO;
		totGanancias = BigDecimal.ZERO;
		totIngBrut = BigDecimal.ZERO;
		importeTransfBancaria = BigDecimal.ZERO;
		totIva = BigDecimal.ZERO;
		totalNotasCredito = 0d;
	}

	public void visit(FormaPagoOrdenDePagoCheque formaPago) {
		ChequeTO chequeTO = new ChequeTO();
		chequeTO.setBanco(formaPago.getCheque().getBanco().getNombre());
		chequeTO.setCuit(formaPago.getCheque().getCuit());
		chequeTO.setImporte(Utils.getDecimalFormat().format(formaPago.getImporte().floatValue()));
		chequeTO.setNumero(formaPago.getCheque().getNumero() + " (" + formaPago.getCheque().getNumeracion() + ")");
		chequeTO.setIdDocumento(formaPago.getCheque().getId());
		chequeTO.setIdTipoDocumento(ETipoDocumento.CHEQUE.getId());
		cheques.add(chequeTO);
		totalCheques = totalCheques.add(formaPago.getCheque().getImporte());
		total = total.add(formaPago.getCheque().getImporte());
	}

	public void visit(FormaPagoOrdenDePagoEfectivo formaPagoEfectivo) {
		totEfectivo = formaPagoEfectivo.getImporte();
		total = total.add(formaPagoEfectivo.getImporte());
	}

	public void visit(FormaPagoOrdenDePagoRetencionIngresosBrutos formaPagoRetencionIngresosBrutos) {
		totIngBrut = formaPagoRetencionIngresosBrutos.getImporte();
		total = total.add(formaPagoRetencionIngresosBrutos.getImporte());
	}

	public void visit(FormaPagoOrdenDePagoRetencionIVA formaPagoRetencionIVA) {
		totIva = formaPagoRetencionIVA.getImporte();
		total = total.add(formaPagoRetencionIVA.getImporte());
	}

	public void visit(FormaPagoOrdenDePagoTransferencia formaPagoTransferencia) {
		total = total.add(formaPagoTransferencia.getImporte());
		importeTransfBancaria = formaPagoTransferencia.getImporte();
		setInfoTransf(new TransferenciaBancariaTO(Utils.getDecimalFormat().format(formaPagoTransferencia.getImporte().doubleValue()), String.valueOf(formaPagoTransferencia.getNroTx()), formaPagoTransferencia.getObservaciones()));
	}

	public void visit(FormaPagoOrdenDePagoNotaCredito formaPagoNotaCredito) {
		NotaCreditoTO nc = new NotaCreditoTO();
		nc.setDescrNC("Nota de crédito Nro. :" + formaPagoNotaCredito.getNotaCredito().getNroCorreccion());
		nc.setFecha(DateUtil.dateToString(formaPagoNotaCredito.getFechaEmision(), DateUtil.SHORT_DATE));
		nc.setImporte(Utils.getDecimalFormat().format(formaPagoNotaCredito.getImporteNC()));
		nc.setIdDocumento(formaPagoNotaCredito.getNotaCredito().getId());
		nc.setIdTipoDocumento(ETipoDocumento.NOTA_CREDITO_PROV.getId());
		notasCredito.add(nc);
		totalNotasCredito += formaPagoNotaCredito.getImporteNC().doubleValue();
		total = total.add(formaPagoNotaCredito.getImporteNC());
	}

	public void visit(FormaPagoOrdenDePagoRetencionesGanancias formaPagoGanancias) {
		totGanancias = formaPagoGanancias.getImporte();
		total = total.add(formaPagoGanancias.getImporte());
	}

	public List<ChequeTO> getCheques() {
		return cheques;
	}

	public void setCheques(List<ChequeTO> cheques) {
		this.cheques = cheques;
	}

	public List<NotaCreditoTO> getNotasCredito() {
		return notasCredito;
	}

	public Double getTotalNotasCredito() {
		return totalNotasCredito;
	}

	public void setTotalNotasCredito(Double totalNotasCredito) {
		this.totalNotasCredito = totalNotasCredito;
	}

	public TransferenciaBancariaTO getInfoTransf() {
		return infoTransf;
	}

	public void setInfoTransf(TransferenciaBancariaTO infoTransf) {
		this.infoTransf = infoTransf;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getTotalCheques() {
		return totalCheques;
	}

	public void setTotalCheques(BigDecimal totalCheques) {
		this.totalCheques = totalCheques;
	}

	public BigDecimal getTotEfectivo() {
		return totEfectivo;
	}

	public void setTotEfectivo(BigDecimal totEfectivo) {
		this.totEfectivo = totEfectivo;
	}

	public BigDecimal getTotGanancias() {
		return totGanancias;
	}

	public void setTotGanancias(BigDecimal totGanancias) {
		this.totGanancias = totGanancias;
	}

	public BigDecimal getTotIngBrut() {
		return totIngBrut;
	}

	public void setTotIngBrut(BigDecimal totIngBrut) {
		this.totIngBrut = totIngBrut;
	}

	public BigDecimal getTotIva() {
		return totIva;
	}

	public void setTotIva(BigDecimal totIva) {
		this.totIva = totIva;
	}

	public BigDecimal getImporteTransfBancaria() {
		return importeTransfBancaria;
	}
}
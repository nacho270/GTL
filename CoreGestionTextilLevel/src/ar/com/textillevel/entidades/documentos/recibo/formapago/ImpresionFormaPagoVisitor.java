package ar.com.textillevel.entidades.documentos.recibo.formapago;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.recibo.to.ChequeTO;
import ar.com.textillevel.entidades.documentos.recibo.to.NotaCreditoTO;
import ar.com.textillevel.util.Utils;

public class ImpresionFormaPagoVisitor implements IFormaPagoVisitor {
	
	private final List<ChequeTO> chequeList;
	private final List<NotaCreditoTO> notaCreditoList;
	private BigDecimal importeTransfBancaria;
	private String observacionesTx;
	private String nroTx;

	private BigDecimal importeRetIVA;
	private BigDecimal importeRetGan;
	private BigDecimal importeEfectivo;
	private BigDecimal totalCheques;
	private BigDecimal totalNCs;
	private BigDecimal importeRetIIBB;

	public ImpresionFormaPagoVisitor() {
		this.chequeList = new ArrayList<ChequeTO>();
		this.notaCreditoList = new ArrayList<NotaCreditoTO>();
		this.importeTransfBancaria = BigDecimal.ZERO;
		this.importeRetIVA = BigDecimal.ZERO;
		this.importeRetGan = BigDecimal.ZERO;
		this.importeRetIIBB = BigDecimal.ZERO;
		this.importeEfectivo = BigDecimal.ZERO;
		this.totalCheques = BigDecimal.ZERO;
		this.totalNCs = BigDecimal.ZERO;
	}

	public void visit(FormaPagoCheque fpc) {
		ChequeTO chequeTO = new ChequeTO();
		chequeTO.setBanco(fpc.getCheque().getBanco().getNombre());
		chequeTO.setCuit(fpc.getCheque().getCuit());
		chequeTO.setImporte(Utils.getDecimalFormat().format(fpc.getImporte().floatValue()));
		chequeTO.setNumero(fpc.getCheque().getNumero());
		chequeTO.setNumeroInterno(fpc.getCheque().getNumeracion().toString());
		chequeTO.setIdTipoDocumento(ETipoDocumento.CHEQUE.getId());
		chequeTO.setIdDocumento(fpc.getCheque().getId());
		chequeList.add(chequeTO);
		totalCheques = totalCheques.add(fpc.getImporte());
	}

	public void visit(FormaPagoEfectivo formaPagoEfectivo) {
		this.importeEfectivo = formaPagoEfectivo.getImporte();
	}

	public void visit(FormaPagoRetencionIngresosBrutos formaPagoRetencionIngresosBrutos) {
		this.importeRetIIBB = formaPagoRetencionIngresosBrutos.getImporte();
	}

	public void visit(FormaPagoRetencionGanancias formaPagoRetGanancias) {
		this.importeRetGan = formaPagoRetGanancias.getImporte();
	}
	
	public void visit(FormaPagoRetencionIVA formaPagoRetencionIVA) {
		this.importeRetIVA = formaPagoRetencionIVA.getImporte();
	}

	public void visit(FormaPagoTransferencia formaPagoTransferencia) {
		this.importeTransfBancaria = formaPagoTransferencia.getImporte();
		this.observacionesTx = formaPagoTransferencia.getObservaciones();
		this.nroTx = formaPagoTransferencia.getNroTx() == null ? "" : String.valueOf(formaPagoTransferencia.getNroTx());
	}

	public void visit(FormaPagoNotaCredito formaPagoNotaCredito) {
		NotaCreditoTO notaCreditoTO = new NotaCreditoTO();
		notaCreditoTO.setFecha(DateUtil.dateToString(formaPagoNotaCredito.getFechaEmision()));
		notaCreditoTO.setDescrNC("NC " + formaPagoNotaCredito.getNotaCredito().getNroFactura());
		notaCreditoTO.setImporte(Utils.getDecimalFormat().format(formaPagoNotaCredito.getImporte().floatValue()));
		notaCreditoTO.setIdDocumento(formaPagoNotaCredito.getNotaCredito().getId());
		notaCreditoTO.setIdTipoDocumento(ETipoDocumento.NOTA_CREDITO.getId());
		notaCreditoList.add(notaCreditoTO);
		totalNCs = totalNCs.add(formaPagoNotaCredito.getImporte());
	}

	public List<ChequeTO> getChequeList() {
		return chequeList;
	}

	public List<NotaCreditoTO> getNotaCreditoList() {
		return notaCreditoList;
	}

	public BigDecimal getImporteTransfBancaria() {
		return importeTransfBancaria;
	}

	public BigDecimal getImporteRetIVA() {
		return importeRetIVA;
	}

	public BigDecimal getImporteRetGan() {
		return importeRetGan;
	}

	public BigDecimal getImporteRetIIBB() {
		return importeRetIIBB;
	}

	public BigDecimal getImporteEfectivo() {
		return importeEfectivo;
	}

	public BigDecimal getTotalCheques() {
		return totalCheques;
	}

	public BigDecimal getTotalNCs() {
		return totalNCs;
	}

	public String getObservacionesTx() {
		return observacionesTx;
	}

	public String getNtoTx() {
		return nroTx;
	}

}
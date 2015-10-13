package ar.com.textillevel.entidades.documentos.recibo.to;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.cuenta.to.CuentaOwnerTO;
import ar.com.textillevel.entidades.documentos.recibo.Recibo;
import ar.com.textillevel.entidades.documentos.recibo.formapago.FormaPago;
import ar.com.textillevel.entidades.documentos.recibo.formapago.ImpresionFormaPagoVisitor;
import ar.com.textillevel.entidades.documentos.recibo.pagos.PagoRecibo;
import ar.com.textillevel.entidades.documentos.recibo.pagos.visitor.ItemImpresionReciboVisitor;
import ar.com.textillevel.util.Utils;

public class ReciboMobileTO implements Serializable {

	private static final long serialVersionUID = -4505741114457472275L;

	private String nroRecibo;
	private String txtCantidadPesos;
	private String fecha;
	private CuentaOwnerTO cliente;
	private List<PagoTO> pagos;
	private List<NotaCreditoTO> notasCredito;
	private List<ChequeTO> cheques;
	private TransferenciaBancariaTO datosTransferencia;
	private String importeRetIVA;
	private String importeRetGan;
	private String importeRetIIBB;
	private String importeEfectivo;
	private String importeTotal;
	private String totalPagos;
	private String totalCheques;
	private String totalNC;

	public ReciboMobileTO(Recibo recibo) {
		this.nroRecibo = String.valueOf(recibo.getNroRecibo());
		this.txtCantidadPesos = recibo.getTxtCantidadPesos();
		this.fecha = DateUtil.dateToString(recibo.getFechaEmision());
		this.cliente = new CuentaOwnerTO(recibo.getCliente());
		this.importeTotal = Utils.getDecimalFormat().format(recibo.getMonto().doubleValue());
		cargarDatosFormasDePago(recibo);
		cargarDatosPagos(recibo);
	}

	private void cargarDatosPagos(Recibo recibo) {
		BigDecimal total = new BigDecimal(0);
		this.pagos = new ArrayList<PagoTO>();
		for (PagoRecibo pr : recibo.getPagoReciboList()) {
			ItemImpresionReciboVisitor iirv = new ItemImpresionReciboVisitor(recibo);
			pr.accept(iirv);
			this.pagos.add(iirv.getPagoTO());
			total = total.add(pr.getMontoPagado());
		}
		setTotalPagos(Utils.getDecimalFormat().format(total.floatValue()));
	}

	private void cargarDatosFormasDePago(Recibo recibo) {
		ImpresionFormaPagoVisitor ifpv = new ImpresionFormaPagoVisitor();
		for (FormaPago fp : recibo.getPagos()) {
			fp.accept(ifpv);
		}
		if (!ifpv.getTotalCheques().equals(BigDecimal.ZERO)) {
			this.cheques = ifpv.getChequeList();
			setTotalCheques(Utils.getDecimalFormat().format(ifpv.getTotalCheques().doubleValue()));
		}
		if (!ifpv.getTotalNCs().equals(BigDecimal.ZERO)) {
			this.notasCredito = ifpv.getNotaCreditoList();
			setTotalNC(Utils.getDecimalFormat().format(ifpv.getTotalNCs().doubleValue()));
		}
		if (!ifpv.getImporteRetIVA().equals(BigDecimal.ZERO)) {
			this.importeRetIVA = Utils.fixPrecioCero(Utils.getDecimalFormat().format(ifpv.getImporteRetIVA().doubleValue()));
		}
		if (!ifpv.getImporteRetGan().equals(BigDecimal.ZERO)) {
			this.importeRetGan = Utils.fixPrecioCero(Utils.getDecimalFormat().format(ifpv.getImporteRetGan().doubleValue()));
		}
		if (!ifpv.getImporteRetIIBB().equals(BigDecimal.ZERO)) {
			this.importeRetIIBB = Utils.fixPrecioCero(Utils.getDecimalFormat().format(ifpv.getImporteRetIIBB().doubleValue()));
		}
		if (!ifpv.getImporteEfectivo().equals(BigDecimal.ZERO)) {
			this.importeEfectivo = Utils.fixPrecioCero(Utils.getDecimalFormat().format(ifpv.getImporteEfectivo().doubleValue()));
		}
		if (!ifpv.getImporteTransfBancaria().equals(BigDecimal.ZERO)) {
			datosTransferencia = new TransferenciaBancariaTO();
			datosTransferencia.setImporteTransfBancaria(Utils.fixPrecioCero(Utils.getDecimalFormat().format(ifpv.getImporteTransfBancaria().doubleValue())));
			String observacionesTx2 = ifpv.getObservacionesTx();
			String nroTx2 = ifpv.getNtoTx();
			datosTransferencia.setObservacionesTx(StringUtil.isNullOrEmpty(observacionesTx2) ? "" : observacionesTx2);
			datosTransferencia.setNroTx(StringUtil.isNullOrEmpty(nroTx2) ? "" : nroTx2);
		}
	}

	public String getNroRecibo() {
		return nroRecibo;
	}

	public void setNroRecibo(String nroRecibo) {
		this.nroRecibo = nroRecibo;
	}

	public String getTxtCantidadPesos() {
		return txtCantidadPesos;
	}

	public void setTxtCantidadPesos(String txtCantidadPesos) {
		this.txtCantidadPesos = txtCantidadPesos;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public CuentaOwnerTO getCliente() {
		return cliente;
	}

	public void setCliente(CuentaOwnerTO cliente) {
		this.cliente = cliente;
	}

	public List<PagoTO> getPagos() {
		return pagos;
	}

	public void setPagos(List<PagoTO> pagos) {
		this.pagos = pagos;
	}

	public List<NotaCreditoTO> getNotasCredito() {
		return notasCredito;
	}

	public void setNotasCredito(List<NotaCreditoTO> notasCredito) {
		this.notasCredito = notasCredito;
	}

	public List<ChequeTO> getCheques() {
		return cheques;
	}

	public void setCheques(List<ChequeTO> cheques) {
		this.cheques = cheques;
	}

	public TransferenciaBancariaTO getDatosTransferencia() {
		return datosTransferencia;
	}

	public void setDatosTransferencia(TransferenciaBancariaTO datosTransferencia) {
		this.datosTransferencia = datosTransferencia;
	}

	public String getImporteRetIVA() {
		return importeRetIVA;
	}

	public void setImporteRetIVA(String importeRetIVA) {
		this.importeRetIVA = importeRetIVA;
	}

	public String getImporteRetGan() {
		return importeRetGan;
	}

	public void setImporteRetGan(String importeRetGan) {
		this.importeRetGan = importeRetGan;
	}

	public String getImporteRetIIBB() {
		return importeRetIIBB;
	}

	public void setImporteRetIIBB(String importeRetIIBB) {
		this.importeRetIIBB = importeRetIIBB;
	}

	public String getImporteEfectivo() {
		return importeEfectivo;
	}

	public void setImporteEfectivo(String importeEfectivo) {
		this.importeEfectivo = importeEfectivo;
	}

	public String getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(String importeTotal) {
		this.importeTotal = importeTotal;
	}

	public String getTotalPagos() {
		return totalPagos;
	}

	public void setTotalPagos(String totalPagos) {
		this.totalPagos = totalPagos;
	}

	public String getTotalCheques() {
		return totalCheques;
	}

	public void setTotalCheques(String totalCheques) {
		this.totalCheques = totalCheques;
	}

	public String getTotalNC() {
		return totalNC;
	}

	public void setTotalNC(String totalNC) {
		this.totalNC = totalNC;
	}
}

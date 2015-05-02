package ar.com.textillevel.entidades.documentos.ordendepago.to;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.entidades.cuenta.to.CuentaOwnerTO;
import ar.com.textillevel.entidades.documentos.ordendepago.OrdenDePago;
import ar.com.textillevel.entidades.documentos.ordendepago.formapago.FormaPagoOrdenDePago;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePago;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.visitor.GetTotalFormasPagoVisitor;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.visitor.GetTotalPagosVisitor;
import ar.com.textillevel.entidades.documentos.recibo.to.ChequeTO;
import ar.com.textillevel.entidades.documentos.recibo.to.NotaCreditoTO;
import ar.com.textillevel.entidades.documentos.recibo.to.PagoTO;
import ar.com.textillevel.entidades.documentos.recibo.to.TransferenciaBancariaTO;
import ar.com.textillevel.util.Utils;

public class OrdenDePagoMobileTO implements Serializable {

	private static final long serialVersionUID = -1346964711573578263L;

	private String nroOrden;
	private String fechaOrden;
	private CuentaOwnerTO cliente;
	private String totIngBrut;
	private String totIva;
	private String totGanancias;
	private String totEfectivo;
	private String total;
	private String totalPagos;
	private String totalCheques;
	private String totalNC;
	private List<PagoTO> pagos;
	private List<ChequeTO> cheques;
	private List<NotaCreditoTO> notasCredito;
	private TransferenciaBancariaTO infoTransferencia;
	private String txtCantidadPesos;

	public OrdenDePagoMobileTO(OrdenDePago orden) {
		this.nroOrden = String.valueOf(orden.getNroOrden());
		this.fechaOrden = DateUtil.dateToString(orden.getFechaEmision());
		this.cliente = new CuentaOwnerTO(orden.getProveedor());
		this.txtCantidadPesos = orden.getTxtCantidadPesos();
		cargarDatosFormasDePago(orden);
		cargarDatosPagos(orden);
	}

	private void cargarDatosPagos(OrdenDePago orden) {
		this.pagos = new ArrayList<PagoTO>();
		BigDecimal totalPagos = new BigDecimal(0);
		GetTotalPagosVisitor sumaPagos = new GetTotalPagosVisitor(orden);
		for (PagoOrdenDePago p : orden.getPagos()) {
			p.accept(sumaPagos);
			this.pagos.add(sumaPagos.getPagoTO());
			totalPagos = totalPagos.add(p.getMontoPagado());
		}
		setTotalPagos(Utils.getDecimalFormat().format(totalPagos.floatValue()));
	}

	private void cargarDatosFormasDePago(OrdenDePago orden) {
		GetTotalFormasPagoVisitor totalesFormaPago = new GetTotalFormasPagoVisitor();
		for (FormaPagoOrdenDePago fp : orden.getFormasDePago()) {
			fp.accept(totalesFormaPago);
		}

		if (!totalesFormaPago.getTotalCheques().equals(BigDecimal.ZERO)) {
			this.cheques = totalesFormaPago.getCheques();
			totalCheques = Utils.getDecimalFormat().format(totalesFormaPago.getTotalCheques().doubleValue());
		}
		if (!totalesFormaPago.getTotalNotasCredito().equals(BigDecimal.ZERO)) {
			this.notasCredito = totalesFormaPago.getNotasCredito();
			setTotalNC(Utils.getDecimalFormat().format(totalesFormaPago.getTotalNotasCredito().doubleValue()));
		}
		if (!totalesFormaPago.getTotEfectivo().equals(BigDecimal.ZERO)) {
			this.totEfectivo = Utils.fixPrecioCero(Utils.getDecimalFormat().format(totalesFormaPago.getTotEfectivo().doubleValue()));
		}
		if (!totalesFormaPago.getTotGanancias().equals(BigDecimal.ZERO)) {
			this.totGanancias = Utils.fixPrecioCero(Utils.getDecimalFormat().format(totalesFormaPago.getTotGanancias().doubleValue()));
		}
		if (!totalesFormaPago.getTotIngBrut().equals(BigDecimal.ZERO)) {
			this.totIngBrut = Utils.fixPrecioCero(Utils.getDecimalFormat().format(totalesFormaPago.getTotIngBrut().doubleValue()));
		}
		if (!totalesFormaPago.getTotIva().equals(BigDecimal.ZERO)) {
			this.totIva = Utils.fixPrecioCero(Utils.getDecimalFormat().format(totalesFormaPago.getTotIva().doubleValue()));
		}
		if (!totalesFormaPago.getImporteTransfBancaria().equals(BigDecimal.ZERO)) {
			infoTransferencia = totalesFormaPago.getInfoTransf();
		}
		this.total = Utils.getDecimalFormat().format(totalesFormaPago.getTotal().doubleValue());
	}

	public String getNroOrden() {
		return nroOrden;
	}

	public void setNroOrden(String nroOrden) {
		this.nroOrden = nroOrden;
	}

	public String getFechaOrden() {
		return fechaOrden;
	}

	public void setFechaOrden(String fechaOrden) {
		this.fechaOrden = fechaOrden;
	}

	public CuentaOwnerTO getCliente() {
		return cliente;
	}

	public void setCliente(CuentaOwnerTO cliente) {
		this.cliente = cliente;
	}

	public String getTotIngBrut() {
		return totIngBrut;
	}

	public void setTotIngBrut(String totIngBrut) {
		this.totIngBrut = totIngBrut;
	}

	public String getTotIva() {
		return totIva;
	}

	public void setTotIva(String totIva) {
		this.totIva = totIva;
	}

	public String getTotGanancias() {
		return totGanancias;
	}

	public void setTotGanancias(String totGanancias) {
		this.totGanancias = totGanancias;
	}

	public String getTotEfectivo() {
		return totEfectivo;
	}

	public void setTotEfectivo(String totEfectivo) {
		this.totEfectivo = totEfectivo;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
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

	public List<PagoTO> getPagos() {
		return pagos;
	}

	public void setPagos(List<PagoTO> pagos) {
		this.pagos = pagos;
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

	public void setNotasCredito(List<NotaCreditoTO> notasCredito) {
		this.notasCredito = notasCredito;
	}

	public TransferenciaBancariaTO getInfoTransferencia() {
		return infoTransferencia;
	}

	public void setInfoTransferencia(TransferenciaBancariaTO infoTransferencia) {
		this.infoTransferencia = infoTransferencia;
	}

	public String getTotalNC() {
		return totalNC;
	}

	public void setTotalNC(String totalNC) {
		this.totalNC = totalNC;
	}

	public String getTxtCantidadPesos() {
		return txtCantidadPesos;
	}

	public void setTxtCantidadPesos(String txtCantidadPesos) {
		this.txtCantidadPesos = txtCantidadPesos;
	}
}

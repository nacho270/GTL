package ar.com.textillevel.mobile.modules.recibo.to;

import java.io.Serializable;
import java.util.List;

import ar.com.textillevel.mobile.modules.common.to.ChequeTO;
import ar.com.textillevel.mobile.modules.common.to.NotaCreditoTO;
import ar.com.textillevel.mobile.modules.common.to.PagoTO;
import ar.com.textillevel.mobile.modules.common.to.TransferenciaBancariaTO;
import ar.com.textillevel.mobile.modules.cuenta.to.CuentaOwnerTO;

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

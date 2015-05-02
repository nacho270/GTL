package ar.com.textillevel.mobile.modules.ordenpago.to;

import java.io.Serializable;
import java.util.List;

import ar.com.textillevel.mobile.modules.common.to.ChequeTO;
import ar.com.textillevel.mobile.modules.common.to.NotaCreditoTO;
import ar.com.textillevel.mobile.modules.common.to.PagoTO;
import ar.com.textillevel.mobile.modules.common.to.TransferenciaBancariaTO;
import ar.com.textillevel.mobile.modules.cuenta.to.CuentaOwnerTO;

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

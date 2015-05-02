package ar.com.textillevel.mobile.modules.common.to;

import java.io.Serializable;

public class TransferenciaBancariaTO implements Serializable {

	private static final long serialVersionUID = 1159074976905996044L;

	private String importeTransfBancaria;
	private String nroTx;
	private String observacionesTx;

	public TransferenciaBancariaTO() {

	}

	public TransferenciaBancariaTO(String importe, String nroTransf, String observaciones) {
		this.importeTransfBancaria = importe;
		this.nroTx = nroTransf;
		this.observacionesTx = observaciones;
	}

	public String getImporteTransfBancaria() {
		return importeTransfBancaria;
	}

	public void setImporteTransfBancaria(String importeTransfBancaria) {
		this.importeTransfBancaria = importeTransfBancaria;
	}

	public String getNroTx() {
		return nroTx;
	}

	public void setNroTx(String nroTx) {
		this.nroTx = nroTx;
	}

	public String getObservacionesTx() {
		return observacionesTx;
	}

	public void setObservacionesTx(String observacionesTx) {
		this.observacionesTx = observacionesTx;
	}
}

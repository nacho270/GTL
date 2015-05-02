package ar.com.textillevel.entidades.to.ivacompras;

import java.io.Serializable;

public class DetalleImpuestoFacturaIVAComprasTO implements Serializable {

	private static final long serialVersionUID = -754281516255609520L;

	private Integer idImpuesto;
	private double importe;

	public DetalleImpuestoFacturaIVAComprasTO(Integer idImpuesto, double importe) {
		this.idImpuesto = idImpuesto;
		this.importe = importe;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	public Integer getIdImpuesto() {
		return idImpuesto;
	}

	public void setIdImpuesto(Integer idImpuesto) {
		this.idImpuesto = idImpuesto;
	}

}
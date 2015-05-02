package ar.com.textillevel.mobile.modules.remito.entrada.proveedor.to;

import java.io.Serializable;
import java.util.List;

import ar.com.textillevel.mobile.modules.cuenta.to.CuentaOwnerTO;

public class RemitoEntradaProvMobileTO implements Serializable {

	private static final long serialVersionUID = 2209563763060485491L;

	private CuentaOwnerTO owner;
	private String nroRemito;
	private String fecha;
	private List<PiezaRemitoProvMobileTO> piezas;

	public CuentaOwnerTO getOwner() {
		return owner;
	}

	public void setOwner(CuentaOwnerTO owner) {
		this.owner = owner;
	}

	public String getNroRemito() {
		return nroRemito;
	}

	public void setNroRemito(String nroRemito) {
		this.nroRemito = nroRemito;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public List<PiezaRemitoProvMobileTO> getPiezas() {
		return piezas;
	}

	public void setPiezas(List<PiezaRemitoProvMobileTO> piezas) {
		this.piezas = piezas;
	}

}
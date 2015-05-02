package ar.com.textillevel.mobile.modules.remito.salida.to;

import java.io.Serializable;
import java.util.List;

import ar.com.textillevel.mobile.modules.common.DocumentoRelMobTO;
import ar.com.textillevel.mobile.modules.cuenta.to.CuentaOwnerTO;
import ar.com.textillevel.mobile.modules.remito.entrada.cliente.to.PiezaRemitoMobileTO;

public class RemitoSalidaMobileTO implements Serializable {

	private static final long serialVersionUID = -235914064511919664L;

	private CuentaOwnerTO owner;
	private String nroRemito;
	private String fecha;
	private String pesoTotal;
	private String totalMetros;
	private String productos;
	private String odts;
	private List<PiezaRemitoMobileTO> piezas;
	private List<DocumentoRelMobTO> remitosEntrada;

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

	public String getPesoTotal() {
		return pesoTotal;
	}

	public void setPesoTotal(String pesoTotal) {
		this.pesoTotal = pesoTotal;
	}

	public String getTotalMetros() {
		return totalMetros;
	}

	public void setTotalMetros(String totalMetros) {
		this.totalMetros = totalMetros;
	}

	public String getProductos() {
		return productos;
	}

	public void setProductos(String productos) {
		this.productos = productos;
	}

	public String getOdts() {
		return odts;
	}

	public void setOdts(String odts) {
		this.odts = odts;
	}

	public List<PiezaRemitoMobileTO> getPiezas() {
		return piezas;
	}

	public List<DocumentoRelMobTO> getRemitosEntrada() {
		return remitosEntrada;
	}
	
	public void setPiezas(List<PiezaRemitoMobileTO> piezas) {
		this.piezas = piezas;
	}

	public void setRemitosEntrada(List<DocumentoRelMobTO> remitosEntrada) {
		this.remitosEntrada = remitosEntrada;
	}

}
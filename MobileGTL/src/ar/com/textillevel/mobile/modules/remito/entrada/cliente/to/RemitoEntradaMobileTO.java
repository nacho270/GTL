package ar.com.textillevel.mobile.modules.remito.entrada.cliente.to;

import java.io.Serializable;
import java.util.List;

import ar.com.textillevel.mobile.modules.cuenta.to.CuentaOwnerTO;

public class RemitoEntradaMobileTO implements Serializable {

	private static final long serialVersionUID = 5262895614447579804L;

	private CuentaOwnerTO owner;
	private String nroRemito;
	private Integer idDocumento;
	private Integer idTipoDocumento;
	private String fecha;
	private String pesoTotal;
	private String anchoCrudo;
	private String anchoFinal;
	private String totalMetros;
	private String productos;
	private String tarima;
	private Boolean enPalet;
	private List<PiezaRemitoMobileTO> piezas;

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

	public Integer getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(Integer idDocumento) {
		this.idDocumento = idDocumento;
	}

	public Integer getIdTipoDocumento() {
		return idTipoDocumento;
	}

	public void setIdTipoDocumento(Integer idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
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

	public String getAnchoCrudo() {
		return anchoCrudo;
	}

	public void setAnchoCrudo(String anchoCrudo) {
		this.anchoCrudo = anchoCrudo;
	}

	public String getAnchoFinal() {
		return anchoFinal;
	}

	public void setAnchoFinal(String anchoFinal) {
		this.anchoFinal = anchoFinal;
	}

	public String getProductos() {
		return productos;
	}

	public void setProductos(String productos) {
		this.productos = productos;
	}

	public String getTarima() {
		return tarima;
	}

	public void setTarima(String tarima) {
		this.tarima = tarima;
	}

	public Boolean getEnPalet() {
		return enPalet;
	}

	public void setEnPalet(Boolean enPalet) {
		this.enPalet = enPalet;
	}

	public String getTotalMetros() {
		return totalMetros;
	}

	public void setTotalMetros(String totalMetros) {
		this.totalMetros = totalMetros;
	}

	public List<PiezaRemitoMobileTO> getPiezas() {
		return piezas;
	}

	public void setPiezas(List<PiezaRemitoMobileTO> piezas) {
		this.piezas = piezas;
	}
}

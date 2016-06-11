package ar.com.textillevel.modulos.odt.to.intercambio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;

public class PiezaRemitoTO implements Serializable {

	private static final long serialVersionUID = -5227026270655000463L;

	private Integer id;
	private Integer ordenPieza;
	private BigDecimal metros;
	private String observaciones;
	private PiezaRemitoTO piezaEntrada;
	private List<PiezaODTTO> piezasPadreODT;
	private Boolean piezaSinODT;
	private Boolean enSalida;
	private Integer idPmpDescuentoStock; // PrecioMateriaPrima esta federada
	private String ordenPiezaCalculado;

	public PiezaRemitoTO() {

	}

	public PiezaRemitoTO(PiezaRemito pr) {
		this.id = pr.getId();
		this.ordenPieza = pr.getOrdenPieza();
		this.metros = pr.getMetros();
		this.observaciones = pr.getObservaciones();
		this.piezaSinODT = pr.getPiezaSinODT();
		this.enSalida = pr.getEnSalida();
		this.ordenPiezaCalculado = pr.getOrdenPiezaCalculado();
		if (pr.getPmpDescuentoStock() != null) {
			this.idPmpDescuentoStock = pr.getPmpDescuentoStock().getId();
		}
		if (pr.getPiezaEntrada() != null){
			this.piezaEntrada = new PiezaRemitoTO(pr.getPiezaEntrada());
		}
		if (pr.getPiezasPadreODT() != null && !pr.getPiezasPadreODT().isEmpty()) {
			this.piezasPadreODT = new ArrayList<PiezaODTTO>();
			for (PiezaODT po : pr.getPiezasPadreODT()) {
				this.piezasPadreODT.add(new PiezaODTTO(po));
			}
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrdenPieza() {
		return ordenPieza;
	}

	public void setOrdenPieza(Integer ordenPieza) {
		this.ordenPieza = ordenPieza;
	}

	public BigDecimal getMetros() {
		return metros;
	}

	public void setMetros(BigDecimal metros) {
		this.metros = metros;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public PiezaRemitoTO getPiezaEntrada() {
		return piezaEntrada;
	}

	public void setPiezaEntrada(PiezaRemitoTO piezaEntrada) {
		this.piezaEntrada = piezaEntrada;
	}

	public List<PiezaODTTO> getPiezasPadreODT() {
		return piezasPadreODT;
	}

	public void setPiezasPadreODT(List<PiezaODTTO> piezasPadreODT) {
		this.piezasPadreODT = piezasPadreODT;
	}

	public Boolean getPiezaSinODT() {
		return piezaSinODT;
	}

	public void setPiezaSinODT(Boolean piezaSinODT) {
		this.piezaSinODT = piezaSinODT;
	}

	public Boolean getEnSalida() {
		return enSalida;
	}

	public void setEnSalida(Boolean enSalida) {
		this.enSalida = enSalida;
	}

	public Integer getIdPmpDescuentoStock() {
		return idPmpDescuentoStock;
	}

	public void setIdPmpDescuentoStock(Integer idPmpDescuentoStock) {
		this.idPmpDescuentoStock = idPmpDescuentoStock;
	}

	public String getOrdenPiezaCalculado() {
		return ordenPiezaCalculado;
	}

	public void setOrdenPiezaCalculado(String ordenPiezaCalculado) {
		this.ordenPiezaCalculado = ordenPiezaCalculado;
	}
}

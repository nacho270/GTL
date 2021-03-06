package ar.com.textillevel.modulos.odt.to.intercambio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;

public class PiezaODTTO implements Serializable {

	private static final long serialVersionUID = 6354127632575678745L;

	private Integer id;
	private Integer orden;
	private PiezaRemitoTO piezaRemito;
	private List<PiezaRemitoTO> piezasSalida;
	private String codigoOdt;
	private Integer nroPiezaStockInicial;
	private BigDecimal metrosStockInicial;
	private BigDecimal metros;
	private Integer ordenSubpieza;
	private Boolean esDeSegunda;

	public PiezaODTTO() {

	}

	public PiezaODTTO(PiezaODT po) {
		this.id = po.getId();
		this.piezaRemito = new PiezaRemitoTO(po.getPiezaRemito());
		this.nroPiezaStockInicial = po.getNroPiezaStockInicial();
		this.metrosStockInicial = po.getMetrosStockInicial();
		this.metros = po.getMetros();
		this.ordenSubpieza = po.getOrdenSubpieza();
		this.orden = po.getOrden();
		this.esDeSegunda = po.getEsDeSegunda();
		if (po.getPiezasSalida() != null && !po.getPiezasSalida().isEmpty()) {
			this.piezasSalida = new ArrayList<PiezaRemitoTO>();
			for (PiezaRemito pr : po.getPiezasSalida()) {
				this.piezasSalida.add(new PiezaRemitoTO(pr));
			}
		}
		if (po.getOdt() != null) {
			this.codigoOdt = po.getOdt().getCodigo();
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PiezaRemitoTO getPiezaRemito() {
		return piezaRemito;
	}

	public void setPiezaRemito(PiezaRemitoTO piezaRemito) {
		this.piezaRemito = piezaRemito;
	}

	public List<PiezaRemitoTO> getPiezasSalida() {
		return piezasSalida;
	}

	public void setPiezasSalida(List<PiezaRemitoTO> piezasSalida) {
		this.piezasSalida = piezasSalida;
	}

//	public ODTEagerTO getOdt() {
//		return odt;
//	}
//
//	public void setOdt(ODTEagerTO odt) {
//		this.odt = odt;
//	}

	public Integer getNroPiezaStockInicial() {
		return nroPiezaStockInicial;
	}

	public void setNroPiezaStockInicial(Integer nroPiezaStockInicial) {
		this.nroPiezaStockInicial = nroPiezaStockInicial;
	}

	public BigDecimal getMetrosStockInicial() {
		return metrosStockInicial;
	}

	public void setMetrosStockInicial(BigDecimal metrosStockInicial) {
		this.metrosStockInicial = metrosStockInicial;
	}

	public String getCodigoOdt() {
		return codigoOdt;
	}

	public void setCodigoOdt(String codigoOdt) {
		this.codigoOdt = codigoOdt;
	}

	public BigDecimal getMetros() {
		return metros;
	}

	public void setMetros(BigDecimal metros) {
		this.metros = metros;
	}

	public Integer getOrdenSubpieza() {
		return ordenSubpieza;
	}

	public void setOrdenSubpieza(Integer ordenSubpieza) {
		this.ordenSubpieza = ordenSubpieza;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public Boolean getEsDeSegunda() {
		return esDeSegunda;
	}

	public void setEsDeSegunda(Boolean esDeSegunda) {
		this.esDeSegunda = esDeSegunda;
	}

}

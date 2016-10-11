package ar.com.textillevel.modulos.odt.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;

@Entity
@Table(name = "T_PIEZA_ODT")
public class PiezaODT implements Serializable {

	private static final long serialVersionUID = 3651496545967580809L;

	private Integer id;
	private Integer orden;
	private PiezaRemito piezaRemito; //Pieza de remito de entrada asociada
	private List<PiezaRemito> piezasSalida; //Piezas de remito de salida
	private OrdenDeTrabajo odt;
	private Integer nroPiezaStockInicial; //Es solo relevante para las piezas que se generan a partir del descuento del stock inicial
	private BigDecimal metrosStockInicial; //Es solo relevante para las piezas que se generan a partir del descuento del stock inicial. 
										  //son los metros que se descontaron del stock inicial. En el caso normal se toman de la piezaRemito de entrada.
	private BigDecimal metros;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "P_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_ORDEN")
	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	@ManyToOne
	@JoinColumn(name="F_PIEZA_REMITO_P_ID", nullable=true)
	public PiezaRemito getPiezaRemito() {
		return piezaRemito;
	}

	public void setPiezaRemito(PiezaRemito piezaRemito) {
		this.piezaRemito = piezaRemito;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_ODT_P_ID", nullable=false, insertable=false,updatable=false)
	public OrdenDeTrabajo getOdt() {
		return odt;
	}

	public void setOdt(OrdenDeTrabajo odt) {
		this.odt = odt;
	}

	@ManyToMany(mappedBy="piezasPadreODT")
	public List<PiezaRemito> getPiezasSalida() {
		return piezasSalida;
	}

	public void setPiezasSalida(List<PiezaRemito> piezasSalida) {
		this.piezasSalida = piezasSalida;
	}

	@Column(name="A_NRO_PIEZA_STOCK_INI", nullable=true)
	public Integer getNroPiezaStockInicial() {
		return nroPiezaStockInicial;
	}

	public void setNroPiezaStockInicial(Integer nroPiezaStockInicial) {
		this.nroPiezaStockInicial = nroPiezaStockInicial;
	}

	@Column(name="A_METROS_STOCK_INI", nullable=true)
	public BigDecimal getMetrosStockInicial() {
		return metrosStockInicial;
	}

	public void setMetrosStockInicial(BigDecimal metrosStockInicial) {
		this.metrosStockInicial = metrosStockInicial;
	}

	@Column(name="A_METROS", nullable=true)
	public BigDecimal getMetros() {
		return metros;
	}

	public void setMetros(BigDecimal metros) {
		this.metros = metros;
	}

}
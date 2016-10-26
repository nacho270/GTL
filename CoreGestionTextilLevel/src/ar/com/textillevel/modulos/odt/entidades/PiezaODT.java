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
import javax.persistence.Transient;

import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;

@Entity
@Table(name = "T_PIEZA_ODT")
public class PiezaODT implements Serializable, Comparable<PiezaODT> {

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
	private Integer ordenSubpieza;

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

	@Column(name="A_ORDEN_SUBPIEZA", nullable=true)
	public Integer getOrdenSubpieza() {
		return ordenSubpieza;
	}

	public void setOrdenSubpieza(Integer ordenSubpieza) {
		this.ordenSubpieza = ordenSubpieza;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((metros == null) ? 0 : metros.hashCode());
		result = prime * result
				+ ((ordenSubpieza == null) ? 0 : ordenSubpieza.hashCode());
		result = prime * result
				+ ((piezaRemito == null) ? 0 : piezaRemito.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PiezaODT other = (PiezaODT) obj;
		if (metros == null) {
			if (other.metros != null)
				return false;
		} else if (!metros.equals(other.metros))
			return false;
		if (ordenSubpieza == null) {
			if (other.ordenSubpieza != null)
				return false;
		} else if (!ordenSubpieza.equals(other.ordenSubpieza))
			return false;
		if (piezaRemito == null) {
			if (other.piezaRemito != null)
				return false;
		} else if (!piezaRemito.equals(other.piezaRemito))
			return false;
		return true;
	}

	@Override
	public int compareTo(PiezaODT o) {
		if(this.getOrden() == null || o.getOrden() == null) {
			return 0;
		} else if(this.getOrden().compareTo(o.getOrden()) != 0) {
			return this.getOrden().compareTo(o.getOrden());
		} else if(this.getOrdenSubpieza() != null && o.getOrdenSubpieza()!=null){
			return this.getOrdenSubpieza().compareTo(o.getOrdenSubpieza());
		}
		return 0;
	}

	@Transient
	public boolean tieneSalida() {
		return getPiezasSalida() != null && !getPiezasSalida().isEmpty();
	}

}

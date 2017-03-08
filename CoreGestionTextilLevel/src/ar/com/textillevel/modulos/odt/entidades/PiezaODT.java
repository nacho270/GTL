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

import ar.com.fwcommon.util.StringUtil;
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
	private Boolean esDeSegunda;


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

	@Column(name="A_ES_DE_SEGUNDA", nullable=true)
	public Boolean getEsDeSegunda() {
		return esDeSegunda;
	}

	public void setEsDeSegunda(Boolean esDeSegunda) {
		this.esDeSegunda = esDeSegunda;
	}

	@Override
	public String toString() {
		return getOrden() + (getOrdenSubpieza() == null  ? "" : " / " + getOrdenSubpieza());
	}

	/**
	 * Calcula el código de barras para una pieza. 
	 * Si NO es una subpieza => el cod de barras es [COD_ODT] + [ORDEN_PIEZA] + 00
	 * Si es una subpieza => el cod barras es  [COD_ODT] + [ORDEN_PIEZA] + [ORDEN_SUBPIEZA]
	 * @param codODT
	 */
	public String getCodigoBarras(String codODT) {
		return codODT + StringUtil.fillLeftWithZeros(getOrden()+"", 2) + (getOrdenSubpieza() == null ? "00" : StringUtil.fillLeftWithZeros(getOrdenSubpieza()+"", 2));
	}


	@Override
	public int compareTo(PiezaODT o) {
		if(this.getOrden() == null && o.getOrden() == null) {
			return 0;
		} else if(this.getOrden() == null && o.getOrden() != null) {
			return -1;
		} else if(this.getOrden() != null && o.getOrden() == null) {
			return 1;
		} else if(this.getOrden().compareTo(o.getOrden()) == 0) {
			if(this.getOrdenSubpieza() == null && o.getOrdenSubpieza()==null) {
				return 0;
			} else if(this.getOrdenSubpieza() == null && o.getOrdenSubpieza()!=null) {
				return -1;
			} else if(this.getOrdenSubpieza() != null && o.getOrdenSubpieza()==null) {
				return 1;
			} else {
				return this.getOrdenSubpieza().compareTo(o.getOrdenSubpieza());
			}
		} else {
			return this.getOrden().compareTo(o.getOrden());
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((orden == null) ? 0 : orden.hashCode());
		result = prime * result + ((ordenSubpieza == null) ? 0 : ordenSubpieza.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (orden == null) {
			if (other.orden != null)
				return false;
		} else if (!orden.equals(other.orden))
			return false;
		if (ordenSubpieza == null) {
			if (other.ordenSubpieza != null)
				return false;
		} else if (!ordenSubpieza.equals(other.ordenSubpieza))
			return false;
		return true;
	}
	
	@Transient
	public boolean tieneSalida() {
		return getPiezasSalida() != null && !getPiezasSalida().isEmpty();
	}

	public static void main(String[] args) {
		int totalPiezas = 8;
		String infoPieza = "503";
		Integer nroPieza=null, nroSubPieza=null;
		
		String patternSubPieza = StringUtil.fillLeftWithZeros("", Double.valueOf(Math.ceil(Math.log10(totalPiezas))).intValue());
		int indexPattern = infoPieza.lastIndexOf(patternSubPieza);
		if(indexPattern == -1) {//no hay subpieza => todo el string es un número de pieza
			nroPieza = Integer.valueOf(infoPieza);
		} else {//existe subpieza! => quito los ceros separadores y separo en pieza/subpieza
			nroPieza = Integer.valueOf(infoPieza.substring(0, indexPattern));
			nroSubPieza = Integer.valueOf(infoPieza.substring(indexPattern, infoPieza.length()));
		}

		System.out.println(nroPieza);
		System.out.println(nroSubPieza);
	}

}

package ar.com.textillevel.entidades.documentos.remito;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;

@Entity
@Table(name="T_PIEZA_REMITO")
public class PiezaRemito implements Serializable {

	private static final long serialVersionUID = 296410844192013760L;

	private Integer id;
	private Integer ordenPieza;
	private BigDecimal metros;
	private String observaciones;
	private PiezaRemito piezaEntrada; //Usado para modelar subpiezas cuando es de R.E sino cuando es de R.S aloja la pieza original del R.E.
	private List<PiezaODT> piezasPadreODT; //Usado para modelar piezas combinadas
	private Boolean piezaSinODT; //Solo para piezas de remito de entrada, si es true significa que es una pieza que aún no tiene ODT
	private Boolean enSalida;	//Tiene sentido para una pieza de entrada. Si está en true significa que ya se le dio salida
	private PrecioMateriaPrima pmpDescuentoStock; //es la pieza materia prima desde donde se descuenta stock. Solo tiene sentido para las piezas de R.S. de descuento de stock
	private String ordenPiezaCalculado;

	public PiezaRemito() {
		this.piezasPadreODT = new ArrayList<PiezaODT>();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "P_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="A_ORDEN_PIEZA", nullable=false)
	public Integer getOrdenPieza() {
		return ordenPieza;
	}

	public void setOrdenPieza(Integer ordenPieza) {
		this.ordenPieza = ordenPieza;
	}

	@Column(name="A_METROS", nullable=false)
	public BigDecimal getMetros() {
		return metros;
	}

	public void setMetros(BigDecimal metros) {
		this.metros = metros;
	}

	@Column(name="A_OBSERVACIONES", nullable=true)
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	@ManyToOne
	@JoinColumn(name="F_PIEZA_PADRE_P_ID")
	public PiezaRemito getPiezaEntrada() {
		return piezaEntrada;
	}

	public void setPiezaEntrada(PiezaRemito piezaEntrada) {
		this.piezaEntrada = piezaEntrada;
	}

	@ManyToMany
	@JoinTable(name = "T_PIEZA_REM_SAL_PIEZA_ODT", 
			joinColumns = { @JoinColumn(name = "F_PIEZA_REM_SAL_P_ID") }, 
			inverseJoinColumns = { @JoinColumn(name = "F_PIEZA_ODT_P_ID") })
	public List<PiezaODT> getPiezasPadreODT() {
		return piezasPadreODT;
	}

	public void setPiezasPadreODT(List<PiezaODT> piezasPadrePODT) {
		this.piezasPadreODT = piezasPadrePODT;
	}

	@Column(name="A_PIEZA_SIN_ODT", nullable=true)
	public Boolean getPiezaSinODT() {
		return piezaSinODT;
	}

	public void setPiezaSinODT(Boolean piezaSinODT) {
		this.piezaSinODT = piezaSinODT;
	}

	@Column(name="A_EN_SALIDA", nullable=true)
	public Boolean getEnSalida() {
		return enSalida;
	}

	public void setEnSalida(Boolean enSalida) {
		this.enSalida = enSalida;
	}

	@ManyToOne
	@JoinColumn(name = "F_PMP_DESC_STOCK_P_ID")
	public PrecioMateriaPrima getPmpDescuentoStock() {
		return pmpDescuentoStock;
	}

	public void setPmpDescuentoStock(PrecioMateriaPrima pmpDescuentoStock) {
		this.pmpDescuentoStock = pmpDescuentoStock;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		final PiezaRemito other = (PiezaRemito) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id)) {
			return false;
		} else {
			return true;
		}
		return false;
	}

	@Transient
	public BigDecimal getTotalMetros() {
		return (getMetros() == null) ? new BigDecimal(0) : getMetros();
	}

	@Transient
	public String toString() {
		return "Nro: " + getOrdenPieza() + " - Metros : " + getTotalMetros(); 
	}

	@Transient
	public String getOrdenPiezaCalculado() {
		return ordenPiezaCalculado;
	}

	public void setOrdenPiezaCalculado(String ordenPiezaCalculado) {
		this.ordenPiezaCalculado = ordenPiezaCalculado;
	}

	@Transient
	public PiezaODT getPiezaPadreODT() {
		if(!getPiezasPadreODT().isEmpty()) {
			return getPiezasPadreODT().get(0);
		}
		return null;
	}

}
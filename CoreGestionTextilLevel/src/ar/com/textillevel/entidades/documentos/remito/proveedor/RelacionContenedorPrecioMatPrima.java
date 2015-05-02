package ar.com.textillevel.entidades.documentos.remito.proveedor;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;

@Entity
@Table(name = "T_REL_CONT_PREC_MAT_PRIMA")
public class RelacionContenedorPrecioMatPrima implements Serializable {

	private static final long serialVersionUID = 4538662324839786831L;

	private Integer id;
	private ContenedorMateriaPrima contenedor;
	private PrecioMateriaPrima precioMateriaPrima;
	private BigDecimal stockActual;
	private Integer version;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "F_CONT_MAT_PRIMA_P_ID")
	@Fetch(FetchMode.JOIN)
	public ContenedorMateriaPrima getContenedor() {
		return contenedor;
	}

	public void setContenedor(ContenedorMateriaPrima contenedor) {
		this.contenedor = contenedor;
	}

	@ManyToOne
	@JoinColumn(name = "F_PRECIO_MAT_PRIMA_P_ID")
	@Fetch(FetchMode.JOIN)
	public PrecioMateriaPrima getPrecioMateriaPrima() {
		return precioMateriaPrima;
	}

	public void setPrecioMateriaPrima(PrecioMateriaPrima precioMateriaPrima) {
		this.precioMateriaPrima = precioMateriaPrima;
	}

	@Column(name="A_STOCK_ACTUAL", nullable=false)
	public BigDecimal getStockActual() {
		return stockActual;
	}

	public void setStockActual(BigDecimal stockActual) {
		this.stockActual = stockActual;
	}

	@Version
	@Column(name="A_VERSION")
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
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
		final RelacionContenedorPrecioMatPrima other = (RelacionContenedorPrecioMatPrima) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Transient
	public String getKey() {
		return getPrecioMateriaPrima().getId() + " - " + getContenedor().getId();
	}

}
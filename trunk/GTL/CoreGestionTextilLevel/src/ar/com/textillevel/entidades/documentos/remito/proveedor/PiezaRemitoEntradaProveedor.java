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

import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;

@Entity
@Table(name = "T_PIEZA_REMITO_ENTRADA_PROV")
public class PiezaRemitoEntradaProveedor implements Serializable {

	private static final long serialVersionUID = -4523098666602633960L;

	private Integer id;
	private BigDecimal cantidad;
	private PrecioMateriaPrima precioMateriaPrima;
	private BigDecimal cantContenedor;
	private RelacionContenedorPrecioMatPrima relContenedorPrecioMatPrima;

	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="A_CANTIDAD", nullable=false)
	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	@ManyToOne
	@JoinColumn(name="F_PRECIO_MP_P_ID")
	public PrecioMateriaPrima getPrecioMateriaPrima() {
		return precioMateriaPrima;
	}

	public void setPrecioMateriaPrima(PrecioMateriaPrima precioMateriaPrima) {
		this.precioMateriaPrima = precioMateriaPrima;
	}

	@Column(name="A_CANT_CONTENEDOR", nullable=false)
	public BigDecimal getCantContenedor() {
		return cantContenedor;
	}

	public void setCantContenedor(BigDecimal cantContenedor) {
		this.cantContenedor = cantContenedor;
	}

	@ManyToOne
	@JoinColumn(name="F_REL_CONT_MP_P_ID")
	public RelacionContenedorPrecioMatPrima getRelContenedorPrecioMatPrima() {
		return relContenedorPrecioMatPrima;
	}

	public void setRelContenedorPrecioMatPrima(RelacionContenedorPrecioMatPrima relContenedorPrecioMatPrima) {
		this.relContenedorPrecioMatPrima = relContenedorPrecioMatPrima;
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
		PiezaRemitoEntradaProveedor other = (PiezaRemitoEntradaProveedor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}

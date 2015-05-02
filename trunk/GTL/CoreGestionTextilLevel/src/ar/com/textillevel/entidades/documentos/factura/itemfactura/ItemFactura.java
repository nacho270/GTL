package ar.com.textillevel.entidades.documentos.factura.itemfactura;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoItemFactura;
import ar.com.textillevel.entidades.enums.EUnidad;

@Entity
@Table(name = "T_ITEM_FACTURA")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "IT")
public abstract class ItemFactura implements Serializable{

	private static final long serialVersionUID = 4898892434065986891L;

	private Integer id;
	private BigDecimal cantidad;
	private Integer idTipoUnidad;
	private BigDecimal importe;
	private String descripcion;
	private BigDecimal precioUnitario;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_CANITDAD", nullable = false)
	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	@Column(name = "A_UNIDAD", nullable = false)
	private Integer getIdTipoUnidad() {
		return idTipoUnidad;
	}

	private void setIdTipoUnidad(Integer idTipoUnidad) {
		this.idTipoUnidad = idTipoUnidad;
	}

	public void setUnidad(EUnidad unidad) {
		if (unidad == null) {
			this.setIdTipoUnidad(null);
		}
		setIdTipoUnidad(unidad.getId());
	}

	@Transient
	public EUnidad getUnidad() {
		if (getIdTipoUnidad() == null) {
			return null;
		}
		return EUnidad.getById(getIdTipoUnidad());
	}

	@Column(name = "A_IMPORTE", nullable = false)
	public BigDecimal getImporte() {
		return this.importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	@Column(name = "A_DESCRIPCION", nullable = false)
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "A_PRECIO_UNITARIO")
	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	@Transient
	public abstract ETipoItemFactura getTipo();

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
		final ItemFactura other = (ItemFactura) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return false;
	}
}

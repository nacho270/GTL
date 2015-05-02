package ar.com.textillevel.entidades.documentos.remito.proveedor;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.documentos.remito.proveedor.visitor.IItemRemitoSalidaVisitor;

@Entity
@Table(name = "T_ITEM_REMITO_SALIDA_PROV")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="TIPO",discriminatorType=DiscriminatorType.STRING)
public abstract class ItemRemitoSalidaProveedor implements Serializable {

	private static final long serialVersionUID = -4948423851061587265L;

	private Integer id;
	private BigDecimal cantSalida;

	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="A_CANT_SALIDA", nullable=false)
	public BigDecimal getCantSalida() {
		return cantSalida;
	}

	public void setCantSalida(BigDecimal cantSalida) {
		this.cantSalida = cantSalida;
	}

	@Transient
	public abstract BigDecimal getStockActual();

	@Transient
	public abstract void accept(IItemRemitoSalidaVisitor visitor);

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
		final ItemRemitoSalidaProveedor other = (ItemRemitoSalidaProveedor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
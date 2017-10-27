package ar.com.textillevel.entidades.documentos.remito;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;

@Entity
@Table(name="T_ITEM_DIBUJO_REMITO_ENTRADA")
public class ItemDibujoRemitoEntrada implements Serializable {

	private static final long serialVersionUID = -2001206447726516665L;

	private Integer id;
	private DibujoEstampado dibujo;
	private BigDecimal precio;
	
	public ItemDibujoRemitoEntrada() {
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

	@ManyToOne(cascade=CascadeType.MERGE)
	@JoinColumn(name="F_DIBUJO_P_ID", nullable=false)
	public DibujoEstampado getDibujo() {
		return dibujo;
	}

	public void setDibujo(DibujoEstampado dibujo) {
		this.dibujo = dibujo;
	}

	@Column(name="A_PRECIO", nullable=true)
	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		res.append(getDibujo().toString()).append(getPrecio() == null ? " " : (" PRECIO: " + getPrecio().doubleValue()));
		return res.toString();
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
		ItemDibujoRemitoEntrada other = (ItemDibujoRemitoEntrada) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
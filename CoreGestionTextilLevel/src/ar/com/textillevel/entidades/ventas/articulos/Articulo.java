package ar.com.textillevel.entidades.ventas.articulos;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "T_ARTICULO")
public class Articulo implements Serializable {

	private static final long serialVersionUID = -4640054858121248516L;

	private Integer id;
	private String nombre;
	private String descripcion;
	private BigDecimal gramaje;
	private BigDecimal ancho;
	private TipoArticulo tipoArticulo;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_NOMBRE", nullable = false)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "A_DESC", nullable = false)
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name="A_GRAMAJE")
	public BigDecimal getGramaje() {
		return gramaje;
	}

	public void setGramaje(BigDecimal gramaje) {
		this.gramaje = gramaje;
	}

	@Column(name="A_ANCHO")
	public BigDecimal getAncho() {
		return ancho;
	}

	public void setAncho(BigDecimal ancho) {
		this.ancho = ancho;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="F_TIPO_ARTICULO_P_ID",insertable=false, updatable=false)
	@Fetch(FetchMode.JOIN)
	public TipoArticulo getTipoArticulo() {
		return tipoArticulo;
	}

	public void setTipoArticulo(TipoArticulo tipoArticulo) {
		this.tipoArticulo = tipoArticulo;
	}
	
	@Override
	@Transient
	public String toString() {
		return nombre;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
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
		Articulo other = (Articulo) obj;
		if (descripcion == null) {
			if (other.descripcion != null)
				return false;
		} else if (!descripcion.equals(other.descripcion))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}
}

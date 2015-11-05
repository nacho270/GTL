package ar.com.textillevel.entidades.ventas.cotizacion;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;

@MappedSuperclass
public class GrupoTipoArticulo implements Serializable {

	private static final long serialVersionUID = 2918848319937718773L;

	private Integer id;
	private TipoArticulo tipoArticulo;
	private Articulo articulo;
	
	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name = "F_TIPO_ARTICULO_P_ID", nullable = false)
	public TipoArticulo getTipoArticulo() {
		return tipoArticulo;
	}

	public void setTipoArticulo(TipoArticulo tipoArticulo) {
		this.tipoArticulo = tipoArticulo;
	}

	@ManyToOne
	@JoinColumn(name = "F_ARTICULO_P_ID", nullable = true)
	public Articulo getArticulo() {
		return articulo;
	}

	public void setArticulo(Articulo articulo) {
		this.articulo = articulo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((tipoArticulo == null) ? 0 : tipoArticulo.hashCode());
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
		GrupoTipoArticulo other = (GrupoTipoArticulo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (tipoArticulo == null) {
			if (other.tipoArticulo != null)
				return false;
		} else if (!tipoArticulo.equals(other.tipoArticulo))
			return false;
		return true;
	}

}
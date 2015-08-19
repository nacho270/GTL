package ar.com.textillevel.entidades.ventas.cotizacion;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "T_RANGO_COBERTURA_ESTAMPADO")
public class RangoCoberturaEstampado implements Serializable {

	private static final long serialVersionUID = 4223229114159062052L;

	private Integer id;
	private Integer minimo;
	private Integer maximo;
	private Float precio;
	private RangoCantidadColores rangoCantidadColores;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_MINIMO", nullable = false)
	public Integer getMinimo() {
		return minimo;
	}

	public void setMinimo(Integer minimo) {
		this.minimo = minimo;
	}

	@Column(name = "A_MAXIMO", nullable = false)
	public Integer getMaximo() {
		return maximo;
	}

	public void setMaximo(Integer maximo) {
		this.maximo = maximo;
	}

	@Column(name = "A_PRECIO", nullable = false)
	public Float getPrecio() {
		return precio;
	}

	public void setPrecio(Float precio) {
		this.precio = precio;
	}

	@ManyToOne
	@JoinColumn(name = "F_RANGO_CANT_COLORES_P_ID", updatable=false, insertable=false, nullable=false)
	public RangoCantidadColores getRangoCantidadColores() {
		return rangoCantidadColores;
	}

	public void setRangoCantidadColores(RangoCantidadColores rangoCantidadColores) {
		this.rangoCantidadColores = rangoCantidadColores;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((maximo == null) ? 0 : maximo.hashCode());
		result = prime * result + ((minimo == null) ? 0 : minimo.hashCode());
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
		RangoCoberturaEstampado other = (RangoCoberturaEstampado) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (maximo == null) {
			if (other.maximo != null)
				return false;
		} else if (!maximo.equals(other.maximo))
			return false;
		if (minimo == null) {
			if (other.minimo != null)
				return false;
		} else if (!minimo.equals(other.minimo))
			return false;
		return true;
	}

	@Transient
	public void deepRemove() {
		//nivel 0
		RangoCantidadColores rangoCantidadColores = this.getRangoCantidadColores();
		rangoCantidadColores.getRangos().remove(this);
		//nivel 1
		if(rangoCantidadColores.getRangos().isEmpty()) {
			PrecioBaseEstampado precioBase = rangoCantidadColores.getPrecioBase();
			precioBase.getRangosDeColores().remove(rangoCantidadColores);
			//nivel 2
			if(precioBase.getRangosDeColores().isEmpty()) {
				GrupoTipoArticuloBaseEstampado grupoTipoArticuloBase = precioBase.getGrupoTipoArticuloBase();
				grupoTipoArticuloBase.getPrecios().remove(precioBase);
				//nivel 3
				if(grupoTipoArticuloBase.getPrecios().isEmpty()) {
					RangoAnchoArticuloEstampado rangoAnchoArticulo = grupoTipoArticuloBase.getRangoAnchoArticulo(); 
					rangoAnchoArticulo.getGruposBase().remove(grupoTipoArticuloBase);
					//nivel 4
					if(rangoAnchoArticulo.getGruposBase().isEmpty()) {
						DefinicionPrecio definicionPrecio = rangoAnchoArticulo.getDefinicionPrecio();
						definicionPrecio.getRangos().remove(rangoAnchoArticulo);
					}
				}
			}
		}
	}

}
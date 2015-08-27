package ar.com.textillevel.entidades.ventas.articulos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "T_TIPO_ARTICULO")
public class TipoArticulo implements Serializable, Comparable<TipoArticulo> {

	private static final long serialVersionUID = 3675012123576376920L;

	private Integer id;
	private String nombre;
	private String sigla;
	private List<Articulo> articulos;
	private List<TipoArticulo> tiposArticuloComponentes;
	
	public TipoArticulo() {
		this.articulos = new ArrayList<Articulo>();
		this.tiposArticuloComponentes = new ArrayList<TipoArticulo>(); 
	}

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

	@Column(name = "A_SIGLA", nullable = false)
	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="F_TIPO_ARTICULO_P_ID", nullable=false)
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<Articulo> getArticulos() {
		return articulos;
	}

	public void setArticulos(List<Articulo> articulos) {
		this.articulos = articulos;
	}

	@ManyToMany
	@JoinTable(name = "T_TIPO_ART_TIPO_ART_ASOC", 
			joinColumns = { @JoinColumn(name = "P_ID") }, 
			inverseJoinColumns = { @JoinColumn(name = "F_TIPO_ARTICULO_PADRE_P_ID") })
	public List<TipoArticulo> getTiposArticuloComponentes() {
		return tiposArticuloComponentes;
	}

	public void setTiposArticuloComponentes(List<TipoArticulo> tiposArticuloComponentes) {
		this.tiposArticuloComponentes = tiposArticuloComponentes;
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
		result = prime * result + ((sigla == null) ? 0 : sigla.hashCode());
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
		TipoArticulo other = (TipoArticulo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (sigla == null) {
			if (other.sigla != null)
				return false;
		} else if (!sigla.equals(other.sigla))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}

	@Transient
	public boolean isCompuesto() {
		return getTiposArticuloComponentes()!=null && !getTiposArticuloComponentes().isEmpty();
	}

	@Transient
	public int compareTo(TipoArticulo o) {
		return getNombre().compareToIgnoreCase(o.getNombre());
	}

}
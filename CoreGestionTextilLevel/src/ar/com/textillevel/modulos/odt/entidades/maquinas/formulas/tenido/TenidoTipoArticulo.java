package ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.ReactivoCantidad;

@Entity
@Table(name = "T_TENIDO_TIPO_ARTICULO")
public class TenidoTipoArticulo implements Serializable {

	private static final long serialVersionUID = -8325840691284551609L;

	private Integer id;
	private TipoArticulo tipoArticulo;
	private List<AnilinaCantidad> anilinasCantidad;
	private List<ReactivoCantidad> reactivosCantidad;

	public TenidoTipoArticulo() {
		this.anilinasCantidad = new ArrayList<AnilinaCantidad>();
		this.reactivosCantidad = new ArrayList<ReactivoCantidad>();
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

	@ManyToOne
	@JoinColumn(name="F_TIPO_ARTICULO_P_ID", nullable=false)
	@Fetch(FetchMode.JOIN)
	public TipoArticulo getTipoArticulo() {
		return tipoArticulo;
	}

	public void setTipoArticulo(TipoArticulo tipoArticulo) {
		this.tipoArticulo = tipoArticulo;
	}

	@OneToMany(cascade = { CascadeType.ALL })
	@JoinColumn(name = "F_TENIDO_TIPO_ARTICULO_P_ID")
	@org.hibernate.annotations.Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	public List<AnilinaCantidad> getAnilinasCantidad() {
		return anilinasCantidad;
	}

	public void setAnilinasCantidad(List<AnilinaCantidad> anilinasCantidad) {
		this.anilinasCantidad = anilinasCantidad;
	}

	@OneToMany(cascade = { CascadeType.ALL })
	@JoinColumn(name = "F_TENIDO_TIPO_ARTICULO_P_ID")
	@org.hibernate.annotations.Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	public List<ReactivoCantidad> getReactivosCantidad() {
		return reactivosCantidad;
	}

	public void setReactivosCantidad(List<ReactivoCantidad> reactivosCantidad) {
		this.reactivosCantidad = reactivosCantidad;
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
		TenidoTipoArticulo other = (TenidoTipoArticulo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
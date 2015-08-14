package ar.com.textillevel.entidades.ventas.cotizacion;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;

@Entity
@Table(name = "T_GRUPO_TIPO_ARTICULO_GAMA")
public class GrupoTipoArticuloGama implements Serializable {

	private static final long serialVersionUID = -6971873499189577231L;

	private Integer id;
	private TipoArticulo tipoArticulo;
	private List<PrecioGama> precios;

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
	@JoinColumn(name = "F_TIPO_ARTICULO_P_ID", nullable = false)
	public TipoArticulo getTipoArticulo() {
		return tipoArticulo;
	}

	public void setTipoArticulo(TipoArticulo tipoArticulo) {
		this.tipoArticulo = tipoArticulo;
	}

	@OneToMany(cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
	@JoinColumn(name = "F_GRUPO_P_ID")
	@org.hibernate.annotations.Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public List<PrecioGama> getPrecios() {
		return precios;
	}

	public void setPrecios(List<PrecioGama> precios) {
		this.precios = precios;
	}
}

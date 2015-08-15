package ar.com.textillevel.entidades.ventas.cotizacion;

import java.io.Serializable;
import java.util.ArrayList;
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
@Table(name = "T_GRUPO_TIPO_ARTICULO_BASE")
public class GrupoTipoArticuloBaseEstampado implements Serializable {

	private static final long serialVersionUID = 2124229339045732222L;

	private Integer id;
	private TipoArticulo tipoArticulo;
	private List<PrecioBaseEstampado> precios;

	public GrupoTipoArticuloBaseEstampado() {
		this.precios = new ArrayList<PrecioBaseEstampado>();
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
	public List<PrecioBaseEstampado> getPrecios() {
		return precios;
	}

	public void setPrecios(List<PrecioBaseEstampado> precios) {
		this.precios = precios;
	}

}

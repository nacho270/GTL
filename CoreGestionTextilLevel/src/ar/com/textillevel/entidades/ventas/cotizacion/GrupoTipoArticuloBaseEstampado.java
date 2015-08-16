package ar.com.textillevel.entidades.ventas.cotizacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "T_GRUPO_TIPO_ARTICULO_BASE")
public class GrupoTipoArticuloBaseEstampado extends GrupoTipoArticulo implements Serializable {

	private static final long serialVersionUID = 2124229339045732222L;

	private List<PrecioBaseEstampado> precios;

	public GrupoTipoArticuloBaseEstampado() {
		this.precios = new ArrayList<PrecioBaseEstampado>();
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

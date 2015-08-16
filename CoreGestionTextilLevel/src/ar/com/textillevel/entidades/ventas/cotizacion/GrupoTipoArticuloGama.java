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
@Table(name = "T_GRUPO_TIPO_ARTICULO_GAMA")
public class GrupoTipoArticuloGama extends GrupoTipoArticulo implements Serializable {

	private static final long serialVersionUID = -6971873499189577231L;

	private List<PrecioGama> precios;

	public GrupoTipoArticuloGama() {
		this.precios = new ArrayList<PrecioGama>();
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

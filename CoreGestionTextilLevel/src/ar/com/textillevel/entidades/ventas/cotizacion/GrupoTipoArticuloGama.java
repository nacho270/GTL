package ar.com.textillevel.entidades.ventas.cotizacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.ventas.articulos.GamaColorCliente;

@Entity
@Table(name = "T_GRUPO_TIPO_ARTICULO_GAMA")
public class GrupoTipoArticuloGama extends GrupoTipoArticulo implements Serializable, Comparable<GrupoTipoArticuloGama> {

	private static final long serialVersionUID = -6971873499189577231L;

	private List<PrecioGama> precios;
	private RangoAnchoArticuloTenido rangoAnchoArticuloTenido;

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

	@ManyToOne
	@JoinColumn(name = "F_RANGO_P_ID", updatable=false, insertable=false, nullable=false)
	public RangoAnchoArticuloTenido getRangoAnchoArticuloTenido() {
		return rangoAnchoArticuloTenido;
	}

	public void setRangoAnchoArticuloTenido(RangoAnchoArticuloTenido rangoAnchoArticuloTenido) {
		this.rangoAnchoArticuloTenido = rangoAnchoArticuloTenido;
	}
	
	@Transient
	public PrecioGama getPrecioGama(GamaColorCliente base) {
		for(PrecioGama p : getPrecios()) {
			if(p.getGamaCliente().equals(base)) {
				return p;
			}
		}
		return null;
	}

	@Transient
	public void deepOrderBy() {
		Collections.sort(getPrecios());
	}

	@Transient
	public int compareTo(GrupoTipoArticuloGama o) {
		return getTipoArticulo().compareTo(o.getTipoArticulo());
	}

}
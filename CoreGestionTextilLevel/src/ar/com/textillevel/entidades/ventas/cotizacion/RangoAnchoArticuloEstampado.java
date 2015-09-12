package ar.com.textillevel.entidades.ventas.cotizacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.entidades.ventas.productos.ProductoEstampado;

@Entity
@DiscriminatorValue(value = "RANGOESTAMPADO")
public class RangoAnchoArticuloEstampado extends RangoAncho {

	private static final long serialVersionUID = -1160049675365083402L;
	
	private List<GrupoTipoArticuloBaseEstampado> gruposBase;

	public RangoAnchoArticuloEstampado() {
		this.gruposBase = new ArrayList<GrupoTipoArticuloBaseEstampado>();
	}

	@OneToMany(cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
	@JoinColumn(name = "F_RANGO_P_ID", nullable=false)
	@org.hibernate.annotations.Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public List<GrupoTipoArticuloBaseEstampado> getGruposBase() {
		return gruposBase;
	}

	public void setGruposBase(List<GrupoTipoArticuloBaseEstampado> gruposBase) {
		this.gruposBase = gruposBase;
	}

	@Transient
	public GrupoTipoArticuloBaseEstampado getGrupo(TipoArticulo ta) {
		for(GrupoTipoArticuloBaseEstampado g : getGruposBase()) {
			if(g.getTipoArticulo().equals(ta)) {
				return g;
			}
		}
		return null;
	}

	@Override
	public void deepOrderBy() {
		Collections.sort(gruposBase);
		for(GrupoTipoArticuloBaseEstampado g : getGruposBase()) {
			g.deepOrderBy();
		}
	}

	@Override
	@Transient
	protected Float buscarPrecio(Producto producto) {
		GrupoTipoArticuloBaseEstampado grupo = getGrupo(producto.getArticulo().getTipoArticulo());
		return grupo != null ? grupo.getPrecio((ProductoEstampado) producto) : null;
	}

	@Override
	@Transient
	public boolean estaDefinido(Articulo art) {
		return enRango(art.getAncho().floatValue()) && getGrupo(art.getTipoArticulo()) != null;
	}

	@Override
	@Transient	
	public RangoAncho deepClone(DefinicionPrecio def) {
		RangoAnchoArticuloEstampado rango = new RangoAnchoArticuloEstampado();
		rango.setDefinicionPrecio(def);
		rango.setAnchoExacto(getAnchoExacto());
		rango.setAnchoMaximo(getAnchoMaximo());
		rango.setAnchoMinimo(getAnchoMinimo());
		for(GrupoTipoArticuloBaseEstampado g : getGruposBase()) {
			rango.getGruposBase().add(g.deepClone(rango));
		}
		return rango;
	}

	@Override
	@Transient
	public void aumentarPrecios(float porcentajeAumento) {
		for(GrupoTipoArticuloBaseEstampado gtabe : getGruposBase()) {
			gtabe.aumentarPrecios(porcentajeAumento);
		}
	}

}
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
import ar.com.textillevel.entidades.ventas.productos.ProductoTenido;

@Entity
@DiscriminatorValue(value = "RANGOTENIDO")
public class RangoAnchoArticuloTenido extends RangoAncho {

	private static final long serialVersionUID = -8264351911905316172L;

	private List<GrupoTipoArticuloGama> gruposGama;

	public RangoAnchoArticuloTenido() {
		this.gruposGama = new ArrayList<GrupoTipoArticuloGama>();
	}

	@OneToMany(cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
	@JoinColumn(name = "F_RANGO_P_ID", nullable=false)
	@org.hibernate.annotations.Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public List<GrupoTipoArticuloGama> getGruposGama() {
		return gruposGama;
	}

	public void setGruposGama(List<GrupoTipoArticuloGama> gruposGama) {
		this.gruposGama = gruposGama;
	}

	@Transient
	public GrupoTipoArticuloGama getGrupo(TipoArticulo ta) {
		for(GrupoTipoArticuloGama g : getGruposGama()) {
			if(g.getTipoArticulo().equals(ta)) {
				return g;
			}
		}
		return null;
	}

	@Override
	public void deepOrderBy() {
		Collections.sort(gruposGama);
		for(GrupoTipoArticuloGama g : getGruposGama()) {
			g.deepOrderBy();
		}
	}

	@Override
	@Transient
	protected Float buscarPrecio(Producto producto) {
		GrupoTipoArticuloGama grupo = getGrupo(producto.getArticulo().getTipoArticulo());
		return grupo != null ? grupo.getPrecio((ProductoTenido)producto) : null;
	}

	@Override
	@Transient
	public boolean estaDefinido(Articulo art) {
		return enRango(art.getAncho().floatValue()) && getGrupo(art.getTipoArticulo()) != null;
	}

	@Override
	@Transient
	public RangoAncho deepClone(DefinicionPrecio def) {
		RangoAnchoArticuloTenido rango = new RangoAnchoArticuloTenido();
		rango.setDefinicionPrecio(def);
		rango.setAnchoExacto(getAnchoExacto());
		rango.setAnchoMaximo(getAnchoMaximo());
		rango.setAnchoMinimo(getAnchoMinimo());
		for(GrupoTipoArticuloGama g : getGruposGama()) {
			rango.getGruposGama().add(g.deepClone(rango));
		}
		return rango;
	}

}
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

import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;

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

	@Override
	public void deepOrderBy() {
		Collections.sort(gruposGama);
		for(GrupoTipoArticuloGama g : getGruposGama()) {
			g.deepOrderBy();
		}
	}

	@Override
	@Transient
	protected Float buscarPrecio(ProductoArticulo productoArticulo) {
		Articulo articulo = productoArticulo.getArticulo();
		for(GrupoTipoArticuloGama grupo : getGruposTipoArticulo(articulo.getTipoArticulo(), articulo, GrupoTipoArticuloGama.class)) {
			Float precio = grupo.getPrecio(productoArticulo);
			if(precio != null) {
				return precio;
			}
		}
		return null;
	}

	@Override
	@Transient
	public boolean estaDefinido(Articulo art) {
		return enRango(art.getAncho().floatValue()) && getPrecioArticulo(art.getTipoArticulo(), art, GrupoTipoArticuloGama.class) != null;
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

	@Override
	@Transient
	public void aumentarPrecios(float porcentajeAumento) {
		for(GrupoTipoArticuloGama gtag : getGruposGama()) {
			gtag.aumentarPrecios(porcentajeAumento);
		}
	}

	@Override
	@Transient
	@SuppressWarnings("unchecked")
	protected <G extends GrupoTipoArticulo> List<G> getGruposTipoArticulo(Class<G> clazz) {
		return (List<G>) getGruposGama();
	}

}
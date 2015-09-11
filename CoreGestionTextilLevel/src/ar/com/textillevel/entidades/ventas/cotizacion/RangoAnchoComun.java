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

@Entity
@DiscriminatorValue(value = "RANGOCOMUN")
public class RangoAnchoComun extends RangoAncho {

	private static final long serialVersionUID = -5924812101662593637L;
	
	private List<PrecioTipoArticulo> precios;

	public RangoAnchoComun() {
		this.precios = new ArrayList<PrecioTipoArticulo>();
	}

	@OneToMany(cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
	@JoinColumn(name = "F_RANGO_P_ID", nullable=false)
	@org.hibernate.annotations.Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public List<PrecioTipoArticulo> getPrecios() {
		return precios;
	}

	public void setPrecios(List<PrecioTipoArticulo> precios) {
		this.precios = precios;
	}

	@Transient
	public PrecioTipoArticulo getPrecioArticulo(TipoArticulo ta) {
		for(PrecioTipoArticulo pta : getPrecios()) {
			if(pta.getTipoArticulo().equals(ta)) {
				return pta;
			}
		}
		return null;
	}

	@Override
	public void deepOrderBy() {
		Collections.sort(precios);
	}

	@Override
	@Transient
	protected Float buscarPrecio(Producto producto) {
		PrecioTipoArticulo pta = getPrecioArticulo(producto.getArticulo().getTipoArticulo());
		return pta != null ? pta.getPrecio() : null;
	}

	@Override
	@Transient
	public boolean estaDefinido(Articulo art) {
		return enRango(art.getAncho().floatValue()) && getPrecioArticulo(art.getTipoArticulo()) != null;
	}

	@Override
	@Transient
	public RangoAnchoComun deepClone(DefinicionPrecio def) {
		RangoAnchoComun rango = new RangoAnchoComun();
		rango.setDefinicionPrecio(def);
		rango.setAnchoExacto(getAnchoExacto());
		rango.setAnchoMaximo(getAnchoMaximo());
		rango.setAnchoMinimo(getAnchoMinimo());
		for(PrecioTipoArticulo g : getPrecios()) {
			rango.getPrecios().add(g.deepClone(rango));
		}
		return rango;
	}

}
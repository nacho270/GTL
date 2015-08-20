package ar.com.textillevel.entidades.ventas.cotizacion;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;

@Entity
@DiscriminatorValue(value = "RANGOTENIDO")
public class RangoAnchoArticuloTenido extends RangoAncho{

	private static final long serialVersionUID = -8264351911905316172L;
	
	private List<GrupoTipoArticuloGama> gruposGama;

	public RangoAnchoArticuloTenido() {
		this.gruposGama = new ArrayList<GrupoTipoArticuloGama>();
	}

	@OneToMany(cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
	@JoinColumn(name = "F_RANGO_P_ID")
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
}

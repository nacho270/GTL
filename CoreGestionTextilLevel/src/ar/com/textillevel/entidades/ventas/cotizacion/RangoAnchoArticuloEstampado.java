package ar.com.textillevel.entidades.ventas.cotizacion;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue(value = "RANGOESTAMPADO")
public class RangoAnchoArticuloEstampado extends RangoAncho {

	private static final long serialVersionUID = -1160049675365083402L;
	
	private List<GrupoTipoArticuloBaseEstampado> gruposBase;

	public RangoAnchoArticuloEstampado() {
		this.gruposBase = new ArrayList<GrupoTipoArticuloBaseEstampado>();
	}

	@OneToMany(cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
	@JoinColumn(name = "F_RANGO_P_ID")
	@org.hibernate.annotations.Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public List<GrupoTipoArticuloBaseEstampado> getGruposBase() {
		return gruposBase;
	}

	public void setGruposBase(List<GrupoTipoArticuloBaseEstampado> gruposBase) {
		this.gruposBase = gruposBase;
	}
}

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
@DiscriminatorValue(value = "RANGOCOMUN")
public class RangoAnchoComun extends RangoAncho {

	private static final long serialVersionUID = -5924812101662593637L;
	
	private List<PrecioTipoArticulo> precios;

	public RangoAnchoComun() {
		this.precios = new ArrayList<PrecioTipoArticulo>();
	}

	@OneToMany(cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
	@JoinColumn(name = "F_RANGO_P_ID")
	@org.hibernate.annotations.Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public List<PrecioTipoArticulo> getPrecios() {
		return precios;
	}

	public void setPrecios(List<PrecioTipoArticulo> precios) {
		this.precios = precios;
	}
}
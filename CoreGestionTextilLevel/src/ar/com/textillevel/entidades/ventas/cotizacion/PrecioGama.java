package ar.com.textillevel.entidades.ventas.cotizacion;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.ventas.articulos.GamaColor;
import ar.com.textillevel.entidades.ventas.articulos.GamaColorCliente;

@Entity
@Table(name = "T_PRECIO_GAMA")
public class PrecioGama implements Serializable, Comparable<PrecioGama> {

	private static final long serialVersionUID = -4042531226341019113L;
	
	private Integer id;
	private GamaColorCliente gamaCliente; // nullable.Si no esta, se lee la default
	private GamaColor gamaDefault; // no nullable. Se lee de aca si la otra es null
	private Float precio;
	private GrupoTipoArticuloGama grupoTipoArticuloGama;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "F_GAMA_CLIENTE_P_ID", nullable = true)
	public GamaColorCliente getGamaCliente() {
		return gamaCliente;
	}

	public void setGamaCliente(GamaColorCliente gamaCliente) {
		this.gamaCliente = gamaCliente;
	}

	@ManyToOne
	@JoinColumn(name = "F_GAMA_DEFAULT_P_ID", nullable = true)
	public GamaColor getGamaDefault() {
		return gamaDefault;
	}

	public void setGamaDefault(GamaColor gamaDefault) {
		this.gamaDefault = gamaDefault;
	}

	@Column(name = "A_PRECIO", nullable = false)
	public Float getPrecio() {
		return precio;
	}

	public void setPrecio(Float precio) {
		this.precio = precio;
	}

	@ManyToOne
	@JoinColumn(name = "F_GRUPO_P_ID", updatable=false, insertable=false, nullable=false)
	public GrupoTipoArticuloGama getGrupoTipoArticuloGama() {
		return grupoTipoArticuloGama;
	}

	public void setGrupoTipoArticuloGama(GrupoTipoArticuloGama grupoTipoArticuloGama) {
		this.grupoTipoArticuloGama = grupoTipoArticuloGama;
	}

	@Transient
	public void deepRemove() {
		//nivel 0
		GrupoTipoArticuloGama grupoGama = this.getGrupoTipoArticuloGama();
		grupoGama.getPrecios().remove(this);
		//nivel 1
		if(grupoGama.getPrecios().isEmpty()) {
			RangoAnchoArticuloTenido rangoTenido = grupoGama.getRangoAnchoArticuloTenido();
			rangoTenido.getGruposGama().remove(grupoGama);
			//nivel 2
			if(rangoTenido.getGruposGama().isEmpty()) {
				DefinicionPrecio definicionPrecio = rangoTenido.getDefinicionPrecio();
				definicionPrecio.getRangos().remove(rangoTenido);
			}
		}
	}

	@Transient
	public int compareTo(PrecioGama o) {
		String thisNombreGama = getGamaCliente() == null ? getGamaDefault().getNombre() : getGamaCliente().getNombre(); 
		String otherNombreGama = o.getGamaCliente() == null ? o.getGamaDefault().getNombre() : o.getGamaCliente().getNombre();
		int res = thisNombreGama.compareToIgnoreCase(otherNombreGama);
		if(res == 0) {
			return getPrecio().compareTo(o.getPrecio());
		}
		return res;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gamaCliente == null) ? 0 : gamaCliente.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrecioGama other = (PrecioGama) obj;
		if (gamaCliente == null) {
			if (other.gamaCliente != null)
				return false;
		} else if (!gamaCliente.equals(other.gamaCliente))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Transient
	public PrecioGama deepClone(GrupoTipoArticuloGama grupo) {
		PrecioGama precioGama = new PrecioGama();
		precioGama.setGrupoTipoArticuloGama(grupo);
		precioGama.setGamaCliente(getGamaCliente());
		precioGama.setGamaDefault(getGamaDefault());
		precioGama.setPrecio(getPrecio());
		return precioGama;
	}

}
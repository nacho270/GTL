package ar.com.textillevel.entidades.ventas.cotizacion;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoProducto;

@Entity
@Table(name = "T_VERSION_LISTA_PRECIO")
public class VersionListaDePrecios implements Serializable {

	private static final long serialVersionUID = 8639306062580166848L;
	
	private Integer id;
	private Date inicioValidez;
	private List<DefinicionPrecio> precios;
	
	public VersionListaDePrecios() {
		this.precios = new ArrayList<DefinicionPrecio>();
	}

	public VersionListaDePrecios(Date inicioValidez) {
		this();
		this.inicioValidez = inicioValidez;
	}

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_INICIO_VALIDEZ", nullable = false)
	public Date getInicioValidez() {
		return inicioValidez;
	}

	public void setInicioValidez(Date inicioValidez) {
		this.inicioValidez = inicioValidez;
	}

	@OneToMany(cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
	@JoinColumn(name = "F_VERSION_LISTA_PRECIO_P_ID")
	@org.hibernate.annotations.Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public List<DefinicionPrecio> getPrecios() {
		return precios;
	}

	public void setPrecios(List<DefinicionPrecio> precios) {
		this.precios = precios;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((inicioValidez == null) ? 0 : inicioValidez.hashCode());
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
		VersionListaDePrecios other = (VersionListaDePrecios) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (inicioValidez == null) {
			if (other.inicioValidez != null)
				return false;
		} else if (!inicioValidez.equals(other.inicioValidez))
			return false;
		return true;
	}
	
	@Transient
	public DefinicionPrecio getDefinicionPorTipoProducto(ETipoProducto tipoProducto) {
		for(DefinicionPrecio d : getPrecios()) {
			if (d.getTipoProducto() == tipoProducto) {
				return d;
			}
		}
		return null;
	}

	@Transient
	public VersionListaDePrecios deepClone() {
		VersionListaDePrecios version = new VersionListaDePrecios();
		version.setInicioValidez(getInicioValidez());
		for(DefinicionPrecio def : getPrecios()) {
			version.getPrecios().add(def.deepClone());
		}
		return version;
	}

}
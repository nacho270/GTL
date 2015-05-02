package ar.com.textillevel.entidades.ventas.materiaprima;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import ar.com.textillevel.entidades.gente.Proveedor;

@Entity
@Table(name="T_LISTA_PRECIOS_PROV")
public class ListaDePreciosProveedor implements Serializable {

	private static final long serialVersionUID = -3823366476053289833L;

	private Integer id;
	private Proveedor proveedor;
	private List<PrecioMateriaPrima> precios;

	public ListaDePreciosProveedor() {
		this.precios = new ArrayList<PrecioMateriaPrima>();  
	}
	
	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name="F_PROVEEDOR_P_ID")
	@Fetch(FetchMode.JOIN)
	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="F_LISTA_DE_PRECIOS_P_ID")
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<PrecioMateriaPrima> getPrecios() {
		return precios;
	}

	public void setPrecios(List<PrecioMateriaPrima> precios) {
		this.precios = precios;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		final ListaDePreciosProveedor other = (ListaDePreciosProveedor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
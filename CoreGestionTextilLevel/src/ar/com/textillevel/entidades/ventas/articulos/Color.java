package ar.com.textillevel.entidades.ventas.articulos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="T_COLOR")
public class Color implements Serializable{
	
	private static final long serialVersionUID = -5046319603735453463L;

	private Integer id;
	private String nombre;
	private GamaColor gama;
	
	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="A_NOMBRE", nullable=false)
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_GAMA_P_ID", insertable=false, updatable=false)
	public GamaColor getGama() {
		return gama;
	}
	
	public void setGama(GamaColor gama) {
		this.gama = gama;
	}

	@Transient
	public String toString(){
		return nombre;
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
		Color other = (Color) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}

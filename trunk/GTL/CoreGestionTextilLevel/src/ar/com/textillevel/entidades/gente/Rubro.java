package ar.com.textillevel.entidades.gente;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoRubro;

@Entity
@Table(name="T_RUBRO")
public class Rubro implements Serializable {

	private static final long serialVersionUID = 2243004280132816622L;
	
	private Integer id;
	private String nombre;
	private Integer idTipoRubro;
	
	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="A_NOMBRE")
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@Override
	public String toString() {
		return getNombre();
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
		final Rubro other = (Rubro) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Column(name="A_TIPO_RUBRO", nullable=false)
	private Integer getIdTipoRubro() {
		return idTipoRubro;
	}

	
	private void setIdTipoRubro(Integer idTipoRubro) {
		this.idTipoRubro = idTipoRubro;
	}
	
	public void setTipoRubro(ETipoRubro tipoRubro){
		if(tipoRubro == null){
			setIdTipoRubro(null);
		}
		
		setIdTipoRubro(tipoRubro.getId());
	}
	
	@Transient
	public ETipoRubro getTipoRubro(){
		if(ETipoRubro.getById(getIdTipoRubro())!=null){
			return ETipoRubro.getById(getIdTipoRubro());
		}
		return null;
	}
}
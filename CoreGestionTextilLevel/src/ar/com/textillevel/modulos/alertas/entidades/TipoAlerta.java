package ar.com.textillevel.modulos.alertas.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.modulos.alertas.enums.ETipoAlerta;

@Entity
@Table(name = "T_TIPO_ALERTA")
public class TipoAlerta implements Serializable {

	private static final long serialVersionUID = -5622216024879012301L;

	private Integer id;
	private String descripcion;
	private Integer idTipo;

	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="A_DESCRIPCION",nullable=false)
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name="A_ID_TIPO",nullable=false)
	private Integer getIdTipo() {
		return idTipo;
	}

	private void setIdTipo(Integer idTipo) {
		this.idTipo = idTipo;
	}
	
	@Transient
	public ETipoAlerta getTipo(){
		return ETipoAlerta.getById(getIdTipo());
	}
	
	public void setTipoAlerta(ETipoAlerta tipo){
		setIdTipo(tipo.getId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idTipo == null) ? 0 : idTipo.hashCode());
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
		TipoAlerta other = (TipoAlerta) obj;
		if (descripcion == null) {
			if (other.descripcion != null)
				return false;
		} else if (!descripcion.equals(other.descripcion))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idTipo == null) {
			if (other.idTipo != null)
				return false;
		} else if (!idTipo.equals(other.idTipo))
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return descripcion;
	}
}

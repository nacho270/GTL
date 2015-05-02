package ar.com.textillevel.modulos.odt.entidades.maquinas.procesos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;

@Entity
@Table(name = "T_ACCION_PRODECIMIENTO")
public class AccionProcedimiento implements Serializable {

	private static final long serialVersionUID = -3938913521967942817L;

	private Integer id;
	private String nombre;
	private Byte idTipoSector;

	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="A_NOMBRE",nullable=false)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name="A_ID_SECTOR",nullable=false)
	private Byte getIdTipoSector() {
		return idTipoSector;
	}

	private void setIdTipoSector(Byte idTipoSector) {
		this.idTipoSector = idTipoSector;
	}
	
	@Transient
	public ESectorMaquina getSectorMaquina(){
		return ESectorMaquina.getById(getIdTipoSector());
	}
	
	public void setSectorMaquina(ESectorMaquina sector){
		setIdTipoSector(sector.getId());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
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
		AccionProcedimiento other = (AccionProcedimiento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return nombre;
	}
}

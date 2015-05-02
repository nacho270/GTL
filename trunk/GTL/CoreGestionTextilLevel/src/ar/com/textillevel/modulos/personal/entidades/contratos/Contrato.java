package ar.com.textillevel.modulos.personal.entidades.contratos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
@Table(name = "T_PERS_CONTRATO")
public abstract class Contrato implements Serializable {

	private static final long serialVersionUID = -5568165283881971480L;

	private Integer id;
	private String pathArchivoContrato;
	private Integer idTipoContrato;

	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Transient
	public ETipoContrato getTipoContrato() {
		return ETipoContrato.getById(getIdTipoContrato());
	}
	
	@Override
	@Transient
	public String toString(){
		return getTipoContrato().getDescripcion();
	}
	
	@Column(name="A_PATH_ARCHIVO",nullable=true)
	public String getPathArchivoContrato() {
		return pathArchivoContrato;
	}
	
	public void setPathArchivoContrato(String pathArchivoContrato) {
		this.pathArchivoContrato = pathArchivoContrato;
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
		Contrato other = (Contrato) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Column(name="A_ID_TIPO_CONTRATO",nullable=false)
	protected Integer getIdTipoContrato() {
		return idTipoContrato;
	}
	
	protected void setIdTipoContrato(Integer idTipoContrato) {
		this.idTipoContrato = idTipoContrato;
	}
}

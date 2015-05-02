package ar.com.textillevel.modulos.personal.entidades.legajos.estadocivil;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "T_PERS_INFO_EST_CIVIL")
public class InfoEstadoCivil implements Serializable {

	private static final long serialVersionUID = -5270151557578182700L;

	private Integer id;
	private Integer idEstadoCivil;
	private Integer anio;

	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="A_ID_ESTADO_CIVIL",nullable=false)
	private Integer getIdEstadoCivil() {
		return idEstadoCivil;
	}

	private void setIdEstadoCivil(Integer idEstadoCivil) {
		this.idEstadoCivil = idEstadoCivil;
	}

	@Column(name="A_ANIO")
	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	
	@Transient
	public EEstadoCivil getEstadoCivil(){
		return EEstadoCivil.getById(getIdEstadoCivil());
	}
	
	public void setEstadoCivil(EEstadoCivil estado){
		if(estado == null){
			setIdEstadoCivil(null);
			return;
		}
		setIdEstadoCivil(estado.getId());
	}
}

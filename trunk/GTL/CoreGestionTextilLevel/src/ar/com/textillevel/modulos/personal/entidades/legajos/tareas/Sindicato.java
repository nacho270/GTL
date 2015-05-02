package ar.com.textillevel.modulos.personal.entidades.legajos.tareas;

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

@Entity
@Table(name = "T_PERS_SINDICATO")
public class Sindicato implements Serializable {

	private static final long serialVersionUID = 525635982164023687L;

	private Integer id;
	private String nombre;
	private Integer idTipoCobro;
	private ObraSocial obraSocial;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_NOMBRE", nullable=false)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "A_ID_TIPO_COBRO", nullable=false)
	private Integer getIdTipoCobro() {
		return idTipoCobro;
	}

	private void setIdTipoCobro(Integer idTipoCobro) {
		this.idTipoCobro = idTipoCobro;
	}

	@Transient
	public ETipoCobro getTipoCobro() {
		return ETipoCobro.getById(getIdTipoCobro());
	}

	public void setTipoCobro(ETipoCobro tipoCobro){
		if(tipoCobro == null){
			setIdTipoCobro(null);
			return;
		}
		setIdTipoCobro(tipoCobro.getId());
	}

	@ManyToOne
	@JoinColumn(name="F_OBRA_SOCIAL_P_ID",nullable=false)
	public ObraSocial getObraSocial() {
		return obraSocial;
	}

	public void setObraSocial(ObraSocial obraSocial) {
		this.obraSocial = obraSocial;
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
		Sindicato other = (Sindicato) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getNombre();
	}

}
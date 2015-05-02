package ar.com.textillevel.modulos.personal.entidades.legajos;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import ar.com.textillevel.modulos.personal.entidades.legajos.visitor.IAccionHistoricaVisitor;

@MappedSuperclass
public abstract class AccionHistorica implements Serializable {

	private static final long serialVersionUID = -4105306299737814386L;

	private Integer id;
	private Timestamp fechaHora;
	private String usuario;
	private String fullNameDocCargado;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_FECHA_HORA", nullable=false)
	public Timestamp getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Timestamp fechaHora) {
		this.fechaHora = fechaHora;
	}

	@Column(name = "A_USUARIO", nullable=false)
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	@Column(name = "A_FULL_NAME_DOC_CARGADO", nullable=true)
	public String getFullNameDocCargado() {
		return fullNameDocCargado;
	}

	public void setFullNameDocCargado(String fullNameDocCargado) {
		this.fullNameDocCargado = fullNameDocCargado;
	}

	@Transient
	public abstract String getDescrResumen();

	@Transient
	public abstract String calculateNombreDocumento();

	@Transient
	public abstract void accept(IAccionHistoricaVisitor visitor);
	
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
		AccionHistorica other = (AccionHistorica) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
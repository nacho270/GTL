package ar.com.textillevel.entidades.documentos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoBusquedaAgenda;

@Entity
@Table(name="T_DESTINATARIO_EMAIL")
public class DestinatarioEmail implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String email;
	private Integer idEntidad;
	private Integer idTipoEntidad;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "P_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "A_EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "A_ID_ENTIDAD")
	public Integer getIdEntidad() {
		return idEntidad;
	}

	public void setIdEntidad(Integer idEntidad) {
		this.idEntidad = idEntidad;
	}

	@Column(name = "A_ID_TIPO_ENTIDAD")
	private Integer getIdTipoEntidad() {
		return idTipoEntidad;
	}

	private void setIdTipoEntidad(Integer idTipoEntidad) {
		this.idTipoEntidad = idTipoEntidad;
	}
	
	@Transient
	public ETipoBusquedaAgenda getTipoEntidad() {
		if(getIdTipoEntidad() == null) {
			return null;
		}
		return ETipoBusquedaAgenda.getById(getIdTipoEntidad());
	}

	public void setTipoEntidad(ETipoBusquedaAgenda tipoEntidad) {
		if (tipoEntidad == null) {
			this.setIdTipoEntidad(null);;
		}
		setIdTipoEntidad(tipoEntidad.getId());
	}

	@Override
	public String toString() {
		return getEmail();
	}

}
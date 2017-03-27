package ar.com.textillevel.modulos.odt.entidades.workflow;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.modulos.odt.enums.EAvanceODT;
import ar.com.textillevel.modulos.terminal.entidades.Terminal;

@Entity
@Table(name="T_CAMBIO_AVANCE_ODT")
public class CambioAvance implements Serializable {

	private static final long serialVersionUID = 731109379829445321L;

	private Integer id;
	private Byte idAvance; // EAvanceODT
	private Timestamp fechaHora;
	private UsuarioSistema usuario;
	private Terminal terminal;
	private String observaciones;
	
	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="A_ID_AVANCE",nullable=false)
	private Byte getIdAvance() {
		return idAvance;
	}

	private void setIdAvance(Byte idAvance) {
		this.idAvance = idAvance;
	}

	@Transient
	public EAvanceODT getAvance(){
		return EAvanceODT.getById(getIdAvance());
	}
	
	public void setAvance(EAvanceODT estado){
		setIdAvance(estado.getId());
	}
	
	@Column(name="A_FECHA_HORA",nullable=false)
	public Timestamp getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Timestamp fechaHora) {
		this.fechaHora = fechaHora;
	}

	@ManyToOne
	@JoinColumn(name="F_USUARIO_P_ID",nullable=true)
	public UsuarioSistema getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioSistema usuario) {
		this.usuario = usuario;
	}

	@Column(name="A_OBSERVACIONES",nullable=true)
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	@ManyToOne
	@JoinColumn(name="F_TERMINAL_P_ID",nullable=true)
	public Terminal getTerminal() {
		return terminal;
	}

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idAvance == null) ? 0 : idAvance.hashCode());
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
		CambioAvance other = (CambioAvance) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idAvance == null) {
			if (other.idAvance != null)
				return false;
		} else if (!idAvance.equals(other.idAvance))
			return false;
		return true;
	}
}

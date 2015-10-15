package ar.com.fwcommon.auditoria.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "T_Evento")
public class Evento implements Serializable {

	private static final long serialVersionUID = 5012643907282588139L;

	private int idEvento;
	private Timestamp fechaHora;
	private String usuario;
	private int idModulo;
	private TipoEvento tipoEvento;
	private String clase;
	private String descripcion;
	private boolean esDeSistema;

	/** Método constructor */
	public Evento() {
	}

	public Evento(String clase, String nombreUsuario) {
		setUsuario(nombreUsuario);
		setClase(clase);
	}

	/**
	 * Devuelve el id del Evento.
	 * @return idEvento
	 */
	@Id
	@GeneratedValue
	@Column(name = "P_IdEvento")
	public int getIdEvento() {
		return idEvento;
	}

	/**
	 * Setea el id de un Evento.
	 * @param idEvento
	 */
	public void setIdEvento(int idEvento) {
		this.idEvento = idEvento;
	}

	/**
	 * Devuelve la fecha y hora de un Evento.
	 * @return fechaHora
	 */
	@Column(name = "A_Fecha", insertable = false, updatable = false)//, columnDefinition="TIMESTAMP DEFAULT SYSTIMESTAMP"
	public Timestamp getFechaHora() {
		return fechaHora;
	}

	/**
	 * Setea la fecha y hora de un Evento.
	 * @param fechaHora
	 */
	public void setFechaHora(Timestamp fechaHora) {
		this.fechaHora = fechaHora;
	}

	/**
	 * Devuelve el usuario asociado al Evento.
	 * @return usuario
	 */
	@Column(name = "A_Usuario")
	public String getUsuario() {
		return usuario;
	}

	/**
	 * Setea el usuario asociado de un Evento.
	 * @param usuario
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * Devuelve el modulo desde donde se disparó el Evento.
	 * @return modulo
	 */
	@Column(name = "A_IdModulo")
	public int getIdModulo() {
		return idModulo;
	}

	/**
	 * Setea el modulo asociado.
	 * @param modulo
	 */
	public void setIdModulo(int idModulo) {
		this.idModulo = idModulo;
	}

	/**
	 * Devuelve el tipo de evento asociado.
	 * @return
	 */
	@ManyToOne
	@JoinColumn(name = "F_IdTipoEvento")
	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}

	/**
	 * Setea el tipo de evento asociado.
	 * @param tipoEvento
	 */
	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	/**
	 * Devuelve la clase asociada al tipo de evento.
	 * @return clase
	 */
	@Column(name = "A_Clase")
	public String getClase() {
		return clase;
	}

	/**
	 * Setea la clase asociada.
	 * @param clase
	 */
	public void setClase(String clase) {
		this.clase = clase;
	}

	/**
	 * Devuelve la descripcion.
	 * @return descripcion
	 */
	@Column(name = "A_Descripcion",length=4000)//Oracle tiene 4000 de maximo.
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Setea la descripcion.
	 * @param descripcion
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Devuelve true si es un Evento de sistema.
	 * @return esDeSistema
	 */
	@Column(name = "A_EsDeSistema")
	public boolean isEsDeSistema() {
		return esDeSistema;
	}

	/**
	 * Setea el flag.
	 * @param esDeSistema
	 */
	public void setEsDeSistema(boolean esDeSistema) {
		this.esDeSistema = esDeSistema;
	}
}
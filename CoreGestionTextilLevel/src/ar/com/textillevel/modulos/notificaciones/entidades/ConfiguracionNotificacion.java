package ar.com.textillevel.modulos.notificaciones.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.portal.Modulo;
import ar.com.textillevel.modulos.notificaciones.enums.ETipoDestinoNotificacion;
import ar.com.textillevel.modulos.notificaciones.enums.ETipoNotificacion;

@Entity
@Table(name = "T_CONF_NOTIFICACIONES")
public class ConfiguracionNotificacion implements Serializable {

	private static final long serialVersionUID = 6419683108111163305L;

	private Integer id;
	private Integer idTipo;
	private Modulo moduloAsociado;
	private String nombreDestino;
	private Byte idTipoDestino;

	@Id
	@Column(name = "P_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_ID_TIPO", nullable = false)
	private Integer getIdTipo() {
		return idTipo;
	}

	private void setIdTipo(Integer idTipo) {
		this.idTipo = idTipo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_ID_MODULO_P_ID")
	public Modulo getModuloAsociado() {
		return moduloAsociado;
	}

	public void setModuloAsociado(Modulo moduloAsociado) {
		this.moduloAsociado = moduloAsociado;
	}

	@Transient
	public ETipoNotificacion getTipo() {
		return ETipoNotificacion.getById(getIdTipo());
	}

	public void setTipoNotificacion(ETipoNotificacion tipo) {
		setIdTipo(tipo.getId());
	}

	@Column(name = "A_NOMBRE_DESTINO", nullable = false)
	public String getNombreDestino() {
		return nombreDestino;
	}

	public void setNombreDestino(String nombreDestino) {
		this.nombreDestino = nombreDestino;
	}

	@Column(name = "A_ID_TIPO_DESTINO", nullable = false, unique = true)
	private Byte getIdTipoDestino() {
		return idTipoDestino;
	}

	private void setIdTipoDestino(Byte idTipoDestino) {
		this.idTipoDestino = idTipoDestino;
	}

	@Transient
	public ETipoDestinoNotificacion getTipoDestino() {
		return ETipoDestinoNotificacion.getById(getIdTipoDestino());
	}

	public void setTipoDestinoNotificacion(ETipoDestinoNotificacion tipo) {
		setIdTipoDestino(tipo.getId());
	}
}

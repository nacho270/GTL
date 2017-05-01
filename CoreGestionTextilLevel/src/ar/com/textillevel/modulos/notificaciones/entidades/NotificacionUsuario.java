package ar.com.textillevel.modulos.notificaciones.entidades;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.modulos.notificaciones.enums.ETipoNotificacion;

@Entity
@Table(name = "T_NOTIFICACION_USUARIO")
public class NotificacionUsuario implements Serializable {

	private static final long serialVersionUID = 3186592429222397192L;

	private Integer id;
	private String texto;
	private Integer idTipo;
	private transient UsuarioSistema usuarioSistema;
	private Boolean leida;
	private Timestamp fecha;

	public NotificacionUsuario() {
		this.leida = false;
		this.fecha = DateUtil.getAhora();
	}

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_TEXTO", nullable = false)
	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	@Column(name = "A_ID_TIPO", nullable = false)
	private Integer getIdTipo() {
		return idTipo;
	}

	private void setIdTipo(Integer idTipo) {
		this.idTipo = idTipo;
	}

	@Transient
	public ETipoNotificacion getTipo() {
		return ETipoNotificacion.getById(getIdTipo());
	}

	public void setTipoNotificacion(ETipoNotificacion tipo) {
		setIdTipo(tipo.getId());
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_USUARIO_SISTEMA_P_ID")
	public UsuarioSistema getUsuarioSistema() {
		return usuarioSistema;
	}

	public void setUsuarioSistema(UsuarioSistema usuarioSistema) {
		this.usuarioSistema = usuarioSistema;
	}

	@Column(name = "A_LEIDA", nullable = true)
	public Boolean getLeida() {
		return leida;
	}

	public void setLeida(Boolean leida) {
		this.leida = leida;
	}

	@Column(name = "A_FECHA")
	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}
}

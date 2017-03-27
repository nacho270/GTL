package ar.com.textillevel.modulos.odt.entidades.workflow;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.Maquina;
import ar.com.textillevel.modulos.odt.entidades.maquinas.TipoMaquina;
import ar.com.textillevel.modulos.terminal.entidades.Terminal;

@Entity
@Table(name="T_TRANSICION_ODT")
public class TransicionODT implements Serializable {

	private static final long serialVersionUID = -1357596915241440886L;

	private Integer id;
	private OrdenDeTrabajo odt;
	private Maquina maquina;
	private TipoMaquina tipoMaquina; // replicacion para acceder directamente al dato, podria acceder tambien haciendo maquina.tipoMaquina
	private Timestamp fechaHoraRegistro;
	private List<CambioAvance> cambiosAvance;
	private UsuarioSistema usuarioSistema;
	private Terminal terminal;

	public TransicionODT() {
		cambiosAvance = new ArrayList<CambioAvance>();
	}

	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_ODT_P_ID",nullable=false)
	public OrdenDeTrabajo getOdt() {
		return odt;
	}

	public void setOdt(OrdenDeTrabajo odt) {
		this.odt = odt;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_MAQUINA_P_ID",nullable=true)
	public Maquina getMaquina() {
		return maquina;
	}

	public void setMaquina(Maquina maquina) {
		this.maquina = maquina;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_TIPO_MAQUINA_P_ID",nullable=true)
	public TipoMaquina getTipoMaquina() {
		return tipoMaquina;
	}

	public void setTipoMaquina(TipoMaquina tipoMaquina) {
		this.tipoMaquina = tipoMaquina;
	}
	
	@Column(name="A_FECHA_HORA",nullable=false)
	public Timestamp getFechaHoraRegistro() {
		return fechaHoraRegistro;
	}

	public void setFechaHoraRegistro(Timestamp fechaHoraRegistro) {
		this.fechaHoraRegistro = fechaHoraRegistro;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_USUARIO_P_ID",nullable=true)
	public UsuarioSistema getUsuarioSistema() {
		return usuarioSistema;
	}

	public void setUsuarioSistema(UsuarioSistema usuarioSistema) {
		this.usuarioSistema = usuarioSistema;
	}

	@ManyToOne
	@JoinColumn(name="F_TERMINAL_P_ID",nullable=true)
	public Terminal getTerminal() {
		return terminal;
	}

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}
	
	@OneToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	@org.hibernate.annotations.Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name="F_TRANSICION_P_ID")
	public List<CambioAvance> getCambiosAvance() {
		return cambiosAvance;
	}

	public void setCambiosAvance(List<CambioAvance> cambiosAvance) {
		this.cambiosAvance = cambiosAvance;
	}
}

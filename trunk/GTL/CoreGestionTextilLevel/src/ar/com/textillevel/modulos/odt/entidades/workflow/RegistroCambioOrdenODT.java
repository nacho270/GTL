package ar.com.textillevel.modulos.odt.entidades.workflow;

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

import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.Maquina;

@Entity
@Table(name="T_REGISTRO_CAMBIO_ODT")
public class RegistroCambioOrdenODT implements Serializable {

	private static final long serialVersionUID = 4737636738377176739L;

	private Integer id;
	private OrdenDeTrabajo odt;
	private Timestamp fechaHora;
	private UsuarioSistema usuario;
	private Short orden;
	private Maquina maquina;
	
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

	@Column(name="A_FECHA_HORA")
	public Timestamp getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Timestamp fechaHora) {
		this.fechaHora = fechaHora;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_USUARIO_P_ID",nullable=false)
	public UsuarioSistema getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioSistema usuario) {
		this.usuario = usuario;
	}

	@Column(name="A_ORDEN",nullable=false)
	public Short getOrden() {
		return orden;
	}

	public void setOrden(Short orden) {
		this.orden = orden;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_MAQUINA_P_ID",nullable=false)
	public Maquina getMaquina() {
		return maquina;
	}

	public void setMaquina(Maquina maquina) {
		this.maquina = maquina;
	}
}
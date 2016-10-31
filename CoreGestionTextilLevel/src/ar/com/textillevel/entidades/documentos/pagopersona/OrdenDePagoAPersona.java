package ar.com.textillevel.entidades.documentos.pagopersona;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
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

import ar.com.textillevel.entidades.documentos.pagopersona.formapago.FormaPagoOrdenDePagoPersona;
import ar.com.textillevel.entidades.gente.Persona;

@Entity
@Table(name = "T_ORDEN_PAGO_PERS")
public class OrdenDePagoAPersona implements Serializable {

	private static final long serialVersionUID = 8509952371646684502L;

	private Integer id;
	private Integer nroOrden;
	private Date fecha;
	private BigDecimal montoTotal;
	private String detalle;
	private Persona persona;
	private List<FormaPagoOrdenDePagoPersona> formasDePago;
	private String usuarioVerificador;
	private String usuarioCreador;
	private Boolean entregado;
	private Timestamp fechaHoraEntregada;
	private String terminalEntrega;
	
	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "A_NRO_ORDEN", nullable = false)
	public Integer getNroOrden() {
		return nroOrden;
	}
	
	public void setNroOrden(Integer nroOrden) {
		this.nroOrden = nroOrden;
	}
	
	@Column(name = "A_FECHA", nullable = false)
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Column(name="A_MONTO_TOTAL",nullable=false)
	public BigDecimal getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(BigDecimal montoTotal) {
		this.montoTotal = montoTotal;
	}

	@Column(name="A_DETALLE",nullable=false)
	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	@ManyToOne
	@JoinColumn(name = "F_PERS_P_ID",nullable=false)
	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "F_ODP_PERSONA_P_ID")
	public List<FormaPagoOrdenDePagoPersona> getFormasDePago() {
		return formasDePago;
	}

	public void setFormasDePago(List<FormaPagoOrdenDePagoPersona> formasDePago) {
		this.formasDePago = formasDePago;
	}

	@Column(name="A_USR_VERFICADOR",nullable=true)
	public String getUsuarioVerificador() {
		return usuarioVerificador;
	}

	public void setUsuarioVerificador(String usuarioVerificador) {
		this.usuarioVerificador = usuarioVerificador;
	}

	@Column(name="A_USR_CREADOR",nullable=true)
	public String getUsuarioCreador() {
		return usuarioCreador;
	}

	public void setUsuarioCreador(String usuarioCreador) {
		this.usuarioCreador = usuarioCreador;
	}
	
	@Column(name = "A_ENTREGADA", nullable = true)
	public Boolean getEntregado() {
		return entregado;
	}

	public void setEntregado(Boolean entregado) {
		this.entregado = entregado;
	}
	
	@Column(name = "A_FECHA_HORA_ENTREGADA", nullable = true)
	public Timestamp getFechaHoraEntregada() {
		return fechaHoraEntregada;
	}

	public void setFechaHoraEntregada(Timestamp fechaHoraEntregada) {
		this.fechaHoraEntregada = fechaHoraEntregada;
	}
	
	@Column(name = "A_TERMINAL_ENTREGA", nullable = true)
	public String getTerminalEntrega() {
		return terminalEntrega;
	}

	public void setTerminalEntrega(String terminalEntrega) {
		this.terminalEntrega = terminalEntrega;
	}
}

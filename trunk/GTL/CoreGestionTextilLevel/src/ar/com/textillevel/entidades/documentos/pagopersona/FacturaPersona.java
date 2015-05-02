package ar.com.textillevel.entidades.documentos.pagopersona;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import ar.com.textillevel.entidades.documentos.factura.proveedor.ImpuestoItemProveedor;
import ar.com.textillevel.entidades.enums.ETipoFactura;
import ar.com.textillevel.entidades.gente.Persona;

@Entity
@Table(name = "T_FACTURA_PERSONA")
public class FacturaPersona implements Serializable {

	private static final long serialVersionUID = 5754786612049174636L;

	private Integer id;
	private Integer nroFactura;
	private Persona persona;
	private Date fecha;
	private BigDecimal monto;
	private BigDecimal montoSinImpuestos;
	private String detalle;
	private String usuarioVerificador;
	private String usuarioCreador;
	private Integer idTipoFactura;
	private List<ImpuestoItemProveedor> impuestos;
	
	public FacturaPersona() {
		impuestos = new ArrayList<ImpuestoItemProveedor>();
	}

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_NRO_FACTURA")
	public Integer getNroFactura() {
		return nroFactura;
	}

	public void setNroFactura(Integer nroFactura) {
		this.nroFactura = nroFactura;
	}

	@ManyToOne
	@JoinColumn(name = "F_PERSONA_P_ID")
	@Fetch(FetchMode.JOIN)
	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	@Column(name = "A_FECHA", nullable = false)
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Column(name = "A_MONTO", nullable = false)
	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	@Column(name = "A_DETALLE", nullable = false)
	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	@Column(name = "A_USR_VERIFICADOR", nullable = true)
	public String getUsuarioVerificador() {
		return usuarioVerificador;
	}

	public void setUsuarioVerificador(String usuarioVerificador) {
		this.usuarioVerificador = usuarioVerificador;
	}

	@Column(name = "A_USR_CREADOR", nullable = true)
	public String getUsuarioCreador() {
		return usuarioCreador;
	}

	public void setUsuarioCreador(String usuarioCreador) {
		this.usuarioCreador = usuarioCreador;
	}
	
	@Column(name = "A_ID_TIPO_FACTURA", nullable = false)
	public Integer getIdTipoFactura() {
		return idTipoFactura;
	}

	public void setIdTipoFactura(Integer idTipoFactura) {
		this.idTipoFactura = idTipoFactura;
	}

	public void setTipoFactura(ETipoFactura tipoFactura) {
		if (tipoFactura == null) {
			this.setIdTipoFactura(null);
		}
		setIdTipoFactura(tipoFactura.getId());
	}

	@Transient
	public ETipoFactura getTipoFactura() {
		if (getIdTipoFactura() == null) {
			return null;
		}
		return ETipoFactura.getById(getIdTipoFactura());
	}

	@ManyToMany
	@JoinTable(name = "T_FACT_PERS_IMPUESTO", 
			joinColumns = { @JoinColumn(name = "F_FACT_PERS_P_ID") }, 
			inverseJoinColumns = { @JoinColumn(name = "F_IMPUESTO_P_ID") })
	public List<ImpuestoItemProveedor> getImpuestos() {
		return impuestos;
	}
	
	public void setImpuestos(List<ImpuestoItemProveedor> impuestos) {
		this.impuestos = impuestos;
	}

	@Column(name="A_MONTO_SIN_IMPUESTOS",nullable=false)
	public BigDecimal getMontoSinImpuestos() {
		return montoSinImpuestos;
	}
	
	public void setMontoSinImpuestos(BigDecimal montoSinImpuestos) {
		this.montoSinImpuestos = montoSinImpuestos;
	}
}

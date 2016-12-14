package ar.com.textillevel.entidades.cheque;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.entidades.enums.EEstadoCheque;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.gente.Persona;
import ar.com.textillevel.entidades.gente.Proveedor;

@Entity
@Table(name = "T_CHEQUE")
public class Cheque implements Serializable {

	private static final long serialVersionUID = -7970725828602017267L;

	private Integer id;
	private Cliente cliente;
	private Integer idEstado;
	private BigDecimal importe;
	private String numero;
	private Banco banco;
	private Date fechaDeposito;
	private Date fechaEntrada;
	private NumeracionCheque numeracion;
	private String cuit;
	private Character capitalOInterior;
	private String nombreProveedorSalida;
	private Date fechaSalida;
	private Proveedor proveedorSalida;
	private Cliente clienteSalida;
	private Persona personaSalida;
	private Banco bancoSalida;
	private String nroCuenta;
	
	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "F_CLIENTE_P_ID", nullable = false)
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Column(name = "A_ID_ESTADO_CHEQUE", nullable = false)
	private Integer getIdEstado() {
		return idEstado;
	}

	private void setIdEstado(Integer idEstado) {
		this.idEstado = idEstado;
	}

	public void setEstadoCheque(EEstadoCheque estadoCheque) {
		if (estadoCheque == null) {
			this.setIdEstado(null);
		}
		setIdEstado(estadoCheque.getId());
	}

	@Transient
	public EEstadoCheque getEstadoCheque() {
		if (getIdEstado() == null) {
			return null;
		}
		return EEstadoCheque.getById(getIdEstado());
	}

	@Column(name = "A_IMPORTE_CHEQUE", nullable = false)
	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	@Column(name = "A_NUMERO_CHEQUE", nullable = false)
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@ManyToOne
	@JoinColumn(name = "F_BANCO_P_ID", nullable = false)
	@Fetch(FetchMode.JOIN)
	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	@Column(name = "A_FECHA_DEPOSITO", nullable = false)
	public Date getFechaDeposito() {
		return fechaDeposito;
	}

	public void setFechaDeposito(Date fechaDeposito) {
		this.fechaDeposito = fechaDeposito;
	}

	@Embedded
	public NumeracionCheque getNumeracion() {
		return numeracion;
	}

	public void setNumeracion(NumeracionCheque numeracion) {
		this.numeracion = numeracion;
	}

	@Column(name="A_CUIT", nullable=false, length=50)
	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}
	
	@Column(name = "A_FECHA_ENTRADA", nullable = false)
	public Date getFechaEntrada() {
		return fechaEntrada;
	}
	
	public void setFechaEntrada(Date fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}

	@Column(name="A_CAPITAL_O_INTERIOR", nullable=false)
	public Character getCapitalOInterior() {
		return capitalOInterior;
	}

	public void setCapitalOInterior(Character capitalOInterior) {
		this.capitalOInterior = capitalOInterior;
	}
	
	@Column(name="A_PROV_SALIDA", nullable=true)
	public String getNombreProveedorSalida() {
		return nombreProveedorSalida;
	}

	public void setNombreProveedorSalida(String nombreProveedorSalida) {
		this.nombreProveedorSalida = nombreProveedorSalida;
	}

	@Column(name = "A_FECHA_SALIDA")
	public Date getFechaSalida() {
		return fechaSalida;
	}
	
	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_PROV_SAL_P_ID",nullable=true)
	public Proveedor getProveedorSalida() {
		return proveedorSalida;
	}
	
	public void setProveedorSalida(Proveedor proveedorSalida) {
		this.proveedorSalida = proveedorSalida;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_CLIENTE_SAL_P_ID",nullable=true)
	public Cliente getClienteSalida() {
		return clienteSalida;
	}
	
	public void setClienteSalida(Cliente clienteSalida) {
		this.clienteSalida = clienteSalida;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_PERS_SAL_P_ID",nullable=true)
	public Persona getPersonaSalida() {
		return personaSalida;
	}

	public void setPersonaSalida(Persona personaSalida) {
		this.personaSalida = personaSalida;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_BANCO_SALIDA_P_ID",nullable=true)
	public Banco getBancoSalida() {
		return bancoSalida;
	}
	
	public void setBancoSalida(Banco bancoSalida) {
		this.bancoSalida = bancoSalida;
	}
	
	@Column(name = "A_NRO_CUENTA")
	public String getNroCuenta() {
		return nroCuenta;
	}

	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}

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
		Cheque other = (Cheque) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return getNumeracion().toString() + " - " + getBanco().getNombre() + " - " + getNumero() + " - " + DateUtil.dateToString(getFechaDeposito(), DateUtil.SHORT_DATE) + " - " + getImporte().doubleValue() + " - " + getCapitalOInterior();
	}
}

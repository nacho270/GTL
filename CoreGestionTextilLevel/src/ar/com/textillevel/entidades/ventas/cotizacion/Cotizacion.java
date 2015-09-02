package ar.com.textillevel.entidades.ventas.cotizacion;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.com.textillevel.entidades.gente.Cliente;

@Entity
@Table(name = "T_COTIZACION")
public class Cotizacion implements Serializable {

	private static final long serialVersionUID = -9020791443951044495L;

	private Integer id;
	private Cliente cliente;
	private VersionListaDePrecios versionListaPrecio;
	private Date fechaInicio;
	private Integer validez;
	private Integer numero;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_CLIENTE_P_ID", nullable=false)
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@ManyToOne
	@JoinColumn(name="F_VERSION_LISTA_PRECIOS_P_ID", nullable=false)
	public VersionListaDePrecios getVersionListaPrecio() {
		return versionListaPrecio;
	}

	public void setVersionListaPrecio(VersionListaDePrecios versionListaPrecio) {
		this.versionListaPrecio = versionListaPrecio;
	}

	@Column(name="A_FECHA_INICIO", nullable=false)
	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	@Column(name="A_VALIDEZ", nullable=false)
	public Integer getValidez() {
		return validez;
	}

	public void setValidez(Integer validez) {
		this.validez = validez;
	}

	@Column(name="A_NUMERO", nullable=false, unique=true)
	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

}
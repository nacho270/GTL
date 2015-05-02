package ar.com.textillevel.entidades.documentos.factura;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.EEstadoCorreccion;
import ar.com.textillevel.entidades.enums.EEstadoImpresionDocumento;
import ar.com.textillevel.entidades.enums.ETipoCorreccionFactura;
import ar.com.textillevel.entidades.enums.ETipoFactura;

@Entity
public abstract class CorreccionFactura extends DocumentoContableCliente implements Serializable {

	private static final long serialVersionUID = -1548548866392928892L;

	private String descripcion;
	private Integer idEstado;
	private Boolean verificada;
	private String usuarioVerificador;
	private Boolean anulada;

	public CorreccionFactura(){
		this.idEstado = EEstadoCorreccion.IMPAGA.getId();
		setIdEstadoImpresion(EEstadoImpresionDocumento.PENDIENTE.getId());
		this.verificada = false;
		this.anulada = false;
	}

	@Column(name="A_DESCRIPCION", nullable=false)
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Column(name = "A_ID_ESTADO_CORRECCION", nullable = false)
	public Integer getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Integer idEstado) {
		this.idEstado = idEstado;
	}

	public void setEstadoCorreccion(EEstadoCorreccion estado) {
		if (estado == null) {
			this.setIdEstado(null);
		}
		setIdEstado(estado.getId());
	}

	@Transient
	public EEstadoCorreccion getEstadoCorreccion() {
		if (getIdEstado() == null) {
			return null;
		}
		return EEstadoCorreccion.getById(getIdEstado());
	}

	@Column(name="A_VERIFICADA",nullable=false)
	public Boolean getVerificada() {
		return verificada;
	}
	
	public void setVerificada(Boolean verificada) {
		this.verificada = verificada;
	}
	
	@Column(name="A_USUARIO_VERIFICADOR",nullable=true)
	public String getUsuarioVerificador() {
		return usuarioVerificador;
	}
	
	public void setUsuarioVerificador(String usuarioVerificador) {
		this.usuarioVerificador = usuarioVerificador;
	}

	public void setTipoFactura(ETipoFactura tipoFactura) {
		if (tipoFactura == null) {
			this.setIdTipoFactura(null);
		}
		setIdTipoFactura(tipoFactura.getId());
	}
	
	@Column(name="A_ANULADA",nullable=false)
	public Boolean getAnulada() {
		return anulada;
	}
	
	public void setAnulada(Boolean anulada) {
		this.anulada = anulada;
	}


	@Transient
	public ETipoFactura getTipoFactura() {
		if (getIdTipoFactura() == null) {
			return null;
		}
		return ETipoFactura.getById(getIdTipoFactura());
	}

	@Transient
	public abstract ETipoCorreccionFactura getTipo();

}
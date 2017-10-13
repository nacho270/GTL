package ar.com.textillevel.gui.modulos.cheques.cabecera;

import java.sql.Date;

import ar.com.textillevel.entidades.enums.EEstadoCheque;
import ar.com.textillevel.entidades.enums.EnumTipoFecha;

public class ModeloCabeceraCheques {

	private Integer paginaActual;
	private Date fechaDesde;
	private Date fechaHasta;
	private EEstadoCheque estadoCheque;
	private Integer nroCliente;
	private String numeracionCheque;
	private String nombreProveedor;
	private String nombrePersona;
	private EnumTipoFecha tipoFecha;
	private String numeroCheque;
	private Integer idBanco;
	private Double montoDesde;
	private Double montoHasta;

	public Integer getPaginaActual() {
		return paginaActual;
	}

	public void setPaginaActual(Integer paginaActual) {
		this.paginaActual = paginaActual;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public EEstadoCheque getEstadoCheque() {
		return estadoCheque;
	}

	public void setEstadoCheque(EEstadoCheque estadoCheque) {
		this.estadoCheque = estadoCheque;
	}

	public Integer getNroCliente() {
		return nroCliente;
	}

	public void setNroCliente(Integer nroCliente) {
		this.nroCliente = nroCliente;
	}

	public String getNumeracionCheque() {
		return numeracionCheque;
	}

	public void setNumeracionCheque(String numeracionCheque) {
		this.numeracionCheque = numeracionCheque;
	}

	public String getNombreProveedor() {
		return nombreProveedor;
	}

	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
	}

	public EnumTipoFecha getTipoFecha() {
		return tipoFecha;
	}

	public void setTipoFecha(EnumTipoFecha tipoFecha) {
		this.tipoFecha = tipoFecha;
	}

	public String getNumeroCheque() {
		return numeroCheque;
	}

	public void setNumeroCheque(String numeroCheque) {
		this.numeroCheque = numeroCheque;
	}

	
	public String getNombrePersona() {
		return nombrePersona;
	}

	
	public void setNombrePersona(String nombrePersona) {
		this.nombrePersona = nombrePersona;
	}

	public Integer getIdBanco() {
		return idBanco;
	}

	public void setIdBanco(Integer idBanco) {
		this.idBanco = idBanco;
	}

	public Double getMontoDesde() {
		return montoDesde;
	}

	public void setMontoDesde(Double montoDesde) {
		this.montoDesde = montoDesde;
	}

	public Double getMontoHasta() {
		return montoHasta;
	}

	public void setMontoHasta(Double montoHasta) {
		this.montoHasta = montoHasta;
	}
}

package ar.com.textillevel.entidades.cuenta.to;

import java.io.Serializable;

import ar.com.textillevel.entidades.gente.IAgendable;

public class CuentaOwnerTO implements Serializable{

	private static final long serialVersionUID = -8978848841425753085L;
	
	private String razonSocial;
	private String identificadorInterno;
	private String email;
	private String telefono;
	private String direccion;
	private String localidad;
	private String celular;
	private String posicionIVA;
	private String cuit;
	private String condicionVenta;
	private String nroIngresosBrutos;
	
	public CuentaOwnerTO(IAgendable contacto) {
		this.razonSocial = contacto.getRazonSocial();
		this.identificadorInterno = contacto.getIdentificadorInterno();
		this.email = contacto.getEmail();
		this.telefono = contacto.getTelefono();
		this.direccion = contacto.getDireccion();
		this.localidad = contacto.getLocalidad();
		this.celular = contacto.getCelular();
		if(contacto.getPosicionIva()!=null){
			this.posicionIVA = contacto.getPosicionIva().getDescripcion();
		}
		this.condicionVenta = contacto.getCondicionDeVenta();
		this.cuit = contacto.getCuit();
		this.nroIngresosBrutos = contacto.getNroIngresosBrutos();
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getIdentificadorInterno() {
		return identificadorInterno;
	}

	public void setIdentificadorInterno(String identificadorInterno) {
		this.identificadorInterno = identificadorInterno;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	
	public String getPosicionIVA() {
		return posicionIVA;
	}

	
	public void setPosicionIVA(String posicionIVA) {
		this.posicionIVA = posicionIVA;
	}

	
	public String getCuit() {
		return cuit;
	}

	
	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	
	public String getCondicionVenta() {
		return condicionVenta;
	}

	
	public void setCondicionVenta(String condicionVenta) {
		this.condicionVenta = condicionVenta;
	}

	public String getNroIngresosBrutos() {
		return nroIngresosBrutos;
	}

	public void setNroIngresosBrutos(String nroIngresosBrutos) {
		this.nroIngresosBrutos = nroIngresosBrutos;
	}

}
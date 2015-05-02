package ar.com.textillevel.mobile.modules.cuenta.to;

import ar.com.textillevel.mobile.modules.common.to.EPosicionIVA;

public interface IAgendable {
	public Integer getId();
	public String getRazonSocial();
	public String getTelefono();
	public String getDireccion();
	public Integer getCodigoPostal();
	public String getLocalidad();
	public String getFax();
	public String getCelular();
	public String getContacto();
	public String getEmail();
	public String getIdentificadorInterno();
	public EPosicionIVA getPosicionIva();
	public String getCuit();
	public String getCondicionDeVenta();
	public String getNroIngresosBrutos();
}

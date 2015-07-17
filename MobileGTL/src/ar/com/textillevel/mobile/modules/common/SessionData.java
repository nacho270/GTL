package ar.com.textillevel.mobile.modules.common;

import ar.com.textillevel.mobile.modules.common.to.UsuarioSistemaTO;

public class SessionData {

	private static final SessionData instance = new SessionData();
	
	private SessionData(){}
	
	public static SessionData getInstance(){
		return instance;
	}
	
	private UsuarioSistemaTO usuarioEnSesion;

	public UsuarioSistemaTO getUsuarioEnSesion() {
		return usuarioEnSesion;
	}

	public void setUsuarioEnSesion(UsuarioSistemaTO usuarioEnSesion) {
		this.usuarioEnSesion = usuarioEnSesion;
	}

	public Integer getNroSucursal() {
		if(getUsuarioEnSesion() == null) {
			throw new IllegalStateException("ERROR: Aun no se inicializó usuarioEnSession...");
		}
		return getUsuarioEnSesion().getParamsSistemaTO().getNroSucursal();
	}

}
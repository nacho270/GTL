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
}

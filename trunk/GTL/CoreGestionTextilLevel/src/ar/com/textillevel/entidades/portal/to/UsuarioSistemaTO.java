package ar.com.textillevel.entidades.portal.to;

import java.io.Serializable;

public class UsuarioSistemaTO implements Serializable {

	private static final long serialVersionUID = 3431259419845112687L;

	private String token;
	private String usrName;

	public UsuarioSistemaTO(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUsrName() {
		return usrName;
	}

	public void setUsrName(String usrName) {
		this.usrName = usrName;
	}
}

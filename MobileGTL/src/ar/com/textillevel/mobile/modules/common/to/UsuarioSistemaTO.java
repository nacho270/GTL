package ar.com.textillevel.mobile.modules.common.to;

import java.io.Serializable;

public class UsuarioSistemaTO implements Serializable {

	private static final long serialVersionUID = 3431259419845112687L;

	private String token;
	private String usrName;
	private ParamsSistemaTO paramsSistemaTO; 
	
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

	public ParamsSistemaTO getParamsSistemaTO() {
		return paramsSistemaTO;
	}

	public void setParamsSistemaTO(ParamsSistemaTO paramsSistemaTO) {
		this.paramsSistemaTO = paramsSistemaTO;
	}

}

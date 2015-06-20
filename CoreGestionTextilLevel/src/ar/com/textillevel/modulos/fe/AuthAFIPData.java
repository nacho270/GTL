package ar.com.textillevel.modulos.fe;

public class AuthAFIPData {

	private String token;
	private String hash;
	private Long cuitEmpresa;

	public AuthAFIPData(String token, String hash, Long cuitEmpresa) {
		this.token = token;
		this.hash = hash;
		this.cuitEmpresa = cuitEmpresa;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Long getCuitEmpresa() {
		return cuitEmpresa;
	}

	public void setCuitEmpresa(Long cuitEmpresa) {
		this.cuitEmpresa = cuitEmpresa;
	}
}
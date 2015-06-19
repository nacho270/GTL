package ar.com.textillevel.modulos.fe;

public class AuthAFIPData {

	private String token;
	private String hash;

	public AuthAFIPData(String token, String hash) {
		this.token = token;
		this.hash = hash;
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
}
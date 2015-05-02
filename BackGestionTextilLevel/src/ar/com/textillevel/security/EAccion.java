package ar.com.textillevel.security;

public enum EAccion {

	_(-1),
	;

	private EAccion(int id) {
		this.id = id;
	}

	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	
}

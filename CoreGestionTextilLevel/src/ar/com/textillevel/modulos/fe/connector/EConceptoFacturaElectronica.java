package ar.com.textillevel.modulos.fe.connector;

public enum EConceptoFacturaElectronica {
	PRODUCTOS(1),
	SERVICIOS(2),
	PRODUCTOS_SERVICIOS(3);
	
	private EConceptoFacturaElectronica(int id){
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

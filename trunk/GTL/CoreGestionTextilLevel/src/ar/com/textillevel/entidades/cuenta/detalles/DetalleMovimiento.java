package ar.com.textillevel.entidades.cuenta.detalles;

//TODO: Mapear herencia en una sola tabla
public abstract class DetalleMovimiento {

	private Integer id;

	public DetalleMovimiento() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}

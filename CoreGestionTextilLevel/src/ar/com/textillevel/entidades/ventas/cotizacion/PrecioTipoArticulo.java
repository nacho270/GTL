package ar.com.textillevel.entidades.ventas.cotizacion;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "T_PRECIO_TIPO_ARTICULO")
public class PrecioTipoArticulo extends GrupoTipoArticulo implements Serializable {

	private static final long serialVersionUID = 1487171810237263464L;

	private Float precio;

	@Column(name = "A_PRECIO", nullable = false)
	public Float getPrecio() {
		return precio;
	}

	public void setPrecio(Float precio) {
		this.precio = precio;
	}
}

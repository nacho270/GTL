package ar.com.textillevel.entidades.ventas.cotizacion;

import ar.com.textillevel.entidades.ventas.articulos.GamaColor;
import ar.com.textillevel.entidades.ventas.articulos.GamaColorCliente;

public class PrecioGama {
	private Integer id;
	private GamaColorCliente gamaCliente; //nullable.Si no esta, se lee la default
	private GamaColor gamaDefault; //no nullable. Se lee de aca si la otra es null
	private Float precio;
}

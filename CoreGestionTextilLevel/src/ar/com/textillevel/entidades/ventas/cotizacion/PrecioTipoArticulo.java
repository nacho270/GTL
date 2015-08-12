package ar.com.textillevel.entidades.ventas.cotizacion;

import java.util.List;

import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;

public class PrecioTipoArticulo {

	private Integer id;
	private TipoArticulo tipoArticulo; //AP, AA, POL
	private List<PrecioRangoAnchoArticulo> precios;
}

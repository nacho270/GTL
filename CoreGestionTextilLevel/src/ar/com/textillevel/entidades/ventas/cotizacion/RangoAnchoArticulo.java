package ar.com.textillevel.entidades.ventas.cotizacion;


public abstract class RangoAnchoArticulo {
	private Integer id;
	private Float anchoMinimo; //nulleable, si el otro no es null
	private Float anchoMaximo; //nulleable, si el otro no es null
}

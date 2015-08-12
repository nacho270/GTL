package ar.com.textillevel.entidades.ventas.cotizacion;

import java.sql.Date;
import java.util.List;

public class VersionListaDePrecios {
	private Integer id;
	private Date inicioValidez;
	private List<DefinicionPrecio> precios;
}

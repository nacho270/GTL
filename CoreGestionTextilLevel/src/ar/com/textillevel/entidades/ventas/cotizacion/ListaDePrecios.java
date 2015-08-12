package ar.com.textillevel.entidades.ventas.cotizacion;

import java.util.List;

import ar.com.textillevel.entidades.gente.Cliente;

public class ListaDePrecios {
	private Integer id;
	private Cliente cliente; //si es null, es la lista default
	private List<VersionListaDePrecios> versiones;
/*	
 * Cliente
 * 	Version
 * 		Definicion
			- Tenido:
				gama
					AA
						1.5 precio
						2.0 precio
					AP
						1.5 precio
						3.0 precio
					POL
						2.0 precio
						4.0 precio
			- Estampado:
				Base
					AA
						1.5 precio
						2.0 precio
					AP
						1.5 precio
						3.0 precio
					POL
						2.0 precio
						4.0 precio	
			- Comun:
				AA
					1.5 precio
					2.0 precio
				AP
					1.5 precio
					3.0 precio
				POL
					2.0 precio
					4.0 precio
	*/
}

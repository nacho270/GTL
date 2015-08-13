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
				Rango ancho
					Articulo
						Gama Precio
			- Estampado:
				Rango ancho
					Articulo
						Base (Gama)
							Cantidad Colores + Cobertura
								1 a 3	 0 a 50			
													Precio
								1 a 3	 51 a 100		
													Precio
								4 a 5	 0 a 50			
													Precio
								4 a 5	 51 a 100	
													Precio
								6 a 8 	 0 a 50			
													Precio
								6 a 8	 51 a 100	
													Precio
			- Comun:
				Rango ancho
					Articulo
						precio
	*/
}

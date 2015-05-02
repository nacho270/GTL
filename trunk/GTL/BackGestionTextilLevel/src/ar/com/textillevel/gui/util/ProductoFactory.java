package ar.com.textillevel.gui.util;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.productos.Producto;

public class ProductoFactory {
	public static Producto createProducto(ETipoProducto tipoProducto){
		try {
			Class<?> clase = Class.forName(tipoProducto.getClase());
			return (Producto) clase.newInstance();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("NO SE DEFINIO LA CLASE " + tipoProducto.getClase());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
//		if(tipoProducto == ETipoProducto.CALANDRADO) return new ProductoCalandrado();
//		if(tipoProducto == ETipoProducto.DOBLADO) return new ProductoDoblado();
//		if(tipoProducto == ETipoProducto.ESTAMPADO)	return new ProductoEstampado();
//		if(tipoProducto == ETipoProducto.IMPERMEABILIZADO) return new ProductoImpermeabilizado();
//		if(tipoProducto == ETipoProducto.PLANCHADO)	return new ProductoPlanchado();
//		if(tipoProducto == ETipoProducto.SUAVIZADO)	return new ProductoSuavizado();
//		if(tipoProducto == ETipoProducto.TENIDO) return new ProductoTenido();
//		if(tipoProducto == ETipoProducto.TERMOFIJADO) return new ProductoTermofijado();
//		if(tipoProducto == ETipoProducto.SANDFORIZADO) return new ProductoSandforizado();
//		if(tipoProducto == ETipoProducto.REPROCESO_SIN_CARGO) return new ProductoReprocesoSinCargo();
//		if(tipoProducto == ETipoProducto.DEVOLUCION) return new ProductoDevolucion();
//		if(tipoProducto == ETipoProducto.LAVADO) return new ProductoLavado();
//		if(tipoProducto == ETipoProducto.DESMANCHADO) return new ProductoDesmanchado();
//		if(tipoProducto == ETipoProducto.DESMANCHADO_Y_LAVADO) return new ProductoDesmanchadoYLavado();
//		if(tipoProducto == ETipoProducto.REVISADO) return new ProductoRevisado();
		return null;
	}
}

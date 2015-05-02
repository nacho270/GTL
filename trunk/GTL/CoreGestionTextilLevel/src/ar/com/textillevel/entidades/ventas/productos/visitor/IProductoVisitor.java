package ar.com.textillevel.entidades.ventas.productos.visitor;

import ar.com.textillevel.entidades.ventas.productos.ProductoAprestado;
import ar.com.textillevel.entidades.ventas.productos.ProductoCalandrado;
import ar.com.textillevel.entidades.ventas.productos.ProductoDesmanchado;
import ar.com.textillevel.entidades.ventas.productos.ProductoDesmanchadoYLavado;
import ar.com.textillevel.entidades.ventas.productos.ProductoDevolucion;
import ar.com.textillevel.entidades.ventas.productos.ProductoDoblado;
import ar.com.textillevel.entidades.ventas.productos.ProductoEstampado;
import ar.com.textillevel.entidades.ventas.productos.ProductoImpermeabilizado;
import ar.com.textillevel.entidades.ventas.productos.ProductoLavado;
import ar.com.textillevel.entidades.ventas.productos.ProductoPlanchado;
import ar.com.textillevel.entidades.ventas.productos.ProductoReprocesoConCargo;
import ar.com.textillevel.entidades.ventas.productos.ProductoReprocesoSinCargo;
import ar.com.textillevel.entidades.ventas.productos.ProductoRevisado;
import ar.com.textillevel.entidades.ventas.productos.ProductoSandforizado;
import ar.com.textillevel.entidades.ventas.productos.ProductoSuavizado;
import ar.com.textillevel.entidades.ventas.productos.ProductoTenido;
import ar.com.textillevel.entidades.ventas.productos.ProductoTermofijado;

public interface IProductoVisitor {
	
	public void visit(ProductoCalandrado pc);
	public void visit(ProductoDoblado pd);
	public void visit(ProductoEstampado pe);
	public void visit(ProductoImpermeabilizado pi);
	public void visit(ProductoPlanchado pp);
	public void visit(ProductoSuavizado ps);
	public void visit(ProductoTenido pt);
	public void visit(ProductoTermofijado pterm);
	public void visit(ProductoSandforizado psand);
	public void visit(ProductoReprocesoSinCargo preprocsc);
	public void visit(ProductoDevolucion productoDevolucion);
	public void visit(ProductoRevisado productoRevisado);
	public void visit(ProductoDesmanchadoYLavado productoDesmanchadoYLavado);
	public void visit(ProductoDesmanchado productoDesmanchado);
	public void visit(ProductoLavado productoLavado);
	public void visit(ProductoReprocesoConCargo productoReprocesoConCargo);
	public void visit(ProductoAprestado productoAprestado);
	
}

package ar.com.textillevel.gui.modulos.abm.listaprecios.comun;

import java.awt.Frame;

import javax.swing.JPanel;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.cotizacion.DefinicionPrecio;
import ar.com.textillevel.entidades.ventas.cotizacion.PrecioTipoArticulo;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoComun;
import ar.com.textillevel.gui.modulos.abm.listaprecios.JDialogAgregarModificarDefinicionPrecios;
import ar.com.textillevel.gui.modulos.abm.listaprecios.PanelTablaRango;

public class JDialogAgregarModificarDefinicionPreciosComun extends JDialogAgregarModificarDefinicionPrecios<RangoAnchoComun, PrecioTipoArticulo> {

	private static final long serialVersionUID = -6851805146971694269L;
	
	public JDialogAgregarModificarDefinicionPreciosComun(Frame padre, Cliente cliente, ETipoProducto tipoProducto) {
		super(padre, cliente, tipoProducto);
	}

	public JDialogAgregarModificarDefinicionPreciosComun(Frame padre, Cliente cliente, ETipoProducto tipoProducto, DefinicionPrecio definicionAModificar) {
		super(padre, cliente, tipoProducto, definicionAModificar);
	}

	@Override
	protected JPanel createPanelDatosEspecificos() {
		return null;
	}

	@Override
	protected PanelTablaRango<RangoAnchoComun, PrecioTipoArticulo> createPanelTabla(JDialogAgregarModificarDefinicionPrecios<RangoAnchoComun, PrecioTipoArticulo> parent) {
		return null;
	}

	@Override
	protected void botonAgregarPresionado() {
		// TODO Auto-generated method stub
	}

	@Override
	protected boolean validar() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void setModoEdicionExtended(boolean modoEdicion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void limpiarDatosExtended() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void botonAgregarOrCancelarPresionado() {
		// TODO Auto-generated method stub
	}

	@Override
	public void setElemHojaSiendoEditado(PrecioTipoArticulo elemHoja, boolean modoEdicion) {
		// TODO Auto-generated method stub
	}

}
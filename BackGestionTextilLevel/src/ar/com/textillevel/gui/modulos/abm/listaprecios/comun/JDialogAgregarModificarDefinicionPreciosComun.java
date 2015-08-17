package ar.com.textillevel.gui.modulos.abm.listaprecios.comun;

import java.awt.Frame;

import javax.swing.JPanel;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.cotizacion.DefinicionPrecio;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoComun;
import ar.com.textillevel.gui.modulos.abm.listaprecios.JDialogAgregarModificarDefinicionPrecios;
import ar.com.textillevel.gui.modulos.abm.listaprecios.PanelTablaRango;

public class JDialogAgregarModificarDefinicionPreciosComun extends JDialogAgregarModificarDefinicionPrecios<RangoAnchoComun> {

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
	protected PanelTablaRango<RangoAnchoComun> createPanelTabla(JDialogAgregarModificarDefinicionPrecios<RangoAnchoComun> parent) {
		return null;
	}

	@Override
	protected void botonAgregarPresionado() {
		// TODO Auto-generated method stub
	}

}

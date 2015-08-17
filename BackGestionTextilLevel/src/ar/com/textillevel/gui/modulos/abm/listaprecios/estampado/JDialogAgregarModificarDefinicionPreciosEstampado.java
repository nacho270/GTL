package ar.com.textillevel.gui.modulos.abm.listaprecios.estampado;

import java.awt.Frame;

import javax.swing.JPanel;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.cotizacion.DefinicionPrecio;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoArticuloEstampado;
import ar.com.textillevel.gui.modulos.abm.listaprecios.JDialogAgregarModificarDefinicionPrecios;
import ar.com.textillevel.gui.modulos.abm.listaprecios.PanelTablaRango;

public class JDialogAgregarModificarDefinicionPreciosEstampado extends JDialogAgregarModificarDefinicionPrecios {

	private static final long serialVersionUID = -6851805146971694269L;
	
	public JDialogAgregarModificarDefinicionPreciosEstampado(Frame padre, Cliente cliente, ETipoProducto tipoProducto) {
		super(padre, cliente, tipoProducto);
	}

	public JDialogAgregarModificarDefinicionPreciosEstampado(Frame padre, Cliente cliente, ETipoProducto tipoProducto, DefinicionPrecio definicionAModificar) {
		super(padre, cliente, tipoProducto, definicionAModificar);
	}

	@Override
	protected JPanel createPanelDatosEspecificos() {
		return null;
	}

	@Override
	protected PanelTablaRango<RangoAnchoArticuloEstampado> createPanelTabla(JDialogAgregarModificarDefinicionPrecios parent) {
		return null;
	}
}

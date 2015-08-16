package ar.com.textillevel.gui.modulos.abm.listaprecios.estampado;

import java.awt.Frame;

import javax.swing.JPanel;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.cotizacion.DefinicionPrecio;
import ar.com.textillevel.entidades.ventas.cotizacion.GrupoTipoArticuloBaseEstampado;
import ar.com.textillevel.gui.modulos.abm.listaprecios.JDialogAgregarModificarDefinicionPreciosV2;
import ar.com.textillevel.gui.modulos.abm.listaprecios.PanelTablaRango;

public class JDialogAgregarModificarDefinicionPreciosEstampado extends JDialogAgregarModificarDefinicionPreciosV2 {

	private static final long serialVersionUID = -6851805146971694269L;
	
	public JDialogAgregarModificarDefinicionPreciosEstampado(Frame padre, ETipoProducto tipoProducto) {
		super(padre, tipoProducto);
	}

	public JDialogAgregarModificarDefinicionPreciosEstampado(Frame padre, ETipoProducto tipoProducto, DefinicionPrecio definicionAModificar) {
		super(padre, tipoProducto, definicionAModificar);
	}

	@Override
	protected JPanel createPanelDatosEspecificos() {
		return null;
	}

	@Override
	protected PanelTablaRango<GrupoTipoArticuloBaseEstampado> createPanelTabla() {
		return null;
	}
}

package ar.com.textillevel.gui.modulos.abm.listaprecios.tenido;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.cotizacion.DefinicionPrecio;
import ar.com.textillevel.entidades.ventas.cotizacion.GrupoTipoArticuloGama;
import ar.com.textillevel.gui.modulos.abm.listaprecios.JDialogAgregarModificarDefinicionPreciosV2;
import ar.com.textillevel.gui.modulos.abm.listaprecios.PanelTablaRango;
import ar.com.textillevel.gui.util.GenericUtils;

public class JDialogAgregarModificarDefinicionPreciosTenido extends JDialogAgregarModificarDefinicionPreciosV2 {

	private static final long serialVersionUID = -6851805146971694269L;
	
	private JComboBox cmbGama;
	
	public JDialogAgregarModificarDefinicionPreciosTenido(Frame padre, ETipoProducto tipoProducto) {
		super(padre, tipoProducto);
	}

	public JDialogAgregarModificarDefinicionPreciosTenido(Frame padre, ETipoProducto tipoProducto, DefinicionPrecio definicionAModificar) {
		super(padre, tipoProducto, definicionAModificar);
	}
	
	@Override
	protected JPanel createPanelDatosEspecificos() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(new JLabel("Gama: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 0, 0));
		panel.add(getCmbGama(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1));
		return panel;
	}

	public JComboBox getCmbGama() {
		if (cmbGama == null) {
			cmbGama = new JComboBox();
		}
		return cmbGama;
	}

	@Override
	protected PanelTablaRango<GrupoTipoArticuloGama> createPanelTabla() {
		return new PanelTablaRangoTenido(JDialogAgregarModificarDefinicionPreciosTenido.this);
	}
}

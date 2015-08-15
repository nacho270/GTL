package ar.com.textillevel.gui.modulos.abm.listaprecios.tenido;

import java.util.List;

import javax.swing.JDialog;

import ar.com.textillevel.entidades.ventas.cotizacion.GrupoTipoArticuloGama;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAncho;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoArticuloTenido;
import ar.com.textillevel.gui.modulos.abm.listaprecios.JDialogAgregarModificarRangoAncho;
import ar.com.textillevel.gui.modulos.abm.listaprecios.PanelTablaRango;

public class JDialogAgregarModificarRangoAnchoTenido extends JDialogAgregarModificarRangoAncho {

	private static final long serialVersionUID = 450322898427792643L;

	public JDialogAgregarModificarRangoAnchoTenido(JDialog padre, List<RangoAncho> rangosActuales) {
		super(padre, rangosActuales);
	}

	public JDialogAgregarModificarRangoAnchoTenido(JDialog padre, List<RangoAncho> rangosActuales, RangoAncho rango) {
		super(padre, rangosActuales, rango);
	}

	@Override
	protected RangoAncho crearNuevoRango() {
		return new RangoAnchoArticuloTenido();
	}

	@Override
	protected PanelTablaRango<GrupoTipoArticuloGama> crearPanelRango() {
		return new PanelTablaRangoTenido(JDialogAgregarModificarRangoAnchoTenido.this);
	}
}

package ar.com.textillevel.gui.modulos.abm.listaprecios.estampado;

import java.util.List;

import javax.swing.JDialog;

import ar.com.textillevel.entidades.ventas.cotizacion.RangoAncho;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoArticuloEstampado;
import ar.com.textillevel.gui.modulos.abm.listaprecios.JDialogAgregarModificarRangoAncho;
import ar.com.textillevel.gui.modulos.abm.listaprecios.PanelTablaRango;

public class JDialogAgregarModificarRangoAnchoEstampado extends JDialogAgregarModificarRangoAncho {

	private static final long serialVersionUID = -2507367044138805696L;

	public JDialogAgregarModificarRangoAnchoEstampado(JDialog padre, List<RangoAncho> rangosActuales) {
		super(padre, rangosActuales);
	}
	
	public JDialogAgregarModificarRangoAnchoEstampado(JDialog padre, List<RangoAncho> rangosActuales, RangoAncho rango) {
		super(padre, rangosActuales, rango);
	}

	@Override
	protected RangoAncho crearNuevoRango() {
		return new RangoAnchoArticuloEstampado();
	}

	@Override
	protected PanelTablaRango<RangoAnchoArticuloEstampado> crearPanelRango() {
		// TODO Auto-generated method stub
		return null;
	}
}

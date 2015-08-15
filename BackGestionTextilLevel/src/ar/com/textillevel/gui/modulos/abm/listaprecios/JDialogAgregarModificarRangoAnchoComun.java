package ar.com.textillevel.gui.modulos.abm.listaprecios;

import java.util.List;

import javax.swing.JDialog;

import ar.com.textillevel.entidades.ventas.cotizacion.RangoAncho;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoComun;

public class JDialogAgregarModificarRangoAnchoComun extends JDialogAgregarModificarRangoAncho {

	private static final long serialVersionUID = -5172144162617886910L;

	public JDialogAgregarModificarRangoAnchoComun(JDialog padre, List<RangoAncho> rangosActuales) {
		super(padre, rangosActuales);
	}

	public JDialogAgregarModificarRangoAnchoComun(JDialog padre, List<RangoAncho> rangosActuales, RangoAncho rango) {
		super(padre, rangosActuales, rango);
	}

	@Override
	protected RangoAncho crearNuevoRango() {
		return new RangoAnchoComun();
	}

	@Override
	protected PanelTablaRango<RangoAnchoComun> crearPanelRango() {
		// TODO Auto-generated method stub
		return null;
	}
}

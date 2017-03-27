package ar.com.textillevel.gui.acciones.odt.componentes;

import java.awt.event.MouseEvent;

import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.acciones.odt.JDialogVisualizarDetalleODT;
import ar.com.textillevel.gui.util.controles.LinkableLabel;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.util.ODTCodigoHelper;

public class ODTLinkeableLabel extends LinkableLabel {

	private static final long serialVersionUID = -8690402922462933808L;

	private OrdenDeTrabajo odt;

	public ODTLinkeableLabel(OrdenDeTrabajo odt) {
		super("ORDEN DE TRABAJO N° " + ODTCodigoHelper.getInstance().formatCodigo(odt.getCodigo()));
		this.odt = odt;
	}

	@Override
	public void labelClickeada(MouseEvent e) {
		if (e.getClickCount() == 1 && odt!=null) {
			JDialogVisualizarDetalleODT v = new JDialogVisualizarDetalleODT(null, odt);
			GuiUtil.centrar(v);
			v.setVisible(true);
		}
	}

	public void setODT(OrdenDeTrabajo odt) {
		this.odt = odt;
		if(odt != null) {
			setText("ORDEN DE TRABAJO N° " + ODTCodigoHelper.getInstance().formatCodigo(odt.getCodigo()));
			refreshLabel();
		}
	}

}
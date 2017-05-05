package ar.com.textillevel.gui.acciones.odt.componentes;

import java.awt.event.MouseEvent;

import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.acciones.odt.JDialogVisualizarDetalleODT;
import ar.com.textillevel.gui.util.controles.LinkableLabel;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;
import ar.com.textillevel.util.ODTCodigoHelper;

public class ODTLinkeableLabel extends LinkableLabel {

	private static final long serialVersionUID = -8690402922462933808L;

	private String titulo;
	private OrdenDeTrabajo odt;

	public ODTLinkeableLabel(OrdenDeTrabajo odt, String titulo) {
		super(titulo + ODTCodigoHelper.getInstance().formatCodigo(odt.getCodigo()));
		this.odt = odt;
		this.titulo = titulo;
	}

	@Override
	public void labelClickeada(MouseEvent e) {
		if (e.getClickCount() == 1 && odt!=null) {
			OrdenDeTrabajo ordenDeTrabajo = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class).getByIdEager(odt.getId());
			JDialogVisualizarDetalleODT v = new JDialogVisualizarDetalleODT(null, ordenDeTrabajo);
			GuiUtil.centrar(v);
			v.setVisible(true);
		}
	}

	public void setODT(OrdenDeTrabajo odt) {
		this.odt = odt;
		if(odt != null) {
			setText(titulo + ODTCodigoHelper.getInstance().formatCodigo(odt.getCodigo()));
			refreshLabel();
		}
	}

}
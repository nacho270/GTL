package ar.com.textillevel.gui.acciones;

import java.awt.event.MouseEvent;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.facade.api.remote.FacturaFacadeRemote;
import ar.com.textillevel.gui.util.controles.LinkableLabel;
import ar.com.textillevel.util.GTLBeanFactory;

public class FacturaLinkableLabel extends LinkableLabel {

	private static final long serialVersionUID = -8690402922462933808L;

	private Factura factura;

	public FacturaLinkableLabel() {
		super("x");
	}

	@Override
	public void labelClickeada(MouseEvent e) {
		if (e.getClickCount() == 1 && factura!=null) {
			factura = GTLBeanFactory.getInstance().getBean2(FacturaFacadeRemote.class).getByIdEager(factura.getId());
			JDialogCargaFactura d = new JDialogCargaFactura(null, factura, true);
			GuiUtil.centrar(d);
			d.setVisible(true);
		}
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
		if(factura != null) {
			String suc = factura.getNroSucursal() == null ? null : StringUtil.fillLeftWithZeros(String.valueOf(factura.getNroSucursal()), 4);
			String nro = StringUtil.fillLeftWithZeros(String.valueOf(factura.getNroFactura()), 8);
			setTexto(suc == null ? nro : (suc + "- " + nro));
			refreshLabel();
		}
	}

}
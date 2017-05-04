package ar.com.textillevel.gui.acciones.remitosalida;

import java.awt.event.MouseEvent;

import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.gui.util.controles.LinkableLabel;
import main.acciones.facturacion.OperacionSobreRemitoSalidaHandler;

public class RemitoSalidaLinkeableLabel extends LinkableLabel {

	private static final long serialVersionUID = -8690402922462933808L;

	private RemitoSalida remito;

	public RemitoSalidaLinkeableLabel() {
		super("x");
	}

	@Override
	public void labelClickeada(MouseEvent e) {
		if (e.getClickCount() == 1 && remito!=null) {
			OperacionSobreRemitoSalidaHandler consultaREHandler = new OperacionSobreRemitoSalidaHandler(null, remito, true);
			consultaREHandler.showRemitoEntradaDialog();
		}
	}

	public void setRemito(RemitoSalida remito) {
		this.remito = remito;
		if(remito != null) {
			setTexto(String.valueOf(remito.getNroRemito()));
			refreshLabel();
		}
	}

}
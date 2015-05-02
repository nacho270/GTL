package ar.com.textillevel.gui.acciones;

import java.awt.event.MouseEvent;
import main.acciones.facturacion.OperacionSobreRemitoEntradaHandler;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.gui.util.controles.LinkableLabel;

public class RemitoEntradaLinkeableLabel extends LinkableLabel {

	private static final long serialVersionUID = -8690402922462933808L;

	private RemitoEntrada remito;

	public RemitoEntradaLinkeableLabel() {
		super("x");
	}

	@Override
	public void labelClickeada(MouseEvent e) {
		if (e.getClickCount() == 1 && remito!=null) {
			OperacionSobreRemitoEntradaHandler consultaREHandler = new OperacionSobreRemitoEntradaHandler(null, remito, true);
			consultaREHandler.showRemitoEntradaDialog();
		}
	}

	public void setRemito(RemitoEntrada remito) {
		this.remito = remito;
		if(remito != null) {
			setTexto(String.valueOf(remito.getNroRemito()));
			refreshLabel();
		}
	}

}
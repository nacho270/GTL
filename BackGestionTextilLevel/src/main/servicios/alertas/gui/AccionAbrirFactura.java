package main.servicios.alertas.gui;

import javax.swing.JButton;

import main.servicios.alertas.AccionNotificacion;

public class AccionAbrirFactura extends JButton implements AccionNotificacion<Integer> {

	private static final long serialVersionUID = 8429099807596334619L;

	public AccionAbrirFactura() {
		super("Abrir factura");
	}

	@Override
	public void ejecutar(Integer idFactura) {
//		Factura factura = GTLBeanFactory.getInstance().getBean2(FacturaFacadeRemote.class).getByIdEager(idFactura);
//		if (factura == null) {
//			FWJOptionPane.showErrorMessage(getRootPane(), "No se ha encont", title);
//		}
	}
}

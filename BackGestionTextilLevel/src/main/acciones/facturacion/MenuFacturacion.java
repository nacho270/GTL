package main.acciones.facturacion;

import java.awt.Frame;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;

import ar.com.fwcommon.templates.main.menu.FWJMenu;

public class MenuFacturacion extends FWJMenu {

	private static final long serialVersionUID = 3657289797505567141L;

	private JMenuItem menuItemAgregarRemitoEntrada;
	private JMenuItem menuItemAgregarRemitoSalida;
	private JMenuItem menuItemAgregarFactura;
	private JMenuItem menuItemAgregarFacturaSinRemito;
	private JMenuItem menuItemAgregarCorreccion;
	private JMenuItem menuItemAgregarRecibo;
	private FWJMenu menuConsultas;
	private final Frame frame;

	public MenuFacturacion(Frame frame) {
		super("Facturación", 'F');
		this.frame = frame;
		setMnemonic(KeyEvent.VK_F);
		add(getMenuItemAgregarRemitoEntrada());
		add(getMenuItemAgregarRemitoSalida());
		add(getMenuItemAgregarFactura());
		add(getMenuItemAgregarFacturaSinRecibo());
		add(getMenuItemAgregarCorreccion());
		add(getMenuItemAgregarRecibo());
		add(getMenuConsultas());
	}

	private JMenuItem getMenuItemAgregarRemitoEntrada() {
		if(menuItemAgregarRemitoEntrada == null) {
			menuItemAgregarRemitoEntrada = new JMenuItem(new AgregarRemitoEntradaAction(frame));
			menuItemAgregarRemitoEntrada.setText("Agregar Remito de Entrada");
		}
		return menuItemAgregarRemitoEntrada;
	}

	public JMenuItem getMenuItemAgregarRemitoSalida() {
		if(menuItemAgregarRemitoSalida == null) {
			menuItemAgregarRemitoSalida = new JMenuItem(new AgregarRemitoSalidaAction(frame));
			menuItemAgregarRemitoSalida.setText("Agregar Remito de Salida");
		}
		return menuItemAgregarRemitoSalida;
	}

	public JMenuItem getMenuItemAgregarFactura() {
		if(menuItemAgregarFactura == null) {
			menuItemAgregarFactura = new JMenuItem(new AgregarFacturaAction(frame));
			menuItemAgregarFactura.setText("Agregar factura");
		}
		return menuItemAgregarFactura;
	}
	
	public JMenuItem getMenuItemAgregarFacturaSinRecibo() {
		if(menuItemAgregarFacturaSinRemito == null) {
			menuItemAgregarFacturaSinRemito = new JMenuItem(new AccionAgregarFacturaSinRemitoAction(frame));
			menuItemAgregarFacturaSinRemito.setText("Agregar factura sin recibo");
		}
		return menuItemAgregarFacturaSinRemito;
	}
	
	public JMenuItem getMenuItemAgregarCorreccion() {
		if(menuItemAgregarCorreccion == null) {
			menuItemAgregarCorreccion = new JMenuItem(new AgregarCorreccionAction(frame));
			menuItemAgregarCorreccion.setText("Agregar nota de débito o crédito");
		}
		return menuItemAgregarCorreccion;
	}

	public JMenuItem getMenuItemAgregarRecibo() {
		if(menuItemAgregarRecibo == null) {
			menuItemAgregarRecibo = new JMenuItem(new AgregarReciboAction(frame));
			menuItemAgregarRecibo.setText("Agregar Recibo");
		}
		return menuItemAgregarRecibo;
	}

	public FWJMenu getMenuConsultas(){
		if(menuConsultas == null){
			menuConsultas = new FWJMenu("Consultas");
			JMenuItem menuConsultarFactura = new JMenuItem(new ConsultarFacturaAction(frame));
			menuConsultarFactura.setText("Consultar factura");
			JMenuItem menuConsultarCorrecciones = new JMenuItem(new ConsultarCorreccionesAction(frame));
			menuConsultarCorrecciones.setText("Consultar notas de crédito y débito");
			JMenuItem menuConsultarRemitoEntrada = new JMenuItem(new ConsultarRemitoEntradaAction(frame));
			menuConsultarRemitoEntrada.setText("Consultar remito de entrada");
			JMenuItem menuConsultarRemitoSalida = new JMenuItem(new ConsultarRemitoSalidaAction(frame));
			menuConsultarRemitoSalida.setText("Consultar remito de salida");
			JMenuItem menuConsultarRecibo = new JMenuItem(new ConsultarReciboAction(frame));
			menuConsultarRecibo.setText("Consultar recibo");
			menuConsultas.add(menuConsultarFactura);
			menuConsultas.add(menuConsultarCorrecciones);
			menuConsultas.add(menuConsultarRemitoEntrada);
			menuConsultas.add(menuConsultarRemitoSalida);
			menuConsultas.add(menuConsultarRecibo);
		}
		return menuConsultas;
	}

}

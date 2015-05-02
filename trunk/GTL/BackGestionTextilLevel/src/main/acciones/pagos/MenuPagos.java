package main.acciones.pagos;

import java.awt.Frame;

import javax.swing.JMenuItem;

import ar.clarin.fwjava.templates.main.menu.CLJMenu;

public class MenuPagos extends CLJMenu{

	private static final long serialVersionUID = 7893157749927984476L;

	private final Frame frame;
	private JMenuItem itemAgregarOrdenDePago;
	private JMenuItem itemAgregarOrdenDePagoAPersonas;
	private JMenuItem itemConsultarOrdenDePago;
	private JMenuItem itemAgregarOrdenDeDeposito;
	private JMenuItem itemConsultarOrdenDeDeposito;
	private JMenuItem itemAgregarFacturaPersona;
	private CLJMenu menuConsultas;

	public MenuPagos(Frame frame) {
		super("Pagos", 'P');
		this.frame = frame;
		this.add(getItemAgregarOrdenDePago());
		this.add(getItemAgregarOrdenDePagoAPersonas());
		this.add(getMenuConsultas());
	}

	public JMenuItem getItemAgregarOrdenDePago() {
		if(itemAgregarOrdenDePago == null){
			itemAgregarOrdenDePago = new JMenuItem(new CargarOrdenDePagoAction(frame));
			itemAgregarOrdenDePago.setText("Cargar orden de pago");
		}
		return itemAgregarOrdenDePago;
	}
	
	public JMenuItem getItemAgregarOrdenDePagoAPersonas() {
		if(itemAgregarOrdenDePagoAPersonas == null){
			itemAgregarOrdenDePagoAPersonas = new JMenuItem(new CargarOrdenDePagoAPersonaAction(frame));
			itemAgregarOrdenDePagoAPersonas.setText("Cargar orden de pago a persona");
		}
		return itemAgregarOrdenDePagoAPersonas;
	}
	
	public JMenuItem getItemConsultarOrdenDePago() {
		if(itemConsultarOrdenDePago == null){
			itemConsultarOrdenDePago = new JMenuItem(new ConsultarOrdenDePagoAction(frame));
			itemConsultarOrdenDePago.setText("Consultar orden de pago");
		}
		return itemConsultarOrdenDePago;
	}

	
	public CLJMenu getMenuConsultas() {
		if(menuConsultas == null){
			menuConsultas = new CLJMenu("Consultas");
			menuConsultas.add(getItemConsultarOrdenDePago());
			menuConsultas.add(getItemConsultarOrdenDeDeposito());
		}
		return menuConsultas;
	}

	public JMenuItem getItemAgregarOrdenDeDeposito() {
		if(itemAgregarOrdenDeDeposito==null){
			itemAgregarOrdenDeDeposito = new JMenuItem(new CargarOrdenDeDepositoAction(frame));
			itemAgregarOrdenDeDeposito.setText("Cargar órden de deposito");
		}
		return itemAgregarOrdenDeDeposito;
	}
	
	public JMenuItem getItemConsultarOrdenDeDeposito() {
		if(itemConsultarOrdenDeDeposito==null){
			itemConsultarOrdenDeDeposito = new JMenuItem(new CargarOrdenDeDepositoAction(frame));
			itemConsultarOrdenDeDeposito.setText("Consultar órden de deposito");
		}
		return itemConsultarOrdenDeDeposito;
	}

	public JMenuItem getItemAgregarFacturaPersona() {
		if(itemAgregarFacturaPersona==null){
			itemAgregarFacturaPersona = new JMenuItem(new CargarFacturaDePersonaAction(frame));
			itemAgregarFacturaPersona.setText("Cargar factura a persona");
		}
		return itemAgregarFacturaPersona;
	}
}

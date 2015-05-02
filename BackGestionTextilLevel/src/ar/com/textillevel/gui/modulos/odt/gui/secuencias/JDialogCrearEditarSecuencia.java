package ar.com.textillevel.gui.modulos.odt.gui.secuencias;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.gui.util.controles.PanelBusquedaClienteMinimal;
import ar.com.textillevel.modulos.odt.entidades.secuencia.generica.SecuenciaTipoProducto;

public class JDialogCrearEditarSecuencia extends JDialog {

	private static final long serialVersionUID = 5881443862030005534L;

	private static final int MAX_LONG_NOMBRE = 50;
	
	private CLJTextField txtNombreSecuencia;
	private PanelBusquedaClienteMinimal panelBusquedaCliente;
	private PanelPasosSecuencia panelPasos;
	private JButton btnAceptar;
	private JButton btnCancelar;

	private SecuenciaTipoProducto secuenciaActual;
	private boolean acepto;

	public JDialogCrearEditarSecuencia(Frame owner, ETipoProducto tipoProducto, Cliente cliente) {
		super(owner);
		setSecuenciaActual(new SecuenciaTipoProducto());
		getSecuenciaActual().setTipoProducto(tipoProducto);
		getSecuenciaActual().setCliente(cliente);
		if(cliente!=null){
			getPanelBusquedaCliente().setCliente(cliente);
		}
		setUpComponentes();
		setUpScreen();
	}

	public JDialogCrearEditarSecuencia(Frame frame, SecuenciaTipoProducto secuencia) {
		super(frame);
		setSecuenciaActual(secuencia);
		setUpComponentes();
		setUpScreen();
		loadData();
	}

	private void loadData() {
		getPanelBusquedaCliente().setCliente(getSecuenciaActual().getCliente());
	}

	private void setUpScreen() {
		setTitle("Crear/Editar Secuencia");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(new Dimension(650, 370));
		setModal(true);
		setResizable(false);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelBusquedaCliente(),BorderLayout.NORTH);
		add(getPanelPasos(),BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}

	private JPanel getPanelSur(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnAceptar());
		panel.add(getBtnCancelar());
		return panel;
	}
	
	private SecuenciaTipoProducto getSecuenciaActual() {
		return secuenciaActual;
	}
	
	public SecuenciaTipoProducto getSecuenciaFinal() {
		return getPanelPasos().getSecuenciaActual();
	}

	private void setSecuenciaActual(SecuenciaTipoProducto secuenciaActual) {
		this.secuenciaActual = secuenciaActual;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public CLJTextField getTxtNombreSecuencia() {
		if(txtNombreSecuencia == null){
			txtNombreSecuencia = new CLJTextField(MAX_LONG_NOMBRE);
		}
		return txtNombreSecuencia;
	}

	public PanelPasosSecuencia getPanelPasos() {
		if(panelPasos == null){
			panelPasos = new PanelPasosSecuencia(JDialogCrearEditarSecuencia.this,getSecuenciaActual(),true);
		}
		return panelPasos;
	}

	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(getPanelPasos().validar()){
//						setSecuenciaActual(getPanelPasos().getSecuenciaActual());
						setAcepto(true);
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}

	public JButton getBtnCancelar() {
		if(btnCancelar == null){
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setAcepto(false);
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	public PanelBusquedaClienteMinimal getPanelBusquedaCliente() {
		if(panelBusquedaCliente == null){
			panelBusquedaCliente = new PanelBusquedaClienteMinimal() {
				
				private static final long serialVersionUID = -6112295166996091740L;

				@Override
				protected void clienteEncontrado(Cliente cliente) {
					if(cliente.getNroCliente().intValue() == 1){
						getSecuenciaActual().setCliente(null);
						limpiar();
					}else{
						getSecuenciaActual().setCliente(cliente);
					}
				}
				
				@Override
				protected void botonLimpiarPresionado() {
					getSecuenciaActual().setCliente(null);
				}
			};
		}
		return panelBusquedaCliente;
	}
}

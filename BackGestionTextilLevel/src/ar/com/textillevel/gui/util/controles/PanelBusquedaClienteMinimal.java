package ar.com.textillevel.gui.util.controles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.boss.BossEstilos;
import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public abstract class PanelBusquedaClienteMinimal extends JPanel {

	private static final long serialVersionUID = 6987748755450833777L;

	private CLJNumericTextField txtNroCliente;
	private JButton btnBuscar;
	private JButton btnClear;
	private JLabel lblResultado;

	private ClienteFacadeRemote clienteFacade;
	
	private static final String TEXTO_DEFAULT_CLIENTE_01 = "<html><b style='color:red;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;TEXTIL LEVEL&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></html>";

	public PanelBusquedaClienteMinimal() {
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		add(new JLabel("Cliente: "));
		add(getTxtNroCliente());
		add(getBtnBuscar());
		add(getBtnClear());
		add(getLblResultado());
	}

	public CLJNumericTextField getTxtNroCliente() {
		if (txtNroCliente == null) {
			txtNroCliente = new CLJNumericTextField();
			txtNroCliente.setPreferredSize(new Dimension(100, 20));
			txtNroCliente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getBtnBuscar().doClick();
				}
			});
		}
		return txtNroCliente;
	}

	public JButton getBtnBuscar() {
		if (btnBuscar == null) {
			btnBuscar = BossEstilos.createButton("ar/com/textillevel/imagenes/b_buscar_moderno.png", "ar/com/textillevel/imagenes/b_buscar_moderno_des.png");
			btnBuscar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getTxtNroCliente().getValueWithNull() == null) {
						CLJOptionPane.showErrorMessage(PanelBusquedaClienteMinimal.this, "Debe ingresar el número de cliente", "Error");
						return;
					}
					Integer nroCliente = getTxtNroCliente().getValue();
					Cliente cliente = getClienteFacade().getClienteByNumero(nroCliente);
					if (cliente == null) {
						clienteNoEncontrado(nroCliente);
					} else {
						getLblResultado().setText("<html><b style='color:red;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+cliente.getDescripcionResumida()+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></html>");
						clienteEncontrado(cliente);
					}
				}
			});
		}
		return btnBuscar;
	}

	protected void clienteNoEncontrado(Integer nroCliente) {
		CLJOptionPane.showErrorMessage(PanelBusquedaClienteMinimal.this, "No se ha encontrado al cliente " + nroCliente +".", "Error");
	}

	public JButton getBtnClear() {
		if (btnClear == null) {
			btnClear = new JButton("Limpiar");
			btnClear.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					getLblResultado().setText(TEXTO_DEFAULT_CLIENTE_01);
					getTxtNroCliente().setText("");
					botonLimpiarPresionado();
				}
			});
		}
		return btnClear;
	}

	public ClienteFacadeRemote getClienteFacade() {
		if (clienteFacade == null) {
			clienteFacade = GTLBeanFactory.getInstance().getBean2(ClienteFacadeRemote.class);
		}
		return clienteFacade;
	}
	
	public JLabel getLblResultado() {
		if(lblResultado == null){
			lblResultado = new JLabel(TEXTO_DEFAULT_CLIENTE_01);
			lblResultado.setBorder(BorderFactory.createLineBorder(Color.RED.darker()));
		}
		return lblResultado;
	}

	public void setCliente(Cliente cl){
		if(cl!=null){
			getTxtNroCliente().setValue(cl.getNroCliente().longValue());
			getLblResultado().setText("<html><b style='color:red;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+cl.getDescripcionResumida()+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></html>");
		}
	}
	
	public void limpiar(){
		setCliente(null);
		getLblResultado().setText(TEXTO_DEFAULT_CLIENTE_01);
		getTxtNroCliente().setText("");
	}
	
	protected abstract void botonLimpiarPresionado();

	protected abstract void clienteEncontrado(Cliente cliente);
	
}
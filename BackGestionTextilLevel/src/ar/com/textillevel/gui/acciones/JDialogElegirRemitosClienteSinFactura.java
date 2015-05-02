package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ar.clarin.fwjava.componentes.CLCheckBoxList;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.facade.api.remote.RemitoSalidaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogElegirRemitosClienteSinFactura extends JDialog {

	private static final long serialVersionUID = 6510608624599552621L;

	private CLJTextField txtNombreCliente;
	private CLCheckBoxList<RemitoSalida> chkListRemitos;
	private JButton btnAceptar;
	private JButton btnSalir;

	private List<RemitoSalida> remitosSeleccionados;
	private boolean acepto;
	private RemitoSalidaFacadeRemote remitoFacade;
	private Cliente cliente;

	public JDialogElegirRemitosClienteSinFactura(Frame padre, Cliente cliente) {
		super(padre);
		setCliente(cliente);
		setUpComponentes();
		setUpScreen();
		cargarRemitos();
	}

	private void cargarRemitos() {
		List<RemitoSalida> remitos = getRemitoFacade().getRemitosSalidaSinFacturaPorCliente(getCliente());
		if(remitos!=null){
			getChkListRemitos().setValues(remitos.toArray(new RemitoSalida[remitos.size()]));
			int[] selectedIndices = getChkListRemitos().getSelectedIndices();
			for(int index : selectedIndices) {
				getChkListRemitos().setSelectedIndex(index, false);
			}
		}else{
			CLJOptionPane.showErrorMessage(this, "No se han encontrado remitos sin factura", "Error");
			dispose();
		}
	}

	private void setUpScreen() {
		setTitle("Seleccionar remitos de salida");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setModal(true);
		setSize(new Dimension(350, 400));
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelNorte(),BorderLayout.NORTH);
		add(getJspCheck(),BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}
	
	private JScrollPane getJspCheck(){
		return new JScrollPane(getChkListRemitos(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}

	private JPanel getPanelNorte(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(new JLabel("Cliente: "));
		panel.add(getTxtNombreCliente());
		return panel;
	}

	private JPanel getPanelSur(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnAceptar());
		panel.add(getBtnSalir());
		return panel;
	}

	
	public CLJTextField getTxtNombreCliente() {
		if (txtNombreCliente == null) {
			txtNombreCliente = new CLJTextField(getCliente().getRazonSocial());
			txtNombreCliente.setEditable(false);
			txtNombreCliente.setPreferredSize(new Dimension(230, 20));
		}
		return txtNombreCliente;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public CLCheckBoxList<RemitoSalida> getChkListRemitos() {
		if (chkListRemitos == null) {
			chkListRemitos = new CLCheckBoxList<RemitoSalida>();
		}
		return chkListRemitos;
	}

	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(getChkListRemitos().getSelectedCount() > 0){
						int[] selectedIndices = getChkListRemitos().getSelectedIndices();
						List<RemitoSalida> lista = new ArrayList<RemitoSalida>();
						for(int index : selectedIndices) {
							lista.add((RemitoSalida)getChkListRemitos().getItemAt(index));
						}
						setRemitosSeleccionados(lista);
						setAcepto(true);
						dispose();
					}else{
						CLJOptionPane.showErrorMessage(JDialogElegirRemitosClienteSinFactura.this, "Debe elegir al menos un remito", "Error");
					}
				}
			});
		}
		return btnAceptar;
	}

	public JButton getBtnSalir() {
		if(btnSalir == null){
			btnSalir = new JButton("Cancelar");
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnSalir;
	}

	public RemitoSalidaFacadeRemote getRemitoFacade() {
		if(remitoFacade == null){
			remitoFacade = GTLBeanFactory.getInstance().getBean2(RemitoSalidaFacadeRemote.class);
		}
		return remitoFacade;
	}

	
	public List<RemitoSalida> getRemitosSeleccionados() {
		return remitosSeleccionados;
	}

	
	public void setRemitosSeleccionados(List<RemitoSalida> remitosSeleccionados) {
		this.remitosSeleccionados = remitosSeleccionados;
	}

	
	public boolean isAcepto() {
		return acepto;
	}

	
	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}
}

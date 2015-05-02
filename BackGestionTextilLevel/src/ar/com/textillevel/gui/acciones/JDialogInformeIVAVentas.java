package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.CLTxtComboBoxBusqueda;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.NumUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.enums.ETipoFactura;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;
import ar.com.textillevel.gui.util.controles.LinkableLabel;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogInformeIVAVentas extends JDialog {

	private static final long serialVersionUID = -5312992576670286259L;

	private boolean acepto;

	private Date fechaDesde;
	private Date fechaHasta;
	private ETipoFactura tipoFactura;
	private Cliente cliente;
	
	private JButton btnCancelar;
	private JButton btnAceptar;
	private PanelDatePicker panelFechaDesde;
	private PanelDatePicker panelFechaHasta;
	private JComboBox cmbTipoFactura;
	private CLJTextField txtNroCliente;
	private LinkableLabel lblElegirCliente;
	private CLTxtComboBusquedaUsuarioByCodigo comboBusquedaUsuario;
	
	private ClienteFacadeRemote clienteFacade;
	
	private Frame frame;
	
	public JDialogInformeIVAVentas(Frame padre) {
		super(padre);
		this.frame = padre; 
		setAcepto(false);
		setUpScreen();
		setUpComponentes();
	}

	private void setUpComponentes() {
		add(getPanelCentral(), BorderLayout.CENTER);
		add(getPanelBotones(), BorderLayout.SOUTH);
	}

	private JPanel getPanelBotones() {
		JPanel pan = new JPanel();
		pan.setLayout(new FlowLayout(FlowLayout.CENTER,5,2));
		pan.add(getBtnAceptar());
		pan.add(getBtnCancelar());
		return pan;
	}

	private JPanel getPanelCentral() {
		JPanel pan = new JPanel();
		pan.setLayout(new FlowLayout(FlowLayout.CENTER,5,2));
		pan.add(getPanelFechaDesde());
		pan.add(getPanelFechaHasta());
		pan.add(new JLabel("Tipo de factura"));
		pan.add(getCmbTipoFactura());
		pan.add(getPanelElegirCliente());
		return pan;
	}

	private void setUpScreen() {
		setTitle("Informe de IVA - Ventas");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(600, 150));
		setResizable(true);
		setModal(true);
		GuiUtil.centrarEnPadre(this);
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public ETipoFactura getTipoFactura() {
		return tipoFactura;
	}

	public void setTipoFactura(ETipoFactura tipoFactura) {
		this.tipoFactura = tipoFactura;
	}

	public JButton getBtnCancelar() {
		if(btnCancelar == null){
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(getPanelFechaDesde().getDate() == null || getPanelFechaHasta().getDate() ==null){
						CLJOptionPane.showErrorMessage(JDialogInformeIVAVentas.this, "Alguna de las fechas son inválidas", "Error");
						return;
					}
					if(!getPanelFechaDesde().getDate().after(getPanelFechaHasta().getDate())){
						setFechaDesde(new java.sql.Date(getPanelFechaDesde().getDate().getTime()));
						setFechaHasta(new java.sql.Date(getPanelFechaHasta().getDate().getTime()));
						if(getCmbTipoFactura().getSelectedItem().equals("TODAS")){
							setTipoFactura(null);
						}else{
							setTipoFactura((ETipoFactura)getCmbTipoFactura().getSelectedItem());
						}
						setAcepto(true);
						dispose();
					}else{
						CLJOptionPane.showErrorMessage(JDialogInformeIVAVentas.this, "La 'fecha desde' debe ser mayor que la 'fecha hasta'", "Error");
					}
				}
			});
		}
		return btnAceptar;
	}

	public PanelDatePicker getPanelFechaDesde() {
		if (panelFechaDesde == null) {
			panelFechaDesde = new PanelDatePicker();
			panelFechaDesde.setCaption("Fecha desde:");
			panelFechaDesde.setSelectedDate(DateUtil.restarDias(DateUtil.getHoy(), 30));
		}
		return panelFechaDesde;
	}

	public PanelDatePicker getPanelFechaHasta() {
		if (panelFechaHasta == null) {
			panelFechaHasta = new PanelDatePicker();
			panelFechaHasta.setCaption("Fecha hasta:");
			panelFechaHasta.setSelectedDate(DateUtil.getHoy());
		}
		return panelFechaHasta;
	}

	public JComboBox getCmbTipoFactura() {
		if(cmbTipoFactura == null){
			cmbTipoFactura = new JComboBox();
			cmbTipoFactura.addItem("TODAS");
			for(ETipoFactura t : ETipoFactura.values()){
				cmbTipoFactura.addItem(t);
			}
		}
		return cmbTipoFactura;
	}

	private JPanel getPanelElegirCliente(){
		JPanel panCliente = new JPanel();
		panCliente.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
		panCliente.add(new JLabel("Cliente Nº: "));
		panCliente.add(getTxtNroCliente());
		panCliente.add(getComboBusquedaUsuario());
		panCliente.add(getLblelegirCliente());
		return panCliente;
	}
	
	private CLJTextField getTxtNroCliente() {
		if (txtNroCliente == null) {
			txtNroCliente = new CLJTextField();
			txtNroCliente.setEditable(false);
			txtNroCliente.setPreferredSize(new Dimension(50, 20));
		}
		return txtNroCliente;
	}
	
	private LinkableLabel getLblelegirCliente() {
		if (lblElegirCliente == null) {
			lblElegirCliente = new LinkableLabel("Elegir cliente") {

				private static final long serialVersionUID = 580819185565135378L;

				@Override
				public void labelClickeada(MouseEvent e) {
					if (e.getClickCount() == 1) {
						JDialogSeleccionarCliente dialogSeleccionarCliente = new JDialogSeleccionarCliente(frame);
						GuiUtil.centrar(dialogSeleccionarCliente);
						dialogSeleccionarCliente.setVisible(true);
						Cliente clienteElegido = dialogSeleccionarCliente.getCliente();
						if (clienteElegido != null) {
							setCliente(clienteElegido);
							getTxtNroCliente().setText(String.valueOf(clienteElegido.getNroCliente()));
							getComboBusquedaUsuario().reset();
						}
					}
				}
			};
		}
		return lblElegirCliente;
	}
	
	private CLTxtComboBusquedaUsuarioByCodigo getComboBusquedaUsuario() {
		if(comboBusquedaUsuario == null){
			comboBusquedaUsuario = new CLTxtComboBusquedaUsuarioByCodigo();
		}
		return comboBusquedaUsuario;
	}
	
	private class CLTxtComboBusquedaUsuarioByCodigo extends CLTxtComboBoxBusqueda<Cliente> {

		private static final long serialVersionUID = -8069636605971687535L;

		@Override
		protected List<Cliente> buscar(String text) {
			List<Cliente> clientes = new ArrayList<Cliente>();
			try {
				Cliente cliente = getClienteFacade().getClienteByNumero(Integer.valueOf(text));
				if(cliente!=null){
					setCliente(cliente);
					getTxtNroCliente().setText(String.valueOf(cliente.getNroCliente()));
					clientes.add(cliente);
				}
			} catch (Exception ex) {
				BossError.gestionarError(new RuntimeException(ex.getMessage(), ex));
			}
			return clientes;
		}

		@Override
		protected boolean realizarBusqueda(String text) {
			if (StringUtil.isNullOrEmptyString(text)) {
				return false;
			}
			if (!NumUtil.esNumerico(text)) {
				CLJOptionPane.showWarningMessage(this, StringW.wordWrap("Debe ingresar sólo números"), "Error");
				return false;
			}
			return true;
		}

		@Override
		public void noHayResultado() {
			CLJOptionPane.showInformationMessage(this, "No se encontraron resultados para la búsqueda.", "Información");
			setCliente(null);
			getTxtNroCliente().setText("");
		}
	}
	
	private ClienteFacadeRemote getClienteFacade() {
		if(clienteFacade == null){
			clienteFacade = GTLBeanFactory.getInstance().getBean2(ClienteFacadeRemote.class);
		}
		return clienteFacade;
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
}

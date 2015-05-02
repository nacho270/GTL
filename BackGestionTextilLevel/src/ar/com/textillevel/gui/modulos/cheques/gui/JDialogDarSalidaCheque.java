package ar.com.textillevel.gui.modulos.cheques.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.cheque.Banco;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.enums.EEstadoCheque;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.gente.Persona;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.facade.api.remote.BancoFacadeRemote;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;
import ar.com.textillevel.facade.api.remote.PersonaFacadeRemote;
import ar.com.textillevel.facade.api.remote.ProveedorFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogDarSalidaCheque extends JDialog {

	private static final long serialVersionUID = 6838596162143020024L;

	private boolean acepto;

	private JComboBox cmbOpciones;
	private JComboBox cmbProveedores;
	private JButton btnAceptar;
	private JButton btnSalir;

	private JPanel panelCentral;
	private JPanel panelBotones;

	private ProveedorFacadeRemote proveedoresFacade;
	private ClienteFacadeRemote clientesFacade;
	private PersonaFacadeRemote personasFacade;
	private BancoFacadeRemote bancosFacade;
	private Cheque cheque;

	public JDialogDarSalidaCheque(Frame padre, Cheque cheque) {
		super(padre);
		setCheque(cheque);
		setUpComponentes();
		setUpScreen();
		GuiUtil.centrarEnFramePadre(this);
		GuiUtil.llenarCombo(getCmbProveedores(), getBancosFacade().getAllOrderByName(), true);
	}

	private void setUpScreen() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Dar salida a cheque");
		setResizable(false);
		setSize(new Dimension(640, 150));
		setModal(true);
	}

	private void setUpComponentes() {
		add(getPanelCentral(),BorderLayout.CENTER);
		add(getPanelBotones(),BorderLayout.SOUTH);
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public Cheque getCheque() {
		return cheque;
	}

	public void setCheque(Cheque cheque) {
		this.cheque = cheque;
	}

	private JComboBox getCmbOpciones() {
		if (cmbOpciones == null) {
			cmbOpciones = new JComboBox();
			cmbOpciones.addItem(EEstadoCheque.SALIDA_BANCO);
			cmbOpciones.addItem(EEstadoCheque.SALIDA_PROVEEDOR);
			cmbOpciones.addItem(EEstadoCheque.SALIDA_CLIENTE);
			cmbOpciones.addItem(EEstadoCheque.SALIDA_PERSONA);
			if(getCheque().getEstadoCheque() == EEstadoCheque.SALIDA_BANCO){
				cmbOpciones.setSelectedItem(EEstadoCheque.SALIDA_BANCO);
				getCmbProveedores().setEnabled(true);
				getCmbProveedores().removeAllItems();
				GuiUtil.llenarCombo(getCmbProveedores(), getBancosFacade().getAllOrderByName(), true);
			}else if (getCheque().getEstadoCheque() == EEstadoCheque.SALIDA_PROVEEDOR){
				cmbOpciones.setSelectedItem(EEstadoCheque.SALIDA_PROVEEDOR);
				getCmbProveedores().setEnabled(true);
				getCmbProveedores().removeAllItems();
				GuiUtil.llenarCombo(getCmbProveedores(), getProveedoresFacade().getAllProveedoresOrderByName(), true);
			}else if (getCheque().getEstadoCheque() == EEstadoCheque.SALIDA_CLIENTE){
				cmbOpciones.setSelectedItem(EEstadoCheque.SALIDA_CLIENTE);
				getCmbProveedores().setEnabled(true);
				getCmbProveedores().removeAllItems();
				GuiUtil.llenarCombo(getCmbProveedores(),  getClientesFacade().getAllOrderByName(), true);
			}else if (getCheque().getEstadoCheque() == EEstadoCheque.SALIDA_PERSONA){
				cmbOpciones.setSelectedItem(EEstadoCheque.SALIDA_PERSONA);
				getCmbProveedores().setEnabled(true);
				getCmbProveedores().removeAllItems();
				GuiUtil.llenarCombo(getCmbProveedores(), getPersonasFacade().getAllOrderByName(), true);
			}
			cmbOpciones.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						if (((EEstadoCheque) getCmbOpciones().getSelectedItem()) == EEstadoCheque.SALIDA_PROVEEDOR) {
							getCmbProveedores().setEnabled(true);
							getCmbProveedores().removeAllItems();
							GuiUtil.llenarCombo(getCmbProveedores(), getProveedoresFacade().getAllProveedoresOrderByName(), true);
						}else if (((EEstadoCheque) getCmbOpciones().getSelectedItem()) == EEstadoCheque.SALIDA_CLIENTE) {
							getCmbProveedores().setEnabled(true);
							getCmbProveedores().removeAllItems();
							GuiUtil.llenarCombo(getCmbProveedores(), getClientesFacade().getAllOrderByName(), true);
						}else if (((EEstadoCheque) getCmbOpciones().getSelectedItem()) == EEstadoCheque.SALIDA_PERSONA) {
							getCmbProveedores().setEnabled(true);
							getCmbProveedores().removeAllItems();
							GuiUtil.llenarCombo(getCmbProveedores(), getPersonasFacade().getAllOrderByName(), true);
						}else{
							getCmbProveedores().setEnabled(true);
							getCmbProveedores().removeAllItems();
							GuiUtil.llenarCombo(getCmbProveedores(), getBancosFacade().getAllOrderByName(), true);
						}
					}
				}
			});
		}
		return cmbOpciones;
	}

	private JComboBox getCmbProveedores() {
		if (cmbProveedores == null) {
			cmbProveedores = new JComboBox();
		}
		return cmbProveedores;
	}

	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cambiarEstado();
					setAcepto(true);
					dispose();
				}
			});
		}
		return btnAceptar;
	}

	private void cambiarEstado() {
		EEstadoCheque estadoSeleccionado = (EEstadoCheque)getCmbOpciones().getSelectedItem();
		getCheque().setEstadoCheque(estadoSeleccionado);
		if(estadoSeleccionado == EEstadoCheque.SALIDA_PROVEEDOR){
			getCheque().setProveedorSalida(((Proveedor)getCmbProveedores().getSelectedItem()));
		}else if(estadoSeleccionado == EEstadoCheque.SALIDA_CLIENTE){
			getCheque().setClienteSalida(((Cliente)getCmbProveedores().getSelectedItem()));
		}else if(estadoSeleccionado == EEstadoCheque.SALIDA_PERSONA){
			getCheque().setPersonaSalida(((Persona)getCmbProveedores().getSelectedItem()));
		}else{
			getCheque().setBancoSalida(((Banco)getCmbProveedores().getSelectedItem()));
		}
	}

	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Cancelar");
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setAcepto(false);
					dispose();
				}
			});
		}
		return btnSalir;
	}

	private ProveedorFacadeRemote getProveedoresFacade() {
		if (proveedoresFacade == null) {
			proveedoresFacade = GTLBeanFactory.getInstance().getBean2(ProveedorFacadeRemote.class);
		}
		return proveedoresFacade;
	}

	public JPanel getPanelCentral() {
		if(panelCentral == null){
			panelCentral = new JPanel();
			panelCentral.setLayout(new GridBagLayout());
			panelCentral.add(new JLabel("Motivo: "), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getCmbOpciones(),  GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,new Insets(10, 10, 10, 10), 1, 1, 1, 0));
			panelCentral.add(new JLabel("Proveedor: "), GenericUtils.createGridBagConstraints(2, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getCmbProveedores(),  GenericUtils.createGridBagConstraints(3, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 1, 1, 1, 0));
		}
		return panelCentral;
	}

	public JPanel getPanelBotones() {
		if(panelBotones == null){
			panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelBotones.add(getBtnAceptar());
			panelBotones.add(getBtnSalir());
		}
		return panelBotones;
	}
	
	public ClienteFacadeRemote getClientesFacade() {
		if (clientesFacade == null) {
			clientesFacade = GTLBeanFactory.getInstance().getBean2(ClienteFacadeRemote.class);
		}
		return clientesFacade;
	}
	
	public PersonaFacadeRemote getPersonasFacade() {
		if (personasFacade == null) {
			personasFacade = GTLBeanFactory.getInstance().getBean2(PersonaFacadeRemote.class);
		}
		return personasFacade;
	}

	public BancoFacadeRemote getBancosFacade() {
		if(bancosFacade == null){
			bancosFacade = GTLBeanFactory.getInstance().getBean2(BancoFacadeRemote.class);
		}
		return bancosFacade;
	}
}

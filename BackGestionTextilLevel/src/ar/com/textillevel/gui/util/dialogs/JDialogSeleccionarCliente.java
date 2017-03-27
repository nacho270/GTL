package ar.com.textillevel.gui.util.dialogs;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FocusTraversalPolicy;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogSeleccionarCliente extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel panDetalle;
	private JCheckBox chkBuscarNro;
	private FWJNumericTextField txtNroCliente;
	private JTextField txtRazSoc;
	private JButton btnBuscar;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JPanel pnlBotones;
	private FWJTable tablaResultados;
	private ClienteFacadeRemote clienteFacade;
	private Cliente cliente;
	private EModoDialogo modo;
	
	public JDialogSeleccionarCliente(Frame owner, EModoDialogo modo) {
		super(owner);
		this.modo = modo;
		this.clienteFacade = GTLBeanFactory.getInstance().getBean2(ClienteFacadeRemote.class);
		setModal(true);
		setSize(new Dimension(455, 550));
		setTitle("Seleccionar Cliente");
		construct();
	}

	private void construct() {
		setLayout(new GridBagLayout());
		add(getPanDetalle(), createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 1, 1));
		add(getPanelBotones(), createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		getChkBuscarNro().setSelected(modo == EModoDialogo.MODO_ID);
	}

	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(getChkBuscarNro(), createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(new JLabel(" NRO. DE CLIENTE:"), createGridBagConstraints(1, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtNroCliente(), createGridBagConstraints(2, 0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel(" RAZON SOCIAL:"), createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtRazSoc(), createGridBagConstraints(1, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 2, 1, 1, 0));
			panDetalle.add(getBtnBuscar(), createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			JScrollPane scrollPane = new JScrollPane(getTablaResultados());
			scrollPane.setBorder(BorderFactory.createTitledBorder("Resultados"));
			panDetalle.add(scrollPane, createGridBagConstraints(0, 3,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 3, 1, 0, 1));
			
			final Component[] comps = new Component[]{getChkBuscarNro(),getTxtNroCliente(),getTxtRazSoc(),getBtnBuscar()};
			FocusTraversalPolicy policy = new FocusTraversalPolicy() {
				List<Component> textList = Arrays.asList(comps);

				public Component getFirstComponent(Container focusCycleRoot) {
					return comps[modo == EModoDialogo.MODO_ID ? 1:2];
				}

				public Component getLastComponent(Container focusCycleRoot) {
					return comps[comps.length - 1];
				}

				public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
					int index = textList.indexOf(aComponent);
					return comps[(index + 1) % comps.length];
				}

				public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
					int index = textList.indexOf(aComponent);
					return comps[(index - 1 + comps.length) % comps.length];
				}

				public Component getDefaultComponent(Container focusCycleRoot) {
					return comps[modo == EModoDialogo.MODO_ID ? 1 : 2];
				}
			};
			
			setFocusTraversalPolicy(policy);
			
		}
		return panDetalle;
	}

	private FWJTable getTablaResultados() {
		if(tablaResultados == null) {
			tablaResultados = new FWJTable(0, 2) {

				private static final long serialVersionUID = -2960448130069418277L;

				@Override
				public void newRowSelected(int newRow, int oldRow) {
						getBtnAceptar().setEnabled(newRow != -1);
				}

			};
			tablaResultados.setStringColumn(0, "CLIENTE", 400, 400, true);
			tablaResultados.setStringColumn(1, "", 0, 0, true);
			tablaResultados.setAlignment(0, FWJTable.CENTER_ALIGN);
			tablaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tablaResultados.addMouseListener(new MouseAdapter () {

				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2) {
						handleSeleccionCliente();
					}
				}

			});
		}
		return tablaResultados;
	}

	private JButton getBtnBuscar() {
		if(btnBuscar == null) {
			btnBuscar = new JButton("Buscar");
			btnBuscar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					List<Cliente> clienteList = null;
					if(getChkBuscarNro().isSelected()) {
						Integer nroCliente = getTxtNroCliente().getValue();
						Cliente cliente = clienteFacade.getClienteByNumero(nroCliente);
						if(cliente == null) {
							FWJOptionPane.showInformationMessage(JDialogSeleccionarCliente.this, "No se encontraron resultados.", "Atención");
							clienteList = Collections.emptyList();
						} else {
							clienteList = Collections.singletonList(cliente);
						}
					} else {
						String razSoc = getTxtRazSoc().getText().trim();
						clienteList = clienteFacade.getAllByRazonSocial(razSoc);
						if(clienteList.isEmpty()) {
							FWJOptionPane.showInformationMessage(JDialogSeleccionarCliente.this, "No se encontraron resultados.", "Atención");
						}
					}
					llenarTabla(clienteList);
				}
			});
		}
		return btnBuscar;
	}

	protected void llenarTabla(List<Cliente> clienteList) {
		getTablaResultados().setNumRows(0);
		int row = 0;
		for(Cliente c : clienteList) {
			getTablaResultados().addRow();
			getTablaResultados().setValueAt(c.getRazonSocial(), row, 0);
			getTablaResultados().setValueAt(c, row, 1);
			row ++;
		}
	}

	private JTextField getTxtRazSoc() {
		if(txtRazSoc == null) {
			txtRazSoc = new JTextField();
			txtRazSoc.addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER) {
						getBtnBuscar().doClick();
					}
				}

			});
		}
		return txtRazSoc;
	}

	private FWJNumericTextField getTxtNroCliente() {
		if(txtNroCliente == null) {
			txtNroCliente = new FWJNumericTextField(0, Long.MAX_VALUE);
			txtNroCliente.setEditable(modo == EModoDialogo.MODO_ID);
			txtNroCliente.addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER) {
						getBtnBuscar().doClick();
					}
				}

			});
			
		}
		return txtNroCliente;
	}

	private JCheckBox getChkBuscarNro() {
		if(chkBuscarNro == null) {
			chkBuscarNro = new JCheckBox("Buscar por número");
			chkBuscarNro.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					getTxtNroCliente().setEditable(e.getStateChange() == ItemEvent.SELECTED);
					getTxtRazSoc().setEditable(e.getStateChange() != ItemEvent.SELECTED);
					if(e.getStateChange() == ItemEvent.SELECTED) {
						getTxtNroCliente().requestFocus();
						getTxtNroCliente().setText("");
					} else {
						getTxtRazSoc().requestFocus();
						getTxtRazSoc().setText("");
					}
				}
			});
		}
		return chkBuscarNro;
	}

	private JPanel getPanelBotones() {
		if(pnlBotones == null){
			pnlBotones = new JPanel();
			pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
			pnlBotones.add(getBtnAceptar());
			pnlBotones.add(getBtnCancelar());
		}
		return pnlBotones;
	}

	
	private JButton getBtnCancelar() {
		if(btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dispose();
				}

			});
		}
		return btnCancelar;
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					handleSeleccionCliente();
				}


			});
			btnAceptar.setEnabled(false);
		}
		return btnAceptar;
	}

	private void handleSeleccionCliente() {
		int selectedRow = getTablaResultados().getSelectedRow();
		cliente = (Cliente)getTablaResultados().getValueAt(selectedRow, 1);
		dispose();
	}

	private GridBagConstraints createGridBagConstraints(int x, int y, int posicion, int fill, Insets insets, int cantX, int cantY, double weightx, double weighty) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = posicion;
		gbc.fill = fill;
		gbc.insets = insets;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = cantX;
		gbc.gridheight = cantY;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		return gbc;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public enum EModoDialogo {
		MODO_ID,
		MODO_NOMBRE;
	}
}
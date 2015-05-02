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

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.com.textillevel.entidades.gente.Persona;
import ar.com.textillevel.facade.api.remote.PersonaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogSeleccionarPersona extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel panDetalle;
	private JCheckBox chkBuscarNombreCorto;
	private CLJTextField txtNombreCortoProveedor;
	private JTextField txtRazSoc;
	private JButton btnBuscar;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JPanel pnlBotones;
	private CLJTable tablaResultados;
	private final PersonaFacadeRemote personaFacade;
	private Persona persona;

	public JDialogSeleccionarPersona(Frame owner) {
		super(owner);
		this.personaFacade = GTLBeanFactory.getInstance().getBean2(PersonaFacadeRemote.class);
		setModal(true);
		setSize(new Dimension(455, 550));
		setTitle("Seleccionar persona");
		construct();
	}

	private void construct() {
		setLayout(new GridBagLayout());
		add(getPanDetalle(), createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 1, 1));
		add(getPanelBotones(), createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		getChkBuscarNombreCorto().setSelected(true);
	}

	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(getChkBuscarNombreCorto(), createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(new JLabel("NOMBRE CORTO: "), createGridBagConstraints(1, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtNombreCortoProveedor(), createGridBagConstraints(2, 0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(getBtnBuscar(), createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			JScrollPane scrollPane = new JScrollPane(getTablaResultados());
			scrollPane.setBorder(BorderFactory.createTitledBorder("Resultados"));
			panDetalle.add(scrollPane, createGridBagConstraints(0, 3,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 3, 1, 0, 1));
			
			final Component[] comps = new Component[]{getChkBuscarNombreCorto(),getTxtNombreCortoProveedor(),getTxtRazSoc(),getBtnBuscar()};
			FocusTraversalPolicy policy = new FocusTraversalPolicy() {
				List<Component> textList = Arrays.asList(comps);

				@Override
				public Component getFirstComponent(Container focusCycleRoot) {
					return comps[1];
				}

				@Override
				public Component getLastComponent(Container focusCycleRoot) {
					return comps[comps.length - 1];
				}

				@Override
				public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
					int index = textList.indexOf(aComponent);
					return comps[(index + 1) % comps.length];
				}

				@Override
				public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
					int index = textList.indexOf(aComponent);
					return comps[(index - 1 + comps.length) % comps.length];
				}

				@Override
				public Component getDefaultComponent(Container focusCycleRoot) {
					return comps[1];
				}
			};
			
			setFocusTraversalPolicy(policy);
			
		}
		return panDetalle;
	}

	private CLJTable getTablaResultados() {
		if(tablaResultados == null) {
			tablaResultados = new CLJTable(0, 2) {

				private static final long serialVersionUID = -2960448130069418277L;

				@Override
				public void newRowSelected(int newRow, int oldRow) {
					getBtnAceptar().setEnabled(newRow != -1);
				}

			};
			tablaResultados.setStringColumn(0, "PERSONA", 400, 400, true);
			tablaResultados.setStringColumn(1, "", 0, 0, true);
			tablaResultados.setAlignment(0, CLJTable.CENTER_ALIGN);
			tablaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tablaResultados.addMouseListener(new MouseAdapter () {

				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2) {
						handleSeleccionProveedor();
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
					List<Persona> proveedorList = null;
					String nombreCorto = getTxtNombreCortoProveedor().getText();
					List<Persona> persona = personaFacade.getByApellido(nombreCorto);
					if(persona == null || persona.isEmpty()) {
						CLJOptionPane.showInformationMessage(JDialogSeleccionarPersona.this, "No se encontraron resultados.", "Atención");
						proveedorList = Collections.emptyList();
					} else {
						proveedorList = persona;
					}
					
					llenarTabla(proveedorList);
				}

			});
		}
		return btnBuscar;
	}

	protected void llenarTabla(List<Persona> proveedorList) {
		getTablaResultados().setNumRows(0);
		int row = 0;
		for(Persona c : proveedorList) {
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

	private CLJTextField getTxtNombreCortoProveedor() {
		if(txtNombreCortoProveedor == null) {
			txtNombreCortoProveedor = new CLJTextField();
			txtNombreCortoProveedor.addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER) {
						getBtnBuscar().doClick();
					}
				}

			});
			
		}
		return txtNombreCortoProveedor;
	}

	private JCheckBox getChkBuscarNombreCorto() {
		if(chkBuscarNombreCorto == null) {
			chkBuscarNombreCorto = new JCheckBox("Buscar por nombre corto");
			chkBuscarNombreCorto.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					getTxtNombreCortoProveedor().setEditable(e.getStateChange() == ItemEvent.SELECTED);
					getTxtRazSoc().setEditable(e.getStateChange() != ItemEvent.SELECTED);
				}

			});
		}
		return chkBuscarNombreCorto;
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
					handleSeleccionProveedor();
				}


			});
			btnAceptar.setEnabled(false);
		}
		return btnAceptar;
	}

	private void handleSeleccionProveedor() {
		int selectedRow = getTablaResultados().getSelectedRow();
		persona = (Persona)getTablaResultados().getValueAt(selectedRow, 1);
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

	public Persona getPersona() {
		return persona;
	}

}
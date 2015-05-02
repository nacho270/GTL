package ar.com.textillevel.gui.modulos.personal.gui.util;

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

import main.GTLGlobalCache;
import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.facade.api.remote.EmpleadoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class JDialogSeleccionarEmpleado extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel panDetalle;
	private JCheckBox chkBuscarNro;
	private CLJNumericTextField txtNroLegajo;
	private JTextField txtApellido;
	private JButton btnBuscar;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JPanel pnlBotones;
	private CLJTable tablaResultados;
	private final EmpleadoFacadeRemote empleadoFacade;
	private Empleado empleado;

	public JDialogSeleccionarEmpleado(Frame owner) {
		super(owner);
		this.empleadoFacade = GTLPersonalBeanFactory.getInstance().getBean2(EmpleadoFacadeRemote.class);
		setModal(true);
		setSize(new Dimension(455, 550));
		setTitle("Seleccionar Empleado");
		construct();
	}

	private void construct() {
		setLayout(new GridBagLayout());
		add(getPanDetalle(), createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 1, 1));
		add(getPanelBotones(), createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		getChkBuscarNro().setSelected(false);
		getTxtNroLegajo().setEditable(false);
	}

	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(getChkBuscarNro(), createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(new JLabel(" NRO. DE LEGAJO:"), createGridBagConstraints(1, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtNroLegajo(), createGridBagConstraints(2, 0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel(" APELLIDO:"), createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtApellido(), createGridBagConstraints(1, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 2, 1, 1, 0));
			panDetalle.add(getBtnBuscar(), createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			JScrollPane scrollPane = new JScrollPane(getTablaResultados());
			scrollPane.setBorder(BorderFactory.createTitledBorder("Resultados"));
			panDetalle.add(scrollPane, createGridBagConstraints(0, 3,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 3, 1, 0, 1));
			
			final Component[] comps = new Component[]{getChkBuscarNro(),getTxtNroLegajo(),getTxtApellido(),getBtnBuscar()};
			FocusTraversalPolicy policy = new FocusTraversalPolicy() {
				List<Component> textList = Arrays.asList(comps);

				@Override
				public Component getFirstComponent(Container focusCycleRoot) {
					return comps[2];
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
					return comps[2];
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
			tablaResultados.setStringColumn(0, "EMPLEADO", 400, 400, true);
			tablaResultados.setStringColumn(1, "", 0, 0, true);
			tablaResultados.setAlignment(0, CLJTable.CENTER_ALIGN);
			tablaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tablaResultados.setAllowHidingColumns(false);
			tablaResultados.setReorderingAllowed(false);
			tablaResultados.setAllowSorting(false);
			tablaResultados.addMouseListener(new MouseAdapter () {

				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2) {
						handleSeleccionEmpleado();
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
					List<Empleado> empleadoList = null;
					if(getChkBuscarNro().isSelected()) {
						Integer nroLegajo = getTxtNroLegajo().getValue();
						Empleado empleado = empleadoFacade.getEmpleadoByNumeroLegajo(nroLegajo);
						if(empleado == null) {
							CLJOptionPane.showInformationMessage(JDialogSeleccionarEmpleado.this, "No se encontraron resultados.", "Atenci�n");
							empleadoList = Collections.emptyList();
						} else {
							empleadoList = Collections.singletonList(empleado);
						}
					} else {
						String apellido = getTxtApellido().getText().trim();
						empleadoList = empleadoFacade.getAllOrderByApellido(apellido,GTLGlobalCache.getInstance().getUsuarioSistema().getPerfil().getIsAdmin());
						if(empleadoList.isEmpty()) {
							CLJOptionPane.showInformationMessage(JDialogSeleccionarEmpleado.this, "No se encontraron resultados.", "Atenci�n");
						}
					}
					llenarTabla(empleadoList);
				}

			});
		}
		return btnBuscar;
	}

	protected void llenarTabla(List<Empleado> empleadoList) {
		getTablaResultados().setNumRows(0);
		int row = 0;
		for(Empleado e : empleadoList) {
			getTablaResultados().addRow();
			getTablaResultados().setValueAt(e.getNombre() + " " + e.getApellido(), row, 0);
			getTablaResultados().setValueAt(e, row, 1);
			row ++;
		}
	}

	private JTextField getTxtApellido() {
		if(txtApellido == null) {
			txtApellido = new JTextField();
			txtApellido.addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER) {
						getBtnBuscar().doClick();
					}
				}

			});
		}
		return txtApellido;
	}

	private CLJNumericTextField getTxtNroLegajo() {
		if(txtNroLegajo == null) {
			txtNroLegajo = new CLJNumericTextField(0, Long.MAX_VALUE);
			txtNroLegajo.addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER) {
						getBtnBuscar().doClick();
					}
				}

			});
			
		}
		return txtNroLegajo;
	}

	private JCheckBox getChkBuscarNro() {
		if(chkBuscarNro == null) {
			chkBuscarNro = new JCheckBox("Buscar por n�mero");
			chkBuscarNro.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					getTxtNroLegajo().setEditable(e.getStateChange() == ItemEvent.SELECTED);
					getTxtApellido().setEditable(e.getStateChange() != ItemEvent.SELECTED);
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
					handleSeleccionEmpleado();
				}


			});
			btnAceptar.setEnabled(false);
		}
		return btnAceptar;
	}

	private void handleSeleccionEmpleado() {
		int selectedRow = getTablaResultados().getSelectedRow();
		empleado = (Empleado)getTablaResultados().getValueAt(selectedRow, 1);
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

	public Empleado getEmpleadoSeleccionado() {
		return empleado;
	}

}

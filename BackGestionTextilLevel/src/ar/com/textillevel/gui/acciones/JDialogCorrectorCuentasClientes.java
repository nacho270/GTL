package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;

import main.GTLGlobalCache;
import ar.com.fwcommon.componentes.FWCursor;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.ImageUtil;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;
import ar.com.textillevel.facade.api.remote.CorrectorCuentasClientesFacadeRemote;
import ar.com.textillevel.facade.api.remote.CorrectorCuentasProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.ProveedorFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarProveedor;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogCorrectorCuentasClientes extends JDialog {

	private static final long serialVersionUID = 7820593012217980430L;

	private CorrectorCuentasClientesFacadeRemote correctorFacade;
	private CorrectorCuentasProveedorFacadeRemote correctorProveedorFacade;
	private ProveedorFacadeRemote proveedorFacade;
	private ClienteFacadeRemote clienteFacade;

	private static final int CANT_COLS = 2;
	private static final int COL_CLIENTE = 0;
	private static final int COL_RESULTADO = 1;

	private JCheckBox chkSeleccionarCliente;
	private FWJTextField txtNroCliente;
	private JButton btnSalir;
	private JButton btnCorregir;
	private JProgressBar progreso;
	private JLabel lblEstado;
	private JLabel lblWorking;
	private FWJTable tablaAvance;
	private JScrollPane jsp;
	private JComboBox cmbTipoCuenta;

	private JPanel panelNorte;
	private JPanel panelCentro;
	private JPanel panelSur;

	private Proveedor proveedor;

	public JDialogCorrectorCuentasClientes(Frame padre) {
		super(padre);
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Corrector de cuentas");
		setModal(true);
		setSize(new Dimension(500, 400));
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelNorte(), BorderLayout.NORTH);
		add(getPanelCentro(), BorderLayout.CENTER);
		add(getPanelSur(), BorderLayout.SOUTH);
	}

	public CorrectorCuentasClientesFacadeRemote getCorrectorFacade() {
		if (correctorFacade == null) {
			correctorFacade = GTLBeanFactory.getInstance().getBean2(CorrectorCuentasClientesFacadeRemote.class);
		}
		return correctorFacade;
	}

	public JCheckBox getChkSeleccionarCliente() {
		if (chkSeleccionarCliente == null) {
			chkSeleccionarCliente = new JCheckBox("Especificar entidad");
			chkSeleccionarCliente.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					ETipoCuenta tipoEntidad = (ETipoCuenta) getCmbTipoCuenta().getSelectedItem();
					if (chkSeleccionarCliente.isSelected()) {
						if (tipoEntidad == ETipoCuenta.PROVEEDOR) {
							getTxtNroCliente().setEditable(false);
							JDialogSeleccionarProveedor dialogSeleccionarProveedor = new JDialogSeleccionarProveedor(null);
							GuiUtil.centrar(dialogSeleccionarProveedor);
							dialogSeleccionarProveedor.setVisible(true);
							Proveedor proveedorElegido = dialogSeleccionarProveedor.getProveedor();
							if (proveedorElegido != null) {
								getTxtNroCliente().setText(proveedorElegido.getRazonSocial());
								proveedor = proveedorElegido;
							} else {
								getChkSeleccionarCliente().setSelected(false);
							}
						}
					} else {
						getTxtNroCliente().setText(null);
					}
					boolean habilitarCampoParaCliente = tipoEntidad == ETipoCuenta.CLIENTE && getChkSeleccionarCliente().isSelected();
					getTxtNroCliente().setEditable(habilitarCampoParaCliente);
					getTxtNroCliente().setEnabled(habilitarCampoParaCliente);
				}

			});
		}
		return chkSeleccionarCliente;
	}

	public FWJTextField getTxtNroCliente() {
		if (txtNroCliente == null) {
			txtNroCliente = new FWJTextField();
			txtNroCliente.setEnabled(false);
			txtNroCliente.setEditable(false);
			txtNroCliente.setPreferredSize(new Dimension(100, 20));
		}
		return txtNroCliente;
	}

	public JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Salir");
			btnSalir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnSalir;
	}

	public JButton getBtnCorregir() {
		if (btnCorregir == null) {
			btnCorregir = new JButton("Corregir");
			btnCorregir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					realizarCorrecciones();
				}
			});
		}
		return btnCorregir;
	}

	private void realizarCorrecciones() {
		if (getChkSeleccionarCliente().isSelected()) {
			ETipoCuenta tipoCuenta = (ETipoCuenta) getCmbTipoCuenta().getSelectedItem();
			String textoIngresado = getTxtNroCliente().getText().trim();
			if (tipoCuenta == ETipoCuenta.CLIENTE) {
				if (GenericUtils.esNumerico(textoIngresado)) {
					Cliente cl = getClienteFacade().getClienteByNumero(Integer.valueOf(textoIngresado));
					if (cl == null) {
						FWJOptionPane.showErrorMessage(this, "No se ha encontrado al cliente indicado", "Error");
						return;
					}
					EntidadWrapper entidad = new EntidadWrapper(cl);
					new ThreadCorreccion(entidad).start();
				} else {
					FWJOptionPane.showErrorMessage(this, "El número de cliente debe ser numérico", "Error");
					return;
				}
			} else {
				EntidadWrapper entidad = new EntidadWrapper(proveedor);
				new ThreadCorreccion(entidad).start();
			}
		} else {
			ETipoCuenta tipoEntidad = (ETipoCuenta) getCmbTipoCuenta().getSelectedItem();
			if (FWJOptionPane.showQuestionMessage(this, "Va a corregir todas las cuentas de '" + tipoEntidad + "'. ¿Desea continuar?", "Pregunta") == FWJOptionPane.YES_OPTION) {
				List<EntidadWrapper> entidades = getEntidades();
				if (entidades != null && !entidades.isEmpty()) {
					new ThreadCorreccion(entidades).start();
				} else {
					FWJOptionPane.showErrorMessage(this, "No se han encontrado clientes en el sistema", "Error");
					return;
				}
			}
		}
	}

	private List<EntidadWrapper> getEntidades() {
		List<EntidadWrapper> entidades = new ArrayList<JDialogCorrectorCuentasClientes.EntidadWrapper>();
		ETipoCuenta tipoCuenta = (ETipoCuenta) getCmbTipoCuenta().getSelectedItem();
		if (tipoCuenta == ETipoCuenta.CLIENTE) {
			List<Cliente> clientes = getClienteFacade().getAllOrderByName();
			for (Cliente cl : clientes) {
				entidades.add(new EntidadWrapper(cl));
			}
		} else {
			List<Proveedor> proveedores = getProveedorFacade().getAllProveedoresOrderByName();
			for (Proveedor pr : proveedores) {
				entidades.add(new EntidadWrapper(pr));
			}
		}
		return entidades;
	}

	private void corregirCuentaEntidad(EntidadWrapper e) throws ValidacionException {
		ETipoCuenta tipoCuenta = (ETipoCuenta) getCmbTipoCuenta().getSelectedItem();
		String usrName = GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName();
		Integer entidadId = e.getId();
		if (tipoCuenta == ETipoCuenta.CLIENTE) {
			getCorrectorFacade().corregirCuenta(entidadId, usrName);
		} else {
			getCorrectorProveedorFacade().corregirCuenta(entidadId, usrName);
		}
	}

	private CorrectorCuentasProveedorFacadeRemote getCorrectorProveedorFacade() {
		if (correctorProveedorFacade == null) {
			correctorProveedorFacade = GTLBeanFactory.getInstance().getBean2(CorrectorCuentasProveedorFacadeRemote.class);
		}
		return correctorProveedorFacade;
	}

	private ProveedorFacadeRemote getProveedorFacade() {
		if (proveedorFacade == null) {
			proveedorFacade = GTLBeanFactory.getInstance().getBean2(ProveedorFacadeRemote.class);
		}
		return proveedorFacade;
	}

	private class ThreadCorreccion extends Thread {

		private EntidadWrapper entidadWrapper;
		private List<EntidadWrapper> listaEntidades;

		public ThreadCorreccion(EntidadWrapper entidadWrapper) {
			setEntidad(entidadWrapper);
		}

		public ThreadCorreccion(List<EntidadWrapper> clientes) {
			setListaEntidades(clientes);
		}

		@Override
		public void run() {
			bloquearComponentes();
			FWCursor.startWait(JDialogCorrectorCuentasClientes.this);
			if (getEntidad() == null) {
				double avance = 100f / getListaEntidades().size();
				double avanceAcumulado = 0;
				int contador = 1;
				for (EntidadWrapper e : getListaEntidades()) {
					try {
						agregarFila(e.getRazonSocial());
						getLblEstado().setText((contador++) + " de " + getListaEntidades().size() + " - " + "Corrigiendo " + e.getRazonSocial());
						corregirCuentaEntidad(e);
						avanceAcumulado += avance;
						getProgreso().setValue((int) avanceAcumulado);
						actualizarUltimaFila(EEstadoCorreccionCuentaCliente.OK);
					} catch (ValidacionException ex) {
						ex.printStackTrace();
						actualizarUltimaFila(EEstadoCorreccionCuentaCliente.ERROR);
						continue;
					}
				}
			} else {
				try {
					agregarFila(getEntidad().getRazonSocial());
					getLblEstado().setText("Corrigiendo " + getEntidad().getRazonSocial());
					corregirCuentaEntidad(getEntidad());
					getProgreso().setValue(100);
					actualizarUltimaFila(EEstadoCorreccionCuentaCliente.OK);
				} catch (ValidacionException e) {
					e.printStackTrace();
					actualizarUltimaFila(EEstadoCorreccionCuentaCliente.ERROR);
				}
			}
			desBloquearComponentes();
			FWCursor.endWait(JDialogCorrectorCuentasClientes.this);
		}

		private void actualizarUltimaFila(EEstadoCorreccionCuentaCliente estado) {
			getTablaAvance().setValueAt(estado, getTablaAvance().getRowCount() - 1, COL_RESULTADO);
		}

		private void agregarFila(String razonSocial) {
			Object[] row = new Object[CANT_COLS];
			row[COL_CLIENTE] = razonSocial;
			row[COL_RESULTADO] = EEstadoCorreccionCuentaCliente.PROCESANDO;
			getTablaAvance().addRow(row);
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					getTablaAvance().scrollRectToVisible(getTablaAvance().getCellRect(getTablaAvance().getRowCount() - 1, getTablaAvance().getColumnCount(), true));
					getJsp().scrollRectToVisible(getTablaAvance().getCellRect(getTablaAvance().getRowCount() - 1, getTablaAvance().getColumnCount(), true));
					getJsp().invalidate();
					getJsp().repaint();
				}
			});
		}

		private void bloquearComponentes() {
			getTablaAvance().removeAllRows();
			getChkSeleccionarCliente().setEnabled(false);
			getLblWorking().setVisible(true);
			getBtnSalir().setEnabled(false);
			getBtnCorregir().setEnabled(false);
			getProgreso().setValue(0);
		}

		private void desBloquearComponentes() {
			getLblWorking().setVisible(false);
			getBtnSalir().setEnabled(true);
			getBtnCorregir().setEnabled(true);
			getChkSeleccionarCliente().setEnabled(true);
			getLblEstado().setText("FIN");
		}

		public List<EntidadWrapper> getListaEntidades() {
			return listaEntidades;
		}

		public void setListaEntidades(List<EntidadWrapper> listaEntidades) {
			this.listaEntidades = listaEntidades;
		}

		public EntidadWrapper getEntidad() {
			return entidadWrapper;
		}

		public void setEntidad(EntidadWrapper entidad) {
			this.entidadWrapper = entidad;
		}
	}

	private enum EEstadoCorreccionCuentaCliente {
		OK, PROCESANDO, ERROR;
	}

	public JProgressBar getProgreso() {
		if (progreso == null) {
			progreso = new JProgressBar(0, 100);
			progreso.setStringPainted(true);
		}
		return progreso;
	}

	public JLabel getLblEstado() {
		if (lblEstado == null) {
			lblEstado = new JLabel();
		}
		return lblEstado;
	}

	public FWJTable getTablaAvance() {
		if (tablaAvance == null) {
			tablaAvance = new FWJTable(0, CANT_COLS);
			tablaAvance.setStringColumn(COL_CLIENTE, "Razón Social", 250, 250, true);
			tablaAvance.setStringColumn(COL_RESULTADO, "Resultado", 100, 100, true);
			tablaAvance.setReorderingAllowed(false);
			tablaAvance.setAllowHidingColumns(false);
			tablaAvance.setAllowSorting(false);
			tablaAvance.getColumnModel().getColumn(COL_RESULTADO).setCellRenderer(new EstadoCorreccionCuentaClienteCellRenderer());
		}
		return tablaAvance;
	}

	private class EstadoCorreccionCuentaClienteCellRenderer extends JLabel implements TableCellRenderer {

		private static final long serialVersionUID = 1496405966581926966L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			EEstadoCorreccionCuentaCliente estado = (EEstadoCorreccionCuentaCliente) value;
			setText(String.valueOf(estado));
			if (estado == EEstadoCorreccionCuentaCliente.ERROR) {
				setForeground(Color.RED);
			} else if (estado == EEstadoCorreccionCuentaCliente.OK) {
				setForeground(Color.GREEN.darker());
			} else {
				setForeground(Color.BLACK);
			}
			return this;
		}
	}

	public JLabel getLblWorking() {
		if (lblWorking == null) {
			lblWorking = new JLabel(ImageUtil.loadIcon("ar/com/textillevel/imagenes/loading-chiquito.gif"));
			lblWorking.setVisible(false);
		}
		return lblWorking;
	}

	public JScrollPane getJsp() {
		if (jsp == null) {
			jsp = new JScrollPane(getTablaAvance(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jsp.setPreferredSize(new Dimension(450, 150));
		}
		return jsp;
	}

	public JPanel getPanelNorte() {
		if (panelNorte == null) {
			panelNorte = new JPanel(new GridBagLayout());
			panelNorte.add(new JLabel("Tipo de Cuenta:"), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelNorte.add(getCmbTipoCuenta(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));

			panelNorte.add(getChkSeleccionarCliente(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelNorte.add(new JLabel("Entidad: "), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelNorte.add(getTxtNroCliente(), GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
			panelNorte.add(getBtnCorregir(), GenericUtils.createGridBagConstraints(3, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));

			panelNorte.add(new JLabel("Estado: "), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelNorte.add(getLblEstado(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 4, 1, 0, 0));
			panelNorte.add(getLblWorking(), GenericUtils.createGridBagConstraints(5, 2, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));

			panelNorte.add(new JLabel("Avance: "), GenericUtils.createGridBagConstraints(0, 3, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelNorte.add(getProgreso(), GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 3, 1, 0, 0));
		}
		return panelNorte;
	}

	public JPanel getPanelCentro() {
		if (panelCentro == null) {
			panelCentro = new JPanel();
			panelCentro.add(getJsp(), BorderLayout.CENTER);
		}
		return panelCentro;
	}

	public JPanel getPanelSur() {
		if (panelSur == null) {
			panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelSur.add(getBtnSalir());
		}
		return panelSur;
	}

	public ClienteFacadeRemote getClienteFacade() {
		if (clienteFacade == null) {
			clienteFacade = GTLBeanFactory.getInstance().getBean2(ClienteFacadeRemote.class);
		}
		return clienteFacade;
	}

	private static enum ETipoCuenta {
		CLIENTE("Cliente"), PROVEEDOR("Proveedor");

		private String tipo;

		private ETipoCuenta(String tipo) {
			this.tipo = tipo;
		}

		public String toString() {
			return tipo;
		}
	}

	private static class EntidadWrapper {

		private Cliente cliente;
		private Proveedor proveedor;

		public EntidadWrapper(Cliente cliente) {
			this.cliente = cliente;
		}

		public EntidadWrapper(Proveedor proveedor) {
			this.proveedor = proveedor;
		}

		public Integer getId() {
			if (cliente != null) {
				return cliente.getId();
			}
			if (proveedor != null) {
				return proveedor.getId();
			}
			throw new IllegalArgumentException("Entidad mal formada");
		}

		public String getRazonSocial() {
			if (cliente != null) {
				return cliente.getRazonSocial();
			}
			if (proveedor != null) {
				return proveedor.getRazonSocial();
			}
			throw new IllegalArgumentException("Entidad mal formada");
		}

	}

	private JComboBox getCmbTipoCuenta() {
		if (cmbTipoCuenta == null) {
			this.cmbTipoCuenta = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoCuenta, Arrays.asList(ETipoCuenta.values()), true);
			this.cmbTipoCuenta.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					getChkSeleccionarCliente().setSelected(false);
					getTxtNroCliente().setText(null);
					getTxtNroCliente().setEditable(false);
					proveedor = null;

				}

			});
		}
		return cmbTipoCuenta;
	}

}
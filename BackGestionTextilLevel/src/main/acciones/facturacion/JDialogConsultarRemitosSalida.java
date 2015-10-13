package main.acciones.facturacion;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import main.GTLGlobalCache;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.enums.ETipoRemitoSalida;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.facade.api.remote.RemitoSalidaFacadeRemote;
import ar.com.textillevel.gui.acciones.proveedor.remitosalida.JDialogAgregarRemitoSalidaProveedor;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.LinkableLabel;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarProveedor;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogConsultarRemitosSalida extends JDialog {

	private static final long serialVersionUID = -5312992576670286259L;

	private boolean acepto;

	private Date fechaDesde;
	private Date fechaHasta;
	private Cliente cliente;
	private Proveedor proveedor;
	private JButton btnCancelar;
	private JButton btnAceptar;
	private JButton btnBuscar;
	private PanelDatePicker panelFechaDesde;
	private PanelDatePicker panelFechaHasta;
	private FWJTextField txtNroCliente;
	private FWJTextField txtNombreProveedor;
	private LinkableLabel lblElegirCliente;
	private LinkableLabel lblElegirProveedor;	
	private PanTablaRemitosSalida panTablaRemitosSalida;
	private FWJNumericTextField txtNroRemito;

	private RemitoSalidaFacadeRemote remitoSalidaFacade;

	private final Frame frame;

	private JRadioButton rbtClienteOrProveedor;
	private JRadioButton rbtPorNroRemito;
	private JPanel panEleccionPorClienteOrProveedor;
	private JPanel panCliente;
	private JPanel panProveedor;
	private EleccionBusquedaListener eleccionPersonaListener;

	public JDialogConsultarRemitosSalida(Frame padre) {
		super(padre);
		this.frame = padre; 
		setAcepto(false);
		setUpScreen();
		setUpComponentes();
		getRbtPorNumeroRemito().setSelected(true);
	}

	private void setUpComponentes() {
		add(getPanelCentral(), BorderLayout.NORTH);
		add(getPanTablaRemitosSalida(), BorderLayout.CENTER);
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
		pan.setLayout(new GridBagLayout());

		pan.add(getRbtPorNumeroRemito(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		pan.add(getTxtNroRemito(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		pan.add(getRbtPorClienteOrProveedor(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		pan.add(getPanEleccionPorClienteOrProveedor(), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 2, 1, 1, 1));
		pan.add(getBtnBuscar(), GenericUtils.createGridBagConstraints(0, 3, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 0, 0));		

		ButtonGroup bgOpcionProceso = new ButtonGroup();
		bgOpcionProceso.add(getRbtPorClienteOrProveedor());
		bgOpcionProceso.add(getRbtPorNumeroRemito());

		return pan;
	}

	
	
	private void setUpScreen() {
		setTitle("Consulta de Remitos de Salida");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(660, 500));
		setResizable(false);
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

	private JRadioButton getRbtPorClienteOrProveedor() {
		if (rbtClienteOrProveedor == null) {
			rbtClienteOrProveedor = new JRadioButton();
			rbtClienteOrProveedor.addItemListener(getEleccionPersonaListener());
			rbtClienteOrProveedor.setText("Por Cliente/Proveedor");
		}
		return rbtClienteOrProveedor;
	}

	private JRadioButton getRbtPorNumeroRemito() {
		if (rbtPorNroRemito == null) {
			rbtPorNroRemito = new JRadioButton();
			rbtPorNroRemito.addItemListener(getEleccionPersonaListener());
			rbtPorNroRemito.setText("Por Número de Remito");
		}
		return rbtPorNroRemito;
	}

	private JPanel getPanEleccionPorClienteOrProveedor() {
		if(panEleccionPorClienteOrProveedor == null) {
			panEleccionPorClienteOrProveedor = new JPanel();
			panEleccionPorClienteOrProveedor.setBorder(BorderFactory.createTitledBorder(""));
			panEleccionPorClienteOrProveedor.setLayout(new GridBagLayout());
			panEleccionPorClienteOrProveedor.add(getPanelFechaDesde(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panEleccionPorClienteOrProveedor.add(getPanelFechaHasta(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panEleccionPorClienteOrProveedor.add(getPanelElegirCliente(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.5, 0.5));
			panEleccionPorClienteOrProveedor.add(getPanelElegirProveedor(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.5, 0.5));
		}
		return panEleccionPorClienteOrProveedor;
	}

	private EleccionBusquedaListener getEleccionPersonaListener() {
		if(eleccionPersonaListener == null) {
			eleccionPersonaListener = new EleccionBusquedaListener();
		}
		return eleccionPersonaListener;
	}

	private class EleccionBusquedaListener implements ItemListener {

		public void itemStateChanged(ItemEvent e) {
			if(e.getSource() == rbtClienteOrProveedor) {
				GuiUtil.setEstadoPanel(getPanEleccionPorClienteOrProveedor(), true);
				getLblelegirCliente().setEnabled(true);
				getLblelegirProveedor().setEnabled(true);
				getTxtNroRemito().setEnabled(false);
				getTxtNroRemito().setValue(null);
			}

			if(e.getSource() == rbtPorNroRemito) {
				setCliente(null);
				setProveedor(null);
				getPanTablaRemitosSalida().getTabla().removeAllRows();
				getTxtNombreProveedor().setText(null);
				getTxtNroCliente().setText(null);
				getLblelegirCliente().setEnabled(false);
				getLblelegirProveedor().setEnabled(false);
				GuiUtil.setEstadoPanel(getPanEleccionPorClienteOrProveedor(), false);
				getTxtNroRemito().setEnabled(true);
			}
		}

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
					if(!getPanelFechaDesde().getDate().after(getPanelFechaHasta().getDate())){
						setFechaDesde(new java.sql.Date(getPanelFechaDesde().getDate().getTime()));
						setFechaHasta(new java.sql.Date(getPanelFechaHasta().getDate().getTime()));
						setAcepto(true);
						dispose();
					}else{
						FWJOptionPane.showErrorMessage(JDialogConsultarRemitosSalida.this, "La 'fecha desde' debe ser mayor que la 'fecha hasta'", "Error");
					}
				}
			});
		}
		return btnAceptar;
	}

	@SuppressWarnings("serial")
	public PanelDatePicker getPanelFechaDesde() {
		if (panelFechaDesde == null) {
			panelFechaDesde = new PanelDatePicker() {

				@Override
				public void accionBotonCalendarioAdicional() {
					buscarRemitos();
				}
				
			};
			panelFechaDesde.setCaption("Fecha desde:");
			panelFechaDesde.setSelectedDate(DateUtil.restarDias(DateUtil.getHoy(), 30));
		}
		return panelFechaDesde;
	}

	@SuppressWarnings("serial")
	public PanelDatePicker getPanelFechaHasta() {
		if (panelFechaHasta == null) {
			panelFechaHasta = new PanelDatePicker() {

				@Override
				public void accionBotonCalendarioAdicional() {
					buscarRemitos();
				}
				
			};
			panelFechaHasta.setCaption("Fecha hasta:");
			panelFechaHasta.setSelectedDate(DateUtil.getHoy());
		}
		return panelFechaHasta;
	}

	private JPanel getPanelElegirCliente(){
		if(panCliente == null) {
			panCliente = new JPanel();
			panCliente.setLayout(new GridBagLayout());
			panCliente.setBorder(BorderFactory.createTitledBorder(""));
			panCliente.add(new JLabel("Cliente: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panCliente.add(getTxtNroCliente(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panCliente.add(getLblelegirCliente(), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		}
		return panCliente;
	}

	private JPanel getPanelElegirProveedor() {
		if(panProveedor == null) {
			panProveedor = new JPanel();
			panProveedor.setLayout(new GridBagLayout());
			panProveedor.setBorder(BorderFactory.createTitledBorder(""));
			panProveedor.add(new JLabel("Proveedor: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panProveedor.add(getTxtNombreProveedor(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panProveedor.add(getLblelegirProveedor(), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		}
		return panProveedor;
	}

	private FWJTextField getTxtNombreProveedor() {
		if (txtNombreProveedor == null) {
			txtNombreProveedor = new FWJTextField();
			txtNombreProveedor.setEditable(false);
			txtNombreProveedor.setPreferredSize(new Dimension(50, 20));
		}
		return txtNombreProveedor;
	}

	private FWJNumericTextField getTxtNroRemito() {
		if (txtNroRemito == null) {
			txtNroRemito = new FWJNumericTextField();
			txtNroRemito.setPreferredSize(new Dimension(50, 20));
			
			txtNroRemito.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					getBtnBuscar().doClick();
				}

			});
			
		}
		return txtNroRemito;
	}

	private FWJTextField getTxtNroCliente() {
		if (txtNroCliente == null) {
			txtNroCliente = new FWJTextField();
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
							setProveedor(null);
							getTxtNombreProveedor().setText(null);
							setCliente(clienteElegido);
							getTxtNroCliente().setText(String.valueOf(clienteElegido.getNroCliente()));
						}
					}
				}
			};
		}
		return lblElegirCliente;
	}

	private LinkableLabel getLblelegirProveedor() {
		if (lblElegirProveedor == null) {
			lblElegirProveedor = new LinkableLabel("Elegir proveedor") {

				private static final long serialVersionUID = 874808168456197457L;

				@Override
				public void labelClickeada(MouseEvent e) {
					if (e.getClickCount() == 1) {
						JDialogSeleccionarProveedor dialogSeleccionarProveedor = new JDialogSeleccionarProveedor(frame);
						GuiUtil.centrar(dialogSeleccionarProveedor);
						dialogSeleccionarProveedor.setVisible(true);
						Proveedor proveedorElegido = dialogSeleccionarProveedor.getProveedor();
						if (proveedorElegido != null) {
							setCliente(null);
							getTxtNroCliente().setText(null);
							setProveedor(proveedorElegido);
							getTxtNombreProveedor().setText(String.valueOf(proveedorElegido.getNombreCorto()));
						}
					}
				}
			};
		}
		return lblElegirProveedor;
	}

	private void buscarRemitos() {
		getPanTablaRemitosSalida().getTabla().removeAllRows();
		if(getRbtPorClienteOrProveedor().isSelected()) {
			if(getCliente() == null && getProveedor() == null) {
				FWJOptionPane.showErrorMessage(JDialogConsultarRemitosSalida.this, "Debe seleccionar un cliente o un proveedor.", "Error");
				return;
			}
			if(getCliente() != null) {
				getPanTablaRemitosSalida().getTabla().removeAllRows();
				List<RemitoSalida> remitoSalidaList = getRemitoSalidaFacade().getRemitoSalidaByFechasAndCliente(new Date(getPanelFechaDesde().getDate().getTime()), DateUtil.getManiana(new Date(getPanelFechaHasta().getDate().getTime())), getCliente().getId());
				getPanTablaRemitosSalida().agregarElementos(remitoSalidaList);
			}
			if(getProveedor() != null) {
				getPanTablaRemitosSalida().getTabla().removeAllRows();
				List<RemitoSalida> remitoSalidaList = getRemitoSalidaFacade().getRemitoSalidaByFechasAndProveedor(new Date(getPanelFechaDesde().getDate().getTime()), DateUtil.getManiana(new Date(getPanelFechaHasta().getDate().getTime())), getProveedor().getId());
				getPanTablaRemitosSalida().agregarElementos(remitoSalidaList);
			}
		} else {
			if(StringUtil.isNullOrEmpty(getTxtNroRemito().getText()) || getTxtNroRemito().getValue() == null) {
				FWJOptionPane.showErrorMessage(JDialogConsultarRemitosSalida.this, "Debe ingresar un número de remito.", "Error");
				return;
			}
			getPanTablaRemitosSalida().agregarElementos(getRemitoSalidaFacade().getRemitosByNroRemitoConPiezasYProductos(getTxtNroRemito().getValue()));
		}
	}
	
	private Cliente getCliente() {
		return cliente;
	}

	private void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	private Proveedor getProveedor() {
		return proveedor;
	}

	private void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}


	
	public RemitoSalidaFacadeRemote getRemitoSalidaFacade() {
		if(remitoSalidaFacade == null) {
			remitoSalidaFacade = GTLBeanFactory.getInstance().getBean2(RemitoSalidaFacadeRemote.class);
		}
		return remitoSalidaFacade;
	}
	
	private PanTablaRemitosSalida getPanTablaRemitosSalida() {
		if(panTablaRemitosSalida == null) {
			panTablaRemitosSalida = new PanTablaRemitosSalida();
		}
		return panTablaRemitosSalida;
	}

	private class PanTablaRemitosSalida extends PanelTabla<RemitoSalida> {

		private static final long serialVersionUID = -53996356314921059L;

		private static final int CANT_COLS = 4;
		private static final int COL_REMITO = 0;
		private static final int COL_CLIENTE = 1;
		private static final int COL_PROVEEDOR = 2;
		private static final int COL_OBJ = 3;
		
		private JButton btnAnularRemitoSalida;

		public PanTablaRemitosSalida() {
			getBotonAgregar().setVisible(false);
			agregarBotonModificar();
			agregarBoton(getBtnAnularRemitoSalida());
		}

		private JButton getBtnAnularRemitoSalida() {
			if(btnAnularRemitoSalida == null) {
				btnAnularRemitoSalida = BossEstilos.createButton("ar/com/textillevel/imagenes/b_anular_recibo.png", "ar/com/textillevel/imagenes/b_anular_recibo_des.png"); 
				btnAnularRemitoSalida.setToolTipText("Anular Remito de Salida");
				btnAnularRemitoSalida.setVisible(true);
				btnAnularRemitoSalida.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						if(FWJOptionPane.showQuestionMessage(JDialogConsultarRemitosSalida.this, "¿Está seguro que desea anular el Remito de Salida?", "Confirmación") == FWJOptionPane.YES_OPTION) {
							int selectedRow = getTabla().getSelectedRow();
							RemitoSalida remitoSalida = getPanTablaRemitosSalida().getElemento(selectedRow);
							try {
								getRemitoSalidaFacade().checkEliminacionOrAnulacionRemitoSalida(remitoSalida.getId());
								getRemitoSalidaFacade().anularRemitoSalida(remitoSalida);
								FWJOptionPane.showInformationMessage(JDialogConsultarRemitosSalida.this, "El remito se ha anulado con éxito.", "Información");
								buscarRemitos();
							} catch (ValidacionException e1) {
								FWJOptionPane.showInformationMessage(JDialogConsultarRemitosSalida.this, StringW.wordWrap(e1.getMensajeError()), "Información");
							}
						}
					}

				});
			}
			btnAnularRemitoSalida.setEnabled(false);
			return btnAnularRemitoSalida;
		}

		
		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_REMITO, "REMITO", 200, 200, true);
			tabla.setStringColumn(COL_CLIENTE, "CLIENTE", 190, 190, true);
			tabla.setStringColumn(COL_PROVEEDOR, "PROVEEDOR", 190, 190, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			tabla.setHeaderAlignment(COL_REMITO, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_CLIENTE, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_PROVEEDOR, FWJTable.CENTER_ALIGN);
			tabla.addMouseListener(new MouseAdapter () {

				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2) {
						if(getTabla().getSelectedRow() != -1) {
							RemitoSalida remitoSalida = getElemento(getTabla().getSelectedRow());
							remitoSalida = getRemitoSalidaFacade().getByNroRemitoConPiezasYProductosAnulado(remitoSalida.getNroRemito());
							if(remitoSalida.getCliente() == null) {//Es un remito de salida de proveedor
								JDialogAgregarRemitoSalidaProveedor dialogoRemitoSalida = new JDialogAgregarRemitoSalidaProveedor(frame, true, remitoSalida);
								GuiUtil.centrar(dialogoRemitoSalida);
								dialogoRemitoSalida.setVisible(true);
							} else {
								OperacionSobreRemitoSalidaHandler handler = new OperacionSobreRemitoSalidaHandler(frame, remitoSalida, true);
								handler.showRemitoEntradaDialog();
							}
							
						}
					}
				}

			});

			return tabla;
		}

		@Override
		protected void agregarElemento(RemitoSalida elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_OBJ] = elemento;
			row[COL_CLIENTE] = elemento.getCliente() == null ? "" : elemento.getCliente().getRazonSocial();
			row[COL_PROVEEDOR] = elemento.getProveedor() == null ? "" : elemento.getProveedor().getRazonSocial();
			String descrRemito = "Nro. " + elemento.getNroRemito() + " -  Fecha: " + DateUtil.dateToString(elemento.getFechaEmision()) + ((elemento.getAnulado() != null && elemento.getAnulado()) ? " - ANULADO " : "");
			row[COL_REMITO] = descrRemito;
			getTabla().addRow(row);
		}

		@Override
		protected RemitoSalida getElemento(int fila) {
			return (RemitoSalida)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			RemitoSalida remitoSalida = getElemento(filaSeleccionada);
			RemitoSalida remitoConPiezasYProductos = getRemitoSalidaFacade().getByIdConPiezasYProductos(remitoSalida.getId());			
			if(remitoSalida.getCliente() == null) {
				JDialogAgregarRemitoSalidaProveedor dialogoRemitoSalida = new JDialogAgregarRemitoSalidaProveedor(frame, false, remitoConPiezasYProductos);
				GuiUtil.centrar(dialogoRemitoSalida);
				dialogoRemitoSalida.setVisible(true);
			} else {
				try {
					getRemitoSalidaFacade().checkEliminacionOrAnulacionRemitoSalida(remitoConPiezasYProductos.getId());
					OperacionSobreRemitoSalidaHandler handler = new OperacionSobreRemitoSalidaHandler(frame, remitoConPiezasYProductos, false);
					handler.showRemitoEntradaDialog();
				} catch (ValidacionException e1) {
					FWJOptionPane.showErrorMessage(frame, StringW.wordWrap(e1.getMensajeError()), "Imposible Editar");
				}
			}
			buscarRemitos();
		}

		@Override
		public boolean validarQuitar() {
			if(FWJOptionPane.showQuestionMessage(frame, "¿Está seguro que desea eliminar el remito?", "Confirmación") == FWJOptionPane.YES_OPTION) {
				RemitoSalida remitoSalida = getElemento(getTabla().getSelectedRow());
				try {
					if(remitoSalida.getTipoRemitoSalida() == null || remitoSalida.getTipoRemitoSalida() == ETipoRemitoSalida.CLIENTE || remitoSalida.getTipoRemitoSalida() == ETipoRemitoSalida.PROVEEDOR) {
						getRemitoSalidaFacade().eliminarRemitoSalida(remitoSalida.getId(), GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
					} else {
						getRemitoSalidaFacade().eliminarRemitoSalida01OrVentaTela(remitoSalida.getId(), GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
					}
					FWJOptionPane.showInformationMessage(frame, "Remito borrado éxitosamente.", "Información");				
				} catch (ValidacionException e) {
					FWJOptionPane.showErrorMessage(JDialogConsultarRemitosSalida.this, StringW.wordWrap(e.getMensajeError()), "Imposible Eliminar");
					return false;
				}
				return true;
			} else {
				return false;
			}
		}

		@Override
		protected void filaTablaSeleccionada() {
			int selectedRow = getTabla().getSelectedRow();
			if(selectedRow != -1 ) {
				RemitoSalida remitoSalida = getElemento(selectedRow);
				boolean botonAnularEnabled = (remitoSalida.getAnulado() == null || !remitoSalida.getAnulado()) && getBtnAnularRemitoSalida().isVisible();
				getBtnAnularRemitoSalida().setEnabled(botonAnularEnabled && remitoSalida.getProveedor() != null);
			} else {
				getBtnAnularRemitoSalida().setEnabled(false);
			}
		}

	}

	private JButton getBtnBuscar() {
		if(btnBuscar == null) {
			btnBuscar = new JButton("Buscar");
			btnBuscar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					buscarRemitos();
				}

			});

		}
		return btnBuscar;
	}

}
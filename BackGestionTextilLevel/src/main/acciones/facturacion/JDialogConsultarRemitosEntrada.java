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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import main.GTLGlobalCache;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.enums.ETipoFactura;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.facade.api.remote.RemitoEntradaFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.LinkableLabel;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente.EModoDialogo;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogConsultarRemitosEntrada extends JDialog {

	private static final long serialVersionUID = -5312992576670286259L;

	private boolean acepto;

	private Date fechaDesde;
	private Date fechaHasta;
	private ETipoFactura tipoFactura;
	private Cliente cliente;
	private JButton btnCancelar;
	private JButton btnAceptar;
	private JRadioButton rbtBuscarRemitoPorCliente;
	private JRadioButton rbtBuscarRemitoPorNro;
	private FWJNumericTextField txtNroRemito;
	private PanelDatePicker panelFechaDesde;
	private PanelDatePicker panelFechaHasta;
	private FWJTextField txtNroCliente;
	private LinkableLabel lblElegirCliente;
	private PanTablaRemitosEntrada panTablaRemitosEntrada;
	private RemitoEntradaFacadeRemote remitoEntradaFacade;
	private OrdenDeTrabajoFacadeRemote odtFacade;

	private Frame frame;
	private JPanel panCliente;
	
	public JDialogConsultarRemitosEntrada(Frame padre) {
		super(padre);
		this.frame = padre; 
		setAcepto(false);
		setUpScreen();
		setUpComponentes();
	}

	private void setUpComponentes() {
		add(getPanelCentral(), BorderLayout.NORTH);
		add(getPanTablaRemitosEntrada(), BorderLayout.CENTER);
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
		GridBagConstraints gc = GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 1, 1, 1, 0);
		pan.add(getPanBusquedaPorCliente(), gc);
		gc = GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 1, 1, 1, 0);
		pan.add(getPanBusquedaPorNro(), gc);
		ButtonGroup bgOpcionProceso = new ButtonGroup();
		bgOpcionProceso.add(getRbtBuscarRemitoPorCliente());
		bgOpcionProceso.add(getRbtBuscarRemitoPorNro());
		return pan;
	}

	private JPanel getPanBusquedaPorCliente() {
		JPanel pan = new JPanel();
		pan.setLayout(new FlowLayout(FlowLayout.CENTER,5,2));
		pan.add(getRbtBuscarRemitoPorCliente());
		pan.add(getPanelFechaDesde());
		pan.add(getPanelFechaHasta());
		pan.add(getPanelElegirCliente());
		return pan;
	}

	private JPanel getPanBusquedaPorNro() {
		JPanel pan = new JPanel();
		pan.setLayout(new FlowLayout(FlowLayout.CENTER,5,2));
		pan.add(getRbtBuscarRemitoPorNro());
		pan.add(getTxtNroRemito());
		return pan;
	}

 	private void setUpScreen() {
		setTitle("Consulta de Remitos de Entrada");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(750, 500));
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
					if(!getPanelFechaDesde().getDate().after(getPanelFechaHasta().getDate())){
						setFechaDesde(new java.sql.Date(getPanelFechaDesde().getDate().getTime()));
						setFechaHasta(new java.sql.Date(getPanelFechaHasta().getDate().getTime()));
						setAcepto(true);
						dispose();
					}else{
						FWJOptionPane.showErrorMessage(JDialogConsultarRemitosEntrada.this, "La 'fecha desde' debe ser mayor que la 'fecha hasta'", "Error");
					}
				}
			});
		}
		return btnAceptar;
	}

	@SuppressWarnings("serial")
	private PanelDatePicker getPanelFechaDesde() {
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
	private PanelDatePicker getPanelFechaHasta() {
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

	private JRadioButton getRbtBuscarRemitoPorCliente() {
		if(rbtBuscarRemitoPorCliente == null) {
			rbtBuscarRemitoPorCliente = new JRadioButton("Por Cliente");
			rbtBuscarRemitoPorCliente.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					setEnabledBusquedaPorClienteComponents(true);
					setEnabledBusquedaPorNro(false);
					getTxtNroRemito().setText(null);
					getPanTablaRemitosEntrada().getTabla().removeAllRows();
				}

			});

			rbtBuscarRemitoPorCliente.setSelected(true);
		}
		return rbtBuscarRemitoPorCliente;
	}

	private JRadioButton getRbtBuscarRemitoPorNro() {
		if(rbtBuscarRemitoPorNro == null) {
			rbtBuscarRemitoPorNro = new JRadioButton("Por Número de Remito");
			rbtBuscarRemitoPorNro.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					setEnabledBusquedaPorClienteComponents(false);
					setEnabledBusquedaPorNro(true);
					getTxtNroCliente().setText(null);
					getTxtNroRemito().requestFocus();
					getPanTablaRemitosEntrada().getTabla().removeAllRows();
				}

			});
			
		}
		return rbtBuscarRemitoPorNro;
	}

	private void setEnabledBusquedaPorNro(boolean enabled) {
		getTxtNroRemito().setEnabled(enabled);
	}

	private void setEnabledBusquedaPorClienteComponents(boolean enabled) {
		getLblelegirCliente().setVisible(enabled);
		getPanelFechaDesde().setEnabled(enabled);
		getPanelFechaHasta().setEnabled(enabled);
	}

	private FWJNumericTextField getTxtNroRemito() {
		if(txtNroRemito == null) {
			txtNroRemito = new FWJNumericTextField(0, Long.MAX_VALUE);
			
			txtNroRemito.addKeyListener(new KeyListener() {
				
				public void keyTyped(KeyEvent e) {
				}
				
				public void keyReleased(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER) {
						Integer value = getTxtNroRemito().getValue();
						if(value == null) {
							FWJOptionPane.showErrorMessage(JDialogConsultarRemitosEntrada.this, "Debe ingresar el número de remito", "Error");
							getTxtNroRemito().requestFocus();
						} else {
							buscarRemitos();
						}
					}
				}
				
				public void keyPressed(KeyEvent e) {
				}
			});
			
			txtNroRemito.setPreferredSize(new Dimension(70, 20));
			txtNroRemito.setEnabled(false);
		}
		return txtNroRemito;
	}

	private JPanel getPanelElegirCliente(){
		if(panCliente == null) {
			panCliente = new JPanel();
			panCliente.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
			panCliente.add(new JLabel("Cliente: "));
			panCliente.add(getTxtNroCliente());
			panCliente.add(getLblelegirCliente());
		}
		return panCliente;
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
						JDialogSeleccionarCliente dialogSeleccionarCliente = new JDialogSeleccionarCliente(frame, EModoDialogo.MODO_ID);
						GuiUtil.centrar(dialogSeleccionarCliente);
						dialogSeleccionarCliente.setVisible(true);
						Cliente clienteElegido = dialogSeleccionarCliente.getCliente();
						if (clienteElegido != null) {
							setCliente(clienteElegido);
							getTxtNroCliente().setText(String.valueOf(clienteElegido.getNroCliente()));
							buscarRemitos();
						}
					}
				}
			};
		}
		return lblElegirCliente;
	}
	
	private OrdenDeTrabajoFacadeRemote getOdtFacade() {
		if(odtFacade == null) {
			odtFacade = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class);
		}
		return odtFacade;
	}

	private void buscarRemitos() {
		if(getRbtBuscarRemitoPorCliente().isSelected()) {
			if(getCliente() != null) {
				getPanTablaRemitosEntrada().getTabla().removeAllRows();
				List<RemitoEntrada> remitoEntradaList = getRemitoEntradaFacade().getRemitoEntradaByFechasAndCliente(new Date(getPanelFechaDesde().getDate().getTime()), DateUtil.getManiana(new Date(getPanelFechaHasta().getDate().getTime())), getCliente().getId());
				getPanTablaRemitosEntrada().agregarElementos(remitoEntradaList);
			}
		} else {
			getPanTablaRemitosEntrada().getTabla().removeAllRows();
			Integer nroRemito = getTxtNroRemito().getValue();
			List<RemitoEntrada> remitoEntradaList = getRemitoEntradaFacade().getByNroRemito(nroRemito);
			getPanTablaRemitosEntrada().agregarElementos(remitoEntradaList);
		}
	}

	private Cliente getCliente() {
		return cliente;
	}

	private void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	private RemitoEntradaFacadeRemote getRemitoEntradaFacade() {
		if(remitoEntradaFacade == null) {
			remitoEntradaFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaFacadeRemote.class);
		}
		return remitoEntradaFacade;
	}
	
	private PanTablaRemitosEntrada getPanTablaRemitosEntrada() {
		if(panTablaRemitosEntrada == null) {
			panTablaRemitosEntrada = new PanTablaRemitosEntrada();
		}
		return panTablaRemitosEntrada;
	}

	private class PanTablaRemitosEntrada extends PanelTabla<RemitoEntrada> {

		private static final long serialVersionUID = -6459394446491824602L;

		private static final int CANT_COLS = 2;
		private static final int COL_REMITO = 0;
		private static final int COL_OBJ = 1;

		public PanTablaRemitosEntrada() {
			getBotonAgregar().setVisible(false);
			agregarBotonModificar();
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_REMITO, "REMITO", 580, 580, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			tabla.setHeaderAlignment(COL_REMITO, FWJTable.CENTER_ALIGN);
			tabla.addMouseListener(new MouseAdapter () {

				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2) {
						if(getTabla().getSelectedRow() != -1) {
							RemitoEntrada remitoEntrada = getElemento(getTabla().getSelectedRow());
							OperacionSobreRemitoEntradaHandler consultaREHandler = new OperacionSobreRemitoEntradaHandler(frame, remitoEntrada, true);
							consultaREHandler.showRemitoEntradaDialog();
						}
					}
				}

			});
			
			return tabla;
		}

		@Override
		protected void agregarElemento(RemitoEntrada elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_OBJ] = elemento;
			row[COL_REMITO] = elemento.toString();
			getTabla().addRow(row);
		}

		@Override
		protected RemitoEntrada getElemento(int fila) {
			return (RemitoEntrada)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			RemitoEntrada re = ((RemitoEntrada)getTabla().getValueAt(filaSeleccionada, COL_OBJ));
			List<OrdenDeTrabajo> odtList = getOdtFacade().getOdtEagerByRemitoList(re.getId());
			try {
				getRemitoEntradaFacade().checkEliminacionRemitoEntrada(re.getId(), odtList);
				RemitoEntrada remitoEntrada = getElemento(getTabla().getSelectedRow());
				OperacionSobreRemitoEntradaHandler consultaREHandler = new OperacionSobreRemitoEntradaHandler(frame, remitoEntrada, false);
				consultaREHandler.showRemitoEntradaDialog();
			} catch (ValidacionException e) {
				FWJOptionPane.showInformationMessage(frame, StringW.wordWrap(e.getMensajeError()), "Imposible Editar");
			}
		}

		@Override
		public boolean validarQuitar() {
			if(FWJOptionPane.showQuestionMessage(frame, "¿Está seguro que desea eliminar el remito?", "Confirmación") == FWJOptionPane.YES_OPTION) {
				RemitoEntrada remitoEntrada = getElemento(getTabla().getSelectedRow());
				try {
					if(remitoEntrada.getArticuloStock() != null || remitoEntrada.getPrecioMatPrima() != null) {
						getRemitoEntradaFacade().eliminarRemitoEntrada01OrCompraDeTela(remitoEntrada.getId(), GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
					} else {
						getRemitoEntradaFacade().checkEliminacionRemitoEntrada(remitoEntrada.getId(), new ArrayList<OrdenDeTrabajo>());
						getRemitoEntradaFacade().eliminarRemitoEntrada(remitoEntrada.getId());
					}
					FWJOptionPane.showInformationMessage(frame, "Remito borrado éxitosamente.", "Información");				
				} catch (ValidacionException e) {
					FWJOptionPane.showErrorMessage(JDialogConsultarRemitosEntrada.this, StringW.wordWrap(e.getMensajeError()), "Imposible Eliminar");
					return false;
				}
				return true;
			} else {
				return false;
			}
		}

	}

}
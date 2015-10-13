package main.acciones.compras;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.acciones.facturacion.OperacionSobreRemitoEntradaHandler;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;
import ar.com.textillevel.entidades.enums.ETipoFactura;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.facade.api.remote.RemitoEntradaProveedorFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogAgregarRemitoEntradaProveedor;
import ar.com.textillevel.gui.util.controles.LinkableLabel;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarProveedor;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogConsultarRemitosEntradaProveedor extends JDialog {

	private static final long serialVersionUID = -5312992576670286259L;

	private boolean acepto;

	private Date fechaDesde;
	private Date fechaHasta;
	private ETipoFactura tipoFactura;
	private Proveedor proveedor;
	private JButton btnCancelar;
	private JButton btnAceptar;
	private PanelDatePicker panelFechaDesde;
	private PanelDatePicker panelFechaHasta;
	private FWJTextField txtNroProveedor;
	private LinkableLabel lblElegirProveedor;
	private PanTablaRemitosEntrada panTablaRemitosEntrada;
	
	private RemitoEntradaProveedorFacadeRemote remitoEntradaFacade;

	private Frame frame;
	
	public JDialogConsultarRemitosEntradaProveedor(Frame padre) {
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
		pan.setLayout(new FlowLayout(FlowLayout.CENTER,5,2));
		pan.add(getPanelFechaDesde());
		pan.add(getPanelFechaHasta());
		pan.add(getPanelElegirProveedor());
		return pan;
	}

	private void setUpScreen() {
		setTitle("Consulta de Remitos de Entrada de Proveedor");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(700, 500));
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
					if(!getPanelFechaDesde().getDate().after(getPanelFechaHasta().getDate())){
						setFechaDesde(new java.sql.Date(getPanelFechaDesde().getDate().getTime()));
						setFechaHasta(new java.sql.Date(getPanelFechaHasta().getDate().getTime()));
						setAcepto(true);
						dispose();
					}else{
						FWJOptionPane.showErrorMessage(JDialogConsultarRemitosEntradaProveedor.this, "La 'fecha desde' debe ser mayor que la 'fecha hasta'", "Error");
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

	private JPanel getPanelElegirProveedor(){
		JPanel panProveedor = new JPanel();
		panProveedor.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
		panProveedor.add(new JLabel("Proveedor: "));
		panProveedor.add(getTxtNroProveedor());
		panProveedor.add(getLblelegirProveedor());
		return panProveedor;
	}

	private FWJTextField getTxtNroProveedor() {
		if (txtNroProveedor == null) {
			txtNroProveedor = new FWJTextField();
			txtNroProveedor.setEditable(false);
			txtNroProveedor.setPreferredSize(new Dimension(50, 20));
		}
		return txtNroProveedor;
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
							setProveedor(proveedorElegido);
							getTxtNroProveedor().setText(String.valueOf(proveedorElegido.getNombreCorto()));
							buscarRemitos();
						}
					}
				}
			};
		}
		return lblElegirProveedor;
	}
	
	private void buscarRemitos() {
		if(getProveedor() != null) {
			getPanTablaRemitosEntrada().getTabla().removeAllRows();
			List<RemitoEntradaProveedor> remitoEntradaList = getRemitoEntradaFacade().getRemitoEntradaByFechasAndProveedor(new Date(getPanelFechaDesde().getDate().getTime()), DateUtil.getManiana(new Date(getPanelFechaHasta().getDate().getTime())), getProveedor().getId());
			getPanTablaRemitosEntrada().agregarElementos(remitoEntradaList);
		}
	}

	public RemitoEntradaProveedorFacadeRemote getRemitoEntradaFacade() {
		if(remitoEntradaFacade == null) {
			remitoEntradaFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaProveedorFacadeRemote.class);
		}
		return remitoEntradaFacade;
	}
	
	private PanTablaRemitosEntrada getPanTablaRemitosEntrada() {
		if(panTablaRemitosEntrada == null) {
			panTablaRemitosEntrada = new PanTablaRemitosEntrada();
		}
		return panTablaRemitosEntrada;
	}

	private class PanTablaRemitosEntrada extends PanelTabla<RemitoEntradaProveedor> {

		private static final long serialVersionUID = 2228594831259897097L;

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
							RemitoEntradaProveedor remitoEntrada = getElemento(getTabla().getSelectedRow());
							remitoEntrada = getRemitoEntradaFacade().getByIdEager(remitoEntrada.getId());
							if(remitoEntrada.getRemitoEntrada() == null) {//Se trata de un remito de entrada NO generado en forma indirecta
								JDialogAgregarRemitoEntradaProveedor dialogoRemitoEntrada = new JDialogAgregarRemitoEntradaProveedor(frame,  true, remitoEntrada);
								GuiUtil.centrar(dialogoRemitoEntrada);
								dialogoRemitoEntrada.setVisible(true);
							} else {//Se trata de un remito de entrada generado en forma indirecta desde una operación de compra de Tela
								OperacionSobreRemitoEntradaHandler handler = new OperacionSobreRemitoEntradaHandler(frame, remitoEntrada.getRemitoEntrada(), true);
								handler.showRemitoEntradaDialog();
							}
						}
					}
				}

			});
			
			return tabla;
		}

		@Override
		protected void agregarElemento(RemitoEntradaProveedor elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_OBJ] = elemento;
			row[COL_REMITO] = elemento.toString();
			getTabla().addRow(row);
		}

		@Override
		protected RemitoEntradaProveedor getElemento(int fila) {
			return (RemitoEntradaProveedor)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			RemitoEntradaProveedor re = ((RemitoEntradaProveedor)getTabla().getValueAt(filaSeleccionada, COL_OBJ));
			try {
				getRemitoEntradaFacade().checkEliminacionEdicionRemitoEntradaProveedor(re);
				RemitoEntradaProveedor reEager = getRemitoEntradaFacade().getByIdEager(re.getId());
				JDialogAgregarRemitoEntradaProveedor dialogo = new JDialogAgregarRemitoEntradaProveedor(frame, false, reEager);
				GuiUtil.centrar(dialogo);
				dialogo.setVisible(true);
			} catch (ValidacionException e) {
				FWJOptionPane.showInformationMessage(frame, StringW.wordWrap(e.getMensajeError()), "Imposible Editar");
				return;
			}
		}

		@Override
		public boolean validarQuitar() {
			if(FWJOptionPane.showQuestionMessage(frame, "¿Está seguro que desea eliminar el remito?", "Confirmación") == FWJOptionPane.YES_OPTION) { 
				RemitoEntradaProveedor remitoEntrada = getElemento(getTabla().getSelectedRow());
				try {
					getRemitoEntradaFacade().eliminarRemitoEntrada(remitoEntrada.getId());
					FWJOptionPane.showInformationMessage(frame, "Remito borrado éxitosamente.", "Información");
				} catch (ValidacionException e) {
					FWJOptionPane.showErrorMessage(JDialogConsultarRemitosEntradaProveedor.this, StringW.wordWrap(e.getMensajeError()), "Imposible Eliminar");
					return false;
				}
				return true;
			} else {
				return false;
			}
		}
	}

	private Proveedor getProveedor() {
		return proveedor;
	}

	private void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

}

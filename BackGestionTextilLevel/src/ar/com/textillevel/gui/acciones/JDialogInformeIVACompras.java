package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.acciones.informes.IvaComprasParam;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoFactura;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.to.ivacompras.IVAComprasTO;
import ar.com.textillevel.facade.api.remote.FacturaProveedorFacadeRemote;
import ar.com.textillevel.gui.util.controles.LinkableLabel;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarProveedor;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogInformeIVACompras extends JDialog {

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
	private CLJTextField txtNroCliente;
	private LinkableLabel lblElegirProveedor;
	
	private Frame frame;
	
	public JDialogInformeIVACompras(Frame padre) {
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
		pan.add(getPanelElegirProveedor());
		return pan;
	}

	private void setUpScreen() {
		setTitle("Informe de IVA - Compras");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(600, 150));
		setResizable(true);
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
						
						
						Date fechaDesde = getFechaDesde();
						Date fechaHasta = getFechaHasta();
						Proveedor proveedor = getProveedor();
						IVAComprasTO ivaCompras = GTLBeanFactory.getInstance().getBean2(FacturaProveedorFacadeRemote.class).calcularIVACompras(DateUtil.getManiana(fechaDesde),  fechaHasta, proveedor);
						if(!ivaCompras.getItems().isEmpty()) {
							IvaComprasParam ivaComprasParam = new IvaComprasParam(fechaDesde,  fechaHasta, proveedor);
							JDialogReporteIVAComprasPreview jDialogReporteIVAComprasPreview = new JDialogReporteIVAComprasPreview(frame, ivaCompras, ivaComprasParam);
							jDialogReporteIVAComprasPreview.setVisible(true);
						}else{
							CLJOptionPane.showWarningMessage(frame, "No se han encontrado resultados", "Advertencia");
						}
					}else{
						CLJOptionPane.showErrorMessage(JDialogInformeIVACompras.this, "La 'fecha desde' debe ser mayor que la 'fecha hasta'", "Error");
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

	private JPanel getPanelElegirProveedor(){
		JPanel panProveedor = new JPanel();
		panProveedor.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
		panProveedor.add(new JLabel("Proveedor: "));
		panProveedor.add(getTxtNroCliente());
		panProveedor.add(getLblelegirProveedor());
		return panProveedor;
	}

	private CLJTextField getTxtNroCliente() {
		if (txtNroCliente == null) {
			txtNroCliente = new CLJTextField();
			txtNroCliente.setEditable(false);
			txtNroCliente.setPreferredSize(new Dimension(50, 20));
		}
		return txtNroCliente;
	}
	
	private LinkableLabel getLblelegirProveedor() {
		if (lblElegirProveedor == null) {
			lblElegirProveedor = new LinkableLabel("Elegir proveedor") {

				private static final long serialVersionUID = 580819185565135378L;

				@Override
				public void labelClickeada(MouseEvent e) {
					if (e.getClickCount() == 1) {
						JDialogSeleccionarProveedor dialogSeleccionarProveedor = new JDialogSeleccionarProveedor(frame);
						GuiUtil.centrar(dialogSeleccionarProveedor);
						dialogSeleccionarProveedor.setVisible(true);
						Proveedor proveedorElegido = dialogSeleccionarProveedor.getProveedor();
						if (proveedorElegido != null) {
							setProveedor(proveedorElegido);
							getTxtNroCliente().setText(String.valueOf(proveedorElegido.getNombreCorto()));
						}
					}
				}
			};
		}
		return lblElegirProveedor;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

}

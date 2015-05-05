package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import main.GTLGlobalCache;
import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.CLJTextArea;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ImpuestoItemProveedor;
import ar.com.textillevel.entidades.documentos.pagopersona.FacturaPersona;
import ar.com.textillevel.entidades.enums.ETipoFactura;
import ar.com.textillevel.entidades.gente.Persona;
import ar.com.textillevel.facade.api.remote.FacturaPersonaFacadeRemote;
import ar.com.textillevel.gui.acciones.proveedor.JDialogSeleccionarCrearImpuesto;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogAgregarFacturaPersona extends JDialog {

	private static final long serialVersionUID = 8227095054206482927L;

	private PanelDatePicker panelFecha;
	private CLJTextField txtTotal;
	private CLJTextArea txtDetalle;
	private CLJTextField txtPersona;
	private CLJNumericTextField txtNroFactura;
	private JButton btnGuardar;
	private JButton btnSalir;
	private PanelTablaImpuestosFacturaPersona panelTabla;
	private JComboBox cmbTipoFactura;
	private CLJTextField txtMontoFinal;
	
	
	
	
	private Persona persona;
	private FacturaPersona factura;
	private FacturaPersonaFacadeRemote facturaFacade;
	
	private List<ImpuestoItemProveedor> impuestosElegidos;
	private Double totalImpuestos;
	
	private boolean edicion;

	public JDialogAgregarFacturaPersona(Frame padre, Persona persona) {
		super(padre);
		setPersona(persona);
		setUpComponentes();
		setUpScreen();
		getTxtPersona().setText(persona.getRazonSocial());
		getTxtNroFactura().requestFocus();
		setImpuestosElegidos(new ArrayList<ImpuestoItemProveedor>());
		totalImpuestos = 0d;
	}
	
	public JDialogAgregarFacturaPersona(Frame padre, FacturaPersona factura, boolean consulta) {
		super(padre);
		setPersona(factura.getPersona());
		setFactura(factura);
		setEdicion(!consulta);
		setUpComponentes();
		setUpScreen();
		getTxtPersona().setText(getPersona().getRazonSocial());
		getTxtDetalle().setText(factura.getDetalle());
		getTxtTotal().setText(String.valueOf(factura.getMontoSinImpuestos().doubleValue()));
		getPanelFecha().setSelectedDate(factura.getFecha());
		getCmbTipoFactura().setSelectedItem(factura.getTipoFactura());
		getTxtNroFactura().setText(String.valueOf(factura.getNroFactura()));
		if(consulta){
			getTxtDetalle().setEnabled(false);
			getTxtTotal().setEnabled(false);
			GuiUtil.setEstadoPanel(getPanelFecha(), false);
			getTxtNroFactura().setEnabled(false);
			getBtnGuardar().setEnabled(false);
			getCmbTipoFactura().setEnabled(false);
			GuiUtil.setEstadoPanel(getPanelTabla(), false);
		}
		setImpuestosElegidos(factura.getImpuestos());
		getPanelTabla().refreshTable();
	}

	private void setUpScreen() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("Factura de persona");
		setModal(true);
		setResizable(false);
		pack();
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		add(getPanelNorte(), BorderLayout.NORTH);
		add(getPanelCentro(), BorderLayout.CENTER);
		add(getPanelSur(), BorderLayout.SOUTH);
	}

	private JPanel getPanelNorte() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(new JLabel("Persona: "));
		panel.add(getTxtPersona());
		panel.add(new JLabel("Nro factura: "));
		panel.add(getTxtNroFactura());
		panel.add(new JLabel("Factura: "));
		panel.add(getCmbTipoFactura());
		panel.add(getPanelFecha());
		return panel;
	}

	private JPanel getPanelCentro() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(new JSeparator(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 7, 1, 1, 1));
		panel.add(new JLabel("Detalle: "), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		JScrollPane jsp = new JScrollPane(getTxtDetalle(), JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(jsp, GenericUtils.createGridBagConstraints(1, 1,  GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 2, 1, 1));
		panel.add(getPanelDerecha(), GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 3, 1, 1));
		panel.add(new JSeparator(), GenericUtils.createGridBagConstraints(0, 4, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 7, 1, 1, 1));
		return panel;
	}
	
	private JPanel getPanelDerecha(){
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(new JLabel("Monto: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 0, 1, 1));
		panel.add(getTxtTotal(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 1, 1, 1, 1));
		panel.add(getPanelTabla(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 2, 1, 1));
		panel.add(new JLabel("Monto total: "), GenericUtils.createGridBagConstraints(0, 4, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 0, 1, 1));
		panel.add(getTxtMontoFinal(), GenericUtils.createGridBagConstraints(1, 4, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 1, 1, 1, 1));
		return panel;
	}

	private JPanel getPanelSur() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnGuardar());
		panel.add(getBtnSalir());
		return panel;
	}

	public PanelDatePicker getPanelFecha() {
		if(panelFecha == null){
			panelFecha = new PanelDatePicker();
			panelFecha.setCaption("Fecha: ");
		}
		return panelFecha;
	}

	public CLJTextField getTxtTotal() {
		if(txtTotal == null){
			txtTotal = new CLJTextField();
			txtTotal.setPreferredSize(new Dimension(120, 20));
			txtTotal.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					getPanelTabla().refreshTable();
				}
			});
		}
		return txtTotal;
	}

	public CLJTextArea getTxtDetalle() {
		if(txtDetalle == null){
			txtDetalle = new CLJTextArea(500);
			txtDetalle.setLineWrap(true);
			txtDetalle.setPreferredSize(new Dimension(300, 170));
		}
		return txtDetalle;
	}

	public CLJTextField getTxtPersona() {
		if(txtPersona == null){
			txtPersona = new CLJTextField();
			txtPersona.setEditable(false);
			txtPersona.setPreferredSize(new Dimension(220, 20));
		}
		return txtPersona;
	}

	public CLJNumericTextField getTxtNroFactura() {
		if(txtNroFactura == null){
			txtNroFactura = new CLJNumericTextField();
			txtNroFactura.setPreferredSize(new Dimension(120, 20));
		}
		return txtNroFactura;
	}

	public JButton getBtnGuardar() {
		if(btnGuardar == null){
			btnGuardar = new JButton("Guardar");
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					guardarFactura();
				}
			});
		}
		return btnGuardar;
	}

	private void guardarFactura() {
		if(validar()){
			FacturaPersona fp = isEdicion()?getFactura(): new FacturaPersona();
			fp.setDetalle(getTxtDetalle().getText().toUpperCase());
			fp.setFecha(new java.sql.Date(getPanelFecha().getDate().getTime()));
			fp.setMonto(new BigDecimal(Double.valueOf(getTxtMontoFinal().getText())));
			fp.setMontoSinImpuestos(new BigDecimal(Double.valueOf(getTxtTotal().getText())));
			fp.setNroFactura(getTxtNroFactura().getValue());
			fp.setPersona(getPersona());
			fp.setTipoFactura((ETipoFactura)getCmbTipoFactura().getSelectedItem());
			fp.setImpuestos(getImpuestosElegidos());
			String usrName = GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName();
			if(isEdicion()){
				getFacturaFacade().editarFactura(fp, usrName);
			}else{
				fp.setUsuarioCreador(usrName);
				getFacturaFacade().guardarFactura(fp, usrName);
			}
			CLJOptionPane.showInformationMessage(this, "La factura se ha guardado con éxito", "Información");
			dispose();
		}
	}

	private boolean validar() {
		if(getTxtNroFactura().getText().trim().length()==0){
			CLJOptionPane.showErrorMessage(this, "Debe completar el número de factura", "Error");
			getTxtNroFactura().requestFocus();
			return false;
		}
		if(getTxtDetalle().getText().trim().length()==0){
			CLJOptionPane.showErrorMessage(this, "Debe completar el detalle de la factura", "Error");
			getTxtDetalle().requestFocus();
			return false;
		}
		if(getTxtTotal().getText().trim().length()==0){
			CLJOptionPane.showErrorMessage(this, "Debe completar el monto de la factura", "Error");
			getTxtTotal().requestFocus();
			return false;
		}
		if(!GenericUtils.esNumerico(getTxtTotal().getText().trim())){
			CLJOptionPane.showErrorMessage(this, "El monto debe ser numérico", "Error");
			getTxtTotal().requestFocus();
			return false;
		}
		if(!isEdicion() && getFacturaFacade().existeNroFacturaParaPersona(getTxtNroFactura().getValue(), getPersona())){
			CLJOptionPane.showErrorMessage(this, "Ya existe el número de factura ingresado para dicha persona.", "Error");
			getTxtNroFactura().requestFocus();
			return false;
		}
		return true;
	}

	public JButton getBtnSalir() {
		if(btnSalir == null){
			btnSalir = new  JButton("Salir");
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnSalir;
	}

	public FacturaPersonaFacadeRemote getFacturaFacade() {
		if(facturaFacade == null){
			facturaFacade = GTLBeanFactory.getInstance().getBean2(FacturaPersonaFacadeRemote.class);
		}
		return facturaFacade;
	}

	public Persona getPersona() {
		return persona;
	}
	
	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	
	public boolean isEdicion() {
		return edicion;
	}

	
	public void setEdicion(boolean edicion) {
		this.edicion = edicion;
	}

	
	public FacturaPersona getFactura() {
		return factura;
	}

	
	public void setFactura(FacturaPersona factura) {
		this.factura = factura;
	}
	
	private class PanelTablaImpuestosFacturaPersona extends PanelTabla<ImpuestoItemProveedor>{
		
		private static final long serialVersionUID = -7723028359712183037L;

		private static final int CANT_COLS = 4;
		private static final int COL_NOMBRE_IMPUESTO = 0;
		private static final int COL_PORC_IMPUESTO = 1;
		private static final int COL_MONTO_IMPUESTO = 2;
		private static final int COL_OBJ = 3;
		
		@Override
		protected void agregarElemento(ImpuestoItemProveedor elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_NOMBRE_IMPUESTO] = elemento.getNombre();
			row[COL_PORC_IMPUESTO] = String.valueOf(elemento.getPorcDescuento())+"%";
			if(getTxtTotal().getText().trim().length()>0){
				Double total = Double.valueOf(getTxtTotal().getText());
				double montoImpuesto = total*elemento.getPorcDescuento()/100;
				row[COL_MONTO_IMPUESTO] = montoImpuesto;
			}else{
				row[COL_MONTO_IMPUESTO] ="";
			}
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0,CANT_COLS);
			tabla.setStringColumn(COL_NOMBRE_IMPUESTO, "Impuesto", 100, 100, true);
			tabla.setStringColumn(COL_PORC_IMPUESTO, "Porc.", 70, 70, true);
			tabla.setFloatColumn(COL_MONTO_IMPUESTO, "Monto", 90, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setAlignment(COL_NOMBRE_IMPUESTO, CLJTable.LEFT_ALIGN);
			tabla.setAlignment(COL_PORC_IMPUESTO, CLJTable.RIGHT_ALIGN);
			tabla.setAlignment(COL_MONTO_IMPUESTO, CLJTable.RIGHT_ALIGN);
			tabla.setHeaderAlignment(COL_NOMBRE_IMPUESTO, CLJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_PORC_IMPUESTO, CLJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_MONTO_IMPUESTO, CLJTable.CENTER_ALIGN);
			return tabla;
		}

		@Override
		protected ImpuestoItemProveedor getElemento(int fila) {
			return (ImpuestoItemProveedor)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}
		
		@Override
		public boolean validarAgregar() {
			if(getTxtTotal().getText().trim().length()>0){
				JDialogSeleccionarCrearImpuesto dialogSeleccionarCrearImpuesto = new JDialogSeleccionarCrearImpuesto(JDialogAgregarFacturaPersona.this, getImpuestosElegidos());
				dialogSeleccionarCrearImpuesto.setVisible(true);
				if(dialogSeleccionarCrearImpuesto.isAcepto()) {
					List<ImpuestoItemProveedor> impuestos = dialogSeleccionarCrearImpuesto.getImpuestosSelectedResult();
					getImpuestosElegidos().clear();
					getImpuestosElegidos().addAll(impuestos);
					refreshTable();
				}
			}else{
				CLJOptionPane.showErrorMessage(JDialogAgregarFacturaPersona.this, "Debe cargar el monto", "Error");
			}
			return false;
		}

		private void refreshTable() {
			getTabla().removeAllRows();
			setTotalImpuestos(0d);
			for(ImpuestoItemProveedor im : getImpuestosElegidos()){
				if(getTxtTotal().getText().trim().length()>0){
					double total = Double.valueOf(getTxtTotal().getText().trim());
					double montoImpuesto = total*im.getPorcDescuento()/100;
					setTotalImpuestos(getTotalImpuestos()+montoImpuesto);
				}
				agregarElemento(im);
			}
			updateTotales();
		}
		
		@Override
		public boolean validarQuitar() {
			ImpuestoItemProveedor impuesto = getElemento(getTabla().getSelectedRow());
			getImpuestosElegidos().remove(impuesto);
			Double quitar = (Double) getTabla().getValueAt(getTabla().getSelectedRow(), COL_MONTO_IMPUESTO);
			setTotalImpuestos(getTotalImpuestos()-quitar);
			updateTotales();
			return true;
		}
	}
	
	private void updateTotales(){
		if(getTxtTotal().getText().trim().length()>0){
			Double monto = Double.valueOf(getTxtTotal().getText()) + getTotalImpuestos();
			getTxtMontoFinal().setText(String.valueOf(monto));
		}else{
			getTxtMontoFinal().setText("");
		}
	}
	
	public PanelTablaImpuestosFacturaPersona getPanelTabla() {
		if(panelTabla == null){
			panelTabla = new PanelTablaImpuestosFacturaPersona();
			panelTabla.setPreferredSize(new Dimension(300, 150));
		}
		return panelTabla;
	}

	public JComboBox getCmbTipoFactura() {
		if(cmbTipoFactura == null){
			cmbTipoFactura = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoFactura, Arrays.asList(ETipoFactura.values()), true);
		}
		return cmbTipoFactura;
	}

	
	public CLJTextField getTxtMontoFinal() {
		if(txtMontoFinal == null){
			txtMontoFinal = new CLJTextField();
			txtMontoFinal.setEditable(false);
		}
		return txtMontoFinal;
	}

	
	public List<ImpuestoItemProveedor> getImpuestosElegidos() {
		return impuestosElegidos;
	}

	
	public void setImpuestosElegidos(List<ImpuestoItemProveedor> impuestosElegidos) {
		this.impuestosElegidos = impuestosElegidos;
	}

	
	public Double getTotalImpuestos() {
		return totalImpuestos;
	}

	
	public void setTotalImpuestos(Double totalImpuestos) {
		this.totalImpuestos = totalImpuestos;
	}
}

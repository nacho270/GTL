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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ImpuestoItemProveedor;
import ar.com.textillevel.entidades.documentos.pagopersona.ItemCorreccionFacturaPersona;
import ar.com.textillevel.entidades.documentos.pagopersona.ItemCorreccionPersResumen;
import ar.com.textillevel.entidades.documentos.pagopersona.NotaDebitoPersona;
import ar.com.textillevel.entidades.gente.Persona;
import ar.com.textillevel.facade.api.remote.CorreccionFacturaPersonaFacadeRemote;
import ar.com.textillevel.gui.acciones.proveedor.JDialogSeleccionarCrearImpuesto;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.util.GTLBeanFactory;
import main.GTLGlobalCache;

public class JDialogEditarNDPersona extends JDialog {

	private static final long serialVersionUID = 8227095054206482927L;

	private PanelDatePicker panelFecha;
	private FWJTextField txtPersona;
	private FWJNumericTextField txtNroND;
	private JButton btnGuardar;
	private JButton btnSalir;
	private PanelTablaItemsCorreccionFacturaPersona panelTabla;
	private FWJTextField txtMontoFinal;

	private Persona persona;
	private NotaDebitoPersona nd;
	private CorreccionFacturaPersonaFacadeRemote correccionFacade;
	
	private boolean edicion;

	public JDialogEditarNDPersona(Frame padre, NotaDebitoPersona nd, boolean consulta) {
		super(padre);
		setPersona(nd.getPersona());
		setND(nd);
		setEdicion(!consulta);
		setUpComponentes();
		setUpScreen();
		getTxtPersona().setText(getPersona().getRazonSocial());
		getPanelFecha().setSelectedDate(nd.getFechaIngreso());
		getTxtNroND().setText(String.valueOf(nd.getNroCorreccion()));
		if(consulta){
			GuiUtil.setEstadoPanel(getPanelFecha(), false);
			getTxtNroND().setEnabled(false);
			getBtnGuardar().setEnabled(false);
			GuiUtil.setEstadoPanel(getPanelTabla(), false);
		}
		getPanelTabla().refreshTable();
	}

	private void setUpScreen() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("Nota de Débito de persona");
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
		panel.add(new JLabel("Nro. Nota Débito: "));
		panel.add(getTxtNroND());
		panel.add(getPanelFecha());
		return panel;
	}

	private JPanel getPanelCentro() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(new JSeparator(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 7, 1, 1, 1));
		panel.add(getPanelDerecha(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		panel.add(new JSeparator(), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 7, 1, 1, 1));
		return panel;
	}
	
	private JPanel getPanelDerecha(){
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(getPanelTabla(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 2, 1, 1));
		panel.add(new JLabel("Monto total: "), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 0, 1, 1));
		panel.add(getTxtMontoFinal(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 1, 1, 1, 1));
		return panel;
	}

	private JPanel getPanelSur() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnGuardar());
		panel.add(getBtnSalir());
		return panel;
	}

	private PanelDatePicker getPanelFecha() {
		if(panelFecha == null){
			panelFecha = new PanelDatePicker();
			panelFecha.setCaption("Fecha: ");
		}
		return panelFecha;
	}

	private FWJTextField getTxtPersona() {
		if(txtPersona == null){
			txtPersona = new FWJTextField();
			txtPersona.setEditable(false);
			txtPersona.setPreferredSize(new Dimension(270, 20));
		}
		return txtPersona;
	}

	private FWJNumericTextField getTxtNroND() {
		if(txtNroND == null){
			txtNroND = new FWJNumericTextField();
			txtNroND.setPreferredSize(new Dimension(100, 20));
		}
		return txtNroND;
	}

	private JButton getBtnGuardar() {
		if(btnGuardar == null){
			btnGuardar = new JButton("Guardar");
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					guardarND();
				}
			});
		}
		return btnGuardar;
	}

	private void guardarND() {
		if(validar()) {
			capturarSetearDatos();
			String usrName = GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName();
			getCorreccionFacade().guardarCorreccionYGenerarMovimiento(getND(), usrName, null);
			FWJOptionPane.showInformationMessage(this, "La nota de débito se ha guardado con éxito", "Información");
			dispose();
		}
	}

	private void capturarSetearDatos() {
		getND().getItemsCorreccion().clear();
		getND().getItemsCorreccion().addAll(getPanelTabla().getElementos());
		getND().setFechaIngreso(new java.sql.Date(getPanelFecha().getDate().getTime()));
		getND().setNroCorreccion(getTxtNroND().getText().trim());
	}

	private boolean validar() {
		if(getTxtNroND().getText().trim().length()==0) {
			FWJOptionPane.showErrorMessage(this, "Debe completar el número de Nota de Débito", "Error");
			getTxtNroND().requestFocus();
			return false;
		}
		if(!isEdicion() && getTxtNroND().getValue() != null && getTxtNroND().getValue() > 0 && getCorreccionFacade().existeNroNDParaPersona(getTxtNroND().getValue(), getPersona())){
			FWJOptionPane.showErrorMessage(this, "Ya existe el número de Nota de Débito ingresado para dicha persona.", "Error");
			getTxtNroND().requestFocus();
			return false;
		}
		String msg = getPanelTabla().validar();
		if(!StringUtil.isNullOrEmpty(msg)) {
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap(msg), "Error");
			return false;
		}
		return true;
	}

	private JButton getBtnSalir() {
		if(btnSalir == null){
			btnSalir = new  JButton("Salir");
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setND(null);
					dispose();
				}
			});
		}
		return btnSalir;
	}

	private CorreccionFacturaPersonaFacadeRemote getCorreccionFacade() {
		if(correccionFacade == null){
			correccionFacade = GTLBeanFactory.getInstance().getBean2(CorreccionFacturaPersonaFacadeRemote.class);
		}
		return correccionFacade;
	}

	private Persona getPersona() {
		return persona;
	}
	
	private void setPersona(Persona persona) {
		this.persona = persona;
	}

	
	public boolean isEdicion() {
		return edicion;
	}

	
	public void setEdicion(boolean edicion) {
		this.edicion = edicion;
	}

	public NotaDebitoPersona getND() {
		return nd;
	}

	private void setND(NotaDebitoPersona nd) {
		this.nd = nd;
	}

	private class PanelTablaItemsCorreccionFacturaPersona extends PanelTabla<ItemCorreccionFacturaPersona> {
		
		private static final long serialVersionUID = -7723028359712183037L;

		private static final int CANT_COLS = 6;
		private static final int COL_DESCR = 0;
		private static final int COL_MONTO = 1;
		private static final int COL_IMPUESTOS = 2;
		private static final int COL_TOTAL = 3;
		private static final int COL_DETALLE_IMPUESTOS = 4;
		private static final int COL_OBJ = 5;

		private JButton btnSelImpuestos;

		public PanelTablaItemsCorreccionFacturaPersona() {
			super();
			agregarBoton(getBtnSelImpuestos());
		}

		public String validar() {
			if(getTabla().getRowCount() <= 0) {
				return "Debe ingresar al menos un item.";
			}
			for(int i=0; i<getTabla().getRowCount(); i++) {
				String msgElem = validarElemento(i);
				if(!StringUtil.isNullOrEmpty(msgElem)) {
					return msgElem;
				}
			}
			return null;
		}

		private JButton getBtnSelImpuestos() {
			if(btnSelImpuestos == null) {
				btnSelImpuestos = new JButton("I");
				btnSelImpuestos.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						handleSeleccionImpuestos();
					}

				});
				
				btnSelImpuestos.setEnabled(false);

			}
			return btnSelImpuestos;
		}

		private void handleSeleccionImpuestos() {
			int selectedRowCount = getTabla().getSelectedRowCount();
			if(selectedRowCount > 0) {
				List<ItemCorreccionFacturaPersona> elementos = new ArrayList<ItemCorreccionFacturaPersona>();
				List<ImpuestoItemProveedor> impuestos = new ArrayList<ImpuestoItemProveedor>();
				int[] selectedRows = getTabla().getSelectedRows();
				for(int i = 0; i < selectedRows.length; i ++) {
					ItemCorreccionFacturaPersona itemFactura = getElemento(selectedRows[i]);
					elementos.add(itemFactura);
					impuestos.addAll(itemFactura.getImpuestos());
				}
				JDialogSeleccionarCrearImpuesto dialogSeleccionarCrearImpuesto = new JDialogSeleccionarCrearImpuesto(JDialogEditarNDPersona.this, impuestos);
				dialogSeleccionarCrearImpuesto.setVisible(true);
				if(dialogSeleccionarCrearImpuesto.isAcepto()) {
					for(ItemCorreccionFacturaPersona elemento : elementos) {
						elemento.getImpuestos().clear();
					}
					List<ImpuestoItemProveedor> impuestosSelectedResult = dialogSeleccionarCrearImpuesto.getImpuestosSelectedResult();
					int indexRows = 0;
					for(ItemCorreccionFacturaPersona elemento : elementos) {
						elemento.getImpuestos().addAll(impuestosSelectedResult);
						
						getTabla().setValueAt(elemento.getImporteImpuestos().doubleValue(), selectedRows[indexRows], COL_IMPUESTOS);
						getTabla().setValueAt(elemento.getImporteTotalConImpuestos().doubleValue(), selectedRows[indexRows], COL_TOTAL);
						
						indexRows++;
					}
					updateTotales();
				}
			}
		}		
		
		@Override
		protected void agregarElemento(ItemCorreccionFacturaPersona elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_DESCR] = elemento.getDescripcion();
			row[COL_DETALLE_IMPUESTOS] = StringUtil.getCadena(elemento.getImpuestos(), "-");
			row[COL_MONTO] = elemento.getImporte().doubleValue();
			row[COL_IMPUESTOS] = elemento.getImporteImpuestos().doubleValue();
			row[COL_TOTAL] = elemento.getImporteTotalConImpuestos().doubleValue();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS) {

				private static final long serialVersionUID = 1L;

				@Override
				public void cellEdited(int cell, int row) {
					if(cell == COL_MONTO) {
						String valor = ((String) getValueAt(row, cell)).trim();
						if (GenericUtils.esNumerico(valor)) {
							double monto = Double.valueOf(valor);
							ItemCorreccionFacturaPersona elemento = getElemento(row);
							elemento.setImporte(new BigDecimal(monto)); //es lo mismo

							getTabla().setValueAt(elemento.getImporteTotalConImpuestos().doubleValue(), row, COL_TOTAL);
							getTabla().setValueAt(elemento.getImporteImpuestos().doubleValue(), row, COL_IMPUESTOS);
							
							updateTotales();
						}
					}
					if(cell == COL_DESCR) {
						String descr = ((String) getValueAt(row, cell)).trim();
						ItemCorreccionFacturaPersona elemento = getElemento(row);
						elemento.setDescripcion(descr);
					}
				}

				@Override
				public void newRowSelected(int newRow, int oldRow) {
					getBtnSelImpuestos().setEnabled(!modoConsulta && newRow != -1);
				}
				
			};
			tabla.setStringColumn(COL_DESCR, "Detalle", 300, 300, false);
			tabla.setFloatColumn(COL_MONTO, "Monto", 50, false);
			tabla.setFloatColumn(COL_IMPUESTOS, "Total Impuestos", 90, true);
			tabla.setFloatColumn(COL_TOTAL, "Total", 50, true);
			tabla.setStringColumn(COL_DETALLE_IMPUESTOS, "Detalle Impuestos", 110, 110, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setAlignment(COL_MONTO, FWJTable.RIGHT_ALIGN);
			tabla.setAlignment(COL_DETALLE_IMPUESTOS, FWJTable.LEFT_ALIGN);
			tabla.setAlignment(COL_IMPUESTOS, FWJTable.RIGHT_ALIGN);
			tabla.setAlignment(COL_TOTAL, FWJTable.RIGHT_ALIGN);
			tabla.setHeaderAlignment(COL_DESCR, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_DETALLE_IMPUESTOS, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_MONTO, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_IMPUESTOS, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_TOTAL, FWJTable.CENTER_ALIGN);
			return tabla;
		}

		@Override
		protected ItemCorreccionFacturaPersona getElemento(int fila) {
			return (ItemCorreccionFacturaPersona)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			ItemCorreccionFacturaPersona elemento = getElemento(fila);
			if(elemento.getImporte() == null || elemento.getImporte().doubleValue() <= 0d) {
				return "Debe ingresar un monto mayor a cero para todos los ítems.";
			}
			if(StringUtil.isNullOrEmpty(elemento.getDescripcion())) {
				return "Debe ingresar una descripción para todos los ítems";
			}
			return null;
		}

		@Override
		public void setModoConsulta(boolean modoConsulta) {
			super.setModoConsulta(modoConsulta);
			getBtnSelImpuestos().setEnabled(false);
		}

		@Override
		public boolean validarAgregar() {
			return true;
		}

		@Override
		protected void botonAgregarPresionado() {
			getTabla().removeRow(getTabla().getRowCount()-1);
			ItemCorreccionPersResumen it = new ItemCorreccionPersResumen();
			agregarElemento(it);
			getND().getItemsCorreccion().add(it);
		}

		private void refreshTable() {
			getTabla().removeAllRows();
			for(ItemCorreccionFacturaPersona im : getND().getItemsCorreccion()){
				agregarElemento(im);
			}
			updateTotales();
		}
	
		@Override
		public boolean validarQuitar() {
			return true;
		}

		@Override
		protected void botonQuitarPresionado() {
			updateTotales();
		}
		
	}

	private void updateTotales() {
		BigDecimal total = new BigDecimal(0d);
		for(ItemCorreccionFacturaPersona ic : getPanelTabla().getElementos()) {
			total = total.add(ic.getImporteTotalConImpuestos());
		}
		getND().setMontoTotal(total);
		getND().setMontoFaltantePorPagar(total);
		getTxtMontoFinal().setText(total.doubleValue() + "");
	}
	
	private PanelTablaItemsCorreccionFacturaPersona getPanelTabla() {
		if(panelTabla == null){
			panelTabla = new PanelTablaItemsCorreccionFacturaPersona();
			panelTabla.setPreferredSize(new Dimension(400, 150));
		}
		return panelTabla;
	}
	
	private FWJTextField getTxtMontoFinal() {
		if(txtMontoFinal == null){
			txtMontoFinal = new FWJTextField();
			txtMontoFinal.setEditable(false);
		}
		return txtMontoFinal;
	}

}
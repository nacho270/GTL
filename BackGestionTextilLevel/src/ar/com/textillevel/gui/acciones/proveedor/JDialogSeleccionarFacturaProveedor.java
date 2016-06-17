package ar.com.textillevel.gui.acciones.proveedor;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import main.GTLGlobalCache;
import main.acciones.compras.OperacionSobreFacturaProveedorHandler;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemFacturaMateriaPrima;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemFacturaProveedor;
import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.facade.api.remote.FacturaProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.PrecioMateriaPrimaFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.gui.util.panels.PanelSeleccionarElementoEvent;
import ar.com.textillevel.gui.util.panels.PanelSeleccionarElementoEventListener;
import ar.com.textillevel.gui.util.panels.PanelSeleccionarElementos;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogSeleccionarFacturaProveedor extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel panDetalle;
	private JTextField txtRazSoc;
	private JFormattedTextField txtNroFactura;
	private JButton btnCerrar;
	private JPanel pnlBotones;
	private JButton btnBuscar;
	
	private Frame owner;
	private PanTablaFacturas panTablasFacturas;
	private Proveedor proveedor;
	private PanelSeleccionarElementos<ETipoMateriaPrima> panSelTipoMateriaPrima;
	private PanelSeleccionarElementos<PrecioMateriaPrima> panSelMatPrima;
	private PanelDatePicker panelFechaDesde;
	private PanelDatePicker panelFechaHasta;
	private List<PrecioMateriaPrima> precioMatPrimaList;
	private PrecioMateriaPrimaFacadeRemote precioMatPrimaFacade;
	private FacturaProveedorFacadeRemote facturaProveedorFacade;
	

	public JDialogSeleccionarFacturaProveedor(Frame owner, Proveedor proveedor) {
		super(owner);
		this.owner = owner;
		this.proveedor = proveedor;
		this.precioMatPrimaList = getPrecioMatPrimaFacade().getAllOrderByName(proveedor);

		setModal(true);
		setSize(new Dimension(700, 550));
		setTitle("Seleccionar Facturas de proveedor");
		construct();
	}

	private void construct() {
		setLayout(new GridBagLayout());
		add(getPanDetalle(), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 1, 1));
		add(getPanelBotones(), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
	}

	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			
			panDetalle.add(new JLabel(" PROVEEDOR:"), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 1, 1, 0, 0));
			panDetalle.add(getTxtRazSoc(), GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));

			panDetalle.add(getPanelFechaDesde(), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getPanelFechaHasta(), GenericUtils.createGridBagConstraints(1, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));

			panDetalle.add(new JLabel(" NÚMERO DE FACTURA:"), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtNroFactura(), GenericUtils.createGridBagConstraints(1, 2,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			
			panDetalle.add(getPanSelTipoMateriaPrima(), GenericUtils.createGridBagConstraints(0, 3,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 2, 1, 1, 0));
			panDetalle.add(getPanSelMatPrima(), GenericUtils.createGridBagConstraints(0, 4,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 2, 1, 1, 0));
			panDetalle.add(getBtnBuscar(), GenericUtils.createGridBagConstraints(0, 5,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 20, 5, 5), 1, 1, 1, 0));

			panDetalle.add(getPanTablasFacturas(), GenericUtils.createGridBagConstraints(0, 6,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 2, 1, 0, 1));
		}
		return panDetalle;
	}

	private JFormattedTextField getTxtNroFactura() {
		if(txtNroFactura == null) {
			try {
				txtNroFactura = new JFormattedTextField(new MaskFormatter("####-########"));
				txtNroFactura.setFocusLostBehavior(JFormattedTextField.PERSIST);
				txtNroFactura.addFocusListener(new FocusAdapter() {
					public void focusLost(FocusEvent e) {
						try {
							txtNroFactura.commitEdit();
						} catch (ParseException e1) {
						}
					}
				});
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return txtNroFactura;
	}

	private PanelDatePicker getPanelFechaDesde() {
		if (panelFechaDesde == null) {
			panelFechaDesde = new PanelDatePicker();
			panelFechaDesde.setCaption("Fecha desde:");
			panelFechaDesde.setSelectedDate(DateUtil.restarDias(DateUtil.getHoy(), 30));
		}
		return panelFechaDesde;
	}

	private PanelDatePicker getPanelFechaHasta() {
		if (panelFechaHasta == null) {
			panelFechaHasta = new PanelDatePicker();
			panelFechaHasta.setCaption("Fecha hasta:");
			panelFechaHasta.setSelectedDate(DateUtil.getHoy());
		}
		return panelFechaHasta;
	}

	
	private JButton getBtnBuscar() {
		if(btnBuscar == null) {
			btnBuscar = new JButton("Buscar");
			btnBuscar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					
					//TODO: Emprolijar los criterios de búsqueda!!!
					
					Date fechaHasta = DateUtil.getManiana(new java.sql.Date(getPanelFechaHasta().getDate().getTime()));
					List<FacturaProveedor> facturaListByParams = getFacturaProveedorFacade().getFacturaListByParams(proveedor.getId(), extractIds(getPanSelMatPrima().getSelectedElements()), new java.sql.Date(getPanelFechaDesde().getDate().getTime()), fechaHasta);
					getPanTablasFacturas().getTabla().removeAllRows();
					getPanTablasFacturas().agregarElementos(facturaListByParams);
				}

			});
		}
		return btnBuscar;
	}

	protected List<Integer> extractIds(List<PrecioMateriaPrima> selectedElements) {
		List<Integer> idList = new ArrayList<Integer>();
		for(PrecioMateriaPrima pmp : selectedElements) {
			idList.add(pmp.getId());
		}
		return idList;
	}

	private PanTablaFacturas getPanTablasFacturas() {
		if(panTablasFacturas == null) {
			panTablasFacturas = new PanTablaFacturas();
		}
		return panTablasFacturas;
	}

	private class PanTablaFacturas extends PanelTabla<FacturaProveedor> {

		private static final long serialVersionUID = -1644473275777548600L;

		private static final int CANT_COLS = 2;
		private static final int COL_FACTURA = 0;
		private static final int COL_OBJ = 1;

		public PanTablaFacturas() {
			super();
			getBotonAgregar().setVisible(false);
			agregarBotonModificar();
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tablaFacturas = new FWJTable(0, CANT_COLS);
			tablaFacturas.setStringColumn(COL_FACTURA, "FACTURA", 590, 590, true);
			tablaFacturas.setStringColumn(COL_OBJ, "", 0, 0, true);
			tablaFacturas.setAlignment(COL_FACTURA, FWJTable.CENTER_ALIGN);
			tablaFacturas.addMouseListener(new MouseAdapter () {

				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2) {
						handleSeleccionFactura();
					}
				}

			});
			return tablaFacturas;
		}

		private void handleSeleccionFactura() {
			int selectedRow = getTabla().getSelectedRow();
			FacturaProveedor facturaProveedor = getElemento(selectedRow);
			OperacionSobreFacturaProveedorHandler operacionHandler = new OperacionSobreFacturaProveedorHandler(owner, facturaProveedor, true);
			operacionHandler.showFacturaProveedorDialog();
		}

		private String toStringFactura(FacturaProveedor fp) {
			String datosFactura = DateUtil.dateToString(fp.getFechaIngreso()) + " - " + fp.getNroFactura();
			Set<String> precioMatPrimaListMatched = new HashSet<String>();
			for(ItemFacturaProveedor ifp : fp.getItems()) {
				if(ifp instanceof ItemFacturaMateriaPrima) {
					List<PrecioMateriaPrima> selectedElements = getPanSelMatPrima().getSelectedElements();
					PrecioMateriaPrima precioMateriaPrima = ((ItemFacturaMateriaPrima)ifp).getPrecioMateriaPrima();
					if(selectedElements.isEmpty() || selectedElements.contains(precioMateriaPrima)) {
						precioMatPrimaListMatched.add(precioMateriaPrima.toString());
					}
				}
			}
			datosFactura+= " (" + StringUtil.getCadena(precioMatPrimaListMatched, ", ") + ")";
			return datosFactura;
		}

		@Override
		protected void agregarElemento(FacturaProveedor elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_FACTURA] = toStringFactura(elemento);
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected FacturaProveedor getElemento(int fila) {
			return (FacturaProveedor)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			FacturaProveedor fp = getElemento(filaSeleccionada);
			try {
				getFacturaProveedorFacade().checkEliminacionOrEdicionFacturaProveedor(fp);
				OperacionSobreFacturaProveedorHandler operacionHandler = new OperacionSobreFacturaProveedorHandler(owner, fp, false); 
				operacionHandler.showFacturaProveedorDialog();
			} catch (ValidacionException e) {
				FWJOptionPane.showErrorMessage(JDialogSeleccionarFacturaProveedor.this, StringW.wordWrap(e.getMensajeError()), "Imposible Editar");
				return;
			}
		}

		@Override
		public boolean validarQuitar() {
			if(FWJOptionPane.showQuestionMessage(owner, "¿Está seguro que desea eliminar la factura?", "Confirmación") == FWJOptionPane.YES_OPTION) { 
				FacturaProveedor fp = getElemento(getTabla().getSelectedRow());
				try {
					getFacturaProveedorFacade().borrarFactura(fp, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
					FWJOptionPane.showInformationMessage(owner, "Factura borrada éxitosamente.", "Información");
				} catch (ValidacionException e) {
					FWJOptionPane.showErrorMessage(JDialogSeleccionarFacturaProveedor.this, StringW.wordWrap(e.getMensajeError()), "Imposible Eliminar");
					return false;
				}
				return true;
			} else {
				return false;
			}
		}

	}


	private JTextField getTxtRazSoc() {
		if(txtRazSoc == null) {
			txtRazSoc = new JTextField();
			txtRazSoc.setEditable(false);
			txtRazSoc.setText(proveedor.getRazonSocial());
		}
		return txtRazSoc;
	}

	private JPanel getPanelBotones() {
		if(pnlBotones == null){
			pnlBotones = new JPanel();
			pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
			pnlBotones.add(getBtnCerrar());
		}
		return pnlBotones;
	}

	private JButton getBtnCerrar() {
		if(btnCerrar == null) {
			btnCerrar = new JButton("Cerrar");
			btnCerrar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dispose();
				}

			});
		}
		return btnCerrar;
	}

	private PrecioMateriaPrimaFacadeRemote getPrecioMatPrimaFacade() {
		if(precioMatPrimaFacade == null) {
			this.precioMatPrimaFacade = GTLBeanFactory.getInstance().getBean2(PrecioMateriaPrimaFacadeRemote.class);
		}
		return precioMatPrimaFacade;
	}

	private PanelSeleccionarElementos<PrecioMateriaPrima> getPanSelMatPrima() {
		if(panSelMatPrima == null) {
			panSelMatPrima = new PanelSeleccionarElementos<PrecioMateriaPrima>(owner, precioMatPrimaList, "Materias Primas");			
		}
		return panSelMatPrima;
	}

	private PanelSeleccionarElementos<ETipoMateriaPrima> getPanSelTipoMateriaPrima() {
		if(panSelTipoMateriaPrima == null) {
			panSelTipoMateriaPrima = new PanelSeleccionarElementos<ETipoMateriaPrima>(owner, Arrays.asList(ETipoMateriaPrima.values()), "Tipos de Mat. Prima");
			panSelTipoMateriaPrima.addPanelSeleccionarElementosListener(new PanelSeleccionarElementoEventListener<ETipoMateriaPrima>() {

				public void elementsSelected(PanelSeleccionarElementoEvent<ETipoMateriaPrima> evt) {
					if(evt.getElements().isEmpty()) {
						getPanSelMatPrima().setElementsAndSelectedElements(precioMatPrimaList, new ArrayList<PrecioMateriaPrima>());
					} else {
						List<PrecioMateriaPrima> filteredPrecioMateriaPrimaList = getFilteredPrecioMateriaPrimaList(evt.getElements());
						getPanSelMatPrima().setElementsAndSelectedElements(filteredPrecioMateriaPrimaList, filteredPrecioMateriaPrimaList);
					}
				}

			});
		}
		return panSelTipoMateriaPrima;
	}

	private List<PrecioMateriaPrima> getFilteredPrecioMateriaPrimaList(List<ETipoMateriaPrima> elements) {
		List<PrecioMateriaPrima> filteredPrecioMateriaPrimaList = new ArrayList<PrecioMateriaPrima>();
		for(PrecioMateriaPrima pm : precioMatPrimaList) {
			if(elements.contains(pm.getMateriaPrima().getTipo())) {
				filteredPrecioMateriaPrimaList.add(pm);
			}
		}
		return filteredPrecioMateriaPrimaList;
	}

	private FacturaProveedorFacadeRemote getFacturaProveedorFacade() {
		if(facturaProveedorFacade == null) {
			facturaProveedorFacade = GTLBeanFactory.getInstance().getBean2(FacturaProveedorFacadeRemote.class);
		}
		return facturaProveedorFacade;
	}

}
package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import main.GTLGlobalCache;
import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextArea;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.VerticalFlowLayout;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.enums.ETipoMoneda;
import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.materiaprima.Cabezal;
import ar.com.textillevel.entidades.ventas.materiaprima.Cilindro;
import ar.com.textillevel.entidades.ventas.materiaprima.MateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.Tela;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.TipoAnilina;
import ar.com.textillevel.facade.api.remote.ArticuloFacadeRemote;
import ar.com.textillevel.facade.api.remote.MateriaPrimaFacadeRemote;
import ar.com.textillevel.facade.api.remote.PrecioMateriaPrimaFacadeRemote;
import ar.com.textillevel.facade.api.remote.TipoAnilinaFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.MateriaPrimaFactory;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogAltaNuevaMateriaPrimaYPrecioMateriaPrima extends JDialog {

	private static final long serialVersionUID = 8181355871160830494L;

	private static final int MAX_LONGITUD_NOMBRE = 50;
	private static final int MAX_LONGITUD_OBSERVACIONES = 3000;

	private static final String PNL_ANILINA = "Anilina";
	private static final String PNL_TELA = "Tela";
	private static final String PNL_CILINDRO = "Cilindro";
	private static final String PNL_CABEZAL = "Cabezal";

	private JPanel tabDetalle;
	private JPanel panDetalle;
	private JPanel panDatosPrecioMateriaPrima;

	private JPanel pnlControlesExtra;

	private List<MateriaPrima> mats;

	private FWJTextField txtNombreProducto;
	private FWJTextField txtConcentracion;
	private JComboBox cmbTipoMateriaPrima;
	private JComboBox cmbUnidades;
	private FWJTextArea txtObservaciones;

	private CardLayout cardLayout;

	private JLabel lblNombreProveedor;

	// anilina
	private JPanel pnlAnilina;
	private JComboBox cmbTipoAnilina;
	private FWJNumericTextField txtColorIndex;
	private FWJTextField txtHexaDecimalColor;
	private JPanel panColor;
	private JButton btnElegirColor;
	private Color colorAnilina;
	private JLabel lblColor;

	// tela
	private JPanel pnlTela;
	private JComboBox cmbArticulo;
	private JTextField txtGramaje;
	
	//cilindro
	private JPanel pnlCilindro;
	private JComboBox cmbAnchoCilindro;
	private FWJTextField txtMeshCilindro;
	private FWJTextField txtDiametroCilindro;
	
	//cabezal
	private JPanel pnlCabezal;
	private FWJTextField txtDiametroCabezal;
	
	// panel precio materia prima
	private FWJTextField txtPrecioPrecioMateriaPrima;
	private FWJTextField txtAliasPrecioMateriaPrima;
	private JComboBox cmbTipoMonedaPrecioMateriaPrima;
	private FWJTextField txtSiglasPrecioMateriaPrima;
	private FWJTextField txtStockActualPrecioMateriaPrima;
	private FWJTextField txtPuntoPedidoPrecioMateriaPrima;

	private JButton btnAceptar;
	private JButton btnSalir;

	private MateriaPrima materiaPrima;

	private PrecioMateriaPrima precioMateriaPrima;

	private MateriaPrimaFacadeRemote materiaPrimaFacade;
	private PrecioMateriaPrimaFacadeRemote precioMateriaPrimaFacade;

	private Proveedor proveedor;

	private boolean acepto = false;

	public JDialogAltaNuevaMateriaPrimaYPrecioMateriaPrima(Frame padre, Proveedor proveedor) {
		super(padre);
		this.proveedor = proveedor;
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen() {
		setSize(new Dimension(750, 650));
		setModal(true);
		setTitle("Nueva materia prima");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		GuiUtil.centrar(this);
		setResizable(false);
		pack();
	}

	private void setUpComponentes() {
		add(getPanelNorte(), BorderLayout.NORTH);
		add(getTabDetalle(), BorderLayout.CENTER);
		add(getPanelSur(), BorderLayout.SOUTH);
	}

	private JPanel getPanelSur() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnAceptar());
		panel.add(getBtnSalir());
		return panel;
	}

	private JPanel getPanelNorte() {
		JPanel panelNorte = new JPanel();
		panelNorte.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		JPanel pnl = new JPanel();
		pnl.setLayout(new VerticalFlowLayout(VerticalFlowLayout.BOTTOM, 5, 5));
		pnl.add(getLblNombreProveedor());
		panelNorte.add(pnl);
		return panelNorte;
	}

	private JPanel getPanDatosPrecioMateriaPrima() {
		if (panDatosPrecioMateriaPrima == null) {
			panDatosPrecioMateriaPrima = new JPanel();
			panDatosPrecioMateriaPrima.setBorder(BorderFactory.createTitledBorder("Información del proveedor"));
			panDatosPrecioMateriaPrima.setLayout(new GridLayout(3, 4, 25, 10));
			panDatosPrecioMateriaPrima.add(new JLabel("Alias: "));
			panDatosPrecioMateriaPrima.add(getTxtAliasPrecioMateriaPrima());
			panDatosPrecioMateriaPrima.add(new JLabel("Siglas: "));
			panDatosPrecioMateriaPrima.add(getTxtSiglasPrecioMateriaPrima());
			panDatosPrecioMateriaPrima.add(new JLabel("Precio: "));
			panDatosPrecioMateriaPrima.add(getTxtPrecioPrecioMateriaPrima());
			panDatosPrecioMateriaPrima.add(new JLabel("Moneda: "));
			panDatosPrecioMateriaPrima.add(getCmbTipoMonedaPrecioMateriaPrima());
			panDatosPrecioMateriaPrima.add(new JLabel("Stock actual: "));
			panDatosPrecioMateriaPrima.add(getTxtStockActualPrecioMateriaPrima());
			panDatosPrecioMateriaPrima.add(new JLabel("Punto de pedido: "));
			panDatosPrecioMateriaPrima.add(getTxtPuntoPedidoPrecioMateriaPrima());
		}
		return panDatosPrecioMateriaPrima;
	}

	private JPanel getTabDetalle() {
		if (tabDetalle == null) {
			tabDetalle = new JPanel();
			tabDetalle.setLayout(new VerticalFlowLayout(VerticalFlowLayout.CENTER, 5, 5));
			tabDetalle.add(getPanDetalle());
			tabDetalle.add(getPanDatosPrecioMateriaPrima());
		}
		return tabDetalle;
	}

	private JPanel getPanDetalle() {
		if (panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.setBorder(BorderFactory.createTitledBorder("Información de materia prima"));
			panDetalle.add(new JLabel("Tipo de materia prima: "), createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getCmbTipoMateriaPrima(), createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("Descripción: "), createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtDescripcion(), createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("Unidad: "), createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getCmbUnidades(), createGridBagConstraints(1, 2, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("Concentracion:  "), createGridBagConstraints(0, 3, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtConcentracion(), createGridBagConstraints(1, 3, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("Observaciones  "), createGridBagConstraints(0, 4, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			JScrollPane jsp = new JScrollPane(getTxtObservaciones(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jsp.setPreferredSize(new Dimension(500, 80));
			panDetalle.add(jsp, createGridBagConstraints(1, 4, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(getPnlControlesExtra(), createGridBagConstraints(0, 5, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 5), 2, 1, 0, 0));
		}
		return panDetalle;
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

	private void capturarSetearDatosMateriaPrima() {
		ETipoMateriaPrima tipoMateriaPrima = (ETipoMateriaPrima) getCmbTipoMateriaPrima().getSelectedItem();
		if (getMateriaPrima()==null || (getMateriaPrima()!=null && getMateriaPrima().getId() == null)) {
			setMateriaPrima(MateriaPrimaFactory.createMateriaPrima(tipoMateriaPrima));
		}
		if (getTxtConcentracion().getText().trim().length() > 0) {
			getMateriaPrima().setConcentracion(new BigDecimal(getTxtConcentracion().getText().replace(',', '.')));
		}
		getMateriaPrima().setDescripcion(getTxtDescripcion().getText().toUpperCase());
		getMateriaPrima().setUnidad((EUnidad) getCmbUnidades().getSelectedItem());
		getMateriaPrima().setObservaciones(getTxtObservaciones().getText());
		if (tipoMateriaPrima == ETipoMateriaPrima.ANILINA) {
			((Anilina) getMateriaPrima()).setColorIndex(getTxtColorIndex().getValue());
			((Anilina) getMateriaPrima()).setHexaDecimalColor(getTxtHexaDecimalColor().getText());
			((Anilina) getMateriaPrima()).setTipoAnilina((TipoAnilina) getCmbTipoAnilina().getSelectedItem());
		}

		if (tipoMateriaPrima == ETipoMateriaPrima.TELA) {
			((Tela) getMateriaPrima()).setArticulo((Articulo) getCmbArticulo().getSelectedItem());
		}
		
		if(tipoMateriaPrima == ETipoMateriaPrima.CILINDRO){
			((Cilindro)getMateriaPrima()).setAnchoCilindro(((EAnchoCilindro)getCmbAnchoCilindro().getSelectedItem()).getAncho());
			((Cilindro)getMateriaPrima()).setDiametroCilindro(new BigDecimal(Double.valueOf(getTxtDiametroCilindro().getText().replace(',', '.'))));
			((Cilindro)getMateriaPrima()).setMeshCilindro(new BigDecimal(Double.valueOf(getTxtMeshCilindro().getText().replace(',', '.'))));
		}
		
		if(tipoMateriaPrima == ETipoMateriaPrima.CABEZAL){
			((Cabezal)getMateriaPrima()).setDiametroCabezal(new BigDecimal(Double.valueOf(getTxtDiametroCabezal().getText().replace(',', '.'))));
		}
	}

	private void capturarSetearDatosPrecioMateriaPrima() {
		getPrecioMateriaPrima().setAlias(getTxtAliasPrecioMateriaPrima().getText().toUpperCase().replace(',', '.'));
		getPrecioMateriaPrima().setFechaUltModif(DateUtil.getAhora());
		getPrecioMateriaPrima().setMateriaPrima(getMateriaPrima());
		getPrecioMateriaPrima().setPrecio(new BigDecimal(Double.valueOf(getTxtPrecioPrecioMateriaPrima().getText().replace(',', '.'))));
		getPrecioMateriaPrima().setPuntoDePedido(new BigDecimal(Double.valueOf(getTxtPuntoPedidoPrecioMateriaPrima().getText().replace(',', '.'))));
		getPrecioMateriaPrima().setSiglas(getTxtSiglasPrecioMateriaPrima().getText().toUpperCase().replace(',', '.'));
		getPrecioMateriaPrima().setStockActual(new BigDecimal(Double.valueOf(getTxtStockActualPrecioMateriaPrima().getText().replace(',', '.'))));
		getPrecioMateriaPrima().setTipoMoneda((ETipoMoneda) getCmbTipoMonedaPrecioMateriaPrima().getSelectedItem());
		getPrecioMateriaPrima().setStockInicial(new BigDecimal(Double.valueOf(getTxtStockActualPrecioMateriaPrima().getText().replace(',', '.'))));
		getPrecioMateriaPrima().setStockInicialDisponible(new BigDecimal(Double.valueOf(getTxtStockActualPrecioMateriaPrima().getText().replace(',', '.'))));
	}

	private boolean validar() {
		return validarDatosMateriaPrima() && validarDatosPrecioMateriaPrima();
	}

	private boolean validarDatosPrecioMateriaPrima() {
		if (getTxtAliasPrecioMateriaPrima().getText().trim().length() == 0) {
			FWJOptionPane.showErrorMessage(this, "Debe completar el alias", "Error");
			getTxtAliasPrecioMateriaPrima().requestFocus();
			return false;
		}

		if( ((ETipoMateriaPrima) getCmbTipoMateriaPrima().getSelectedItem())==ETipoMateriaPrima.ANILINA){
			if (getTxtSiglasPrecioMateriaPrima().getText().trim().length() == 0) {
				FWJOptionPane.showErrorMessage(this, "Debe completar las siglas", "Error");
				getTxtSiglasPrecioMateriaPrima().requestFocus();
				return false;
			}
		}

		if (getTxtPrecioPrecioMateriaPrima().getText().trim().length() == 0) {
			FWJOptionPane.showErrorMessage(this, "Debe completar el precio", "Error");
			getTxtPrecioPrecioMateriaPrima().requestFocus();
			return false;
		}

		if (!GenericUtils.esNumerico(getTxtPrecioPrecioMateriaPrima().getText())) {
			FWJOptionPane.showErrorMessage(this, "El precio debe ser numérico", "Error");
			getTxtPrecioPrecioMateriaPrima().requestFocus();
			return false;
		}

		if (getTxtStockActualPrecioMateriaPrima().getText().trim().length() == 0) {
			FWJOptionPane.showErrorMessage(this, "Debe completar el stock actual", "Error");
			getTxtStockActualPrecioMateriaPrima().requestFocus();
			return false;
		}

		if (!GenericUtils.esNumerico(getTxtStockActualPrecioMateriaPrima().getText())) {
			FWJOptionPane.showErrorMessage(this, "El stock actual debe ser numérico", "Error");
			getTxtStockActualPrecioMateriaPrima().requestFocus();
			return false;
		}

		if (getTxtPuntoPedidoPrecioMateriaPrima().getText().trim().length() == 0) {
			FWJOptionPane.showErrorMessage(this, "Debe completar el punto de pedido", "Error");
			getTxtPuntoPedidoPrecioMateriaPrima().requestFocus();
			return false;
		}

		if (!GenericUtils.esNumerico(getTxtPuntoPedidoPrecioMateriaPrima().getText())) {
			FWJOptionPane.showErrorMessage(this, "El punto de pedido debe ser numérico", "Error");
			getTxtPuntoPedidoPrecioMateriaPrima().requestFocus();
			return false;
		}

		return true;
	}

	private boolean validarDatosMateriaPrima() {

		if (getTxtDescripcion().getText().trim().length() == 0) {
			FWJOptionPane.showErrorMessage(this, "Debe ingresar la descripción", "Advertencia");
			getTxtDescripcion().requestFocus();
			return false;
		}

		if (getTxtConcentracion().getText().trim().length() > 0 && !GenericUtils.esNumerico(getTxtConcentracion().getText())) {
			FWJOptionPane.showErrorMessage(this, "Solo puede ingresar números.", "Advertencia");
			getTxtConcentracion().requestFocus();
			return false;
		}

		ETipoMateriaPrima tipoMateriaPrima = (ETipoMateriaPrima) getCmbTipoMateriaPrima().getSelectedItem();

		if (tipoMateriaPrima == ETipoMateriaPrima.ANILINA) {

			if (getCmbTipoAnilina().getSelectedItem() == null) {
				FWJOptionPane.showErrorMessage(this, "Debe seleccionar un tipo de anilina.", "Advertencia");
				return false;
			}

			if(getTxtColorIndex().getText().trim().length()==0){
				FWJOptionPane.showErrorMessage(this, "Debe ingresar el color index.", "Advertencia");
				getTxtColorIndex().requestFocus();
				return false;
			}
			
			if (!GenericUtils.esNumerico(getTxtColorIndex().getText())) {
				FWJOptionPane.showErrorMessage(this, "Debe ingresar solo números.", "Advertencia");
				getTxtColorIndex().requestFocus();
				return false;
			}

			BigDecimal concentracion = getTxtConcentracion().getText().trim().length()>0?new BigDecimal(getTxtConcentracion().getText().replace(',', '.')):null;
			if (getMateriaPrimaFacade().existeAnilina(((TipoAnilina) getCmbTipoAnilina().getSelectedItem()), getTxtColorIndex().getValue(),concentracion,getMateriaPrima().getId())) {
				FWJOptionPane.showErrorMessage(this, "Ya existe una anilina para el tipo, el color index" + (concentracion != null? " y la concentración indicada":"")+".", "Error");
				return false;
			}
		}
		
		if(tipoMateriaPrima ==ETipoMateriaPrima.CILINDRO){
			if(getTxtMeshCilindro().getText().trim().length() == 0){
				FWJOptionPane.showErrorMessage(this, "Debe ingresar el mesh del cilindro", "Advertencia");
				getTxtMeshCilindro().requestFocus();
				return false;
			}

			if (!GenericUtils.esNumerico(getTxtMeshCilindro().getText())) {
				FWJOptionPane.showErrorMessage(this, "Debe ingresar solo números.", "Advertencia");
				getTxtMeshCilindro().requestFocus();
				return false;
			}
			
			if(getTxtDiametroCilindro().getText().trim().length() == 0){
				FWJOptionPane.showErrorMessage(this, "Debe ingresar el diámetro", "Advertencia");
				getTxtDiametroCilindro().requestFocus();
				return false;
			}

			if (!GenericUtils.esNumerico(getTxtDiametroCilindro().getText())) {
				FWJOptionPane.showErrorMessage(this, "Debe ingresar solo números.", "Advertencia");
				getTxtDiametroCilindro().requestFocus();
				return false;
			}
		}
		
		if(tipoMateriaPrima == ETipoMateriaPrima.CABEZAL){
			if(getTxtDiametroCabezal().getText().trim().length() == 0){
				FWJOptionPane.showErrorMessage(this, "Debe ingresar el diámetro", "Advertencia");
				getTxtDiametroCabezal().requestFocus();
				return false;
			}

			if (!GenericUtils.esNumerico(getTxtDiametroCabezal().getText())) {
				FWJOptionPane.showErrorMessage(this, "Debe ingresar solo números.", "Advertencia");
				getTxtDiametroCabezal().requestFocus();
				return false;
			}
		}

		return true;
	}

	private JPanel getPnlControlesExtra() {
		if (pnlControlesExtra == null) {
			cardLayout = new CardLayout();
			pnlControlesExtra = new JPanel(cardLayout);
			pnlControlesExtra.setBorder(BorderFactory.createTitledBorder("Datos adicionales"));
			pnlControlesExtra.add(PNL_ANILINA, getPnlAnilina());
			pnlControlesExtra.add(PNL_TELA, getPnlTela());
			pnlControlesExtra.add(PNL_CILINDRO, getPnlCilindro());
			pnlControlesExtra.add(PNL_CABEZAL, getPnlCabezal());
		}
		return pnlControlesExtra;
	}

	private FWJTextField getTxtDescripcion() {
		if (txtNombreProducto == null) {
			txtNombreProducto = new FWJTextField(MAX_LONGITUD_NOMBRE);
		}
		return txtNombreProducto;
	}

	private FWJTextField getTxtConcentracion() {
		if (txtConcentracion == null) {
			txtConcentracion = new FWJTextField();
		}
		return txtConcentracion;
	}

	private JComboBox getCmbTipoMateriaPrima() {
		if (cmbTipoMateriaPrima == null) {
			cmbTipoMateriaPrima = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoMateriaPrima, Arrays.asList(ETipoMateriaPrima.values()), true);
			cmbTipoMateriaPrima.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent evt) {
					if (evt.getStateChange() == ItemEvent.SELECTED) {
						ETipoMateriaPrima itemSeleccionado = (ETipoMateriaPrima) cmbTipoMateriaPrima.getSelectedItem();
						itemTipoProductoSeleccionado(itemSeleccionado);
					}
				}
			});
		}
		return cmbTipoMateriaPrima;
	}

	private JComboBox getCmbUnidades() {
		if (cmbUnidades == null) {
			cmbUnidades = new JComboBox();
			GuiUtil.llenarCombo(cmbUnidades, Arrays.asList(EUnidad.values()), true);
		}
		return cmbUnidades;
	}

	private void itemTipoProductoSeleccionado(ETipoMateriaPrima selectedItem) {
		cambiarPanel(selectedItem);
	}

	private void cambiarPanel(ETipoMateriaPrima selectedItem) {
		getTxtDescripcion().setEditable(true);
		if (selectedItem == ETipoMateriaPrima.ANILINA) {
			getPnlControlesExtra().setVisible(true);
			getCardLayout().show(getPnlControlesExtra(), PNL_ANILINA);
			setSize(new Dimension(750, 650));
			return;
		}
		if (selectedItem == ETipoMateriaPrima.TELA) {
			getPnlControlesExtra().setVisible(true);
			getCardLayout().show(getPnlControlesExtra(), PNL_TELA);
			getTxtDescripcion().setText(((Articulo) getCmbArticulo().getSelectedItem()).getDescripcion());
			BigDecimal gramaje = ((Articulo) getCmbArticulo().getSelectedItem()).getGramaje();
			getTxtGramaje().setText(String.valueOf(gramaje == null ? "" : gramaje));
			getTxtDescripcion().setEditable(false);
			getCmbUnidades().setSelectedItem(EUnidad.METROS);
			getCmbUnidades().setEnabled(false);
			getTxtConcentracion().setEnabled(false);
			setSize(new Dimension(750, 650));
			return;
		}
		if(selectedItem == ETipoMateriaPrima.CILINDRO){
			getPnlControlesExtra().setVisible(true);
			getCardLayout().show(getPnlControlesExtra(), PNL_CILINDRO);
			getCmbUnidades().setSelectedItem(EUnidad.UNIDAD);
			getCmbUnidades().setEnabled(false);
			getTxtConcentracion().setEnabled(false);
			setSize(new Dimension(750, 650));
			return;
		}
		if(selectedItem == ETipoMateriaPrima.CABEZAL){
			getPnlControlesExtra().setVisible(true);
			getCardLayout().show(getPnlControlesExtra(), PNL_CABEZAL);
			getCmbUnidades().setSelectedItem(EUnidad.UNIDAD);
			getCmbUnidades().setEnabled(false);
			getTxtConcentracion().setEnabled(false);
			setSize(new Dimension(750, 650));
			return;
		}		
		setSize(new Dimension(750, 500));
		getPnlControlesExtra().setVisible(false);
	}

	private CardLayout getCardLayout() {
		return cardLayout;
	}

	private JPanel getPnlAnilina() {
		if (pnlAnilina == null) {
			pnlAnilina = new JPanel();
			pnlAnilina.setLayout(new GridBagLayout());
			pnlAnilina.add(new JLabel("Tipo anilina: "), createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlAnilina.add(getCmbTipoAnilina(), createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlAnilina.add(new JLabel("Color index: "), createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlAnilina.add(getTxtColorIndex(), createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlAnilina.add(getPanColor(), createGridBagConstraints(0, 2, GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 0, 1, 0, 0));
		}
		return pnlAnilina;
	}

	private MateriaPrimaFacadeRemote getMateriaPrimaFacade() {
		if (materiaPrimaFacade == null) {
			materiaPrimaFacade = GTLBeanFactory.getInstance().getBean2(MateriaPrimaFacadeRemote.class);
		}
		return materiaPrimaFacade;
	}

	private PrecioMateriaPrimaFacadeRemote getPrecioMateriaPrimaFacade() {
		if (precioMateriaPrimaFacade == null) {
			precioMateriaPrimaFacade = GTLBeanFactory.getInstance().getBean2(PrecioMateriaPrimaFacadeRemote.class);
		}
		return precioMateriaPrimaFacade;
	}

	private JComboBox getCmbTipoAnilina() {
		if (cmbTipoAnilina == null) {
			cmbTipoAnilina = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoAnilina, GTLBeanFactory.getInstance().getBean2(TipoAnilinaFacadeRemote.class).getAllOrderByName(), true);
		}
		return cmbTipoAnilina;
	}

	private FWJNumericTextField getTxtColorIndex() {
		if (txtColorIndex == null) {
			txtColorIndex = new FWJNumericTextField();
			txtColorIndex.setPreferredSize(new Dimension(120, 20));
		}
		return txtColorIndex;
	}

	private FWJTextField getTxtHexaDecimalColor() {
		if (txtHexaDecimalColor == null) {
			txtHexaDecimalColor = new FWJTextField();
			txtHexaDecimalColor.setPreferredSize(new Dimension(120, 20));
			txtHexaDecimalColor.setEditable(false);
			txtHexaDecimalColor.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		}
		return txtHexaDecimalColor;
	}

	private FWJTextArea getTxtObservaciones() {
		if (txtObservaciones == null) {
			txtObservaciones = new FWJTextArea(MAX_LONGITUD_OBSERVACIONES);
			txtObservaciones.setPreferredSize(new Dimension(510, 50));
			txtObservaciones.setLineWrap(true);
			txtObservaciones.setBorder(BorderFactory.createLineBorder(Color.BLUE.darker()));
		}
		return txtObservaciones;
	}

	private JPanel getPanColor() {
		if (panColor == null) {
			panColor = new JPanel();
			panColor.setLayout(new GridBagLayout());
			panColor.add(new JLabel("Muestra: "), createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panColor.add(getTxtColor(), createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 10), 1, 1, 1, 0));
			panColor.add(getBtnElegirColor(), createGridBagConstraints(3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 10), 1, 1, 0, 0));
			panColor.add(getTxtHexaDecimalColor(), createGridBagConstraints(4, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 10), 1, 1, 0, 0));
		}
		return panColor;
	}

	private JButton getBtnElegirColor() {
		if (btnElegirColor == null) {
			btnElegirColor = new JButton("Elija el color");
			btnElegirColor.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					Color colorElegido = JColorChooser.showDialog(JDialogAltaNuevaMateriaPrimaYPrecioMateriaPrima.this, "Seleccione el color de la anilina", getTxtColor().getBackground());
					if (colorElegido != null) {
						updateColor(colorElegido);
					}
				}
			});
		}
		return btnElegirColor;
	}

	private void updateColor(final Color colorElegido) {
		colorAnilina = colorElegido;
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				getTxtColor().setBackground(colorAnilina);
				getTxtColor().setOpaque(true);
				getTxtHexaDecimalColor().setText("#" + Integer.toHexString(colorElegido.getRGB() & 0x00ffffff));
			}
		});
	}

	private JLabel getTxtColor() {
		if (lblColor == null) {
			lblColor = new JLabel("               ");
			lblColor.setBorder(BorderFactory.createEtchedBorder());
		}
		return lblColor;
	}

	public List<MateriaPrima> getMats() {
		return mats;
	}

	public void setMats(List<MateriaPrima> mats) {
		this.mats = mats;
	}

	private JPanel getPnlTela() {
		if (pnlTela == null) {
			pnlTela = new JPanel();
			pnlTela.setLayout(new GridBagLayout());
			pnlTela.add(new JLabel("Artículo: "), createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlTela.add(getCmbArticulo(), createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlTela.add(new JLabel("Gramaje: "), createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 0, 1, 0, 0));
			pnlTela.add(getTxtGramaje(), createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 0, 1, 0, 0));
		}
		return pnlTela;
	}

	private JComboBox getCmbArticulo() {
		if (cmbArticulo == null) {
			cmbArticulo = new JComboBox();
			GuiUtil.llenarCombo(cmbArticulo, GTLBeanFactory.getInstance().getBean2(ArticuloFacadeRemote.class).getAllOrderByName(), true);
			cmbArticulo.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						BigDecimal gramaje = ((Articulo) cmbArticulo.getSelectedItem()).getGramaje();
						getTxtGramaje().setText(String.valueOf(gramaje == null ? "" : gramaje));
						getTxtDescripcion().setText(((Articulo) cmbArticulo.getSelectedItem()).getDescripcion());
					}
				}
			});
		}
		return cmbArticulo;
	}

	private JTextField getTxtGramaje() {
		if (txtGramaje == null) {
			txtGramaje = new JTextField();
			txtGramaje.setEditable(false);
			txtGramaje.setPreferredSize(new Dimension(120, 20));
		}
		return txtGramaje;
	}
	
	private JComboBox getCmbAnchoCilindro() {
		if(cmbAnchoCilindro == null){
			cmbAnchoCilindro = new JComboBox();
			GuiUtil.llenarCombo(cmbAnchoCilindro, Arrays.asList(EAnchoCilindro.values()), true);
		}
		return cmbAnchoCilindro;
	}

	private FWJTextField getTxtMeshCilindro() {
		if(txtMeshCilindro == null){
			txtMeshCilindro = new FWJTextField();
			txtMeshCilindro.setPreferredSize(new Dimension(120, 20));
		}
		return txtMeshCilindro;
	}

	private FWJTextField getTxtDiametroCilindro() {
		if(txtDiametroCilindro == null){
			txtDiametroCilindro = new FWJTextField();
			txtDiametroCilindro.setPreferredSize(new Dimension(120, 20));
		}
		return txtDiametroCilindro;
	}
	
	private enum EAnchoCilindro {
		ANCHO1_5	(1.5d), 
		ANCHO2		(2d), 
		ANCHO2_4	(2.4d), 
		ANCHO3		(3d);

		private EAnchoCilindro(Double ancho) {
			setAncho(new BigDecimal(ancho));
		}

		private BigDecimal ancho;

		public BigDecimal getAncho() {
			return ancho;
		}

		public void setAncho(BigDecimal ancho) {
			this.ancho = ancho;
		}

		@Override
		public String toString() {
			return String.valueOf(getAncho().doubleValue());
		}
	}

	private JPanel getPnlCilindro() {
		if(pnlCilindro == null){
			pnlCilindro = new JPanel();
			pnlCilindro.setLayout(new GridBagLayout());
			pnlCilindro.add(new JLabel("Ancho: "), createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlCilindro.add(getCmbAnchoCilindro(), createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlCilindro.add(new JLabel("Mesh: "), createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 0, 1, 0, 0));
			pnlCilindro.add(getTxtMeshCilindro(), createGridBagConstraints(1,1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 0, 1, 0, 0));
			pnlCilindro.add(new JLabel("Diámetro: "), createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 0, 1, 0, 0));
			pnlCilindro.add(getTxtDiametroCilindro(), createGridBagConstraints(1,2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 0, 1, 0, 0));
		}
		return pnlCilindro;
	}
	
	private JPanel getPnlCabezal() {
		if(pnlCabezal == null){
			pnlCabezal = new JPanel();
			pnlCabezal.setLayout(new GridBagLayout());
			pnlCabezal.add(new JLabel("Diámetro: "), createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlCabezal.add(getTxtDiametroCabezal(), createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		}
		return pnlCabezal;
	}
	
	private FWJTextField getTxtDiametroCabezal() {
		if(txtDiametroCabezal == null){
			txtDiametroCabezal = new FWJTextField();
			txtDiametroCabezal.setPreferredSize(new Dimension(120, 20));
		}
		return txtDiametroCabezal;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public JLabel getLblNombreProveedor() {
		if (lblNombreProveedor == null) {
			lblNombreProveedor = new JLabel("   Proveedor: " + getProveedor().getNombreCorto() + "   ");
			lblNombreProveedor.setBorder(BorderFactory.createLineBorder(Color.RED));
			Font fuente = lblNombreProveedor.getFont();
			lblNombreProveedor.setFont(new Font(fuente.getName(), Font.BOLD, fuente.getSize() + 2));
		}
		return lblNombreProveedor;
	}

	public MateriaPrima getMateriaPrima() {
		return materiaPrima;
	}

	public void setMateriaPrima(MateriaPrima materiaPrima) {
		this.materiaPrima = materiaPrima;
	}

	public PrecioMateriaPrima getPrecioMateriaPrima() {
		if(precioMateriaPrima == null){
			precioMateriaPrima = new PrecioMateriaPrima();
		}
		return precioMateriaPrima;
	}

	public void setPrecioMateriaPrima(PrecioMateriaPrima precioMateriaPrima) {
		this.precioMateriaPrima = precioMateriaPrima;
	}

	private FWJTextField getTxtPrecioPrecioMateriaPrima() {
		if (txtPrecioPrecioMateriaPrima == null) {
			txtPrecioPrecioMateriaPrima = new FWJTextField();
		}
		return txtPrecioPrecioMateriaPrima;
	}

	private FWJTextField getTxtAliasPrecioMateriaPrima() {
		if (txtAliasPrecioMateriaPrima == null) {
			txtAliasPrecioMateriaPrima = new FWJTextField();
		}
		return txtAliasPrecioMateriaPrima;
	}

	private JComboBox getCmbTipoMonedaPrecioMateriaPrima() {
		if (cmbTipoMonedaPrecioMateriaPrima == null) {
			cmbTipoMonedaPrecioMateriaPrima = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoMonedaPrecioMateriaPrima, Arrays.asList(ETipoMoneda.values()), true);
		}
		return cmbTipoMonedaPrecioMateriaPrima;
	}

	private FWJTextField getTxtSiglasPrecioMateriaPrima() {
		if (txtSiglasPrecioMateriaPrima == null) {
			txtSiglasPrecioMateriaPrima = new FWJTextField();
		}
		return txtSiglasPrecioMateriaPrima;
	}

	private FWJTextField getTxtStockActualPrecioMateriaPrima() {
		if (txtStockActualPrecioMateriaPrima == null) {
			txtStockActualPrecioMateriaPrima = new FWJTextField();
		}
		return txtStockActualPrecioMateriaPrima;
	}

	private FWJTextField getTxtPuntoPedidoPrecioMateriaPrima() {
		if (txtPuntoPedidoPrecioMateriaPrima == null) {
			txtPuntoPedidoPrecioMateriaPrima = new FWJTextField();
		}
		return txtPuntoPedidoPrecioMateriaPrima;
	}

	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (validar()) {
						capturarSetearDatosMateriaPrima();
						setMateriaPrima(getMateriaPrimaFacade().save(getMateriaPrima()));
						capturarSetearDatosPrecioMateriaPrima();
						setPrecioMateriaPrima(getPrecioMateriaPrimaFacade().grabarNuevoPrecioMateriaPrimaYAgregarloAListaDePreciosProveedor(getPrecioMateriaPrima(), getProveedor(),
								GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName()));
						setAcepto(true);
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}

	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Salir");
			btnSalir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					setAcepto(false);
					dispose();
				}
			});
		}
		return btnSalir;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}
}

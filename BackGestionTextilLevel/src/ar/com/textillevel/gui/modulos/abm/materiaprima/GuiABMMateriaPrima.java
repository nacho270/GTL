package ar.com.textillevel.gui.modulos.abm.materiaprima;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextArea;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.templates.GuiABMListaTemplate;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.materiaprima.Cabezal;
import ar.com.textillevel.entidades.ventas.materiaprima.Cilindro;
import ar.com.textillevel.entidades.ventas.materiaprima.MateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.Tela;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.TipoAnilina;
import ar.com.textillevel.facade.api.remote.ArticuloFacadeRemote;
import ar.com.textillevel.facade.api.remote.MateriaPrimaFacadeRemote;
import ar.com.textillevel.facade.api.remote.TipoAnilinaFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.MateriaPrimaFactory;
import ar.com.textillevel.util.GTLBeanFactory;

public class GuiABMMateriaPrima extends GuiABMListaTemplate {

	private static final long serialVersionUID = -8651844648013003067L;

	private static final int MAX_LONGITUD_NOMBRE = 50;
	private static final int MAX_LONGITUD_OBSERVACIONES = 3000;

	private static final String PNL_ANILINA = "Anilina";
	private static final String PNL_TELA = "Tela";
	private static final String PNL_CILINDRO = "Cilindro";
	private static final String PNL_CABEZAL = "Cabezal";

	private JPanel tabDetalle;
	private JPanel panDetalle;

	private JPanel pnlControlesExtra;

	private List<MateriaPrima> mats;

	private FWJTextField txtNombreProducto;
	private FWJTextField txtConcentracion;
//	private CLJTextField txtStockActual;
//	private CLJTextField txtPuntoPedido;
	private JComboBox cmbTipoMateriaPrima;
	private JComboBox cmbUnidades;
	private FWJTextArea txtObservaciones;
	private JButton btnVerSeleccionarHijos;

	private CardLayout cardLayout;

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

	private MateriaPrima materiaPrima;

	private MateriaPrimaFacadeRemote materiaPrimaFacade;

	public GuiABMMateriaPrima(Integer idModulo) {
		super();
		setHijoCreado(true);
		setTitle("Administrar Materias primas");
		constructGui();
		setEstadoInicial();
	}

	private void constructGui() {
		panTabs.addTab("Información de la materia prima", getTabDetalle());
	}

	private JPanel getTabDetalle() {
		if (tabDetalle == null) {
			tabDetalle = new JPanel();
			tabDetalle.setLayout(new BorderLayout());
			tabDetalle.add(getPanDetalle(), BorderLayout.NORTH);
		}
		return tabDetalle;
	}

	private JPanel getPanDetalle() {
		if (panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(new JLabel("Tipo de materia prima: "), createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getCmbTipoMateriaPrima(), createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("Descripción: "), createGridBagConstraints(0,1 , GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtDescripcion(), createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("Unidad: "), createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getCmbUnidades(), createGridBagConstraints(1, 2, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("Concentracion:  "), createGridBagConstraints(0, 3, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtConcentracion(), createGridBagConstraints(1, 3, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
//			panDetalle.add(new JLabel("Stock actual:  "), createGridBagConstraints(0, 4, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
//			panDetalle.add(getTxtStockActual(), createGridBagConstraints(1, 4, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
//			panDetalle.add(new JLabel("Punto de pedido:  "), createGridBagConstraints(0, 5, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
//			panDetalle.add(getTxtPuntoPedido(), createGridBagConstraints(1,5, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("Observaciones  "), createGridBagConstraints(0, 4, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			JScrollPane jsp = new JScrollPane(getTxtObservaciones(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jsp.setPreferredSize(new Dimension(500, 80));
			panDetalle.add(jsp, createGridBagConstraints(1, 4, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(getBtnVerSeleccionarHijos(), createGridBagConstraints(0, 5, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getPnlControlesExtra(), createGridBagConstraints(0, 6, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 5), 2, 1, 0, 0));
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

	@Override
	public void cargarLista() {
		llenarLista();
	}

	private void refreshMaterasPrimas(){
		setMats(getMateriaPrimaFacade().getAllOrderByName(false));
		lista.removeAll();
	}
	
	private void llenarLista() {
		lista.clear();
		if (mats == null) {
			refreshMaterasPrimas();
		}
		for (MateriaPrima m : mats) {
			if (getCmbMaestro().getSelectedItem().equals("TODAS") || m.getTipo() == ((ETipoMateriaPrima) getCmbMaestro().getSelectedItem())) {
				lista.addItem(m);
			}
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		setMateriaPrimaActual(MateriaPrimaFactory.createMateriaPrima((ETipoMateriaPrima) getCmbTipoMateriaPrima().getSelectedItem()));
		getTxtDescripcion().requestFocus();
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if (lista.getSelectedIndex() >= 0) {
			if (FWJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar la materia prima seleccionada?", "Confirmación") == FWJOptionPane.YES_OPTION) {
				getMateriaPrimaFacade().remove(getMateriaPrimaActual());
				itemSelectorSeleccionado(-1);
			}
		}
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if (validar()) {
			capturarSetearDatos();
			MateriaPrima mat = getMateriaPrimaFacade().save(getMateriaPrimaActual());
			lista.setSelectedValue(mat, true);
			refreshMaterasPrimas();
			cargarLista();
			llenarLista();
			return true;
		}
		return false;
	}

	private void capturarSetearDatos() {
		ETipoMateriaPrima tipoMateriaPrima = (ETipoMateriaPrima) getCmbTipoMateriaPrima().getSelectedItem();
		Integer idActual = getMateriaPrimaActual().getId();
		List<MateriaPrima> mpHijas = getMateriaPrimaActual().getMpHijas();
		setMateriaPrimaActual(MateriaPrimaFactory.createMateriaPrima(tipoMateriaPrima));
		getMateriaPrimaActual().setId(idActual);
		getMateriaPrimaActual().setMpHijas(mpHijas);
		if (getTxtConcentracion().getText().trim().length() > 0) {
			getMateriaPrimaActual().setConcentracion(new BigDecimal(getTxtConcentracion().getText().replace(',', '.')));
		}
		getMateriaPrimaActual().setDescripcion(getTxtDescripcion().getText().toUpperCase());
//		getMateriaPrimaActual().setPuntoDePedido(new BigDecimal(getTxtPuntoPedido().getText().replace(',', '.')));
//		getMateriaPrimaActual().setStockActual(new BigDecimal(getTxtStockActual().getText().replace(',', '.')));
		getMateriaPrimaActual().setUnidad((EUnidad) getCmbUnidades().getSelectedItem());
		getMateriaPrimaActual().setObservaciones(getTxtObservaciones().getText());
		if (tipoMateriaPrima == ETipoMateriaPrima.ANILINA) {
			((Anilina) getMateriaPrimaActual()).setColorIndex(getTxtColorIndex().getValue());
			((Anilina) getMateriaPrimaActual()).setHexaDecimalColor(getTxtHexaDecimalColor().getText());
			((Anilina) getMateriaPrimaActual()).setTipoAnilina((TipoAnilina) getCmbTipoAnilina().getSelectedItem());
		}
		
		if (tipoMateriaPrima == ETipoMateriaPrima.TELA) {
			((Tela) getMateriaPrimaActual()).setArticulo((Articulo)getCmbArticulo().getSelectedItem());
		}
		
		if(tipoMateriaPrima == ETipoMateriaPrima.CILINDRO){
			((Cilindro)getMateriaPrimaActual()).setAnchoCilindro(((EAnchoCilindro)getCmbAnchoCilindro().getSelectedItem()).getAncho());
			((Cilindro)getMateriaPrimaActual()).setDiametroCilindro(new BigDecimal(Double.valueOf(getTxtDiametroCilindro().getText().replace(',', '.'))));
			((Cilindro)getMateriaPrimaActual()).setMeshCilindro(new BigDecimal(Double.valueOf(getTxtMeshCilindro().getText().replace(',', '.'))));
		}
		
		if(tipoMateriaPrima == ETipoMateriaPrima.CABEZAL){
			((Cabezal)getMateriaPrimaActual()).setDiametroCabezal(new BigDecimal(Double.valueOf(getTxtDiametroCabezal().getText().replace(',', '.'))));
		}
		
		// copio el nombre del padre a todos los hijos
		if(!getMateriaPrimaActual().getMpHijas().isEmpty()) {
			for(MateriaPrima mp : getMateriaPrimaActual().getMpHijas()) {
				mp.setDescripcion(getMateriaPrimaActual().getDescripcion());
			}
		}
	}

	private boolean validar() {

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
		
		// TODO QUITADO TEMPORALMENTE!! Volver a agregar
		
//		if(getMateriaPrimaFacade().existeMateriaPrima(getTxtDescripcion().getText().trim(),getMateriaPrimaActual().getId())){
//			CLJOptionPane.showErrorMessage(this, "Ya existe un materia prima con el nombre ingresado", "Error");
//			getTxtDescripcion().requestFocus();
//			return false;
//		}

		ETipoMateriaPrima tipoMateriaPrima = (ETipoMateriaPrima) getCmbTipoMateriaPrima().getSelectedItem();

		if (tipoMateriaPrima == ETipoMateriaPrima.ANILINA) {

			if (getCmbTipoAnilina().getSelectedItem() == null) {
				FWJOptionPane.showErrorMessage(this, "Debe seleccionar un tipo de anilina.", "Advertencia");
				return false;
			}
			
			if(getTxtColorIndex().getText().trim().length() == 0){
				FWJOptionPane.showErrorMessage(this, "Debe ingresar el color index", "Advertencia");
				getTxtColorIndex().requestFocus();
				return false;
			}

			if (!GenericUtils.esNumerico(getTxtColorIndex().getText())) {
				FWJOptionPane.showErrorMessage(this, "Debe ingresar solo números.", "Advertencia");
				getTxtColorIndex().requestFocus();
				return false;
			}
			// TODO QUITADO TEMPORALMENTE!! Volver a agregar
			
//			BigDecimal concentracion = getTxtConcentracion().getText().trim().length()>0?new BigDecimal(getTxtConcentracion().getText().replace(',', '.')):null;
//			if (getMateriaPrimaFacade().existeAnilina(((TipoAnilina) getCmbTipoAnilina().getSelectedItem()), getTxtColorIndex().getValue(),concentracion,getMateriaPrimaActual().getId())) {
//				CLJOptionPane.showErrorMessage(this, "Ya existe una anilina para el tipo, el color index" + (concentracion != null? " y la concentración indicada":"")+".", "Error");
//				return false;
//			}
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

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if (nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getCmbTipoMateriaPrima().setEditable(false);
			getTxtDescripcion().requestFocus();
			return true;
		} else {
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar un producto", "Error");
			return false;
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
		llenarLista();
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		MateriaPrima mpEager = getMateriaPrimaFacade().getByIdEager(((MateriaPrima) lista.getSelectedValue()).getId());
		setMateriaPrimaActual(mpEager);
		limpiarDatos();
		if (getMateriaPrimaActual() != null) {
			getCmbTipoMateriaPrima().setSelectedItem(getMateriaPrimaActual().getTipo());
			if (getMateriaPrimaActual() instanceof Anilina) {
				getPnlControlesExtra().setVisible(true);
				getTxtColorIndex().setValue(((Anilina) getMateriaPrimaActual()).getColorIndex().longValue());
				String hexaStr = ((Anilina) getMateriaPrimaActual()).getHexaDecimalColor();
				if (hexaStr.length() < 7 && hexaStr.length() > 0) {
					hexaStr = "#" + StringUtil.fillLeftWithZeros(hexaStr.substring(1, hexaStr.length()), 6);
				}
				if (hexaStr != null && hexaStr.length() == 7) {
					updateColor(fromHexaString(hexaStr));
					getTxtHexaDecimalColor().setText(hexaStr);
				}
				getCmbTipoAnilina().setSelectedItem(((Anilina) getMateriaPrimaActual()).getTipoAnilina());
			} else if(getMateriaPrimaActual() instanceof Tela) {
				getPnlControlesExtra().setVisible(true);
				BigDecimal gramaje = ((Tela)getMateriaPrimaActual()).getArticulo().getGramaje();
				getTxtGramaje().setText(String.valueOf(gramaje==null?"":gramaje));
				getCmbArticulo().setSelectedItem(((Tela)getMateriaPrimaActual()).getArticulo());
			}else if(getMateriaPrimaActual() instanceof Cilindro){
				getPnlControlesExtra().setVisible(true);
				getCmbAnchoCilindro().setSelectedItem(((Cilindro)getMateriaPrimaActual()).getAnchoCilindro());
				getTxtMeshCilindro().setText(String.valueOf(((Cilindro)getMateriaPrimaActual()).getMeshCilindro().doubleValue()));
				getTxtDiametroCilindro().setText(String.valueOf(((Cilindro)getMateriaPrimaActual()).getDiametroCilindro().doubleValue()));
			}else if(getMateriaPrimaActual() instanceof Cabezal){
				getPnlControlesExtra().setVisible(true);
				getTxtDiametroCabezal().setText(String.valueOf(((Cabezal)getMateriaPrimaActual()).getDiametroCabezal().doubleValue()));
			}else{
				getPnlControlesExtra().setVisible(false);
			}
			getTxtDescripcion().setText(getMateriaPrimaActual().getDescripcion());
			if (getMateriaPrimaActual().getConcentracion() != null) {
				getTxtConcentracion().setText(String.valueOf(getMateriaPrimaActual().getConcentracion().doubleValue()));
			}
			getTxtObservaciones().setText(getMateriaPrimaActual().getObservaciones());
//			getTxtPuntoPedido().setText(String.valueOf(getMateriaPrimaActual().getPuntoDePedido().doubleValue()));
//			getTxtStockActual().setText(String.valueOf(getMateriaPrimaActual().getStockActual().doubleValue()));
			getCmbUnidades().setSelectedItem(getMateriaPrimaActual().getUnidad());
		}
	}

	private Color fromHexaString(String hexaStr) {
		if (hexaStr == null) {
			return getTabDetalle().getBackground();
		} else {
			int r = Integer.valueOf(hexaStr.substring(1, 3), 16);
			int g = Integer.valueOf(hexaStr.substring(3, 5), 16);
			int b = Integer.valueOf(hexaStr.substring(5, 7), 16);
			return new Color(r, g, b);
		}
	}

	@Override
	public void limpiarDatos() {
		getTxtDescripcion().setText("");
		getTxtColorIndex().setText("");
//		getTxtStockActual().setText("");
		getTxtConcentracion().setText("");
		getTxtHexaDecimalColor().setText("");
		getTxtObservaciones().setText("");
//		getTxtPuntoPedido().setText("");
		getTxtColor().setText("               ");
		getTxtColor().setBackground(getTabDetalle().getBackground());
		getTxtGramaje().setText("");
		getCmbAnchoCilindro().setSelectedIndex(0);
		getTxtMeshCilindro().setText("");
		getTxtDiametroCilindro().setText("");
		getTxtDiametroCabezal().setText("");
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setEstadoInicial() {
		setModoEdicion(false);
		setModoEdicionTemplate(false);
		List tipos = new ArrayList();
		tipos.add("TODAS");
		tipos.addAll(Arrays.asList(ETipoMateriaPrima.values()));
		setContenidoComboMaestro(tipos, "Materia prima");
		cargarLista();
		llenarLista();
		ETipoMateriaPrima selectedItem = (ETipoMateriaPrima) getCmbTipoMateriaPrima().getSelectedItem();
		cambiarPanel(selectedItem);
		setMateriaPrimaActual(MateriaPrimaFactory.createMateriaPrima(selectedItem));
		if (lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);
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

//	private CLJTextField getTxtStockActual() {
//		if (txtStockActual == null) {
//			txtStockActual = new CLJTextField();
//		}
//		return txtStockActual;
//	}
//
//	private CLJTextField getTxtPuntoPedido() {
//		if (txtPuntoPedido == null) {
//			txtPuntoPedido = new CLJTextField();
//		}
//		return txtPuntoPedido;
//	}

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
			return;
		}
		if(selectedItem == ETipoMateriaPrima.TELA){
			getPnlControlesExtra().setVisible(true);
			getCardLayout().show(getPnlControlesExtra(), PNL_TELA);
			getTxtDescripcion().setText(((Articulo)getCmbArticulo().getSelectedItem()).getDescripcion());
			BigDecimal gramaje = ((Articulo)getCmbArticulo().getSelectedItem()).getGramaje();
			getTxtGramaje().setText(String.valueOf(gramaje==null?"":gramaje));
			getTxtDescripcion().setEditable(false);
			getCmbUnidades().setSelectedItem(EUnidad.METROS);
			getCmbUnidades().setEnabled(false);
			getTxtConcentracion().setEnabled(false);
			return;
		}
		if(selectedItem == ETipoMateriaPrima.CILINDRO){
			getPnlControlesExtra().setVisible(true);
			getCardLayout().show(getPnlControlesExtra(), PNL_CILINDRO);
			getCmbUnidades().setSelectedItem(EUnidad.UNIDAD);
			getCmbUnidades().setEnabled(false);
			getTxtConcentracion().setEnabled(false);
			return;
		}
		if(selectedItem == ETipoMateriaPrima.CABEZAL){
			getPnlControlesExtra().setVisible(true);
			getCardLayout().show(getPnlControlesExtra(), PNL_CABEZAL);
			getCmbUnidades().setSelectedItem(EUnidad.UNIDAD);
			getCmbUnidades().setEnabled(false);
			getTxtConcentracion().setEnabled(false);
			return;
		}
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

	private MateriaPrima getMateriaPrimaActual() {
		return materiaPrima;
	}

	private void setMateriaPrimaActual(MateriaPrima materiaPrima) {
		this.materiaPrima = materiaPrima;
	}

	private MateriaPrimaFacadeRemote getMateriaPrimaFacade() {
		if (materiaPrimaFacade == null) {
			materiaPrimaFacade = GTLBeanFactory.getInstance().getBean2(MateriaPrimaFacadeRemote.class);
		}
		return materiaPrimaFacade;
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
					Color colorElegido = JColorChooser.showDialog(GuiABMMateriaPrima.this, "Seleccione el color de la anilina", getTxtColor().getBackground());
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
		if(pnlTela == null){
			pnlTela = new JPanel();
			pnlTela.setLayout(new GridBagLayout());
			pnlTela.add(new JLabel("Artículo: "), createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlTela.add(getCmbArticulo(), createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlTela.add(new JLabel("Gramaje: "), createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 0, 1, 0, 0));
			pnlTela.add(getTxtGramaje(), createGridBagConstraints(1,1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 0, 1, 0, 0));
		}
		return pnlTela;
	}

	private JComboBox getCmbArticulo() {
		if(cmbArticulo == null){
			cmbArticulo = new JComboBox();
			GuiUtil.llenarCombo(cmbArticulo, GTLBeanFactory.getInstance().getBean2(ArticuloFacadeRemote.class).getAllOrderByName(), true);
			cmbArticulo.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED){
						BigDecimal gramaje = ((Articulo)cmbArticulo.getSelectedItem()).getGramaje();
						getTxtGramaje().setText(String.valueOf(gramaje==null?"":gramaje));
						getTxtDescripcion().setText(((Articulo)cmbArticulo.getSelectedItem()).getDescripcion() );
					}
				}
			});
		}
		return cmbArticulo;
	}

	private JTextField getTxtGramaje() {
		if(txtGramaje == null){
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

	private JButton getBtnVerSeleccionarHijos() {
		if (btnVerSeleccionarHijos == null) {
			btnVerSeleccionarHijos = new JButton("Ver/Seleccionar hijos");
			btnVerSeleccionarHijos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JDialogSelccionarHijosMateriaPrima d = new JDialogSelccionarHijosMateriaPrima(GuiABMMateriaPrima.this.getFrame(), getMateriaPrimaActual());
					d.setVisible(true);
					if(d.isAcepto()) {
						getMateriaPrimaActual().getMpHijas().clear();
						getMateriaPrimaActual().getMpHijas().addAll(d.getMateriasPrimasSeleccionadas());
					}
				}
			});
		}
		return btnVerSeleccionarHijos;
	}
}

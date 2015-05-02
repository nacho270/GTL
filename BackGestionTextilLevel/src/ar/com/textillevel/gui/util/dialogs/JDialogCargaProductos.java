package ar.com.textillevel.gui.util.dialogs;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.entidades.ventas.articulos.GamaColor;
import ar.com.textillevel.entidades.ventas.articulos.VarianteEstampado;
import ar.com.textillevel.facade.api.remote.DibujoEstampadoFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogCargaProductos extends JDialog {

	private static final long serialVersionUID = -5952510440654673411L;
	
	private static final String PNL_TENIDO = "Teñido";
	private static final String PNL_ESTAMPADO = "Estampado";
	
	private JPanel panDetalle;
	private CLJTextField txtPrecioProducto;
	private JComboBox cmbArticulos;
	private JComboBox cmbTipoProducto;
	private JPanel pnlControlesExtra;
	private CardLayout cardLayout;
	
	private JPanel panBotones;
	private JButton btnAceptar;
	private JButton btnCancelar;
	
	//tenido
	private JComboBox cmbGamas;
	private JPanel pnlTenido;
	
	//estampado
	private JComboBox cmbEstampados;
	private JComboBox cmbVariantes;
	private JPanel pnlEstampado;
	
	private InfoProductoElegido infoProductoElegido;

	private DibujoEstampadoFacadeRemote dibujosFacade;

	private Boolean acepto;
	private List<DibujoEstampado> dibujoEstampadoList;
	private List<Articulo> articuloList;
	private List<GamaColor> gamaColorList;

	public JDialogCargaProductos(Frame padre, List<DibujoEstampado> dibujoEstampadoList, List<Articulo> articuloList, List<GamaColor> gamaColorList){
		super(padre);
		this.dibujoEstampadoList = dibujoEstampadoList;
		this.articuloList = articuloList;
		this.gamaColorList = gamaColorList;
		setUpComponentes();
		setUpScreen();
		infoProductoElegido = null;
		setModal(true);
	}

	private void setUpComponentes() {
		this.add(getPanDetalle(),BorderLayout.CENTER);
		this.add(getPanBotones(),BorderLayout.SOUTH);
	}

	private JPanel getPanBotones() {
		if(panBotones == null){
			panBotones = new JPanel();
			panBotones.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
			panBotones.add(getBtnAceptar());
			panBotones.add(getBtnCancelar());
		}
		return panBotones;
	}

	private JButton getBtnCancelar() {
		if(btnCancelar == null){
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					infoProductoElegido = null;
					salir();
				}
			});
		}
		return btnCancelar;
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(validar()) {
						capturarSetearDatos();
						dispose();
					}
				}

			});
		}
		return btnAceptar;
	}

	private boolean validar() {
		if(getCmbArticulos().getSelectedItem() == null) {
			CLJOptionPane.showErrorMessage(this.getParent(), "Falta seleccionar un artículo.", "Advertencia");
			return false;
		}
		ETipoProducto tipoProducto = (ETipoProducto)getCmbTipoProducto().getSelectedItem();
		if(tipoProducto == ETipoProducto.TENIDO && getCmbGamas().getSelectedItem() == null) {
			CLJOptionPane.showErrorMessage(this.getParent(), "Falta seleccionar una gama de color.", "Advertencia");
			return false;
		}
		if(tipoProducto == ETipoProducto.ESTAMPADO) {
			if(getCmbEstampados().getSelectedItem() == null) {
				CLJOptionPane.showErrorMessage(this.getParent(), "Falta seleccionar un dibujo.", "Advertencia");
				return false;
			}
			if(getCmbVariantes().getSelectedItem() == null) {
				CLJOptionPane.showErrorMessage(this.getParent(), "Falta seleccionar una variante para el dibujo.", "Advertencia");
				return false;
			}
		}
		if(StringUtil.isNullOrEmpty(getTxtPrecioProducto().getText())) {
			CLJOptionPane.showErrorMessage(this.getParent(), "Falta ingresar un precio.", "Advertencia");
			getTxtPrecioProducto().requestFocus();
			return false;
		}
		if(!GenericUtils.esNumerico(getTxtPrecioProducto().getText())) {
			CLJOptionPane.showErrorMessage(this.getParent(), "El precio debe ser un número.", "Advertencia");
			getTxtPrecioProducto().requestFocus();
			return false;
		}
		return true;
	}

	private void capturarSetearDatos() {
		ETipoProducto tipoProducto = (ETipoProducto)getCmbTipoProducto().getSelectedItem();
		BigDecimal precio = new BigDecimal(getTxtPrecioProducto().getText().replace(',', '.'));
		if(tipoProducto == ETipoProducto.TENIDO) {
			infoProductoElegido = new InfoProductoElegido(tipoProducto, precio, (Articulo)getCmbArticulos().getSelectedItem(), (GamaColor)getCmbGamas().getSelectedItem());
		} else if(tipoProducto == ETipoProducto.ESTAMPADO) {
			infoProductoElegido = new InfoProductoElegido(tipoProducto, precio, (Articulo)getCmbArticulos().getSelectedItem(), (DibujoEstampado)getCmbEstampados().getSelectedItem(), (VarianteEstampado)getCmbVariantes().getSelectedItem());
		} else {
			infoProductoElegido = new InfoProductoElegido(tipoProducto, precio, (Articulo)getCmbArticulos().getSelectedItem());
		}
	}

	private void setUpScreen(){
		this.setTitle("Selección de producto");
		this.setSize(new Dimension(400,300));
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		
		this.setResizable(true);
	}
	
	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(new JLabel("Tipo de producto: "), createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getCmbTipoProducto(),  createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("Tipo artículo: "), createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getCmbArticulos(),  createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(getPnlControlesExtra(), createGridBagConstraints(0, 2,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 5), 2, 1, 0, 0));
			panDetalle.add(new JLabel("Precio: "), createGridBagConstraints(0, 3,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtPrecioProducto(),  createGridBagConstraints(1, 3,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
		}
		return panDetalle;
	}
	
	private JComboBox getCmbTipoProducto() {
		if(cmbTipoProducto == null){
			cmbTipoProducto = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoProducto, Arrays.asList(ETipoProducto.values()), true);
			cmbTipoProducto.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					if (evt.getStateChange() == ItemEvent.SELECTED){
						itemTipoProductoSeleccionado((ETipoProducto)cmbTipoProducto.getSelectedItem());
					}
				}
			});
		}
		return cmbTipoProducto;
	}
	
	private void itemTipoProductoSeleccionado(ETipoProducto selectedItem) {
		cambiarPanel(selectedItem);
	}
	
	private JPanel getPnlControlesExtra() {
		if(pnlControlesExtra == null){
			cardLayout = new CardLayout();
			pnlControlesExtra = new JPanel(cardLayout);
			pnlControlesExtra.setBorder(BorderFactory.createTitledBorder("Datos adicionales"));
			pnlControlesExtra.add("Teñido", getPnlTenido());
			pnlControlesExtra.add("Estampado",getPnlEstampado());
		}
		return pnlControlesExtra;
	}

	private void cambiarPanel(ETipoProducto selectedItem) {
		if(selectedItem == ETipoProducto.TENIDO){
			getPnlControlesExtra().setVisible(true);
			getCardLayout().show(getPnlControlesExtra(), PNL_TENIDO);
			return;
		}
		
		if(selectedItem == ETipoProducto.ESTAMPADO){
			getPnlControlesExtra().setVisible(true);
			getCardLayout().show(getPnlControlesExtra(), PNL_ESTAMPADO);
			return;
		}
		getPnlControlesExtra().setVisible(false);
	}

	private CardLayout getCardLayout() {
		return cardLayout;
	}

	private JComboBox getCmbGamas() {
		if(cmbGamas == null){
			cmbGamas = new JComboBox();
			GuiUtil.llenarCombo(cmbGamas,gamaColorList, false);
			cmbGamas.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if((e.getStateChange() == ItemEvent.SELECTED)){
						getTxtPrecioProducto().setText(String.valueOf(((GamaColor)cmbGamas.getSelectedItem()).getPrecio()));
					}
				}
			});
		}
		return cmbGamas;
	}

	private JPanel getPnlTenido() {
		if(pnlTenido == null){
			pnlTenido = new JPanel();
			pnlTenido.setLayout(new GridBagLayout());
			pnlTenido.add(new JLabel("Nombre: "), createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlTenido.add(getCmbGamas(),  createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
		}
		return pnlTenido;
	}

	private JComboBox getCmbEstampados() {
		if(cmbEstampados == null){
			cmbEstampados = new JComboBox();
			cmbEstampados.addItem(null);
			for(DibujoEstampado d : dibujoEstampadoList){
				cmbEstampados.addItem(d);
			}
			cmbEstampados.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					llenarVariantes((DibujoEstampado)cmbEstampados.getSelectedItem());
				}
			});
		}
		return cmbEstampados;
	}

	private void llenarVariantes(DibujoEstampado selectedItem) {
		if(selectedItem != null){
			List<VarianteEstampado> variantes = getDibujosFacade().getByIdEager(selectedItem.getId()).getVariantes();
			GuiUtil.llenarCombo(getCmbVariantes(), variantes, false);
		}
	}

	private JComboBox getCmbVariantes() {
		if(cmbVariantes == null){
			cmbVariantes = new JComboBox();
		}
		return cmbVariantes;
	}

	private JPanel getPnlEstampado() {
		if(pnlEstampado == null){
			pnlEstampado = new JPanel();
			pnlEstampado.add(new JLabel("Dibujo: "), createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlEstampado.add(getCmbEstampados(),  createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			pnlEstampado.add(new JLabel("Variante: "), createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlEstampado.add(getCmbVariantes(),  createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
		}
		return pnlEstampado;
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

	private DibujoEstampadoFacadeRemote getDibujosFacade() {
		if(dibujosFacade == null){
			dibujosFacade = GTLBeanFactory.getInstance().getBean2(DibujoEstampadoFacadeRemote.class);
		}
		return dibujosFacade;
	}

	private CLJTextField getTxtPrecioProducto() {
		if(txtPrecioProducto == null){
			txtPrecioProducto = new CLJTextField();
		}
		return txtPrecioProducto;
	}

	private JComboBox getCmbArticulos() {
		if(cmbArticulos == null){
			cmbArticulos = new JComboBox();
			GuiUtil.llenarCombo(cmbArticulos, articuloList, true);
		}
		return cmbArticulos;
	}

	public Boolean getAcepto() {
		return acepto;
	}

	public void setAcepto(Boolean acepto) {
		this.acepto = acepto;
	}

	private void salir() {
		int opc = CLJOptionPane.showQuestionMessage(JDialogCargaProductos.this, "Sale sin guardar, desea continuar?", "Pregunta");
		if(opc == CLJOptionPane.YES_OPTION){
			dispose();
		}
	}

	public static class InfoProductoElegido {

		private ETipoProducto tipoProducto;
		private BigDecimal precio;
		private Articulo articulo;
		private GamaColor gamaColor; 
		private DibujoEstampado dibujoEstampado;
		private VarianteEstampado variante;
		
		public InfoProductoElegido(ETipoProducto tipoProducto, BigDecimal precio, Articulo articulo) {
			super();
			this.tipoProducto = tipoProducto;
			this.precio = precio;
			this.articulo = articulo;
		}

		public InfoProductoElegido(ETipoProducto tipoProducto, BigDecimal precio, Articulo articulo, GamaColor gamaColor) {
			super();
			this.tipoProducto = tipoProducto;
			this.precio = precio;
			this.articulo = articulo;
			this.gamaColor = gamaColor;
		}

		public InfoProductoElegido(ETipoProducto tipoProducto,BigDecimal precio, Articulo articulo, DibujoEstampado dibujoEstampado, VarianteEstampado variante) {
			super();
			this.tipoProducto = tipoProducto;
			this.precio = precio;
			this.articulo = articulo;
			this.dibujoEstampado = dibujoEstampado;
			this.variante = variante;
		}

		public ETipoProducto getTipoProducto() {
			return tipoProducto;
		}

		public BigDecimal getPrecio() {
			return precio;
		}

		public Articulo getArticulo() {
			return articulo;
		}

		public GamaColor getGamaColor() {
			return gamaColor;
		}

		public DibujoEstampado getDibujoEstampado() {
			return dibujoEstampado;
		}

		public VarianteEstampado getVariante() {
			return variante;
		}

	}

	public InfoProductoElegido getInfoProductoElegido() {
		return infoProductoElegido;
	}

}

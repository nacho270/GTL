package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ar.clarin.fwjava.componentes.CLCheckBoxList;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.facade.api.remote.ArticuloFacadeRemote;
import ar.com.textillevel.facade.api.remote.ProductoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogSeleccionarProducto extends JDialog {

	private static final long serialVersionUID = 7364390484648139031L;

	private JButton btnAceptar;
	private JButton btnCancelar;
	
	private JComboBox cmbArticulo;
	private JComboBox cmbTipoProducto;
	private CLCheckBoxList<Producto> checkBoxList;
	private JButton btnBuscar;
	private boolean acepto;
	private List<Producto> productoSelectedList;
	private List<Producto> allProductoList;

	private JPanel pnlBotones;
	private JPanel pnlDatos;

	private ArticuloFacadeRemote articuloFacade;
	private ProductoFacadeRemote productoFacade;

	private List<Articulo> articuloFilterList;
	
	public JDialogSeleccionarProducto(JDialog owner, List<Producto> productoSelectedList) {
		super(owner);
		this.productoSelectedList = new ArrayList<Producto>(productoSelectedList);
		setUpComponentes();
		setUpScreen();
		setDatos();
	}

	public JDialogSeleccionarProducto(JDialog owner, List<Producto> productoSelectedList, List<Articulo> articuloFilterList) {
		super(owner);
		this.productoSelectedList = new ArrayList<Producto>(productoSelectedList);
		this.articuloFilterList = articuloFilterList;
		setUpComponentes();
		setUpScreen();
		setDatos();
	}

	private void setDatos() {
		for(Producto producto : productoSelectedList) {
			getClCheckBoxList().setSelectedValue(producto, false);
		}
	}

	private void setUpScreen(){
		setTitle("Selección de Productos");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(400, 550));
		setResizable(false);
		setModal(true);
	}

	private void setUpComponentes(){
		add(getPanelDatos(),BorderLayout.CENTER);
		add(getPanelBotones(),BorderLayout.SOUTH);
	}

	private JPanel getPanelBotones() {
		if(pnlBotones == null){
			pnlBotones = new JPanel();
			pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
			pnlBotones.add(getBtnAceptar());
			pnlBotones.add(getBtnCancelar());
		}
		return pnlBotones;
	}

	private JPanel getPanelDatos() {
		if(pnlDatos == null){
			pnlDatos = new JPanel();
			pnlDatos.setLayout(new GridBagLayout());
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			pnlDatos.add(new JLabel("TIPO DE PRODUCTO: "), gridBagConstraints);
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.insets = new Insets(5,5,5,5);
			pnlDatos.add(getCmbTipoProducto(), gridBagConstraints);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.insets = new Insets(5,5,5,5);
			pnlDatos.add(new JLabel("ARTICULO: "), gridBagConstraints);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new Insets(5,5,5,5);
			pnlDatos.add(getCmbArticulo(), gridBagConstraints);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 2;
			gridBagConstraints.insets = new Insets(5,5,5,5);
			pnlDatos.add(getBtnBuscar(), gridBagConstraints);

			
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 3;
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1;
			gridBagConstraints.weighty = 1;
			JScrollPane scrollPane = new JScrollPane(getClCheckBoxList());
			scrollPane.setBorder(BorderFactory.createTitledBorder("COLORES"));
			pnlDatos.add(scrollPane, gridBagConstraints);
		}
		return pnlDatos;
	}

	private JButton getBtnBuscar() {
		if(btnBuscar == null) {
			btnBuscar = new JButton("Buscar");
			btnBuscar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					Articulo artSelected = (Articulo)getCmbArticulo().getSelectedItem();
					ETipoProducto tipoProd = (ETipoProducto)getCmbTipoProducto().getSelectedItem();
					List<Producto> productoMatchedList = new ArrayList<Producto>();
					getClCheckBoxList().setAllSelectedItems(false);
					for(Producto p : allProductoList) {
						boolean cumpleTipoProd = tipoProd == null || tipoProd == p.getTipo();
						boolean cumpleArticulo = artSelected == null || artSelected.equals(p.getArticulo());
						if(cumpleArticulo && cumpleTipoProd) {
							productoMatchedList.add(p);
						}
					}
					checkBoxList.setValues(productoMatchedList.toArray(new Object[productoMatchedList.size()]));
				}

			});
		}
		return btnBuscar;
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(validar()) {
						acepto = true;
						dispose();
					}
				}

				private boolean validar() {
					if(getClCheckBoxList().getSelectedValues().length == 0){
						CLJOptionPane.showErrorMessage(JDialogSeleccionarProducto.this, "Debe seleccionar al menos un producto.", JDialogSeleccionarProducto.this.getTitle());
						return false;
					}
					return true;
				}
			});
		}
		return btnAceptar;
	}

	private JButton getBtnCancelar() {
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

	private CLCheckBoxList<Producto> getClCheckBoxList() {
		if(checkBoxList == null) {
			checkBoxList = new CLCheckBoxList<Producto>() {

				private static final long serialVersionUID = -8028977693425752374L;

				@Override
				public void itemListaSeleccionado(Object item, boolean seleccionado) {
					if(seleccionado) {
						Producto prod = (Producto)item;
						if(!productoSelectedList.contains(prod)) {
							productoSelectedList.add(prod);
						}
					} else {
						productoSelectedList.remove(item);
					}
				}

			};
			allProductoList = getProductoList();
			checkBoxList.setValues(allProductoList.toArray(new Object[allProductoList.size()]));
		}
		return checkBoxList;
	}

	private List<Producto> getProductoList() {
		List<Producto> allOrderByName = getProductoFacade().getAllOrderByName();
		if(articuloFilterList == null) {
			return allOrderByName;
		} else {
			List<Producto> productoResultList = new ArrayList<Producto>();
			for(Producto p : allOrderByName) {
				if(articuloFilterList.contains(p.getArticulo())) {
					productoResultList.add(p);
				}
			}
			return productoResultList;
		}
	}

	private JComboBox getCmbArticulo() {
		if(cmbArticulo == null) {
			cmbArticulo = new JComboBox();
			GuiUtil.llenarCombo(cmbArticulo, getArticuloList(), false);
			cmbArticulo.setSelectedIndex(-1);
		}
		return cmbArticulo;
	}

	private List<Articulo> getArticuloList() {
		List<Articulo> allOrderByName = getArticuloFacade().getAllOrderByName();
		if(articuloFilterList == null) {
			return allOrderByName;
		} else {
			List<Articulo> articuloResultList = new ArrayList<Articulo>();
			for(Articulo a : allOrderByName) {
				if(articuloFilterList.contains(a)) {
					articuloResultList.add(a);
				}
			}
			return articuloResultList;
		}
	}

	public boolean isAcepto() {
		return acepto;
	}

	private JComboBox getCmbTipoProducto() {
		if(cmbTipoProducto == null) {
			cmbTipoProducto = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoProducto, Arrays.asList(ETipoProducto.values()), false);
			cmbTipoProducto.setSelectedIndex(-1);
		}
		return cmbTipoProducto;
	}

	public ArticuloFacadeRemote getArticuloFacade() {
		if(articuloFacade == null) {
			articuloFacade = GTLBeanFactory.getInstance().getBean2(ArticuloFacadeRemote.class);
		}
		return articuloFacade;
	}

	public ProductoFacadeRemote getProductoFacade() {
		if(productoFacade == null) {
			productoFacade = GTLBeanFactory.getInstance().getBean2(ProductoFacadeRemote.class);
		}
		return productoFacade;
	}

	public List<Producto> getProductoSelectedList() {
		return productoSelectedList;
	}

}
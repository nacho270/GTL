package ar.com.textillevel.gui.modulos.abm.listaprecios;

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

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.NumUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.entidades.ventas.cotizacion.DefinicionPrecio;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAncho;
import ar.com.textillevel.facade.api.remote.TipoArticuloFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

public abstract class JDialogAgregarModificarDefinicionPrecios<T extends RangoAncho, E> extends JDialog {

	private static final long serialVersionUID = 1317620079501375084L;

	private CLJTextField txtTipoProducto;
	private JCheckBox chkAnchoExacto;
	private CLJTextField txtAnchoInicial;
	private CLJTextField txtAnchoFinal;
	private CLJTextField txtAnchoExacto;
	private JComboBox cmbTipoArticulo;
	private CLJTextField txtPrecio;
	private PanelTablaRango<T, E> tablaRango;
	private JButton btnNuevoOrCancelar;
	private JButton btnAgregar;
	private JButton btnAceptar;
	private JButton btnCancelar;
	
	private JPanel panelNorte;
	
	private static String TEXT_BTN_NUEVO = "Nuevo";
	private static String TEXT_BTN_CANCELAR = "Cancelar";

	private Cliente cliente;
	private ETipoProducto tipoProducto;
	private boolean acepto;
	private DefinicionPrecio definicion;
	private TipoArticuloFacadeRemote tipoArticuloFacade;
	
	public JDialogAgregarModificarDefinicionPrecios(Frame padre, Cliente cliente, ETipoProducto tipoProducto) {
		super(padre);
		setCliente(cliente);
		setDefinicion(new DefinicionPrecio());
		setTipoProducto(tipoProducto);
		setUpComponentes();
		setUpScreen();
	}
	
	public JDialogAgregarModificarDefinicionPrecios(Frame padre, Cliente cliente, ETipoProducto tipoProducto, DefinicionPrecio definicionAModificar) {
		super(padre);
		setCliente(cliente);
		setDefinicion(definicionAModificar);
		setTipoProducto(tipoProducto);
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen() {
		setTitle("Agregar/modificar definición de precios para - " + getTipoProducto().getDescripcion().toUpperCase());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(new Dimension(700, 600));
		setModal(true);
		setResizable(false);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		
		JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelSur.add(getBtnAceptar());
		panelSur.add(getBtnCancelar());
		
		add(getPanelNorte(), BorderLayout.NORTH);
		PanelTablaRango<T, E> tablaCentral = getTablaRango();
		if (tablaCentral != null) {
			add(tablaCentral, BorderLayout.CENTER);
		}
		add(panelSur, BorderLayout.SOUTH);
	}
	
	private JPanel getPanelNorte() {
		if(panelNorte == null) {
			panelNorte = new JPanel(new GridBagLayout());
			panelNorte.add(new JLabel("Producto: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 0, 0));
			panelNorte.add(getTxtTipoProducto(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 7, 1, 1, 1));
			
			panelNorte.add(new JLabel("Ancho inicial: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 0, 0));
			panelNorte.add(getTxtAnchoInicial(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1));
			panelNorte.add(new JLabel("Ancho final: "), GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 0, 0));
			panelNorte.add(getTxtAnchoFinal(), GenericUtils.createGridBagConstraints(3, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1));
			panelNorte.add(new JLabel("Artículo: "), GenericUtils.createGridBagConstraints(6, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 0, 0));
			panelNorte.add(getCmbTipoArticulo(), GenericUtils.createGridBagConstraints(7, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1));

			panelNorte.add(new JLabel("Usar ancho exacto"), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 1, 1));
			panelNorte.add(getChkAnchoExacto(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 1, 1));
			panelNorte.add(new JLabel("Ancho exacto: "), GenericUtils.createGridBagConstraints(2, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 0, 0));
			panelNorte.add(getTxtAnchoExacto(), GenericUtils.createGridBagConstraints(3, 2, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1));
			panelNorte.add(new JLabel("Precio: "), GenericUtils.createGridBagConstraints(6, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 0, 0));
			panelNorte.add(getTxtPrecio(), GenericUtils.createGridBagConstraints(7, 2, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1));

			JPanel panelEspecifico = createPanelDatosEspecificos();
			if (panelEspecifico != null) {
				panelNorte.add(panelEspecifico, GenericUtils.createGridBagConstraints(0, 3, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 8,1,1,1));
			}
			panelNorte.add(getBtnNuevoOrCancelar(), GenericUtils.createGridBagConstraints(3, 4, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 2, 1, 1, 1));
			panelNorte.add(getBtnAgregar(), GenericUtils.createGridBagConstraints(4, 4, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 2, 1, 1, 1));
		}
		return panelNorte;
	}

	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getTablaRango().validarNuevoRegistro();
				}
			});
		}
		return btnAceptar;
	}
	
	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public DefinicionPrecio getDefinicion() {
		return definicion;
	}

	public void setDefinicion(DefinicionPrecio definicion) {
		this.definicion = definicion;
	}

	private void salir() {
		int ret = CLJOptionPane.showQuestionMessage(this, "Va a salir sin grabar, desea continuar?", "Agregar/modificar definición de precios");
		if (ret == CLJOptionPane.YES_OPTION) {
			setAcepto(false);
			dispose();
		}
	}
	
	public JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					salir();
				}
			});
		}
		return btnCancelar;
	}

	public CLJTextField getTxtTipoProducto() {
		if (txtTipoProducto == null) {
			txtTipoProducto = new CLJTextField(getTipoProducto().getDescripcion().toUpperCase());
			txtTipoProducto.setEditable(false);
			txtTipoProducto.setPreferredSize(new Dimension(300, 20));
			txtTipoProducto.setSize(new Dimension(300, 20));
		}
		return txtTipoProducto;
	}

	public ETipoProducto getTipoProducto() {
		return tipoProducto;
	}

	public void setTipoProducto(ETipoProducto tipoProducto) {
		this.tipoProducto = tipoProducto;
	}

	protected void setModoEdicion(boolean modoEdicion) {
		GuiUtil.setEstadoPanel(getPanelNorte(), modoEdicion);
		setModoEdicionExtended(modoEdicion);
		getBtnNuevoOrCancelar().setEnabled(true);
		if(modoEdicion) {
			getBtnNuevoOrCancelar().setText(TEXT_BTN_CANCELAR);
		} else {
			getBtnNuevoOrCancelar().setText(TEXT_BTN_NUEVO);
		}
		
	}

	protected void limpiarDatos() {
		getTxtAnchoExacto().setText(null);
		getTxtAnchoFinal().setText(null);
		getTxtAnchoInicial().setText(null);
		getTxtPrecio().setText(null);
		getChkAnchoExacto().setSelected(false);
		getCmbTipoArticulo().setSelectedIndex(-1);
		limpiarDatosExtended();
	}

	protected JButton getBtnNuevoOrCancelar() {
		if (btnNuevoOrCancelar == null) {
			btnNuevoOrCancelar = new JButton(TEXT_BTN_NUEVO);
			btnNuevoOrCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(btnNuevoOrCancelar.getText().equals(TEXT_BTN_NUEVO)) {
						btnNuevoOrCancelar.setText(TEXT_BTN_CANCELAR);
						setModoEdicion(true);
						getTxtAnchoInicial().requestFocus();
					} else {
						limpiarDatos();
						btnNuevoOrCancelar.setText(TEXT_BTN_NUEVO);
						setModoEdicion(false);
					}
					botonAgregarOrCancelarPresionado();
				}
			});
		}
		return btnNuevoOrCancelar;
	}

	protected JButton getBtnAgregar() {
		if (btnAgregar == null) {
			btnAgregar = new JButton("Agregar");
			btnAgregar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(validar()) {
						botonAgregarPresionado();
						setModoEdicion(false);
						limpiarDatos();
					}
				}
			});
		}
		return btnAgregar;
	}

	public CLJTextField getTxtAnchoInicial() {
		if (txtAnchoInicial == null) {
			txtAnchoInicial = new CLJTextField();
			txtAnchoInicial.setSize(100, 20);
		}
		return txtAnchoInicial;
	}

	public CLJTextField getTxtAnchoFinal() {
		if (txtAnchoFinal == null) {
			txtAnchoFinal = new CLJTextField();
		}
		return txtAnchoFinal;
	}

	public JComboBox getCmbTipoArticulo() {
		if (cmbTipoArticulo == null) {
			cmbTipoArticulo = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoArticulo, getTipoArticuloFacade().getAllTipoArticulos(), true);
		}
		return cmbTipoArticulo;
	}

	public CLJTextField getTxtPrecio() {
		if (txtPrecio == null) {
			txtPrecio = new CLJTextField();
		}
		return txtPrecio;
	}
	
	public TipoArticuloFacadeRemote getTipoArticuloFacade() {
		if (tipoArticuloFacade == null) {
			tipoArticuloFacade = GTLBeanFactory.getInstance().getBean2(TipoArticuloFacadeRemote.class);
		}
		return tipoArticuloFacade;
	}

	public JCheckBox getChkAnchoExacto() {
		if (chkAnchoExacto == null) {
			chkAnchoExacto = new JCheckBox();
			chkAnchoExacto.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getTxtAnchoExacto().setEditable(getChkAnchoExacto().isSelected());
					getTxtAnchoInicial().setEditable(!getChkAnchoExacto().isSelected());
					getTxtAnchoFinal().setEditable(!getChkAnchoExacto().isSelected());
					if (getChkAnchoExacto().isSelected()) {
						getTxtAnchoInicial().setText("");
						getTxtAnchoFinal().setText("");
					} else {
						getTxtAnchoExacto().setText("");
					}
				}
			});
		}
		return chkAnchoExacto;
	}

	public CLJTextField getTxtAnchoExacto() {
		if (txtAnchoExacto == null) {
			txtAnchoExacto = new CLJTextField();
			txtAnchoExacto.setEditable(false);
		}
		return txtAnchoExacto;
	}
	
	public PanelTablaRango<T, E> getTablaRango() {
		if (tablaRango == null) {
			tablaRango = createPanelTabla(JDialogAgregarModificarDefinicionPrecios.this);
		}
		return tablaRango;
	}

	protected boolean validarDatosComunes() {
		boolean usaAnchoExacto = getChkAnchoExacto().isSelected();
		if(usaAnchoExacto) {
			if(StringUtil.isNullOrEmpty(getTxtAnchoExacto().getText()) && !GenericUtils.esNumerico(getTxtAnchoExacto().getText())) {
				CLJOptionPane.showErrorMessage(this, "El 'Ancho Exacto' no fue ingresado o es inválido.", "Error");
				getTxtAnchoExacto().requestFocus();
				return false;
			}
		} else {
			boolean validarRango = validarRango(getTxtAnchoInicial(), "Ancho Inicial", getTxtAnchoFinal(), "Ancho Final", true);
			if(!validarRango) {
				return false;
			}
		}
		//Tipo Articulo
		if(getCmbTipoArticulo().getSelectedItem() == null) {
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar un 'Tipo de Artículo'.", "Error");
			return false;
		}
		//Precio
		if(StringUtil.isNullOrEmpty(getTxtPrecio().getText()) && !GenericUtils.esNumerico(getTxtPrecio().getText())) {
			CLJOptionPane.showErrorMessage(this, "El 'Precio' no fue ingresado o es inválido.", "Error");
			getTxtPrecio().requestFocus();
			return false;
		}
		return true;
	}
	
	protected Float getAnchoExacto() {
		if(getChkAnchoExacto().isSelected()) {
			return Float.valueOf(getTxtAnchoExacto().getText());
		} else {
			return null;
		}
	}
	
	protected Float getAnchoInicial() {
		return Float.valueOf(getTxtAnchoInicial().getText());
	}
	
	protected Float getAnchoFinal() {
		return Float.valueOf(getTxtAnchoFinal().getText());
	}

	protected Float getPrecio() {
		return Float.valueOf(getTxtPrecio().getText());
	}

	protected TipoArticulo getTipoArticulo() {
		return (TipoArticulo)getCmbTipoArticulo().getSelectedItem();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected boolean validarRango(CLJTextField desde, String labelDesde, CLJTextField hasta, String labelHasta, boolean isFloat) {
		if(StringUtil.isNullOrEmpty(desde.getText()) && (isFloat  ? !GenericUtils.esNumerico(desde.getText()) : !NumUtil.esNumerico(desde.getText()))) {
			CLJOptionPane.showErrorMessage(this, "'" + labelDesde + "' no fue ingresado o es inválido.", "Error");
			desde.requestFocus();
			return false;
		}
		if(StringUtil.isNullOrEmpty(hasta.getText()) && (isFloat  ? !GenericUtils.esNumerico(hasta.getText()) : !NumUtil.esNumerico(hasta.getText()))) {
			CLJOptionPane.showErrorMessage(this, "'" + labelHasta + "' no fue ingresado o es inválido.", "Error");
			hasta.requestFocus();
			return false;
		}
		Comparable desdeVal = isFloat ? Float.valueOf(desde.getText()) : Integer.valueOf(desde.getText());
		Comparable hastaVal = isFloat ? Float.valueOf(hasta.getText()) : Integer.valueOf(hasta.getText());
		if(desdeVal.compareTo(hastaVal) > 0) {
			CLJOptionPane.showErrorMessage(this, "'" + labelDesde + "' debe ser menor a '" + labelHasta + "'" , "Error");
			desde.requestFocus();
			return false;
		}
		return true;
	}
	
	
	/* ABSTRACTOS */
	protected abstract JPanel createPanelDatosEspecificos();
	protected abstract PanelTablaRango<T, E> createPanelTabla(JDialogAgregarModificarDefinicionPrecios<T, E> parent);
	protected abstract void botonAgregarPresionado();
	protected abstract boolean validar();
	protected abstract void setModoEdicionExtended(boolean modoEdicion);
	protected abstract void limpiarDatosExtended();
	protected abstract void botonAgregarOrCancelarPresionado();
	public abstract void setElemHojaSiendoEditado(E elemHoja, boolean modoEdicion);

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	

}
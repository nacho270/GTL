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
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.cotizacion.DefinicionPrecio;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAncho;
import ar.com.textillevel.facade.api.remote.TipoArticuloFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

public abstract class JDialogAgregarModificarDefinicionPrecios extends JDialog {

	private static final long serialVersionUID = 1317620079501375084L;

	private CLJTextField txtTipoProducto;
	private JCheckBox chkAnchoExacto;
	private CLJTextField txtAnchoInicial;
	private CLJTextField txtAnchoFinal;
	private CLJTextField txtAnchoExacto;
	private JComboBox cmbTipoArticulo;
	private CLJTextField txtPrecio;
	private PanelTablaRango<? extends RangoAncho> tablaRango;
	private JButton btnAgregar;
	private JButton btnAceptar;
	private JButton btnCancelar;

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
		PanelTablaRango<? extends RangoAncho> tablaCentral = getTablaRango();
		if (tablaCentral != null) {
			add(tablaCentral, BorderLayout.CENTER);
		}
		add(panelSur, BorderLayout.SOUTH);
	}
	
	private JPanel getPanelNorte() {
		JPanel panelNorte = new JPanel(new GridBagLayout());
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
		panelNorte.add(getBtnAgregar(), GenericUtils.createGridBagConstraints(3, 4, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 2, 1, 1, 1));
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

	public JButton getBtnAgregar() {
		if (btnAgregar == null) {
			btnAgregar = new JButton("Agregar");
			btnAgregar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
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
	
	public PanelTablaRango<? extends RangoAncho> getTablaRango() {
		if (tablaRango == null) {
			tablaRango = createPanelTabla(JDialogAgregarModificarDefinicionPrecios.this);
		}
		return tablaRango;
	}
	
	/* ABSTRACTOS */

	protected abstract JPanel createPanelDatosEspecificos();
	protected abstract PanelTablaRango<? extends RangoAncho> createPanelTabla(JDialogAgregarModificarDefinicionPrecios parent);

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}

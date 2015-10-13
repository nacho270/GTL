package ar.com.textillevel.gui.acciones;

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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.text.MaskFormatter;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.boss.BossError;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.documentos.remito.proveedor.Bidon;
import ar.com.textillevel.entidades.documentos.remito.proveedor.ContenedorMateriaPrima;
import ar.com.textillevel.entidades.documentos.remito.proveedor.PiezaRemitoEntradaProveedor;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RelacionContenedorPrecioMatPrima;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;
import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;
import ar.com.textillevel.facade.api.remote.ContenedorMateriaPrimaFacadeRemote;
import ar.com.textillevel.facade.api.remote.PrecioMateriaPrimaFacadeRemote;
import ar.com.textillevel.facade.api.remote.RelacionContenedorMatPrimaFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoEntradaProveedorFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogAgregarRemitoEntradaProveedor extends JDialog {

	private static final long serialVersionUID = 8707094567428354187L;

	private JPanel panelDatosCliente;
	private JPanel panelDatosRemito;
	private JPanel panelBotones;
	private JPanel panDetalle;

	private FWJTextField txtRazonSocial;
	private FWJTextField txtCuit;
	private FWJTextField txtLocalidad;
	private FWJTextField txtDireccion;
	private FWJTextField txtPosicionIva;
	private FWJTextField txtIngBrutos;
	private JFormattedTextField txtNroRemito;

	private JButton btnAceptar;
	private JButton btnSalir;
	private JButton btnAgregarProducto;
	
	private PanelDatePicker panelFecha;
	private PanelTablaPiezasRemitoProveedor panTablaPiezas;

	private final Frame padre;
	private final boolean modoConsulta;

	private Proveedor proveedor;

	private RemitoEntradaProveedor remitoEntrada;
	private List<PrecioMateriaPrima> allMateriaPrimaList;
	private Map<String, RelacionContenedorPrecioMatPrima> allRelaContenedorMatPrimaMap;

	public JDialogAgregarRemitoEntradaProveedor(Frame padre, Boolean modoConsulta, Proveedor proveedor) {
		super(padre);
		this.padre = padre;
		this.proveedor = proveedor;
		this.modoConsulta = modoConsulta;
		prepareListsAndMaps(proveedor);
		setUpComponentes();
		setUpScreen();
		setDatosProveedor();
		if (modoConsulta) {
			GuiUtil.setEstadoPanel(getPanDetalle(), false);
		}
	}

	public JDialogAgregarRemitoEntradaProveedor(Frame frame, boolean modoConsulta2, RemitoEntradaProveedor remitoEntrada) {
		super(frame);
		this.padre = frame;
		this.proveedor = remitoEntrada.getProveedor();
		this.modoConsulta = modoConsulta2;
		this.remitoEntrada = remitoEntrada;
		setUpComponentes();
		setUpScreen();
		setDatosProveedor();
		setPiezasRemito();
		getTxtNroRemito().setText(String.valueOf(remitoEntrada.getNroRemito()));
		if (modoConsulta) {
			GuiUtil.setEstadoPanel(getPanDetalle(), false);
			getPanelFecha().setSelectedDate(remitoEntrada.getFechaEmision());
		} else {
			prepareListsAndMaps(proveedor);
		}
	}

	private void prepareListsAndMaps(Proveedor proveedor) {
		this.allMateriaPrimaList = quitarTelas(GTLBeanFactory.getInstance().getBean2(PrecioMateriaPrimaFacadeRemote.class).getAllOrderByName(proveedor));
		this.allRelaContenedorMatPrimaMap = GTLBeanFactory.getInstance().getBean2(RelacionContenedorMatPrimaFacadeRemote.class).getAllRelacionContenedorByIdProveedor(proveedor.getId());
		Collections.sort(allMateriaPrimaList, new MateriaPrimaComparator());
	}

	private void setPiezasRemito() {
		for (PiezaRemitoEntradaProveedor p : getRemitoEntrada().getPiezas()) {
			getPanTablaPiezas().agregarElemento(p);
		}
	}

	private void setDatosProveedor() {
		getTxtCuit().setText(getProveedor().getCuit());
		getTxtDireccion().setText(getProveedor().getDireccionFiscal().getDireccion());
		getTxtLocalidad().setText(getProveedor().getDireccionFiscal().getLocalidad().getNombreLocalidad());
		getTxtIngBrutos().setText(getProveedor().getNroIngresosBrutos());
		if (getProveedor().getPosicionIva() != null) {
			getTxtPosicionIva().setText(getProveedor().getPosicionIva().getDescripcion());
		}
		getTxtRazonSocial().setText(getProveedor().getRazonSocial());
	}

	private void setUpScreen() {
		setSize(new Dimension(790, 750));
		if(modoConsulta) {
			setTitle("Consulta remito entrada proveedor");
		} else if(getRemitoEntrada() == null || getRemitoEntrada().getId() == null) {
			setTitle("Alta remito entrada proveedor");
		} else {
			setTitle("Edición remito entrada proveedor");
		}
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent event) {
				salir();
			}
		});
		setLayout(new GridBagLayout());
		add(getPanDetalle(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 0, 1));
		add(getPanelBotones(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
	}

	private JPanel getPanelDatosCliente() {
		if (panelDatosCliente == null) {
			panelDatosCliente = new JPanel();
			panelDatosCliente.setLayout(new GridBagLayout());
			panelDatosCliente.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			panelDatosCliente.add(new JLabel("Señor/es: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosCliente.add(getTxtRazonSocial(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosCliente.add(new JLabel("C.U.I.T: "), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosCliente.add(getTxtCuit(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 3, 1, 1, 0));
			panelDatosCliente.add(new JLabel("Direccion: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosCliente.add(getTxtDireccion(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelDatosCliente.add(new JLabel("Localidad: "), GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosCliente.add(getTxtLocalidad(), GenericUtils.createGridBagConstraints(3, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelDatosCliente.add(new JLabel("Ing. Brutos: "), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosCliente.add(getTxtIngBrutos(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelDatosCliente.add(new JLabel("Posicion IVA: "), GenericUtils.createGridBagConstraints(2, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosCliente.add(getTxtPosicionIva(), GenericUtils.createGridBagConstraints(3, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
		}

		return panelDatosCliente;
	}

	private JPanel getPanDetalle() {
		if (panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(getPanelDatosCliente(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 6, 1, 0, 0));
			panDetalle.add(getPanelDatosRemito(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 6, 1, 0, 0));
			panDetalle.add(getPanTablaPiezas(), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 6, 1, 1, 1));
		}
		return panDetalle;
	}

	private FWJTextField getTxtRazonSocial() {
		if (txtRazonSocial == null) {
			txtRazonSocial = new FWJTextField();
			txtRazonSocial.setEditable(false);
		}
		return txtRazonSocial;
	}

	private FWJTextField getTxtCuit() {
		if (txtCuit == null) {
			txtCuit = new FWJTextField();
			txtCuit.setEditable(false);
		}
		return txtCuit;
	}

	private FWJTextField getTxtDireccion() {
		if (txtDireccion == null) {
			txtDireccion = new FWJTextField();
			txtDireccion.setEditable(false);
		}
		return txtDireccion;
	}

	private FWJTextField getTxtLocalidad() {
		if (txtLocalidad == null) {
			txtLocalidad = new FWJTextField();
			txtLocalidad.setEditable(false);
		}
		return txtLocalidad;
	}

	private FWJTextField getTxtPosicionIva() {
		if (txtPosicionIva == null) {
			txtPosicionIva = new FWJTextField();
			txtPosicionIva.setEditable(false);
		}
		return txtPosicionIva;
	}

	private FWJTextField getTxtIngBrutos() {
		if (txtIngBrutos == null) {
			txtIngBrutos = new FWJTextField();
			txtIngBrutos.setEditable(false);
		}
		return txtIngBrutos;
	}

	private PanelDatePicker getPanelFecha() {
		if (panelFecha == null) {
			panelFecha = new PanelDatePicker();
		}
		return panelFecha;
	}

	private JFormattedTextField getTxtNroRemito() {
		if(txtNroRemito == null) {
			try {
				txtNroRemito = new JFormattedTextField(new MaskFormatter("####-########"));
				txtNroRemito.setFocusLostBehavior(JFormattedTextField.PERSIST);
				txtNroRemito.setPreferredSize(new Dimension(120, 20));
				txtNroRemito.addFocusListener(new FocusAdapter() {
					public void focusLost(FocusEvent e) {
						try {
							if(!StringUtil.isNullOrEmpty(txtNroRemito.getText())) {
								txtNroRemito.commitEdit();
							}
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
					}
				});
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return txtNroRemito;
	}

	private JPanel getPanelDatosRemito() {
		if (panelDatosRemito == null) {
			panelDatosRemito = new JPanel();
			panelDatosRemito.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			panelDatosRemito.setLayout(new GridBagLayout());
			panelDatosRemito.add(new JLabel("Nro. Remito: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosRemito.add(getTxtNroRemito(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelDatosRemito.add(new JLabel("Fecha: "), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosRemito.add(getPanelFecha(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosRemito.add(getBtnAgregarProducto(), GenericUtils.createGridBagConstraints(4, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		}
		return panelDatosRemito;
	}

	private class PanelTablaPiezasRemitoProveedor extends PanelTabla<PiezaRemitoEntradaProveedor> {

		private static final long serialVersionUID = 4118594187928778745L;

		public static final int ID_BIDON_DUMMY = -1;

		private static final int CANT_COL_PIEZAS = 6;
		private static final int COL_CANT = 0;
		private static final int COL_UNIDAD = 1;
		private static final int COL_DESCRIPCION = 2;
		private static final int COL_CONTENEDOR = 3;
		private static final int COL_CANT_CONTENEDOR = 4;
		private static final int COL_OBJ = 5;

		private ContenedorMateriaPrimaFacadeRemote contenedorFacade;
		private JComboBox cmbContenedor;

		public PanelTablaPiezasRemitoProveedor() {
			construirTabla();
		}

		@Override
		protected void agregarElemento(PiezaRemitoEntradaProveedor elemento) {
			Object[] row = new Object[CANT_COL_PIEZAS];
			row[COL_CANT] = elemento.getCantidad() == null ? null : elemento.getCantidad().floatValue();
			row[COL_UNIDAD] = elemento.getPrecioMateriaPrima().getMateriaPrima().getUnidad().getDescripcion();
			row[COL_DESCRIPCION] = elemento.getPrecioMateriaPrima().getMateriaPrima().getDescripcion() + " - " + elemento.getPrecioMateriaPrima().getAlias();
			row[COL_CONTENEDOR] = elemento.getRelContenedorPrecioMatPrima() == null ? null : elemento.getRelContenedorPrecioMatPrima().getContenedor();
			row[COL_CANT_CONTENEDOR] = elemento.getCantContenedor() == null ? Float.valueOf("0") : elemento.getCantContenedor().floatValue();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COL_PIEZAS);
			tabla.setReorderingAllowed(false);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setHeaderAlignment(COL_CANT, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_UNIDAD, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_DESCRIPCION, FWJTable.CENTER_ALIGN);
			tabla.setFloatColumn(COL_CANT, "CANTIDAD", 120, false);
			tabla.setStringColumn(COL_UNIDAD, "UNIDAD", 80, 80, true);
			tabla.setStringColumn(COL_DESCRIPCION, "DESCRIPCION", 300, 300, true);
			tabla.setComboColumn(COL_CONTENEDOR, "Contenedor", getCmbContenedor(), 100, false);
			tabla.setFloatColumn(COL_CANT_CONTENEDOR, "Cant. Cont.", 80, false);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			return tabla;
		}

		private JComboBox getCmbContenedor() {
			if(cmbContenedor == null) {
				cmbContenedor = new JComboBox();
				cmbContenedor.addItemListener(new ItemListener() {

					public void itemStateChanged(ItemEvent e) {
						if(e.getStateChange() == ItemEvent.SELECTED && getTabla() != null) {
							if(getCmbContenedor().getSelectedIndex() != -1) {
								ContenedorMateriaPrima contenedor = (ContenedorMateriaPrima)getCmbContenedor().getSelectedItem();
								if(contenedor.getId().equals(ID_BIDON_DUMMY)) {
									int fila = getTabla().getSelectedRow();
									getTabla().setValueAt(null, fila, COL_CANT_CONTENEDOR);
								}
							}
						}
					}

				});
				
				
				List<ContenedorMateriaPrima> allContenedores = getContenedorFacade().getAllOrderByName();
				//Agrego un contenedor dummy para que puedan "deseleccionar"
				Bidon bidonDummy = new Bidon();
				bidonDummy.setId(ID_BIDON_DUMMY);
				bidonDummy.setNombre("");
				allContenedores.set(0, bidonDummy);

				GuiUtil.llenarCombo(cmbContenedor, allContenedores, true);
			}
			return cmbContenedor;
		}

		private ContenedorMateriaPrimaFacadeRemote getContenedorFacade() {
			if(contenedorFacade == null) {
				contenedorFacade = GTLBeanFactory.getInstance().getBean2(ContenedorMateriaPrimaFacadeRemote.class);
			}
			return contenedorFacade;
		}

		@Override
		protected PiezaRemitoEntradaProveedor getElemento(int fila) {
			return (PiezaRemitoEntradaProveedor) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			PiezaRemitoEntradaProveedor pieza = (PiezaRemitoEntradaProveedor) getTabla().getValueAt(fila, COL_OBJ);
			Object valueAt = getTabla().getValueAt(fila, COL_CANT);
			if (valueAt == null) {
				boolean faltaCompletarDato = ((valueAt instanceof String) && StringUtil.isNullOrEmpty(((String)valueAt).trim()));
				if(faltaCompletarDato) {
					return "Falta completar la 'Cantidad' para la pieza correspondiente a " + pieza.getPrecioMateriaPrima().getMateriaPrima().getDescripcion() + ".";
				}
			}
			boolean datoIgualACero = ((valueAt instanceof String) && ((String) valueAt).trim().equals("0")) ||
									 ((valueAt instanceof Float) && ((Float) valueAt).compareTo(0f) == 0);
			if(datoIgualACero) {
				return "La cantidad no puede ser 0";
			}
			ContenedorMateriaPrima contenedor = (ContenedorMateriaPrima)getTabla().getValueAt(fila, COL_CONTENEDOR);
			Float cantContenedor = (Float)getTabla().getTypedValueAt(fila, COL_CANT_CONTENEDOR);
			if(contenedor != null && !contenedor.getId().equals(ID_BIDON_DUMMY) && (cantContenedor == null || cantContenedor == 0f)) {
				return "Debe ingresar un valor para el contenedor";
			}
			if((contenedor == null || contenedor.getId().equals(ID_BIDON_DUMMY)) && cantContenedor != null && cantContenedor != 0f) {
				return "Debe seleccionar el contenedor";
			}
			return null;
		}

		@Override
		public boolean validarAgregar() {
			JDialogSeleccionarPrecioMateriaPrima jDialogSeleccionarMateriaPrima = new JDialogSeleccionarPrecioMateriaPrima(padre,allMateriaPrimaList);
			GuiUtil.centrar(jDialogSeleccionarMateriaPrima);
			jDialogSeleccionarMateriaPrima.setVisible(true);
			if(jDialogSeleccionarMateriaPrima.isAcepto()) {
				List<PrecioMateriaPrima> valoresSeleccionados = jDialogSeleccionarMateriaPrima.getPrecioMateriaPrimaSelectedList();
				for (PrecioMateriaPrima mp : valoresSeleccionados) {
					agregarPrecioMateriaPrimaALaTabla(mp);
				}
			}
			return false;
		}

		@Override
		protected void botonQuitarPresionado() {
			if (getTabla().getSelectedRow() > -1) {
				getTabla().removeRow(getTabla().getSelectedRow());
				getTabla().repaint();
			}
		}
	}

	private List<PrecioMateriaPrima> quitarTelas(List<PrecioMateriaPrima> allMateriaPrimaList) {
		List<PrecioMateriaPrima> pmpTelasList = new ArrayList<PrecioMateriaPrima>();
		for(PrecioMateriaPrima pmp : allMateriaPrimaList) {
			if(isTela(pmp)) {
				pmpTelasList.add(pmp);
			}
		}
		allMateriaPrimaList.removeAll(pmpTelasList);
		return allMateriaPrimaList;
	}

	private boolean isTela(PrecioMateriaPrima pmp) {
		return pmp.getMateriaPrima() != null && (pmp.getMateriaPrima().getTipo() == ETipoMateriaPrima.TELA);
	}

	private void agregarPrecioMateriaPrimaALaTabla(PrecioMateriaPrima mp) {
		if (getPanTablaPiezas().getTabla().getFirstRowWithValue(PanelTablaPiezasRemitoProveedor.COL_DESCRIPCION, mp.getMateriaPrima().getDescripcion() + " - " + mp.getAlias()) == -1) {
			PiezaRemitoEntradaProveedor pre = new PiezaRemitoEntradaProveedor();
			pre.setPrecioMateriaPrima(mp);
			getPanTablaPiezas().agregarElemento(pre);
		}
	}
	
	private PanelTablaPiezasRemitoProveedor getPanTablaPiezas() {
		if (panTablaPiezas == null) {
			panTablaPiezas = new PanelTablaPiezasRemitoProveedor();
		}
		return panTablaPiezas;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
			panelBotones.add(getBtnAceptar());
			panelBotones.add(getBtnSalir());
		}
		return panelBotones;
	}

	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.setEnabled(!modoConsulta);

			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (validar()) {
						RemitoEntradaProveedor remito = null;
						if(getRemitoEntrada() == null) { 
							remito = new RemitoEntradaProveedor();	
						} else {
							remito = getRemitoEntrada();
							remito.getPiezas().clear();
						}
						remito.setProveedor(getProveedor());
						remito.setNroRemito(getTxtNroRemito().getText().trim());
						remito.setFechaEmision(new java.sql.Date(getPanelFecha().getDate().getTime()));
						
						for (int i = 0; i < getPanTablaPiezas().getTabla().getRowCount(); i++) {
							PiezaRemitoEntradaProveedor pieza = new PiezaRemitoEntradaProveedor();
							Object cantObj = getPanTablaPiezas().getTabla().getValueAt(i, PanelTablaPiezasRemitoProveedor.COL_CANT);
							if(cantObj instanceof String) {
								pieza.setCantidad(new BigDecimal(Double.parseDouble((String) cantObj)));
							} else {
								pieza.setCantidad(new BigDecimal( (Float)cantObj ));
							}
							PrecioMateriaPrima precioMateriaPrima = ((PiezaRemitoEntradaProveedor) getPanTablaPiezas().getTabla().getValueAt(i, PanelTablaPiezasRemitoProveedor.COL_OBJ)).getPrecioMateriaPrima();
							pieza.setPrecioMateriaPrima(precioMateriaPrima);
							ContenedorMateriaPrima contenedor = ((ContenedorMateriaPrima) getPanTablaPiezas().getTabla().getValueAt(i, PanelTablaPiezasRemitoProveedor.COL_CONTENEDOR));
							if(contenedor != null && !contenedor.getId().equals(PanelTablaPiezasRemitoProveedor.ID_BIDON_DUMMY)) {
								BigDecimal cantContenedor = new BigDecimal((Float)getPanTablaPiezas().getTabla().getTypedValueAt(i, PanelTablaPiezasRemitoProveedor.COL_CANT_CONTENEDOR));
								pieza.setCantContenedor(cantContenedor);
								pieza.setRelContenedorPrecioMatPrima(getRelacionContenedor(precioMateriaPrima, contenedor));
							} else {
								pieza.setCantContenedor(new BigDecimal(0));
							}
							remito.getPiezas().add(pieza);
						}
						try {
							RemitoEntradaProveedor rep = GTLBeanFactory.getInstance().getBean2(RemitoEntradaProveedorFacadeRemote.class).save(remito);
							setRemitoEntrada(rep);
							FWJOptionPane.showInformationMessage(JDialogAgregarRemitoEntradaProveedor.this, "El remito se ha guardado con éxito", "Información");
							dispose();
						} catch (FWException cle) {
							BossError.gestionarError(cle);
						}
					}
				}

				
			});
		}
		return btnAceptar;
	}

	private RelacionContenedorPrecioMatPrima getRelacionContenedor(PrecioMateriaPrima precioMateriaPrima, ContenedorMateriaPrima contenedor) {
		RelacionContenedorPrecioMatPrima relacionContenedorPrecioMatPrima = new RelacionContenedorPrecioMatPrima();
		relacionContenedorPrecioMatPrima.setContenedor(contenedor);
		relacionContenedorPrecioMatPrima.setPrecioMateriaPrima(precioMateriaPrima);
		relacionContenedorPrecioMatPrima.setStockActual(new BigDecimal(0));
		if(allRelaContenedorMatPrimaMap.containsKey(relacionContenedorPrecioMatPrima.getKey())) {
			relacionContenedorPrecioMatPrima = allRelaContenedorMatPrimaMap.get(relacionContenedorPrecioMatPrima.getKey());
		}
		return relacionContenedorPrecioMatPrima;
	}

	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Salir");
			btnSalir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					salir();
				}
			});
		}
		return btnSalir;
	}

	private void salir() {
		if (!modoConsulta) {
			int ret = FWJOptionPane.showQuestionMessage(this, "Va a salir sin grabar, está seguro?", "Pregunta");
			if (ret == FWJOptionPane.YES_OPTION) {
				setRemitoEntrada(null);
				dispose();
			}
		} else {
			dispose();
		}
	}

	private boolean validar() {
		if (getTxtNroRemito().getText().trim().length() == 0) {
			FWJOptionPane.showErrorMessage(this, "Debe ingresar el número de remito", "Error");
			getTxtNroRemito().requestFocus();
			return false;
		}

		String regExpNroFactura = "[0-9]{4}-[0-9]{8}";
		Pattern p = Pattern.compile(regExpNroFactura);
		Matcher matcher = p.matcher(getTxtNroRemito().getText().trim());
		if(!matcher.matches()) {
			FWJOptionPane.showErrorMessage(this, "Debe ingresar un número de remito válido.", getTitle());
			getTxtNroRemito().requestFocus();
			return false;
		}

		RemitoEntradaProveedorFacadeRemote remitoEntradaProveedorFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaProveedorFacadeRemote.class);
		boolean existeNroRemito = remitoEntradaProveedorFacade.existeNroFacturaByProveedor(getRemitoEntrada() == null ? null : getRemitoEntrada().getId(), getTxtNroRemito().getText().trim(), getProveedor().getId());
		if(existeNroRemito) {
			getTxtNroRemito().requestFocus();
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap("El número de remito ya existe para el mismo proveedor."), getTitle());
			return false;
		}

		if(getPanTablaPiezas().getElementos().size()==0){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar las piezas", "Error");
			return false;
		}
		
		String textoValidacion = getPanTablaPiezas().validarElementos();
		if (textoValidacion == null) {
			return true;
		} else {
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap(textoValidacion), "Error");
			return false;
		}
	}

	public RemitoEntradaProveedor getRemitoEntrada() {
		return remitoEntrada;
	}

	public void setRemitoEntrada(RemitoEntradaProveedor remitoEntrada) {
		this.remitoEntrada = remitoEntrada;
	}

	private class MateriaPrimaComparator implements Comparator<PrecioMateriaPrima> {

		public int compare(PrecioMateriaPrima o1, PrecioMateriaPrima o2) {
			if (o1.getMateriaPrima().getTipo() == ETipoMateriaPrima.ANILINA && o2.getMateriaPrima().getTipo() == ETipoMateriaPrima.ANILINA) {
				return ((Anilina) o1.getMateriaPrima()).getTipoAnilina().getDescripcion().compareTo(((Anilina) o2.getMateriaPrima()).getTipoAnilina().getDescripcion());
			} else {
				return o1.getMateriaPrima().getTipo().getId().compareTo(o2.getMateriaPrima().getTipo().getId());
			}
		}
	}

	private JButton getBtnAgregarProducto() {
		if(btnAgregarProducto == null) {
			btnAgregarProducto = new JButton("Agregar nueva materia prima");
			btnAgregarProducto.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JDialogAltaNuevaMateriaPrimaYPrecioMateriaPrima dialogo = new JDialogAltaNuevaMateriaPrimaYPrecioMateriaPrima(padre, getProveedor());
					dialogo.setVisible(true);
					if(dialogo.isAcepto()) {
						PrecioMateriaPrima pmpAgregada = dialogo.getPrecioMateriaPrima();
						if(isTela(pmpAgregada)) {
							FWJOptionPane.showErrorMessage(JDialogAgregarRemitoEntradaProveedor.this, StringW.wordWrap("No es posible comprar tela por esta acción. Lo correcto es utilizar la operación de 'Compra de Tela' para ese fin."), "Operación inválida");
						} else {
							allMateriaPrimaList.add(pmpAgregada);
							agregarPrecioMateriaPrimaALaTabla(pmpAgregada);
						}
					}
				}
			});
		}
		return btnAgregarProducto;
	}
}

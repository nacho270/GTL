package ar.com.textillevel.gui.acciones;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWDateField;
import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.componentes.error.FWRuntimeException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.documentos.factura.CondicionDeVenta;
import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.Tarima;
import ar.com.textillevel.entidades.documentos.remito.enums.ELugarTarima;
import ar.com.textillevel.entidades.documentos.remito.enums.ESituacionODTRE;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.IProductoParaODT;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.entidades.ventas.ProductoArticuloParcial;
import ar.com.textillevel.facade.api.remote.RemitoEntradaFacadeRemote;
import ar.com.textillevel.facade.api.remote.TarimaFacadeRemote;
import ar.com.textillevel.gui.acciones.impresionremito.ImpresionRemitoEntradaFichaHandler;
import ar.com.textillevel.gui.acciones.odt.componentes.ODTLinkeableLabel;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.panels.PanComboConElementoOtro;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;
import main.GTLGlobalCache;

public class JDialogAgregarRemitoEntrada extends JDialog {

	private static final long serialVersionUID = 1L;

	private static final int MAX_LONGITUD_RAZ_SOCIAL = 50;

	private JPanel panDetalle;
	private FWJTextField txtRazonSocial;
	private PanelTablaPieza panTablaPieza;
	private JPanel pnlBotones;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JButton btnImprimir;
	private FWJNumericTextField txtNroRemito;
	private FWDateField txtFechaEmision;
	private FWJTextField txtPesoTotal;
	private FWJTextField txtAnchoCrudo;
	private FWJTextField txtAnchoFinal;
	private PanComboTarima panComboTarima;
	private JCheckBox chkEnPalet;
	private FWJTextField txtControl;
	private JPanel panTarimaEnPalet;
	private JComboBox cmbLugarTarima;

	private JButton btnSelProductos;
	private JTextField txtProductos;
	private JPanel panSelProductos;
	
	private RemitoEntrada remitoEntrada;
	private JPanel panTotales; 
	private JTextField txtMetros;
	private JTextField txtPiezas;
	private JMenuItem menuItemEliminarFilas;
	private JMenuItem menuItemAgregarPiezas;
	
	private JPanel panelDatosCliente; 

	private JPanel panOpcionPiezasODT;
	private JRadioButton rbtOpcionConPiezasODT;
	private JRadioButton rbtOpcionConPiezasODTParcial;
	private JRadioButton rbtOpcionSinPiezasODT;

	private FWJTextField txtObservacionesODT;
	private FWJTextField txtArticuloCliente;
	
	private CardLayout situacionODTCardLayout;
	private JPanel panelSituacionODT;
	
	private JPanel panSelProductosConODTParcial;
	
	private JPanel panelDatosFactura;
	private JPanel panelDatosMisc;
	private FWJTextField txtCondicionVenta;
	private JMenu menuODT;
	private boolean modoConsulta;
	private Frame owner;
	private List<OrdenDeTrabajo> odtList;

	private RemitoEntradaFacadeRemote remitoEntradaFacade;
	private TarimaFacadeRemote tarimaFacade;
	private CondicionDeVenta condicionDeVenta;

	private JPanel panODTs;
	private JPanel panODTsParcial;

	private JButton btnSelProductosConODTParcial;
	private JTextField txtProductosConODTParcial;

	private List<ProductoArticuloParcial> productoArticuloParcialList;


	public JDialogAgregarRemitoEntrada(Frame owner, RemitoEntrada remitoEntrada, List<OrdenDeTrabajo> odtList, boolean modoConsulta) {
		super(owner);
		this.owner = owner;
		this.remitoEntrada = remitoEntrada;
		this.odtList = new ArrayList<OrdenDeTrabajo>(odtList);
		this.condicionDeVenta = remitoEntrada.getCliente().getCondicionVenta();
		setSize(new Dimension(730, 750));
		this.modoConsulta = modoConsulta;
		ESituacionODTRE situacionODT = remitoEntrada.getSituacionODT();
		if(situacionODT == ESituacionODTRE.CON_ODT_PARCIAL) {
			this.productoArticuloParcialList = extractProductoArticulosParcial(odtList);
		}
		construct();
		setDatos();
		if(modoConsulta) {
			setTitle("Consulta de Remito de Entrada");
		} else {
			setTitle(remitoEntrada.getId() == null ? "Alta de Remito de Entrada" : "Edici�n de Remito de Entrada");
		}
		setModal(true);
	}

	private void setDatos() {
		Cliente cliente = remitoEntrada.getCliente();
		getTxtRazonSocial().setText(cliente.getRazonSocial());
		if(modoConsulta || remitoEntrada.getId() != null) {
			getTxtFechaEmision().setFecha(remitoEntrada.getFechaEmision());
		} else {
			getTxtFechaEmision().setFecha(DateUtil.getHoy());
		}
		
		//Unifico instancias de las piezas s�lo si es una modificacion
		if(remitoEntrada.getId()  != null) {
			unificarInstanciasEnODTs();
		}
	}

	private void unificarInstanciasEnODTs() {
		for(OrdenDeTrabajo odt : odtList) {
			for(PiezaODT podt : odt.getPiezas()) {
				PiezaRemito pr = getPiezaRemito(remitoEntrada.getPiezas(), podt.getPiezaRemito());
				if(pr == null) {
					throw new FWRuntimeException("Estado inconsistente!!!");
				}
				podt.setPiezaRemito(pr);
			}
		}
	}

	private PiezaRemito getPiezaRemito(List<PiezaRemito> piezas, PiezaRemito piezaRemito) {
		for(PiezaRemito pr : piezas) {
			if(pr.equals(piezaRemito)) {
				return pr;
			}
		}
		return null;
	}

	private void construct() {
		setLayout(new GridBagLayout());
		add(getPanDetalle(), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 0, 1));
		add(getPanTotales(), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getPanelBotones(), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
	}

	private JPanel getPanTotales() {
		if(panTotales == null) {
			panTotales = new JPanel();
			panTotales.setLayout(new GridBagLayout());
			panTotales.add(new JLabel(" PIEZAS:"), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panTotales.add(getTxtPiezas(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));
			panTotales.add(new JLabel(" METROS:"), GenericUtils.createGridBagConstraints(2, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panTotales.add(getTxtMetros(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));
		}
		return panTotales;
	}

	private JTextField getTxtPiezas() {
		if(txtPiezas == null) {
			txtPiezas = new JTextField();
			txtPiezas.setEditable(false);
			if(modoConsulta || remitoEntrada.getId() != null) {
				getTxtPiezas().setText(String.valueOf(remitoEntrada.getPiezas().size()));
			}
		}
		return txtPiezas;
	}

	private JTextField getTxtMetros() {
		if(txtMetros == null) {
			txtMetros = new JTextField();
			txtMetros.setEditable(false);
			if(modoConsulta || remitoEntrada.getId() != null) {
				txtMetros.setText(remitoEntrada.getTotalMetros().toString());
			}
		}
		return txtMetros;
	}

	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(getPanelDatosCliente(), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 3, 1, 0, 0));
			panDetalle.add(getPanelDatosFactura(), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 3, 1, 0, 0));
			panDetalle.add(getPanDatosMisc(), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 3, 1, 1, 0));
			panDetalle.add(new JLabel("Control:"), GenericUtils.createGridBagConstraints(0, 3,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtControl(), GenericUtils.createGridBagConstraints(1, 3,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.5, 0));
			panDetalle.add(getPanTarimaEnPalet(), GenericUtils.createGridBagConstraints(2, 3,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.5, 0));
			panDetalle.add(getPanOpcionPiezasODT(), GenericUtils.createGridBagConstraints(0, 4,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 3, 1, 0.3, 0));
			panDetalle.add(getPanelSituacionODT(), GenericUtils.createGridBagConstraints(0, 5,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 3, 1, 0, 0));
			panDetalle.add(getPanTablaPieza(), GenericUtils.createGridBagConstraints(0, 6, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 3, 1, 1, 1));
		}
		GuiUtil.setEstadoPanel(panDetalle, !modoConsulta);
		return panDetalle;
	}

	private JPanel getPanOpcionPiezasODT() {
		if(panOpcionPiezasODT == null) {
			panOpcionPiezasODT = new JPanel();
			panOpcionPiezasODT.setLayout(new GridBagLayout());
			panOpcionPiezasODT.add(getRbtOpcionConPiezasODT(), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 1, 1, 0, 0));
			panOpcionPiezasODT.add(getRbtOpcionConPiezasODTParcial(), GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 1, 1, 0, 0));
			panOpcionPiezasODT.add(getRbtOpcionSinPiezasODT(), GenericUtils.createGridBagConstraints(2, 0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 1, 1, 0, 0));
			ButtonGroup bgOpcionProceso = new ButtonGroup();
			bgOpcionProceso.add(getRbtOpcionConPiezasODT());
			bgOpcionProceso.add(getRbtOpcionConPiezasODTParcial());
			bgOpcionProceso.add(getRbtOpcionSinPiezasODT());
			panOpcionPiezasODT.setBorder(BorderFactory.createTitledBorder(""));
		}
		return panOpcionPiezasODT;
	}

	private JRadioButton getRbtOpcionConPiezasODT() {
		if (rbtOpcionConPiezasODT == null) {
			rbtOpcionConPiezasODT = new JRadioButton();
			rbtOpcionConPiezasODT.setText("Con ODT");
			rbtOpcionConPiezasODT.setSelected(remitoEntrada.getSituacionODT() == ESituacionODTRE.CON_ODT);
			
			rbtOpcionConPiezasODT.addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent e) {
					getPanelSituacionODT().setVisible(true);
					getSituacionODTCardLayout().show(getPanelSituacionODT(), ESituacionODTRE.CON_ODT.toString());
					getPanTablaPieza().clearODTs();
					getProductoArticuloParcialList().clear();
					odtList.clear();
				}
				
			});
		}
		return rbtOpcionConPiezasODT;
	}
	
	private JRadioButton getRbtOpcionConPiezasODTParcial() {
		if (rbtOpcionConPiezasODTParcial == null) {
			rbtOpcionConPiezasODTParcial = new JRadioButton();
			rbtOpcionConPiezasODTParcial.setText("Con ODT Sin Color");
			rbtOpcionConPiezasODTParcial.setSelected(remitoEntrada.getSituacionODT() == ESituacionODTRE.CON_ODT_PARCIAL);
			rbtOpcionConPiezasODTParcial.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					getPanelSituacionODT().setVisible(true);
					getSituacionODTCardLayout().show(getPanelSituacionODT(), ESituacionODTRE.CON_ODT_PARCIAL.toString());
					getPanTablaPieza().clearODTs();
					remitoEntrada.getProductoArticuloList().clear();
					odtList.clear();
				}

			});
		}
		return rbtOpcionConPiezasODTParcial;
	}

	private JRadioButton getRbtOpcionSinPiezasODT() {
		if (rbtOpcionSinPiezasODT == null) {
			rbtOpcionSinPiezasODT = new JRadioButton();
			rbtOpcionSinPiezasODT.setSelected(remitoEntrada.getSituacionODT() == ESituacionODTRE.SIN_ODT);
			rbtOpcionSinPiezasODT.setText("Sin ODT");
			
			rbtOpcionSinPiezasODT.addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent e) {
					getPanelSituacionODT().setVisible(false);
					getPanTablaPieza().clearODTs();
					getProductoArticuloParcialList().clear();
					remitoEntrada.getProductoArticuloList().clear();
					odtList.clear();
				}
				
			});
			
		}
		return rbtOpcionSinPiezasODT;
	}

	private JPanel getPanelDatosCliente() {
		if(panelDatosCliente == null){
			panelDatosCliente = new JPanel();
			panelDatosCliente.setLayout(new GridBagLayout());
			panelDatosCliente.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			panelDatosCliente.add(new JLabel("Se�or/es: "), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosCliente.add(getTxtRazonSocial(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 3, 1, 1, 0));
		}
		return panelDatosCliente;
	}

	private JPanel getPanelDatosFactura() {
		if(panelDatosFactura == null){
			panelDatosFactura = new JPanel();
			panelDatosFactura.setLayout(new GridBagLayout());
			panelDatosFactura.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			panelDatosFactura.add(new JLabel("Condicion de venta: "), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosFactura.add(getTxtCondicionVenta(), GenericUtils.createGridBagConstraints(1, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelDatosFactura.add(new JLabel("Remito N�: "), GenericUtils.createGridBagConstraints(2, 1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosFactura.add(getTxtNroRemito(), GenericUtils.createGridBagConstraints(3, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));

		}
		return panelDatosFactura;
	}

	private JPanel getPanDatosMisc() {
		if(panelDatosMisc == null) {
			panelDatosMisc = new JPanel();
			panelDatosMisc.setLayout(new GridBagLayout());
			panelDatosMisc.add(new JLabel(" PESO TOTAL:"), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosMisc.add(getTxtPesoTotal(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.25, 0));
			panelDatosMisc.add(new JLabel(" ANCHO CRUDO:"), GenericUtils.createGridBagConstraints(2, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosMisc.add(getTxtAnchoCrudo(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.25, 0));
			panelDatosMisc.add(new JLabel(" ANCHO FINAL:"), GenericUtils.createGridBagConstraints(4, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosMisc.add(getTxtAnchoFinal(), GenericUtils.createGridBagConstraints(5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.25, 0));
			panelDatosMisc.add(new JLabel(" FECHA:"), GenericUtils.createGridBagConstraints(6, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosMisc.add(getTxtFechaEmision(), GenericUtils.createGridBagConstraints(7, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			
			panelDatosMisc.add(new JLabel(" ART. CLIENTE:"), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosMisc.add(getTxtArticuloCliente(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosMisc.add(new JLabel(" OBSERVACIONES ODT:"), GenericUtils.createGridBagConstraints(2, 1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosMisc.add(getTxtObservacionesODT(), GenericUtils.createGridBagConstraints(3, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 6, 1, 0, 0));
		}
		return panelDatosMisc;
	}

	private FWJTextField getTxtCondicionVenta() {
		if(txtCondicionVenta == null) {
			txtCondicionVenta = new FWJTextField(condicionDeVenta.getNombre());
			txtCondicionVenta.setEditable(false);
		}
		return txtCondicionVenta;
	}

	private JButton getBtnSelProductos() {
		if(btnSelProductos == null) {
			btnSelProductos = new JButton("PRODUCTOS: ");
			btnSelProductos.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					JDialogSeleccionarProducto dialogSeleccionarProducto = new JDialogSeleccionarProducto(JDialogAgregarRemitoEntrada.this, remitoEntrada.getCliente(), remitoEntrada.getProductoArticuloList());
					GuiUtil.centrar(dialogSeleccionarProducto);
					dialogSeleccionarProducto.setVisible(true);
					if(dialogSeleccionarProducto.isAcepto()) {
						remitoEntrada.getProductoArticuloList().clear();
						List<ProductoArticulo> productoSelectedList = dialogSeleccionarProducto.getProductoSelectedList();
						remitoEntrada.getProductoArticuloList().addAll(productoSelectedList);
						getTxtProductos().setText(StringUtil.getCadena(productoSelectedList, ", "));
					}
				}

			});

		}
		return btnSelProductos;
	}

	private JPanel getPanSelProductosConODTParcial() {
		if(panSelProductosConODTParcial == null) {
			panSelProductosConODTParcial = new JPanel();
			panSelProductosConODTParcial.setLayout(new GridBagLayout());
			panSelProductosConODTParcial.add(getBtnSelProductosConODTParcial(), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panSelProductosConODTParcial.add(getTxtProductosConODTParcial(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			if(modoConsulta) {
				panSelProductosConODTParcial.add(getPanODTsParcial(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 2, 1, 0, 0));
			}
		}
		return panSelProductosConODTParcial;
	}
	
	private JTextField getTxtProductosConODTParcial() {
		if(txtProductosConODTParcial == null) {
			txtProductosConODTParcial = new JTextField();
			txtProductosConODTParcial.setEditable(false);
			txtProductosConODTParcial.setText(StringUtil.getCadena(getProductoArticuloParcialList(), ", "));
		}
		return txtProductosConODTParcial;
	}

	private JButton getBtnSelProductosConODTParcial() {
		if(btnSelProductosConODTParcial == null) {
			btnSelProductosConODTParcial = new JButton("PRODUCTOS (PARCIALES): ");
			btnSelProductosConODTParcial.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					JDialogSeleccionarProductoParcial dialogSeleccionarProducto = new JDialogSeleccionarProductoParcial(JDialogAgregarRemitoEntrada.this, extractProductoArticulosParcial(odtList), null);
					GuiUtil.centrar(dialogSeleccionarProducto);
					dialogSeleccionarProducto.setVisible(true);
					if(dialogSeleccionarProducto.isAcepto()) {
						getProductoArticuloParcialList().clear();
						List<ProductoArticuloParcial> productoSelectedList = dialogSeleccionarProducto.getProductoSelectedList();
						getTxtProductosConODTParcial().setText(StringUtil.getCadena(productoSelectedList, ", "));
						getProductoArticuloParcialList().addAll(dialogSeleccionarProducto.getProductoSelectedList());
					}
				}

			});

		}
		return btnSelProductosConODTParcial;
	}
	
	private List<ProductoArticuloParcial> extractProductoArticulosParcial(List<OrdenDeTrabajo> odtList) {
		List<ProductoArticuloParcial> all = new ArrayList<ProductoArticuloParcial>();
		for(OrdenDeTrabajo odt : odtList) {
			if(odt.getProductoParcial() != null) {
				all.add(odt.getProductoParcial());
			}
		}
		return all;
	}

	private JPanel getPanSelProductos() {
		if(panSelProductos == null) {
			panSelProductos = new JPanel();
			panSelProductos.setLayout(new GridBagLayout());
			panSelProductos.add(getBtnSelProductos(), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panSelProductos.add(getTxtProductos(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			if(modoConsulta) {
				panSelProductos.add(getPanODTs(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 2, 1, 0, 0));
			}
		}
		return panSelProductos;
	}

	private JPanel getPanODTs() {
		if(panODTs == null) {
			panODTs = new JPanel(new FlowLayout());
			panODTs.add(new JLabel("ODTs: "));
			for(OrdenDeTrabajo odt : odtList) {
				ODTLinkeableLabel odtLL = new ODTLinkeableLabel(odt, "");
				odtLL.setODT(odt);
				panODTs.add(odtLL);
			}
		}
		return panODTs;
	}

	private JPanel getPanODTsParcial() {
		if(panODTsParcial == null) {
			panODTsParcial = new JPanel(new FlowLayout());
			panODTsParcial.add(new JLabel("ODTs: "));
			for(OrdenDeTrabajo odt : odtList) {
				ODTLinkeableLabel odtLL = new ODTLinkeableLabel(odt, "");
				odtLL.setODT(odt);
				panODTsParcial.add(odtLL);
			}
		}
		return panODTsParcial;
	}
	
	
	private JTextField getTxtProductos() {
		if(txtProductos == null) {
			txtProductos = new JTextField();
			txtProductos.setEditable(false);
			if(modoConsulta || remitoEntrada.getId() != null) {
				txtProductos.setText(StringUtil.getCadena(remitoEntrada.getProductoArticuloList(), ", "));
			}
		}
		return txtProductos;
	}

	private FWJTextField getTxtPesoTotal() {
		if(txtPesoTotal == null) {
			txtPesoTotal = new FWJTextField();
			if(modoConsulta || remitoEntrada.getId() != null) {
				txtPesoTotal.setText(remitoEntrada.getPesoTotal().toString());
			}
		}
		return txtPesoTotal;
	}

	private FWJTextField getTxtAnchoCrudo() {
		if(txtAnchoCrudo == null) {
			txtAnchoCrudo = new FWJTextField();
			if(modoConsulta || remitoEntrada.getId() != null) {
				txtAnchoCrudo.setText(remitoEntrada.getAnchoCrudo() == null ? "" : remitoEntrada.getAnchoCrudo().toString());
			}
		}
		return txtAnchoCrudo;
	}

	private FWJTextField getTxtAnchoFinal() {
		if(txtAnchoFinal == null) {
			txtAnchoFinal = new FWJTextField();
			if(modoConsulta || remitoEntrada.getId() != null) {
				txtAnchoFinal.setText(remitoEntrada.getAnchoFinal() == null ? "" : remitoEntrada.getAnchoFinal().toString());
			}
		}
		return txtAnchoFinal;
	}

	private FWJTextField getTxtControl() {
		if(txtControl == null) {
			txtControl = new FWJTextField();
			if(modoConsulta || remitoEntrada.getId() != null) {
				txtControl.setText(remitoEntrada.getControl());
			}
		}
		return txtControl;
	}
	
	private PanComboTarima getPanComboTarima() {
		if(panComboTarima == null) {
			Tarima itemOtro = new Tarima();
			itemOtro.setId(-1);
			this.panComboTarima = new PanComboTarima("TARIMA: ", getTarimaFacade().getAllSorted(), itemOtro);
			if(modoConsulta || remitoEntrada.getId() != null) {
				this.panComboTarima.setSelectedItem(remitoEntrada.getTarima());
			}
		}
		return panComboTarima;
	}

	private JCheckBox getChkEnPalet() {
		if(chkEnPalet == null) {
			chkEnPalet = new JCheckBox("EN PALET");
			if(modoConsulta || remitoEntrada.getId() != null) {
				chkEnPalet.setSelected(remitoEntrada.getEnPalet() != null && remitoEntrada.getEnPalet());
			}
		}
		return chkEnPalet;
	}

	private JPanel getPanTarimaEnPalet() {
		if(panTarimaEnPalet == null) {
			panTarimaEnPalet = new JPanel();
			panTarimaEnPalet.setLayout(new GridBagLayout());
			panTarimaEnPalet.add(getPanComboTarima(), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
			panTarimaEnPalet.add(new JLabel("UBICACI�N:"), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panTarimaEnPalet.add(getCmbLugarTarima(), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panTarimaEnPalet.add(getChkEnPalet(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			
		}
		return panTarimaEnPalet;
	}

	private JComboBox getCmbLugarTarima() {
		if(cmbLugarTarima == null) {
			cmbLugarTarima = new JComboBox();
			GuiUtil.llenarCombo(cmbLugarTarima, Arrays.asList(ELugarTarima.values()), false);
			if(remitoEntrada.getLugarTarima() == null) {
				cmbLugarTarima.setSelectedIndex(-1);
			} else {
				cmbLugarTarima.setSelectedItem(remitoEntrada.getLugarTarima());
			}
		}
		return cmbLugarTarima;
	}

	private FWDateField getTxtFechaEmision() {
		if(txtFechaEmision == null) {
			txtFechaEmision = new FWDateField();
			if(modoConsulta || remitoEntrada.getId() != null) {
				txtFechaEmision.setFecha(remitoEntrada.getFechaEmision());
			}
		}
		return txtFechaEmision;
	}

	private FWJNumericTextField getTxtNroRemito() {
		if(txtNroRemito == null) {
			txtNroRemito = new FWJNumericTextField(new Long(0), Long.MAX_VALUE);
			if(modoConsulta || remitoEntrada.getId() != null) {
				getTxtNroRemito().setText(remitoEntrada.getNroRemito().toString());
			}
		}
		return txtNroRemito;
	}

	private FWJTextField getTxtRazonSocial() {
		if(txtRazonSocial == null) {
			txtRazonSocial = new FWJTextField(MAX_LONGITUD_RAZ_SOCIAL);
			txtRazonSocial.setEditable(false);
		}
		return txtRazonSocial;
	}

	private JPanel getPanelBotones() {
		if(pnlBotones == null){
			pnlBotones = new JPanel();
			pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
			pnlBotones.add(getBtnAceptar());
			pnlBotones.add(getBtnCancelar());
			if(modoConsulta) {
				pnlBotones.add(getBtnImprimir());
			}
			getBtnCancelar().setEnabled(!modoConsulta);
		}
		return pnlBotones;
	}
	
	private JButton getBtnImprimir() {
		if(btnImprimir == null) {
			btnImprimir = new JButton("Imprimir");
			btnImprimir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					List<OrdenDeTrabajo> odts = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class).getOdtEagerByRemitoList(remitoEntrada.getId());
					handleImprimir(odts);
				}

			});

		}
		return btnImprimir;
	}

	private JButton getBtnCancelar() {
		if(btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					setRemitoEntrada(null);
					dispose();
				}

			});

		}
		return btnCancelar;
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if(modoConsulta) {
						dispose();
						return;
					}
					if(validar()) {
						List<OrdenDeTrabajo> odtCapturedList = capturarSetearDatos();
						RemitoEntrada remitoEntradaSaved;
						try {
							remitoEntradaSaved = getRemitoEntradaFacade().save(remitoEntrada, odtCapturedList, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
							setRemitoEntrada(remitoEntradaSaved);
							FWJOptionPane.showInformationMessage(JDialogAgregarRemitoEntrada.this, "El remito se ha grabado con �xito.", "Atenci�n");
						} catch (ValidacionException e1) {
							FWJOptionPane.showInformationMessage(JDialogAgregarRemitoEntrada.this, StringW.wordWrap(e1.getMensajeError()), "Atenci�n");
							return;
						}

						//si hay ODTs => pregunto si las quiere imprimir
						if(!odtCapturedList.isEmpty()) {
							if(FWJOptionPane.showQuestionMessage(JDialogAgregarRemitoEntrada.this, "�Desea imprimir la(s) fichas de entrada de las ODT(s)?", "Confirmaci�n") == FWJOptionPane.YES_OPTION) {
								handleImprimir(odtCapturedList);
							}
						}

						dispose();
					} 
				}


			});
		}
		return btnAceptar;
	}

	private void handleImprimir(List<OrdenDeTrabajo> odtCapturedList) {
		for(OrdenDeTrabajo odt : odtCapturedList) {
			ImpresionRemitoEntradaFichaHandler handler = new ImpresionRemitoEntradaFichaHandler(odt, owner);
			handler.imprimir();
		}
		/*
					JDialogSeleccionarImprimirODT dialogoImpresionODT = new JDialogSeleccionarImprimirODT(owner, odtCapturedList);
					GuiUtil.centrarEnPadre(dialogoImpresionODT);
					dialogoImpresionODT.setVisible(true);
		 */
	}

	private void setRemitoEntrada(RemitoEntrada remitoEntradaSaved) {
		this.remitoEntrada = remitoEntradaSaved;
	}

	private RemitoEntradaFacadeRemote getRemitoEntradaFacade() {
		if(remitoEntradaFacade == null) {
			remitoEntradaFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaFacadeRemote.class);
		}
		return remitoEntradaFacade;
	}

	private TarimaFacadeRemote getTarimaFacade() {
		if(tarimaFacade == null) {
			tarimaFacade = GTLBeanFactory.getInstance().getBean2(TarimaFacadeRemote.class);
		}
		return tarimaFacade;
	}

	private List<OrdenDeTrabajo> capturarSetearDatos() {
		remitoEntrada.setFechaEmision(getTxtFechaEmision().getFecha());
		BigDecimal pesoTotal = new BigDecimal(getTxtPesoTotal().getText().trim().replace(',', '.'));
		remitoEntrada.setPesoTotal(pesoTotal);
		BigDecimal anchoCrudo = new BigDecimal(getTxtAnchoCrudo().getText().trim().replace(',', '.'));
		remitoEntrada.setAnchoCrudo(anchoCrudo);
		BigDecimal anchoFinal = new BigDecimal(getTxtAnchoFinal().getText().trim().replace(',', '.'));
		remitoEntrada.setAnchoFinal(anchoFinal);
		remitoEntrada.setCondicionDeVenta(condicionDeVenta);
		remitoEntrada.setNroRemito(getTxtNroRemito().getValue());
		remitoEntrada.setTarima(getPanComboTarima().getSelectedItem());
		if(getPanComboTarima().getSelectedItem() != null && getCmbLugarTarima().getSelectedItem() != null) {
			remitoEntrada.setLugarTarima((ELugarTarima)getCmbLugarTarima().getSelectedItem());
		}
		remitoEntrada.setEnPalet(getChkEnPalet().isSelected());
		remitoEntrada.setControl(getTxtControl().getText().trim());
		
		
		if (!StringUtil.isNullOrEmpty(getTxtObservacionesODT().getText())) {
			remitoEntrada.setObservacionesODT(getTxtObservacionesODT().getText().trim());
		}
		if (!StringUtil.isNullOrEmpty(getTxtArticuloCliente().getText())) {
			remitoEntrada.setArticuloCliente(getTxtArticuloCliente().getText().trim());
		}
		
		getPanTablaPieza().capturarSetearDatos();
		
		List<OrdenDeTrabajo> odTs = getPanTablaPieza().getODTs();
		if(rbtOpcionConPiezasODT.isSelected()) {
			remitoEntrada.setSituacion(ESituacionODTRE.CON_ODT);
			return odTs;
		}
		if(rbtOpcionConPiezasODTParcial.isSelected()) {
			remitoEntrada.setSituacion(ESituacionODTRE.CON_ODT_PARCIAL);
			remitoEntrada.getProductoArticuloList().clear();
			return odTs;
		}
		if(rbtOpcionSinPiezasODT.isSelected()) {
			remitoEntrada.setSituacion(ESituacionODTRE.SIN_ODT);
			remitoEntrada.getProductoArticuloList().clear();
		}
		return Collections.emptyList();
	}

	private boolean validar() {
		String pesoTotalStr = getTxtPesoTotal().getText();
		if(StringUtil.isNullOrEmpty(pesoTotalStr) || !GenericUtils.esNumerico(pesoTotalStr)) {
			FWJOptionPane.showErrorMessage(JDialogAgregarRemitoEntrada.this, "Debe ingresar un peso total v�lido.", "Error");
			getTxtPesoTotal().requestFocus();
			return false;
		}
		String anchoCrudoStr = getTxtAnchoCrudo().getText();
		if(StringUtil.isNullOrEmpty(anchoCrudoStr) || !GenericUtils.esNumerico(anchoCrudoStr)) {
			FWJOptionPane.showErrorMessage(JDialogAgregarRemitoEntrada.this, "Debe ingresar un ancho crudo v�lido.", "Error");
			getTxtAnchoCrudo().requestFocus();
			return false;
		}
		String anchoFinalStr = getTxtAnchoFinal().getText();
		if(StringUtil.isNullOrEmpty(anchoFinalStr) || !GenericUtils.esNumerico(anchoFinalStr)) {
			FWJOptionPane.showErrorMessage(JDialogAgregarRemitoEntrada.this, "Debe ingresar un ancho final v�lido.", "Error");
			getTxtAnchoFinal().requestFocus();
			return false;
		}
		Integer nroRemito = getTxtNroRemito().getValue();
		if(nroRemito == null || nroRemito <= 0) {
			FWJOptionPane.showErrorMessage(JDialogAgregarRemitoEntrada.this, "Debe ingresar un n�mero de remito.", "Error");
			getTxtNroRemito().requestFocus();
			return false;
		}
		if(StringUtil.isNullOrEmpty(getTxtControl().getText())) {
			FWJOptionPane.showErrorMessage(JDialogAgregarRemitoEntrada.this, "Debe completar el campo 'Control'", "Error");
			getTxtControl().requestFocus();
			return false;
		}
		if(getRbtOpcionConPiezasODT().isSelected() && StringUtil.isNullOrEmpty(getTxtProductos().getText())) {
			FWJOptionPane.showErrorMessage(JDialogAgregarRemitoEntrada.this, "Debe seleccionar al menos un producto.", "Error");
			return false;
		}
		String msgValidacionPiezas = getPanTablaPieza().validar();
		if(msgValidacionPiezas != null) {
			FWJOptionPane.showErrorMessage(JDialogAgregarRemitoEntrada.this, StringW.wordWrap(msgValidacionPiezas), "Error");
			return false;
		}
		BigDecimal anchoCrudo = new BigDecimal(getTxtAnchoCrudo().getText().trim().replace(',', '.'));
		BigDecimal anchoFinal = new BigDecimal(getTxtAnchoFinal().getText().trim().replace(',', '.'));
		if(anchoFinal.compareTo(anchoCrudo) > 0) {
			if(FWJOptionPane.showQuestionMessage(JDialogAgregarRemitoEntrada.this, StringW.wordWrap("El ancho final es mayor que el ancho crudo. �Desea Continuar?"), "Confirmaci�n") == FWJOptionPane.NO_OPTION) {
				return false;
			}
		}
		if(getPanComboTarima().getSelectedItem() != null && getCmbLugarTarima().getSelectedIndex() == -1) {
			FWJOptionPane.showErrorMessage(JDialogAgregarRemitoEntrada.this, "Falta elegir el lugar de la 'Tarima'", "Error");
			return false;
		}
		if(getCmbLugarTarima().getSelectedIndex() != -1 && getPanComboTarima().getSelectedItem() == null) {
			FWJOptionPane.showErrorMessage(JDialogAgregarRemitoEntrada.this, "Falta elegir la 'Tarima'", "Error");
			return false;
		}
		if(getPanComboTarima().getSelectedItem() != null && chkEnPalet.isSelected()) {
			FWJOptionPane.showErrorMessage(JDialogAgregarRemitoEntrada.this, "No se puede elegir 'en palet' y 'Tarima' al mismo tiempo.", "Error");
			return false;
		}
		return true;
	}

	private PanelTablaPieza getPanTablaPieza() {
		if(panTablaPieza == null) {
			panTablaPieza = new PanelTablaPieza(remitoEntrada);
		}
		return panTablaPieza;
	}

	private class ODTSelectedAction implements Action {
		
		private OrdenDeTrabajo odt;
		
		public ODTSelectedAction(OrdenDeTrabajo odt) {
			this.odt = odt;
		}

		public void addPropertyChangeListener(PropertyChangeListener listener) {
		}

		public Object getValue(String key) {
			return null;
		}

		public boolean isEnabled() {
			return true;
		}

		public void putValue(String key, Object value) {
		}

		public void removePropertyChangeListener(PropertyChangeListener listener) {
		}

		public void setEnabled(boolean b) {
		}

		public void actionPerformed(ActionEvent e) {
			if(odt!=null){
				getPanTablaPieza().asignarODT(odt);
			}else{
				getPanTablaPieza().getBtnSelODTs().doClick();
			}
		}

		public String toString() {
			return odt.toString();
		}

	}

	private class PanelTablaPieza extends PanelTabla<PiezaRemito> {

		private static final long serialVersionUID = 1L;

		private static final int CANT_COLS = 5;
		private static final int COL_NRO_PIEZA = 0;
		private static final int COL_METROS_PIEZA = 1;
		private static final int COL_OBSERVACIONES = 2;
		private static final int COL_ODT = 3;
		private static final int COL_OBJ = 4;

		private static final int CANT_PIEZAS_INICIALES = 30;

		private JButton btnSelODTs;
		
		private RemitoEntrada remitoEntrada;
		private Map<PiezaRemito, OrdenDeTrabajo> odtPiezaMap;

		public PanelTablaPieza(RemitoEntrada remitoEntrada) {
			agregarBoton(getBtnSelODTs());
			initializePopupMenu();
			this.remitoEntrada = remitoEntrada;
			cargarMapODTs();
			if(remitoEntrada.getPiezas().isEmpty()) {
				addRowsInTabla(CANT_PIEZAS_INICIALES);
			} else {
				agregarElementos(remitoEntrada.getPiezas());
			}
		}

		public void clearODTs() {
			List<PiezaRemito> elementos = getElementos();
			odtPiezaMap.clear();
			getTabla().removeAllRows();
			agregarElementos(elementos);
		}
		
		private void cargarMapODTs() {
			odtPiezaMap = new HashMap<PiezaRemito, OrdenDeTrabajo>();
			for(OrdenDeTrabajo odt : odtList) {
				for(PiezaODT piezaODT : odt.getPiezas()) {
					odtPiezaMap.put(piezaODT.getPiezaRemito(), odt);
				}
			}
		}

		public List<OrdenDeTrabajo> getODTs() {
			Set<OrdenDeTrabajo> odtSet = new HashSet<OrdenDeTrabajo>();
			for(int i = 0; i < getTabla().getRowCount(); i++) {
				PiezaRemito elemento = getElemento(i);
				String metrosStr = (String)getTabla().getValueAt(i, COL_METROS_PIEZA);
				OrdenDeTrabajo odt = (OrdenDeTrabajo)getTabla().getValueAt(i, COL_ODT);
				if(metrosStr != null && !metrosStr.equals("0")) {
					if(odt != null) {
						elemento.setMetros(new BigDecimal(metrosStr));
						if(odt.getId() == null) {
							odt.setRemito(remitoEntrada);
						}
						if(noExistePieza(elemento, odt)) {
							PiezaODT piezaODT = new PiezaODT();
							piezaODT.setOdt(odt);
							piezaODT.setPiezaRemito(elemento);
							odt.getPiezas().add(piezaODT);
						}
						odtSet.add(odt);
					} else {
						elemento.setPiezaSinODT(true);
					}
				}
				elemento.setObservaciones((String)getTabla().getValueAt(i, COL_OBSERVACIONES));
			}
			return new ArrayList<OrdenDeTrabajo>(odtSet);
		}

		private boolean noExistePieza(PiezaRemito elemento, OrdenDeTrabajo odt) {
			for(PiezaODT podt : odt.getPiezas()) {
				if(podt.getPiezaRemito() != null && podt.getPiezaRemito().equals(elemento)) {
					return false;
				}
			}
			return true;
		}

		private JButton getBtnSelODTs() {
			if(btnSelODTs == null) {
				btnSelODTs = new JButton("ODT");
				btnSelODTs.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						List<? extends IProductoParaODT> productosParaODT = rbtOpcionConPiezasODT.isSelected() ? remitoEntrada.getProductoArticuloList() : getProductoArticuloParcialList(); 
						JDialogSeleccionarCrearODT dialogSeleccionarCrearODT = new JDialogSeleccionarCrearODT(owner, productosParaODT, odtList);
						GuiUtil.centrarEnFramePadre(dialogSeleccionarCrearODT);
						dialogSeleccionarCrearODT.setVisible(true);
						if(dialogSeleccionarCrearODT.isAcepto()){
							OrdenDeTrabajo selectedODT = dialogSeleccionarCrearODT.getSelectedODT();
							List<OrdenDeTrabajo> odtListAgregadas = dialogSeleccionarCrearODT.getOdtList();
							getMenuODT().removeAll();
							for(OrdenDeTrabajo odt : odtListAgregadas) {
								if(!odtList.contains(odt)) {
									odtList.add(odt);
								}
								agregarODTEnMenu(odt);
							}
							asignarODT(selectedODT);
							ODTSelectedAction odtAction = new ODTSelectedAction(null);
							JMenuItem menuItem = new JMenuItem(odtAction);
							menuItem.setText("Crear ODT");
							getMenuODT().add(menuItem);
						}
					}
				});
			}
			return btnSelODTs;
		}

		private void agregarODTEnMenu(OrdenDeTrabajo odt) {
			ODTSelectedAction odtAction = new ODTSelectedAction(odt);
			JMenuItem menuItem = new JMenuItem(odtAction);
			menuItem.setText(odt.toString());
			getMenuODT().add(menuItem);
		}

		public void asignarODT(OrdenDeTrabajo selectedODT) {
			int[] selectedRows = getTabla().getSelectedRows();
			for(int row : selectedRows) {
				getTabla().setValueAt(selectedODT, row, COL_ODT);
				odtPiezaMap.put((PiezaRemito)getTabla().getValueAt(row, COL_OBJ), selectedODT);
			}
		}

		public String validar() {
			String ret = null;
			for(int i = 0; i < getTabla().getRowCount(); i++) {
				String metrosStr = (String)getTabla().getValueAt(i, COL_METROS_PIEZA);
				OrdenDeTrabajo odt = (OrdenDeTrabajo)getTabla().getValueAt(i, COL_ODT);
				if(metrosStr != null && !metrosStr.equals("0")) {
					if(odt == null && !getRbtOpcionSinPiezasODT().isSelected()) {
						ret = "Debe cargar las ODT";
						break;
					}
				}
			}
			return ret;
		}

		public void capturarSetearDatos() {
			table2Objects();
			List<PiezaRemito> piezasToRemove = new ArrayList<PiezaRemito>();
			for(PiezaRemito pe : remitoEntrada.getPiezas()) {
				if(pe.getMetros() == null || pe.getMetros().equals(new BigDecimal(0))) {
					piezasToRemove.add(pe);
				}
			}
			remitoEntrada.getPiezas().removeAll(piezasToRemove);
			remitoEntrada.recalcularOrdenes();
		}
		
		@Override
		protected void agregarElemento(PiezaRemito elemento) {
			Object[] row = getRow(elemento);
			getTabla().addRow(row);
		}

		private Object[] getRow(PiezaRemito elemento) {
			String nroPieza = null;
			nroPieza = elemento.getOrdenPieza().toString();
			Object[] row = new Object[CANT_COLS];
			row[COL_NRO_PIEZA] = nroPieza;
			row[COL_METROS_PIEZA] = elemento.getMetros() == null ? null : elemento.getMetros().toString();
			row[COL_OBSERVACIONES] = elemento.getObservaciones();
			row[COL_ODT] = getODTFromPieza(elemento);
			row[COL_OBJ] = elemento;
			return row;
		}

		private OrdenDeTrabajo getODTFromPieza(PiezaRemito pr) {
			if(pr.getId() == null || pr.getId() == 0) {
				return odtPiezaMap.get(pr);
			}
			for(Entry<PiezaRemito, OrdenDeTrabajo> entry : odtPiezaMap.entrySet()) {
				if(entry.getKey().getId().equals(pr.getId())) {
					return entry.getValue();
				}
			}
			return null;
		}
		
		@SuppressWarnings("serial")
		@Override
		protected FWJTable construirTabla() {
			FWJTable tablaPiezaEntrada = new FWJTable(0, CANT_COLS) {

				@Override
				public void cellEdited(int cell, int row) {
					if(cell == COL_METROS_PIEZA) {
						String metrosStr = (String)getTabla().getValueAt(row, COL_METROS_PIEZA);
						if(!StringUtil.isNullOrEmpty(metrosStr)) {
							table2Objects();
							actualizarTotales();
						}
					}
				}

			};
			tablaPiezaEntrada.setStringColumn(COL_NRO_PIEZA, "NUMERO", 80, 80, true);
			tablaPiezaEntrada.setFloatColumn(COL_METROS_PIEZA, "METROS", 80, false);
			tablaPiezaEntrada.setStringColumn(COL_OBSERVACIONES, "OBSERVACIONES", 205, 205, false);
			tablaPiezaEntrada.setStringColumn(COL_ODT, "ODT", 220, 220, true);
			tablaPiezaEntrada.setStringColumn(COL_OBJ, "", 0, 0, true);

			tablaPiezaEntrada.addMouseListener(new MouseAdapter() {

				@Override
				public void mousePressed(MouseEvent e) {
					handleMouseEvent(e);
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					handleMouseEvent(e);
				}

				private void handleMouseEvent(MouseEvent e) {
					if(e.isPopupTrigger()) {
						getMenuItemEliminarFilas().setEnabled(getTabla().getSelectedRow() != -1);
						getComponentPopupMenu().show(e.getComponent(), e.getX(), e.getY());
					}
				}

			});

			return tablaPiezaEntrada;
		}

		private void initializePopupMenu() {
			setComponentPopupMenu(new JPopupMenu());
			getComponentPopupMenu().add(getMenuItemAgregarPiezas());
			getComponentPopupMenu().add(getMenuItemEliminarFilas());
			getComponentPopupMenu().add(getMenuODT());
		}

		private JMenuItem getMenuItemEliminarFilas() {
			if(menuItemEliminarFilas == null) {
				menuItemEliminarFilas = new JMenuItem("Eliminar Fila(s)");
				menuItemEliminarFilas.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						getBotonEliminar().doClick();
					}
				});
			}
			return menuItemEliminarFilas;
		}

		private JMenuItem getMenuItemAgregarPiezas() {
			if(menuItemAgregarPiezas == null) {
				menuItemAgregarPiezas = new JMenuItem("Agregar Pieza(s)");
				menuItemAgregarPiezas.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						getBotonAgregar().doClick();
					}
				});
			}
			return menuItemAgregarPiezas;
		}

		private void actualizarTotales() {
			BigDecimal tm = new BigDecimal(0);
			int piezas = 0;
			for(PiezaRemito pe : remitoEntrada.getPiezas()) {
				tm = tm.add(pe.getTotalMetros());
				if(piezaValida(pe)) {
					piezas++;
				}
			}
			getTxtMetros().setText(tm.toString());
			getTxtPiezas().setText(String.valueOf(piezas));
		}

		private boolean piezaValida(PiezaRemito pe) {
			BigDecimal cero = new BigDecimal(0);
			return pe.getMetros() != null && pe.getMetros().compareTo(cero) > 0;
		}

		
		
		@Override
		protected void botonQuitarPresionado() {
			getTabla().setNumRows(0);
			agregarElementos(remitoEntrada.getPiezas());
			actualizarTotales();
		}

		@Override
		public boolean validarAgregar() {
			JDialogCantFilasInput dialogCantFilasInput = new JDialogCantFilasInput(JDialogAgregarRemitoEntrada.this, "Cantidad de Piezas");
			GuiUtil.centrarEnPadre(JDialogAgregarRemitoEntrada.this);			
			GuiUtil.centrarEnPadre(dialogCantFilasInput);
			dialogCantFilasInput.setVisible(true);
			Integer cantFilas = dialogCantFilasInput.getCantFilas();
			addRowsInTabla(cantFilas);
//			if(cantFilas != null) {
//				if(getTabla().getRowCount() + cantFilas > CANT_FILAS_MAX) {
//					CLJOptionPane.showErrorMessage(JDialogAgregarRemitoEntrada.this, "La cantidad de piezas debe ser menor a " + CANT_FILAS_MAX, "Error");
//				} else {
//				}
//			}
			return false;
		}

		private void addRowsInTabla(Integer cantFilas) {
			if(cantFilas!=null){
				for(int i = 0; i < cantFilas; i++) {
					getTabla().addRow();
					PiezaRemito piezaEntrada = new PiezaRemito();
					remitoEntrada.getPiezas().add(piezaEntrada);
					int rowCount = remitoEntrada.getPiezas().size();
					piezaEntrada.setOrdenPieza(rowCount);
					getTabla().setValueAt(piezaEntrada.getOrdenPieza(), rowCount - 1, COL_NRO_PIEZA);
					getTabla().setValueAt(piezaEntrada, rowCount - 1, COL_OBJ);
				}
			}
		}

		@Override
		public boolean validarQuitar() {
			table2Objects();
			int[] selectedRows = getTabla().getSelectedRows();
			List<PiezaRemito> piezaRemitoList = new ArrayList<PiezaRemito>();
			for(int sr : selectedRows) {
				piezaRemitoList.add(getElemento(sr));
			}
			for(PiezaRemito elemento : piezaRemitoList) {
				remitoEntrada.getPiezas().remove(elemento);

				for(OrdenDeTrabajo odt : odtPiezaMap.values()) {
					List<PiezaODT> podtToBorrarList = new ArrayList<PiezaODT>();
					for(PiezaODT podt : odt.getPiezas()) {
						if(podt.getPiezaRemito() != null && podt.getPiezaRemito().equals(elemento)) {
							podtToBorrarList.add(podt);
						}
					}
					odt.getPiezas().removeAll(podtToBorrarList);
				}
				
				odtPiezaMap.remove(elemento);
			}
			remitoEntrada.recalcularOrdenes();
			return true;
		}

		private void table2Objects() {
			for(int i = 0; i < getTabla().getRowCount(); i++) {
				PiezaRemito elemento = getElemento(i);
				String metrosStr = (String)getTabla().getValueAt(i, COL_METROS_PIEZA);
				if(metrosStr != null) {
					elemento.setMetros(new BigDecimal(metrosStr));
				}
				elemento.setObservaciones((String)getTabla().getValueAt(i, COL_OBSERVACIONES));
			}
		}

		@Override
		protected PiezaRemito getElemento(int fila) {
			return (PiezaRemito)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

	}

	private JMenu getMenuODT() {
		if(menuODT == null) {
			menuODT = new JMenu("ODTs");
			ODTSelectedAction odtAction = new ODTSelectedAction(null);
			JMenuItem menuItem = new JMenuItem(odtAction);
			menuItem.setText("Crear ODT");
			menuODT.add(menuItem);
		}
		return menuODT;
	}

	private class PanComboTarima extends PanComboConElementoOtro<Tarima> {

		private static final long serialVersionUID = 1L;

		public PanComboTarima(String lblCombo, List<Tarima> items, Tarima itemOtro) {
			super(lblCombo, items, itemOtro);
		}

		@Override
		public Tarima itemOtroSelected() {
			JDialogAltaTarima dialogAltaTarima = new JDialogAltaTarima(owner);
			GuiUtil.centrar(dialogAltaTarima);
			dialogAltaTarima.setVisible(true);
			return dialogAltaTarima.getTarima();
		}

	}

	private FWJTextField getTxtObservacionesODT() {
		if (txtObservacionesODT == null) {
			txtObservacionesODT = new FWJTextField(255);
			if(modoConsulta || remitoEntrada.getId() != null) {
				getTxtObservacionesODT().setText(remitoEntrada.getObservacionesODT());
			}
		}
		return txtObservacionesODT;
	}

	private FWJTextField getTxtArticuloCliente() {
		if (txtArticuloCliente == null) {
			txtArticuloCliente = new FWJTextField(255);
			if(modoConsulta || remitoEntrada.getId() != null) {
				getTxtArticuloCliente().setText(remitoEntrada.getArticuloCliente());
			}
		}
		return txtArticuloCliente;
	}

	private JPanel getPanelSituacionODT() {
		if(panelSituacionODT == null) {
			panelSituacionODT = new JPanel();
			panelSituacionODT.setLayout(getSituacionODTCardLayout());
			panelSituacionODT.setVisible(true);
			panelSituacionODT.add(ESituacionODTRE.CON_ODT.toString(), getPanSelProductos());
			panelSituacionODT.add(ESituacionODTRE.CON_ODT_PARCIAL.toString(), getPanSelProductosConODTParcial());
			getSituacionODTCardLayout().show(panelSituacionODT, remitoEntrada.getSituacionODT().toString());
			if(remitoEntrada.getSituacionODT() == ESituacionODTRE.SIN_ODT) {
				getPanelSituacionODT().setVisible(false);
			}
		}
		return panelSituacionODT;
	}

	private List<ProductoArticuloParcial> getProductoArticuloParcialList() {
		if(productoArticuloParcialList == null) {
			productoArticuloParcialList = new ArrayList<ProductoArticuloParcial>();
		}
		return productoArticuloParcialList;
	}

	private CardLayout getSituacionODTCardLayout() {
		if(situacionODTCardLayout == null) {
			situacionODTCardLayout = new CardLayout(5, 2);
		}
		return situacionODTCardLayout;
	}
}
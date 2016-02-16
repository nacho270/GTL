package ar.com.textillevel.gui.acciones.remitosalida;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import main.GTLGlobalCache;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWDateField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.documentos.factura.CondicionDeVenta;
import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoSalidaFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogCantFilasInput;
import ar.com.textillevel.gui.acciones.impresionremito.ImprimirRemitoHandler;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;
import ar.com.textillevel.util.GTLBeanFactory;
import ar.com.textillevel.util.ODTCodigoHelper;

public class JDialogAgregarRemitoSalida extends JDialog {

	private static final long serialVersionUID = 1L;

	private static final int MAX_LONGITUD_RAZ_SOCIAL = 50;

	private JPanel panDetalle;
	private FWJTextField txtRazonSocial;
	private PanelTablaPieza panTablaPieza;
	private JPanel pnlBotones;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JButton btnImprimir;

	private JTextField txtNroRemito;
	private FWDateField txtFechaEmision;
	private JTextField txtRemitosEntrada;
	private JTextField txtCodODT;
	private FWJTextField txtPesoTotal;
	private JTextField txtProductos;
	private RemitoSalida remitoSalida;
	private List<RemitoSalida> remitosSalida;
	private JPanel panTotales; 
	private JTextField txtMetros;
	private JTextField txtPiezas;
	private JMenuItem menuItemEliminarFilas;
	private JMenuItem menuItemAgregarPiezas;
	private JMenuItem menuItemAgregarSubpiezas;

	private JPanel panelDatosCliente; 
	private JTextField txtLocalidad;
	private JTextField txtDireccion;
	private JPanel panelDatosFactura;
	private JTextField txtCondicionVenta;
	private JTextField txtCUIT;
	private JTextField txtCondicionIVA;
	private boolean modoConsulta;

	private RemitoSalidaFacadeRemote remitoSalidaFacade;
	private ParametrosGeneralesFacadeRemote parametrosGeneralesFacade;

	private final int CANT_PIEZAS_POR_REMITO_MAX = GenericUtils.isSistemaTest() ? 48 : 53;;

	public JDialogAgregarRemitoSalida(Frame owner, RemitoSalida remitoSalida, boolean modoConsulta) {
		super(owner);
		this.remitoSalida = remitoSalida;
		this.modoConsulta = modoConsulta;
		setSize(new Dimension(680, 750));
		if(modoConsulta) {
			setTitle("Consulta de Remito de Salida");
		} else {
			setTitle("Alta de Remito de Salida");
		}
		construct();
		setDatos();
		setModal(true);
	}

	private void setDatos() {
		Cliente cliente = remitoSalida.getCliente();
		getTxtRazonSocial().setText(cliente.getRazonSocial());
		if(modoConsulta || remitoSalida.getId() != null) {
			getTxtFechaEmision().setFecha(remitoSalida.getFechaEmision());
		} else {
			getTxtFechaEmision().setFecha(DateUtil.getHoy());
		}
		getTxtCuit().setText(cliente.getCuit());
		if(cliente.getDireccionReal() != null) {
			getTxtDireccion().setText(cliente.getDireccionReal().getDireccion());
			if(cliente.getDireccionReal().getLocalidad() != null) {
				getTxtLocalidad().setText(cliente.getDireccionReal().getLocalidad().getNombreLocalidad());
			}
		}
		if(cliente.getPosicionIva() != null) {
			getTxtCondicionIVA().setText(cliente.getPosicionIva().getDescripcion());
		}
		getTxtNroRemito().setText(remitoSalida.getNroRemito().toString());
		List<OrdenDeTrabajo> odts = remitoSalida.getOdts();
		if(!odts.isEmpty()) {
			OrdenDeTrabajo odt = odts.get(0);
			if(odt.getRemito() != null) {
				CondicionDeVenta condicionDeVenta = odt.getRemito().getCondicionDeVenta();
				if(condicionDeVenta != null) {
					getTxtCondicionVenta().setText(condicionDeVenta.getNombre());
				}
			}
		}
		getTxtCodODT().setText(StringUtil.getCadena(extractCodigos(odts), ", "));
		Set<ProductoArticulo> productoList = new HashSet<ProductoArticulo>();
		for(OrdenDeTrabajo odt : odts) {
			productoList.add(odt.getProductoArticulo());
		}
		getTxtProductos().setText(StringUtil.getCadena(productoList, ", "));
		getTxtRemitosEntrada().setText(StringUtil.getCadena(extractRemitosEntrada(odts), ", "));
	}

	private Set<String> extractRemitosEntrada(List<OrdenDeTrabajo> odts) {
		Set<String> nroRemitoEntradaList = new HashSet<String>();
		for(OrdenDeTrabajo odt : odts) {
			if(odt.getRemito() != null) {
				nroRemitoEntradaList.add(String.valueOf(odt.getRemito().getNroRemito()));
			}
		}
		return nroRemitoEntradaList;
	}

	private List<String> extractCodigos(List<OrdenDeTrabajo> odts) {
		List<String> codODTList = new ArrayList<String>();
		for(OrdenDeTrabajo odt : odts) {
			codODTList.add(ODTCodigoHelper.getInstance().formatCodigo(odt.getCodigo()));
		}
		return codODTList;
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
		}
		return txtPiezas;
	}

	private JTextField getTxtMetros() {
		if(txtMetros == null) {
			txtMetros = new JTextField();
			txtMetros.setEditable(false);
		}
		return txtMetros;
	}

	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(getPanelDatosCliente(), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 6, 1, 0, 0));

			panDetalle.add(getPanelDatosFactura(), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 6, 1, 0, 0));

			panDetalle.add(new JLabel("ODT(s):"), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtCodODT(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));

			panDetalle.add(new JLabel(" PESO TOTAL:"), GenericUtils.createGridBagConstraints(2, 2,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtPesoTotal(), GenericUtils.createGridBagConstraints(3, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));

			panDetalle.add(new JLabel(" FECHA:"), GenericUtils.createGridBagConstraints(4, 2,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtFechaEmision(), GenericUtils.createGridBagConstraints(5, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));

			panDetalle.add(new JLabel(" PRODUCTOS: "), GenericUtils.createGridBagConstraints(0, 3,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtProductos(), GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 5, 1, 1, 0));
			
			panDetalle.add(getPanTablaPieza(), GenericUtils.createGridBagConstraints(0, 4, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 6, 1, 1, 1));
		}

		getTxtPesoTotal().setEnabled(!modoConsulta);
		getTxtFechaEmision().setEnabled(!modoConsulta);

		return panDetalle;
	}

	private JPanel getPanelDatosCliente() {
		if(panelDatosCliente == null){
			panelDatosCliente = new JPanel();
			panelDatosCliente.setLayout(new GridBagLayout());
			panelDatosCliente.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			panelDatosCliente.add(new JLabel("Señor/es: "), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosCliente.add(getTxtRazonSocial(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 3, 1, 1, 0));
			panelDatosCliente.add(new JLabel("Direccion: "), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosCliente.add(getTxtDireccion(), GenericUtils.createGridBagConstraints(1, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelDatosCliente.add(new JLabel("Localidad: "), GenericUtils.createGridBagConstraints(2, 1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosCliente.add(getTxtLocalidad(), GenericUtils.createGridBagConstraints(3, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
		}
		return panelDatosCliente;
	}

	private JTextField getTxtLocalidad() {
		if(txtLocalidad == null) {
			txtLocalidad = new JTextField();
			txtLocalidad.setEditable(false);
		}
		return txtLocalidad;
	}

	private JTextField getTxtDireccion() {
		if(txtDireccion == null) {
			txtDireccion = new JTextField();
			txtDireccion.setEditable(false);
		}
		return txtDireccion;
	}

	private JPanel getPanelDatosFactura() {
		if(panelDatosFactura == null){
			panelDatosFactura = new JPanel();
			panelDatosFactura.setLayout(new GridBagLayout());
			panelDatosFactura.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			panelDatosFactura.add(new JLabel("IVA: "), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosFactura.add(getTxtCondicionIVA(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelDatosFactura.add(new JLabel("C.U.I.T: "), GenericUtils.createGridBagConstraints(2, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosFactura.add(getTxtCuit(), GenericUtils.createGridBagConstraints(3, 0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelDatosFactura.add(new JLabel("Condicion de venta: "), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosFactura.add(getTxtCondicionVenta(), GenericUtils.createGridBagConstraints(1, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelDatosFactura.add(new JLabel("Remito Nº: "), GenericUtils.createGridBagConstraints(2, 1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosFactura.add(getTxtNroRemito(), GenericUtils.createGridBagConstraints(3, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelDatosFactura.add(new JLabel("Remito(s) de Entrada: "), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosFactura.add(getTxtRemitosEntrada(), GenericUtils.createGridBagConstraints(1, 2,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 3, 1, 1, 0));
		}
		return panelDatosFactura;
	}

	private JTextField getTxtCondicionVenta() {
		if(txtCondicionVenta == null) {
			txtCondicionVenta = new JTextField();
			txtCondicionVenta.setEditable(false);
		}
		return txtCondicionVenta;
	}

	private JTextField getTxtCuit() {
		if(txtCUIT == null) {
			txtCUIT = new JTextField();
			txtCUIT.setEditable(false);
		}
		return txtCUIT;
	}

	private JTextField getTxtCondicionIVA() {
		if(txtCondicionIVA == null) {
			txtCondicionIVA = new JTextField();
			txtCondicionIVA.setEditable(false);
		}
		return txtCondicionIVA;
	}

	private JTextField getTxtProductos() {
		if(txtProductos == null) {
			txtProductos = new JTextField();
			txtProductos.setEditable(false);
			if(modoConsulta || remitoSalida.getId() != null) {
				txtProductos.setText(StringUtil.getCadena(remitoSalida.getProductoList(), ", "));
			}
		}
		return txtProductos;
	}

	private FWJTextField getTxtPesoTotal() {
		if(txtPesoTotal == null) {
			txtPesoTotal = new FWJTextField();
			if(modoConsulta || remitoSalida.getId() != null) {
				txtPesoTotal.setText(remitoSalida.getPesoTotal().toString());
			}
		}
		return txtPesoTotal;
	}

	private JTextField getTxtCodODT() {
		if(txtCodODT == null) {
			txtCodODT = new JTextField();
			txtCodODT.setEditable(false);
		}
		return txtCodODT;
	}

	private FWDateField getTxtFechaEmision() {
		if(txtFechaEmision == null) {
			txtFechaEmision = new FWDateField();
			if(modoConsulta || remitoSalida.getId() != null) {
				txtFechaEmision.setFecha(remitoSalida.getFechaEmision());
			}
		}
		return txtFechaEmision;
	}

	private JTextField getTxtNroRemito() {
		if(txtNroRemito == null) {
			txtNroRemito = new FWJTextField();
			txtNroRemito.setEditable(false);
			if(modoConsulta || remitoSalida.getId() != null) {
				txtNroRemito.setText(remitoSalida.getNroRemito().toString());
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

	private JButton getBtnCancelar() {
		if(btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					setRemitoSalida(null);
					setRemitosSalida(null);
					dispose();
				}

			});
		}
		return btnCancelar;
	}

	private JButton getBtnImprimir() {
		if(btnImprimir == null) {
			btnImprimir = new JButton("Imprimir");
			btnImprimir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					ImprimirRemitoHandler imprimirRemitoHandler = new ImprimirRemitoHandler(getRemitoSalida(), getParametrosGeneralesFacade().getParametrosGenerales().getNroSucursal(), JDialogAgregarRemitoSalida.this);
					imprimirRemitoHandler.imprimir();
				}

			});
			
		}
		return btnImprimir;
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
						RemitoSalida remitoSalida = capturarSetearDatos();
						//Lógica de múltiples remitos
						if(remitoSalida.getPiezas().size() > CANT_PIEZAS_POR_REMITO_MAX) {
							Integer nroRemito = remitoSalida.getNroRemito();
							Integer cantRemitos = (int)Math.ceil(remitoSalida.getPiezas().size() / (double)CANT_PIEZAS_POR_REMITO_MAX);
							List<Integer> nrosRemito = new ArrayList<Integer>();
							for(int i = 0; i < cantRemitos; i++) {
								nrosRemito.add(nroRemito + i);
							}
							StringBuffer msg = new StringBuffer();
							msg.append("La cantidad de piezas excede la máxima por remito de salida: " + CANT_PIEZAS_POR_REMITO_MAX + ", ")
							   .append("como consecuencia se grabarán " + cantRemitos + " remitos con números : " + StringUtil.getCadena(nrosRemito, ", "))
							   .append(". ¿Desea Continuar?");

							if(FWJOptionPane.showQuestionMessage(JDialogAgregarRemitoSalida.this, StringW.wordWrap(msg.toString()), "Advertencia") == FWJOptionPane.YES_OPTION) {
								List<RemitoSalida> remitosSalida = calcularRemitosSalida(remitoSalida, cantRemitos);
								//Se calcula la merma de cada uno
								for(RemitoSalida rs : remitosSalida) {
									calcularSetearMerma(rs);
								}

								Collections.sort(remitosSalida, new Comparator<RemitoSalida>() {
									public int compare(RemitoSalida o1, RemitoSalida o2) {
										return o1.getNroRemito().compareTo(o2.getNroRemito());
									}
								});

								//calculo los pesos totales
								JDialogIngresoPesoTotalMultipleRemitos dialogo = new JDialogIngresoPesoTotalMultipleRemitos(JDialogAgregarRemitoSalida.this, remitosSalida);
								GuiUtil.centrar(dialogo);
								dialogo.setVisible(true);
								if(!dialogo.isAcepto()) {
									remitoSalida.getPiezas().clear();
									return;
								}

								List<RemitoSalida> remitosSalidaSaved = getRemitoSalidaFacade().save(remitosSalida, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
								setRemitosSalida(remitosSalidaSaved);
								setRemitoSalida(null);
								BigDecimal toleranciaPorcentajeMerma = getParametrosGeneralesFacade().getParametrosGenerales().getPorcentajeToleranciaMermaNegativa();
								if(toleranciaPorcentajeMerma == null) {
									FWJOptionPane.showErrorMessage(JDialogAgregarRemitoSalida.this, StringW.wordWrap("Falta configurar el porcentaje de tolerancia de merma en los parámetros generales."), "Error");
									return;
								}
								JDialogResultadoAltaRemitoSalida dialogResultadoAltaRemitoSalida = new JDialogResultadoAltaRemitoSalida(JDialogAgregarRemitoSalida.this, remitosSalidaSaved, toleranciaPorcentajeMerma);
								GuiUtil.centrar(dialogResultadoAltaRemitoSalida);
								dialogResultadoAltaRemitoSalida.setVisible(true);
								if(FWJOptionPane.showQuestionMessage(JDialogAgregarRemitoSalida.this, "¿Desea imprimir los remitos?", "Confirmación") == FWJOptionPane.YES_OPTION) {
									ImprimirRemitoHandler imprimirRemitoHandler = new ImprimirRemitoHandler(remitosSalidaSaved, getParametrosGeneralesFacade().getParametrosGenerales().getNroSucursal(), JDialogAgregarRemitoSalida.this);
									imprimirRemitoHandler.imprimir();
									dispose();
									return;
								} else {
									dispose();
									return;
								}
							} else {
								return;
							}
						}

						//Valido y seteo el peso total válido cuando se trata de un remito de salida simple
						String pesoTotalStr = getTxtPesoTotal().getText();
						if(StringUtil.isNullOrEmpty(pesoTotalStr) || !GenericUtils.esNumerico(pesoTotalStr)) {
							FWJOptionPane.showErrorMessage(JDialogAgregarRemitoSalida.this, "Debe ingresar un peso total válido.", "Error");
							getTxtPesoTotal().requestFocus();
							return;
						}
						BigDecimal pesoTotal = new BigDecimal(getTxtPesoTotal().getText().trim().replace(',', '.'));
						remitoSalida.setPesoTotal(pesoTotal);

						//Lógica de remito simple
						calcularSetearMerma(remitoSalida);
						RemitoSalida remitoSalidaSaved = getRemitoSalidaFacade().save(remitoSalida, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
						setRemitoSalida(remitoSalidaSaved);
						setRemitosSalida(null);
						BigDecimal toleranciaPorcentajeMerma = getParametrosGeneralesFacade().getParametrosGenerales().getPorcentajeToleranciaMermaNegativa();
						if(toleranciaPorcentajeMerma == null) {
							FWJOptionPane.showErrorMessage(JDialogAgregarRemitoSalida.this, StringW.wordWrap("Falta configurar el porcentaje de tolerancia de merma en los parámetros generales."), "Error");
							return;
						}
						JDialogResultadoAltaRemitoSalida dialogResultadoAltaRemitoSalida = new JDialogResultadoAltaRemitoSalida(JDialogAgregarRemitoSalida.this, Collections.singletonList(remitoSalidaSaved), toleranciaPorcentajeMerma);
						GuiUtil.centrar(dialogResultadoAltaRemitoSalida);
						dialogResultadoAltaRemitoSalida.setVisible(true);

						if(FWJOptionPane.showQuestionMessage(JDialogAgregarRemitoSalida.this, "¿Desea imprimir el remito?", "Confirmación") == FWJOptionPane.YES_OPTION) {
							ImprimirRemitoHandler imprimirRemitoHandler = new ImprimirRemitoHandler(remitoSalidaSaved, getParametrosGeneralesFacade().getParametrosGenerales().getNroSucursal(), JDialogAgregarRemitoSalida.this);
							imprimirRemitoHandler.imprimir();
						}
						
						dispose();
					} 
				}

			});
		}
		return btnAceptar;
	}

	private List<RemitoSalida> calcularRemitosSalida(RemitoSalida rs, Integer cantRemitos) {
		Integer nroRemito = rs.getNroRemito();
		List<RemitoSalida> remitos = new ArrayList<RemitoSalida>();
		List<PiezaRemito> piezas = new ArrayList<PiezaRemito>(rs.getPiezas());
		rs.getPiezas().clear();
		rs.getPiezas().addAll(new ArrayList<PiezaRemito>(piezas.subList(0, CANT_PIEZAS_POR_REMITO_MAX)));
		//Creo los remitos y seteo los datos básicos
		for(int i = 1; i < cantRemitos; i++) {
			RemitoSalida rsNew = new RemitoSalida();
			rsNew.setNroRemito(nroRemito + i);
			rsNew.setNroOrden(i);
			rsNew.setTipoRemitoSalida(rs.getTipoRemitoSalida());
			rsNew.setNroFactura(rs.getNroFactura());
			rsNew.setNroSucursal(rs.getNroSucursal());
			rsNew.setFechaEmision(rs.getFechaEmision());
			rsNew.setCliente(rs.getCliente());
			rsNew.getPiezas().addAll(new ArrayList<PiezaRemito>(piezas.subList(CANT_PIEZAS_POR_REMITO_MAX*i, Math.min(piezas.size(), CANT_PIEZAS_POR_REMITO_MAX*(i+1)))));
			remitos.add(rsNew);
		}
		remitos.add(rs);
		//seteo las ODTS
		for(RemitoSalida remitoSalida : remitos) {
			remitoSalida.getOdts().clear();
			Set<OrdenDeTrabajo> odts = new HashSet<OrdenDeTrabajo>(); 
			for(PiezaRemito pr : rs.getPiezas()) {
				for(PiezaODT podt : pr.getPiezasPadreODT()) {
					odts.add(podt.getOdt());
				}
			}
			remitoSalida.getOdts().addAll(odts);
		}
		return remitos;
	}

	private void calcularSetearMerma(RemitoSalida remitoSalida) {
		double totalMetrosDesdeODTs = 0;
		//Calculo Primero las piezas ODTs involucradas y luego sumo el total de metros en base a ellas
		Set<PiezaODT> piezaODTSet = new HashSet<PiezaODT>();
		for(PiezaRemito pr : remitoSalida.getPiezas()) {
			piezaODTSet.addAll(pr.getPiezasPadreODT());
		}
		for(PiezaODT podt : piezaODTSet) {
			totalMetrosDesdeODTs += podt.getMetros().doubleValue();
		}
		//hago el cálculo
		double totalMetrosRS = remitoSalida.getTotalMetros().doubleValue();
		double difMetros = totalMetrosRS - totalMetrosDesdeODTs;
		double porcentajeMerma = (difMetros*100)/totalMetrosDesdeODTs;
		remitoSalida.setPorcentajeMerma(new BigDecimal(porcentajeMerma));
	}

	private void setRemitoSalida(RemitoSalida remitoSalidaSaved) {
		this.remitoSalida = remitoSalidaSaved;
	}

	public RemitoSalida getRemitoSalida() {
		return remitoSalida;
	}

	private RemitoSalidaFacadeRemote getRemitoSalidaFacade() {
		if(remitoSalidaFacade == null) {
			remitoSalidaFacade = GTLBeanFactory.getInstance().getBean2(RemitoSalidaFacadeRemote.class);
		}
		return remitoSalidaFacade;
	}

	private RemitoSalida capturarSetearDatos() {
		remitoSalida.setFechaEmision(getTxtFechaEmision().getFecha());
		getPanTablaPieza().capturarSetearDatos();
		return remitoSalida;
	}

	private boolean validar() {
		String msgValidacionPiezas = getPanTablaPieza().validar();
		if(msgValidacionPiezas != null) {
			FWJOptionPane.showErrorMessage(JDialogAgregarRemitoSalida.this, StringW.wordWrap(msgValidacionPiezas), "Error");
			return false;
		}
		return true;
	}

	private PanelTablaPieza getPanTablaPieza() {
		if(panTablaPieza == null) {
			panTablaPieza = new PanelTablaPieza(remitoSalida, modoConsulta);
			if(modoConsulta) {
				panTablaPieza.actualizarTotales();
			}
		}
		return panTablaPieza;
	}

	private class PanelTablaPieza extends PanelTabla<PiezaRemito> {

		private static final long serialVersionUID = 1L;

		private static final int CANT_COLS = 5;
		private static final int COL_METROS_PIEZA_ORIG = 0;
		private static final int COL_NRO_PIEZA = 1;
		private static final int COL_METROS_PIEZA = 2;
		private static final int COL_OBSERVACIONES = 3;
		private static final int COL_OBJ = 4;

		private static final int CANT_PIEZAS_INICIALES = 15;
		private final int CANT_FILAS_MAX = GenericUtils.isSistemaTest() ? 48 : 53;

		private JButton btnAgregarSubPiezas;
		private JButton btnCombinarPiezas;
		private JButton btnDescombinarPiezas;

		private RemitoSalida remitoSalida;

		public PanelTablaPieza(RemitoSalida remitoSalida, boolean modoConsulta) {
			setModoConsulta(modoConsulta);
			initializePopupMenu();
			agregarBoton(getBtnAgregarSubPiezas());
			agregarBoton(getBtnCombinarPiezas());
			agregarBoton(getBtnDescombinarPiezas());
			getBotonAgregar().setVisible(false);
			this.remitoSalida = remitoSalida;
			if(remitoSalida.getPiezas().isEmpty()) {
				addRowsInTabla(CANT_PIEZAS_INICIALES, true);
				actualizarTotales();
			} else {
				agregarElementos(remitoSalida.getPiezas());
				if(!modoConsulta) {
					setearNumerosPiezas();
				}
			}
		}

		private JButton getBtnCombinarPiezas() {
			if(btnCombinarPiezas == null) {
				btnCombinarPiezas = new JButton("COMBINAR");
				btnCombinarPiezas.setEnabled(false);
				btnCombinarPiezas.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						int[] selectedRows = getTabla().getSelectedRows();
						table2Objects();
						
						List<PiezaRemito> prList = new ArrayList<PiezaRemito>();
						for(int sr : selectedRows) {
							prList.add(getElemento(sr));
						}
						remitoSalida.getPiezas().removeAll(prList);

						PiezaRemito piezaRemito = prList.get(0);
						for(int i = 1; i < prList.size(); i ++) {
							PiezaRemito piezaRemito2 = prList.get(i);
							piezaRemito.getPiezasPadreODT().addAll(piezaRemito2.getPiezasPadreODT());
							piezaRemito.setMetros(piezaRemito.getMetros().add(piezaRemito2.getMetros()));
						}
						remitoSalida.getPiezas().add(piezaRemito);

						refreshTabla();
					}

				});
			}
			return btnCombinarPiezas;
		}

		private JButton getBtnDescombinarPiezas() {
			if(btnDescombinarPiezas == null) {
				btnDescombinarPiezas = new JButton("DESCOMBINAR");
				btnDescombinarPiezas.setEnabled(false);
				btnDescombinarPiezas.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						int[] selectedRows = getTabla().getSelectedRows();
						for(int sr : selectedRows) {
							PiezaRemito elemento = getElemento(sr);
							remitoSalida.getPiezas().remove(elemento);
							for(PiezaODT podt : elemento.getPiezasPadreODT()) {
								PiezaRemito pr = new PiezaRemito();
								pr.setPiezaEntrada(podt.getPiezaRemito());
								pr.getPiezasPadreODT().add(podt);
								pr.setMetros(podt.getPiezaRemito().getMetros());
								pr.setOrdenPieza(podt.getPiezaRemito().getOrdenPieza());
								remitoSalida.getPiezas().add(pr);
							}
						}
						refreshTabla();
					}

				});
			}
			return btnDescombinarPiezas;
		}
		
		public String validar() {
			return null;
		}

		public void capturarSetearDatos() {
			table2Objects();
			List<PiezaRemito> piezasToRemove = new ArrayList<PiezaRemito>();
			for(PiezaRemito pe : remitoSalida.getPiezas()) {
				if(pe.getMetros() == null) {
					piezasToRemove.add(pe);
				}
			}
			remitoSalida.getPiezas().removeAll(piezasToRemove);
			remitoSalida.recalcularOrdenes();
		}

		@Override
		protected void agregarElemento(PiezaRemito elemento) {
			Object[] row = getRow(elemento);
			getTabla().addRow(row);
		}

		private Object[] getRow(PiezaRemito elemento) {
			String nroPieza = elemento.getOrdenPieza().toString();
			Object[] row = new Object[CANT_COLS];
			row[COL_NRO_PIEZA] = nroPieza;
			row[COL_METROS_PIEZA] = elemento.getMetros() == null ? null : elemento.getMetros().toString();
			row[COL_OBSERVACIONES] = elemento.getObservaciones();
			row[COL_METROS_PIEZA_ORIG] = elemento.getPiezaEntrada() == null ? null : getSumMetros(elemento);
			row[COL_OBJ] = elemento;
			return row;
		}

		private BigDecimal getSumMetros(PiezaRemito elemento) {
			BigDecimal sum = new BigDecimal(0);
			for(PiezaODT podt : elemento.getPiezasPadreODT()) {
				sum = sum.add(podt.getPiezaRemito().getMetros());
			}
			return sum;
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
			tablaPiezaEntrada.setFloatColumn(COL_METROS_PIEZA_ORIG, "METROS ENT.", 80, true);
			tablaPiezaEntrada.setFloatColumn(COL_METROS_PIEZA, "METROS", 0, Float.MAX_VALUE, 80, false);
			tablaPiezaEntrada.setStringColumn(COL_OBSERVACIONES, "OBSERVACIONES", 225, 225, false);
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
						getMenuItemAgregarSubpiezas().setEnabled(!esSubpieza(getTabla().getSelectedRow()));
						getComponentPopupMenu().show(e.getComponent(), e.getX(), e.getY());
					}
				}

			});

			return tablaPiezaEntrada;
		}

		private void initializePopupMenu() {
			setComponentPopupMenu(new JPopupMenu());
			getComponentPopupMenu().add(getMenuItemAgregarSubpiezas());
			getComponentPopupMenu().add(getMenuItemEliminarFilas());
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

		@SuppressWarnings("unused")
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

		private JMenuItem getMenuItemAgregarSubpiezas() {
			if(menuItemAgregarSubpiezas == null) {
				menuItemAgregarSubpiezas = new JMenuItem("Agregar Subpieza(s)");
				menuItemAgregarSubpiezas.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						getBtnAgregarSubPiezas().doClick();
					}
				});
			}
			return menuItemAgregarSubpiezas;
		}

		public void actualizarTotales() {
			BigDecimal tm = new BigDecimal(0);
			int piezas = 0;
			for(PiezaRemito pe : remitoSalida.getPiezas()) {
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
			refreshTabla();
		}

		private void refreshTabla() {
			remitoSalida.recalcularOrdenes();
			getTabla().setNumRows(0);
			agregarElementos(remitoSalida.getPiezas());
			setearNumerosPiezas();
			actualizarTotales();
		}

		@Override
		public boolean validarAgregar() {
			JDialogCantFilasInput dialogCantFilasInput = new JDialogCantFilasInput(JDialogAgregarRemitoSalida.this, "Cantidad de Piezas");
			GuiUtil.centrarEnPadre(JDialogAgregarRemitoSalida.this);			
			GuiUtil.centrarEnPadre(dialogCantFilasInput);
			dialogCantFilasInput.setVisible(true);
			Integer cantFilas = dialogCantFilasInput.getCantFilas();
			if(cantFilas != null) {
				if(remitoSalida.getPiezas().size() + cantFilas > CANT_FILAS_MAX) {
					FWJOptionPane.showErrorMessage(JDialogAgregarRemitoSalida.this, "La cantidad de piezas debe ser menor a " + CANT_FILAS_MAX, "Error");
				} else {
					addRowsInTabla(cantFilas, false);
				}
			}
			return false;
		}

		private void addRowsInTabla(Integer cantFilas, boolean sugerirMetrosEntrada) {
			int ordenPieza = 0;
			for(OrdenDeTrabajo odt : remitoSalida.getOdts()) {
				for(PiezaODT podt : odt.getPiezas()) {
					if(podt.getPiezasSalida().isEmpty()) {
						PiezaRemito piezaEntrada = new PiezaRemito();
						piezaEntrada.setPiezaEntrada(podt.getPiezaRemito());
						piezaEntrada.getPiezasPadreODT().add(podt);
						if(sugerirMetrosEntrada) {
							piezaEntrada.setMetros(podt.getPiezaRemito().getMetros());
						}
						remitoSalida.getPiezas().add(piezaEntrada);
						piezaEntrada.setOrdenPieza(ordenPieza);
						agregarElemento(piezaEntrada);
						ordenPieza ++;
					}
				}
			}
//			remitoSalida.recalcularOrdenes();
			setearNumerosPiezas();
		}

		@Override
		public boolean validarQuitar() {
			table2Objects();
			int[] selectedRows = getTabla().getSelectedRows();
			List<PiezaRemito> piezaEntradaList = new ArrayList<PiezaRemito>();
			for(int sr : selectedRows) {
				PiezaRemito elemento = getElemento(sr);
				piezaEntradaList.add(elemento);
			}
			remitoSalida.getPiezas().removeAll(piezaEntradaList);

			//FIXME: borrar bien cuando selecciono una pieza padre y otra subpieza
			//FIXME: deshabilitar el combinar cuando se selecciona una subpieza

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

		@Override
		protected void filaTablaSeleccionada() {
			getBtnAgregarSubPiezas().setEnabled(!modoConsulta && !esSubpieza(getTabla().getSelectedRow()));
			//manejo de la habilitación del boton combinar
			int[] selectedRows = getTabla().getSelectedRows();
			if(selectedRows.length > 1) {
				boolean enabledComb = true;
				for(int sr : selectedRows) {
					enabledComb = enabledComb && !esSubpieza(sr);
				}
				getBtnCombinarPiezas().setEnabled(!modoConsulta && enabledComb);
			} else {
				getBtnCombinarPiezas().setEnabled(false);
			}
			//manejo de la habilitación del boton descombinar
			boolean enabledDescomb = true;
			for(int sr : selectedRows) {
				PiezaRemito elemento = getElemento(sr);
				enabledDescomb =  enabledDescomb && elemento.getPiezasPadreODT().size() > 1;
			}
			getBtnDescombinarPiezas().setEnabled(!modoConsulta && selectedRows.length > 0 && enabledDescomb);
		}

		private boolean esSubpieza(int selectedRow) {
			if(selectedRow < 0) {
				return true;
			}
			if(selectedRow == 0) {
				return false;
			}
			PiezaRemito piezaRemitoAnt = (PiezaRemito)getTabla().getValueAt(selectedRow - 1, COL_OBJ);
			PiezaRemito piezaRemitoAct = (PiezaRemito)getTabla().getValueAt(selectedRow, COL_OBJ);
			return piezaRemitoAnt.getPiezaEntrada().equals(piezaRemitoAct.getPiezaEntrada());
		}

		private JButton getBtnAgregarSubPiezas() {
			if(btnAgregarSubPiezas == null) {
				btnAgregarSubPiezas = new JButton("Agregar Subpiezas");
				btnAgregarSubPiezas.setEnabled(false);
				btnAgregarSubPiezas.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						int selectedRow = getTabla().getSelectedRow();
						JDialogCantFilasInput dialogCantFilasInput = new JDialogCantFilasInput(JDialogAgregarRemitoSalida.this, "Cantidad de Subpiezas");
						GuiUtil.centrarEnPadre(dialogCantFilasInput);
						dialogCantFilasInput.setVisible(true);
						Integer cantRowsAgregar = dialogCantFilasInput.getCantFilas();
						if(cantRowsAgregar != null) {
							if(remitoSalida.getPiezas().size() + cantRowsAgregar > CANT_FILAS_MAX) {
								FWJOptionPane.showErrorMessage(JDialogAgregarRemitoSalida.this, "La cantidad de piezas debe ser menor a " + CANT_FILAS_MAX, "Error");
							} else {
								PiezaRemito pSelected = getElemento(selectedRow);
								for(int i = 1; i <= cantRowsAgregar; i++) {
									PiezaRemito subpieza = new PiezaRemito();
									subpieza.setOrdenPieza(pSelected.getOrdenPieza());
									subpieza.setPiezaEntrada(pSelected.getPiezaEntrada());
									subpieza.getPiezasPadreODT().addAll(pSelected.getPiezasPadreODT());
									remitoSalida.getPiezas().add(subpieza);
								}
								refreshTabla();
							}
						}
					}

				});
			}

			return btnAgregarSubPiezas;
		}

		private void setearNumerosPiezas() {
			Map<PiezaRemito, Integer> piezaPadreMap = new LinkedHashMap<PiezaRemito, Integer>();
			for(PiezaRemito pr : remitoSalida.getPiezas()) {
				PiezaRemito pPadre = pr.getPiezaEntrada();
				if(pPadre != null) {
					Integer cantSubpiezas = piezaPadreMap.get(pPadre);
					if(cantSubpiezas == null) {
						cantSubpiezas = 1;
					} else {
						cantSubpiezas += 1;
					}
					piezaPadreMap.put(pPadre, cantSubpiezas);
				}
			}
			
			int actual = 0;
			int orden = 0;
			for(int fila = 0; fila < getTabla().getRowCount(); fila++) {
				PiezaRemito pr = getElemento(fila);
				orden = pr.getOrdenPieza();
				Integer totalSubpiezas = piezaPadreMap.get(pr.getPiezaEntrada());
				if(totalSubpiezas == null || totalSubpiezas == 1) {
					getTabla().setValueAt(StringUtil.getCadena(extractOrdenes(pr.getPiezasPadreODT()), ", ") , actual, COL_NRO_PIEZA);
					actual++;
				} else {
					for(int cant = 1; cant <= totalSubpiezas; cant ++) {
						if(cant == 1) {
							getTabla().setValueAt(orden+1, actual, COL_NRO_PIEZA);
						} else {
							getTabla().setValueAt((orden+1)+"-"+(cant-1), actual, COL_NRO_PIEZA);
							getTabla().setValueAt(null, actual, COL_METROS_PIEZA_ORIG);
						}
						actual++;
					}
					fila += totalSubpiezas-1;
				}
			}

		}

		private List<Integer> extractOrdenes(List<PiezaODT> piezasPadreODT) {
			List<Integer> ordenList = new ArrayList<Integer>();
			for(PiezaODT podt : piezasPadreODT) {
				if(podt.getPiezaRemito() != null) {
					ordenList.add(podt.getPiezaRemito().getOrdenPieza());
				}
			}
			return ordenList;
		}

	}

	private ParametrosGeneralesFacadeRemote getParametrosGeneralesFacade() {
		if(parametrosGeneralesFacade == null) {
			parametrosGeneralesFacade = GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class);
		}
		return parametrosGeneralesFacade;
	}

	private JTextField getTxtRemitosEntrada() {
		if(txtRemitosEntrada == null) {
			txtRemitosEntrada = new JTextField();
			txtRemitosEntrada.setEditable(false);
		}
		return txtRemitosEntrada;
	}

	public List<RemitoSalida> getRemitosSalida() {
		return remitosSalida;
	}
	
	private void setRemitosSalida(List<RemitoSalida> remitosSalida) {
		this.remitosSalida = remitosSalida;
	}

}
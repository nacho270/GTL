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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.GTLGlobalCache;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.cheque.Banco;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.documentos.factura.CorreccionFactura;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.NotaCredito;
import ar.com.textillevel.entidades.documentos.factura.NotaDebito;
import ar.com.textillevel.entidades.documentos.recibo.Recibo;
import ar.com.textillevel.entidades.documentos.recibo.formapago.FormaPago;
import ar.com.textillevel.entidades.documentos.recibo.formapago.FormaPagoCheque;
import ar.com.textillevel.entidades.documentos.recibo.formapago.FormaPagoEfectivo;
import ar.com.textillevel.entidades.documentos.recibo.formapago.FormaPagoNotaCredito;
import ar.com.textillevel.entidades.documentos.recibo.formapago.FormaPagoRetencionGanancias;
import ar.com.textillevel.entidades.documentos.recibo.formapago.FormaPagoRetencionIVA;
import ar.com.textillevel.entidades.documentos.recibo.formapago.FormaPagoRetencionIngresosBrutos;
import ar.com.textillevel.entidades.documentos.recibo.formapago.FormaPagoTransferencia;
import ar.com.textillevel.entidades.documentos.recibo.formapago.IFormaPagoVisitor;
import ar.com.textillevel.entidades.documentos.recibo.pagos.PagoRecibo;
import ar.com.textillevel.entidades.documentos.recibo.pagos.PagoReciboACuenta;
import ar.com.textillevel.entidades.documentos.recibo.pagos.PagoReciboFactura;
import ar.com.textillevel.entidades.documentos.recibo.pagos.PagoReciboNotaDebito;
import ar.com.textillevel.entidades.documentos.recibo.pagos.visitor.IPagoReciboVisitor;
import ar.com.textillevel.entidades.enums.EDescripcionPagoFactura;
import ar.com.textillevel.entidades.enums.EEstadoCheque;
import ar.com.textillevel.entidades.enums.EEstadoCorreccion;
import ar.com.textillevel.entidades.enums.EEstadoFactura;
import ar.com.textillevel.entidades.enums.EEstadoRecibo;
import ar.com.textillevel.entidades.enums.ETipoCorreccionFactura;
import ar.com.textillevel.facade.api.remote.BancoFacadeRemote;
import ar.com.textillevel.facade.api.remote.ChequeFacadeRemote;
import ar.com.textillevel.facade.api.remote.CorreccionFacadeRemote;
import ar.com.textillevel.facade.api.remote.FacturaFacadeRemote;
import ar.com.textillevel.facade.api.remote.NotaDebitoFacadeRemote;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.facade.api.remote.ReciboFacadeRemote;
import ar.com.textillevel.gui.acciones.impresionrecibo.ImprimirReciboHandler;
import ar.com.textillevel.gui.modulos.cheques.gui.JDialogAgregarCheque;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogCargaRecibo extends JDialog {

	private static final long serialVersionUID = -273160864757961489L;

	private CLJTextField txtRetencionIVA;
	private CLJTextField txtRetencionGanancias;
	private CLJTextField txtRetencionIIBB;
	private CLJTextField txtEfectivo;
	private CLJTextField txtTotalRecibo;

	private CLJTextField txtRazonSocial;
	private CLJTextField txtDomicilio;
	private CLJTextField txtIva;
	private CLJTextField txtCuit;
	private CLJTextField txtCantPesos;
	private CLJTextField txtTotalFacturas;
	private JTextField txtNroRecibo;
	private PanelDatePicker panFecha;

	private JButton btnVerificar;
	private JButton btnGuardar;
	private JButton btnImprimir;
	private JButton btnSalir;

	private JPanel panelSuperior;
	private JPanel panelCliente;
	private JPanel panelCheques;
	private JPanel panelDatosRecibos;

	private Recibo recibo;
	private PanelTablaPagosRecibo panelTablaPagosRecibo;
	private PanelTablaCheque panelTablaCheque;
	private PanelTablaNotaCredito panelTablaNC;

	private FacturaFacadeRemote facturaFacade;
	private ReciboFacadeRemote reciboFacade;
	private NotaDebitoFacadeRemote notaDebitoFacade;
	private CorreccionFacadeRemote correccionFacturaFacade;
	private ParametrosGeneralesFacadeRemote parametrosGeneralesFacade;

	private List<Factura> facturaNoPagadas;
	private List<NotaDebito> notaDebitoPendienteList;
	private List<NotaCredito> notaCreditoList;

	private final Frame owner;
	private final boolean modoConsulta;

	private JPanel panTransferencias;

	private CLJTextField txtImporteTransf;
	private CLJTextField txtNroTransf;
	private CLJTextField txtObservaciones;

	
	public JDialogCargaRecibo(Frame owner, Recibo recibo, boolean modoConsulta) {
		super(owner);
		this.modoConsulta = modoConsulta;
		this.recibo = recibo;
		this.owner = owner;
		setUpScreen();
		setUpComponentes();
		setDatos();
	}

	private void setDatos() {
		getTxtRazonSocial().setText(recibo.getCliente().getRazonSocial());
		getTxtDomicilio().setText(recibo.getCliente().getDireccionReal().getDireccion());
		getTxtObservaciones().setText(recibo.getObservaciones());
		if(recibo.getCliente().getPosicionIva() != null) {
			getTxtIva().setText(recibo.getCliente().getPosicionIva().toString());
		}
		getTxtCuit().setText(recibo.getCliente().getCuit());
		getTxtNroRecibo().setText(recibo.getNroRecibo().toString());

		if (modoConsulta) {
			getPanelTablaPagosRecibo().agregarElementos(recibo.getPagoReciboList());
			getTxtTotalFacturas().setText(getPanelTablaPagosRecibo().getTotalPagoFacturas().toString());
			llenarFormasPagoConsulta(recibo.getPagos());
			actualizarTotalMontoPagado();
			getBtnGuardar().setEnabled(false);
			getTxtCantPesos().setEditable(false);
			getTxtEfectivo().setEditable(false);
			getTxtRetencionGanancias().setEditable(false);
			getTxtRetencionIIBB().setEditable(false);
			getTxtRetencionIVA().setEditable(false);
			getPanelFecha().setEnabled(false);
			getBtnVerificar().setEnabled(false);
			getTxtNroTransf().setEditable(false);
			getTxtImporteTransf().setEditable(false);
			getTxtObservaciones().setEditable(false);
			getTxtCantPesos().setText(recibo.getTxtCantidadPesos());
			getTxtTotalRecibo().setText(recibo.getMonto().toString());
			getPanelFecha().setSelectedDate(recibo.getFecha());
		} else {
			if(isEdicionRecibo()) {
				getPanelTablaPagosRecibo().agregarElementos(recibo.getPagoReciboList());
				llenarFormasPagoConsulta(recibo.getPagos());
				actualizarTotalMontoPagado();
				getTxtCantPesos().setText(recibo.getTxtCantidadPesos());
				getTxtTotalRecibo().setText(recibo.getMonto().toString());
				getPanelFecha().setSelectedDate(recibo.getFecha());
			} else {
				getPanelTablaCheque().gestionarAgregadoCheques();
				getPanelTablaNC().gestionarAgregadosNC();
				actualizarPagosReciboEnTabla();
			}
			
			getBtnImprimir().setVisible(false);
		}
	}

	private void llenarFormasPagoConsulta(List<FormaPago> pagos) {
		IFormaPagoVisitor fpv = new MostrarFormaPagoVisitor(panelTablaCheque, panelTablaNC, getTxtEfectivo(), getTxtRetencionIVA(), getTxtRetencionGanancias(), getTxtRetencionIIBB(), getTxtNroTransf(), getTxtImporteTransf(), getTxtObservaciones());
		for (FormaPago fp : pagos) {
			fp.accept(fpv);
		}
	}

	private void setUpComponentes() {
		setLayout(new GridBagLayout());
		this.add(getPanelSuperior(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1, 0.5, 0.5));
		this.add(getPanelCheques(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1, 0.5, 0.5));
		this.add(getPanelAcciones(), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1, 0, 0));
	}

	private JPanel getPanelAcciones() {
		JPanel panBotones = new JPanel();
		panBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panBotones.add(getBtnVerificar());
		panBotones.add(getBtnGuardar());
		panBotones.add(getBtnImprimir());
		panBotones.add(getBtnSalir());
		return panBotones;
	}

	private void setUpScreen() {
		setSize(850, 740);
		setTitle("Alta de recibo");
		setResizable(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	private CLJTextField getTxtRetencionIVA() {
		if (txtRetencionIVA == null) {
			txtRetencionIVA = new CLJTextField();
			txtRetencionIVA.setPreferredSize(new Dimension(70, 20));
			txtRetencionIVA.addFocusListener(new FocusAdapter() {

				@Override
				public void focusLost(FocusEvent e) {
					actualizarTotalMontoPagado();
				}

			});

		}
		return txtRetencionIVA;
	}

	private CLJTextField getTxtRetencionGanancias() {
		if (txtRetencionGanancias == null) {
			txtRetencionGanancias = new CLJTextField();
			txtRetencionGanancias.setPreferredSize(new Dimension(70, 20));
			txtRetencionGanancias.addFocusListener(new FocusAdapter() {

				@Override
				public void focusLost(FocusEvent e) {
					actualizarTotalMontoPagado();
				}

			});

		}
		return txtRetencionGanancias;
	}

	private CLJTextField getTxtRetencionIIBB() {
		if (txtRetencionIIBB == null) {
			txtRetencionIIBB = new CLJTextField();
			txtRetencionIIBB.setPreferredSize(new Dimension(70, 20));
			txtRetencionIIBB.addFocusListener(new FocusAdapter() {

				@Override
				public void focusLost(FocusEvent e) {
					actualizarTotalMontoPagado();
				}

			});

		}
		return txtRetencionIIBB;
	}

	
	private CLJTextField getTxtEfectivo() {
		if (txtEfectivo == null) {
			txtEfectivo = new CLJTextField();
			txtEfectivo.setPreferredSize(new Dimension(70, 20));
			txtEfectivo.addFocusListener(new FocusAdapter() {

				@Override
				public void focusLost(FocusEvent e) {
					actualizarTotalMontoPagado();
				}

			});
		}
		return txtEfectivo;
	}

	private void actualizarTotalMontoPagado() {
		BigDecimal totalMontoPagado = getTotalMontoPagado();
		setTotalReciboIgnorandoMontoNC(totalMontoPagado);
	}

	private void setTotalReciboIgnorandoMontoNC(BigDecimal totalMontoPagado) {
		totalMontoPagado = totalMontoPagado.subtract(getPanelTablaNC().getTotalMontoNC());
		getTxtTotalRecibo().setText(totalMontoPagado.toString());
		getTxtCantPesos().setText(GenericUtils.convertirNumeroATexto(totalMontoPagado.doubleValue()));
	}

	private BigDecimal getTotalMontoPagado() {
		BigDecimal totalMontoPagado = getPanelTablaCheque().getTotalMontoCheques();
		BigDecimal montoTxtEfectivo = getMontoEfectivoInTextField(getTxtEfectivo());
		totalMontoPagado = totalMontoPagado.add(montoTxtEfectivo == null ? BigDecimal.ZERO : montoTxtEfectivo);
		BigDecimal montoTxtRetencionIVA = getMontoEfectivoInTextField(getTxtRetencionIVA());
		totalMontoPagado = totalMontoPagado.add(montoTxtRetencionIVA == null ? BigDecimal.ZERO : montoTxtRetencionIVA);
		BigDecimal montoTxtRetencionIIBB = getMontoEfectivoInTextField(getTxtRetencionIIBB());
		totalMontoPagado = totalMontoPagado.add(montoTxtRetencionIIBB == null ? BigDecimal.ZERO : montoTxtRetencionIIBB);
		BigDecimal montoTxtRetencionGan = getMontoEfectivoInTextField(getTxtRetencionGanancias());
		totalMontoPagado = totalMontoPagado.add(montoTxtRetencionGan == null ? BigDecimal.ZERO : montoTxtRetencionGan);
		BigDecimal montoTxtTransf = getMontoEfectivoInTextField(getTxtImporteTransf());
		totalMontoPagado = totalMontoPagado.add(montoTxtTransf == null ? BigDecimal.ZERO : montoTxtTransf);
		if(!modoConsulta) {
			totalMontoPagado = totalMontoPagado.add(getPanelTablaNC().getTotalMontoNC());
		}
		return totalMontoPagado;
	}

	public BigDecimal getAllTotalMontoNCNoAsociadas() {
		BigDecimal total = new BigDecimal(0);
		for(NotaCredito nc : getNotaCreditoList()) {
			total = total.add(nc.getMontoSobrante());
		}
		return total;
	}
	
	private CLJTextField getTxtRazonSocial() {
		if (txtRazonSocial == null) {
			txtRazonSocial = new CLJTextField();
			txtRazonSocial.setPreferredSize(new Dimension(150, 20));
			txtRazonSocial.setEditable(false);
		}
		return txtRazonSocial;
	}

	private CLJTextField getTxtDomicilio() {
		if (txtDomicilio == null) {
			txtDomicilio = new CLJTextField();
			txtDomicilio.setPreferredSize(new Dimension(150, 20));
			txtDomicilio.setEditable(false);
		}
		return txtDomicilio;
	}

	private CLJTextField getTxtIva() {
		if (txtIva == null) {
			txtIva = new CLJTextField();
			txtIva.setPreferredSize(new Dimension(150, 20));
			txtIva.setEditable(false);
		}
		return txtIva;
	}

	private CLJTextField getTxtCuit() {
		if (txtCuit == null) {
			txtCuit = new CLJTextField();
			txtCuit.setPreferredSize(new Dimension(150, 20));
			txtCuit.setEditable(false);
		}
		return txtCuit;
	}

	private CLJTextField getTxtCantPesos() {
		if (txtCantPesos == null) {
			txtCantPesos = new CLJTextField();
			txtCantPesos.setPreferredSize(new Dimension(150, 20));
		}
		return txtCantPesos;
	}

	private CLJTextField getTxtTotalFacturas() {
		if (txtTotalFacturas == null) {
			txtTotalFacturas = new CLJTextField();
			txtTotalFacturas.setPreferredSize(new Dimension(150, 20));
			txtTotalFacturas.setEditable(false);
		}
		return txtTotalFacturas;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (validar()) {
						capturarSetearDatos();
						try {
							getBtnGuardar().setEnabled(false);
							getReciboFacade().ingresarRecibo(recibo, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
							CLJOptionPane.showInformationMessage(JDialogCargaRecibo.this, "El recibo se ha grabado con éxito.", "Información");
							if(CLJOptionPane.showQuestionMessage(JDialogCargaRecibo.this, "¿Desea imprimir el Recibo?", "Confirmación") == CLJOptionPane.YES_OPTION) {
								imprimirRecibo();
							}
							dispose();
						} catch (ValidacionException e1) {
							CLJOptionPane.showErrorMessage(JDialogCargaRecibo.this, StringW.wordWrap(e1.getMensajeError()), "Atención");
						}
					}
				}

			});
		}
		return btnGuardar;
	}

	private boolean isEdicionRecibo() {
		return recibo.getId() != null;
	}
	
	private void capturarSetearDatos() {
		//limpio las colecciones anteriores
		recibo.getPagoReciboList().clear();
		recibo.getPagos().clear();

		// Obtengo las formas de pago
		List<FormaPago> formaPagoList = getFormasPago();
		BigDecimal montoTotal = new BigDecimal(0);
		for (FormaPago fp : formaPagoList) {
			if(!(fp instanceof FormaPagoNotaCredito)) {
				montoTotal = montoTotal.add(fp.getImporte());
			}
		}

		BigDecimal montoTotalToSet = new BigDecimal(montoTotal.doubleValue());
		// Obtengo los pagos recibo
		List<PagoRecibo> pagoFacturaList = getPanelTablaPagosRecibo().capturarSetearDatos();
		//seteo los datos en el recibo y guardo
		recibo.getPagoReciboList().addAll(pagoFacturaList);
		recibo.getPagos().addAll(formaPagoList);
		recibo.setMonto(montoTotalToSet);
		recibo.setTxtCantidadPesos(getTxtCantPesos().getText().trim());
		long longFecha = 0;
		if(GenericUtils.esHoy(new java.sql.Date(getPanelFecha().getDate().getTime()))){//hoy
			longFecha = DateUtil.getAhora().getTime();
		}else{
			longFecha=getPanelFecha().getDate().getTime();
		}
		recibo.setFecha(new Date(longFecha));
		recibo.setEstadoRecibo(EEstadoRecibo.PENDIENTE);
		recibo.setObservaciones(getTxtObservaciones().getText().trim());
	}

	private List<FormaPago> getFormasPago() {
		List<FormaPago> formaPagoList = new ArrayList<FormaPago>();
		// Cargo los cheques
		List<Cheque> chequeList = getPanelTablaCheque().capturarSetearDatos();
		for (Cheque cheque : chequeList) {
			FormaPagoCheque fpc = new FormaPagoCheque();
			fpc.setCheque(cheque);
			cheque.setEstadoCheque(EEstadoCheque.EN_CARTERA);
			formaPagoList.add(fpc);
		}
		// Cargo el efectivo
		if (!StringUtil.isNullOrEmpty(getTxtEfectivo().getText().trim())) {
			BigDecimal efectivo = getMontoEfectivoInTextField(getTxtEfectivo());
			FormaPagoEfectivo fpe = new FormaPagoEfectivo();
			fpe.setImportePagoSimple(efectivo);
			formaPagoList.add(fpe);
		}
		// Retención de iva
		if (!StringUtil.isNullOrEmpty(getTxtRetencionIVA().getText().trim())) {
			BigDecimal montoRetIVA = getMontoEfectivoInTextField(getTxtRetencionIVA());
			FormaPagoRetencionIVA fpri = new FormaPagoRetencionIVA();
			fpri.setImportePagoSimple(montoRetIVA);
			formaPagoList.add(fpri);
		}
		// Retención ingresos brutos
		if (!StringUtil.isNullOrEmpty(getTxtRetencionIIBB().getText().trim())) {
			BigDecimal montoRetIngBrutos = getMontoEfectivoInTextField(getTxtRetencionIIBB());
			FormaPagoRetencionIngresosBrutos fpib = new FormaPagoRetencionIngresosBrutos();
			fpib.setImportePagoSimple(montoRetIngBrutos);
			formaPagoList.add(fpib);
		}
		// Retención ganancias
		if (!StringUtil.isNullOrEmpty(getTxtRetencionGanancias().getText().trim())) {
			BigDecimal montoRetGanancias = getMontoEfectivoInTextField(getTxtRetencionGanancias());
			FormaPagoRetencionGanancias fprgan = new FormaPagoRetencionGanancias();
			fprgan.setImportePagoSimple(montoRetGanancias);
			formaPagoList.add(fprgan);
		}
		//Importe de Transferencias
		if (!StringUtil.isNullOrEmpty(getTxtImporteTransf().getText().trim())) {
			BigDecimal montoRetIngBrutos = getMontoEfectivoInTextField(getTxtImporteTransf());
			FormaPagoTransferencia fptx = new FormaPagoTransferencia();
			fptx.setImportePagoSimple(montoRetIngBrutos);
			fptx.setNroTx(Double.valueOf(getTxtNroTransf().getText().trim()));
			fptx.setObservaciones(getTxtObservaciones().getText().trim());
			formaPagoList.add(fptx);
		}
		//Cargar las formas de pago asociados a las notas de credito
		List<NotaCredito> ncList = getPanelTablaNC().capturarSetearDatos();
		for(NotaCredito nc : ncList) {
			FormaPagoNotaCredito fpnc = new FormaPagoNotaCredito();
			fpnc.setNotaCredito(nc);
			fpnc.setImporteNC(nc.getMontoSobrante());
			nc.setMontoSobrante(new BigDecimal(0));
			formaPagoList.add(fpnc);
		}
		return formaPagoList;
	}

	private BigDecimal getMontoEfectivoInTextField(CLJTextField txt) {
		String strValue = txt.getText().trim();
		if (StringUtil.isNullOrEmpty(strValue) || !GenericUtils.esNumerico(strValue)) {
			return null;
		} else {
			return new BigDecimal(strValue.replace(',', '.'));
		}
	}

	private boolean validar() {
		String msgPanelFactura = getPanelTablaPagosRecibo().validar();
		if (!StringUtil.isNullOrEmpty(msgPanelFactura)) {
			CLJOptionPane.showErrorMessage(JDialogCargaRecibo.this, StringW.wordWrap(msgPanelFactura), "Error");
			return false;
		}
		String efectivo = getTxtEfectivo().getText().trim();
		if (!StringUtil.isNullOrEmpty(efectivo) && !GenericUtils.esNumerico(efectivo)) {
			CLJOptionPane.showErrorMessage(JDialogCargaRecibo.this, StringW.wordWrap("El valor del efectivo debe ser numérico y mayor que cero."), "Error");
			getTxtEfectivo().requestFocus();
			return false;
		}
		String iva = getTxtRetencionIVA().getText().trim();
		if (!StringUtil.isNullOrEmpty(iva) && !GenericUtils.esNumerico(iva)) {
			CLJOptionPane.showErrorMessage(JDialogCargaRecibo.this, StringW.wordWrap("El valor del IVA debe ser numérico y mayor que cero."), "Error");
			getTxtRetencionIVA().requestFocus();
			return false;
		}
		String retencionGan = getTxtRetencionGanancias().getText().trim();
		if (!StringUtil.isNullOrEmpty(retencionGan) && !GenericUtils.esNumerico(retencionGan)) {
			CLJOptionPane.showErrorMessage(JDialogCargaRecibo.this, StringW.wordWrap("El valor de las retenciones de ganancias debe ser numérico y mayor que cero."), "Error");
			getTxtRetencionGanancias().requestFocus();
			return false;
		}
		String retencionIIBB = getTxtRetencionIIBB().getText().trim();
		if (!StringUtil.isNullOrEmpty(retencionIIBB) && !GenericUtils.esNumerico(retencionIIBB)) {
			CLJOptionPane.showErrorMessage(JDialogCargaRecibo.this, StringW.wordWrap("El valor de las retenciones de ingresos brutos ser numérico y mayor que cero."), "Error");
			getTxtRetencionIIBB().requestFocus();
			return false;
		}
		if (StringUtil.isNullOrEmpty(getTxtCantPesos().getText())) {
			CLJOptionPane.showErrorMessage(JDialogCargaRecibo.this, StringW.wordWrap("Debe ingresar la cantidad de pesos."), "Error");
			getTxtCantPesos().requestFocus();
			return false;
		}
		BigDecimal totalMontoPagado = getTotalMontoPagado();
		if (totalMontoPagado.compareTo(new BigDecimal(0)) == 0) {
			CLJOptionPane.showErrorMessage(JDialogCargaRecibo.this, StringW.wordWrap("Debe ingresar cheques, efectivo o retenciones."), "Error");
			return false;
		}
		if(totalMontoPagado.compareTo(getPanelTablaPagosRecibo().getTotalPagoFacturas()) != 0) {
			CLJOptionPane.showErrorMessage(JDialogCargaRecibo.this, StringW.wordWrap("El monto total ingresado y el que hay que pagar no conciden. Por favor, presione el botón 'Verificar' o revise el monto ingresado."), "Error");
			return false;
		}
		java.util.Date date = getPanelFecha().getDate();
		if(date == null) {
			CLJOptionPane.showErrorMessage(JDialogCargaRecibo.this, StringW.wordWrap("Debe ingresar una fecha con el formato dd/mm/aaaa"), "Error");
			getPanelFecha().requestFocus();
			return false;
		}
//		Date fechaRecibo = new Date(getPanelFecha().getDate().getTime());
//		Date ultimaFechaReciboGrabado = getReciboFacade().getUltimaFechaReciboGrabado();
//		if(ultimaFechaReciboGrabado != null && !fechaRecibo.before(ultimaFechaReciboGrabado)) { 
//			CLJOptionPane.showErrorMessage(JDialogCargaRecibo.this, StringW.wordWrap("La fecha del recibo es menor a una fecha ya existente '" + DateUtil.dateToString(ultimaFechaReciboGrabado) + "'."), "Error");
//			getPanelFecha().requestFocus();
//			return false;
//		}
		
		String txtImporteTx = getTxtImporteTransf().getText().trim();
		
		if(!StringUtil.isNullOrEmpty(txtImporteTx)) {
			if(!GenericUtils.esNumerico(txtImporteTx)) {
				CLJOptionPane.showErrorMessage(JDialogCargaRecibo.this, StringW.wordWrap("El importe de la transferencia bancaria debe ser numérico."), "Error");
				getTxtImporteTransf().requestFocus();
				return false;
			}
			String txtNroTx = getTxtNroTransf().getText().trim();
			if(StringUtil.isNullOrEmpty(txtNroTx)) {
				CLJOptionPane.showErrorMessage(JDialogCargaRecibo.this, StringW.wordWrap("Debe ingresar el número de la transferencia bancaria."), "Error");
				getTxtNroTransf().requestFocus();
				return false;
			}
			if(!GenericUtils.esNumerico(txtNroTx)) {
				CLJOptionPane.showErrorMessage(JDialogCargaRecibo.this, StringW.wordWrap("El número de la transferencia bancaria debe ser numérico."), "Error");
				getTxtNroTransf().requestFocus();
				return false;
			}
		}
		
		return true;
	}

	private JButton getBtnImprimir() {
		if (btnImprimir == null) {
			btnImprimir = new JButton("Imprimir");
			btnImprimir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					imprimirRecibo();
				}

			});
		}
		return btnImprimir;
	}

	private void imprimirRecibo() {
		ImprimirReciboHandler imprimirReciboHandler = new ImprimirReciboHandler(recibo, getParametrosGeneralesFacade().getParametrosGenerales().getNroSucursal(), JDialogCargaRecibo.this);
		imprimirReciboHandler.imprimir();
	}

	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Salir");
			btnSalir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (modoConsulta) {
						dispose();
						return;
					}
					if (CLJOptionPane.showQuestionMessage(JDialogCargaRecibo.this, "¿Está seguro que desea salir?", "Confirmación") == CLJOptionPane.YES_OPTION) {
						List<Cheque> cheques = getPanelTablaCheque().getElementos();
						if(!cheques.isEmpty() && recibo.getId() == null) {
							if (CLJOptionPane.showQuestionMessage(JDialogCargaRecibo.this, "¿Desea borrar los cheques agregados?", "Confirmación") == CLJOptionPane.YES_OPTION) {
								ChequeFacadeRemote cfr = GTLBeanFactory.getInstance().getBean2(ChequeFacadeRemote.class);
								try {
									cfr.eliminarCheques(cheques, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
								} catch (ValidacionException e1) {
									CLJOptionPane.showErrorMessage(JDialogCargaRecibo.this, e1.getMensajeError(), "Error");
								}
							}
						}
						recibo = null;
						dispose();
					}
				}

			});
		}
		return btnSalir;
	}

	public static void main(String[] args) {
		new JDialogCargaRecibo(new Frame(), new Recibo(), false).setVisible(true);
	}

	private JPanel getPanelSuperior() {
		if (panelSuperior == null) {
			panelSuperior = new JPanel();
			panelSuperior.setLayout(new GridBagLayout());
			GridBagConstraints gc = GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(20, 10, 20, 10), 1, 1, 0.7, 0.5);
			panelSuperior.add(getPanelTablaPagosRecibo(), gc);

			gc = GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 0, 0);
			panelSuperior.add(getPanTransferencias(), gc);

			gc = GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 1, 1, 0.3, 0.4);
			panelSuperior.add(getPanelCliente(), gc);
			gc = GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 1, 1, 0, 0);
			panelSuperior.add(getPanelDatosRecibo(), gc);
		}
		return panelSuperior;
	}

	
	private JPanel getPanTransferencias() {
		if(panTransferencias == null) {
			panTransferencias = new JPanel();
			panTransferencias.setLayout(new GridBagLayout());
			GridBagConstraints gc = GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 0, 0);
			panTransferencias.add(new JLabel("Total Facturas: "), gc);
			gc = GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 3, 1, 1, 0);
			panTransferencias.add(getTxtTotalFacturas(), gc);
			
			gc = GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 4, 1, 0, 0);
			panTransferencias.add(getPanelTablaNC(), gc);

			gc = GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 0, 0);
			panTransferencias.add(new JLabel("Importe Transf."), gc);
			gc = GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 0.3, 0);
			panTransferencias.add(getTxtImporteTransf(), gc);
			gc = GenericUtils.createGridBagConstraints(2, 2, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 0, 0);
			panTransferencias.add(new JLabel("Nro. Transf."), gc);
			gc = GenericUtils.createGridBagConstraints(3, 2, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 0.3, 0);
			panTransferencias.add(getTxtNroTransf(), gc);
			gc = GenericUtils.createGridBagConstraints(0, 3, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 0, 0);
			panTransferencias.add(new JLabel("Observaciones: "), gc);
			gc = GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 3, 1, 0, 0);
			panTransferencias.add(getTxtObservaciones(), gc);
		}
		return panTransferencias;
	}

	private CLJTextField getTxtObservaciones() {
		if(txtObservaciones == null) {
			txtObservaciones = new CLJTextField();
		}
		return txtObservaciones;
	}
	
	private CLJTextField getTxtImporteTransf() {
		if(txtImporteTransf == null) {
			txtImporteTransf = new CLJTextField();
			txtImporteTransf.addFocusListener(new FocusAdapter() {

				@Override
				public void focusLost(FocusEvent e) {
					actualizarTotalMontoPagado();
				}

			});

		}
		return txtImporteTransf;
	}

	private CLJTextField getTxtNroTransf() {
		if(txtNroTransf == null) {
			txtNroTransf = new CLJTextField();
		}
		return txtNroTransf;
	}

	private JPanel getPanelDatosRecibo() {
		if(panelDatosRecibos == null) {
			panelDatosRecibos = new JPanel(new GridBagLayout());
			panelDatosRecibos.add(new JLabel("Nro. de Recibo: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 1, 1, 0, 0));
			panelDatosRecibos.add(getTxtNroRecibo(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 1, 1, 1, 0));
			panelDatosRecibos.add(new JLabel("Fecha: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 1, 1, 0, 0));
			panelDatosRecibos.add(getPanelFecha(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 1, 1, 1, 0));
			panelDatosRecibos.setBorder(BorderFactory.createTitledBorder("Datos del Recibo"));
		}
		return panelDatosRecibos;
	}

	private PanelDatePicker getPanelFecha() {
		if(panFecha == null) {
			panFecha = new PanelDatePicker();
		}
		return panFecha;
	}
	
	private JTextField getTxtNroRecibo() {
		if(txtNroRecibo == null) {
			txtNroRecibo = new JTextField();
			txtNroRecibo.setEditable(false);
		}
		return txtNroRecibo;
	}

	private PanelTablaPagosRecibo getPanelTablaPagosRecibo() {
		if (panelTablaPagosRecibo == null) {
			panelTablaPagosRecibo = new PanelTablaPagosRecibo();
			panelTablaPagosRecibo.setBorder(BorderFactory.createTitledBorder("Facturas"));
			panelTablaPagosRecibo.setModoConsulta(modoConsulta);
		}
		return panelTablaPagosRecibo;
	}

	private PanelTablaCheque getPanelTablaCheque() {
		if (panelTablaCheque == null) {
			panelTablaCheque = new PanelTablaCheque(recibo);
			panelTablaCheque.setBorder(BorderFactory.createTitledBorder("Cheques"));
			panelTablaCheque.setModoConsulta(modoConsulta);
		}
		return panelTablaCheque;
	}

	private PanelTablaNotaCredito getPanelTablaNC() {
		if(panelTablaNC == null) {
			panelTablaNC = new PanelTablaNotaCredito();
			panelTablaNC.setBorder(BorderFactory.createTitledBorder("Notas de Crédito"));
			panelTablaNC.setModoConsulta(modoConsulta);
		}
		return panelTablaNC;
	}

	private JPanel getPanelCliente() {
		if (panelCliente == null) {
			panelCliente = new JPanel();
			panelCliente.setLayout(new GridBagLayout());
			panelCliente.setBorder(BorderFactory.createTitledBorder("Datos del cliente"));
			panelCliente.add(new JLabel("CLIENTE:"), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelCliente.add(getTxtRazonSocial(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelCliente.add(new JLabel("DOMICILIO: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelCliente.add(getTxtDomicilio(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelCliente.add(new JLabel("I.V.A: "), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelCliente.add(getTxtIva(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 10), 1, 1, 1, 0));
			panelCliente.add(new JLabel("C.U.I.T: "), GenericUtils.createGridBagConstraints(0, 3, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelCliente.add(getTxtCuit(), GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 10), 1, 1, 1, 0));
			panelCliente.add(new JLabel("Cantidad de pesos: "), GenericUtils.createGridBagConstraints(0, 4, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelCliente.add(getTxtCantPesos(), GenericUtils.createGridBagConstraints(1, 4, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 10), 1, 1, 1, 0));
		}
		return panelCliente;
	}

	private JPanel getPanelCheques() {
		if (panelCheques == null) {
			panelCheques = new JPanel();
			panelCheques.setLayout(new GridBagLayout());
			GridBagConstraints gc = GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(20, 10, 20, 10), 1, 1, 0.7, 0.5);
			panelCheques.add(getPanelTablaCheque(), gc);
			gc = GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(20, 10, 20, 10), 1, 1, 0.3, 0.5);
			panelCheques.add(getPanelTotales(), gc);
		}
		return panelCheques;
	}

	private JPanel getPanelTotales() {
		JPanel pnlTotales = new JPanel();
		pnlTotales.setLayout(new GridBagLayout());
		pnlTotales.setBorder(BorderFactory.createTitledBorder("Totales"));
		pnlTotales.add(new JLabel("RETENCION IVA: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 10, 5, 2), 1, 1, 1, 0));
		pnlTotales.add(getTxtRetencionIVA(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 10, 5, 2), 1, 1, 0, 0));
		pnlTotales.add(new JLabel("RETENCION INGRESOS BRUTOS: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 10, 5, 2), 1, 1, 0, 0));
		pnlTotales.add(getTxtRetencionIIBB(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 10, 5, 2), 1, 1, 0, 0));
		pnlTotales.add(new JLabel("RETENCION GANANCIAS: "), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 10, 5, 2), 1, 1, 0, 0));
		pnlTotales.add(getTxtRetencionGanancias(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 10, 5, 2), 1, 1, 0, 0));
		pnlTotales.add(new JLabel("EFECTIVO: "), GenericUtils.createGridBagConstraints(0, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 10, 5, 2), 1, 1, 0, 0));
		pnlTotales.add(getTxtEfectivo(), GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 10, 5, 2), 1, 1, 0, 0));
		pnlTotales.add(new JLabel("TOTAL: "), GenericUtils.createGridBagConstraints(0, 4, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 2), 1, 1, 0, 0));
		pnlTotales.add(getTxtTotalRecibo(), GenericUtils.createGridBagConstraints(1, 4, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 10, 5, 2), 1, 1, 0, 0));
		return pnlTotales;
	}

	private CLJTextField getTxtTotalRecibo() {
		if (txtTotalRecibo == null) {
			txtTotalRecibo = new CLJTextField();
			txtTotalRecibo.setPreferredSize(new Dimension(70, 20));
			txtTotalRecibo.setEditable(false);
		}
		return txtTotalRecibo;
	}

	private class PanelTablaPagosRecibo extends PanelTabla<PagoRecibo> {

		private static final long serialVersionUID = 1L;

		public static final int CANT_COLS_TBL_PAGOS_RECIBO = 4;
		public static final int COL_FECHA = 0;
		public static final int COL_CONCEPTO_PAGO_RECIBO = 1;
		public static final int COL_IMPORTE_PAGO_RECIBO = 2;
		public static final int COL_OBJ_PAGO_RECIBO = 3;


		public PanelTablaPagosRecibo() {
			getBotonAgregar().setVisible(false);
			getBotonEliminar().setVisible(false);
		}

		public String validar() {
			if (getTabla().getRowCount() == 0) {
				return "No se visualizan Facturas/Notas de débito por pagar. Por favor, presione el botón 'Verificar'.";
			}
			return null;
		}

		public List<PagoRecibo> capturarSetearDatos() {
			List<PagoRecibo> elementos = getElementos();
			ActualizarItemPagoReciboVisitor aiprv = new ActualizarItemPagoReciboVisitor();
			//Actualizo los valores de los montos de los items de los pagos de recibo
			for(PagoRecibo pr : elementos) {
				pr.accept(aiprv);
			}
			return elementos;
		}

		@Override
		protected void agregarElemento(PagoRecibo elemento) {
			MostrarPagoReciboVisitor mprv = new MostrarPagoReciboVisitor(this);
			elemento.accept(mprv);
		}

		public BigDecimal getTotalPagoFacturas() {
			BigDecimal total = new BigDecimal(0);
			for (int i = 0; i < getTabla().getRowCount(); i++) {
				BigDecimal importe = (BigDecimal) getTabla().getValueAt(i, COL_IMPORTE_PAGO_RECIBO);
				if (importe != null) {
					total = total.add(importe);
				}
			}
			return total;
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tablaFacturas = new CLJTable(0, CANT_COLS_TBL_PAGOS_RECIBO);
			tablaFacturas.setStringColumn(COL_FECHA, "Fecha", 230, 100, true);
			tablaFacturas.setStringColumn(COL_CONCEPTO_PAGO_RECIBO, "Concepto", 200, 200, true);
			tablaFacturas.setFloatColumn(COL_IMPORTE_PAGO_RECIBO, "Importe Pagado", 0, Float.MAX_VALUE, 100, true);
			tablaFacturas.setStringColumn(COL_OBJ_PAGO_RECIBO, "", 0, 0, true);

			tablaFacturas.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						int selectedRow = getTabla().getSelectedRow();
						if (selectedRow != -1) {
							ConsultaItemPagoReciboVisitor ciprv = new ConsultaItemPagoReciboVisitor(); 
							PagoRecibo prf = (PagoRecibo) getTabla().getValueAt(selectedRow, COL_OBJ_PAGO_RECIBO);
							prf.accept(ciprv);
						}
					}
				}

			});

			return tablaFacturas;
		}

		@Override
		protected void botonQuitarPresionado() {
			getTxtTotalFacturas().setText(getPanelTablaPagosRecibo().getTotalPagoFacturas().toString());
		}

		@Override
		protected PagoRecibo getElemento(int fila) {
			return (PagoRecibo) getTabla().getValueAt(fila, COL_OBJ_PAGO_RECIBO);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

	}

	private class PanelTablaNotaCredito extends PanelTabla<NotaCredito> {

		private static final long serialVersionUID = 1L;

		private static final int CANT_COLS_TBL_NC = 4;
		private static final int COL_FECHA_NC = 0;
		private static final int COL_NC = 1;
		private static final int COL_IMPORTE = 2;
		private static final int COL_OBJ = 3;

		public PanelTablaNotaCredito() {
			getBotonAgregar().setVisible(false);
		}

		public List<NotaCredito> capturarSetearDatos() {
			return getElementos();
		}

		public BigDecimal getTotalMontoNC() {
			BigDecimal total = new BigDecimal(0);
			for(NotaCredito nc : getElementos()) {
				total = total.add(nc.getMontoSobrante());
			}
			return total;
		}

		public void gestionarAgregadosNC() {
			List<NotaCredito> ncList = getNotaCreditoList();
			if(!ncList.isEmpty()) {
				if(CLJOptionPane.showQuestionMessage(JDialogCargaRecibo.this, StringW.wordWrap("El cliente tiene notas de créditos disponibles ¿Desea agregarlas?"), "Confirmación") == CLJOptionPane.YES_OPTION) {
					agregarElementos(ncList);
				}
			} 
		}

		@Override
		protected void agregarElemento(NotaCredito elemento) {
			Object[] row = new Object[CANT_COLS_TBL_NC];
			row[COL_NC] = "Nota de Crédito: " + elemento.getNroFactura();
			row[COL_FECHA_NC] = DateUtil.dateToString(elemento.getFechaEmision());
			row[COL_IMPORTE] = elemento.getMontoSobrante();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tablaNC = new CLJTable(0, CANT_COLS_TBL_NC);
			tablaNC.setStringColumn(COL_FECHA_NC, "Fecha", 230, 100, true);
			tablaNC.setStringColumn(COL_NC, "Concepto", 150, 150, true);
			tablaNC.setFloatColumn(COL_IMPORTE, "Importe", 80, true);
			tablaNC.setStringColumn(COL_OBJ, "", 0, 0, true);
			tablaNC.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						int selectedRow = getTabla().getSelectedRow();
						if (selectedRow != -1) {
							NotaCredito nc = (NotaCredito)getTabla().getValueAt(selectedRow, COL_OBJ);
							JDialogCargaFactura jDialogCargaFactura = new JDialogCargaFactura(owner, getCorreccionFacturaFacade().getCorreccionByNumero(nc.getNroFactura(), ETipoCorreccionFactura.NOTA_CREDITO), true);
							jDialogCargaFactura.setVisible(true);
						}
					}
				}

			});
			
			return tablaNC;
		}

		@Override
		protected NotaCredito getElemento(int fila) {
			return (NotaCredito)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		protected void botonQuitarPresionado() {
			getBtnVerificar().doClick();
		}

	}
	
	private class PanelTablaCheque extends PanelTabla<Cheque> {

		private static final long serialVersionUID = 1L;

		private static final int CANT_COLS_TBL_CHEQUE = 6;
		private static final int COL_BANCO = 0;
		private static final int COL_NRO = 1;
		private static final int COL_NRO_INTERNO = 2;
		private static final int COL_CUIT = 3;
		private static final int COL_IMPORTE = 4;
		private static final int COL_OBJ = 5;

		private ChequeFacadeRemote chequeFacade;
		private BancoFacadeRemote bancoFacade;
		private List<Banco> bancoList;
		private final Recibo recibo;
		private JComboBox cmbBanco;

		public PanelTablaCheque(Recibo recibo) {
			this.recibo = recibo;
			getBotonEliminar().setVisible(false);
		}

		public boolean gestionarAgregadoCheques() {
			List<Cheque> chequesDisponibles = getChequesDisponibles();
			if(!chequesDisponibles.isEmpty()) {
				if(CLJOptionPane.showQuestionMessage(JDialogCargaRecibo.this, StringW.wordWrap("El cliente tiene cheques disponibles ¿Desea agregarlos?"), "Confirmación") == CLJOptionPane.YES_OPTION) {
					for(Cheque cheque : chequesDisponibles) {
						handleSeleccionCheque(cheque);
					}
				}
			} 
			return false;
		}

		private List<Banco> getBancoList() {
			if (bancoList == null) {
				bancoList = getBancoFacade().getAllOrderByName();
			}
			return bancoList;
		}

		private JComboBox getCmbBanco() {
			if (cmbBanco == null) {
				cmbBanco = new JComboBox();
				GuiUtil.llenarCombo(cmbBanco, getBancoList(), false);
			}
			return cmbBanco;
		}

		public BigDecimal getTotalMontoCheques() {
			BigDecimal montoTotal = new BigDecimal(0);
			for (Cheque ch : getElementos()) {
				montoTotal = montoTotal.add(ch.getImporte());
			}
			return montoTotal;
		}

		@Override
		public boolean validarAgregar() {
			JDialogAgregarCheque dialogAgregarCheque = new JDialogAgregarCheque(owner, recibo.getCliente(), true);
			boolean acepto = dialogAgregarCheque.isAcepto();
			if(acepto){
				ChequeFacadeRemote cfr = GTLBeanFactory.getInstance().getBean2(ChequeFacadeRemote.class);
				try{
					Cheque cheque = dialogAgregarCheque.getCheque();
					cheque = cfr.grabarCheque(cheque, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
					handleSeleccionCheque(cheque);
					boolean agregaOtro = dialogAgregarCheque.isAgregaOtro();
					if(agregaOtro){
						do{
							dialogAgregarCheque = new JDialogAgregarCheque(owner,cloneCheque(cheque),false,true, true);
							cheque = dialogAgregarCheque.getCheque();
							acepto = dialogAgregarCheque.isAcepto();
							agregaOtro = dialogAgregarCheque.isAgregaOtro();
							if(acepto){
								cheque = cfr.grabarCheque(cheque, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
								handleSeleccionCheque(cheque);
							}
						}while(agregaOtro && acepto);
					}
					return false;
				}catch(CLException cle){
					BossError.gestionarError(cle);
					return false;
				}
			}
			return false;
		}

		private Cheque cloneCheque(Cheque cheque) {
			Cheque newCheque = new Cheque();
			newCheque.setBanco(cheque.getBanco());
			newCheque.setBancoSalida(cheque.getBancoSalida());
			newCheque.setCapitalOInterior(cheque.getCapitalOInterior());
			newCheque.setCliente(cheque.getCliente());
			newCheque.setClienteSalida(cheque.getClienteSalida());
			newCheque.setCuit(cheque.getCuit());
			newCheque.setEstadoCheque(cheque.getEstadoCheque());
			newCheque.setFechaDeposito(cheque.getFechaDeposito());
			newCheque.setFechaEntrada(cheque.getFechaEntrada());
			newCheque.setFechaSalida(cheque.getFechaSalida());
			newCheque.setImporte(cheque.getImporte());
			newCheque.setNombreProveedorSalida(cheque.getNombreProveedorSalida());
			newCheque.setNumeracion(cheque.getNumeracion());
			newCheque.setNumero(cheque.getNumero());
			newCheque.setPersonaSalida(cheque.getPersonaSalida());
			newCheque.setProveedorSalida(cheque.getProveedorSalida());
			return newCheque;
		}

		private List<Cheque> getChequesDisponibles() {
			List<Cheque> chequesByClientePendientesCobrar = getChequeFacade().getChequesByCliente(recibo.getCliente().getId(), EEstadoCheque.PENDIENTE_COBRAR);
			for (int i = 0; i < getTabla().getRowCount(); i++) {
				chequesByClientePendientesCobrar.remove(getElemento(i));
			}
			return chequesByClientePendientesCobrar;
		}

		private void handleSeleccionCheque(Cheque cheque) {
			agregarElemento(cheque);
			actualizarTotalMontoPagado();
		}

		private void updateCheque(Cheque c) {
			int rowCheque = -1;
			for(int i = 0; i < getTabla().getRowCount(); i++) {
				Cheque elemento = getElemento(i);
				if(elemento.equals(c)) {
					rowCheque  = i;
					break;
				}
			}
			if(rowCheque != -1) {
				getTabla().setValueAt(c.getBanco().getNombre(), rowCheque, COL_BANCO);
				getTabla().setValueAt(c.getCuit(), rowCheque, COL_CUIT);
				getTabla().setValueAt(c.getImporte().toString(), rowCheque, COL_IMPORTE);
				getTabla().setValueAt(c.getNumero(), rowCheque, COL_NRO);
				getTabla().setValueAt(c.getNumeracion().toString(), rowCheque, COL_NRO_INTERNO);
				getTabla().setValueAt(c, rowCheque, COL_OBJ);

			}
			actualizarTotalMontoPagado();
		}

		@Override
		protected void botonQuitarPresionado() {
			actualizarTotalMontoPagado();
		}

		@Override
		protected void agregarElemento(Cheque c) {
			Object[] row = new Object[CANT_COLS_TBL_CHEQUE];
			row[COL_BANCO] = c.getBanco().getNombre();
			row[COL_CUIT] = c.getCuit();
			row[COL_IMPORTE] = c.getImporte().toString();
			row[COL_NRO] = c.getNumero();
			row[COL_NRO_INTERNO] = c.getNumeracion().toString();
			row[COL_OBJ] = c;
			getTabla().addRow(row);
		}

		public void agregarCheque(Cheque c) {
			agregarElemento(c);
		}

		public List<Cheque> capturarSetearDatos() {
			return getElementos();
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tablaCheques = new CLJTable(0, CANT_COLS_TBL_CHEQUE);
			tablaCheques.setComboColumn(COL_BANCO, "Banco", getCmbBanco(), 100, true);
			tablaCheques.setStringColumn(COL_NRO, "Nº", 50, 100, true);
			tablaCheques.setStringColumn(COL_NRO_INTERNO, "Nº Interno", 60, 60, true);
			tablaCheques.setStringColumn(COL_CUIT, "C.U.I.T", 80, 80, true);
			tablaCheques.setFloatColumn(COL_IMPORTE, "Importe", 80, true);
			tablaCheques.setStringColumn(COL_OBJ, "", 0, 0, true);

			tablaCheques.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						int selectedRow = getTabla().getSelectedRow();
						if(isModoConsulta()) {
							Cheque cheque = (Cheque) getTabla().getValueAt(selectedRow, COL_OBJ);
							new JDialogAgregarCheque(owner, cheque, true,false);
						} else {
							if (selectedRow != -1) {
								Cheque cheque = (Cheque) getTabla().getValueAt(selectedRow, COL_OBJ);
								JDialogAgregarCheque dialogo = new JDialogAgregarCheque(owner, cheque, false,false, true);
								if(dialogo.isAcepto()) {
									ChequeFacadeRemote cfr = GTLBeanFactory.getInstance().getBean2(ChequeFacadeRemote.class);
									cheque = dialogo.getCheque();
									try {
										cheque = cfr.grabarCheque(cheque, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
										updateCheque(cheque);
									} catch (CLException e1) {
										e1.printStackTrace();
									}
								}
								
							}
						}
					}
				}
			});

			return tablaCheques;
		}

		@Override
		protected Cheque getElemento(int fila) {
			return (Cheque) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		private ChequeFacadeRemote getChequeFacade() {
			if (chequeFacade == null) {
				chequeFacade = GTLBeanFactory.getInstance().getBean2(ChequeFacadeRemote.class);
			}
			return chequeFacade;
		}

		private BancoFacadeRemote getBancoFacade() {
			if (bancoFacade == null) {
				bancoFacade = GTLBeanFactory.getInstance().getBean2(BancoFacadeRemote.class);
			}
			return bancoFacade;
		}
	}

	private FacturaFacadeRemote getFacturaFacade() {
		if (facturaFacade == null) {
			facturaFacade = GTLBeanFactory.getInstance().getBean2(FacturaFacadeRemote.class);
		}
		return facturaFacade;
	}

	private ReciboFacadeRemote getReciboFacade() {
		if (reciboFacade == null) {
			reciboFacade = GTLBeanFactory.getInstance().getBean2(ReciboFacadeRemote.class);
		}
		return reciboFacade;
	}

	@SuppressWarnings("unused")
	private class MostrarFormaPagoVisitor implements IFormaPagoVisitor {

		private final PanelTablaCheque panTablaCheque;
		private final PanelTablaNotaCredito panelTablaNC;
		private final CLJTextField txtEfectivo;
		private final CLJTextField txtRetencionIVA;
		private final CLJTextField txtRetencionGanancias;
		private final CLJTextField txtRetencionIIBB;
		private final CLJTextField txtNroTx;
		private final CLJTextField txtImporteTx;
		private final CLJTextField txtObservaciones;

		public MostrarFormaPagoVisitor(PanelTablaCheque panTablaCheque, PanelTablaNotaCredito panelTablaNC, CLJTextField txtEfectivo, CLJTextField txtRetencionIVA, CLJTextField txtRetencionGanancias, CLJTextField txtRetencionIIBB, CLJTextField txtNroTx, CLJTextField txtImporteTx, CLJTextField txtObservaciones) {
			this.panelTablaNC = panelTablaNC;
			this.panTablaCheque = panTablaCheque;
			this.txtEfectivo = txtEfectivo;
			this.txtRetencionIVA = txtRetencionIVA;
			this.txtRetencionGanancias = txtRetencionGanancias;
			this.txtNroTx = txtNroTx;
			this.txtImporteTx = txtImporteTx;
			this.txtObservaciones = txtObservaciones;
			this.txtRetencionIIBB = txtRetencionIIBB;
		}

		public void visit(FormaPagoCheque formaPago) {
			panTablaCheque.agregarCheque(formaPago.getCheque());
		}

		public void visit(FormaPagoEfectivo formaPagoEfectivo) {
			txtEfectivo.setText(formaPagoEfectivo.getImporte().toString());
		}

		public void visit(FormaPagoRetencionIngresosBrutos formaPagoRetencionIngresosBrutos) {
			txtRetencionIIBB.setText(formaPagoRetencionIngresosBrutos.getImporte().toString());
		}

		public void visit(FormaPagoRetencionGanancias formaPagoRetGanancias) {
			txtRetencionGanancias.setText(formaPagoRetGanancias.getImporte().toString());
		}

		public void visit(FormaPagoRetencionIVA formaPagoRetencionIVA) {
			txtRetencionIVA.setText(formaPagoRetencionIVA.getImporte().toString());
		}

		public void visit(FormaPagoTransferencia formaPagoTransferencia) {
			txtObservaciones.setText(formaPagoTransferencia.getObservaciones());
			txtNroTransf.setText(String.valueOf(formaPagoTransferencia.getNroTx()));
			txtImporteTx.setText(String.valueOf(formaPagoTransferencia.getImporte()));
		}

		public void visit(FormaPagoNotaCredito formaPagoNotaCredito) {
			panelTablaNC.agregarElemento(formaPagoNotaCredito.getNotaCredito());
			panelTablaNC.getTabla().setValueAt(formaPagoNotaCredito.getImporte(), panelTablaNC.getTabla().getRowCount()-1, 2);
		}

	}

	private class MostrarPagoReciboVisitor implements IPagoReciboVisitor {

		private final PanelTablaPagosRecibo panelTablaPagosRecibo;

		public MostrarPagoReciboVisitor(PanelTablaPagosRecibo panelTablaPagosRecibo) {
			this.panelTablaPagosRecibo = panelTablaPagosRecibo;
		}

		public void visit(PagoReciboACuenta prac) {
			Object[] row = new Object[PanelTablaPagosRecibo.CANT_COLS_TBL_PAGOS_RECIBO];
			row[PanelTablaPagosRecibo.COL_FECHA] = DateUtil.dateToString(DateUtil.getHoy());
			row[PanelTablaPagosRecibo.COL_CONCEPTO_PAGO_RECIBO] = "A Cuenta";
			row[PanelTablaPagosRecibo.COL_IMPORTE_PAGO_RECIBO] = prac.getMontoPagado();
			row[PanelTablaPagosRecibo.COL_OBJ_PAGO_RECIBO] = prac;
			panelTablaPagosRecibo.getTabla().addRow(row);
		}

		public void visit(PagoReciboFactura prf) {
			Factura elemento = prf.getFactura();
			Object[] row = new Object[PanelTablaPagosRecibo.CANT_COLS_TBL_PAGOS_RECIBO];
			row[PanelTablaPagosRecibo.COL_FECHA] = DateUtil.dateToString(elemento.getFechaEmision());
			row[PanelTablaPagosRecibo.COL_CONCEPTO_PAGO_RECIBO] = "Factura - Nro.: " + elemento.getNroFactura() + getDescrPagoFactura(prf);
			row[PanelTablaPagosRecibo.COL_IMPORTE_PAGO_RECIBO] = prf.getMontoPagado();
			row[PanelTablaPagosRecibo.COL_OBJ_PAGO_RECIBO] = prf;
			panelTablaPagosRecibo.getTabla().addRow(row);
		}

		private String getDescrPagoFactura(PagoReciboFactura prf) {
			if(prf.getDescrPagoFactura() == null) {
				return "";
			}
			if(prf.getDescrPagoFactura() == EDescripcionPagoFactura.FACTURA) {
				return "";
			} else {
				return " (" + prf.getDescrPagoFactura().getDescripcion() + ") ";
			}
		}
		
//		private String getDescrPagoNotaDebito(PagoReciboNotaDebito prf) {
//			if(prf.getDescrPagoFactura() == null) {
//				return "";
//			}
//			if(prf.getDescrPagoFactura() == EDescripcionPagoFactura.FACTURA) {
//				return "";
//			} else {
//				return " (" + prf.getDescrPagoFactura().getDescripcion() + ") ";
//			}
//		}

		public void visit(PagoReciboNotaDebito prnd) {
			NotaDebito elemento = prnd.getNotaDebito();
			Object[] row = new Object[PanelTablaPagosRecibo.CANT_COLS_TBL_PAGOS_RECIBO];
			row[PanelTablaPagosRecibo.COL_FECHA] = DateUtil.dateToString(elemento.getFechaEmision());
			row[PanelTablaPagosRecibo.COL_CONCEPTO_PAGO_RECIBO] = "Nota de Débito - Nro.: " + elemento.getNroFactura(); //TODO: PONER SALDO/A CUENTA, ETC
			row[PanelTablaPagosRecibo.COL_IMPORTE_PAGO_RECIBO] = prnd.getMontoPagado();
			row[PanelTablaPagosRecibo.COL_OBJ_PAGO_RECIBO] = prnd;
			panelTablaPagosRecibo.getTabla().addRow(row);
		}

	}

	private class ActualizarItemPagoReciboVisitor implements IPagoReciboVisitor {

		public void visit(PagoReciboACuenta prac) {
		}

		public void visit(PagoReciboFactura prf) {
			Factura f = prf.getFactura();
			if (prf.getMontoPagado().compareTo(f.getMontoFaltantePorPagar()) == 0) {
				f.setEstadoFactura(EEstadoFactura.PAGADA);
				f.setMontoFaltantePorPagar(new BigDecimal(0));
			} else {
				f.setMontoFaltantePorPagar(f.getMontoFaltantePorPagar().subtract(prf.getMontoPagado()));
				f.setEstadoFactura(EEstadoFactura.IMPAGA);
			}
		}

		public void visit(PagoReciboNotaDebito prnd) {
			NotaDebito nd = prnd.getNotaDebito();
			if (prnd.getMontoPagado().compareTo(nd.getMontoTotal()) == 0) {
				nd.setMontoFaltantePorPagar(new BigDecimal(0));
				nd.setEstadoCorreccion(EEstadoCorreccion.PAGADA);
			} else {
				nd.setMontoFaltantePorPagar(nd.getMontoFaltantePorPagar().subtract(prnd.getMontoPagado()));
				nd.setEstadoCorreccion(EEstadoCorreccion.IMPAGA);
			}
		}

	}

	private class ConsultaItemPagoReciboVisitor implements IPagoReciboVisitor {

		public void visit(PagoReciboACuenta prac) {
		}

		public void visit(PagoReciboFactura prf) {
			Factura factura = getFacturaFacade().getByNroFacturaConItems(prf.getFactura().getNroFactura());
			JDialogCargaFactura dialogSeleccionarFactura = new JDialogCargaFactura(owner, factura, true);
			dialogSeleccionarFactura.setVisible(true);
		}

		public void visit(PagoReciboNotaDebito prnd) {
			try {
				CorreccionFacadeRemote cfr = GTLBeanFactory.getInstance().getBean(CorreccionFacadeRemote.class);
				CorreccionFactura correccion = cfr.getCorreccionByNumero(prnd.getNotaDebito().getNroFactura(), ETipoCorreccionFactura.NOTA_DEBITO);
				JDialogCargaFactura dialogCargaFactura = new JDialogCargaFactura(null,correccion, true);
				dialogCargaFactura.setVisible(true);
			} catch (CLException e) {
				BossError.gestionarError(e);
			}
		}

	}

	private JButton getBtnVerificar() {
		if(btnVerificar == null){
			btnVerificar = new JButton("Verificar");
			btnVerificar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					actualizarPagosReciboEnTabla();
				}
			});
		}
		return btnVerificar;
	}

	private void actualizarPagosReciboEnTabla() {
		BigDecimal montoTotalPagosRecibo = new BigDecimal(0);
		getPanelTablaPagosRecibo().limpiar();
		BigDecimal totalMontoPagado = getTotalMontoPagado();
		List<PagoRecibo> pagoReciboList = new ArrayList<PagoRecibo>();
		if(totalMontoPagado != null) {
			//Me fijo si hay notas de debitos por pagar
			List<NotaDebito> notaDebitoList = getNotaDebitoPendientePagarList();
			for(NotaDebito nd : notaDebitoList) {
				if(totalMontoPagado.compareTo(new BigDecimal(0)) > 0) {
					BigDecimal montoND = nd.getMontoFaltantePorPagar();
					PagoReciboNotaDebito prnd = new PagoReciboNotaDebito();
					prnd.setNotaDebito(nd);
					if(montoND.compareTo(totalMontoPagado) >= 0) {
						prnd.setMontoPagado(totalMontoPagado);
						totalMontoPagado = new BigDecimal(0);
					} else {
						prnd.setMontoPagado(montoND);
						totalMontoPagado = totalMontoPagado.subtract(montoND);
					}
					pagoReciboList.add(prnd);
					montoTotalPagosRecibo = montoTotalPagosRecibo.add(prnd.getMontoPagado());
				}
			}
			//Me fijo si hay facturas por pagar
			List<Factura> facturaList = getFacturaNoPagadaList();
			for(Factura f : facturaList) {
				if(totalMontoPagado.compareTo(new BigDecimal(0)) > 0) {
					BigDecimal montoF = f.getMontoFaltantePorPagar();
					PagoReciboFactura prf = new PagoReciboFactura();
					prf.setFactura(f);
					if(montoF.compareTo(totalMontoPagado) >= 0) {
						prf.setMontoPagado(totalMontoPagado);
						totalMontoPagado = new BigDecimal(0);
					} else {
						prf.setMontoPagado(montoF);
						totalMontoPagado = totalMontoPagado.subtract(montoF);
					}
					//Seteo la descripción del pago de factura
					if(prf.getMontoPagado().compareTo(f.getMontoTotal()) == 0) {
						prf.setDescrPagoFactura(EDescripcionPagoFactura.FACTURA);
					} else {
						if(prf.getMontoPagado().compareTo(montoF) == 0) {
							prf.setDescrPagoFactura(EDescripcionPagoFactura.SALDO);
						} else {
							prf.setDescrPagoFactura(EDescripcionPagoFactura.A_CUENTA);
						}
					}
					pagoReciboList.add(prf);
					montoTotalPagosRecibo = montoTotalPagosRecibo.add(prf.getMontoPagado());
				}
			}
			//Si sobró algo de monto entonces creo un pago "a cuenta" 
			if(totalMontoPagado.compareTo(new BigDecimal(0)) > 0) {
				PagoReciboACuenta prac = new PagoReciboACuenta();
				prac.setMontoPagado(totalMontoPagado);
				pagoReciboList.add(prac);
				montoTotalPagosRecibo = montoTotalPagosRecibo.add(prac.getMontoPagado());
			}
		}
		//Lleno la tabla con los pagos recibos calculados 
		getPanelTablaPagosRecibo().agregarElementos(pagoReciboList);
		//Actualizo el textfield de total de facturas
		getTxtTotalFacturas().setText(montoTotalPagosRecibo.toString());
		//Actualizo el textfield de total
		setTotalReciboIgnorandoMontoNC(montoTotalPagosRecibo);
	}

	private List<Factura> getFacturaNoPagadaList() {
		if(facturaNoPagadas == null) {
			facturaNoPagadas = getFacturaFacade().getFacturaImpagaListByClient(recibo.getCliente().getId());
			if(isEdicionRecibo()) {
				List<Factura> facturaInReciboList = extractFacturas();
				facturaNoPagadas.removeAll(facturaInReciboList);
				facturaNoPagadas.addAll(facturaInReciboList);
				Collections.sort(facturaNoPagadas, new Comparator<Factura>() {

					public int compare(Factura o1, Factura o2) {
						int compareToPorFechaEmision = o1.getFechaEmision().compareTo(o2.getFechaEmision());
						if(compareToPorFechaEmision == 0) {
							return o1.getId().compareTo(o2.getId());
						} else {
							return compareToPorFechaEmision;
						}
					}

				});
			}
		}
		return facturaNoPagadas;
	}

	private List<Factura> extractFacturas() {
		List<Factura> facturasReciboList = new ArrayList<Factura>();
		for(PagoRecibo pr : recibo.getPagoReciboList()) {
			if(pr instanceof PagoReciboFactura) {
				Factura factura = ((PagoReciboFactura)pr).getFactura();
				factura.setMontoFaltantePorPagar(factura.getMontoFaltantePorPagar().add(pr.getMontoPagado()));
				facturasReciboList.add(factura);
			}
		}
		return facturasReciboList;
	}

	private List<NotaDebito> getNotaDebitoPendientePagarList() {
		if(notaDebitoPendienteList == null) {
			notaDebitoPendienteList = getNotaDebitoFacade().getNotaDebitoPendientePagarList(recibo.getCliente().getId());
			if(isEdicionRecibo()) {
				List<NotaDebito> notasDebitoInReciboList = extractNotasDebitos();
				notaDebitoPendienteList.removeAll(notasDebitoInReciboList);
				notaDebitoPendienteList.addAll(notasDebitoInReciboList);
				Collections.sort(notaDebitoPendienteList, new Comparator<NotaDebito>() {

					public int compare(NotaDebito o1, NotaDebito o2) {
						return o1.getFechaEmision().compareTo(o2.getFechaEmision());
					}

				});
			}
		}
		return notaDebitoPendienteList;
	}

	private List<NotaDebito> extractNotasDebitos() {
		List<NotaDebito> notaDebitoReciboList = new ArrayList<NotaDebito>();
		for(PagoRecibo pr : recibo.getPagoReciboList()) {
			if(pr instanceof PagoReciboNotaDebito) {
				NotaDebito nd = ((PagoReciboNotaDebito)pr).getNotaDebito();
				nd.setMontoFaltantePorPagar(nd.getMontoFaltantePorPagar().add(pr.getMontoPagado()));
				notaDebitoReciboList.add(nd);
			}
		}
		return notaDebitoReciboList;
	}

	private List<NotaCredito> getNotaCreditoList() {
		if(notaCreditoList == null) {
			notaCreditoList = getCorreccionFacturaFacade().getNotaCreditoPendienteUsarList(recibo.getCliente().getId());
		}
		return notaCreditoList;
	}

	private NotaDebitoFacadeRemote getNotaDebitoFacade() {
		if(notaDebitoFacade == null) {
			notaDebitoFacade = GTLBeanFactory.getInstance().getBean2(NotaDebitoFacadeRemote.class);
		}
		return notaDebitoFacade;
	}

	private CorreccionFacadeRemote getCorreccionFacturaFacade() {
		if(correccionFacturaFacade == null) {
			correccionFacturaFacade = GTLBeanFactory.getInstance().getBean2(CorreccionFacadeRemote.class);
		}
		return correccionFacturaFacade;
	}

	private ParametrosGeneralesFacadeRemote getParametrosGeneralesFacade() {
		if(parametrosGeneralesFacade == null) {
			parametrosGeneralesFacade = GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class);
		}
		return parametrosGeneralesFacade;
	}

}
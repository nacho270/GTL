package ar.com.textillevel.gui.acciones;

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
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.cheque.Banco;
import ar.com.textillevel.entidades.cheque.NumeracionCheque;
import ar.com.textillevel.entidades.config.ParametrosGenerales;
import ar.com.textillevel.entidades.enums.ETipoFactura;
import ar.com.textillevel.facade.api.remote.BancoFacadeRemote;
import ar.com.textillevel.facade.api.remote.ChequeFacadeRemote;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.DecimalNumericTextField;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogParametrosGenerales extends JDialog {

	private static final long serialVersionUID = -1941414399843591399L;

	private ParametrosGenerales parametrosGenerales;
	private ParametrosGeneralesFacadeRemote parametrosFacade;
	private BancoFacadeRemote bancoFacade;

	private CLJNumericTextField txtNroComienzoRemito;
	private CLJNumericTextField txtNroComienzoFactura;
	private CLJNumericTextField txtNroComienzoRecibo;
	private CLJNumericTextField txtValidezCotizaciones;
	private DecimalNumericTextField txtMontoMinimoValidacionPrecio;
	private DecimalNumericTextField txtMontoMaximoValidacionPrecio;
	private CLJTextField txtPorcentajeIvaInscripto;
	private CLJTextField txtPorcentajeIvaNoInscripto;
	private CLJTextField txtPorcentajeSeguroMercaderia;
	private CLJTextField txtPorcentajeMerma;
	private CLJTextField txtPrecioTubo;
	private CLJTextField txtLetraCheque;
	private CLJTextField txtNumeroCheque;
	private CLJTextField txtCargaMinimaColor;
	private CLJTextField txtCargaMinimaEstampado;
	private CLJNumericTextField txtNroSucursal;
	private CLJNumericTextField txtDiasAvisoVencimientoCheque;
	private CLJNumericTextField txtDiasVencenCheques;
	private CLJNumericTextField txtNroComienzoODT;
	private CLJNumericTextField txtNroIGJ;
	private CLJNumericTextField txtNroComienzoFacturaB;
	private CLJNumericTextField txtNroComienzoOrdenDePago;
	private CLJNumericTextField txtNroComienzoOrdenDeDeposito;
	private CLJNumericTextField txtNroComienzoOrdenDePagoPersona;
	private JComboBox cmbBancos;
	private CLJTextField txtMinimoDeuda;
	private JButton btnConfigurarNumeracionA;
	private JButton btnConfigurarNumeracionB;
	
	private boolean acepto;
	
	private JButton btnGuardar;
	private JButton btnSalir;
	private JTabbedPane panelDetalles;
	private JPanel panelBotones;
	
	private JPanel panelTabCheques;
	private JPanel panelTabFacturacion;
	private JPanel panelTabVarios;

	public JDialogParametrosGenerales(Frame frame, ParametrosGenerales pg) {
		super(frame);
		this.setParametrosGenerales(pg);
		construct();
	}

	private void construct() {
		setUpComponentes();
		setUpScreen();
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		this.add(getPanelDetalles(), BorderLayout.CENTER);
		this.add(getPanelBotones(), BorderLayout.SOUTH);
	}

	private void salir() {
		int ret = CLJOptionPane.showQuestionMessage(this, "Va a salir sin guardar, esta seguro?", "Parametros generales");
		if (ret == CLJOptionPane.YES_OPTION) {
			dispose();
		}
	}

	private void setUpScreen() {
		this.setTitle("Parametros generales");
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.setSize(new Dimension(600, 500));
		this.setResizable(false);
		GuiUtil.centrar(this);
		this.setModal(true);
	}

	private ParametrosGenerales getParametrosGenerales() {
		return parametrosGenerales;
	}

	private void guardar() {
		if (validar()) {
			getParametrosGenerales().setNroComienzoFactura(Integer.valueOf(getTxtNroComienzoFactura().getText().trim().replace(',', '.')));
			getParametrosGenerales().setNroComienzoRemito(Integer.valueOf(getTxtNroComienzoRemito().getText().trim().replace(',', '.')));
			getParametrosGenerales().setNroComienzoRecibo(Integer.valueOf(getTxtNroComienzoRecibo().getText().trim().replace(',', '.')));
			getParametrosGenerales().setPorcentajeIVAInscripto(new BigDecimal(Double.valueOf(getTxtPorcentajeIvaInscripto().getText().trim().replace(',', '.'))));
			getParametrosGenerales().setPorcentajeIVANoInscripto(new BigDecimal(Double.valueOf(getTxtPorcentajeIvaNoInscripto().getText().trim().replace(',', '.'))));
			getParametrosGenerales().setPorcentajeSeguro(new BigDecimal(Double.valueOf(getTxtPorcentajeSeguroMercaderia().getText().trim().replace(',', '.'))));
			getParametrosGenerales().setPorcentajeToleranciaMermaNegativa(new BigDecimal(Double.valueOf(getTxtPorcentajeMerma().getText().trim().replace(',', '.'))));
			getParametrosGenerales().setPrecioPorTubo(new BigDecimal(Double.valueOf(getTxtPrecioTubo().getText().trim().replace(',', '.'))));
			getParametrosGenerales().setValidezCotizaciones(Integer.valueOf(getTxtValidezCotizaciones().getText().trim()));
			NumeracionCheque nc = new NumeracionCheque();
			nc.setLetra(Character.valueOf(getTxtLetraCheque().getText().trim().toUpperCase().charAt(0)));
			nc.setNumero(Integer.valueOf(getTxtNumeroCheque().getText().trim()));
			getParametrosGenerales().setNumeracionCheque(nc);
			getParametrosGenerales().setNroSucursal(Integer.valueOf(getTxtNroSucursal().getText()));
			getParametrosGenerales().setDiasAvisoVencimientoDeCheque(getTxtDiasAvisoVencimientoCheque().getValue());
			getParametrosGenerales().setDiasVenceCheque(getTxtDiasVencenCheques().getValue());
			getParametrosGenerales().setNroComienzoODT(getTxtNroComienzoODT().getValue());
			getParametrosGenerales().setNroIGJ(getTxtNroIGJ().getValue());
			getParametrosGenerales().setNroComienzoFacturaB(getTxtNroComienzoFacturaB().getValue());
			getParametrosGenerales().setNroComienzoOrdenDePago(getTxtNroComienzoOrdenDePago().getValue());
			getParametrosGenerales().setNroComienzoOrdenDeDeposito(getTxtNroComienzoOrdenDeDeposito().getValue());
			getParametrosGenerales().setBancoDefault((Banco)getCmbBancos().getSelectedItem());
			String textoMinimoDeuda = getTxtMinimoDeuda().getText().trim().replaceAll("\\.", "").replaceAll(",", ".");
			getParametrosGenerales().setUmbralDeuda(new BigDecimal(Double.valueOf(textoMinimoDeuda)));
			getParametrosGenerales().setNroComienzoOrdenDePagoPersona(getTxtNroComienzoOrdenDePagoPersona().getValue());
			getParametrosGenerales().setCargaMinimaColor(new BigDecimal(Double.valueOf(getTxtCargaMinimaColor().getText().trim().replace(',', '.'))));
			getParametrosGenerales().setCargaMinimaEstampado(new BigDecimal(Double.valueOf(getTxtCargaMinimaEstampado().getText().trim().replace(',', '.'))));
			getParametrosGenerales().setMontoMinimoValidacionPrecio(new BigDecimal(getTxtMontoMinimoValidacionPrecio().getValue()));
			getParametrosGenerales().setMontoMaximoValidacionPrecio(new BigDecimal(getTxtMontoMaximoValidacionPrecio().getValue()));
			try {
				getParametrosFacade().save(getParametrosGenerales());
				setAcepto(true);
				CLJOptionPane.showInformationMessage(this, "La configuración ha sido guarda con exito", "Parametros generales");
				dispose();
			} catch (CLException cle) {
				BossError.gestionarError(cle);
			}
		}
	}

	private void setParametrosGenerales(ParametrosGenerales parametrosGenerales) {
		this.parametrosGenerales = parametrosGenerales != null ? parametrosGenerales : new ParametrosGenerales();
	}

	private ParametrosGeneralesFacadeRemote getParametrosFacade() {
		if (parametrosFacade == null) {
			try {
				parametrosFacade = GTLBeanFactory.getInstance().getBean(ParametrosGeneralesFacadeRemote.class);
			} catch (CLException e) {
				BossError.gestionarError(e);
			}
		}
		return parametrosFacade;
	}

	private boolean validar() {
		if (StringUtil.isNullOrEmpty(getTxtNroComienzoFactura().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
			getTxtNroComienzoFactura().requestFocus();
			return false;
		} else {
			if (!GenericUtils.esNumerico((getTxtNroComienzoFactura().getText()))) {
				CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
				getTxtNroComienzoFactura().requestFocus();
				return false;
			}
		}

		if (StringUtil.isNullOrEmpty(getTxtNroComienzoRemito().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
			getTxtNroComienzoRemito().requestFocus();
			return false;
		} else {
			if (!GenericUtils.esNumerico((getTxtNroComienzoRemito().getText()))) {
				CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
				getTxtNroComienzoRemito().requestFocus();
				return false;
			}
		}

		if (StringUtil.isNullOrEmpty(getTxtNroComienzoRecibo().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
			getTxtNroComienzoRecibo().requestFocus();
			return false;
		} else {
			if (!GenericUtils.esNumerico((getTxtNroComienzoRecibo().getText()))) {
				CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
				getTxtNroComienzoRecibo().requestFocus();
				return false;
			}
		}

		if (StringUtil.isNullOrEmpty(getTxtPorcentajeIvaInscripto().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
			getTxtPorcentajeIvaInscripto().requestFocus();
			return false;
		} else {
			if (!GenericUtils.esNumerico((getTxtPorcentajeIvaInscripto().getText()))) {
				CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
				getTxtPorcentajeIvaInscripto().requestFocus();
				return false;
			}
		}

		if (StringUtil.isNullOrEmpty(getTxtPorcentajeIvaNoInscripto().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
			getTxtPorcentajeIvaNoInscripto().requestFocus();
			return false;
		} else {
			if (!GenericUtils.esNumerico((getTxtPorcentajeIvaNoInscripto().getText()))) {
				CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
				getTxtPorcentajeIvaNoInscripto().requestFocus();
				return false;
			}
		}

		if (StringUtil.isNullOrEmpty(getTxtPorcentajeSeguroMercaderia().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
			getTxtPorcentajeSeguroMercaderia().requestFocus();
			return false;
		} else {
			if (!GenericUtils.esNumerico((getTxtPorcentajeSeguroMercaderia().getText()))) {
				CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
				getTxtPorcentajeSeguroMercaderia().requestFocus();
				return false;
			}
		}

		if (StringUtil.isNullOrEmpty(getTxtPorcentajeMerma().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
			getTxtPorcentajeMerma().requestFocus();
			return false;
		} else {
			if (!GenericUtils.esNumerico((getTxtPorcentajeMerma().getText()))) {
				CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
				getTxtPorcentajeMerma().requestFocus();
				return false;
			}
		}

		if (StringUtil.isNullOrEmpty(getTxtPrecioTubo().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
			getTxtPrecioTubo().requestFocus();
			return false;
		} else {
			if (!GenericUtils.esNumerico((getTxtPrecioTubo().getText()))) {
				CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
				getTxtPrecioTubo().requestFocus();
				return false;
			}
		}

		if (StringUtil.isNullOrEmpty(getTxtLetraCheque().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
			getTxtLetraCheque().requestFocus();
			return false;
		}

		if (getTxtLetraCheque().getText().trim().length() > 1) {
			CLJOptionPane.showErrorMessage(this, "Solo se puede ingresar un caracter", "Error");
			getTxtLetraCheque().requestFocus();
			return false;
		}

		if (!Character.isLetter(getTxtLetraCheque().getText().trim().charAt(0))) {
			CLJOptionPane.showErrorMessage(this, "Solo se pueden ingresar letras", "Error");
			getTxtLetraCheque().requestFocus();
			return false;
		}

		if (StringUtil.isNullOrEmpty(getTxtNumeroCheque().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
			getTxtNumeroCheque().requestFocus();
			return false;
		} else {
			if (!GenericUtils.esNumerico((getTxtNumeroCheque().getText()))) {
				CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
				getTxtNumeroCheque().requestFocus();
				return false;
			}
		}

		if (getParametrosGenerales() != null && getParametrosGenerales().getNumeracionCheque() != null) {
			Character letra = getTxtLetraCheque().getText().trim().charAt(0);
			Integer numero = Integer.valueOf(getTxtNumeroCheque().getText().trim());
			if (!getParametrosGenerales().getNumeracionCheque().getLetra().equals(letra) || !getParametrosGenerales().getNumeracionCheque().getNumero().equals(numero)) {
				verificarYCorregirNumeracion(letra, numero);
			}
		}

		if (StringUtil.isNullOrEmpty(getTxtNroSucursal().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
			getTxtNroSucursal().requestFocus();
			return false;
		} else {
			if (!GenericUtils.esNumerico((getTxtNroSucursal().getText()))) {
				CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
				getTxtNroSucursal().requestFocus();
				return false;
			}
		}

		if (StringUtil.isNullOrEmpty(getTxtDiasAvisoVencimientoCheque().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
			getTxtDiasAvisoVencimientoCheque().requestFocus();
			return false;
		} else {
			if (!GenericUtils.esNumerico((getTxtDiasAvisoVencimientoCheque().getText()))) {
				CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
				getTxtDiasAvisoVencimientoCheque().requestFocus();
				return false;
			}
		}
		
		if (StringUtil.isNullOrEmpty(getTxtDiasVencenCheques().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
			getTxtDiasVencenCheques().requestFocus();
			return false;
		} else {
			if (!GenericUtils.esNumerico((getTxtDiasVencenCheques().getText()))) {
				CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
				getTxtDiasVencenCheques().requestFocus();
				return false;
			}
		}

		if (StringUtil.isNullOrEmpty(getTxtNroComienzoODT().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
			getTxtNroComienzoODT().requestFocus();
			return false;
		} else {
			if (!GenericUtils.esNumerico((getTxtNroComienzoODT().getText()))) {
				CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
				getTxtNroComienzoODT().requestFocus();
				return false;
			}
		}
		
		if (StringUtil.isNullOrEmpty(getTxtNroIGJ().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
			getTxtNroIGJ().requestFocus();
			return false;
		} else {
			if (!GenericUtils.esNumerico((getTxtNroIGJ().getText()))) {
				CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
				getTxtNroIGJ().requestFocus();
				return false;
			}
		}
		
		if (StringUtil.isNullOrEmpty(getTxtNroComienzoFacturaB().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
			getTxtNroComienzoFacturaB().requestFocus();
			return false;
		} else {
			if (!GenericUtils.esNumerico((getTxtNroComienzoFacturaB().getText()))) {
				CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
				getTxtNroComienzoFacturaB().requestFocus();
				return false;
			}
		}
		
		if (StringUtil.isNullOrEmpty(getTxtNroComienzoOrdenDePago().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
			getTxtNroComienzoOrdenDePago().requestFocus();
			return false;
		} else {
			if (!GenericUtils.esNumerico((getTxtNroComienzoOrdenDePago().getText()))) {
				CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
				getTxtNroComienzoOrdenDePago().requestFocus();
				return false;
			}
		}
		
		if (StringUtil.isNullOrEmpty(getTxtNroComienzoOrdenDeDeposito().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
			getTxtNroComienzoOrdenDeDeposito().requestFocus();
			return false;
		} else {
			if (!GenericUtils.esNumerico((getTxtNroComienzoOrdenDeDeposito().getText()))) {
				CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
				getTxtNroComienzoOrdenDeDeposito().requestFocus();
				return false;
			}
		}
		
		if (StringUtil.isNullOrEmpty(getTxtMinimoDeuda().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
			getTxtMinimoDeuda().requestFocus();
			return false;
		} else {
			String textoMinimoDeuda = getTxtMinimoDeuda().getText().trim().replaceAll("\\.", "").replaceAll(",", ".");
			if (!GenericUtils.esNumerico((textoMinimoDeuda))) {
				CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
				getTxtMinimoDeuda().requestFocus();
				return false;
			}
		}
		
		if (StringUtil.isNullOrEmpty(getTxtNroComienzoOrdenDePagoPersona().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
			getTxtNroComienzoOrdenDePagoPersona().requestFocus();
			return false;
		} else {
			if (!GenericUtils.esNumerico((getTxtNroComienzoOrdenDePagoPersona().getText()))) {
				CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
				getTxtNroComienzoOrdenDePagoPersona().requestFocus();
				return false;
			}
		}
		
		if (StringUtil.isNullOrEmpty(getTxtCargaMinimaColor().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
			getTxtCargaMinimaColor().requestFocus();
			return false;
		} else {
			if (!GenericUtils.esNumerico((getTxtCargaMinimaColor().getText()))) {
				CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
				getTxtCargaMinimaColor().requestFocus();
				return false;
			}
		}
		
		if (StringUtil.isNullOrEmpty(getTxtCargaMinimaEstampado().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
			getTxtCargaMinimaEstampado().requestFocus();
			return false;
		} else {
			if (!GenericUtils.esNumerico((getTxtCargaMinimaEstampado().getText()))) {
				CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
				getTxtCargaMinimaEstampado().requestFocus();
				return false;
			}
		}
		
		if (StringUtil.isNullOrEmpty(getTxtValidezCotizaciones().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
			getTxtValidezCotizaciones().requestFocus();
			return false;
		} else {
			if (!GenericUtils.esNumerico((getTxtValidezCotizaciones().getText()))) {
				CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
				getTxtValidezCotizaciones().requestFocus();
				return false;
			}
		}

		if (StringUtil.isNullOrEmpty(getTxtMontoMinimoValidacionPrecio().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
			getTxtMontoMinimoValidacionPrecio().requestFocus();
			return false;
		} else {
			if (!GenericUtils.esNumerico((getTxtMontoMinimoValidacionPrecio().getText()))) {
				CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
				getTxtMontoMinimoValidacionPrecio().requestFocus();
				return false;
			}
		}
		
		if (StringUtil.isNullOrEmpty(getTxtMontoMaximoValidacionPrecio().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe completar todos los campos", "Error");
			getTxtMontoMaximoValidacionPrecio().requestFocus();
			return false;
		} else {
			if (!GenericUtils.esNumerico((getTxtMontoMaximoValidacionPrecio().getText()))) {
				CLJOptionPane.showErrorMessage(this, "El campo es numerico", "Error");
				getTxtMontoMaximoValidacionPrecio().requestFocus();
				return false;
			}
		}
		
		if (getTxtMontoMinimoValidacionPrecio().getValue().floatValue() >= getTxtMontoMaximoValidacionPrecio().getValue().floatValue()) {
			CLJOptionPane.showErrorMessage(this, "El precio de validación mínimo deber ser menor al máximo.", "Error");
			getTxtMontoMaximoValidacionPrecio().requestFocus();
			return false;
		}
		
		return true;
	}

	private void verificarYCorregirNumeracion(Character letra, Integer numero) {
		ChequeFacadeRemote cfr = GTLBeanFactory.getInstance().getBean2(ChequeFacadeRemote.class);
		Integer ultimoNumeroInternoCheque = cfr.getUltimoNumeroInternoCheque(letra);
		if (ultimoNumeroInternoCheque != null && ultimoNumeroInternoCheque > numero) {
			CLJOptionPane.showWarningMessage(this, StringW.wordWrap("La letra ingresada ya contiene numeros asigandos.\n\nEl ultimo número es: " + ultimoNumeroInternoCheque + ". Se asigna: "
					+ (ultimoNumeroInternoCheque + 1) + "."), "Parametros generales");
			getTxtNumeroCheque().setText(String.valueOf(ultimoNumeroInternoCheque + 1));
		}
	}

	private CLJNumericTextField getTxtNroComienzoRemito() {
		if (txtNroComienzoRemito == null) {
			txtNroComienzoRemito = new CLJNumericTextField();
			if (getParametrosGenerales() != null && getParametrosGenerales().getNroComienzoRemito() != null) {
				txtNroComienzoRemito.setValue(getParametrosGenerales().getNroComienzoRemito().longValue());
			}
		}
		return txtNroComienzoRemito;
	}

	private CLJNumericTextField getTxtNroComienzoFactura() {
		if (txtNroComienzoFactura == null) {
			txtNroComienzoFactura = new CLJNumericTextField();
			if (getParametrosGenerales() != null && getParametrosGenerales().getNroComienzoFactura() != null) {
				txtNroComienzoFactura.setValue(getParametrosGenerales().getNroComienzoFactura().longValue());
			}
		}
		return txtNroComienzoFactura;
	}

	private CLJTextField getTxtPorcentajeIvaInscripto() {
		if (txtPorcentajeIvaInscripto == null) {
			txtPorcentajeIvaInscripto = new CLJTextField();
			if (getParametrosGenerales() != null && getParametrosGenerales().getPorcentajeIVAInscripto() != null) {
				txtPorcentajeIvaInscripto.setText(String.valueOf((getParametrosGenerales().getPorcentajeIVAInscripto().doubleValue())));
			}
		}
		return txtPorcentajeIvaInscripto;
	}

	private CLJTextField getTxtPorcentajeIvaNoInscripto() {
		if (txtPorcentajeIvaNoInscripto == null) {
			txtPorcentajeIvaNoInscripto = new CLJTextField();
			if (getParametrosGenerales() != null && getParametrosGenerales().getPorcentajeIVANoInscripto() != null) {
				txtPorcentajeIvaNoInscripto.setText(String.valueOf((getParametrosGenerales().getPorcentajeIVANoInscripto().doubleValue())));
			}
		}
		return txtPorcentajeIvaNoInscripto;
	}

	private CLJTextField getTxtPorcentajeSeguroMercaderia() {
		if (txtPorcentajeSeguroMercaderia == null) {
			txtPorcentajeSeguroMercaderia = new CLJTextField();
			if (getParametrosGenerales() != null && getParametrosGenerales().getPorcentajeSeguro() != null) {
				txtPorcentajeSeguroMercaderia.setText(String.valueOf((getParametrosGenerales().getPorcentajeSeguro().doubleValue())));
			}
		}
		return txtPorcentajeSeguroMercaderia;
	}
	
	public CLJTextField getTxtCargaMinimaColor() {
		if (txtCargaMinimaColor == null) {
			txtCargaMinimaColor = new CLJTextField();
			if (getParametrosGenerales() != null && getParametrosGenerales().getCargaMinimaColor() != null) {
				txtCargaMinimaColor.setText(String.valueOf((getParametrosGenerales().getCargaMinimaColor().doubleValue())));
			}
		}
		return txtCargaMinimaColor;
	}

	public CLJTextField getTxtCargaMinimaEstampado() {
		if (txtCargaMinimaEstampado == null) {
			txtCargaMinimaEstampado = new CLJTextField();
			if (getParametrosGenerales() != null && getParametrosGenerales().getCargaMinimaEstampado() != null) {
				txtCargaMinimaEstampado.setText(String.valueOf((getParametrosGenerales().getCargaMinimaEstampado().doubleValue())));
			}
		}
		return txtCargaMinimaEstampado;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					guardar();
				}
			});
		}
		return btnGuardar;
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

	private JTabbedPane getPanelDetalles() {
		if (panelDetalles == null) {
			panelDetalles = new JTabbedPane();
			panelDetalles.addTab("Facturación", getPanelTabFacturacion());
			panelDetalles.addTab("Cheques", getPanelTabCheques());
			panelDetalles.addTab("Varios", getPanelTabVarios());
		}
		return panelDetalles;
	}

	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
			panelBotones.add(getBtnGuardar());
			panelBotones.add(getBtnSalir());
		}
		return panelBotones;
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

	private CLJTextField getTxtPorcentajeMerma() {
		if (txtPorcentajeMerma == null) {
			txtPorcentajeMerma = new CLJTextField();
			if (getParametrosGenerales() != null && getParametrosGenerales().getPorcentajeToleranciaMermaNegativa() != null) {
				txtPorcentajeMerma.setText(String.valueOf((getParametrosGenerales().getPorcentajeIVANoInscripto().doubleValue())));
			}
		}
		return txtPorcentajeMerma;
	}

	public CLJTextField getTxtPrecioTubo() {
		if (txtPrecioTubo == null) {
			txtPrecioTubo = new CLJTextField();
			if (getParametrosGenerales() != null && getParametrosGenerales().getPrecioPorTubo() != null) {
				txtPrecioTubo.setText(String.valueOf((getParametrosGenerales().getPrecioPorTubo().doubleValue())));
			}
		}
		return txtPrecioTubo;
	}

	private CLJTextField getTxtLetraCheque() {
		if (txtLetraCheque == null) {
			txtLetraCheque = new CLJTextField();
			if (getParametrosGenerales() != null && getParametrosGenerales().getNumeracionCheque() != null) {
				txtLetraCheque.setText(String.valueOf((getParametrosGenerales().getNumeracionCheque().getLetra())));
			}
		}
		return txtLetraCheque;
	}

	private CLJTextField getTxtNumeroCheque() {
		if (txtNumeroCheque == null) {
			txtNumeroCheque = new CLJTextField();
			if (getParametrosGenerales() != null && getParametrosGenerales().getNumeracionCheque() != null) {
				txtNumeroCheque.setText(String.valueOf((getParametrosGenerales().getNumeracionCheque().getNumero())));
			}
		}
		return txtNumeroCheque;
	}

	private CLJNumericTextField getTxtNroSucursal() {
		if (txtNroSucursal == null) {
			txtNroSucursal = new CLJNumericTextField();
			if (getParametrosGenerales() != null && getParametrosGenerales().getNroSucursal() != null) {
				txtNroSucursal.setText(String.valueOf(getParametrosGenerales().getNroSucursal()));
			}
		}
		return txtNroSucursal;
	}

	private CLJNumericTextField getTxtNroComienzoRecibo() {
		if (txtNroComienzoRecibo == null) {
			txtNroComienzoRecibo = new CLJNumericTextField();
			if (getParametrosGenerales() != null && getParametrosGenerales().getNroComienzoRecibo() != null) {
				txtNroComienzoRecibo.setValue(getParametrosGenerales().getNroComienzoRecibo().longValue());
			}
		}
		return txtNroComienzoRecibo;
	}

	private CLJNumericTextField getTxtDiasAvisoVencimientoCheque() {
		if (txtDiasAvisoVencimientoCheque == null) {
			txtDiasAvisoVencimientoCheque = new CLJNumericTextField();
			if (getParametrosGenerales() != null && getParametrosGenerales().getDiasAvisoVencimientoDeCheque() != null) {
				txtDiasAvisoVencimientoCheque.setValue(getParametrosGenerales().getDiasAvisoVencimientoDeCheque().longValue());
			}
		}
		return txtDiasAvisoVencimientoCheque;
	}

	
	private CLJNumericTextField getTxtDiasVencenCheques() {
		if (txtDiasVencenCheques == null) {
			txtDiasVencenCheques = new CLJNumericTextField();
			if (getParametrosGenerales() != null && getParametrosGenerales().getDiasVenceCheque() != null) {
				txtDiasVencenCheques.setValue(getParametrosGenerales().getDiasVenceCheque().longValue());
			}
		}
		return txtDiasVencenCheques;
	}

	private CLJNumericTextField getTxtNroComienzoODT() {
		if (txtNroComienzoODT == null) {
			txtNroComienzoODT = new CLJNumericTextField();
			if (getParametrosGenerales() != null && getParametrosGenerales().getNroComienzoODT() != null) {
				txtNroComienzoODT.setValue(getParametrosGenerales().getNroComienzoODT().longValue());
			}
		}
		return txtNroComienzoODT;
	}

	private CLJNumericTextField getTxtNroIGJ() {
		if (txtNroIGJ == null) {
			txtNroIGJ = new CLJNumericTextField();
			if (getParametrosGenerales() != null && getParametrosGenerales().getNroIGJ() != null) {
				txtNroIGJ.setValue(getParametrosGenerales().getNroIGJ().longValue());
			}
		}
		return txtNroIGJ;
	}
	
	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	private CLJNumericTextField getTxtNroComienzoFacturaB() {
		if(txtNroComienzoFacturaB == null){
			txtNroComienzoFacturaB = new CLJNumericTextField();
			if (getParametrosGenerales() != null && getParametrosGenerales().getNroComienzoFacturaB() != null) {
				txtNroComienzoFacturaB.setValue(getParametrosGenerales().getNroComienzoFacturaB().longValue());
			}
		}
		return txtNroComienzoFacturaB;
	}
	
	private CLJNumericTextField getTxtNroComienzoOrdenDePago() {
		if(txtNroComienzoOrdenDePago == null){
			txtNroComienzoOrdenDePago = new CLJNumericTextField();
			if (getParametrosGenerales() != null && getParametrosGenerales().getNroComienzoOrdenDePago() != null) {
				txtNroComienzoOrdenDePago.setValue(getParametrosGenerales().getNroComienzoOrdenDePago().longValue());
			}
		}
		return txtNroComienzoOrdenDePago;
	}

	private CLJNumericTextField getTxtNroComienzoOrdenDeDeposito() {
		if(txtNroComienzoOrdenDeDeposito == null){
			txtNroComienzoOrdenDeDeposito = new CLJNumericTextField();
			if (getParametrosGenerales() != null && getParametrosGenerales().getNroComienzoOrdenDeDeposito() != null) {
				txtNroComienzoOrdenDeDeposito.setValue(getParametrosGenerales().getNroComienzoOrdenDeDeposito().longValue());
			}
		}
		return txtNroComienzoOrdenDeDeposito;
	}

	private JPanel getPanelTabCheques() {
		if(panelTabCheques == null){
			panelTabCheques = new JPanel();
			panelTabCheques.setLayout(new GridBagLayout());
			panelTabCheques.add(new JLabel("Letra inicial cheque: "), createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelTabCheques.add(getTxtLetraCheque(), createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTabCheques.add(new JLabel("Número inicial cheque: "), createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelTabCheques.add(getTxtNumeroCheque(), createGridBagConstraints(1,1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTabCheques.add(new JLabel("Aviso de vencimiento de cheques (días): "), createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1,0, 0));
			panelTabCheques.add(getTxtDiasAvisoVencimientoCheque(), createGridBagConstraints(1, 2, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTabCheques.add(new JLabel("Los cheques vencen a los (días): "), createGridBagConstraints(0, 3, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1,0, 0));
			panelTabCheques.add(getTxtDiasVencenCheques(), createGridBagConstraints(1, 3, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));

		}
		return panelTabCheques;
	}
	
	private JPanel getPanelTabFacturacion() {
		if(panelTabFacturacion == null){
			panelTabFacturacion = new JPanel();
			panelTabFacturacion.setLayout(new GridBagLayout());
			
			JPanel pnlBotonesFacturas = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
			if(!GenericUtils.isSistemaTest()){
				pnlBotonesFacturas.add(getBtnConfigurarNumeracionA());
			}else{
				pnlBotonesFacturas.add(getBtnConfigurarNumeracionB());
			}
			
			panelTabFacturacion.add(pnlBotonesFacturas, createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			
//			panelTabFacturacion.add(new JLabel("Comienzo de factura A: "), createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
//			panelTabFacturacion.add(getTxtNroComienzoFactura(), createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
//			panelTabFacturacion.add(new JLabel("Comienzo de factura B: "), createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1,0, 0));
//			panelTabFacturacion.add(getTxtNroComienzoFacturaB(), createGridBagConstraints(1, 2, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTabFacturacion.add(new JLabel("Comienzo de remito: "), createGridBagConstraints(0, 3, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelTabFacturacion.add(getTxtNroComienzoRemito(), createGridBagConstraints(1, 3, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTabFacturacion.add(new JLabel("Comienzo de recibo: "), createGridBagConstraints(0, 4, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelTabFacturacion.add(getTxtNroComienzoRecibo(), createGridBagConstraints(1, 4, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTabFacturacion.add(new JLabel("IVA Inscripto (%): "), createGridBagConstraints(0, 5, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelTabFacturacion.add(getTxtPorcentajeIvaInscripto(), createGridBagConstraints(1, 5, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTabFacturacion.add(new JLabel("IVA No Inscripto (%): "), createGridBagConstraints(0, 6, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelTabFacturacion.add(getTxtPorcentajeIvaNoInscripto(), createGridBagConstraints(1, 6, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTabFacturacion.add(new JLabel("Seguro de mercadoría (%): "), createGridBagConstraints(0, 7, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelTabFacturacion.add(getTxtPorcentajeSeguroMercaderia(), createGridBagConstraints(1, 7, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTabFacturacion.add(new JLabel("Precio de los tubos ($): "), createGridBagConstraints(0, 8, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelTabFacturacion.add(getTxtPrecioTubo(), createGridBagConstraints(1, 8, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTabFacturacion.add(new JLabel("Número de IGJ: "), createGridBagConstraints(0, 9, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1,0, 0));
			panelTabFacturacion.add(getTxtNroIGJ(), createGridBagConstraints(1, 9, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTabFacturacion.add(new JLabel("Número de sucursal: "), createGridBagConstraints(0, 10, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelTabFacturacion.add(getTxtNroSucursal(), createGridBagConstraints(1, 10, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
		}
		return panelTabFacturacion;
	}
	
	private JPanel getPanelTabVarios() {
		if(panelTabVarios == null){
			panelTabVarios = new JPanel();
			panelTabVarios.setLayout(new GridBagLayout());
			panelTabVarios.add(new JLabel("Comienzo de ODT: "), createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1,0, 0));
			panelTabVarios.add(getTxtNroComienzoODT(), createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTabVarios.add(new JLabel("Comienzo de orden de pago: "), createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1,0, 0));
			panelTabVarios.add(getTxtNroComienzoOrdenDePago(), createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTabVarios.add(new JLabel("Comienzo de orden de depósito: "), createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1,0, 0));
			panelTabVarios.add(getTxtNroComienzoOrdenDeDeposito(), createGridBagConstraints(1, 2, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTabVarios.add(new JLabel("Tolerancia de merma negativa (%): "), createGridBagConstraints(0, 3, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelTabVarios.add(getTxtPorcentajeMerma(), createGridBagConstraints(1, 3, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTabVarios.add(new JLabel("Banco predeterminado: "), createGridBagConstraints(0, 4, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelTabVarios.add(getCmbBancos(), createGridBagConstraints(1, 4, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTabVarios.add(new JLabel("Monto de aviso de deuda: "), createGridBagConstraints(0, 5, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelTabVarios.add(getTxtMinimoDeuda(), createGridBagConstraints(1, 5, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTabVarios.add(new JLabel("Comienzo de orden de pago a persona: "), createGridBagConstraints(0, 6, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelTabVarios.add(getTxtNroComienzoOrdenDePagoPersona(), createGridBagConstraints(1, 6, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTabVarios.add(new JLabel("Carga mínima color: "), createGridBagConstraints(0, 7, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelTabVarios.add(getTxtCargaMinimaColor(), createGridBagConstraints(1, 7, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTabVarios.add(new JLabel("Carga mínima estampado: "), createGridBagConstraints(0, 8, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelTabVarios.add(getTxtCargaMinimaEstampado(), createGridBagConstraints(1, 8, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTabVarios.add(new JLabel("Validez cotizaciones: "), createGridBagConstraints(0, 9, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelTabVarios.add(getTxtValidezCotizaciones(), createGridBagConstraints(1, 9, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTabVarios.add(new JLabel("Monto mínimo validacion precio: "), createGridBagConstraints(0, 10, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelTabVarios.add(getTxtMontoMinimoValidacionPrecio(), createGridBagConstraints(1, 10, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTabVarios.add(new JLabel("Monto máximo validacion precio: "), createGridBagConstraints(0, 11, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelTabVarios.add(getTxtMontoMaximoValidacionPrecio(), createGridBagConstraints(1, 11, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
		}
		
		return panelTabVarios;
	}

	private BancoFacadeRemote getBancoFacade() {
		if(bancoFacade == null){
			bancoFacade = GTLBeanFactory.getInstance().getBean2(BancoFacadeRemote.class);
		}
		return bancoFacade;
	}

	private JComboBox getCmbBancos() {
		if(cmbBancos == null){
			cmbBancos = new JComboBox();
			GuiUtil.llenarCombo(cmbBancos, getBancoFacade().getAllOrderByName(), true);
			if(getParametrosGenerales()!=null && getParametrosGenerales().getBancoDefault()!=null){
				cmbBancos.setSelectedItem(getParametrosGenerales().getBancoDefault());
			}
		}
		return cmbBancos;
	}
	
	private CLJTextField getTxtMinimoDeuda() {
		if(txtMinimoDeuda == null){
			txtMinimoDeuda = new CLJTextField();
			if(getParametrosGenerales()!=null && getParametrosGenerales().getUmbralDeuda()!=null){
				txtMinimoDeuda.setText(GenericUtils.getDecimalFormat().format(getParametrosGenerales().getUmbralDeuda()));
			}
		}
		return txtMinimoDeuda;
	}

	private CLJNumericTextField getTxtNroComienzoOrdenDePagoPersona() {
		if(txtNroComienzoOrdenDePagoPersona == null){
			txtNroComienzoOrdenDePagoPersona = new CLJNumericTextField();
			if (getParametrosGenerales() != null && getParametrosGenerales().getNroComienzoOrdenDePagoPersona() != null) {
				txtNroComienzoOrdenDePagoPersona.setValue(getParametrosGenerales().getNroComienzoOrdenDePagoPersona().longValue());
			}
		}
		return txtNroComienzoOrdenDePagoPersona;
	}
	
	public JButton getBtnConfigurarNumeracionA() {
		if(btnConfigurarNumeracionA == null){
			btnConfigurarNumeracionA = new JButton("Numeración Facturas A");
			btnConfigurarNumeracionA.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JDialogAgregarModificarNumeracionesFactura d = null;
					if(getParametrosGenerales().getConfiguracionFacturaA()==null){
						d = new JDialogAgregarModificarNumeracionesFactura(JDialogParametrosGenerales.this, ETipoFactura.A);
					}else{
						d = new JDialogAgregarModificarNumeracionesFactura(JDialogParametrosGenerales.this, getParametrosGenerales().getConfiguracionFacturaA());
					}
					d.setVisible(true);
					if(d.isAcepto()){
						getParametrosGenerales().setConfiguracionFacturaA(d.getConfiguracionActual());
					}
				}
			});
		}
		return btnConfigurarNumeracionA;
	}
	
	public JButton getBtnConfigurarNumeracionB() {
		if(btnConfigurarNumeracionB == null){
			btnConfigurarNumeracionB = new JButton("Numeración Facturas B");
			btnConfigurarNumeracionB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JDialogAgregarModificarNumeracionesFactura d = null;
					if(getParametrosGenerales().getConfiguracionFacturaB()==null){
						d = new JDialogAgregarModificarNumeracionesFactura(JDialogParametrosGenerales.this, ETipoFactura.B);
					}else{
						d = new JDialogAgregarModificarNumeracionesFactura(JDialogParametrosGenerales.this, getParametrosGenerales().getConfiguracionFacturaB());
					}					
					d.setVisible(true);
					if(d.isAcepto()){
						getParametrosGenerales().setConfiguracionFacturaB(d.getConfiguracionActual());
					}
				}
			});
		}
		return btnConfigurarNumeracionB;
	}

	public CLJNumericTextField getTxtValidezCotizaciones() {
		if (txtValidezCotizaciones == null) {
			txtValidezCotizaciones = new CLJNumericTextField();
			if (getParametrosGenerales() != null && getParametrosGenerales().getValidezCotizaciones() != null) {
				txtValidezCotizaciones.setValue(getParametrosGenerales().getValidezCotizaciones().longValue());
			}
		}
		return txtValidezCotizaciones;
	}

	public DecimalNumericTextField getTxtMontoMinimoValidacionPrecio() {
		if (txtMontoMinimoValidacionPrecio == null) {
			txtMontoMinimoValidacionPrecio = new DecimalNumericTextField(new Integer(2), new Integer(2));
			if (getParametrosGenerales() != null && getParametrosGenerales().getMontoMinimoValidacionPrecio()!= null) {
				txtMontoMinimoValidacionPrecio.setValue(getParametrosGenerales().getMontoMinimoValidacionPrecio().doubleValue());
			}
		}
		return txtMontoMinimoValidacionPrecio;
	}

	public DecimalNumericTextField getTxtMontoMaximoValidacionPrecio() {
		if (txtMontoMaximoValidacionPrecio == null) {
			txtMontoMaximoValidacionPrecio = new DecimalNumericTextField(new Integer(2), new Integer(2));
			if (getParametrosGenerales() != null && getParametrosGenerales().getMontoMaximoValidacionPrecio()!= null) {
				txtMontoMaximoValidacionPrecio.setValue(getParametrosGenerales().getMontoMaximoValidacionPrecio().doubleValue());
			}
		}
		return txtMontoMaximoValidacionPrecio;
	}
}

package ar.com.textillevel.gui.modulos.odt.gui.maquinas;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.facade.api.remote.TipoArticuloFacadeRemote;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidadorCamposMaquinaHandler;
import ar.com.textillevel.gui.util.controles.DecimalNumericTextField;
import ar.com.textillevel.gui.util.panels.PanelSeleccionarElementos;
import ar.com.textillevel.util.GTLBeanFactory;

public abstract class PanDatosMaquinaCommon extends JPanel {

	private static final long serialVersionUID = 1L;

	protected final String LABEL_ANCHO_MINIMO = "ANCHO MÍNIMO (MTS)";
	protected final String LABEL_ANCHO_MAXIMO = "ANCHO MÁXIMO (MTS)";
	protected final String LABEL_POTENCIA = "POTENCIA (HP)";
	protected final String LABEL_LITROS_MINIMA = "CANT. LITROS MÍN.";
	protected final String LABEL_LITROS_MAXIMA = "CANT. LITROS MÁX.";
	protected final String LABEL_LITROS_REG = "CANT. LITROS REG.";
	protected final String LABEL_VEL_MINIMA = "VEL. MÍN. (MTS/MIN)";
	protected final String LABEL_VEL_MAXIMA = "VEL. MÁX. (MTS/MIN)";
	protected final String LABEL_VEL_PROM = "VEL. PROM. (MTS/MIN)";

	
	private DecimalNumericTextField txtAnchoMin;
	private DecimalNumericTextField txtAnchoMax;
	private DecimalNumericTextField txtPotencia;
	private DecimalNumericTextField txtCantLitrosMin;
	private DecimalNumericTextField txtCantLitrosMax;
	private DecimalNumericTextField txtCantLitrosProm;
	private DecimalNumericTextField txtVelMin;
	private DecimalNumericTextField txtVelMax;
	private DecimalNumericTextField txtVelProm;
	private PanelSeleccionarElementos<TipoArticulo> panSelTipoArticulos;
	protected List<TipoArticulo> allTipoArticulos;

	private TipoArticuloFacadeRemote tipoArticuloFacade;

	public PanDatosMaquinaCommon() {
		super();
		this.allTipoArticulos = getTipoArticuloFacade().getAllTipoArticulos();
	}

	protected DecimalNumericTextField getTxtAnchoMin() {
		if(txtAnchoMin == null) {
			txtAnchoMin = new DecimalNumericTextField(new Integer(2), new Integer(2));
		}
		return txtAnchoMin;
	}

	protected DecimalNumericTextField getTxtAnchoMax() {
		if(txtAnchoMax == null) {
			txtAnchoMax = new DecimalNumericTextField(new Integer(2), new Integer(2));
		}
		return txtAnchoMax;
	}

	protected DecimalNumericTextField getTxtPotencia() {
		if(txtPotencia == null) {
			txtPotencia = new DecimalNumericTextField(new Integer(2), new Integer(2));
		}
		return txtPotencia;
	}

	protected DecimalNumericTextField getTxtCantLitrosMin() {
		if(txtCantLitrosMin == null) {
			txtCantLitrosMin = new DecimalNumericTextField(new Integer(2), new Integer(2));
		}
		return txtCantLitrosMin;
	}

	protected DecimalNumericTextField getTxtCantLitrosMax() {
		if(txtCantLitrosMax == null) {
			txtCantLitrosMax = new DecimalNumericTextField(new Integer(2), new Integer(2));
		}
		return txtCantLitrosMax;
	}

	protected DecimalNumericTextField getTxtCantLitrosProm() {
		if(txtCantLitrosProm == null) {
			txtCantLitrosProm = new DecimalNumericTextField(new Integer(2), new Integer(2));
		}
		return txtCantLitrosProm;
	}

	protected DecimalNumericTextField getTxtVelMin() {
		if(txtVelMin == null) {
			txtVelMin = new DecimalNumericTextField(new Integer(2), new Integer(2));
		}
		return txtVelMin;
	}

	protected DecimalNumericTextField getTxtVelMax() {
		if(txtVelMax == null) {
			txtVelMax = new DecimalNumericTextField(new Integer(2), new Integer(2));
		}
		return txtVelMax;
	}

	protected DecimalNumericTextField getTxtVelProm() {
		if(txtVelProm == null) {
			txtVelProm = new DecimalNumericTextField(new Integer(2), new Integer(2));
		}
		return txtVelProm;
	}

	protected PanelSeleccionarElementos<TipoArticulo> getPanSelTipoArticulos() {
		if(panSelTipoArticulos == null) {
			panSelTipoArticulos = new PanelSeleccionarElementos<TipoArticulo>(null, new ArrayList<TipoArticulo>(), "Tipos de Artículos:");
		}
		return panSelTipoArticulos;
	}

	protected void setValue(DecimalNumericTextField textField, Float value) {
		if(value != null) {
			textField.setValue(value.doubleValue());
		}
	}

	protected TipoArticuloFacadeRemote getTipoArticuloFacade() {
		if(tipoArticuloFacade == null) {
			tipoArticuloFacade = GTLBeanFactory.getInstance().getBean2(TipoArticuloFacadeRemote.class);
		}
		return tipoArticuloFacade;
	}

	protected JLabel createLabel(String label) {
		return new JLabel(" " + label + ": ");
	}

	protected boolean validarTextField(DecimalNumericTextField txt, String campo) {
		String texto = txt.getText();
		if(StringUtil.isNullOrEmpty(texto)) {
			FWJOptionPane.showErrorMessage(this.getParent(), "Falta completar el campo '" + campo + "'", "Error");
			txt.requestFocus();
			return false;
		}
		return true;
	}

	public boolean validar() {
		ValidadorCamposMaquinaHandler handler = configureValidadorHandler();
		return handler.validar();
	}

	public abstract ValidadorCamposMaquinaHandler configureValidadorHandler();

	public void limpiarDatos() {
		getTxtAnchoMax().setText(null);
		getTxtAnchoMin().setText(null);
		getTxtCantLitrosMax().setText(null);
		getTxtCantLitrosMin().setText(null);
		getTxtCantLitrosProm().setText(null);
		getTxtPotencia().setText(null);
		getTxtVelMax().setText(null);
		getTxtVelMin().setText(null);
		getTxtVelProm().setText(null);
	}

}
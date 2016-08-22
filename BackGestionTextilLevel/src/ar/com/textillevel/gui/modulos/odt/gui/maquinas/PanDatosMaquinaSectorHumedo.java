package ar.com.textillevel.gui.modulos.odt.gui.maquinas;

import static ar.com.textillevel.gui.util.GenericUtils.getFloatValueInJTextField;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JCheckBox;

import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidacionCompletitud;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidacionMayorQueCero;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidacionPanelSeleccionarElementos;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidacionRangoTextFields;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidacionTernaTextFields;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidadorCamposMaquinaHandler;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.DecimalNumericTextField;
import ar.com.textillevel.modulos.odt.entidades.maquinas.MaquinaSectorHumedo;

public class PanDatosMaquinaSectorHumedo extends PanDatosMaquinaCommon {

	private static final long serialVersionUID = 1L;

	private static final String LABEL_CARGA_MIN = "CARGA MÍN. (KG)";
	private static final String LABEL_CARGA_MAX = "CARGA MÁX. (KG)";
	private static final String LABEL_DIAM_TEJ_MAX = "DIÁM. TEJIDO MÁX. (CM)";
	private static final String LABEL_DIAM_CIL_MAX = "DIÁM. CILINDRO MÁX. (CM)";
	
	private JCheckBox chkAltaTemperatura;
	private DecimalNumericTextField txtCargaMin;
	private DecimalNumericTextField txtCargaMax;
	private DecimalNumericTextField txtDiamTejidoMax;
	private DecimalNumericTextField txtDiamCilindroMax;
	
	private MaquinaSectorHumedo maquina;

	public PanDatosMaquinaSectorHumedo() {
		super();
		construct();
	}

	private void construct() {
		setLayout(new GridBagLayout());

		add(getPanSelTipoArticulos(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 6, 1, 1, 0));

		add(createLabel(LABEL_ANCHO_MINIMO), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(getTxtAnchoMin(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.3, 0));		
		add(createLabel(LABEL_ANCHO_MAXIMO), GenericUtils.createGridBagConstraints(2, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(getTxtAnchoMax(), GenericUtils.createGridBagConstraints(3, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.3, 0));
		add(createLabel(LABEL_POTENCIA), GenericUtils.createGridBagConstraints(4, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(getTxtPotencia(), GenericUtils.createGridBagConstraints(5, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.3, 0));

		add(createLabel(LABEL_LITROS_MINIMA), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtCantLitrosMin(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(createLabel(LABEL_LITROS_MAXIMA), GenericUtils.createGridBagConstraints(2, 2,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtCantLitrosMax(), GenericUtils.createGridBagConstraints(3, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(createLabel(LABEL_LITROS_REG), GenericUtils.createGridBagConstraints(4, 2,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(getTxtCantLitrosProm(), GenericUtils.createGridBagConstraints(5, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 5, 1, 0, 0));

		add(createLabel(LABEL_VEL_MINIMA), GenericUtils.createGridBagConstraints(0, 3,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtVelMin(), GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(createLabel(LABEL_VEL_MAXIMA), GenericUtils.createGridBagConstraints(2, 3,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtVelMax(), GenericUtils.createGridBagConstraints(3, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(createLabel(LABEL_VEL_PROM), GenericUtils.createGridBagConstraints(4, 3,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(getTxtVelProm(), GenericUtils.createGridBagConstraints(5, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 5, 1, 0, 0));

		add(getChkAltaTemperatura(), GenericUtils.createGridBagConstraints(0, 4,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 2, 1, 0, 0));
		add(createLabel(LABEL_CARGA_MIN), GenericUtils.createGridBagConstraints(2, 4,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtCargaMin(), GenericUtils.createGridBagConstraints(3, 4, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(createLabel(LABEL_CARGA_MAX), GenericUtils.createGridBagConstraints(4, 4,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(getTxtCargaMax(), GenericUtils.createGridBagConstraints(5, 4, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 5, 1, 0, 0));

		add(createLabel(LABEL_DIAM_TEJ_MAX), GenericUtils.createGridBagConstraints(2, 5,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(getTxtDiamTejidoMax(), GenericUtils.createGridBagConstraints(3, 5, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(createLabel(LABEL_DIAM_CIL_MAX), GenericUtils.createGridBagConstraints(4, 5,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(getTxtDiamCilindroMax(), GenericUtils.createGridBagConstraints(5, 5, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
	}

	private JCheckBox getChkAltaTemperatura() {
		if(chkAltaTemperatura == null) {
			chkAltaTemperatura = new JCheckBox("ALTA TEMPERATURA");
		}
		return chkAltaTemperatura;
	}

	private DecimalNumericTextField getTxtCargaMin() {
		if(txtCargaMin == null) {
			txtCargaMin = new DecimalNumericTextField(new Integer(2), new Integer(2));
		}
		return txtCargaMin;
	}

	private DecimalNumericTextField getTxtCargaMax() {
		if(txtCargaMax == null) {
			txtCargaMax = new DecimalNumericTextField(new Integer(2), new Integer(2));
		}
		return txtCargaMax;
	}

	private DecimalNumericTextField getTxtDiamTejidoMax() {
		if(txtDiamTejidoMax == null) {
			txtDiamTejidoMax = new DecimalNumericTextField(new Integer(2), new Integer(2));
		}
		return txtDiamTejidoMax;
	}

	private DecimalNumericTextField getTxtDiamCilindroMax() {
		if(txtDiamCilindroMax == null) {
			txtDiamCilindroMax = new DecimalNumericTextField(new Integer(2), new Integer(2));
		}
		return txtDiamCilindroMax;
	}

	public MaquinaSectorHumedo getMaquinaConDatosSeteados() {
		maquina.setAnchoMin(getFloatValueInJTextField(getTxtAnchoMin()));
		maquina.setAnchoMax(getFloatValueInJTextField(getTxtAnchoMax()));
		maquina.setPotencia(getFloatValueInJTextField(getTxtPotencia()));
		maquina.setCantLitrosMin(getFloatValueInJTextField(getTxtCantLitrosMin()));
		maquina.setCantLitrosMax(getFloatValueInJTextField(getTxtCantLitrosMax()));
		maquina.setCantLitrosRegular(getFloatValueInJTextField(getTxtCantLitrosProm()));
		maquina.setVelocidadMin(getFloatValueInJTextField(getTxtVelMin()));
		maquina.setVelocidadMax(getFloatValueInJTextField(getTxtVelMax()));
		maquina.setVelocidadProm(getFloatValueInJTextField(getTxtVelProm()));
		maquina.setAltaTemperatura(getChkAltaTemperatura().isSelected());
		maquina.setCapacidadCargaMin(getFloatValueInJTextField(getTxtCargaMin()));
		maquina.setCapacidadCargaMax(getFloatValueInJTextField(getTxtCargaMax()));
		maquina.setDiamCilindroMax(getFloatValueInJTextField(getTxtDiamCilindroMax()));
		maquina.setDiamTejidoMax(getFloatValueInJTextField(getTxtDiamTejidoMax()));
		maquina.getTipoArticulos().clear();
		maquina.getTipoArticulos().addAll(getPanSelTipoArticulos().getSelectedElements());
		return maquina;
	}

	public void setMaquina(MaquinaSectorHumedo maquina) {
		this.maquina = maquina;
		setValue(getTxtAnchoMin(), maquina.getAnchoMin());
		setValue(getTxtAnchoMax(), maquina.getAnchoMax());
		setValue(getTxtPotencia(), maquina.getPotencia());
		setValue(getTxtCantLitrosMin(), maquina.getCantLitrosMin());
		setValue(getTxtCantLitrosMax(), maquina.getCantLitrosMax());
		setValue(getTxtCantLitrosProm(), maquina.getCantLitrosRegular());
		setValue(getTxtVelMin(), maquina.getVelocidadMin());
		setValue(getTxtVelMax(), maquina.getVelocidadMax());
		setValue(getTxtVelProm(), maquina.getVelocidadProm());
		getChkAltaTemperatura().setSelected(maquina.getAltaTemperatura() != null && maquina.getAltaTemperatura());
		setValue(getTxtCargaMin(), maquina.getCapacidadCargaMin());
		setValue(getTxtCargaMax(), maquina.getCapacidadCargaMax());
		setValue(getTxtDiamTejidoMax(), maquina.getDiamTejidoMax());
		setValue(getTxtDiamCilindroMax(), maquina.getDiamCilindroMax());
		getPanSelTipoArticulos().setElementsAndSelectedElements(allTipoArticulos, new ArrayList<TipoArticulo>(maquina.getTipoArticulos()));
	}

	public void limpiarDatos() {
		super.limpiarDatos();
		getTxtCargaMax().setText(null);
		getTxtCargaMin().setText(null);
		getTxtDiamCilindroMax().setText(null);
		getTxtDiamTejidoMax().setText(null);
		getChkAltaTemperatura().setSelected(false);
		getPanSelTipoArticulos().setElementsAndSelectedElements(allTipoArticulos, new ArrayList<TipoArticulo>());
	}

	@Override
	public ValidadorCamposMaquinaHandler configureValidadorHandler() {
		ValidadorCamposMaquinaHandler handler = new ValidadorCamposMaquinaHandler();

		handler.addValidacion(new ValidacionPanelSeleccionarElementos<TipoArticulo>(this.getParent(), getPanSelTipoArticulos()));
		
		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtAnchoMin(), LABEL_ANCHO_MINIMO));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtAnchoMin(), LABEL_ANCHO_MINIMO));
		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtAnchoMax(), LABEL_ANCHO_MAXIMO));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtAnchoMax(), LABEL_ANCHO_MAXIMO));
		handler.addValidacion(new ValidacionRangoTextFields(this.getParent(), getTxtAnchoMin(), LABEL_ANCHO_MINIMO, getTxtAnchoMax(), LABEL_ANCHO_MAXIMO));

		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtPotencia(), LABEL_POTENCIA));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtPotencia(), LABEL_POTENCIA));

		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtCantLitrosMin(), LABEL_LITROS_MINIMA));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtCantLitrosMin(), LABEL_LITROS_MINIMA));
		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtCantLitrosMax(), LABEL_LITROS_MAXIMA));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtCantLitrosMax(), LABEL_LITROS_MAXIMA));
		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtCantLitrosProm(), LABEL_LITROS_REG));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtCantLitrosProm(), LABEL_LITROS_REG));
		handler.addValidacion(new ValidacionTernaTextFields(this.getParent(), getTxtCantLitrosMin(), LABEL_LITROS_MINIMA, getTxtCantLitrosProm(), LABEL_LITROS_REG, getTxtCantLitrosMax(), LABEL_LITROS_MAXIMA));

		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtVelMin(), LABEL_VEL_MINIMA));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtVelMin(), LABEL_VEL_MINIMA));
		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtVelMax(), LABEL_VEL_MAXIMA));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtVelMax(), LABEL_VEL_MAXIMA));
		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtVelProm(), LABEL_VEL_PROM));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtVelProm(), LABEL_VEL_PROM));
		handler.addValidacion(new ValidacionTernaTextFields(this.getParent(), getTxtVelMin(), LABEL_VEL_MINIMA, getTxtVelProm(), LABEL_VEL_PROM, getTxtVelMax(), LABEL_VEL_MAXIMA));

		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtCargaMin(), LABEL_CARGA_MIN));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtCargaMin(), LABEL_CARGA_MIN));
		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtCargaMax(), LABEL_CARGA_MAX));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtCargaMax(), LABEL_CARGA_MAX));
		handler.addValidacion(new ValidacionRangoTextFields(this.getParent(), getTxtCargaMin(), LABEL_CARGA_MIN, getTxtCargaMax(), LABEL_CARGA_MAX));

		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtDiamTejidoMax(), LABEL_DIAM_TEJ_MAX));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtDiamTejidoMax(), LABEL_DIAM_TEJ_MAX));

		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtDiamCilindroMax(), LABEL_DIAM_CIL_MAX));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtDiamCilindroMax(), LABEL_DIAM_CIL_MAX));

		return handler;
	}

}
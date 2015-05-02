package ar.com.textillevel.gui.modulos.odt.gui.maquinas;

import static ar.com.textillevel.gui.util.GenericUtils.getFloatValueInJTextField;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidacionCompletitud;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidacionMayorQueCero;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidacionPanelSeleccionarElementos;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidacionRangoTextFields;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidacionTernaTextFields;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidadorCamposMaquinaHandler;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.DecimalNumericTextField;
import ar.com.textillevel.modulos.odt.entidades.maquinas.MaquinaSectorSeco;

public class PanDatosMaquinaSectorSeco extends PanDatosMaquinaCommon {

	private static final long serialVersionUID = 1L;
	
	private static final String LABEL_LITROS_FOULARD_MIN = "LITROS FOULARD MÍN.";
	private static final String LABEL_LITROS_FOULARD_MAX = "LITROS FOULARD MÁX.";
	private static final String LABEL_LITROS_FOULARD_REG = "LITROS FOULARD REG.";

	private static final String LABEL_TEMP_MIN = "TEMP. MÍN. (ºC)";
	private static final String LABEL_TEMP_MAX = "TEMP. MÁX. (ºC)";
	private static final String LABEL_TEMP_PROM = "TEMP. PROM. (ºC)";
	private static final String LABEL_CANT_CAMPOS = "CANT. DE CAMPOS";

	private CLJNumericTextField txtCantCampos;

	private DecimalNumericTextField txtTempMin;
	private DecimalNumericTextField txtTempMax;
	private DecimalNumericTextField txtTempProm;

	private MaquinaSectorSeco maquina;

	public PanDatosMaquinaSectorSeco() {
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

		add(createLabel(LABEL_LITROS_FOULARD_MIN), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtCantLitrosMin(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(createLabel(LABEL_LITROS_FOULARD_MAX), GenericUtils.createGridBagConstraints(2, 2,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtCantLitrosMax(), GenericUtils.createGridBagConstraints(3, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(createLabel(LABEL_LITROS_FOULARD_REG), GenericUtils.createGridBagConstraints(4, 2,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(getTxtCantLitrosProm(), GenericUtils.createGridBagConstraints(5, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 5, 1, 0, 0));

		add(createLabel(LABEL_VEL_MINIMA), GenericUtils.createGridBagConstraints(0, 3,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtVelMin(), GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(createLabel(LABEL_VEL_MAXIMA), GenericUtils.createGridBagConstraints(2, 3,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtVelMax(), GenericUtils.createGridBagConstraints(3, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(createLabel(LABEL_VEL_PROM), GenericUtils.createGridBagConstraints(4, 3,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(getTxtVelProm(), GenericUtils.createGridBagConstraints(5, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 5, 1, 0, 0));

		add(createLabel(LABEL_TEMP_MIN), GenericUtils.createGridBagConstraints(0, 4,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtTempMin(), GenericUtils.createGridBagConstraints(1, 4, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(createLabel(LABEL_TEMP_MAX), GenericUtils.createGridBagConstraints(2, 4,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtTempMax(), GenericUtils.createGridBagConstraints(3, 4, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(createLabel(LABEL_TEMP_PROM), GenericUtils.createGridBagConstraints(4, 4,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtTempProm(), GenericUtils.createGridBagConstraints(5, 4, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));

		add(createLabel(LABEL_CANT_CAMPOS), GenericUtils.createGridBagConstraints(0, 5,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtCantCampos(), GenericUtils.createGridBagConstraints(1, 5, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
	}

	private CLJNumericTextField getTxtCantCampos() {
		if(txtCantCampos == null) {
			txtCantCampos = new CLJNumericTextField();
		}
		return txtCantCampos;
	}

	private DecimalNumericTextField getTxtTempMin() {
		if(txtTempMin == null) {
			txtTempMin = new DecimalNumericTextField();
		}
		return txtTempMin;
	}

	private DecimalNumericTextField getTxtTempMax() {
		if(txtTempMax == null) {
			txtTempMax = new DecimalNumericTextField();
		}
		return txtTempMax;
	}

	private DecimalNumericTextField getTxtTempProm() {
		if(txtTempProm == null) {
			txtTempProm = new DecimalNumericTextField();
		}
		return txtTempProm;
	}

	public void limpiarDatos() {
		super.limpiarDatos();
		getTxtCantCampos().setText(null);
		getTxtTempMax().setText(null);
		getTxtTempMin().setText(null);
		getTxtTempProm().setText(null);
		getPanSelTipoArticulos().setElementsAndSelectedElements(allTipoArticulos, new ArrayList<TipoArticulo>());
	}

	public void setMaquina(MaquinaSectorSeco mss) {
		this.maquina = mss;
		setValue(getTxtAnchoMin(), maquina.getAnchoMin());
		setValue(getTxtAnchoMax(), maquina.getAnchoMax());
		setValue(getTxtPotencia(), maquina.getPotencia());
		setValue(getTxtCantLitrosMin(), maquina.getCantLitrosFoulardMin());
		setValue(getTxtCantLitrosMax(), maquina.getCantLitrosFoulardMax());
		setValue(getTxtCantLitrosProm(), maquina.getCantLitrosFoulardProm());
		setValue(getTxtVelMin(), maquina.getVelocidadMin());
		setValue(getTxtVelMax(), maquina.getVelocidadMax());
		setValue(getTxtVelProm(), maquina.getVelocidadProm());
		setValue(getTxtTempMin(), maquina.getTemperaturaMin());
		setValue(getTxtTempMax(), maquina.getTemperaturaMax());
		setValue(getTxtTempProm(), maquina.getTemperaturaProm());
		getTxtCantCampos().setText(maquina.getCantCampos() == null ? "" : maquina.getCantCampos().toString());
		getPanSelTipoArticulos().setElementsAndSelectedElements(allTipoArticulos, new ArrayList<TipoArticulo>(maquina.getTipoArticulos()));
	}

	public MaquinaSectorSeco getMaquinaConDatosSeteados() {
		maquina.setAnchoMin(getFloatValueInJTextField(getTxtAnchoMin()));
		maquina.setAnchoMax(getFloatValueInJTextField(getTxtAnchoMax()));
		maquina.setPotencia(getFloatValueInJTextField(getTxtPotencia()));
		maquina.setCantLitrosFoulardMin(getFloatValueInJTextField(getTxtCantLitrosMin()));
		maquina.setCantLitrosFoulardMax(getFloatValueInJTextField(getTxtCantLitrosMax()));
		maquina.setCantLitrosFoulardProm(getFloatValueInJTextField(getTxtCantLitrosProm()));
		maquina.setVelocidadMin(getFloatValueInJTextField(getTxtVelMin()));
		maquina.setVelocidadMax(getFloatValueInJTextField(getTxtVelMax()));
		maquina.setVelocidadProm(getFloatValueInJTextField(getTxtVelProm()));
		maquina.setTemperaturaMin(getFloatValueInJTextField(getTxtTempMin()));
		maquina.setTemperaturaMax(getFloatValueInJTextField(getTxtTempMax()));
		maquina.setTemperaturaProm(getFloatValueInJTextField(getTxtTempProm()));
		maquina.setCantCampos(getTxtCantCampos().getValue());
		maquina.getTipoArticulos().clear();
		maquina.getTipoArticulos().addAll(getPanSelTipoArticulos().getSelectedElements());
		return maquina;
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

		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtCantLitrosMin(), LABEL_LITROS_FOULARD_MIN));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtCantLitrosMin(), LABEL_LITROS_FOULARD_MIN));
		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtCantLitrosMax(), LABEL_LITROS_FOULARD_MAX));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtCantLitrosMax(), LABEL_LITROS_FOULARD_MAX));
		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtCantLitrosProm(), LABEL_LITROS_FOULARD_REG));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtCantLitrosProm(), LABEL_LITROS_FOULARD_REG));
		handler.addValidacion(new ValidacionTernaTextFields(this.getParent(), getTxtCantLitrosMin(), LABEL_LITROS_FOULARD_MIN, getTxtCantLitrosProm(), LABEL_LITROS_FOULARD_REG, getTxtCantLitrosMax(), LABEL_LITROS_FOULARD_MAX));

		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtVelMin(), LABEL_VEL_MINIMA));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtVelMin(), LABEL_VEL_MINIMA));
		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtVelMax(), LABEL_VEL_MAXIMA));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtVelMax(), LABEL_VEL_MAXIMA));
		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtVelProm(), LABEL_VEL_PROM));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtVelProm(), LABEL_VEL_PROM));
		handler.addValidacion(new ValidacionTernaTextFields(this.getParent(), getTxtVelMin(), LABEL_VEL_MINIMA, getTxtVelProm(), LABEL_VEL_PROM, getTxtVelMax(), LABEL_VEL_MAXIMA));

		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtTempMin(), LABEL_TEMP_MIN));
		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtTempMax(), LABEL_TEMP_MAX));
		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtTempProm(), LABEL_TEMP_PROM));
		handler.addValidacion(new ValidacionTernaTextFields(this.getParent(), getTxtTempMin(), LABEL_TEMP_MIN, getTxtTempProm(), LABEL_TEMP_PROM, getTxtTempMax(), LABEL_TEMP_MAX));

		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtCantCampos(), LABEL_CANT_CAMPOS));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtCantCampos(), LABEL_CANT_CAMPOS));

		return handler;
	}
	
}
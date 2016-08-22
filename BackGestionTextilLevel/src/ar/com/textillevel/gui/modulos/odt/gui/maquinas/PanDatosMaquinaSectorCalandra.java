package ar.com.textillevel.gui.modulos.odt.gui.maquinas;

import static ar.com.textillevel.gui.util.GenericUtils.getFloatValueInJTextField;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidacionCompletitud;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidacionMayorQueCero;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidacionPanelSeleccionarElementos;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidacionRangoTextFields;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidacionTernaTextFields;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidadorCamposMaquinaHandler;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.DecimalNumericTextField;
import ar.com.textillevel.modulos.odt.entidades.maquinas.MaquinaSectorTerminadoCalandra;

public class PanDatosMaquinaSectorCalandra extends PanDatosMaquinaCommon {

	private static final long serialVersionUID = 1L;
	private static final String LABEL_VELOCIDAD = "VELOCIDAD (MTS/MIN)";
	private static final String LABEL_CANT_RODILLOS = "CANT. DE RODILLOS";
	private static final String LABEL_PASADAS_MIN = "PASADAS MÍN.";
	private static final String LABEL_PASADAS_MAX = "PASADAS MÁX.";
	private static final String LABEL_TEMP_MIN = "TEMP. MÍN. (ºC)";
	private static final String LABEL_TEMP_MAX = "TEMP. MÁX. (ºC)";
	private static final String LABEL_TEMP_PROM = "TEMP. PROM. (ºC)";
	private static final String LABEL_PRESION_TRAB_MAX = "PRESIÓN TRABAJO MÁX. (TN)";
	
	private FWJNumericTextField txtCantRodillos;
	private FWJNumericTextField txtCantPasadasMin;
	private FWJNumericTextField txtCantPasadasMax;
	private DecimalNumericTextField txtPresionTrabajoMax;
	private DecimalNumericTextField txtTempMin;
	private DecimalNumericTextField txtTempMax;
	private DecimalNumericTextField txtTempProm;
	
	private MaquinaSectorTerminadoCalandra maquina;

	public PanDatosMaquinaSectorCalandra() {
		super();
		construct();
	}

	private void construct() {
		setLayout(new GridBagLayout());

		add(getPanSelTipoArticulos(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 6, 1, 1, 0));

		add(createLabel(LABEL_CANT_RODILLOS), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(getTxtCantRodillos(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.3, 0));		
		add(createLabel(LABEL_PASADAS_MIN), GenericUtils.createGridBagConstraints(2, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(getTxtCantPasadasMin(), GenericUtils.createGridBagConstraints(3, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.3, 0));
		add(createLabel(LABEL_PASADAS_MAX), GenericUtils.createGridBagConstraints(4, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(getTxtCantPasadasMax(), GenericUtils.createGridBagConstraints(5, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.3, 0));

		add(createLabel(LABEL_TEMP_MIN), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtTempMin(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(createLabel(LABEL_TEMP_MAX), GenericUtils.createGridBagConstraints(2, 2,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtTempMax(), GenericUtils.createGridBagConstraints(3, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(createLabel(LABEL_TEMP_PROM), GenericUtils.createGridBagConstraints(4, 2,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtTempProm(), GenericUtils.createGridBagConstraints(5, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));

		add(createLabel(LABEL_PRESION_TRAB_MAX), GenericUtils.createGridBagConstraints(0, 3,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtPresionTrabajoMax(), GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(createLabel(LABEL_VELOCIDAD), GenericUtils.createGridBagConstraints(2, 3,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtVelMin(), GenericUtils.createGridBagConstraints(3, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(createLabel(LABEL_POTENCIA), GenericUtils.createGridBagConstraints(4, 3,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(getTxtPotencia(), GenericUtils.createGridBagConstraints(5, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
	}

	public MaquinaSectorTerminadoCalandra getMaquinaConDatosSeteados() {
		maquina.setCantRodillos(getTxtCantRodillos().getValue());
		maquina.setCantPasadasMin(getTxtCantPasadasMin().getValue());
		maquina.setCantPasadasMax(getTxtCantPasadasMax().getValue());
		maquina.setTemperaturaMin(getFloatValueInJTextField(getTxtTempMin()));
		maquina.setTemperaturaMax(getFloatValueInJTextField(getTxtTempMax()));
		maquina.setTemperaturaProm(getFloatValueInJTextField(getTxtTempProm()));
		maquina.setPresionTrabajoMax(getFloatValueInJTextField(getTxtPresionTrabajoMax()));
		maquina.setVelocidad(getFloatValueInJTextField(getTxtVelMin()));
		maquina.setPotencia(getFloatValueInJTextField(getTxtPotencia()));
		maquina.getTipoArticulos().clear();
		maquina.getTipoArticulos().addAll(getPanSelTipoArticulos().getSelectedElements());
		return maquina;
	}

	public void setMaquina(MaquinaSectorTerminadoCalandra maquina) {
		this.maquina = maquina;
		getTxtCantRodillos().setText(maquina.getCantRodillos() == null ? "" : maquina.getCantRodillos().toString());
		getTxtCantPasadasMin().setText(maquina.getCantPasadasMin() == null ? "" : maquina.getCantPasadasMin().toString());
		getTxtCantPasadasMax().setText(maquina.getCantPasadasMax() == null ? "" : maquina.getCantPasadasMax().toString());

		setValue(getTxtTempMin(), maquina.getTemperaturaMin());
		setValue(getTxtTempMax(), maquina.getTemperaturaMax());
		setValue(getTxtTempProm(), maquina.getTemperaturaProm());
		
		setValue(getTxtPresionTrabajoMax(), maquina.getPresionTrabajoMax());
		setValue(getTxtVelMin(), maquina.getVelocidad());
		setValue(getTxtPotencia(), maquina.getPotencia());

		getPanSelTipoArticulos().setElementsAndSelectedElements(allTipoArticulos, new ArrayList<TipoArticulo>(maquina.getTipoArticulos()));
	}

	public void limpiarDatos() {
		super.limpiarDatos();
		getTxtCantPasadasMax().setText(null);
		getTxtCantPasadasMin().setText(null);
		getTxtCantRodillos().setText(null);
		getTxtPresionTrabajoMax().setText(null);
		getTxtTempMax().setText(null);
		getTxtTempMin().setText(null);
		getTxtTempProm().setText(null);
		getTxtVelMin().setText(null);
		getTxtPotencia().setText(null);
		getPanSelTipoArticulos().setElementsAndSelectedElements(allTipoArticulos, new ArrayList<TipoArticulo>());
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

	private FWJNumericTextField getTxtCantRodillos() {
		if(txtCantRodillos == null) {
			txtCantRodillos = new FWJNumericTextField();
		}
		return txtCantRodillos;
	}

	private FWJNumericTextField getTxtCantPasadasMin() {
		if(txtCantPasadasMin == null) {
			txtCantPasadasMin = new FWJNumericTextField();
		}
		return txtCantPasadasMin;
	}

	private FWJNumericTextField getTxtCantPasadasMax() {
		if(txtCantPasadasMax == null) {
			txtCantPasadasMax = new FWJNumericTextField();
		}
		return txtCantPasadasMax;
	}

	private DecimalNumericTextField getTxtPresionTrabajoMax() {
		if(txtPresionTrabajoMax == null) {
			txtPresionTrabajoMax = new DecimalNumericTextField();
		}
		return txtPresionTrabajoMax;
	}

	@Override
	public ValidadorCamposMaquinaHandler configureValidadorHandler() {
		ValidadorCamposMaquinaHandler handler = new ValidadorCamposMaquinaHandler();

		handler.addValidacion(new ValidacionPanelSeleccionarElementos<TipoArticulo>(this.getParent(), getPanSelTipoArticulos()));

		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtCantRodillos(), LABEL_CANT_RODILLOS));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtCantRodillos(), LABEL_CANT_RODILLOS));

		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtCantPasadasMin(), LABEL_PASADAS_MIN));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtCantPasadasMin(), LABEL_PASADAS_MIN));
		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtCantPasadasMax(), LABEL_PASADAS_MAX));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtCantPasadasMax(), LABEL_PASADAS_MAX));
		handler.addValidacion(new ValidacionRangoTextFields(this.getParent(), getTxtCantPasadasMin(), LABEL_PASADAS_MIN, getTxtCantPasadasMax(), LABEL_PASADAS_MAX));

		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtTempMin(), LABEL_TEMP_MIN));
		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtTempMax(), LABEL_TEMP_MAX));
		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtTempProm(), LABEL_TEMP_PROM));
		handler.addValidacion(new ValidacionTernaTextFields(this.getParent(), getTxtTempMin(), LABEL_TEMP_MIN, getTxtTempProm(), LABEL_TEMP_PROM, getTxtTempMax(), LABEL_TEMP_MAX));
		
		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtPresionTrabajoMax(), LABEL_PRESION_TRAB_MAX));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtPresionTrabajoMax(), LABEL_PRESION_TRAB_MAX));

		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtVelMin(), LABEL_VEL_MINIMA));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtVelMin(), LABEL_VEL_MINIMA));

		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtPotencia(), LABEL_POTENCIA));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtPotencia(), LABEL_POTENCIA));
		
		return handler;
	}

}
package ar.com.textillevel.gui.modulos.odt.gui.maquinas;

import static ar.com.textillevel.gui.util.GenericUtils.getFloatValueInJTextField;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidacionCompletitud;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidacionMayorQueCero;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidacionRangoTextFields;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidadorCamposMaquinaHandler;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.maquinas.MaquinaSectorCosido;

public class PanDatosMaquinaSectorCosido extends PanDatosMaquinaCommon {

	private static final long serialVersionUID = 1L;
	private MaquinaSectorCosido maquina;

	public PanDatosMaquinaSectorCosido() {
		super();
		construct();
	}

	private void construct() {
		setLayout(new GridBagLayout());

		add(createLabel(LABEL_ANCHO_MINIMO), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(getTxtAnchoMin(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.3, 0));		
		add(createLabel(LABEL_ANCHO_MAXIMO), GenericUtils.createGridBagConstraints(2, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(getTxtAnchoMax(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.3, 0));
		add(createLabel(LABEL_POTENCIA), GenericUtils.createGridBagConstraints(4, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(getTxtPotencia(), GenericUtils.createGridBagConstraints(5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.3, 0));
	}

	public void setMaquina(MaquinaSectorCosido msc) {
		this.maquina = msc;
		setValue(getTxtAnchoMin(), maquina.getAnchoMin());
		setValue(getTxtAnchoMax(), maquina.getAnchoMax());
		setValue(getTxtPotencia(), maquina.getPotencia());
	}

	public MaquinaSectorCosido getMaquinaConDatosSeteados() {
		maquina.setAnchoMin(getFloatValueInJTextField(getTxtAnchoMin()));
		maquina.setAnchoMax(getFloatValueInJTextField(getTxtAnchoMax()));
		maquina.setPotencia(getFloatValueInJTextField(getTxtPotencia()));
		return maquina;
	}

	@Override
	public ValidadorCamposMaquinaHandler configureValidadorHandler() {
		ValidadorCamposMaquinaHandler handler = new ValidadorCamposMaquinaHandler();
		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtAnchoMin(), LABEL_ANCHO_MINIMO));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtAnchoMin(), LABEL_ANCHO_MINIMO));
		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtAnchoMax(), LABEL_ANCHO_MAXIMO));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtAnchoMax(), LABEL_ANCHO_MAXIMO));
		handler.addValidacion(new ValidacionRangoTextFields(this.getParent(), getTxtAnchoMin(), LABEL_ANCHO_MINIMO, getTxtAnchoMax(), LABEL_ANCHO_MAXIMO));
		handler.addValidacion(new ValidacionCompletitud(this.getParent(), getTxtPotencia(), LABEL_POTENCIA));
		handler.addValidacion(new ValidacionMayorQueCero(this.getParent(), getTxtPotencia(), LABEL_POTENCIA));
		return handler;
	}

}
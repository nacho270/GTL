package ar.com.textillevel.gui.modulos.odt.gui.maquinas;

import static ar.com.textillevel.gui.util.GenericUtils.getFloatValueInJTextField;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.FWRuntimeException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidacionComboElementoOtro;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidacionCompletitud;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidacionMayorQueCero;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidacionPanelSeleccionarElementos;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidacionRangoTextFields;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidadorCamposMaquinaHandler;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.panels.PanComboConElementoOtro;
import ar.com.textillevel.modulos.odt.entidades.maquinas.MaquinaSectorTerminadoFraccionado;
import ar.com.textillevel.modulos.odt.entidades.maquinas.TerminacionFraccionado;
import ar.com.textillevel.modulos.odt.facade.api.remote.MaquinaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class PanDatosMaquinaSectorTerminadoFraccionado extends PanDatosMaquinaCommon {

	private static final long serialVersionUID = 1L;

	private PanComboConElementoOtroTerminacion panComboTerminacion;
	private MaquinaSectorTerminadoFraccionado maquina;

	public PanDatosMaquinaSectorTerminadoFraccionado() {
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

		add(getPanComboTerminacion(), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 4, 1, 0, 0));
	}

	public MaquinaSectorTerminadoFraccionado getMaquinaConDatosSeteados() {
		maquina.setAnchoMin(getFloatValueInJTextField(getTxtAnchoMin()));
		maquina.setAnchoMax(getFloatValueInJTextField(getTxtAnchoMax()));
		maquina.setPotencia(getFloatValueInJTextField(getTxtPotencia()));
		if(getPanComboTerminacion().getSelectedItem() != null && getPanComboTerminacion().getSelectedItem().getId() != -1) {
			maquina.setTerminacion(getPanComboTerminacion().getSelectedItem());
		}
		maquina.getTipoArticulos().clear();
		maquina.getTipoArticulos().addAll(getPanSelTipoArticulos().getSelectedElements());
		return maquina;
	}

	public void setMaquina(MaquinaSectorTerminadoFraccionado maquina) {
		this.maquina = maquina;
		setValue(getTxtAnchoMin(), maquina.getAnchoMin());
		setValue(getTxtAnchoMax(), maquina.getAnchoMax());
		setValue(getTxtPotencia(), maquina.getPotencia());
		getPanSelTipoArticulos().setElementsAndSelectedElements(allTipoArticulos, new ArrayList<TipoArticulo>(maquina.getTipoArticulos()));
		getPanComboTerminacion().setSelectedItem(maquina.getTerminacion());
	}

	public void limpiarDatos() {
		super.limpiarDatos();
		getPanComboTerminacion().setSelectedItem(null);
		getPanSelTipoArticulos().setElementsAndSelectedElements(allTipoArticulos, new ArrayList<TipoArticulo>());
	}

	private PanComboConElementoOtroTerminacion getPanComboTerminacion() {
		if(panComboTerminacion == null) {
			List<TerminacionFraccionado> allTerminaciones = GTLBeanFactory.getInstance().getBean2(MaquinaFacadeRemote.class).getAllTerminaciones();
			TerminacionFraccionado otro = new TerminacionFraccionado();
			otro.setId(-1);
			otro.setNombre("OTRO");
			panComboTerminacion = new PanComboConElementoOtroTerminacion("TERMINACIÓN", allTerminaciones, otro);
		}
		return panComboTerminacion;
	}

	private static class PanComboConElementoOtroTerminacion extends PanComboConElementoOtro<TerminacionFraccionado> {

		private static final long serialVersionUID = 1L;

		public PanComboConElementoOtroTerminacion(String lblCombo, List<TerminacionFraccionado> items, TerminacionFraccionado itemOtro) {
			super(lblCombo, items, itemOtro);
		}

		@Override
		public TerminacionFraccionado itemOtroSelected() {
			String input = JOptionPane.showInputDialog(null, "Ingrese la Terminación: ", "Alta de terminación", JOptionPane.INFORMATION_MESSAGE);
			if(input == null){
				return null;
			} else {
				TerminacionFraccionado terminacion = new TerminacionFraccionado();
				terminacion.setNombre(input);
				try {
					return GTLBeanFactory.getInstance().getBean2(MaquinaFacadeRemote.class).save(terminacion);
				} catch (FWRuntimeException e) {
					FWJOptionPane.showErrorMessage(null, StringW.wordWrap(e.getMessage()), "Error");
				} catch (ValidacionException e) {
					FWJOptionPane.showErrorMessage(null, StringW.wordWrap(e.getMessage()), "Error");
				}
				return null;
			}
		}

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

		handler.addValidacion(new ValidacionComboElementoOtro<TerminacionFraccionado>(this.getParent(), getPanComboTerminacion()));
		
		return handler;
	}

}
package ar.com.textillevel.gui.modulos.odt.gui.estampado;

import java.awt.Dialog;

import javax.swing.JTabbedPane;

import ar.com.textillevel.entidades.ventas.materiaprima.Pigmento;
import ar.com.textillevel.entidades.ventas.materiaprima.Quimico;
import ar.com.textillevel.entidades.ventas.materiaprima.Reactivo;
import ar.com.textillevel.facade.api.remote.MateriaPrimaFacadeRemote;
import ar.com.textillevel.gui.modulos.odt.gui.PanTablaPigmentoCantidad;
import ar.com.textillevel.gui.modulos.odt.gui.PanTablaQuimicoCantidad;
import ar.com.textillevel.gui.modulos.odt.gui.PanTablaReactivoCantidad;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.estampado.FormulaEstampadoCliente;
import ar.com.textillevel.util.GTLBeanFactory;

public class TabPaneAnilinaPigmentoCantidad extends JTabbedPane {

	private static final long serialVersionUID = 1L;

	private Dialog owner;
	private FormulaEstampadoCliente formula;
	private PanTablaQuimicoCantidad panTablaQuimico;
	private PanTablaPigmentoCantidad panTablaPigmento;
	private PanTablaReactivoCantidad panTablaReactivo;
	private boolean modoConsulta;
	private MateriaPrimaFacadeRemote materiaPrimaFacade;

	public TabPaneAnilinaPigmentoCantidad(Dialog owner, FormulaEstampadoCliente formula, boolean modoConsulta) {
		super();
		this.owner = owner;
		this.modoConsulta = modoConsulta;
		this.formula = formula;
		construct();
	}

	private void construct() {
		panTablaQuimico = new PanTablaQuimicoCantidad(owner, "Químico", getMateriaPrimaFacade().getAllByClase(Quimico.class));
		panTablaQuimico.setModoConsulta(modoConsulta);
		add("Químicos", panTablaQuimico);
		panTablaPigmento = new PanTablaPigmentoCantidad(this, owner, "Pigmento", getMateriaPrimaFacade().getAllByClase(Pigmento.class));
		panTablaPigmento.setModoConsulta(modoConsulta);
		add("Pigmentos", panTablaPigmento);
		panTablaReactivo = new PanTablaReactivoCantidad(owner, "Reactivo", getMateriaPrimaFacade().getAllByClase(Reactivo.class));
		panTablaReactivo.setModoConsulta(modoConsulta);
		add("Reactivos", panTablaReactivo);
		if(formula != null) {
			llenarTabs(formula);
		}
	}

	public void llenarTabs(FormulaEstampadoCliente formula) {
		panTablaPigmento.agregarElementos(formula.getPigmentos());
		panTablaQuimico.agregarElementos(formula.getQuimicos());
		panTablaReactivo.agregarElementos(formula.getReactivos());
	}

	public String validar() {
		return null;
	}

	public FormulaEstampadoCliente setearDatosEnFormula() {
		if(formula != null) {
			formula.getPigmentos().clear();
			formula.getPigmentos().addAll(panTablaPigmento.getElementos());
			formula.getQuimicos().clear();
			formula.getQuimicos().addAll(panTablaQuimico.getElementos());
			formula.getReactivos().clear();
			formula.getReactivos().addAll(panTablaReactivo.getElementos());
		}
		return formula;
	}

	private MateriaPrimaFacadeRemote getMateriaPrimaFacade() {
		if(materiaPrimaFacade == null) {
			materiaPrimaFacade = GTLBeanFactory.getInstance().getBean2(MateriaPrimaFacadeRemote.class);
		}
		return materiaPrimaFacade;
	}


}
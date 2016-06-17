package ar.com.textillevel.gui.modulos.odt.gui.aprestado;

import java.awt.Dialog;

import javax.swing.JTabbedPane;

import ar.com.textillevel.entidades.ventas.materiaprima.Quimico;
import ar.com.textillevel.facade.api.remote.MateriaPrimaFacadeRemote;
import ar.com.textillevel.gui.modulos.odt.gui.PanTablaQuimicoCantidad;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.aprestado.FormulaAprestadoCliente;
import ar.com.textillevel.util.GTLBeanFactory;

public class TabPaneQuimicoCantidad extends JTabbedPane {

	private static final long serialVersionUID = 1L;

	private Dialog owner;
	private FormulaAprestadoCliente formula;
	private PanTablaQuimicoCantidad panTablaQuimico;
	private boolean modoConsulta;
	private MateriaPrimaFacadeRemote materiaPrimaFacade;

	public TabPaneQuimicoCantidad(Dialog owner, FormulaAprestadoCliente formula, boolean modoConsulta) {
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
		if(formula != null) {
			llenarTabs(formula);
		}
	}

	public void llenarTabs(FormulaAprestadoCliente formula) {
		panTablaQuimico.agregarElementos(formula.getQuimicos());
	}

	public String validar() {
		return null;
	}

	public FormulaAprestadoCliente setearDatosEnFormula() {
		if(formula != null) {
			formula.getQuimicos().clear();
			formula.getQuimicos().addAll(panTablaQuimico.getElementos());
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
package ar.com.textillevel.gui.modulos.odt.gui.tenido;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JPanel;

import ar.com.textillevel.gui.modulos.odt.gui.PanTablaVisualizacionFormulaCliente;
import ar.com.textillevel.gui.modulos.odt.gui.PanelTablaFormula;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.FormulaCliente;

public abstract class PanContenedorFormula<T extends FormulaCliente> extends JPanel {

	private static final long serialVersionUID = 1L;

	private final Frame owner;

	private PanelTablaFormula<T> panFormulas;
	private PanTablaVisualizacionFormulaCliente panMateriaPrimaCantidad;
	private boolean modoConsulta;

	public PanContenedorFormula(Frame owner, boolean modoConsulta) {
		this.owner = owner;
		this.modoConsulta = modoConsulta;
		construct();
	}

	private void construct() {
		setLayout(new GridBagLayout());
		add(getPanFormulas(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 1, 1, 0.5, 0.5));
		if(getPanMateriaPrima() != null) {
			add(getPanMateriaPrima(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 1, 1, 0.5, 0.5));
		}
	}

	public PanelTablaFormula<T> getPanFormulas() {
		if(panFormulas == null) {
			panFormulas = createPanFormula();
			panFormulas.setModoConsulta(modoConsulta);
		}
		return panFormulas;
	}

	protected abstract PanelTablaFormula<T> createPanFormula();
	
	public PanTablaVisualizacionFormulaCliente getPanMateriaPrima() {
		if(panMateriaPrimaCantidad == null) {
			panMateriaPrimaCantidad = createPanMateriaPrimaCantidad();
		}
		return panMateriaPrimaCantidad;
	}

	protected abstract PanTablaVisualizacionFormulaCliente createPanMateriaPrimaCantidad();

	public void setFormulas(List<T> formulasTenidoCliente) {
		getPanFormulas().agregarElementos(formulasTenidoCliente);
	}
	
	public Frame getOwner() {
		return owner;
	}


	public void limpiar() {
		getPanFormulas().limpiar();
//		getPanMateriaPrima().limpiar();
		//TODO:
	}

	public void setModoConsulta(boolean modoConsulta) {
		this.modoConsulta = modoConsulta;
		getPanFormulas().setModoConsulta(modoConsulta);
	}

	public void ocultarBotonesTabla(){
		getPanFormulas().ocultarBotones();
	}

	public List<T> getFormulas() {
		return getPanFormulas().getElementos();
	}

	public T getFormulaElegida() {
		return getPanFormulas().getFormulaElegida();
	}

	public void agregarFormula(T formula) {
		getPanFormulas().agregarFormula(formula);
	}

}
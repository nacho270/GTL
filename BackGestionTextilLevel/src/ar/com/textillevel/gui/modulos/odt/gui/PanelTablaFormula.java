package ar.com.textillevel.gui.modulos.odt.gui;

import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.gui.modulos.odt.gui.tenido.PersisterFormulaHandler;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.FormulaCliente;

public abstract class PanelTablaFormula<T extends FormulaCliente> extends PanelTabla<T> {

	private static final long serialVersionUID = 7178532055286935695L;

	protected PersisterFormulaHandler persisterFormulaHandler;
	protected ETipoProducto tipoProducto;

	public PanelTablaFormula(ETipoProducto tipoProducto, PersisterFormulaHandler persisterFormulaHandler) {
		this.tipoProducto = tipoProducto;
		this.persisterFormulaHandler = persisterFormulaHandler;
	}

	public abstract void ocultarBotones();

	public abstract void agregarFormula(T formula);

	public abstract T getFormulaElegida();

	@Override
	public boolean validarQuitar() {
		int selectedRow = getTabla().getSelectedRow();
		if(selectedRow != -1) {
			T formula = getElemento(selectedRow);
			if(formula.getId() != null && formula.getId() > 0) {
				persisterFormulaHandler.addIdFormulaEliminar(formula.getId());
			}
			persisterFormulaHandler.deleteFormulaParaGrabar(tipoProducto, formula);
		}
		return true;
	}

}
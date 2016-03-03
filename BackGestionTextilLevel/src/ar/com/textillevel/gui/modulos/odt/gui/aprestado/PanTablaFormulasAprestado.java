package ar.com.textillevel.gui.modulos.odt.gui.aprestado;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.gui.modulos.odt.gui.PanelTablaFormula;
import ar.com.textillevel.gui.modulos.odt.gui.estampado.PanTablaQuimicosPigmentosVisualizacion;
import ar.com.textillevel.gui.modulos.odt.gui.tenido.PersisterFormulaHandler;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.MateriaPrimaCantidad;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.aprestado.FormulaAprestadoCliente;

public class PanTablaFormulasAprestado extends PanelTablaFormula<FormulaAprestadoCliente> {

	private static final long serialVersionUID = 1L;

	private static final Integer CANT_COLS = 3;
	private static final Integer COL_NOMBRE = 0;
	private static final Integer COL_CODIGO_FORMULA = 1;
	private static final Integer COL_OBJ = 2;

	private PanTablaQuimicosPigmentosVisualizacion panVisualizacionQuimicosPigmentos;
	

	public PanTablaFormulasAprestado(Frame owner, PersisterFormulaHandler persisterFormulaHandler) {
		super(owner, ETipoProducto.APRESTADO, persisterFormulaHandler);
	}

	public void ocultarBotones() {
		getBotonEliminar().setVisible(false);
		if(getBotonModificar()!=null){
			getBotonModificar().setVisible(false);
		}
	}

	@Override
	protected void botonModificarPresionado(int filaSeleccionada) {
		edicionFormula(filaSeleccionada);
	}

	@Override
	protected FWJTable construirTabla() {
		FWJTable tabla = new FWJTable(0, CANT_COLS);
		tabla.setStringColumn(COL_NOMBRE, "NOMBRE DE LA FÓRMULA", 250, 250, true);
		tabla.setStringColumn(COL_CODIGO_FORMULA, "CÓDIGO", 60, 60, true);
		tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
		return tabla;
	}

	@Override
	protected void agregarElemento(FormulaAprestadoCliente elemento) {
		Object[] row = new Object[CANT_COLS];
		row[COL_NOMBRE] = elemento.getNombre();
		row[COL_CODIGO_FORMULA] = elemento.getCodigoFormula();
		row[COL_OBJ] = elemento;
		getTabla().addRow(row);
	}

	@Override
	protected void dobleClickTabla(int filaSeleccionada) {
		edicionFormula(filaSeleccionada);
	}

	@SuppressWarnings("rawtypes")
	private void edicionFormula(int filaSeleccionada) {
		FormulaAprestadoCliente formula = getElemento(filaSeleccionada);
		JDialogEditarFormulaAprestado dialogo = new JDialogEditarFormulaAprestado(owner, formula, modoConsulta);
		GuiUtil.centrarEnPadre(dialogo);
		dialogo.setVisible(true);
		if(dialogo.isAcepto()) {
			getTabla().setValueAt(formula.getNombre(), filaSeleccionada, COL_NOMBRE);
			getTabla().setValueAt(formula, filaSeleccionada, COL_OBJ);
			
			List<MateriaPrimaCantidad> allMateriasPrimas = new ArrayList<MateriaPrimaCantidad>();
			allMateriasPrimas.addAll(formula.getQuimicos());
			panVisualizacionQuimicosPigmentos.setMateriasPrimas(allMateriasPrimas);
			persisterFormulaHandler.addFormulaParaGrabar(tipoProducto, formula);
		}
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected void filaTablaSeleccionada() {
		int selectedRow = getTabla().getSelectedRow();
		if(selectedRow == -1) {
			panVisualizacionQuimicosPigmentos.limpiar();
		} else {
			FormulaAprestadoCliente formula = getElemento(selectedRow);
			List<MateriaPrimaCantidad> allMateriasPrimas = new ArrayList<MateriaPrimaCantidad>();
			allMateriasPrimas.addAll(formula.getQuimicos());
			panVisualizacionQuimicosPigmentos.setMateriasPrimas(allMateriasPrimas);
		}
	}

	@Override
	protected FormulaAprestadoCliente getElemento(int fila) {
		if(fila == -1) {
			return null;
		} else {
			return (FormulaAprestadoCliente)getTabla().getValueAt(fila, COL_OBJ);
		}
	}

	@Override
	protected String validarElemento(int fila) {
		return null;
	}

	@Override
	public boolean validarAgregar() {
		JDialogEditarFormulaAprestado dialogo = new JDialogEditarFormulaAprestado(owner, new FormulaAprestadoCliente(), false);
		GuiUtil.centrarEnPadre(dialogo);
		dialogo.setVisible(true);
		if(dialogo.isAcepto()) {
			agregarElemento(dialogo.getFormula());
			persisterFormulaHandler.addFormulaParaGrabar(tipoProducto, dialogo.getFormula());
		}
		return false;
	}

	@Override
	public FormulaAprestadoCliente getFormulaElegida() {
		if(getTabla().getSelectedRow()!=-1){
			return getElemento(getTabla().getSelectedRow());
		}
		return null;
	}

	@Override
	public void agregarFormula(FormulaAprestadoCliente formula) {
		agregarElemento(formula);
	}

	public void setPanVisualizacionQuimicosPigmentos(PanTablaQuimicosPigmentosVisualizacion panVisualizacionQuimicosPigmentos) {
		this.panVisualizacionQuimicosPigmentos = panVisualizacionQuimicosPigmentos;
	}

}
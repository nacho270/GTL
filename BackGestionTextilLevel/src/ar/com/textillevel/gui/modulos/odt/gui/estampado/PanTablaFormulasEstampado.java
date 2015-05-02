package ar.com.textillevel.gui.modulos.odt.gui.estampado;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.gui.modulos.odt.gui.PanelTablaFormula;
import ar.com.textillevel.gui.modulos.odt.gui.tenido.PersisterFormulaHandler;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.MateriaPrimaCantidad;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.estampado.FormulaEstampadoCliente;

public class PanTablaFormulasEstampado extends PanelTablaFormula<FormulaEstampadoCliente> {

	private static final long serialVersionUID = 1L;

	private static final Integer CANT_COLS = 4;
	private static final Integer COL_NOMBRE = 0;
	private static final Integer COL_COLOR = 1;
	private static final Integer COL_CODIGO_FORMULA = 2;
	private static final Integer COL_OBJ = 3;


	private PanTablaQuimicosPigmentosVisualizacion panVisualizacionQuimicosPigmentos;
	private Frame owner;

	public PanTablaFormulasEstampado(Frame owner, PersisterFormulaHandler persisterFormulaHandler) {
		super(ETipoProducto.ESTAMPADO, persisterFormulaHandler);
		this.owner = owner;
		agregarBotonModificar();
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
	protected CLJTable construirTabla() {
		CLJTable tabla = new CLJTable(0, CANT_COLS);
		tabla.setStringColumn(COL_NOMBRE, "NOMBRE DE LA FÓRMULA", 250, 250, true);
		tabla.setStringColumn(COL_COLOR, "COLOR", 180, 180, true);
		tabla.setStringColumn(COL_CODIGO_FORMULA, "CÓDIGO", 60, 60, true);
		tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
		return tabla;
	}

	@Override
	protected void agregarElemento(FormulaEstampadoCliente elemento) {
		Object[] row = new Object[CANT_COLS];
		row[COL_NOMBRE] = elemento.getNombre();
		row[COL_COLOR] = elemento.getColor().getNombre();
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
		FormulaEstampadoCliente formula = getElemento(filaSeleccionada);
		JDialogEditarFormulaEstampado dialogo = new JDialogEditarFormulaEstampado(owner, formula, modoConsulta);
		GuiUtil.centrarEnPadre(dialogo);
		dialogo.setVisible(true);
		if(dialogo.isAcepto()) {
			getTabla().setValueAt(formula.getNombre(), filaSeleccionada, COL_NOMBRE);
			getTabla().setValueAt(formula.getColor(), filaSeleccionada, COL_COLOR);
			getTabla().setValueAt(formula, filaSeleccionada, COL_OBJ);
			
			List<MateriaPrimaCantidad> allMateriasPrimas = new ArrayList<MateriaPrimaCantidad>();
			allMateriasPrimas.addAll(formula.getPigmentos());
			allMateriasPrimas.addAll(formula.getQuimicos());
			allMateriasPrimas.addAll(formula.getReactivos());
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
			FormulaEstampadoCliente formula = getElemento(selectedRow);
			List<MateriaPrimaCantidad> allMateriasPrimas = new ArrayList<MateriaPrimaCantidad>();
			allMateriasPrimas.addAll(formula.getPigmentos());
			allMateriasPrimas.addAll(formula.getQuimicos());
			allMateriasPrimas.addAll(formula.getReactivos());
			panVisualizacionQuimicosPigmentos.setMateriasPrimas(allMateriasPrimas);
		}
	}

	@Override
	protected FormulaEstampadoCliente getElemento(int fila) {
		if(fila == -1) {
			return null;
		} else {
			return (FormulaEstampadoCliente)getTabla().getValueAt(fila, COL_OBJ);
		}
	}

	@Override
	protected String validarElemento(int fila) {
		return null;
	}

	@Override
	public boolean validarAgregar() {
		JDialogEditarFormulaEstampado dialogo = new JDialogEditarFormulaEstampado(owner, new FormulaEstampadoCliente(), false);
		GuiUtil.centrarEnPadre(dialogo);
		dialogo.setVisible(true);
		if(dialogo.isAcepto()) {
			agregarElemento(dialogo.getFormula());
			persisterFormulaHandler.addFormulaParaGrabar(tipoProducto, dialogo.getFormula());			
		}
		return false;
	}

	@Override
	public FormulaEstampadoCliente getFormulaElegida() {
		if(getTabla().getSelectedRow()!=-1){
			return getElemento(getTabla().getSelectedRow());
		}
		return null;
	}

	@Override
	public void agregarFormula(FormulaEstampadoCliente formula) {
		agregarElemento(formula);
	}

	public void setPanVisualizacionQuimicosPigmentos(PanTablaQuimicosPigmentosVisualizacion panVisualizacionQuimicosPigmentos) {
		this.panVisualizacionQuimicosPigmentos = panVisualizacionQuimicosPigmentos;
	}

}
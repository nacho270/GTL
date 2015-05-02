package ar.com.textillevel.gui.modulos.odt.gui;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.articulos.Color;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.gui.modulos.odt.gui.tenido.JDialogEditarFormula;
import ar.com.textillevel.gui.modulos.odt.gui.tenido.PersisterFormulaHandler;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.FormulaTenidoCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.TenidoTipoArticulo;

public class PanTablaFormulasTenido extends PanelTablaFormula<FormulaTenidoCliente> {

	private static final long serialVersionUID = 1L;

	private static final Integer CANT_COLS = 5;
	private static final Integer COL_NOMBRE = 0;
	private static final Integer COL_TIPO_ARTICULO = 1;
	private static final Integer COL_COLOR = 2;
	private static final Integer COL_CODIGO = 3;
	private static final Integer COL_OBJ = 4;

	private PanTablaQuimicos panQuimicos;
	private Frame owner;
	
	private Color colorFixed;
	private TipoArticulo tipoArticuloFixed;

	public PanTablaFormulasTenido(Frame owner, PersisterFormulaHandler persisterFormulaHandler) {
		super(ETipoProducto.TENIDO, persisterFormulaHandler);
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
		tabla.setStringColumn(COL_NOMBRE, "NOMBRE DE LA FÓRMULA", 200, 200, true);
		tabla.setStringColumn(COL_TIPO_ARTICULO, "TIPO DE ARTÍCULO", 180, 180, true);
		tabla.setStringColumn(COL_COLOR, "COLOR", 180, 180, true);
		tabla.setStringColumn(COL_CODIGO, "CÓDIGO", 200, 200, true);
		tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
		return tabla;
	}

	@Override
	protected void agregarElemento(FormulaTenidoCliente elemento) {
		Object[] row = new Object[CANT_COLS];
		row[COL_NOMBRE] = elemento.getNombre();
		row[COL_TIPO_ARTICULO] = elemento.getTipoArticulo().getNombre();
		row[COL_COLOR] = elemento.getColor().getNombre();
		row[COL_CODIGO] = elemento.getCodigoFormula();
		row[COL_OBJ] = elemento;
		getTabla().addRow(row);
	}

	@Override
	protected void dobleClickTabla(int filaSeleccionada) {
		edicionFormula(filaSeleccionada);
	}

	private void edicionFormula(int filaSeleccionada) {
		FormulaTenidoCliente formula = getElemento(filaSeleccionada);
		JDialogEditarFormula dialogo = new JDialogEditarFormula(owner, formula, modoConsulta);
		GuiUtil.centrarEnPadre(dialogo);
		dialogo.setVisible(true);
		if(dialogo.isAcepto()) {
			getTabla().setValueAt(formula.getNombre(), filaSeleccionada, COL_NOMBRE);
			getTabla().setValueAt(formula.getColor(), filaSeleccionada, COL_COLOR);
			getTabla().setValueAt(formula.getTipoArticulo().getNombre(), filaSeleccionada, COL_TIPO_ARTICULO);
			getTabla().setValueAt(formula, filaSeleccionada, COL_OBJ);
			panQuimicos.setTenidos(formula.getTenidosComponentes());
			persisterFormulaHandler.addFormulaParaGrabar(tipoProducto, formula);			
		}
	}

	@Override
	protected void filaTablaSeleccionada() {
		int selectedRow = getTabla().getSelectedRow();
		if(selectedRow == -1) {
			panQuimicos.limpiar();
		} else {
			FormulaTenidoCliente formula = getElemento(selectedRow);
			panQuimicos.setTenidos(formula.getTenidosComponentes());
		}
	}

	@Override
	protected FormulaTenidoCliente getElemento(int fila) {
		if(fila == -1) {
			return null;
		} else {
			return (FormulaTenidoCliente)getTabla().getValueAt(fila, COL_OBJ);
		}
	}

	@Override
	protected String validarElemento(int fila) {
		return null;
	}

	@Override
	public boolean validarAgregar() {
		FormulaTenidoCliente formula = new FormulaTenidoCliente();
		formula.setColor(colorFixed);
		if(tipoArticuloFixed != null) {
			formula.setTipoArticulo(tipoArticuloFixed);
			List<TipoArticulo> tiposArticulos = new ArrayList<TipoArticulo>();
			if(tipoArticuloFixed.getTiposArticuloComponentes().isEmpty()) {
				tiposArticulos.add(tipoArticuloFixed);
			} else {
				tiposArticulos.addAll(tipoArticuloFixed.getTiposArticuloComponentes());
			}
			for(TipoArticulo ta : tiposArticulos) {
				TenidoTipoArticulo tta = new TenidoTipoArticulo();
				tta.setTipoArticulo(ta);
				formula.getTenidosComponentes().add(tta);
			}
		}
		
		JDialogEditarFormula dialogo = new JDialogEditarFormula(owner, formula, false);
		GuiUtil.centrarEnPadre(dialogo);
		dialogo.setVisible(true);
		if(dialogo.isAcepto()) {
			agregarElemento(dialogo.getFormula());
			persisterFormulaHandler.addFormulaParaGrabar(tipoProducto, dialogo.getFormula());			
		}
		return false;
	}

	@Override
	public FormulaTenidoCliente getFormulaElegida() {
		if(getTabla().getSelectedRow()!=-1){
			return getElemento(getTabla().getSelectedRow());
		}
		return null;
	}

	@Override
	public void agregarFormula(FormulaTenidoCliente formula) {
		agregarElemento(formula);
	}

	public void setPanQuimicos(PanTablaQuimicos panQuimicos) {
		this.panQuimicos = panQuimicos;
	}

	public void setColorFixed(Color colorFixed) {
		this.colorFixed = colorFixed;
	}

	public void setTipoArticuloFixed(TipoArticulo tipoArticuloFixed) {
		this.tipoArticuloFixed = tipoArticuloFixed;
	}

}
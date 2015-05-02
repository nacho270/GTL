package ar.com.textillevel.modulos.odt.entidades.maquinas.formulas;

import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.aprestado.FormulaAprestadoCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.estampado.FormulaEstampadoCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.FormulaTenidoCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.TenidoTipoArticulo;

public class DoEagerFormulaVisitor implements IFormulaClienteVisitor {

	public void visit(FormulaTenidoCliente formulaTenido) {
		for (TenidoTipoArticulo tta : formulaTenido.getTenidosComponentes()) {
			tta.getAnilinasCantidad().size();
			tta.getReactivosCantidad().size();
		}
	}

	public void visit(FormulaEstampadoCliente formulaEstampado) {
		formulaEstampado.getPigmentos().size();
		formulaEstampado.getQuimicos().size();
		formulaEstampado.getReactivos().size();
	}

	public void visit(FormulaAprestadoCliente formulaAprestado) {
		formulaAprestado.getQuimicos().size();
	}

}
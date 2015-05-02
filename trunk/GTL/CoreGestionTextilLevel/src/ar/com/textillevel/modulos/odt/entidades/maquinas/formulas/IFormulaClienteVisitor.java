package ar.com.textillevel.modulos.odt.entidades.maquinas.formulas;

import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.aprestado.FormulaAprestadoCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.estampado.FormulaEstampadoCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.FormulaTenidoCliente;

public interface IFormulaClienteVisitor {

	public void visit(FormulaTenidoCliente formulaTenido);

	public void visit(FormulaEstampadoCliente formulaEstampado);

	public void visit(FormulaAprestadoCliente formulaAprestado);

}

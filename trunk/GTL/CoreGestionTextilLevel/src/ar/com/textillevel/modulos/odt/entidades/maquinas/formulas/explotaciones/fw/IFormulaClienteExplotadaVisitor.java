package ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw;

import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.FormulaEstampadoClienteExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.FormulaTenidoClienteExplotada;

public interface IFormulaClienteExplotadaVisitor {
	public void visit(FormulaEstampadoClienteExplotada fece);
	public void visit(FormulaTenidoClienteExplotada ftce);
}

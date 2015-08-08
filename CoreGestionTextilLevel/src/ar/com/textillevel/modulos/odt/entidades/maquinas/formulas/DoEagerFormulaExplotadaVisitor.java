package ar.com.textillevel.modulos.odt.entidades.maquinas.formulas;

import ar.com.textillevel.entidades.ventas.materiaprima.Pigmento;
import ar.com.textillevel.entidades.ventas.materiaprima.Quimico;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.FormulaEstampadoClienteExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.FormulaTenidoClienteExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.IFormulaClienteExplotadaVisitor;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.MateriaPrimaCantidadExplotada;

public class DoEagerFormulaExplotadaVisitor implements IFormulaClienteExplotadaVisitor{

	public void visit(FormulaEstampadoClienteExplotada fece) {
		fece.getFormulaDesencadenante().getNombre();
		fece.getPigmentos().size();
		for(MateriaPrimaCantidadExplotada<Pigmento> mp : fece.getPigmentos()){
			mp.getMateriaPrimaCantidadDesencadenante().getDescripcion();
			mp.getTipoArticulo().getNombre();
		}
		fece.getQuimicos().size();
		for(MateriaPrimaCantidadExplotada<Quimico> mp : fece.getQuimicos()){
			mp.getMateriaPrimaCantidadDesencadenante().getDescripcion();
			mp.getTipoArticulo().getNombre();
		}
	}

	public void visit(FormulaTenidoClienteExplotada ftce) {
		ftce.getFormulaDesencadenante().getNombre();
		ftce.getMateriasPrimas().size();
		for(MateriaPrimaCantidadExplotada<Anilina> mp : ftce.getMateriasPrimas()){
			mp.getMateriaPrimaCantidadDesencadenante().getDescripcion();
			mp.getTipoArticulo().getNombre();
		}
		DoEagerFormulaVisitor def = new DoEagerFormulaVisitor();
		ftce.getFormulaDesencadenante().accept(def);
	}
}
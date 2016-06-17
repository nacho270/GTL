package ar.com.textillevel.gui.modulos.odt.gui.tenido;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.FormulaCliente;

public class PersisterFormulaHandler {

	private Set<Integer> idsFormulasEliminar;
	private Map<ETipoProducto, Set<FormulaCliente>> formulasMap;

	public PersisterFormulaHandler() {
		this.idsFormulasEliminar = new HashSet<Integer>();
		this.formulasMap = new HashMap<ETipoProducto, Set<FormulaCliente>>(); 
	}

	public void addIdFormulaEliminar(Integer idFormula) {
		this.idsFormulasEliminar.add(idFormula);
	}

	public void addFormulaParaGrabar(ETipoProducto tipoProducto, FormulaCliente formula) {
		Set<FormulaCliente> formulasSet = this.formulasMap.get(tipoProducto);
		if(formulasSet == null) {
			formulasSet = new HashSet<FormulaCliente>();
			this.formulasMap.put(tipoProducto, formulasSet);
		}
		formulasSet.add(formula);
	}

	public List<FormulaCliente> getFormulasToSave() {
		List<FormulaCliente> formulas = new ArrayList<FormulaCliente>();
		for(ETipoProducto tp : formulasMap.keySet()) {
			formulas.addAll(formulasMap.get(tp));
		}
		return formulas;
	}

	public List<Integer> getIdsFormulasToBorrar() {
		return new ArrayList<Integer>(idsFormulasEliminar);
	}

	public void deleteFormulaParaGrabar(ETipoProducto tipoProducto, FormulaCliente formula) {
		if(formulasMap.get(tipoProducto) != null) {
			formulasMap.get(tipoProducto).remove(formula);
		}
	}
	
	public void clear() {
		this.idsFormulasEliminar.clear();
		this.formulasMap.clear();
	}

}
package ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.FormulaClienteExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.IFormulaClienteExplotadaVisitor;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.MateriaPrimaCantidadExplotada;

@Entity
@Table(name="T_FORMULA_TENIDO_EXPLOTADA")
public class FormulaTenidoClienteExplotada extends FormulaClienteExplotada{

	private static final long serialVersionUID = -5173115889522961792L;
	
	private List<MateriaPrimaCantidadExplotada<Anilina>> materiasPrimas;
	
	public FormulaTenidoClienteExplotada() {
		materiasPrimas = new ArrayList<MateriaPrimaCantidadExplotada<Anilina>>();
	}

	@OneToMany(fetch=FetchType.LAZY,cascade={CascadeType.ALL})
	@JoinColumn(name="F_MAT_PRIM_EXPL_ANILINA")
	public List<MateriaPrimaCantidadExplotada<Anilina>> getMateriasPrimas() {
		return materiasPrimas;
	}

	public void setMateriasPrimas(List<MateriaPrimaCantidadExplotada<Anilina>> materiasPrimas) {
		this.materiasPrimas = materiasPrimas;
	}

	@Override
	@Transient
	public void accept(IFormulaClienteExplotadaVisitor visitor) {
		visitor.visit(this);		
	}
}

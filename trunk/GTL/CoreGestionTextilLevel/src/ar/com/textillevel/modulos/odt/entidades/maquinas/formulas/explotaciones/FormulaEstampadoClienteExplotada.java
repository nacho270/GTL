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

import ar.com.textillevel.entidades.ventas.materiaprima.Pigmento;
import ar.com.textillevel.entidades.ventas.materiaprima.Quimico;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.FormulaClienteExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.IFormulaClienteExplotadaVisitor;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.MateriaPrimaCantidadExplotada;

@Entity
@Table(name="T_FORMULA_ESTAMPADO_EXPLOTADA")
public class FormulaEstampadoClienteExplotada extends FormulaClienteExplotada {

	private static final long serialVersionUID = 5122520521649748937L;

	private List<MateriaPrimaCantidadExplotada<Pigmento>> pigmentos;
	private List<MateriaPrimaCantidadExplotada<Quimico>> quimicos;

	public FormulaEstampadoClienteExplotada() {
		pigmentos = new ArrayList<MateriaPrimaCantidadExplotada<Pigmento>>();
		quimicos = new ArrayList<MateriaPrimaCantidadExplotada<Quimico>>();
	}

	@OneToMany(fetch=FetchType.LAZY,cascade={CascadeType.ALL})
	@JoinColumn(name="F_MAT_PRIM_EXPL_PIGMENTOS")
	public List<MateriaPrimaCantidadExplotada<Pigmento>> getPigmentos() {
		return pigmentos;
	}

	public void setPigmentos(List<MateriaPrimaCantidadExplotada<Pigmento>> pigmentos) {
		this.pigmentos = pigmentos;
	}

	@OneToMany(fetch=FetchType.LAZY,cascade={CascadeType.ALL})
	@JoinColumn(name="F_MAT_PRIM_EXPL_QUIMICOS")
	public List<MateriaPrimaCantidadExplotada<Quimico>> getQuimicos() {
		return quimicos;
	}

	public void setQuimicos(List<MateriaPrimaCantidadExplotada<Quimico>> quimicos) {
		this.quimicos = quimicos;
	}

	@Override
	@Transient
	public void accept(IFormulaClienteExplotadaVisitor visitor) {
		visitor.visit(this);
	}
}

package ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.FormulaCliente;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class FormulaClienteExplotada implements Serializable{

	private static final long serialVersionUID = -3171480359750689389L;

	private Integer id;
	private FormulaCliente formulaDesencadenante;

	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.TABLE)
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_FORMULA_CLIENTE_P_ID")
	public FormulaCliente getFormulaDesencadenante() {
		return formulaDesencadenante;
	}

	public void setFormulaDesencadenante(FormulaCliente formulaDesencadenante) {
		this.formulaDesencadenante = formulaDesencadenante;
	}
	
	@Transient
	public abstract void accept(IFormulaClienteExplotadaVisitor visitor);
}

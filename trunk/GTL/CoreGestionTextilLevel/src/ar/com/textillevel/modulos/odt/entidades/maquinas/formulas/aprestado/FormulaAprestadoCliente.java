package ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.aprestado;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.FormulaCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.IFormulaClienteVisitor;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.QuimicoCantidad;

@Entity
@Table(name = "T_FORMULA_APRESTADO_CLIENTE")
public class FormulaAprestadoCliente extends FormulaCliente implements Serializable {

	private static final long serialVersionUID = 5568105327099837199L;

	private List<QuimicoCantidad> quimicos;

	public FormulaAprestadoCliente() {
		this.quimicos = new ArrayList<QuimicoCantidad>();
	}

	@OneToMany(cascade = { CascadeType.ALL })
	@JoinColumn(name = "F_FORMULA_APRESTADO_P_ID")
	@org.hibernate.annotations.Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	public List<QuimicoCantidad> getQuimicos() {
		return quimicos;
	}

	public void setQuimicos(List<QuimicoCantidad> quimicos) {
		this.quimicos = quimicos;
	}

	@Override
	@Transient
	public void accept(IFormulaClienteVisitor visitor) {
		visitor.visit(this);
	}

}
package ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.estampado;

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
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.ReactivoCantidad;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.QuimicoCantidad;

@Entity
@Table(name = "T_FORMULA_ESTAMPADO_CLIENTE")
public class FormulaEstampadoCliente extends FormulaCliente implements Serializable {

	private static final long serialVersionUID = -2584052973310742674L;

	private List<PigmentoCantidad> pigmentos;
	private List<QuimicoCantidad> quimicos;
	private List<ReactivoCantidad> reactivos;

	public FormulaEstampadoCliente() {
		this.pigmentos = new ArrayList<PigmentoCantidad>();
		this.quimicos = new ArrayList<QuimicoCantidad>();
		this.reactivos = new ArrayList<ReactivoCantidad>();
	}

	@OneToMany(cascade = { CascadeType.ALL })
	@JoinColumn(name = "F_FORMULA_P_ID")
	@org.hibernate.annotations.Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	public List<PigmentoCantidad> getPigmentos() {
		return pigmentos;
	}

	public void setPigmentos(List<PigmentoCantidad> pigmentos) {
		this.pigmentos = pigmentos;
	}

	@OneToMany(cascade = { CascadeType.ALL })
	@JoinColumn(name = "F_FORMULA_P_ID")
	@org.hibernate.annotations.Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	public List<QuimicoCantidad> getQuimicos() {
		return quimicos;
	}

	public void setQuimicos(List<QuimicoCantidad> quimicos) {
		this.quimicos = quimicos;
	}

	@OneToMany(cascade = { CascadeType.ALL })
	@JoinColumn(name = "F_FORMULA_P_ID")
	@org.hibernate.annotations.Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	public List<ReactivoCantidad> getReactivos() {
		return reactivos;
	}

	public void setReactivos(List<ReactivoCantidad> reactivos) {
		this.reactivos = reactivos;
	}

	@Override
	@Transient
	public void accept(IFormulaClienteVisitor visitor) {
		visitor.visit(this);
	}

}
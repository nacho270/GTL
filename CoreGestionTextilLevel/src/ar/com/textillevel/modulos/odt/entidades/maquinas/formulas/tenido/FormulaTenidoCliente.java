package ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.FormulaCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.IFormulaClienteVisitor;

@Entity
@Table(name = "T_FORMULA_TENIDO_CLIENTE")
public class FormulaTenidoCliente extends FormulaCliente implements Serializable {

	private static final long serialVersionUID = -6963651280148911225L;

	private TipoArticulo tipoArticulo;
	private List<TenidoTipoArticulo> tenidosComponentes;

	public FormulaTenidoCliente() {
		this.tenidosComponentes = new ArrayList<TenidoTipoArticulo>();
	}
	
	@ManyToOne
	@JoinColumn(name="F_TIPO_ARTICULO_P_ID", nullable=false)
	@Fetch(FetchMode.JOIN)
	public TipoArticulo getTipoArticulo() {
		return tipoArticulo;
	}

	public void setTipoArticulo(TipoArticulo tipoArticulo) {
		this.tipoArticulo = tipoArticulo;
	}

	@OneToMany(cascade = { CascadeType.ALL })
	@JoinColumn(name = "F_FORMULA_P_ID")
	@org.hibernate.annotations.Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	public List<TenidoTipoArticulo> getTenidosComponentes() {
		return tenidosComponentes;
	}

	public void setTenidosComponentes(List<TenidoTipoArticulo> tenidosComponentes) {
		this.tenidosComponentes = tenidosComponentes;
	}

	@Override
	public String toString() {
		return getCodigoFormula() + (getNombre() != null? " - " + getNombre() : "");
	}

	@Transient
	public List<TenidoTipoArticulo> getTenidosComponentesParaArticulo(TipoArticulo tipoArticulo2) {
		if(getTenidosComponentes()!=null && !getTenidosComponentes().isEmpty()){
			List<TenidoTipoArticulo> ret = new ArrayList<TenidoTipoArticulo>();
			for(TenidoTipoArticulo tta : getTenidosComponentes()){
				if(tta.getTipoArticulo().equals(tipoArticulo2)){
					ret.add(tta);
				}
			}
			return ret;
		}
		return Collections.emptyList();
	}

	@Override
	@Transient
	public void accept(IFormulaClienteVisitor visitor) {
		visitor.visit(this);
	}
}
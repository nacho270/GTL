package ar.com.textillevel.modulos.odt.entidades.maquinas.procesos;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.CreadorFormulasVisitor;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.FormulaCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.FormulaClienteExplotada;
import ar.com.textillevel.modulos.odt.enums.ETipoInstruccionProcedimiento;

@Entity
@DiscriminatorValue(value = "IPTP")
public class InstruccionProcedimientoTipoProducto extends InstruccionProcedimiento {

	private static final long serialVersionUID = 3734445224030342615L;

	private TipoArticulo tipoArticulo;
	private Integer idTipoProducto;
	private FormulaClienteExplotada formula;
	private FormulaCliente formulaTransient;

	@ManyToOne
	@JoinColumn(name = "F_TIPO_ARTICULO_P_ID")
	public TipoArticulo getTipoArticulo() {
		return tipoArticulo;
	}

	public void setTipoArticulo(TipoArticulo tipoArticulo) {
		this.tipoArticulo = tipoArticulo;
	}

	@Column(name = "A_ID_TIPO_PRODUCTO_P_ID")
	private Integer getIdTipoProducto() {
		return idTipoProducto;
	}

	private void setIdTipoProducto(Integer idTipoProducto) {
		this.idTipoProducto = idTipoProducto;
	}
	
	@ManyToOne(fetch=FetchType.EAGER,cascade={CascadeType.ALL})
	@JoinColumn(name="F_FORMULA_CLIENTE_P_ID")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public FormulaClienteExplotada getFormula() {
		return formula;
	}

	public void setFormula(FormulaClienteExplotada formula) {
		this.formula = formula;
	}

	@Transient
	public ETipoProducto getTipoProducto() {
		return ETipoProducto.getById(getIdTipoProducto());
	}

	public void setTipoProducto(ETipoProducto tipoProducto) {
		setIdTipoProducto(tipoProducto.getId());
	}

	@Override
	@Transient
	public ETipoInstruccionProcedimiento getTipo() {
		return ETipoInstruccionProcedimiento.TIPO_PRODUCTO;
	}

	@Override
	@Transient
	public void accept(IInstruccionProcedimientoVisitor visitor) {
		visitor.visit(this);
	}

	public void explotarFormula(OrdenDeTrabajo odt, FormulaCliente formula){
//		FormulaTenidoClienteExplotada ftcExplotada = new FormulaTenidoClienteExplotada();
//		ftcExplotada.setFormulaDesencadenante(formula);
//		for(TenidoTipoArticulo tta : ((FormulaTenidoCliente)formula).getTenidosComponentes()){
//			TenidoTipoArticulo ttaNuevo = new TenidoTipoArticulo();
//			ttaNuevo.setTipoArticulo(tta.getTipoArticulo());
//			for(AnilinaCantidad ac : tta.getAnilinasCantidad()){
//				MateriaPrimaCantidadExplotada<Anilina> acNuevo = new MateriaPrimaCantidadExplotada<Anilina>();
//				acNuevo.setMateriaPrimaCantidadDesencadenante(ac);
//				acNuevo.setCantidadExplotada(ac.getCantidad()*odt.getRemito().getPesoTotal().floatValue()/100);
//				acNuevo.setTipoArticulo(tta.getTipoArticulo());
//				ftcExplotada.getMateriasPrimas().add(acNuevo);
//			}
//		}
//		setFormula(ftcExplotada);
		CreadorFormulasVisitor creadorFormula = new CreadorFormulasVisitor(odt);
		formula.accept(creadorFormula);
		setFormula(creadorFormula.getFormulaExplotada());
	}

	@Transient
	public FormulaCliente getFormulaTransient() {
		return formulaTransient;
	}

	public void setFormulaTransient(FormulaCliente formulaTransient) {
		this.formulaTransient = formulaTransient;
	}
}
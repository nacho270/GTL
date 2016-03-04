package ar.com.textillevel.modulos.odt.entidades.maquinas.procesos;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
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

	@Transient
	public String getDescrSimple() {
		return getTipoProducto().getDescripcion().toLowerCase() + " " + getTipoArticulo().getNombre();
	}

	@Transient
	public FormulaCliente getFormulaTransient() {
		return formulaTransient;
	}

	public void setFormulaTransient(FormulaCliente formulaTransient) {
		this.formulaTransient = formulaTransient;
	}

	@Transient
	public FormulaClienteExplotada explotarFormula(OrdenDeTrabajo odt, FormulaCliente formula){
		CreadorFormulasVisitor creadorFormula = new CreadorFormulasVisitor(odt);
		formula.accept(creadorFormula);
		return creadorFormula.getFormulaExplotada();
	}

	@Transient
	public String getDescrDetallada() {
		return "[NO IMPLEMENTADO]";
	}

}
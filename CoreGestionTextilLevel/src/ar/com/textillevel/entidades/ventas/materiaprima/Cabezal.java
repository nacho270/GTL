package ar.com.textillevel.entidades.ventas.materiaprima;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.visitor.IMateriaPrimaVisitor;

@Entity
@DiscriminatorValue(value = "MPCABEZAL")
public class Cabezal extends MateriaPrima {

	private static final long serialVersionUID = -4360813115119631003L;

	private BigDecimal diametroCabezal;

	@Override
	public void accept(IMateriaPrimaVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	@Transient
	public ETipoMateriaPrima getTipo() {
		return ETipoMateriaPrima.CABEZAL;
	}

	@Column(name="A_ANCHO_CABEZAL", nullable=true)
	public BigDecimal getDiametroCabezal() {
		return diametroCabezal;
	}

	public void setDiametroCabezal(BigDecimal diametroCabezal) {
		this.diametroCabezal = diametroCabezal;
	}

}

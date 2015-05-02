package ar.com.textillevel.entidades.ventas.materiaprima;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.visitor.IMateriaPrimaVisitor;

@Entity
@DiscriminatorValue(value="MPVARIOS")
public class MateriaPrimaGenerica extends MateriaPrima{

	private static final long serialVersionUID = 3764054171582299648L;

	@Override
	@Transient
	public void accept(IMateriaPrimaVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	@Transient
	public ETipoMateriaPrima getTipo() {
		return ETipoMateriaPrima.VARIOS;
	}
}

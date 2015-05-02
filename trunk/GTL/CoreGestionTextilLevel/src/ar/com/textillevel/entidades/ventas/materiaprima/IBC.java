package ar.com.textillevel.entidades.ventas.materiaprima;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.visitor.IMateriaPrimaVisitor;

@Entity
@DiscriminatorValue(value = "IBC")
public class IBC extends MateriaPrima {

	private static final long serialVersionUID = 6249055335938753877L;

	@Override
	public void accept(IMateriaPrimaVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	@Transient
	public ETipoMateriaPrima getTipo() {
		return ETipoMateriaPrima.IBC;
	}

}
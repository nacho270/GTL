package ar.com.textillevel.entidades.ventas.materiaprima;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.visitor.IMateriaPrimaVisitor;

@Entity
@DiscriminatorValue(value="MPMC")
public class MaterialConstruccion extends MateriaPrima{

	private static final long serialVersionUID = 2368776556313073477L;

	@Override
	public void accept(IMateriaPrimaVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	@Transient
	public ETipoMateriaPrima getTipo() {
		return ETipoMateriaPrima.MATERIAL_CONSTRUCCION;
	}
}

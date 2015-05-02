package ar.com.textillevel.entidades.ventas.materiaprima;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.visitor.IMateriaPrimaVisitor;

@Entity
@DiscriminatorValue(value="MPQ")
public class Quimico extends MateriaPrima implements Formulable{

	private static final long serialVersionUID = 8685760268207136904L;
	
	@Override
	public void accept(IMateriaPrimaVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	@Transient
	public ETipoMateriaPrima getTipo() {
		return ETipoMateriaPrima.QUIMICO;
	}
}

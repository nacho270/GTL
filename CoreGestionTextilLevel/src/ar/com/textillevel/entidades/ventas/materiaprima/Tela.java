package ar.com.textillevel.entidades.ventas.materiaprima;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.materiaprima.visitor.IMateriaPrimaVisitor;

@Entity
@DiscriminatorValue(value="MPT")
public class Tela extends MateriaPrima {

	private static final long serialVersionUID = -7148773703517360898L;

	private Articulo articulo;

	@Override
	public void accept(IMateriaPrimaVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	@Transient
	public ETipoMateriaPrima getTipo() {
		return ETipoMateriaPrima.TELA;
	}

	@ManyToOne
	@JoinColumn(name="F_ARTICULO_P_ID", nullable=true)
	public Articulo getArticulo() {
		return articulo;
	}

	public void setArticulo(Articulo articulo) {
		this.articulo = articulo;
	}
}

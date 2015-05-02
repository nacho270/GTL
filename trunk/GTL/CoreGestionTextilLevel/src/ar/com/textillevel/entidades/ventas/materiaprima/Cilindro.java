package ar.com.textillevel.entidades.ventas.materiaprima;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.visitor.IMateriaPrimaVisitor;

@Entity
@DiscriminatorValue(value = "MPC")
public class Cilindro extends MateriaPrima {

	private static final long serialVersionUID = 1L;

	private BigDecimal anchoCilindro;
	private BigDecimal meshCilindro;
	private BigDecimal diametroCilindro;

	@Override
	public void accept(IMateriaPrimaVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	@Transient
	public ETipoMateriaPrima getTipo() {
		return ETipoMateriaPrima.CILINDRO;
	}

	@Column(name="A_ANCHO_CILINDRO", nullable=true)
	public BigDecimal getAnchoCilindro() {
		return anchoCilindro;
	}

	public void setAnchoCilindro(BigDecimal anchoCilindro) {
		this.anchoCilindro = anchoCilindro;
	}

	@Column(name="A_MESH_CILINDRO", nullable=true)
	public BigDecimal getMeshCilindro() {
		return meshCilindro;
	}

	public void setMeshCilindro(BigDecimal meshCilindro) {
		this.meshCilindro = meshCilindro;
	}

	@Column(name="A_DIAMETRO_CILINDRO", nullable=true)
	public BigDecimal getDiametroCilindro() {
		return diametroCilindro;
	}

	public void setDiametroCilindro(BigDecimal diametroCilindro) {
		this.diametroCilindro = diametroCilindro;
	}
}

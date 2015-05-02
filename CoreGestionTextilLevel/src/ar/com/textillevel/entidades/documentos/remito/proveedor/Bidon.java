package ar.com.textillevel.entidades.documentos.remito.proveedor;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="BID")
public class Bidon extends ContenedorMateriaPrima {

	private static final long serialVersionUID = -4814544278940424111L;

	private BigDecimal capacidad;

	@Column(name = "A_CAPACIDAD", nullable=false)
	public BigDecimal getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(BigDecimal capacidad) {
		this.capacidad = capacidad;
	}

}
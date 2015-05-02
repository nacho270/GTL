package ar.com.textillevel.entidades.documentos.factura.itemfactura;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoItemFactura;

@Entity
@DiscriminatorValue(value = "ITFP")
public class ItemFacturaPercepcion extends ItemFactura {

	private static final long serialVersionUID = 5789406189693056275L;

	private BigDecimal porcentajePercepcion;

	@Override
	@Transient
	public ETipoItemFactura getTipo() {
		return ETipoItemFactura.PERCEPCION;
	}

	@Column(name = "A_PORC_PERCEPCION")
	public BigDecimal getPorcentajePercepcion() {
		return porcentajePercepcion;
	}

	public void setPorcentajePercepcion(BigDecimal porcentajePercepcion) {
		this.porcentajePercepcion = porcentajePercepcion;
	}
}

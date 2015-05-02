package ar.com.textillevel.entidades.documentos.factura.itemfactura;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoItemFactura;

@Entity
@DiscriminatorValue(value = "ITT")
public class ItemFacturaTubo extends ItemFactura {

	private static final long serialVersionUID = -7291101699985335779L;

	private BigDecimal precioTubo;

	@Override
	@Transient
	public ETipoItemFactura getTipo() {
		return ETipoItemFactura.TUBOS;
	}

	@Column(name="A_PRECIO_TUBO")
	public BigDecimal getPrecioTubo() {
		return precioTubo;
	}

	public void setPrecioTubo(BigDecimal precioTubo) {
		this.precioTubo = precioTubo;
	}
}

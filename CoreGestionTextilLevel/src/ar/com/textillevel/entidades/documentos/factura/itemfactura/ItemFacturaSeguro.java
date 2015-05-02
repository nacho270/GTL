package ar.com.textillevel.entidades.documentos.factura.itemfactura;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoItemFactura;

@Entity
@DiscriminatorValue(value = "ITS")
public class ItemFacturaSeguro extends ItemFactura {

	private static final long serialVersionUID = 2794839125207806590L;

	private BigDecimal porcentajeSeguro;

	@Override
	@Transient
	public ETipoItemFactura getTipo() {
		return ETipoItemFactura.SEGURO;
	}

	@Column(name="A_PORC_SEGURO")
	public BigDecimal getPorcentajeSeguro() {
		return porcentajeSeguro;
	}

	public void setPorcentajeSeguro(BigDecimal porcentajeSeguro) {
		this.porcentajeSeguro = porcentajeSeguro;
	}
}

package ar.com.textillevel.entidades.documentos.factura.proveedor;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "ICR")
public class ItemCorreccionResumen extends ItemCorreccionFacturaProveedor {

	private static final long serialVersionUID = 1405277912537497330L;

	public ItemCorreccionResumen() {
		super();
		setCantidad(new BigDecimal(1));
		setFactorConversionMoneda(new BigDecimal(1));
		setImporte(new BigDecimal(0));
		setPorcDescuento(new BigDecimal(0));
		setPrecioUnitario(new BigDecimal(1));
	}

}

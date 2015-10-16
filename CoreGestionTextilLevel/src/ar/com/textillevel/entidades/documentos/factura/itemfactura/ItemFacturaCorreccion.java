package ar.com.textillevel.entidades.documentos.factura.itemfactura;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoItemFactura;

@Entity
@DiscriminatorValue(value = "ITFC")
public class ItemFacturaCorreccion extends ItemFactura {

	private static final long serialVersionUID = 7848467046295700646L;

	@Override
	@Transient
	public ETipoItemFactura getTipo() {
		return ETipoItemFactura.CORRECCION_FACTURA;
	}

}

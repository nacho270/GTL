package ar.com.textillevel.entidades.documentos.factura.itemfactura;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoItemFactura;

@Entity
@DiscriminatorValue(value = "ITFO")
public class ItemFacturaOtro extends ItemFactura {

	private static final long serialVersionUID = 6756808964448036229L;

	@Override
	@Transient
	public ETipoItemFactura getTipo() {
		return ETipoItemFactura.OTRO;
	}
}

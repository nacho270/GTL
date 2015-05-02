package ar.com.textillevel.entidades.documentos.factura.itemfactura;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoItemFactura;

@Entity
@DiscriminatorValue(value="ITB")
public class ItemFacturaBonificacion extends ItemFactura{

	private static final long serialVersionUID = 8736215833423369943L;

	@Override
	@Transient
	public ETipoItemFactura getTipo() {
		return ETipoItemFactura.BONIFICACION;
	}
}

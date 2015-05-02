package ar.com.textillevel.entidades.documentos.factura.itemfactura;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoItemFactura;

@Entity
@DiscriminatorValue(value="ITR")
public class ItemFacturaRecargo extends ItemFactura{
	
	private static final long serialVersionUID = -9213799408568357101L;

	@Override
	@Transient
	public ETipoItemFactura getTipo() {
		return ETipoItemFactura.RECARGO;
	}
}

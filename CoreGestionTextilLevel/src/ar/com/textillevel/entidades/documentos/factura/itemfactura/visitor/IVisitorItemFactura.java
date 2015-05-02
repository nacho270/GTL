package ar.com.textillevel.entidades.documentos.factura.itemfactura.visitor;

import java.math.BigDecimal;

public interface IVisitorItemFactura {
	public BigDecimal visit(IItemFacturaVisitor visitor);
}

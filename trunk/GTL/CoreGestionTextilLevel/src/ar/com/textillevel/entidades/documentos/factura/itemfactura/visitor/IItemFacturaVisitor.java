package ar.com.textillevel.entidades.documentos.factura.itemfactura.visitor;

import java.math.BigDecimal;

import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaBonificacion;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaProducto;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaRecargo;

public interface IItemFacturaVisitor {
	public BigDecimal visit(ItemFacturaProducto itemProducto);
	public BigDecimal visit(ItemFacturaBonificacion itemBonificacion);
	public BigDecimal visit(ItemFacturaRecargo itemRecargo);
}

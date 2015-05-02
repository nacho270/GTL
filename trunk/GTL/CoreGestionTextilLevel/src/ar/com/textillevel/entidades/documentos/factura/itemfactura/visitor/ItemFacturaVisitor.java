package ar.com.textillevel.entidades.documentos.factura.itemfactura.visitor;

import java.math.BigDecimal;

import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaBonificacion;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaProducto;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaRecargo;

public class ItemFacturaVisitor implements IItemFacturaVisitor {

	public BigDecimal visit(ItemFacturaProducto itemProducto) {
		// TODO Auto-generated method stub
		return null;
	}

	public BigDecimal visit(ItemFacturaBonificacion itemBonificacion) {
		// TODO Auto-generated method stub
		return null;
	}

	public BigDecimal visit(ItemFacturaRecargo itemRecargo) {
		// TODO Auto-generated method stub
		return null;
	}

}

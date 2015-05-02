package ar.com.textillevel.entidades.documentos.factura.proveedor.visitor;

import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemFacturaMateriaPrima;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemFacturaServicio;

public interface IItemFacturaProveedorVisitor {

	public abstract void visit(ItemFacturaMateriaPrima ifmp);
	public abstract void visit(ItemFacturaServicio ifs);

}

package ar.com.textillevel.entidades.documentos.remito.proveedor.visitor;

import ar.com.textillevel.entidades.documentos.remito.proveedor.ItemOtro;
import ar.com.textillevel.entidades.documentos.remito.proveedor.ItemPrecioMateriaPrima;
import ar.com.textillevel.entidades.documentos.remito.proveedor.ItemRelacionContenedor;

public interface IItemRemitoSalidaVisitor {

	public void visit(ItemPrecioMateriaPrima ipmp);
	public void visit(ItemRelacionContenedor irc);
	public void visit(ItemOtro io);

}

package ar.com.textillevel.entidades.documentos.factura.proveedor.visitor;

import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaCreditoProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaDebitoProveedor;

public interface ICorreccionFacturaProveedorVisitor {

	public abstract void visit(NotaCreditoProveedor ncp);
	public abstract void visit(NotaDebitoProveedor ndp);

}

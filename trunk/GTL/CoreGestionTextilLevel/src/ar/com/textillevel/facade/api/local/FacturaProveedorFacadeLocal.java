package ar.com.textillevel.facade.api.local;

import javax.ejb.Local;

import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;

@Local
public interface FacturaProveedorFacadeLocal {

	public FacturaProveedor actualizarFactura(FacturaProveedor factura);
	public FacturaProveedor ingresarFactura(FacturaProveedor factura, String usuario);
	public FacturaProveedor getByIdEager(Integer idFactura);

}

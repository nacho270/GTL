package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;
import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaDebitoProveedor;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePago;

@Local
public interface PagoOrdenDePagoDAOLocal extends DAOLocal<PagoOrdenDePago, Integer> {

	public boolean existsFacturaEnPagoOrdenDePago(FacturaProveedor factura);

	public boolean existsNotaDebitoProvEnPagoOrdenDePago(NotaDebitoProveedor ndp);


}

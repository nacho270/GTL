package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaCreditoProveedor;
import ar.com.textillevel.entidades.documentos.ordendepago.formapago.FormaPagoOrdenDePago;

@Local
public interface FormaPagoOrdenDePagoDAOLocal extends DAOLocal<FormaPagoOrdenDePago, Integer> {

	public boolean existsNotaCreditoProvEnFormaPagoOrdenDePago(NotaCreditoProveedor ncp);

}

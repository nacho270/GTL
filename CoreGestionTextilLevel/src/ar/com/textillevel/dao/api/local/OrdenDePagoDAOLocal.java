package ar.com.textillevel.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.documentos.factura.to.InfoCuentaTO;
import ar.com.textillevel.entidades.documentos.ordendepago.OrdenDePago;

@Local
public interface OrdenDePagoDAOLocal extends DAOLocal<OrdenDePago, Integer> {

	public Integer getNewNumeroOrdenDePago();

	public OrdenDePago getOrdenDePagoByNroOrdenEager(Integer nroOrden);

	public InfoCuentaTO getInfoOrdenDePagoYPagosRecibidos(Integer idProveedor);

	public List<OrdenDePago> getAllByIdProveedor(Integer idProveedor);

	public OrdenDePago getByIdEager(Integer idODP);

	public OrdenDePago getByNumero(String numero);

}

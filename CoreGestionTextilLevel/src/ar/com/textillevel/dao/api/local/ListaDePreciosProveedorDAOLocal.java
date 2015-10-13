package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.ventas.materiaprima.ListaDePreciosProveedor;

@Local
public interface ListaDePreciosProveedorDAOLocal extends DAOLocal<ListaDePreciosProveedor, Integer> {
	
	 public ListaDePreciosProveedor getListaByIdProveedor(Integer idProveedor);

}

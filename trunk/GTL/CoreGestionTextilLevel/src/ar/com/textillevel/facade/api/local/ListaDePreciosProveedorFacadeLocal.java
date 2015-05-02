package ar.com.textillevel.facade.api.local;

import javax.ejb.Local;

import ar.com.textillevel.entidades.ventas.materiaprima.ListaDePreciosProveedor;

@Local
public interface ListaDePreciosProveedorFacadeLocal {

	public ListaDePreciosProveedor getListaByIdProveedor(Integer idProveedor);

	public ListaDePreciosProveedor save(ListaDePreciosProveedor listaDePrecios, String usuario);
}

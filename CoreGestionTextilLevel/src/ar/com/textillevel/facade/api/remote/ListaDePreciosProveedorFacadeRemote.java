package ar.com.textillevel.facade.api.remote;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.ventas.materiaprima.ListaDePreciosProveedor;

@Remote
public interface ListaDePreciosProveedorFacadeRemote {

	public ListaDePreciosProveedor getListaByIdProveedor(Integer idProveedor);

	public ListaDePreciosProveedor save(ListaDePreciosProveedor listaDePrecios, String usuario);

	public void remove(ListaDePreciosProveedor listaDePrecios);

}

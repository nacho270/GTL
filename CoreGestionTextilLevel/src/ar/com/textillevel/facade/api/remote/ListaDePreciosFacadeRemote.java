package ar.com.textillevel.facade.api.remote;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.ventas.productos.ListaDePrecios;

@Remote
public interface ListaDePreciosFacadeRemote {

	public ListaDePrecios getListaByIdCliente(Integer idCliente);

	public ListaDePrecios save(ListaDePrecios listaDePreciosActual);

	public void remove(ListaDePrecios listaDePreciosActual);

}

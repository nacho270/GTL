package ar.com.textillevel.facade.api.local;

import javax.ejb.Local;

import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.cotizacion.Cotizacion;
import ar.com.textillevel.entidades.ventas.cotizacion.VersionListaDePrecios;

@Local
public interface ListaDePreciosFacadeLocal {
	public Cotizacion actualizarVersionListaDePrecios(Cliente cliente, VersionListaDePrecios nuevaVersionListaDePrecios);
}

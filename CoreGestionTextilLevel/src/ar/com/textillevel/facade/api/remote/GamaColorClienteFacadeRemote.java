package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.ventas.articulos.GamaColorCliente;

@Remote
public interface GamaColorClienteFacadeRemote {
	public List<GamaColorCliente> getByCliente(Integer idCliente);
	public GamaColorCliente getByGama(Integer idGamaOriginal);
}

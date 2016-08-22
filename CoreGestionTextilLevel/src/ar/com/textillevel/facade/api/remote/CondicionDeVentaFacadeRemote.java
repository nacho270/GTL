package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.documentos.factura.CondicionDeVenta;

@Remote
public interface CondicionDeVentaFacadeRemote {
	public CondicionDeVenta save(CondicionDeVenta condicion);
	public void remove(CondicionDeVenta condicion);
	public List<CondicionDeVenta> getAllOrderByName();
	public CondicionDeVenta getById(Integer id);
}

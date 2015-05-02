package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.documentos.factura.proveedor.Servicio;
import ar.com.textillevel.entidades.gente.Proveedor;

@Remote
public interface ServicioFacadeRemote {
	public Servicio save(Servicio servicio);
	public void remove(Servicio servicio);
	public List<Servicio> getAllOrderByName();
	public List<Servicio> getAllOrderByNameEager();
	public List<Servicio> getServiciosByProveedor(Integer idProveedor);
	public boolean existeServicio(String nombreServicio, Proveedor proveedor, Integer idAExcluir);
}

package ar.com.textillevel.facade.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.com.textillevel.entidades.gente.Proveedor;

@Local
public interface ProveedorFacadeLocal {
	public Proveedor getById(Integer id);
	public List<Proveedor> getAllProveedoresOrderByName();
	public List<Proveedor> getProveedorByNombreCorto(String nombreCorto);
	public List<Proveedor> getProveedorByRazonSocial(String razonSocial);
}

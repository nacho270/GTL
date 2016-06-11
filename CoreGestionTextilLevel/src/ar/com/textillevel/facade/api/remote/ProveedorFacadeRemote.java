package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.to.ClienteDeudaTO;

@Remote
public interface ProveedorFacadeRemote {
	public List<Proveedor> getAllProveedoresOrderByName();
	public void eliminarProveedor(Integer idProveedor);
	public void guardarProveedor(Proveedor proveedor);
	public boolean existeProveedor(String razonSocial, String cuit);
	public List<Proveedor> getProveedorByNombreCorto(String nombreCorto);
	public List<Proveedor> getProveedorByRazonSocial(String razonSocial);
	public List<ClienteDeudaTO> getProveedoresALosQueSeLesDebe();
	public Proveedor getById(Integer idProveedor);
}

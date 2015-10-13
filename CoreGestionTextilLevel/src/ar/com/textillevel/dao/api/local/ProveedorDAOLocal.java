package ar.com.textillevel.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.gente.Rubro;
import ar.com.textillevel.entidades.to.ClienteDeudaTO;

@Local
public interface ProveedorDAOLocal extends DAOLocal<Proveedor, Integer>{
	public List<Proveedor> getAllOrderByName();
	public List<Proveedor> getAllByRubroOrderByName(Rubro rubro);
	public boolean existeProveedor(String razonSocial, String cuit);
	public List<Proveedor> getAllByRazonSocial(String razonSocial);
	public List<Proveedor> getAllByRazonSocialAndRubro(String razonSocial, Rubro rubro);
	public List<Proveedor> getProveedorByNombreCorto(String nombreCorto);
	public List<Proveedor> getProveedorByRazonSocial(String razonSocial);
	public List<ClienteDeudaTO> getProveedoresALosQueSeLesDebe();
}

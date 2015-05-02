package ar.com.textillevel.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.dao.api.local.ProveedorDAOLocal;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.to.ClienteDeudaTO;
import ar.com.textillevel.facade.api.local.ProveedorFacadeLocal;
import ar.com.textillevel.facade.api.remote.ProveedorFacadeRemote;

@Stateless
public class ProveedorFacade implements ProveedorFacadeLocal, ProveedorFacadeRemote{

	@EJB
	private ProveedorDAOLocal proveedorDao;
	
	public Proveedor getById(Integer id){
		return proveedorDao.getById(id);
	}
	
	public List<Proveedor> getAllProveedoresOrderByName() {
		return proveedorDao.getAllOrderByName();
	}

	public void eliminarProveedor(Integer idProveedor) {
		proveedorDao.removeById(idProveedor);
	}

	public boolean existeProveedor(String razonSocial, String cuit) {
		return proveedorDao.existeProveedor(razonSocial, cuit);
	}

	public void guardarProveedor(Proveedor proveedor) {
		proveedorDao.save(proveedor);
	}

	public List<Proveedor> getProveedorByNombreCorto(String nombreCorto) {
		return proveedorDao.getProveedorByNombreCorto(nombreCorto);
	}

	public List<Proveedor> getProveedorByRazonSocial(String razonSocial) {
		return proveedorDao.getProveedorByRazonSocial(razonSocial);
	}

	public List<ClienteDeudaTO> getProveedoresALosQueSeLesDebe() {
		return proveedorDao.getProveedoresALosQueSeLesDebe();
	}
}

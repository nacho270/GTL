package ar.com.textillevel.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.dao.api.local.ServicioDAOLocal;
import ar.com.textillevel.entidades.documentos.factura.proveedor.Servicio;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.facade.api.remote.ServicioFacadeRemote;

@Stateless
public class ServicioFacade implements ServicioFacadeRemote{

	@EJB
	private ServicioDAOLocal servicioDao;
	
	public List<Servicio> getAllOrderByName() {
		return servicioDao.getAllOrderBy("nombre");
	}

	public void remove(Servicio servicio) {
		servicioDao.removeById(servicio.getId());
	}

	public Servicio save(Servicio servicio) {
		Servicio s = servicioDao.save(servicio);
		s.getProveedor().getCelular();
		return s;
	}

	public List<Servicio> getAllOrderByNameEager() {
		return servicioDao.getAllOrderByNameEager();
	}

	public List<Servicio> getServiciosByProveedor(Integer idProveedor) {
		return servicioDao.getServiciosByProveedor(idProveedor);
	}

	public boolean existeServicio(String nombreServicio, Proveedor proveedor, Integer idAExcluir) {
		return servicioDao.existeServicio(nombreServicio,proveedor,idAExcluir);
	}
}

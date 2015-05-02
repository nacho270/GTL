package ar.com.textillevel.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.documentos.factura.proveedor.Servicio;
import ar.com.textillevel.entidades.gente.Proveedor;

@Local
public interface ServicioDAOLocal extends DAOLocal<Servicio, Integer>{

	List<Servicio> getAllOrderByNameEager();
	List<Servicio> getServiciosByProveedor(Integer idProveedor);
	boolean existeServicio(String nombreServicio, Proveedor proveedor, Integer idAExcluir);

}

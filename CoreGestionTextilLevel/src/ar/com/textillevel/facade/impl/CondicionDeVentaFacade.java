package ar.com.textillevel.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.dao.api.local.CondicionDeVentaDAOLocal;
import ar.com.textillevel.entidades.documentos.factura.CondicionDeVenta;
import ar.com.textillevel.facade.api.remote.CondicionDeVentaFacadeRemote;

@Stateless
public class CondicionDeVentaFacade implements CondicionDeVentaFacadeRemote{
	
	@EJB
	private CondicionDeVentaDAOLocal condicionDao;
	
	public List<CondicionDeVenta> getAllOrderByName() {
		return condicionDao.getAllOrderBy("nombre");
	}

	public void remove(CondicionDeVenta condicion) {
		condicionDao.removeById(condicion.getId());
	}

	public CondicionDeVenta save(CondicionDeVenta condicion) {
		return condicionDao.save(condicion);
	}

	public CondicionDeVenta getById(Integer id) {
		return condicionDao.getById(id);
	}
}

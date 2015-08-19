package ar.com.textillevel.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.dao.api.local.GamaColorClienteDAOLocal;
import ar.com.textillevel.entidades.ventas.articulos.GamaColorCliente;
import ar.com.textillevel.facade.api.remote.GamaColorClienteFacadeRemote;

@Stateless
public class GamaColorClienteFacade implements GamaColorClienteFacadeRemote{

	@EJB
	private GamaColorClienteDAOLocal gamaClienteDao;
	
	public List<GamaColorCliente> getByCliente(Integer idCliente) {
		List<GamaColorCliente> gamas = gamaClienteDao.getByCliente(idCliente);
		if (gamas != null && !gamas.isEmpty()) {
			for(GamaColorCliente gcc : gamas) {
				gcc.getColores().size();
			}
		}
		return gamas;
	}

	public GamaColorCliente getByGama(Integer idGamaOriginal) {
		return gamaClienteDao.getByGama(idGamaOriginal);
	}

	public void save(List<GamaColorCliente> gamas) {
		for(GamaColorCliente gcc : gamas) {
			gamaClienteDao.save(gcc);
		}
	}

}

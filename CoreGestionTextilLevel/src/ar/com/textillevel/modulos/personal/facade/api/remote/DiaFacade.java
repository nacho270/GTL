package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.entidades.Dia;
import ar.com.textillevel.modulos.personal.dao.api.DiaDAOLocal;

@Stateless
public class DiaFacade implements DiaFacadeRemote{

	@EJB
	private DiaDAOLocal diaDao;
	
	public List<Dia> getAllDias() {
		return diaDao.getAll();
	}
}

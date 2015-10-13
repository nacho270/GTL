package ar.com.textillevel.modulos.personal.facade.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.textillevel.modulos.personal.dao.api.ParametrosGeneralesPersonalDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.configuracion.ParametrosGeneralesPersonal;
import ar.com.textillevel.modulos.personal.facade.api.local.ParametrosGeneralesPersonalFacadeLocal;
import ar.com.textillevel.modulos.personal.facade.api.remote.ParametrosGeneralesPersonalFacadeRemote;

@Stateless
public class ParametrosGeneralesPersonalFacade implements ParametrosGeneralesPersonalFacadeLocal, ParametrosGeneralesPersonalFacadeRemote{

	@EJB
	private ParametrosGeneralesPersonalDAOLocal paramDao;
	
	public ParametrosGeneralesPersonal getParametrosGenerales() {
		return paramDao.getParametrosGenerales();
	}

	public ParametrosGeneralesPersonal save(ParametrosGeneralesPersonal parametrosGenerales) throws FWException {
		return paramDao.save(parametrosGenerales);
	}
}

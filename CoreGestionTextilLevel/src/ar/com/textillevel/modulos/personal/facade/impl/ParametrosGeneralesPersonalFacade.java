package ar.com.textillevel.modulos.personal.facade.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.clarin.fwjava.componentes.error.CLException;
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

	public ParametrosGeneralesPersonal save(ParametrosGeneralesPersonal parametrosGenerales) throws CLException {
		return paramDao.save(parametrosGenerales);
	}
}

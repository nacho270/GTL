package ar.com.textillevel.facade.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.textillevel.dao.api.local.ParametrosGeneralesDAOLocal;
import ar.com.textillevel.entidades.config.ParametrosGenerales;
import ar.com.textillevel.facade.api.local.ParametrosGeneralesFacadeLocal;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;

@Stateless
public class ParametrosGeneralesFacade implements ParametrosGeneralesFacadeRemote, ParametrosGeneralesFacadeLocal {

	@EJB
	private ParametrosGeneralesDAOLocal parametrosGeneralesDAOLocal;

	public ParametrosGenerales getParametrosGenerales() {
		return parametrosGeneralesDAOLocal.getParametrosGenerales();
	}

	public ParametrosGenerales save(ParametrosGenerales parametrosGenerales)  throws FWException{
		return parametrosGeneralesDAOLocal.save(parametrosGenerales);
	}
}

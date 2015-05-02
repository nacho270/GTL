package ar.com.textillevel.facade.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.clarin.fwjava.componentes.error.CLException;
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

	public ParametrosGenerales save(ParametrosGenerales parametrosGenerales)  throws CLException{
		return parametrosGeneralesDAOLocal.save(parametrosGenerales);
	}
}

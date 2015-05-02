package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.personal.entidades.commons.AFJP;

@Remote
public interface AFJPFacadeRemote {
	public AFJP save(AFJP afjp);
	public List<AFJP> getAllOrderByName();
}

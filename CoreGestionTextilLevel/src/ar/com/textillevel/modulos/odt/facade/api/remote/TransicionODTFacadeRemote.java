package ar.com.textillevel.modulos.odt.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.odt.entidades.workflow.TransicionODT;

@Remote
public interface TransicionODTFacadeRemote {

	public List<TransicionODT> getAllEagerByODT(Integer idODT);	

}

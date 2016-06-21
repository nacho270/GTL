package ar.com.textillevel.modulos.odt.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.odt.entidades.workflow.TransicionODT;

@Local
public interface TransicionODTDAOLocal extends DAOLocal<TransicionODT, Integer>{

	public TransicionODT getByODT(Integer idOdt);

	public List<TransicionODT> getAllByODT(Integer idODT);
	
	public void deleteTransicionesFromODT(Integer idODT);

}

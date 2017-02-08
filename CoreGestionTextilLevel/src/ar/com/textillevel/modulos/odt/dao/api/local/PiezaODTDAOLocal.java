package ar.com.textillevel.modulos.odt.dao.api.local;

import javax.ejb.Local;
import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;

@Local
public interface PiezaODTDAOLocal extends DAOLocal<PiezaODT, Integer>{

	public PiezaODT getByParams(String codODT, Integer nroPieza, Integer nroSubPieza);

}

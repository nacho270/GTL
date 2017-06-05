package ar.com.textillevel.modulos.odt.dao.api.local;

import javax.ejb.Local;
import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.odt.entidades.CodigoODT;

@Local
public interface CodigoODTDAOLocal extends DAOLocal<CodigoODT, Integer> {

	public String getUltimoCodigoODT();

	public void removeByCodigo(String codigo);

}

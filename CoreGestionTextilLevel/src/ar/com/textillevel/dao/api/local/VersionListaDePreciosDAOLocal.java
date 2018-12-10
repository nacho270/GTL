package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.ventas.cotizacion.VersionListaDePrecios;

@Local
public interface VersionListaDePreciosDAOLocal extends DAOLocal<VersionListaDePrecios, Integer>{

}

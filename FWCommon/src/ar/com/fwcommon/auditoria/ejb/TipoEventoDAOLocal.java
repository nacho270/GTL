package ar.com.fwcommon.auditoria.ejb;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;

@Local
public interface TipoEventoDAOLocal extends DAOLocal<TipoEvento, Integer> {
}
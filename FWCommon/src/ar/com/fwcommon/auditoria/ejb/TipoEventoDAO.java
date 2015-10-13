package ar.com.fwcommon.auditoria.ejb;

import javax.ejb.Stateless;

import ar.com.fwcommon.dao.impl.GenericDAO;

@Stateless
public class TipoEventoDAO extends GenericDAO<TipoEvento, Integer> implements TipoEventoDAOLocal {
}
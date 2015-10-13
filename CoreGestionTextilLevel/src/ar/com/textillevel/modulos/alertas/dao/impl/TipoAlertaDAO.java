package ar.com.textillevel.modulos.alertas.dao.impl;

import javax.ejb.Stateless;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.alertas.dao.api.local.TipoAlertaDAOLocal;
import ar.com.textillevel.modulos.alertas.entidades.TipoAlerta;

@Stateless
public class TipoAlertaDAO extends GenericDAO<TipoAlerta, Integer> implements TipoAlertaDAOLocal{

}

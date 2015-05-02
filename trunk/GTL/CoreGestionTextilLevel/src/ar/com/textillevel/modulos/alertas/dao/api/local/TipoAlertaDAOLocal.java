package ar.com.textillevel.modulos.alertas.dao.api.local;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.alertas.entidades.TipoAlerta;

@Local
public interface TipoAlertaDAOLocal extends DAOLocal<TipoAlerta, Integer>{

}

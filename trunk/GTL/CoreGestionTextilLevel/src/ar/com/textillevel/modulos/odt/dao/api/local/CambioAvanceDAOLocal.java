package ar.com.textillevel.modulos.odt.dao.api.local;

import javax.ejb.Local;
import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.odt.entidades.workflow.CambioAvance;

@Local
public interface CambioAvanceDAOLocal extends DAOLocal<CambioAvance, Integer> {

}

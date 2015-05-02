package ar.com.textillevel.modulos.personal.dao.api;

import javax.ejb.Local;
import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.personal.entidades.calenlaboral.CalendarioAnualFeriado;

@Local
public interface CalendarioAnualFeriadoDAOLocal extends DAOLocal<CalendarioAnualFeriado, Integer> {

	public boolean existeCalendario(Integer anio);

}

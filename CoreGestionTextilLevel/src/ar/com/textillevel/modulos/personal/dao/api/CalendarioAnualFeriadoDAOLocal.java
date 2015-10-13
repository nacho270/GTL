package ar.com.textillevel.modulos.personal.dao.api;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.personal.entidades.calenlaboral.CalendarioAnualFeriado;

@Local
public interface CalendarioAnualFeriadoDAOLocal extends DAOLocal<CalendarioAnualFeriado, Integer> {

	public boolean existeCalendario(Integer anio);

}

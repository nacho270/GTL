package ar.com.textillevel.modulos.personal.dao.api;

import java.sql.Date;
import java.util.List;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.RegistroVacacionesLegajo;

@Local
public interface RegistroVacacionesDAOLocal extends DAOLocal<RegistroVacacionesLegajo, Integer>{

	List<RegistroVacacionesLegajo> getAllRegistrosVacacionesByFecha(Date fechaDesde, Date fechaHasta);

}

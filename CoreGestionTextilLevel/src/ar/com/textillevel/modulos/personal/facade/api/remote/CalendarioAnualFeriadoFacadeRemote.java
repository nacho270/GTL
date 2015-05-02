package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.sql.Date;
import java.util.List;

import javax.ejb.Remote;

import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.modulos.personal.entidades.calenlaboral.CalendarioAnualFeriado;

@Remote
public interface CalendarioAnualFeriadoFacadeRemote {

	public List<CalendarioAnualFeriado> getAll();

	public CalendarioAnualFeriado getByIdEager(Integer anio);

	public CalendarioAnualFeriado save(CalendarioAnualFeriado calendario) throws ValidacionException;

	public void eliminarCalendario(CalendarioAnualFeriado calendario);

	public CalendarioAnualFeriado createCalendario(CalendarioAnualFeriado calendario) throws ValidacionException;

	public CalendarioAnualFeriado copiarCalendario(CalendarioAnualFeriado calendario, CalendarioAnualFeriado calenEjemplo) throws ValidacionException;

	public CalendarioAnualFeriado getCalendarioActual();
	
	public Integer getCantidadDiasHabilesBetweenFechas(Date fechaDesde, Date fechahasta);

}

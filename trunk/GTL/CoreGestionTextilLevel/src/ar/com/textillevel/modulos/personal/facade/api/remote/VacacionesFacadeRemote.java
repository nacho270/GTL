package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.sql.Date;
import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.RegistroVacacionesLegajo;

@Remote
public interface VacacionesFacadeRemote {
	public void grabarVacaciones(Empleado empleado, RegistroVacacionesLegajo registro);
	public void borrarPeriodoVacaciones(Empleado empleado, RegistroVacacionesLegajo registroVacacionesLegajo);
	public void modificarPeriodoVacaciones(Empleado empleado, RegistroVacacionesLegajo reg);
	public List<RegistroVacacionesLegajo> getAllRegistrosVacacionesByFecha(Date fechaDesde, Date fechaHasta);
}

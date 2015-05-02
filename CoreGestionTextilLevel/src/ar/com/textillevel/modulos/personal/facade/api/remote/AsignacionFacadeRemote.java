package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.sql.Date;
import java.util.List;
import javax.ejb.Remote;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones.Asignacion;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones.AsignacionNoRemPorcSueldoBruto;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones.AsignacionNoRemSimple;

@Remote
public interface AsignacionFacadeRemote {

	public void remove(Asignacion asignacion);
	public Asignacion save(Asignacion asignacion);
	public List<Asignacion> getAllByIdSindicato(Integer idSindicato);
	public Asignacion getByIdEager(Integer idAsignacion);
	public List<AsignacionNoRemSimple> getAllNoRemSimpleByFechaAndIdSindicato(Date fecha, Integer idSindicato);
	public List<AsignacionNoRemPorcSueldoBruto> getAllNoRemPorcSueldoBrutoByFechaAndIdSindicato(Date fecha, Integer idSindicato);

}

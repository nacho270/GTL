package ar.com.textillevel.modulos.personal.dao.api;

import java.sql.Date;
import java.util.List;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones.Asignacion;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones.AsignacionNoRemPorcSueldoBruto;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones.AsignacionNoRemSimple;

@Local
public interface AsignacionDAOLocal extends DAOLocal<Asignacion, Integer> {

	public List<Asignacion> getAllByIdSindicato(Integer idSindicato);
	public Asignacion getByIdEager(Integer idAsignacion);
	public List<AsignacionNoRemSimple> getAllNoRemSimpleByFechaAndIdSindicato(Date fecha, Integer idSindicato);
	public List<AsignacionNoRemPorcSueldoBruto> getAllNoRemPorcSueldoBrutoByFechaAndIdSindicato(Date fecha, Integer idSindicato);

}

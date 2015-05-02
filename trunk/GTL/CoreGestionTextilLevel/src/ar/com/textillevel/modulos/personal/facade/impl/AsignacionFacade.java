package ar.com.textillevel.modulos.personal.facade.impl;

import java.sql.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import ar.com.textillevel.modulos.personal.dao.api.AsignacionDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones.Asignacion;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones.AsignacionNoRemPorcSueldoBruto;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones.AsignacionNoRemSimple;
import ar.com.textillevel.modulos.personal.facade.api.remote.AsignacionFacadeRemote;

@Stateless
public class AsignacionFacade implements AsignacionFacadeRemote {

	@EJB
	private AsignacionDAOLocal asignacionDAO;

	public void remove(Asignacion asignacion) {
		asignacionDAO.removeById(asignacion.getId());
	}

	public Asignacion save(Asignacion asignacion) {
		return asignacionDAO.save(asignacion);
	}

	public List<Asignacion> getAllByIdSindicato(Integer idSindicato) {
		return asignacionDAO.getAllByIdSindicato(idSindicato);
	}

	public Asignacion getByIdEager(Integer idAsignacion) {
		return asignacionDAO.getByIdEager(idAsignacion);
	}

	public List<AsignacionNoRemSimple> getAllNoRemSimpleByFechaAndIdSindicato(Date fecha, Integer idSindicato) {
		return asignacionDAO.getAllNoRemSimpleByFechaAndIdSindicato(fecha, idSindicato);
	}

	public List<AsignacionNoRemPorcSueldoBruto> getAllNoRemPorcSueldoBrutoByFechaAndIdSindicato(Date fecha, Integer idSindicato) {
		return asignacionDAO.getAllNoRemPorcSueldoBrutoByFechaAndIdSindicato(fecha, idSindicato);
	}

}
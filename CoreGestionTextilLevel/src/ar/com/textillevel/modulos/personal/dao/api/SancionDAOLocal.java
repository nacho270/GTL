package ar.com.textillevel.modulos.personal.dao.api;

import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.Sancion;

@Local
public interface SancionDAOLocal extends DAOLocal<Sancion, Integer> {

	public List<Sancion> getSanciones(LegajoEmpleado legajo);

	public List<Sancion> getSancionesNoAsociadas(LegajoEmpleado legajo);

}

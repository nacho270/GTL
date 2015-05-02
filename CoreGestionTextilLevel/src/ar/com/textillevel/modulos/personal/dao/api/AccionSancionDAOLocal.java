package ar.com.textillevel.modulos.personal.dao.api;

import java.util.List;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.AccionSancion;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.Sancion;

@SuppressWarnings("rawtypes")
@Local
public interface AccionSancionDAOLocal extends DAOLocal<AccionSancion, Integer> {

	public List<AccionSancion> getHistoria(Sancion sancion);

	public void borrarAccionesSancion(Sancion sancion);

}

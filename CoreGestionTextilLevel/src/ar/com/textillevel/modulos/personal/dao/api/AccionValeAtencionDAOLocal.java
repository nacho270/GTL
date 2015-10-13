package ar.com.textillevel.modulos.personal.dao.api;

import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.AccionValeAtencion;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValeAtencion;

@SuppressWarnings("rawtypes")
@Local
public interface AccionValeAtencionDAOLocal extends DAOLocal<AccionValeAtencion, Integer> {

	public List<AccionValeAtencion> getHistoria(ValeAtencion valeAtencion);

	public void borrarAccionesValeAtencion(ValeAtencion valeAtencion);

}

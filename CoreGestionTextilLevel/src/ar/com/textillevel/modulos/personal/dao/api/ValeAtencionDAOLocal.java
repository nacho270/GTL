package ar.com.textillevel.modulos.personal.dao.api;

import java.util.List;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValeAtencion;

@Local
public interface ValeAtencionDAOLocal extends DAOLocal<ValeAtencion, Integer> {

	public List<ValeAtencion> getValesAtencion(LegajoEmpleado legajo);

}

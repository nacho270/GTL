package ar.com.textillevel.modulos.odt.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.AccionProcedimiento;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;

@Local
public interface AccionProcedimientoDAOLocal extends DAOLocal<AccionProcedimiento, Integer> {

	public boolean existsAccionProcedimiento(AccionProcedimiento accion);

	public List<AccionProcedimiento> getAllSortedBySector(ESectorMaquina sector);

}

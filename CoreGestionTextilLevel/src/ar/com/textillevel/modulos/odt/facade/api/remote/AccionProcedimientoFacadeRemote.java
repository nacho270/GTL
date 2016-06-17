package ar.com.textillevel.modulos.odt.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.AccionProcedimiento;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;

@Remote
public interface AccionProcedimientoFacadeRemote {

	public List<AccionProcedimiento> getAllSorted();

	public List<AccionProcedimiento> getAllSortedBySector(ESectorMaquina sector);

	public AccionProcedimiento save(AccionProcedimiento accion) throws ValidacionException;

	public AccionProcedimiento getById(Integer id);

}

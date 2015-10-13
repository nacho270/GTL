package ar.com.textillevel.modulos.odt.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimiento;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;
import ar.com.textillevel.modulos.odt.enums.ETipoInstruccionProcedimiento;

@Remote
public interface InstruccionProcedimientoFacadeRemote {

	public List<InstruccionProcedimiento> getInstruccionesBySectorAndTipo(ESectorMaquina sectorMaquina, ETipoInstruccionProcedimiento tipoInstruccion);

	public InstruccionProcedimiento save(InstruccionProcedimiento instruccion) throws ValidacionException;

}

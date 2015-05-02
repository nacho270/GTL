package ar.com.textillevel.modulos.odt.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimiento;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;
import ar.com.textillevel.modulos.odt.enums.ETipoInstruccionProcedimiento;

@Local
public interface InstruccionProcedimientoDAOLocal extends DAOLocal<InstruccionProcedimiento, Integer> {

	public List<InstruccionProcedimiento> getInstruccionesBySectorAndTipo(ESectorMaquina sectorMaquina, ETipoInstruccionProcedimiento tipoInstruccion);

	public boolean existsInstruccion(InstruccionProcedimiento instruccion);

}
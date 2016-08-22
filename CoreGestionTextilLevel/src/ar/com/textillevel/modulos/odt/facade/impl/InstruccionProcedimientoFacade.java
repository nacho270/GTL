package ar.com.textillevel.modulos.odt.facade.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.modulos.odt.dao.api.local.InstruccionProcedimientoDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimiento;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;
import ar.com.textillevel.modulos.odt.enums.ETipoInstruccionProcedimiento;
import ar.com.textillevel.modulos.odt.facade.api.remote.InstruccionProcedimientoFacadeRemote;

@Stateless
public class InstruccionProcedimientoFacade implements InstruccionProcedimientoFacadeRemote {

	@EJB
	private InstruccionProcedimientoDAOLocal instruccionDAO;

	public List<InstruccionProcedimiento> getInstruccionesBySectorAndTipo(ESectorMaquina sectorMaquina, ETipoInstruccionProcedimiento tipoInstruccion) {
			return instruccionDAO.getInstruccionesBySectorAndTipo(sectorMaquina, tipoInstruccion);
	}

	public InstruccionProcedimiento save(InstruccionProcedimiento instruccion) throws ValidacionException {
		if(instruccionDAO.existsInstruccion(instruccion)) {
			List<String> strList = new ArrayList<String>();
			strList.add(instruccion.getSectorMaquina().getDescripcion());
			throw new ValidacionException(EValidacionException.INSTRUCCION_PROCEDIMIENTO_EXISTENTE.getInfoValidacion(), strList);
		}
		return instruccionDAO.save(instruccion);
	}

}

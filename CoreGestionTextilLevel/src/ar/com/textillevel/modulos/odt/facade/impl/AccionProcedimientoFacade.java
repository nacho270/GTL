package ar.com.textillevel.modulos.odt.facade.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.modulos.odt.dao.api.local.AccionProcedimientoDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.AccionProcedimiento;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;
import ar.com.textillevel.modulos.odt.facade.api.remote.AccionProcedimientoFacadeRemote;

@Stateless
public class AccionProcedimientoFacade implements AccionProcedimientoFacadeRemote {

	@EJB
	private AccionProcedimientoDAOLocal accionDao;
	
	public List<AccionProcedimiento> getAllSorted() {
		return accionDao.getAllOrderBy("nombre");
	}

	public AccionProcedimiento save(AccionProcedimiento accion) throws ValidacionException {
		if(accionDao.existsAccionProcedimiento(accion)) {
			List<String> strList = new ArrayList<String>();
			strList.add(accion.getNombre().toString());
			strList.add(accion.getSectorMaquina().getDescripcion());
			throw new ValidacionException(EValidacionException.ACCION_PROCEDIMIENTO_EXISTENTE.getInfoValidacion(), strList);
		}
		return accionDao.save(accion);
	}

	public List<AccionProcedimiento> getAllSortedBySector(ESectorMaquina sector) {
		return accionDao.getAllSortedBySector(sector);
	}

}

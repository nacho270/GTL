package ar.com.textillevel.modulos.personal.facade.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.modulos.personal.dao.api.EmpleadoDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.configuracion.DatosAlarmaFinContrato;
import ar.com.textillevel.modulos.personal.entidades.contratos.ETipoContrato;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.entidades.legajos.to.DatosVencimientoContratoEmpleadoTO;
import ar.com.textillevel.modulos.personal.enums.ETipoBusquedaEmpleados;
import ar.com.textillevel.modulos.personal.facade.api.local.EmpleadoFacadeLocal;
import ar.com.textillevel.modulos.personal.facade.api.remote.EmpleadoFacadeRemote;

@Stateless
public class EmpleadoFacade implements EmpleadoFacadeRemote,EmpleadoFacadeLocal{

	@EJB
	private EmpleadoDAOLocal empleadoDao;
	
	public Empleado save(Empleado empleado) {
		return empleadoDao.save(empleado);
	}

	public Empleado getByIdTotalmenteEager(Integer idEmpleado) {
		return empleadoDao.getByIdTotalmenteEager(idEmpleado);
	}

	public void remove(Empleado empleado) {
		empleadoDao.removeById(empleado.getId());
	}

	public List<Empleado> getAllOrderByName(Boolean incluirPrivados) {
		return empleadoDao.getAllOrderByName(incluirPrivados);
	}

	public List<Empleado> buscarEmpleados(Integer nroLegajo, ETipoBusquedaEmpleados modoBusqueda, Sindicato sindicato, String nombreOApellido, Boolean incluirPrivados, ETipoContrato tipoDeContrato) {
		return empleadoDao.buscarEmpleados(nroLegajo,modoBusqueda,sindicato,nombreOApellido,incluirPrivados,tipoDeContrato);
	}
	
	public Integer getProximoNumeroLegajo(){
		return empleadoDao.getProximoNumeroLegajo();
	}

	public Empleado getEmpleadoByNumeroLegajo(Integer nroLegajo) {
		return empleadoDao.getEmpleadoByNumeroLegajo(nroLegajo);
	}

	public List<DatosVencimientoContratoEmpleadoTO> getEmpleadosConContratoPorVencer(List<DatosAlarmaFinContrato> datosAlarmas) {
		List<DatosVencimientoContratoEmpleadoTO> datosReturn = new ArrayList<DatosVencimientoContratoEmpleadoTO>();
		for(DatosAlarmaFinContrato datos : datosAlarmas){
			//hoy + 30 dias >= fecha de vencimiento (fecha de contrato + duracion)
			Date fecha = DateUtil.sumarDias(DateUtil.getHoy(), datos.getDiasAntes());
			List<DatosVencimientoContratoEmpleadoTO> lista = empleadoDao.getEmpleadosConContratosQueVencenEnODespuesDeFecha(fecha,datos.getTipoContrato());
			if(lista!=null && !lista.isEmpty()){
				datosReturn.addAll(lista);
			}
		}
		
		return datosReturn;
	}

	public LegajoEmpleado getLegajoByNumero(Integer nroLegajo) {
		return empleadoDao.getLegajoByNumero(nroLegajo);
	}

	public List<Empleado> getAllOrderByApellido(String apellido, Boolean incluirPrivados) {
		return empleadoDao.getAllOrderByApellido(apellido, incluirPrivados);
	}
}

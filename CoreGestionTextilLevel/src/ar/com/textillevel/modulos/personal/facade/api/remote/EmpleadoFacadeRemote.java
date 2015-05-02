package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.personal.entidades.configuracion.DatosAlarmaFinContrato;
import ar.com.textillevel.modulos.personal.entidades.contratos.ETipoContrato;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.entidades.legajos.to.DatosVencimientoContratoEmpleadoTO;
import ar.com.textillevel.modulos.personal.enums.ETipoBusquedaEmpleados;

@Remote
public interface EmpleadoFacadeRemote {
	public Empleado save(Empleado empleado);
	public Empleado getByIdTotalmenteEager(Integer idEmpleado);
	public void remove(Empleado empleado);
	public List<Empleado> getAllOrderByName(Boolean incluirPrivados);
	public List<Empleado> buscarEmpleados(Integer nroLegajo, ETipoBusquedaEmpleados modoBusqueda, Sindicato sindicato, String nombreOApellido, Boolean incluirPrivados, ETipoContrato tipoDeContrato);
	public Integer getProximoNumeroLegajo();
	public Empleado getEmpleadoByNumeroLegajo(Integer nroLegajo);
	public List<DatosVencimientoContratoEmpleadoTO> getEmpleadosConContratoPorVencer(List<DatosAlarmaFinContrato> datosAlarmas);
	public LegajoEmpleado getLegajoByNumero(Integer nroLegajo);
	public List<Empleado> getAllOrderByApellido(String apellido, Boolean incluirPrivados);
}

package ar.com.textillevel.modulos.personal.dao.api;

import java.sql.Date;
import java.util.List;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.personal.entidades.contratos.ETipoContrato;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.entidades.legajos.to.DatosVencimientoContratoEmpleadoTO;
import ar.com.textillevel.modulos.personal.enums.ETipoBusquedaEmpleados;

@Local
public interface EmpleadoDAOLocal extends DAOLocal<Empleado, Integer> {
	
	public Empleado getByIdTotalmenteEager(Integer id);
	public Integer getProximoNumeroLegajo();
	public List<Empleado> buscarEmpleados(Integer nroLegajo, ETipoBusquedaEmpleados modoBusqueda, Sindicato sindicato, String nombreOApellido, Boolean incluirPrivados, ETipoContrato tipoDeContrato);
	public Empleado getEmpleadoByNumeroLegajo(Integer nroLegajo);
	public List<Empleado> getAllOrderByName(Boolean incluirPrivados);
	public List<DatosVencimientoContratoEmpleadoTO> getEmpleadosConContratosQueVencenEnODespuesDeFecha(Date fecha, ETipoContrato tipoContrato);
	public LegajoEmpleado getLegajoByNumero(Integer nroLegajo);
	public List<Empleado> getAllActivosBySindicato(Sindicato sindicato, Integer nroLegajo, String nombreOrApellido);
	public List<Empleado> getAllOrderByApellido(String apellido, Boolean incluirPrivados);

}

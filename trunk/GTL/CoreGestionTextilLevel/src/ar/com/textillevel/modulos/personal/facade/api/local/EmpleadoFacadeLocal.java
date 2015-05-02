package ar.com.textillevel.modulos.personal.facade.api.local;

import javax.ejb.Local;

import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;

@Local
public interface EmpleadoFacadeLocal {
	public Empleado save(Empleado empleado);
	public LegajoEmpleado getLegajoByNumero(Integer nroLegajo);
}

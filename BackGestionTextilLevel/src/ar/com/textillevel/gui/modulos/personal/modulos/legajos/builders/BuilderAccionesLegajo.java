package ar.com.textillevel.gui.modulos.personal.modulos.legajos.builders;

import java.util.ArrayList;
import java.util.List;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.acciones.Accion;
import ar.clarin.fwjava.templates.modulo.model.acciones.Acciones;
import ar.clarin.fwjava.templates.modulo.model.acciones.IBuilderAcciones;
import ar.clarin.fwjava.templates.modulo.model.accionesmouse.AccionesAdicionales;
import ar.clarin.fwjava.templates.modulo.model.accionesmouse.IBuilderAccionAdicional;
import ar.clarin.fwjava.templates.modulo.model.filtros.Filtros;
import ar.clarin.fwjava.templates.modulo.model.filtros.IBuilderFiltros;
import ar.clarin.fwjava.templates.modulo.model.tabla.IBuilderTabla;
import ar.clarin.fwjava.templates.modulo.model.tabla.Tabla;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.acciones.AccionAgregarLegajo;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.acciones.AccionAgregarVale;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.acciones.AccionAsignarVacaciones;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.acciones.AccionBajaLegajo;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.acciones.AccionDobleClickResumenLegajo;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.acciones.AccionImprimirContrato;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.acciones.AccionPrevisualizarContrato;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.acciones.AccionResumenLegajo;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.acciones.AccionVerFichadas;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.columnas.ColumnaCUITLegajo;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.columnas.ColumnaFechaAltaLegajo;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.columnas.ColumnaFechaBajaLegajo;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.columnas.ColumnaNombreApellidoEmpleado;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.columnas.ColumnaNumeroLegajo;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.columnas.ColumnaObservacionesBajaEmpleado;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.columnas.ColumnaSindicatoEmpleado;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.columnas.ColumnaTelefonoEmpleado;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.columnas.ColumnaTipoContratoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;

public class BuilderAccionesLegajo implements IBuilderAcciones<Empleado>,
											  IBuilderFiltros<Empleado>,
											  IBuilderTabla<Empleado>, 
											  IBuilderAccionAdicional<Empleado> {

	private static BuilderAccionesLegajo instance = new BuilderAccionesLegajo();

	public static BuilderAccionesLegajo getInstance() {
		return instance;
	}

	public Tabla<Empleado> construirTabla(int idModel) {
		Tabla<Empleado> tabla = new Tabla<Empleado>();
		tabla.addColumna(new ColumnaNumeroLegajo());
		tabla.addColumna(new ColumnaNombreApellidoEmpleado());
		tabla.addColumna(new ColumnaCUITLegajo());
		tabla.addColumna(new ColumnaSindicatoEmpleado());
		tabla.addColumna(new ColumnaTelefonoEmpleado());
		tabla.addColumna(new ColumnaFechaAltaLegajo());
		tabla.addColumna(new ColumnaTipoContratoEmpleado());
		tabla.addColumna(new ColumnaFechaBajaLegajo());
		tabla.addColumna(new ColumnaObservacionesBajaEmpleado());
		return tabla;
	}

	public Acciones<Empleado> construirAcciones(int idModel) throws CLException {
		Acciones<Empleado> acciones = new Acciones<Empleado>();
		
		List<Accion<Empleado>> accionesCreacion = new ArrayList<Accion<Empleado>>();
		accionesCreacion.add(new AccionAgregarLegajo());
		accionesCreacion.add(new AccionBajaLegajo());
		accionesCreacion.add(new AccionImprimirContrato());
		accionesCreacion.add(new AccionPrevisualizarContrato());
		accionesCreacion.add(new AccionResumenLegajo());
		acciones.addElementGroup("Acciones",accionesCreacion);
		
		List<Accion<Empleado>> accionesReciboSueldo = new ArrayList<Accion<Empleado>>();
		accionesReciboSueldo.add(new AccionVerFichadas());
		accionesReciboSueldo.add(new AccionAgregarVale());
		accionesReciboSueldo.add(new AccionAsignarVacaciones());
		acciones.addElementGroup("Recibo sueldo",accionesReciboSueldo);
		
		return acciones;
	}

	public Filtros<Empleado> construirFiltros(int idModel) {
		return null;
	}

	public AccionesAdicionales<Empleado> construirAccionAdicional(int idModel) throws CLException {
		AccionesAdicionales<Empleado> acciones = new AccionesAdicionales<Empleado>();
		acciones.addSingleElement(new AccionDobleClickResumenLegajo());
		return acciones;
	}

}
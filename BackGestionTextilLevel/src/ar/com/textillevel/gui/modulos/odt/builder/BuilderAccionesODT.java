package ar.com.textillevel.gui.modulos.odt.builder;

import java.util.ArrayList;
import java.util.List;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.acciones.Acciones;
import ar.com.fwcommon.templates.modulo.model.acciones.IBuilderAcciones;
import ar.com.fwcommon.templates.modulo.model.accionesmouse.AccionesAdicionales;
import ar.com.fwcommon.templates.modulo.model.accionesmouse.IBuilderAccionAdicional;
import ar.com.fwcommon.templates.modulo.model.filtros.Filtros;
import ar.com.fwcommon.templates.modulo.model.filtros.IBuilderFiltros;
import ar.com.fwcommon.templates.modulo.model.tabla.IBuilderTabla;
import ar.com.fwcommon.templates.modulo.model.tabla.Tabla;
import ar.com.fwcommon.templates.modulo.model.totales.IBuilderTotales;
import ar.com.fwcommon.templates.modulo.model.totales.Totales;
import ar.com.textillevel.gui.modulos.odt.acciones.AccionBorrarSecuenciaODT;
import ar.com.textillevel.gui.modulos.odt.acciones.AccionCargarSecuenciaDeTrabajoODT;
import ar.com.textillevel.gui.modulos.odt.acciones.AccionDobleClickODT;
import ar.com.textillevel.gui.modulos.odt.acciones.AccionImprimirODT;
import ar.com.textillevel.gui.modulos.odt.columnas.ColumnaClienteODT;
import ar.com.textillevel.gui.modulos.odt.columnas.ColumnaCodigoODT;
import ar.com.textillevel.gui.modulos.odt.columnas.ColumnaEstadoODT;
import ar.com.textillevel.gui.modulos.odt.columnas.ColumnaProductoODT;
import ar.com.textillevel.gui.modulos.odt.columnas.ColumnaSecuenciaAsignada;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;

public class BuilderAccionesODT implements IBuilderAcciones<OrdenDeTrabajo>,
										   IBuilderFiltros<OrdenDeTrabajo>, 
										   IBuilderTabla<OrdenDeTrabajo>, 
										   IBuilderTotales<OrdenDeTrabajo>,
										   IBuilderAccionAdicional<OrdenDeTrabajo> {
	
	private static BuilderAccionesODT instance = new BuilderAccionesODT();

	public static BuilderAccionesODT getInstance() {
		return instance;
	}
	
	public Acciones<OrdenDeTrabajo> construirAcciones(int idModel) throws FWException {
		Acciones<OrdenDeTrabajo> acciones = new Acciones<OrdenDeTrabajo>();
		List<Accion<OrdenDeTrabajo>> accionesCreacion = new ArrayList<Accion<OrdenDeTrabajo>>();
		accionesCreacion.add(new AccionCargarSecuenciaDeTrabajoODT());
		accionesCreacion.add(new AccionImprimirODT());
		accionesCreacion.add(new AccionBorrarSecuenciaODT());
		acciones.addElementGroup("Acciones",accionesCreacion);
		return acciones;
	}

	public Filtros<OrdenDeTrabajo> construirFiltros(int idModel) {
		return null;
	}

	public Tabla<OrdenDeTrabajo> construirTabla(int idModel) {
		Tabla<OrdenDeTrabajo> tabla = new Tabla<OrdenDeTrabajo>();
		tabla.addColumna(new ColumnaEstadoODT());
		tabla.addColumna(new ColumnaCodigoODT());
		tabla.addColumna(new ColumnaProductoODT());
		tabla.addColumna(new ColumnaClienteODT());
		tabla.addColumna(new ColumnaSecuenciaAsignada());
		return tabla;
	}

	public Totales<OrdenDeTrabajo> construirTotales(int idModel) {
		return null;
	}

	public AccionesAdicionales<OrdenDeTrabajo> construirAccionAdicional(int idModel) throws FWException {
		AccionesAdicionales<OrdenDeTrabajo> acciones = new AccionesAdicionales<OrdenDeTrabajo>();
		acciones.addSingleElement(new AccionDobleClickODT());
		return acciones;
	}
}

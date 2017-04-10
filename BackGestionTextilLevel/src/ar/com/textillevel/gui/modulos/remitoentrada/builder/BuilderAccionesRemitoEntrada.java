package ar.com.textillevel.gui.modulos.remitoentrada.builder;

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
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.gui.modulos.remitoentrada.acciones.AccionCambiarClienteRE;
import ar.com.textillevel.gui.modulos.remitoentrada.acciones.AccionDobleClickRemitoEntrada;
import ar.com.textillevel.gui.modulos.remitoentrada.acciones.AccionEliminarRemitoEntrada;
import ar.com.textillevel.gui.modulos.remitoentrada.acciones.AccionModificarRemitoEntrada;
import ar.com.textillevel.gui.modulos.remitoentrada.columnas.ColumnaClienteRE;
import ar.com.textillevel.gui.modulos.remitoentrada.columnas.ColumnaFechaRE;
import ar.com.textillevel.gui.modulos.remitoentrada.columnas.ColumnaNroRE;
import ar.com.textillevel.gui.modulos.remitoentrada.columnas.ColumnaProductosRE;

public class BuilderAccionesRemitoEntrada implements IBuilderAcciones<RemitoEntrada>,
										   IBuilderFiltros<RemitoEntrada>, 
										   IBuilderTabla<RemitoEntrada>, 
										   IBuilderTotales<RemitoEntrada>,
										   IBuilderAccionAdicional<RemitoEntrada> {

	private static BuilderAccionesRemitoEntrada instance = new BuilderAccionesRemitoEntrada();

	public static BuilderAccionesRemitoEntrada getInstance() {
		return instance;
	}
	
	public Acciones<RemitoEntrada> construirAcciones(int idModel) throws FWException {
		Acciones<RemitoEntrada> acciones = new Acciones<RemitoEntrada>();
		List<Accion<RemitoEntrada>> accionesCreacion = new ArrayList<Accion<RemitoEntrada>>();
		accionesCreacion.add(new AccionEliminarRemitoEntrada());
		accionesCreacion.add(new AccionModificarRemitoEntrada());
		accionesCreacion.add(new AccionCambiarClienteRE());
		acciones.addElementGroup("Acciones",accionesCreacion);
		return acciones;
	}

	public Filtros<RemitoEntrada> construirFiltros(int idModel) {
		return null;
	}

	public Tabla<RemitoEntrada> construirTabla(int idModel) {
		Tabla<RemitoEntrada> tabla = new Tabla<RemitoEntrada>();
		tabla.addColumna(new ColumnaNroRE());
		tabla.addColumna(new ColumnaFechaRE());
		tabla.addColumna(new ColumnaClienteRE());
		tabla.addColumna(new ColumnaProductosRE());
		return tabla;
	}

	public Totales<RemitoEntrada> construirTotales(int idModel) {
		return null;
	}

	public AccionesAdicionales<RemitoEntrada> construirAccionAdicional(int idModel) throws FWException {
		AccionesAdicionales<RemitoEntrada> acciones = new AccionesAdicionales<RemitoEntrada>();
		acciones.addSingleElement(new AccionDobleClickRemitoEntrada());
		return acciones;
	}

}

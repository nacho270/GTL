package ar.com.textillevel.gui.modulos.remitosalida.builder;

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
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.gui.modulos.remitosalida.acciones.AccionAnularRemitoSalida;
import ar.com.textillevel.gui.modulos.remitosalida.acciones.AccionDobleClickRemitoSalida;
import ar.com.textillevel.gui.modulos.remitosalida.acciones.AccionEliminarRemitoSalida;
import ar.com.textillevel.gui.modulos.remitosalida.acciones.AccionModificarRemitoSalida;
import ar.com.textillevel.gui.modulos.remitosalida.columnas.ColumnaAnuladoRS;
import ar.com.textillevel.gui.modulos.remitosalida.columnas.ColumnaClienteRS;
import ar.com.textillevel.gui.modulos.remitosalida.columnas.ColumnaFechaRS;
import ar.com.textillevel.gui.modulos.remitosalida.columnas.ColumnaNroFacturaRS;
import ar.com.textillevel.gui.modulos.remitosalida.columnas.ColumnaNroRS;
import ar.com.textillevel.gui.modulos.remitosalida.columnas.ColumnaProveedorRS;

public class BuilderAccionesRemitoSalida implements IBuilderAcciones<RemitoSalida>,
										   IBuilderFiltros<RemitoSalida>, 
										   IBuilderTabla<RemitoSalida>, 
										   IBuilderTotales<RemitoSalida>,
										   IBuilderAccionAdicional<RemitoSalida> {

	private static BuilderAccionesRemitoSalida instance = new BuilderAccionesRemitoSalida();

	public static BuilderAccionesRemitoSalida getInstance() {
		return instance;
	}
	
	public Acciones<RemitoSalida> construirAcciones(int idModel) throws FWException {
		Acciones<RemitoSalida> acciones = new Acciones<RemitoSalida>();
		List<Accion<RemitoSalida>> accionesCreacion = new ArrayList<Accion<RemitoSalida>>();
		accionesCreacion.add(new AccionEliminarRemitoSalida());
		accionesCreacion.add(new AccionAnularRemitoSalida());
		accionesCreacion.add(new AccionModificarRemitoSalida());
		acciones.addElementGroup("Acciones",accionesCreacion);
		return acciones;
	}

	public Filtros<RemitoSalida> construirFiltros(int idModel) {
		return null;
	}

	public Tabla<RemitoSalida> construirTabla(int idModel) {
		Tabla<RemitoSalida> tabla = new Tabla<RemitoSalida>();
		tabla.addColumna(new ColumnaNroRS());
		tabla.addColumna(new ColumnaFechaRS());
		tabla.addColumna(new ColumnaClienteRS());
		tabla.addColumna(new ColumnaProveedorRS());
		tabla.addColumna(new ColumnaNroFacturaRS());
		tabla.addColumna(new ColumnaAnuladoRS());
		return tabla;
	}

	public Totales<RemitoSalida> construirTotales(int idModel) {
		return null;
	}

	public AccionesAdicionales<RemitoSalida> construirAccionAdicional(int idModel) throws FWException {
		AccionesAdicionales<RemitoSalida> acciones = new AccionesAdicionales<RemitoSalida>();
		acciones.addSingleElement(new AccionDobleClickRemitoSalida());
		return acciones;
	}

}

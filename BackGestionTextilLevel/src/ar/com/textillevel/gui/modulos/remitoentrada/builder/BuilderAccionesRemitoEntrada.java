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
import ar.com.textillevel.gui.modulos.remitoentrada.acciones.AccionCambiarClienteRE;
import ar.com.textillevel.gui.modulos.remitoentrada.acciones.AccionDobleClickRemitoEntrada;
import ar.com.textillevel.gui.modulos.remitoentrada.acciones.AccionEliminarRemitoEntrada;
import ar.com.textillevel.gui.modulos.remitoentrada.acciones.AccionModificarRemitoEntrada;
import ar.com.textillevel.gui.modulos.remitoentrada.columnas.ColumnaClienteRE;
import ar.com.textillevel.gui.modulos.remitoentrada.columnas.ColumnaFechaRE;
import ar.com.textillevel.gui.modulos.remitoentrada.columnas.ColumnaNroRE;
import ar.com.textillevel.gui.modulos.remitoentrada.columnas.ColumnaProductosRE;
import ar.com.textillevel.gui.modulos.remitoentrada.to.RemitoEntradaModuloTO;

public class BuilderAccionesRemitoEntrada implements IBuilderAcciones<RemitoEntradaModuloTO>,
										   IBuilderFiltros<RemitoEntradaModuloTO>, 
										   IBuilderTabla<RemitoEntradaModuloTO>, 
										   IBuilderTotales<RemitoEntradaModuloTO>,
										   IBuilderAccionAdicional<RemitoEntradaModuloTO> {

	private static BuilderAccionesRemitoEntrada instance = new BuilderAccionesRemitoEntrada();

	public static BuilderAccionesRemitoEntrada getInstance() {
		return instance;
	}
	
	public Acciones<RemitoEntradaModuloTO> construirAcciones(int idModel) throws FWException {
		Acciones<RemitoEntradaModuloTO> acciones = new Acciones<RemitoEntradaModuloTO>();
		List<Accion<RemitoEntradaModuloTO>> accionesCreacion = new ArrayList<Accion<RemitoEntradaModuloTO>>();
		accionesCreacion.add(new AccionEliminarRemitoEntrada());
		accionesCreacion.add(new AccionModificarRemitoEntrada());
		accionesCreacion.add(new AccionCambiarClienteRE());
		acciones.addElementGroup("Acciones",accionesCreacion);
		return acciones;
	}

	public Filtros<RemitoEntradaModuloTO> construirFiltros(int idModel) {
		return null;
	}

	public Tabla<RemitoEntradaModuloTO> construirTabla(int idModel) {
		Tabla<RemitoEntradaModuloTO> tabla = new Tabla<RemitoEntradaModuloTO>();
		tabla.addColumna(new ColumnaNroRE());
		tabla.addColumna(new ColumnaFechaRE());
		tabla.addColumna(new ColumnaClienteRE());
		tabla.addColumna(new ColumnaProductosRE());
		return tabla;
	}

	public Totales<RemitoEntradaModuloTO> construirTotales(int idModel) {
		return null;
	}

	public AccionesAdicionales<RemitoEntradaModuloTO> construirAccionAdicional(int idModel) throws FWException {
		AccionesAdicionales<RemitoEntradaModuloTO> acciones = new AccionesAdicionales<RemitoEntradaModuloTO>();
		acciones.addSingleElement(new AccionDobleClickRemitoEntrada());
		return acciones;
	}

}

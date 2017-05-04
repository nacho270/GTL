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
import ar.com.textillevel.gui.modulos.odt.acciones.AccionDarSalidaPiezasODT;
import ar.com.textillevel.gui.modulos.odt.acciones.AccionDobleClickODT;
import ar.com.textillevel.gui.modulos.odt.acciones.AccionImprimirODT;
import ar.com.textillevel.gui.modulos.odt.columnas.ColumnaClienteODT;
import ar.com.textillevel.gui.modulos.odt.columnas.ColumnaCodigoODT;
import ar.com.textillevel.gui.modulos.odt.columnas.ColumnaEstadoODT;
import ar.com.textillevel.gui.modulos.odt.columnas.ColumnaProductoODT;
import ar.com.textillevel.gui.modulos.odt.columnas.ColumnaSecuenciaAsignada;
import ar.com.textillevel.modulos.odt.to.ODTTO;

public class BuilderAccionesODT implements IBuilderAcciones<ODTTO>,
										   IBuilderFiltros<ODTTO>, 
										   IBuilderTabla<ODTTO>, 
										   IBuilderTotales<ODTTO>,
										   IBuilderAccionAdicional<ODTTO> {
	
	private static BuilderAccionesODT instance = new BuilderAccionesODT();

	public static BuilderAccionesODT getInstance() {
		return instance;
	}
	
	public Acciones<ODTTO> construirAcciones(int idModel) throws FWException {
		Acciones<ODTTO> acciones = new Acciones<ODTTO>();
		List<Accion<ODTTO>> accionesCreacion = new ArrayList<Accion<ODTTO>>();
		accionesCreacion.add(new AccionCargarSecuenciaDeTrabajoODT());
		accionesCreacion.add(new AccionImprimirODT());
		accionesCreacion.add(new AccionBorrarSecuenciaODT());
		accionesCreacion.add(new AccionDarSalidaPiezasODT());
		acciones.addElementGroup("Acciones",accionesCreacion);
		return acciones;
	}

	public Filtros<ODTTO> construirFiltros(int idModel) {
		return null;
	}

	public Tabla<ODTTO> construirTabla(int idModel) {
		Tabla<ODTTO> tabla = new Tabla<ODTTO>();
		tabla.setTablaColorManager(new ColorManagerItemODT());
		tabla.addColumna(new ColumnaEstadoODT());
		tabla.addColumna(new ColumnaCodigoODT());
		tabla.addColumna(new ColumnaProductoODT());
		tabla.addColumna(new ColumnaClienteODT());
		tabla.addColumna(new ColumnaSecuenciaAsignada());
		return tabla;
	}

	public Totales<ODTTO> construirTotales(int idModel) {
		return null;
	}

	public AccionesAdicionales<ODTTO> construirAccionAdicional(int idModel) throws FWException {
		AccionesAdicionales<ODTTO> acciones = new AccionesAdicionales<ODTTO>();
		acciones.addSingleElement(new AccionDobleClickODT());
		return acciones;
	}
}

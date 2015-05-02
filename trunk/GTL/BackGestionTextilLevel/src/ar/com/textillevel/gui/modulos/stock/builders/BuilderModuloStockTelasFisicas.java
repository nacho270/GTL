package ar.com.textillevel.gui.modulos.stock.builders;

import java.util.ArrayList;
import java.util.List;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.acciones.Accion;
import ar.clarin.fwjava.templates.modulo.model.acciones.Acciones;
import ar.clarin.fwjava.templates.modulo.model.acciones.IBuilderAcciones;
import ar.clarin.fwjava.templates.modulo.model.filtros.Filtros;
import ar.clarin.fwjava.templates.modulo.model.filtros.IBuilderFiltros;
import ar.clarin.fwjava.templates.modulo.model.status.IBuilderStatuses;
import ar.clarin.fwjava.templates.modulo.model.status.Statuses;
import ar.clarin.fwjava.templates.modulo.model.tabla.IBuilderTabla;
import ar.clarin.fwjava.templates.modulo.model.tabla.Tabla;
import ar.clarin.fwjava.templates.modulo.model.totales.IBuilderTotales;
import ar.clarin.fwjava.templates.modulo.model.totales.TotalGeneral;
import ar.clarin.fwjava.templates.modulo.model.totales.Totales;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.gui.modulos.stock.acciones.AccionBorrarPiezasEnMemoria;
import ar.com.textillevel.gui.modulos.stock.acciones.AccionDarSalida01PiezasEnMemoria;
import ar.com.textillevel.gui.modulos.stock.acciones.AccionVentaDeTelaPiezasEnMemoria;
import ar.com.textillevel.gui.modulos.stock.acciones.AccionVerDetallePiezasCrudas;
import ar.com.textillevel.gui.modulos.stock.acciones.AccionVerDetallePiezasTerminadas;
import ar.com.textillevel.gui.modulos.stock.columnas.telas.ColumnaStockComposicion;
import ar.com.textillevel.gui.modulos.stock.columnas.telas.ColumnaStockCrudo;
import ar.com.textillevel.gui.modulos.stock.columnas.telas.ColumnaStockFisico;
import ar.com.textillevel.gui.modulos.stock.columnas.telas.ColumnaStockGramaje;
import ar.com.textillevel.gui.modulos.stock.columnas.telas.ColumnaStockTerminado;
import ar.com.textillevel.gui.modulos.stock.status.StatusPiezasMemoria;

public class BuilderModuloStockTelasFisicas implements IBuilderAcciones<ItemMateriaPrimaTO>, 
													   IBuilderTabla<ItemMateriaPrimaTO>,
													   IBuilderFiltros<ItemMateriaPrimaTO>,
													   IBuilderTotales<ItemMateriaPrimaTO>,
													   IBuilderStatuses<ItemMateriaPrimaTO> {
	
	private static BuilderModuloStockTelasFisicas instance = new BuilderModuloStockTelasFisicas();

	public static BuilderModuloStockTelasFisicas getInstance() {
		return instance;
	}

	public Acciones<ItemMateriaPrimaTO> construirAcciones(int idModel) throws CLException {
		Acciones<ItemMateriaPrimaTO> acciones = new Acciones<ItemMateriaPrimaTO>();
		List<Accion<ItemMateriaPrimaTO>> accionesCreacion = new ArrayList<Accion<ItemMateriaPrimaTO>>();
		accionesCreacion.add(new AccionDarSalida01PiezasEnMemoria());
		accionesCreacion.add(new AccionVentaDeTelaPiezasEnMemoria());
		accionesCreacion.add(new AccionBorrarPiezasEnMemoria());
		accionesCreacion.add(new AccionVerDetallePiezasCrudas());
		accionesCreacion.add(new AccionVerDetallePiezasTerminadas());
		acciones.addElementGroup("Acciones", accionesCreacion);
		return acciones;
	}

	public Tabla<ItemMateriaPrimaTO> construirTabla(int idModel) {
		Tabla<ItemMateriaPrimaTO> tabla = new Tabla<ItemMateriaPrimaTO>();
		tabla.addColumna(new ColumnaStockComposicion());
		tabla.addColumna(new ColumnaStockGramaje());
		tabla.addColumna(new ColumnaStockFisico());
		tabla.addColumna(new ColumnaStockCrudo());
		tabla.addColumna(new ColumnaStockTerminado());
		return tabla;
	}

	public Filtros<ItemMateriaPrimaTO> construirFiltros(int idModel) {
		return null;
	}

	public Totales<ItemMateriaPrimaTO> construirTotales(int idModel) {
		Totales<ItemMateriaPrimaTO> totales = new Totales<ItemMateriaPrimaTO>();
		totales.addSingleElement(new TotalGeneral<ItemMateriaPrimaTO>());
		return totales;
	}

	public Statuses<ItemMateriaPrimaTO> construirStatuses(int idModel) {
		Statuses<ItemMateriaPrimaTO> s = new Statuses<ItemMateriaPrimaTO>();
		s.addSingleElement(new StatusPiezasMemoria());
		return s;
	}

}

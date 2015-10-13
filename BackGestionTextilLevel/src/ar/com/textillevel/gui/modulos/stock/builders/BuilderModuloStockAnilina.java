package ar.com.textillevel.gui.modulos.stock.builders;

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
import ar.com.fwcommon.templates.modulo.model.totales.TotalGeneral;
import ar.com.fwcommon.templates.modulo.model.totales.Totales;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.gui.modulos.stock.acciones.AccionDobleClickVerMovimientosStock;
import ar.com.textillevel.gui.modulos.stock.acciones.AccionVerMovimientosStockPrecioMateriaPrima;
import ar.com.textillevel.gui.modulos.stock.columnas.anilinas.ColumnaStockColorIndex;
import ar.com.textillevel.gui.modulos.stock.columnas.anilinas.ColumnaStockTipoAnilina;
import ar.com.textillevel.gui.modulos.stock.columnas.commons.ColumnaPrecioMateriaPrimaStockActual;
import ar.com.textillevel.gui.modulos.stock.columnas.commons.ColumnaPrecioMateriaPrimaUnidad;
import ar.com.textillevel.gui.modulos.stock.columnas.commons.ColumnaStockConcetracion;
import ar.com.textillevel.gui.modulos.stock.columnas.commons.ColumnaStockDescripcion;
import ar.com.textillevel.gui.modulos.stock.filtros.FiltroTipoAnilina;

public class BuilderModuloStockAnilina implements IBuilderAccionAdicional<ItemMateriaPrimaTO>, 
												  IBuilderAcciones<ItemMateriaPrimaTO>, 
												  IBuilderTabla<ItemMateriaPrimaTO>,
												  IBuilderFiltros<ItemMateriaPrimaTO>,
												  IBuilderTotales<ItemMateriaPrimaTO>{

	private static BuilderModuloStockAnilina instance = new BuilderModuloStockAnilina();
	
	private FiltroTipoAnilina filtroAnilina;

	public static BuilderModuloStockAnilina getInstance() {
		return instance;
	}

	public AccionesAdicionales<ItemMateriaPrimaTO> construirAccionAdicional(int idModel) throws FWException {
		AccionesAdicionales<ItemMateriaPrimaTO> acciones = new AccionesAdicionales<ItemMateriaPrimaTO>();
		acciones.addSingleElement(new AccionDobleClickVerMovimientosStock());
		return acciones;
	}

	public Acciones<ItemMateriaPrimaTO> construirAcciones(int idModel) throws FWException {
		Acciones<ItemMateriaPrimaTO> acciones = new Acciones<ItemMateriaPrimaTO>();
		List<Accion<ItemMateriaPrimaTO>> accionesCreacion = new ArrayList<Accion<ItemMateriaPrimaTO>>();
		accionesCreacion.add(new AccionVerMovimientosStockPrecioMateriaPrima());
		acciones.addElementGroup("Acciones",accionesCreacion);
		return acciones;
	}

	public Tabla<ItemMateriaPrimaTO> construirTabla(int idModel) {
		Tabla<ItemMateriaPrimaTO> tabla = new Tabla<ItemMateriaPrimaTO>();
		tabla.addColumna(new ColumnaStockDescripcion("Nombre"));
		tabla.addColumna(new ColumnaStockColorIndex());
		tabla.addColumna(new ColumnaStockConcetracion());
		tabla.addColumna(new ColumnaPrecioMateriaPrimaStockActual());
		tabla.addColumna(new ColumnaPrecioMateriaPrimaUnidad());
		tabla.addColumna(new ColumnaStockTipoAnilina());
		return tabla;
	}

	public Filtros<ItemMateriaPrimaTO> construirFiltros(int idModel) {
		Filtros<ItemMateriaPrimaTO> filtros = new Filtros<ItemMateriaPrimaTO>();
		filtros.addSingleElement(getFiltroTipoAnilina());
		return filtros;
	}
	
	public FiltroTipoAnilina getFiltroTipoAnilina() {
		if(filtroAnilina == null){
			filtroAnilina = new FiltroTipoAnilina();
		}
		return filtroAnilina;
	}

	public Totales<ItemMateriaPrimaTO> construirTotales(int idModel) {
		Totales<ItemMateriaPrimaTO> totales = new Totales<ItemMateriaPrimaTO>();
		totales.addSingleElement(new TotalGeneral<ItemMateriaPrimaTO>());
		return totales;
	}
}

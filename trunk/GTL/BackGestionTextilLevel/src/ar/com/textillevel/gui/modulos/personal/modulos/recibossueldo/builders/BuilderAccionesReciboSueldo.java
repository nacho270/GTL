package ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.builders;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.acciones.Acciones;
import ar.clarin.fwjava.templates.modulo.model.acciones.IBuilderAcciones;
import ar.clarin.fwjava.templates.modulo.model.accionesmouse.AccionesAdicionales;
import ar.clarin.fwjava.templates.modulo.model.accionesmouse.IBuilderAccionAdicional;
import ar.clarin.fwjava.templates.modulo.model.filtros.Filtros;
import ar.clarin.fwjava.templates.modulo.model.filtros.IBuilderFiltros;
import ar.clarin.fwjava.templates.modulo.model.tabla.IBuilderTabla;
import ar.clarin.fwjava.templates.modulo.model.tabla.Tabla;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.acciones.AccionDobleClickReciboSueldo;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.acciones.AccionEliminarReciboSueldo;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.acciones.AccionReciboSueldo;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.columnas.ColumnaEstadoReciboSueldo;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.columnas.ColumnaNombreApellidoEmpleado;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.columnas.ColumnaNumeroLegajo;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.columnas.ColumnaPeriodoReciboSueldo;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.columnas.ColumnaValorBruto;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.columnas.ColumnaValorNeto;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.to.InfoReciboSueltoTO;

public class BuilderAccionesReciboSueldo implements IBuilderAcciones<InfoReciboSueltoTO>,
											  IBuilderFiltros<InfoReciboSueltoTO>,
											  IBuilderTabla<InfoReciboSueltoTO>, 
											  IBuilderAccionAdicional<InfoReciboSueltoTO> {

	private static BuilderAccionesReciboSueldo instance = new BuilderAccionesReciboSueldo();

	public static BuilderAccionesReciboSueldo getInstance() {
		return instance;
	}

	public Tabla<InfoReciboSueltoTO> construirTabla(int idModel) {
		Tabla<InfoReciboSueltoTO> tabla = new Tabla<InfoReciboSueltoTO>();
		tabla.addColumna(new ColumnaNombreApellidoEmpleado());
		tabla.addColumna(new ColumnaNumeroLegajo());
		tabla.addColumna(new ColumnaEstadoReciboSueldo());
		tabla.addColumna(new ColumnaValorBruto());
		tabla.addColumna(new ColumnaValorNeto());
		tabla.addColumna(new ColumnaPeriodoReciboSueldo());
		return tabla;
	}

	public Acciones<InfoReciboSueltoTO> construirAcciones(int idModel) throws CLException {
		Acciones<InfoReciboSueltoTO> acciones = new Acciones<InfoReciboSueltoTO>();
		acciones.addSingleElement(new AccionReciboSueldo());
		acciones.addSingleElement(new AccionEliminarReciboSueldo());
		//TODO:
		return acciones;
	}

	public Filtros<InfoReciboSueltoTO> construirFiltros(int idModel) {
		return null;
	}

	public AccionesAdicionales<InfoReciboSueltoTO> construirAccionAdicional(int idModel) throws CLException {
		AccionesAdicionales<InfoReciboSueltoTO> acciones = new AccionesAdicionales<InfoReciboSueltoTO>();
		acciones.addSingleElement(new AccionDobleClickReciboSueldo());
		return acciones;
	}

}
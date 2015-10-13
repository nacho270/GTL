package ar.com.textillevel.gui.modulos.cheques.builders;

import java.util.ArrayList;
import java.util.List;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.acciones.Acciones;
import ar.com.fwcommon.templates.modulo.model.acciones.IBuilderAcciones;
import ar.com.fwcommon.templates.modulo.model.filtros.Filtros;
import ar.com.fwcommon.templates.modulo.model.filtros.IBuilderFiltros;
import ar.com.fwcommon.templates.modulo.model.tabla.IBuilderTabla;
import ar.com.fwcommon.templates.modulo.model.tabla.Tabla;
import ar.com.fwcommon.templates.modulo.model.totales.IBuilderTotales;
import ar.com.fwcommon.templates.modulo.model.totales.Totales;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.gui.modulos.cheques.acciones.AccionAgregarCheque;
import ar.com.textillevel.gui.modulos.cheques.acciones.AccionConsultarCheque;
import ar.com.textillevel.gui.modulos.cheques.acciones.AccionEliminarCheque;
import ar.com.textillevel.gui.modulos.cheques.acciones.AccionModificarCheque;
import ar.com.textillevel.gui.modulos.cheques.acciones.AccionPonerChequeRechazadoEnCartera;
import ar.com.textillevel.gui.modulos.cheques.acciones.AccionPonerChequeRechazadoEnDevuelto;
import ar.com.textillevel.gui.modulos.cheques.acciones.AccionPonerChequeSalidaEnCartera;
import ar.com.textillevel.gui.modulos.cheques.acciones.AccionRechazarCheque;
import ar.com.textillevel.gui.modulos.cheques.columnas.ColumnaBancoCheque;
import ar.com.textillevel.gui.modulos.cheques.columnas.ColumnaCapitalOInteriorCheque;
import ar.com.textillevel.gui.modulos.cheques.columnas.ColumnaClienteCheque;
import ar.com.textillevel.gui.modulos.cheques.columnas.ColumnaCuitCheque;
import ar.com.textillevel.gui.modulos.cheques.columnas.ColumnaEstadoCheque;
import ar.com.textillevel.gui.modulos.cheques.columnas.ColumnaFechaDepositoCheque;
import ar.com.textillevel.gui.modulos.cheques.columnas.ColumnaFechaEntradaCheque;
import ar.com.textillevel.gui.modulos.cheques.columnas.ColumnaFechaSalidaCheque;
import ar.com.textillevel.gui.modulos.cheques.columnas.ColumnaImporteCheque;
import ar.com.textillevel.gui.modulos.cheques.columnas.ColumnaNumeracionCheque;
import ar.com.textillevel.gui.modulos.cheques.columnas.ColumnaNumeroCheque;

public class BuilderAccionesCheque implements IBuilderAcciones<Cheque>,
											  IBuilderFiltros<Cheque>, 
											  IBuilderTabla<Cheque>, 
											  IBuilderTotales<Cheque>{
	
	private static BuilderAccionesCheque instance = new BuilderAccionesCheque();

	public static BuilderAccionesCheque getInstance() {
		return instance;
	}
	
	public Acciones<Cheque> construirAcciones(int idModel) throws FWException {
		Acciones<Cheque> acciones = new Acciones<Cheque>();
		List<Accion<Cheque>> accionesCreacion = new ArrayList<Accion<Cheque>>();
		accionesCreacion.add(new AccionAgregarCheque());
		accionesCreacion.add(new AccionModificarCheque());
		accionesCreacion.add(new AccionEliminarCheque());
		accionesCreacion.add(new AccionConsultarCheque());
		accionesCreacion.add(new AccionRechazarCheque());
		accionesCreacion.add(new AccionPonerChequeRechazadoEnCartera());
		accionesCreacion.add(new AccionPonerChequeRechazadoEnDevuelto());
//		accionesCreacion.add(new AccionDarSalidaCheque());
		accionesCreacion.add(new AccionPonerChequeSalidaEnCartera());
		acciones.addElementGroup("Acciones",accionesCreacion);
		return acciones;
	}

	public Filtros<Cheque> construirFiltros(int idModel) {
		return null;
	}

	public Tabla<Cheque> construirTabla(int idModel) {
		Tabla<Cheque> tabla = new Tabla<Cheque>();
		tabla.addColumna(new ColumnaNumeracionCheque());
		tabla.addColumna(new ColumnaFechaEntradaCheque());
		tabla.addColumna(new ColumnaClienteCheque());
		tabla.addColumna(new ColumnaBancoCheque());
		tabla.addColumna(new ColumnaCuitCheque());
		tabla.addColumna(new ColumnaNumeroCheque());
		tabla.addColumna(new ColumnaFechaDepositoCheque());
		tabla.addColumna(new ColumnaImporteCheque());
		tabla.addColumna(new ColumnaCapitalOInteriorCheque());
		tabla.addColumna(new ColumnaEstadoCheque());
		tabla.addColumna(new ColumnaFechaSalidaCheque());
		return tabla;
	}

	public Totales<Cheque> construirTotales(int idModel) {
		return null;
	}
}

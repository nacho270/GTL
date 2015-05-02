package ar.com.textillevel.gui.modulos.personal.modulos.vales.builders;

import java.util.ArrayList;
import java.util.List;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.acciones.Accion;
import ar.clarin.fwjava.templates.modulo.model.acciones.Acciones;
import ar.clarin.fwjava.templates.modulo.model.acciones.IBuilderAcciones;
import ar.clarin.fwjava.templates.modulo.model.filtros.Filtros;
import ar.clarin.fwjava.templates.modulo.model.filtros.IBuilderFiltros;
import ar.clarin.fwjava.templates.modulo.model.tabla.IBuilderTabla;
import ar.clarin.fwjava.templates.modulo.model.tabla.Tabla;
import ar.com.textillevel.gui.modulos.personal.modulos.vales.acciones.AccionAgregarValeAnticipo;
import ar.com.textillevel.gui.modulos.personal.modulos.vales.acciones.AccionImprimirValeAnticipo;
import ar.com.textillevel.gui.modulos.personal.modulos.vales.acciones.AccionMarcarValeUtilizado;
import ar.com.textillevel.gui.modulos.personal.modulos.vales.columnas.ColumnaConceptoValeAnticipo;
import ar.com.textillevel.gui.modulos.personal.modulos.vales.columnas.ColumnaEmpleadoValeAnticipo;
import ar.com.textillevel.gui.modulos.personal.modulos.vales.columnas.ColumnaFechaValeAnticipo;
import ar.com.textillevel.gui.modulos.personal.modulos.vales.columnas.ColumnaMontoValeAnticipo;
import ar.com.textillevel.gui.modulos.personal.modulos.vales.columnas.ColumnaNroValeAnticipo;
import ar.com.textillevel.gui.modulos.personal.modulos.vales.columnas.ColumnaValeAnticipoUtilizado;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.vale.ValeAnticipo;

public class BuilderAccionesValesAnticipo implements IBuilderAcciones<ValeAnticipo>, 
													 IBuilderFiltros<ValeAnticipo>, 
													 IBuilderTabla<ValeAnticipo> {

	private static BuilderAccionesValesAnticipo instance = new BuilderAccionesValesAnticipo();

	public static BuilderAccionesValesAnticipo getInstance() {
		return instance;
	}

	public Tabla<ValeAnticipo> construirTabla(int idModel) {
		Tabla<ValeAnticipo> tabla = new Tabla<ValeAnticipo>();
		tabla.addColumna(new ColumnaNroValeAnticipo());
		tabla.addColumna(new ColumnaFechaValeAnticipo());
		tabla.addColumna(new ColumnaEmpleadoValeAnticipo());
		tabla.addColumna(new ColumnaConceptoValeAnticipo());
		tabla.addColumna(new ColumnaMontoValeAnticipo());
		tabla.addColumna(new ColumnaValeAnticipoUtilizado());
		return tabla;
	}

	public Filtros<ValeAnticipo> construirFiltros(int idModel) {
		return null;
	}

	public Acciones<ValeAnticipo> construirAcciones(int idModel) throws CLException {
		Acciones<ValeAnticipo> acciones = new Acciones<ValeAnticipo>();
		List<Accion<ValeAnticipo>> accionesCreacion = new ArrayList<Accion<ValeAnticipo>>();
		accionesCreacion.add(new AccionAgregarValeAnticipo());
		accionesCreacion.add(new AccionMarcarValeUtilizado());
		accionesCreacion.add(new AccionImprimirValeAnticipo());
		acciones.addElementGroup("Acciones",accionesCreacion);
		return acciones;
	}
}

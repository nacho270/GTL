package ar.com.textillevel.gui.modulos.dibujos.builders;

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
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.gui.modulos.dibujos.acciones.AccionAgregarDibujo;
import ar.com.textillevel.gui.modulos.dibujos.acciones.AccionConsultarDibujo;
import ar.com.textillevel.gui.modulos.dibujos.acciones.AccionEliminarDibujo;
import ar.com.textillevel.gui.modulos.dibujos.acciones.AccionModificarDibujo;
import ar.com.textillevel.gui.modulos.dibujos.acciones.AccionQuitarClienteDibujo;
import ar.com.textillevel.gui.modulos.dibujos.columnas.ColumnaAnchoCilindroDibujo;
import ar.com.textillevel.gui.modulos.dibujos.columnas.ColumnaClienteDibujo;
import ar.com.textillevel.gui.modulos.dibujos.columnas.ColumnaNombreDibujo;
import ar.com.textillevel.gui.modulos.dibujos.columnas.ColumnaNumeroDibujo;
import ar.com.textillevel.gui.modulos.dibujos.columnas.ColumnaVariantesDibujo;

public class BuilderAccionesDibujo implements IBuilderAcciones<DibujoEstampado>,
											  IBuilderFiltros<DibujoEstampado>, 
											  IBuilderTabla<DibujoEstampado>, 
											  IBuilderTotales<DibujoEstampado>{
	
	private static BuilderAccionesDibujo instance = new BuilderAccionesDibujo();

	public static BuilderAccionesDibujo getInstance() {
		return instance;
	}
	
	public Acciones<DibujoEstampado> construirAcciones(int idModel) throws FWException {
		Acciones<DibujoEstampado> acciones = new Acciones<DibujoEstampado>();
		List<Accion<DibujoEstampado>> accionesCreacion = new ArrayList<Accion<DibujoEstampado>>();
		accionesCreacion.add(new AccionAgregarDibujo());
		accionesCreacion.add(new AccionModificarDibujo());
		accionesCreacion.add(new AccionEliminarDibujo());
		accionesCreacion.add(new AccionConsultarDibujo());
		accionesCreacion.add(new AccionQuitarClienteDibujo());
		acciones.addElementGroup("Acciones",accionesCreacion);
		return acciones;
	}

	public Filtros<DibujoEstampado> construirFiltros(int idModel) {
		return null;
	}

	public Tabla<DibujoEstampado> construirTabla(int idModel) {
		Tabla<DibujoEstampado> tabla = new Tabla<DibujoEstampado>();
		tabla.addColumna(new ColumnaNumeroDibujo());
		tabla.addColumna(new ColumnaNombreDibujo());
		tabla.addColumna(new ColumnaAnchoCilindroDibujo());
		tabla.addColumna(new ColumnaClienteDibujo());
		tabla.addColumna(new ColumnaVariantesDibujo());
		return tabla;
	}

	public Totales<DibujoEstampado> construirTotales(int idModel) {
		return null;
	}
}

package ar.com.textillevel.gui.modulos.eventos.builders;

import ar.clarin.fwjava.auditoria.ejb.Evento;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.acciones.Acciones;
import ar.clarin.fwjava.templates.modulo.model.acciones.IBuilderAcciones;
import ar.clarin.fwjava.templates.modulo.model.filtros.Filtros;
import ar.clarin.fwjava.templates.modulo.model.filtros.IBuilderFiltros;
import ar.clarin.fwjava.templates.modulo.model.tabla.IBuilderTabla;
import ar.clarin.fwjava.templates.modulo.model.tabla.Tabla;
import ar.clarin.fwjava.templates.modulo.model.totales.IBuilderTotales;
import ar.clarin.fwjava.templates.modulo.model.totales.Totales;
import ar.com.textillevel.gui.modulos.eventos.columnas.ColumnaDescripcionEvento;
import ar.com.textillevel.gui.modulos.eventos.columnas.ColumnaHoraEvento;
import ar.com.textillevel.gui.modulos.eventos.columnas.ColumnaUsuarioEvento;


public class BuilderAccionesVisorEventos implements IBuilderAcciones<Evento>,
													IBuilderFiltros<Evento>, 
													IBuilderTabla<Evento>, 
													IBuilderTotales<Evento>{
	
	private static BuilderAccionesVisorEventos instance = new BuilderAccionesVisorEventos();

	public static BuilderAccionesVisorEventos getInstance() {
		return instance;
	}

	public Acciones<Evento> construirAcciones(int idModel) throws CLException {
		return null;
	}

	public Filtros<Evento> construirFiltros(int idModel) {
		return null;
	}

	public Tabla<Evento> construirTabla(int idModel) {
		Tabla<Evento> tabla = new Tabla<Evento>();
		tabla.addColumna(new ColumnaHoraEvento());
		tabla.addColumna(new ColumnaUsuarioEvento());
		tabla.addColumna(new ColumnaDescripcionEvento());
		return tabla;
	}

	public Totales<Evento> construirTotales(int idModel) {
		return null;
	}

}

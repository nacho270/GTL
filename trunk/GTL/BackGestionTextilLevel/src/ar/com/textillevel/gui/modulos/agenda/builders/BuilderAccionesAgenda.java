package ar.com.textillevel.gui.modulos.agenda.builders;

import java.util.ArrayList;
import java.util.List;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.acciones.Accion;
import ar.clarin.fwjava.templates.modulo.model.acciones.Acciones;
import ar.clarin.fwjava.templates.modulo.model.acciones.IBuilderAcciones;
import ar.clarin.fwjava.templates.modulo.model.accionesmouse.AccionesAdicionales;
import ar.clarin.fwjava.templates.modulo.model.accionesmouse.IBuilderAccionAdicional;
import ar.clarin.fwjava.templates.modulo.model.filtros.Filtros;
import ar.clarin.fwjava.templates.modulo.model.filtros.IBuilderFiltros;
import ar.clarin.fwjava.templates.modulo.model.tabla.IBuilderTabla;
import ar.clarin.fwjava.templates.modulo.model.tabla.Tabla;
import ar.clarin.fwjava.templates.modulo.model.totales.IBuilderTotales;
import ar.clarin.fwjava.templates.modulo.model.totales.TotalGeneral;
import ar.clarin.fwjava.templates.modulo.model.totales.Totales;
import ar.com.textillevel.entidades.gente.IAgendable;
import ar.com.textillevel.gui.modulos.agenda.acciones.AccionDobleClickItemAgenda;
import ar.com.textillevel.gui.modulos.agenda.acciones.AccionVerDetallesContactoBuscado;
import ar.com.textillevel.gui.modulos.agenda.columnas.ColumnaCelular;
import ar.com.textillevel.gui.modulos.agenda.columnas.ColumnaCodigoPostal;
import ar.com.textillevel.gui.modulos.agenda.columnas.ColumnaContacto;
import ar.com.textillevel.gui.modulos.agenda.columnas.ColumnaDireccion;
import ar.com.textillevel.gui.modulos.agenda.columnas.ColumnaFax;
import ar.com.textillevel.gui.modulos.agenda.columnas.ColumnaLocalidad;
import ar.com.textillevel.gui.modulos.agenda.columnas.ColumnaMail;
import ar.com.textillevel.gui.modulos.agenda.columnas.ColumnaRazonSocial;
import ar.com.textillevel.gui.modulos.agenda.columnas.ColumnaTelefono;

public class BuilderAccionesAgenda implements IBuilderAcciones<IAgendable>,
											IBuilderFiltros<IAgendable>, IBuilderTabla<IAgendable>,
											IBuilderTotales<IAgendable>, IBuilderAccionAdicional<IAgendable> {

	private static BuilderAccionesAgenda instance = new BuilderAccionesAgenda();

	public static BuilderAccionesAgenda getInstance() {
		return instance;
	}
	
	public Acciones<IAgendable> construirAcciones(int idModel) throws CLException {
		Acciones<IAgendable> acciones = new Acciones<IAgendable>();
		List<Accion<IAgendable>> accionesConsulta = new ArrayList<Accion<IAgendable>>();
		accionesConsulta.add(new AccionVerDetallesContactoBuscado());
		acciones.addElementGroup("Consulta", accionesConsulta);
		return acciones;
	}

	public Filtros<IAgendable> construirFiltros(int idModel) {	
		return null;
	}

	public Tabla<IAgendable> construirTabla(int idModel) {
		Tabla<IAgendable> tabla = new Tabla<IAgendable>();
		tabla.addColumna(new ColumnaRazonSocial());
		tabla.addColumna(new ColumnaContacto());
		tabla.addColumna(new ColumnaTelefono());
		tabla.addColumna(new ColumnaCelular());
		tabla.addColumna(new ColumnaFax());
		tabla.addColumna(new ColumnaDireccion());
		tabla.addColumna(new ColumnaLocalidad());
		tabla.addColumna(new ColumnaCodigoPostal());
		tabla.addColumna(new ColumnaMail());
		return tabla;
	}

	public Totales<IAgendable> construirTotales(int idModel) {
		Totales<IAgendable> totales = new Totales<IAgendable>();
		totales.addSingleElement(new TotalGeneral<IAgendable>());
		return totales;
	}

	public AccionesAdicionales<IAgendable> construirAccionAdicional(int idModel) throws CLException {
		AccionesAdicionales<IAgendable> acciones = new AccionesAdicionales<IAgendable>();
		acciones.addSingleElement(new AccionDobleClickItemAgenda());
		return acciones;
	}
}

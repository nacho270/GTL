package ar.com.textillevel.gui.util.controles.calendario.renderers;

import java.util.List;

import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.com.textillevel.gui.util.controles.calendario.EventoCalendario;

public class EventosTablaConBotonesRenderer implements EventosRenderer<PanelTabla<EventoCalendario>> {

	public PanelTabla<EventoCalendario> getComponent(List<EventoCalendario> eventos) {
		TablaEventos t = new TablaEventos();
		if(eventos!=null){
			t.agregarElementos(eventos);
		}
		return t;
	}
	
	private class TablaEventos extends PanelTabla<EventoCalendario>{

		private static final long serialVersionUID = 5406729694288269349L;

		private static final int CANT_COLS = 2;
		private static final int COL_DESC = 0;
		private static final int COL_OBJ = 1;
		
		public TablaEventos(){
			agregarBotonModificar();
		}
		
		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0,CANT_COLS);
			tabla.setStringColumn(COL_DESC, "Eventos", 180,180,true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setAllowHidingColumns(false);
			tabla.setReorderingAllowed(false);
			tabla.setAllowSorting(false);
			tabla.setHeaderAlignment(COL_DESC, CLJTable.CENTER_ALIGN);
			tabla.setAlignment(COL_DESC, CLJTable.CENTER_ALIGN);
			return tabla;
		}

		@Override
		protected void agregarElemento(EventoCalendario elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_DESC] = elemento.getDescripcion();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected EventoCalendario getElemento(int fila) {
			return (EventoCalendario)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}
	}
}

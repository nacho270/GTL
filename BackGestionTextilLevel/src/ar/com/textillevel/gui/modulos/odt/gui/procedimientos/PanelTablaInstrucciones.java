package ar.com.textillevel.gui.modulos.odt.gui.procedimientos;

import ar.com.fwcommon.componentes.FWJTable;
import ar.com.textillevel.gui.util.panels.PanelTablaAgregarQuitarSubirBajarModificar;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimiento;

public abstract class PanelTablaInstrucciones extends PanelTablaAgregarQuitarSubirBajarModificar<InstruccionProcedimiento> {

		private static final long serialVersionUID = 5002844689448032616L;

		private static final int CANT_COLS = 2;
		private static final int COL_DESCRIPCION = 0;
		private static final int COL_OBJ = 1;

		public PanelTablaInstrucciones() {
		}

		@Override
		protected void agregarElemento(InstruccionProcedimiento elemento) {
			getTabla().addRow(new Object[] {InstruccionProcedimientoRenderer.renderInstruccionASHTML(elemento), elemento });
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setMultilineColumn(COL_DESCRIPCION, "Descripción", 400, true, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setReorderingAllowed(false);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setHeaderAlignment(COL_DESCRIPCION, FWJTable.CENTER_ALIGN);
			return tabla;
		}

		@Override
		protected InstruccionProcedimiento getElemento(int fila) {
			return (InstruccionProcedimiento) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

	}
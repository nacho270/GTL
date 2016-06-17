package ar.com.textillevel.gui.modulos.odt.gui.estampado;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.textillevel.gui.modulos.odt.gui.PanTablaVisualizacionFormulaCliente;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.MateriaPrimaCantidad;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.ReactivoCantidad;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.estampado.PigmentoCantidad;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.QuimicoCantidad;

public class PanTablaQuimicosPigmentosVisualizacion extends PanTablaVisualizacionFormulaCliente {

	private static final long serialVersionUID = 1L;

	private PanTablaMateriaPrimaCantidad panInternal;

	public PanTablaQuimicosPigmentosVisualizacion() {
		construct();
	}

	public void limpiar() {
		getPanInternal().limpiar();
	}

	private void construct() {
		setLayout(new GridBagLayout());
		add(getPanInternal(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 1, 1, 1, 1));			
	}

	private PanTablaMateriaPrimaCantidad getPanInternal() {
		if(panInternal == null) {
			panInternal = new PanTablaMateriaPrimaCantidad();
		}
		return panInternal;
	}

	@SuppressWarnings("rawtypes")
	public void setMateriasPrimas(List<MateriaPrimaCantidad> materiasPrimasCantidad) {
		getPanInternal().limpiar();
		getPanInternal().agregarElementos(materiasPrimasCantidad);
	}

	@SuppressWarnings("rawtypes")
	private class PanTablaMateriaPrimaCantidad extends PanelTabla<MateriaPrimaCantidad> {

		private static final long serialVersionUID = 1L;

		private static final int CANT_COLS = 3;
		private static final int COL_DESCRIPCION = 0;
		private static final int COL_PROPORCION = 1;
		private static final int COL_UNIDAD = 2;

		public Map<String, Color> mapColorMPC = new HashMap<String, Color>();
		public Map<String, String> mapTituloMPC = new HashMap<String, String>();
		
		public PanTablaMateriaPrimaCantidad() {
			getBotonAgregar().setVisible(false);
			getBotonEliminar().setVisible(false);
			mapColorMPC.put(QuimicoCantidad.class.getName(), Color.CYAN);
			mapColorMPC.put(PigmentoCantidad.class.getName(), Color.GRAY);
			mapColorMPC.put(ReactivoCantidad.class.getName(), Color.MAGENTA);
			mapTituloMPC.put(QuimicoCantidad.class.getName(), "QUÍMICOS");
			mapTituloMPC.put(PigmentoCantidad.class.getName(), "PIGMENTOS");
			mapTituloMPC.put(ReactivoCantidad.class.getName(), "REACTIVOS");
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_DESCRIPCION, "MATERIA PRIMA", 200, 200, true);
			tabla.setFloatColumn(COL_PROPORCION, "PROPORCION (%)", 100, true);
			tabla.setFloatColumn(COL_UNIDAD, "UNIDAD", 100, true);
			return tabla;
		}

		@Override
		protected void agregarElemento(MateriaPrimaCantidad elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_DESCRIPCION] = elemento.getMateriaPrima().getDescripcion();
			row[COL_PROPORCION] = elemento.getCantidad();
			row[COL_UNIDAD] = elemento.getUnidad();
			getTabla().addRow(row);
		}

		@Override
		protected MateriaPrimaCantidad getElemento(int fila) {
			return null;
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		public void agregarElementos(Collection<MateriaPrimaCantidad> elementos) {
			Map<String, List<MateriaPrimaCantidad>> mapMPC = new HashMap<String, List<MateriaPrimaCantidad>>();
			for(MateriaPrimaCantidad mpc : elementos) {
				String clazz = mpc.getClass().getName();
				List<MateriaPrimaCantidad> listMPC = mapMPC.get(clazz);
				if(listMPC == null) {
					listMPC = new ArrayList<MateriaPrimaCantidad>();
					mapMPC.put(clazz, listMPC);
				}
				listMPC.add(mpc);
			}
			int fila = 0;
			for(String key : mapMPC.keySet()) {
				List<MateriaPrimaCantidad> listMPC = mapMPC.get(key);
				getTabla().addRow();
				getTabla().setValueAt(mapTituloMPC.get(key), fila, COL_PROPORCION);
				getTabla().setBackgroundRow(fila, mapColorMPC.get(key));
				fila++;
				for(MateriaPrimaCantidad mpc : listMPC) {
					agregarElemento(mpc);
					fila++;
				}
			}
		}
	
	}
}

package ar.com.textillevel.gui.modulos.odt.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTableAnalisis;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.facade.api.remote.TipoArticuloFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.ReactivoCantidad;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.AnilinaCantidad;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.TenidoTipoArticulo;
import ar.com.textillevel.util.GTLBeanFactory;

public class PanTablaQuimicos extends PanTablaVisualizacionFormulaCliente {

	private static final long serialVersionUID = 1L;

	private PanTablaAnilinasReactivosCantidad panInternal;

	public PanTablaQuimicos() {
		construct();
	}

	public void limpiar() {
		getPanInternal().limpiar();
	}

	private void construct() {
		setLayout(new GridBagLayout());
		add(getPanInternal(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 1, 1, 1, 1));			
	}

	private PanTablaAnilinasReactivosCantidad getPanInternal() {
		if(panInternal == null) {
			panInternal = new PanTablaAnilinasReactivosCantidad();
		}
		return panInternal;
	}

	public void setTenidos(List<TenidoTipoArticulo> tenidos) {
		getPanInternal().limpiar();
		getPanInternal().agregarElementos(tenidos);
		((FWJTableAnalisis)getPanInternal().getTabla()).agruparColumna(0);
		((FWJTableAnalisis)getPanInternal().getTabla()).agruparColumnaExtendido(1);
	}

	private class PanTablaAnilinasReactivosCantidad extends PanelTabla<TenidoTipoArticulo> {

		private static final long serialVersionUID = 1L;

		private static final int CANT_COLS = 4;
		private static final int COL_TIPO_ARTICULO = 0;
		private static final int COL_TIPO_FORMULABLE = 1;
		private static final int COL_NOMBRE_FORMULABLE = 2;
		private static final int COL_PROPORCION = 3;

		private TipoArticuloFacadeRemote tipoArticuloFacade;
		private final Map<TipoArticulo, Color> tipoArticuloMap;

		private static final String CHAR_REPEATED = "X"; 

		public PanTablaAnilinasReactivosCantidad() {
			getBotonAgregar().setVisible(false);
			getBotonEliminar().setVisible(false);

			//lleno el map de colores
			this.tipoArticuloMap = new HashMap<TipoArticulo, Color>();
			for(TipoArticulo ta : getTipoArticuloFacade().getAllTipoArticulos()) {
				this.tipoArticuloMap.put(ta, GenericUtils.getRandomColor());
			}
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTableAnalisis(0, CANT_COLS);
			tabla.setStringColumn(COL_TIPO_ARTICULO, "TIPO DE ARTÍCULO", 120, 120, true);
			tabla.setStringColumn(COL_TIPO_FORMULABLE, "TIPO DE MATERIA PRIMA", 150, 150, true);
			tabla.setStringColumn(COL_NOMBRE_FORMULABLE, "NOMBRE", 200, 200, true);
			tabla.setFloatColumn(COL_PROPORCION, "PROPORCION (%)", 100, true);
			return tabla;
		}

		@Override
		protected void agregarElemento(TenidoTipoArticulo elemento) {
			if(getTabla().getRowCount() > 1) {
				Object[] rowSeparator = new Object[CANT_COLS];
				rowSeparator[COL_TIPO_ARTICULO] = GenericUtils.repeat(CHAR_REPEATED, 28);
				rowSeparator[COL_NOMBRE_FORMULABLE] = GenericUtils.repeat(CHAR_REPEATED, 32);
				rowSeparator[COL_TIPO_FORMULABLE] = GenericUtils.repeat(CHAR_REPEATED, 25);
				rowSeparator[COL_PROPORCION] = GenericUtils.repeat(CHAR_REPEATED, 13);
				getTabla().addRow(rowSeparator);
				getTabla().setBackgroundRow(getTabla().getRowCount() - 1, Color.GRAY);
			}
			for(AnilinaCantidad ac : elemento.getAnilinasCantidad()) {
				Object[] row = new Object[CANT_COLS];
				row[COL_TIPO_ARTICULO] = elemento.getTipoArticulo().getNombre();
				row[COL_TIPO_FORMULABLE] = "ANILINA";
				row[COL_NOMBRE_FORMULABLE] = ac.getMateriaPrima().getDescripcion();
				row[COL_PROPORCION] = ac.getCantidad();
				getTabla().addRow(row);
			}
			for(ReactivoCantidad rc : elemento.getReactivosCantidad()) {
				Object[] row = new Object[CANT_COLS];
				row[COL_TIPO_ARTICULO] = elemento.getTipoArticulo().getNombre();
				row[COL_TIPO_FORMULABLE] = "REACTIVO";
				row[COL_NOMBRE_FORMULABLE] = rc.getMateriaPrima().getDescripcion();
				row[COL_PROPORCION] = rc.getCantidad();
				getTabla().addRow(row);
			}
		}

		@Override
		protected TenidoTipoArticulo getElemento(int fila) {
			return null;
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		private TipoArticuloFacadeRemote getTipoArticuloFacade() {
			if(tipoArticuloFacade == null) {
				tipoArticuloFacade = GTLBeanFactory.getInstance().getBean2(TipoArticuloFacadeRemote.class);
			}
			return tipoArticuloFacade;
		}

	}
}

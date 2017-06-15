package ar.com.textillevel.gui.modulos.abm;

import static ar.com.textillevel.gui.util.GenericUtils.createGridBagConstraints;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.templates.GuiABMListaTemplate;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.facade.api.remote.TipoArticuloFacadeRemote;
import ar.com.textillevel.gui.util.panels.PanelSeleccionarElementos;
import ar.com.textillevel.util.GTLBeanFactory;

public class GuiABMArticulos extends GuiABMListaTemplate {

	private static final long serialVersionUID = -4193424482048808415L;

	private static final int MAX_LONGITUD_NOMBRE = 50;
	private static final int MAX_LONGITUD_SIGLA = 150;

	private JPanel tabDetalle;
	private JPanel panDetalle;

	private FWJTextField txtNombreTipoArticulo;
	private FWJTextField txtSigla;
	private PanelTablaArticulos panelTablaArticulos;
	private PanelSeleccionarElementos<TipoArticulo> panSelTipoArticulos;

	private TipoArticuloFacadeRemote tipoArticuloFacade;
	private TipoArticulo articuloActual;

	public GuiABMArticulos(Integer idModulo) {
		super();
		setHijoCreado(true);
		setTitle("Administrar Artículos");
		constructGui();
		setEstadoInicial();
	}

	private void constructGui() {
		panTabs.addTab("Información del tipo de artículo", getTabDetalle());
		getBtnAgregar().setVisible(false);
	}

	private JPanel getTabDetalle() {
		if (tabDetalle == null) {
			tabDetalle = new JPanel();
			tabDetalle.setLayout(new GridBagLayout());
			tabDetalle.add(getPanDetalle(), createGridBagConstraints(0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 1, 1));
		}
		return tabDetalle;
	}

	private JPanel getPanDetalle() {
		if (panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(new JLabel("Nombre: "), createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtNombreTipoArticulo(), createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("Sigla: "), createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtSigla(), createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(getPanSelTipoArticulos(), createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 2, 1, 0, 0));
			panDetalle.add(getPanelTablaArticulo(), createGridBagConstraints(0, 3, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 2, 1, 0, 1));
		}
		return panDetalle;
	}

	private PanelTablaArticulos getPanelTablaArticulo() {
		if (panelTablaArticulos == null) {
			panelTablaArticulos = new PanelTablaArticulos();
		}
		return panelTablaArticulos;
	}

	@Override
	public void cargarLista() {
		List<TipoArticulo> articuloList = getTipoArticuloFacade().getAllTipoArticulos();
		lista.removeAll();
		for (TipoArticulo c : articuloList) {
			lista.addItem(c);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		setTipoArticuloActual(new TipoArticulo());
		getTxtNombreTipoArticulo().requestFocus();
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if (lista.getSelectedIndex() >= 0) {
			if (FWJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar el tipo de artículo seleccionado?", "Confirmación") == FWJOptionPane.YES_OPTION) {
				try {
					getTipoArticuloFacade().remove(getTipoArticuloActual());
					cargarLista();
					itemSelectorSeleccionado(-1);
				} catch (ValidacionException e) {
					FWJOptionPane.showErrorMessage(GuiABMArticulos.this, StringW.wordWrap(e.getMensajeError()), "Error");
				}
			}
		}
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if (validar()) {
			capturarSetearDatos();
			TipoArticulo tipoArticulo = getTipoArticuloFacade().save(getTipoArticuloActual());
			cargarLista();
			lista.setSelectedValue(tipoArticulo, true);
			return true;
		}
		return false;
	}

	private boolean validar() {
		if (getTxtNombreTipoArticulo().getText().trim().length() == 0) {
			FWJOptionPane.showErrorMessage(this, "Debe completar el nombre del artículo.", "Advertencia");
			getTxtNombreTipoArticulo().requestFocus();
			return false;
		}

		if (getTxtSigla().getText().trim().length() == 0) {
			FWJOptionPane.showErrorMessage(this, "Debe completar la descripción del artículo.", "Advertencia");
			getTxtSigla().requestFocus();
			return false;
		}

		/*
		List<TipoArticulo> tasComponentes = getPanSelTipoArticulos().getSelectedElements();
		if(!tasComponentes.isEmpty()) {
			List<TipoArticulo> allTipoArticulos = getTipoArticuloFacade().getAllTipoArticulos();
			TipoArticulo taActual = getTipoArticuloActual();
			for(TipoArticulo ta : allTipoArticulos) {
				if((taActual.getId() == null || taActual.getId() != ta.getId()) && ta.isCompuesto()) {
					List<TipoArticulo> taList1 = new ArrayList<TipoArticulo>(tasComponentes);
					taList1.removeAll(ta.getTiposArticuloComponentes());
					//Es la misma lista
					if(taList1.isEmpty() && ta.getTiposArticuloComponentes().size() == tasComponentes.size()) {
						CLJOptionPane.showErrorMessage(this, "Ya existe un tipo de artículo que tiene esos 2 compuestos.", "Advertencia");
						getTxtSigla().requestFocus();
						return false;
					}
				}
			}
		}
		*/
		return true;
	}

	private void capturarSetearDatos() {
		// BigDecimal gramaje = new BigDecimal(Double.parseDouble(getTxtGramaje().getText().trim()));
		getTipoArticuloActual().setTiposArticuloComponentes(getPanSelTipoArticulos().getSelectedElements());
		getTipoArticuloActual().setNombre(getTxtNombreTipoArticulo().getText().toUpperCase());
		getTipoArticuloActual().setSigla(getTxtSigla().getText().toUpperCase());
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if (nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getTxtNombreTipoArticulo().requestFocus();
			return true;
		} else {
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar un tipo de artículo", "Error");
			return false;
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {

	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		if(nivelItemSelector >= 0) {
			TipoArticulo tp = (TipoArticulo) lista.getSelectedValue();
			tp = getTipoArticuloFacade().getByIdEager(tp.getId());
			setTipoArticuloActual(tp);
			limpiarDatos();
			if (getTipoArticuloActual() != null) {
				getTxtNombreTipoArticulo().setText(getTipoArticuloActual().getNombre());
				getTxtSigla().setText(getTipoArticuloActual().getSigla());
				getPanelTablaArticulo().setTipoArticulo(getTipoArticuloActual());
				getPanSelTipoArticulos().setElementsAndSelectedElements(dejarSoloTAsNoCompuestos(getTipoArticuloFacade().getAllTipoArticulos()), tp.getTiposArticuloComponentes());
			}
		}
	}

	private List<TipoArticulo> dejarSoloTAsNoCompuestos(List<TipoArticulo> todosArticulos) {
		List<TipoArticulo> filtrados = new ArrayList<TipoArticulo>();
		for(TipoArticulo ta : todosArticulos) {
			//Sólo permito elegir TAs que no son compuestos
			if(!ta.isCompuesto()) { 
				filtrados.add(ta);
			}
		}
		return filtrados;
	}

	@Override
	public void limpiarDatos() {
		getTxtNombreTipoArticulo().setText("");
		getTxtSigla().setText("");
		getPanelTablaArticulo().limpiar();
		getPanSelTipoArticulos().limpiar();
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicion(false);
		setModoEdicionTemplate(false);
		cargarLista();
		if (lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);
		getPanelTablaArticulo().setModoConsulta(!estado);
	}

	private TipoArticulo getTipoArticuloActual() {
		return articuloActual;
	}

	private void setTipoArticuloActual(TipoArticulo tipoArticuloActual) {
		this.articuloActual = tipoArticuloActual;
	}

	private FWJTextField getTxtNombreTipoArticulo() {
		if (txtNombreTipoArticulo == null) {
			txtNombreTipoArticulo = new FWJTextField(MAX_LONGITUD_NOMBRE);
		}
		return txtNombreTipoArticulo;
	}

	private FWJTextField getTxtSigla() {
		if (txtSigla == null) {
			txtSigla = new FWJTextField(MAX_LONGITUD_SIGLA);
		}
		return txtSigla;
	}

	private TipoArticuloFacadeRemote getTipoArticuloFacade() {
		if (tipoArticuloFacade == null) {
			tipoArticuloFacade = GTLBeanFactory.getInstance().getBean2(TipoArticuloFacadeRemote.class);
		}
		return tipoArticuloFacade;
	}

	private class PanelTablaArticulos extends PanelTabla<Articulo> {

		private static final long serialVersionUID = 1L;

		private static final int CANT_COLS = 4;
		private static final int COL_NOMBRE = 0;
		private static final int COL_DESCR = 1;
		private static final int COL_ANCHO = 2;
		private static final int COL_OBJ = 3;
		private TipoArticulo tipoArticulo;

		public PanelTablaArticulos() {
			agregarBotonModificar();
		}

		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			if (filaSeleccionada != -1) {
				Articulo articulo = getElemento(filaSeleccionada);
				JDialogEditarArticulo dialogo = new JDialogEditarArticulo(GuiABMArticulos.this.getFrame(), articulo);
				GuiUtil.centrarEnFramePadre(dialogo);
				dialogo.setVisible(true);
				if (dialogo.isAcepto()) {
					getTabla().setValueAt(articulo.getNombre(), filaSeleccionada, COL_NOMBRE);
					getTabla().setValueAt(articulo.getDescripcion(), filaSeleccionada, COL_DESCR);
					getTabla().setValueAt(articulo.getAncho() == null ? "" : articulo.getAncho().toString(), filaSeleccionada, COL_ANCHO);
				}
			}
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_NOMBRE, "NOMBRE", 100, 100, true);
			tabla.setHeaderAlignment(COL_NOMBRE, FWJTable.CENTER_ALIGN);
			tabla.setStringColumn(COL_DESCR, "DESCRIPCION", 225, 225, true);
			tabla.setHeaderAlignment(COL_DESCR, FWJTable.CENTER_ALIGN);
			tabla.setStringColumn(COL_ANCHO, "ANCHO", 100, 100, true);
			tabla.setHeaderAlignment(COL_ANCHO, FWJTable.CENTER_ALIGN);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			return tabla;
		}

		public void setTipoArticulo(TipoArticulo tipoArticulo) {
			this.tipoArticulo = tipoArticulo;
			agregarElementos(this.tipoArticulo.getArticulos());
		}

		@Override
		protected void agregarElemento(Articulo elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_NOMBRE] = elemento.getNombre();
			row[COL_DESCR] = elemento.getDescripcion();
			row[COL_ANCHO] = elemento.getAncho() == null ? "" : elemento.getAncho().toString();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected Articulo getElemento(int fila) {
			return (Articulo) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		public boolean validarQuitar() {
			int selectedRow = getTabla().getSelectedRow();
			tipoArticulo.getArticulos().remove(selectedRow);
			return true;
		}

		@Override
		public boolean validarAgregar() {
			Articulo articulo = new Articulo();
			JDialogEditarArticulo dialogo = new JDialogEditarArticulo(GuiABMArticulos.this.getFrame(), articulo);
			GuiUtil.centrarEnFramePadre(dialogo);
			dialogo.setVisible(true);
			if (dialogo.isAcepto()) {
				agregarElemento(articulo);
				tipoArticulo.getArticulos().add(articulo);
			}
			return false;
		}
	}

	private PanelSeleccionarElementos<TipoArticulo> getPanSelTipoArticulos() {
		if (panSelTipoArticulos == null) {
			panSelTipoArticulos = new PanelSeleccionarElementos<TipoArticulo>(null, new ArrayList<TipoArticulo>(), "Tipos de Artículos componentes:");
		}
		return panSelTipoArticulos;
	}

}

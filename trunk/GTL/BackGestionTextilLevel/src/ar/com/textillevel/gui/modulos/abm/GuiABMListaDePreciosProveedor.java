package ar.com.textillevel.gui.modulos.abm;

import static ar.com.textillevel.gui.util.GenericUtils.createGridBagConstraints;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.GTLGlobalCache;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.templates.GuiABMListaTemplate;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.enums.ETipoMoneda;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.ventas.materiaprima.ListaDePreciosProveedor;
import ar.com.textillevel.entidades.ventas.materiaprima.MateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;
import ar.com.textillevel.facade.api.remote.ListaDePreciosProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.MateriaPrimaFacadeRemote;
import ar.com.textillevel.facade.api.remote.ProveedorFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogSeleccionarMateriaPrima;
import ar.com.textillevel.util.GTLBeanFactory;

public class GuiABMListaDePreciosProveedor extends GuiABMListaTemplate {

	private static final long serialVersionUID = 1L;
	private static final int MAX_LONGITUD_RAZ_SOCIAL = 50;

	private JPanel tabDetalle;
	private JPanel panDetalle; 
	private CLJTextField txtRazonSocial;
	private CLJTable tablaMateriaPrimaPrecios;

	private JTextField txtMateriasPrimas;
	private PanelTablaMatPrimaPrecio panelTablaMatPrimaPrecio;

	private final List<MateriaPrima> allMateriaPrimaList;
	private ListaDePreciosProveedor listaDePrecios;

	private ProveedorFacadeRemote proveedorFacade;
	private MateriaPrimaFacadeRemote materiaPrimaFacade;
	private ListaDePreciosProveedorFacadeRemote listaDePreciosFacade;

	public GuiABMListaDePreciosProveedor(Integer idModulo) {
		super();
		this.allMateriaPrimaList = getMateriaPrimaFacade().getAllOrderByName();
		Collections.sort(allMateriaPrimaList, new MateriaPrimaComparator());
		setHijoCreado(true);
		setTitle("Administrar Listas de Precios");
		constructGui();
		setEstadoInicial();
	}

	private void constructGui() {
		panTabs.addTab("Datos de la lista de precio", getTabDetalle());
		getBtnAgregar().setVisible(false);
		getBtnEliminar().setBounds(getBtnEliminar().getX() - 80, getBtnEliminar().getY(), getBtnEliminar().getWidth(), getBtnEliminar().getHeight());
	}

	private JPanel getTabDetalle() {
		if(tabDetalle == null) {
			tabDetalle = new JPanel();
			tabDetalle.setLayout(new GridBagLayout());
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1;
			gridBagConstraints.weighty = 1;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			tabDetalle.add(getPanDetalle(), gridBagConstraints);
		}
		return tabDetalle;
	}

	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(new JLabel(" PROVEEDOR:"), createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtRazonSocial(), createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(getPanelTablaMatPrimaPrecio(), createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 2, 2, 1, 1));
		}
		return panDetalle;
	}

	private CLJTextField getTxtRazonSocial() {
		if(txtRazonSocial == null) {
			txtRazonSocial = new CLJTextField(MAX_LONGITUD_RAZ_SOCIAL);
			txtRazonSocial.setEditable(false);
		}
		return txtRazonSocial;
	}

	private JTextField getTxtMateriasPrimas() {
		if(txtMateriasPrimas == null) {
			txtMateriasPrimas = new JTextField();
			txtMateriasPrimas.setEditable(false);
		}
		return txtMateriasPrimas;
	}

	@Override
	public void cargarLista() {
		List<Proveedor> allProveedoresOrderByName = getProveedorFacade().getAllProveedoresOrderByName();
		lista.removeAll();
		for(Proveedor p : allProveedoresOrderByName) {
			lista.addItem(p);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if(lista.getSelectedIndex() >= 0) {
			if(CLJOptionPane.showQuestionMessage(GuiABMListaDePreciosProveedor.this, "¿Está seguro que desea eliminar la lista de precios seleccionada?", "Confirmación") == CLJOptionPane.YES_OPTION) {
				getListaDePreciosFacade().remove(listaDePrecios);
				itemSelectorSeleccionado(-1);
			}
		}
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if(validar()) {
			capturarSetearDatos();
			getListaDePreciosFacade().save(listaDePrecios, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
			CLJOptionPane.showInformationMessage(this, "Los datos de la lista se han guardado con éxito", "Administrar Lista de Precios");
			lista.setSelectedValue(lista.getSelectedValue(), true);
			return true;
		}
		return false;
	}

	private void capturarSetearDatos() {
		List<PrecioMateriaPrima> elementos = getPanelTablaMatPrimaPrecio().getElementos();
		//Borro los que estaban antes y ahora no
		List<PrecioMateriaPrima> borrarList = new ArrayList<PrecioMateriaPrima>();
		for(PrecioMateriaPrima pmp : listaDePrecios.getPrecios()) {
			if(!elementos.contains(pmp)) {
				borrarList.add(pmp);
			}
		}
		listaDePrecios.getPrecios().removeAll(borrarList);
		//Agrego los que no estaban
		for(PrecioMateriaPrima pmp : elementos) {
			if(!listaDePrecios.getPrecios().contains(pmp)) {
				listaDePrecios.getPrecios().add(pmp);
			}
		}
		for(PrecioMateriaPrima pm : listaDePrecios.getPrecios()){
			if(pm.getStockActual()==null){
				pm.setStockActual(new BigDecimal(0d));
			}
			if(pm.getPuntoDePedido()==null){
				pm.setPuntoDePedido(new BigDecimal(0d));
			}
		}
	}

	private class PrecioMateriaPrimaComparator implements Comparator<PrecioMateriaPrima> {

		public int compare(PrecioMateriaPrima o1, PrecioMateriaPrima o2) {
			if(o1.getMateriaPrima().equals(o2.getMateriaPrima())) {
				if(o1.getMateriaPrima().getTipo() == ETipoMateriaPrima.ANILINA && o2.getMateriaPrima().getTipo() == ETipoMateriaPrima.ANILINA) {
					return ((Anilina)o1.getMateriaPrima()).getTipoAnilina().getDescripcion().compareTo(((Anilina)o2.getMateriaPrima()).getTipoAnilina().getDescripcion());
				} else {
					return o1.getMateriaPrima().getTipo().getId().compareTo(o2.getMateriaPrima().getTipo().getId());
				}
			} else {
				return o1.getMateriaPrima().getTipo().getId().compareTo(o2.getMateriaPrima().getTipo().getId());
			}
		}

	}

	private class MateriaPrimaComparator implements Comparator<MateriaPrima> {

		public int compare(MateriaPrima o1, MateriaPrima o2) {
			if(o1.getTipo() == ETipoMateriaPrima.ANILINA && o2.getTipo() == ETipoMateriaPrima.ANILINA) {
				return ((Anilina)o1).getTipoAnilina().getDescripcion().compareTo(((Anilina)o2).getTipoAnilina().getDescripcion());
			} else {
				return o1.getTipo().getId().compareTo(o2.getTipo().getId());
			}
		}

	}


	
	private boolean validar() {
		String textoValidacion = getPanelTablaMatPrimaPrecio().validarElementos();
		if(textoValidacion == null) {
			return true;
		} else {
			CLJOptionPane.showErrorMessage(GuiABMListaDePreciosProveedor.this, StringW.wordWrap(textoValidacion), "Error");
			return false;
		}
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if(listaDePrecios == null) {
			listaDePrecios = new ListaDePreciosProveedor();
			listaDePrecios.setProveedor((Proveedor)lista.getSelectedValue());
		}
		if(nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			return true;
		} else {
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar un proveedor", "Error");
			return false;
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		limpiarDatos();
		Proveedor proveedor = (Proveedor)lista.getSelectedValue();
		getTxtRazonSocial().setText(proveedor.getRazonSocial());
		listaDePrecios = getListaDePreciosFacade().getListaByIdProveedor(proveedor.getId());
		if(listaDePrecios != null) {
			Collections.sort(listaDePrecios.getPrecios(), new PrecioMateriaPrimaComparator());
			getPanelTablaMatPrimaPrecio().agregarElementos(listaDePrecios.getPrecios());
		}
	}

	@Override
	public void limpiarDatos() {
		getTxtRazonSocial().setText("");
		getTxtMateriasPrimas().setText("");
		getPanelTablaMatPrimaPrecio().limpiar();
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicion(false);
		setModoEdicionTemplate(false);
		cargarLista();
		if(lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	@Override
	public void setModoEdicion(boolean estado) {
		getPanelTablaMatPrimaPrecio().setModoConsulta(!estado);
	}

	private ProveedorFacadeRemote getProveedorFacade() {
		if(proveedorFacade == null) {
			proveedorFacade = GTLBeanFactory.getInstance().getBean2(ProveedorFacadeRemote.class);
		}
		return proveedorFacade;
	}

	private MateriaPrimaFacadeRemote getMateriaPrimaFacade() {
		if(materiaPrimaFacade == null) {
			materiaPrimaFacade = GTLBeanFactory.getInstance().getBean2(MateriaPrimaFacadeRemote.class);
		}
		return materiaPrimaFacade;
	}

	public ListaDePreciosProveedorFacadeRemote getListaDePreciosFacade() {
		if(listaDePreciosFacade == null) {
			listaDePreciosFacade = GTLBeanFactory.getInstance().getBean2(ListaDePreciosProveedorFacadeRemote.class);
		}
		return listaDePreciosFacade;
	}

	private PanelTablaMatPrimaPrecio getPanelTablaMatPrimaPrecio() {
		if(panelTablaMatPrimaPrecio == null) {
			panelTablaMatPrimaPrecio = new PanelTablaMatPrimaPrecio();
		}
		return panelTablaMatPrimaPrecio;
	}

	private class PanelTablaMatPrimaPrecio extends PanelTabla<PrecioMateriaPrima> {
		
		private static final long serialVersionUID = 1L;

		private static final int CANT_COLS = 10;
		private static final int COL_COLOR_INDEX = 0;
		private static final int COL_MAT_PRIMA = 1;
		private static final int COL_ALIAS = 2;
		private static final int COL_SIGLAS = 3;
		private static final int COL_PRECIO = 4;
		private static final int COL_TIPO_MONEDA = 5;
		private static final int COL_STOCK_ACTUAL = 6;
		private static final int COL_PTO_PEDIDO = 7;
		private static final int COL_FECHA_ULT_MODIF = 8;
		private static final int COL_OBJ = 9;

		private JComboBox cmbTipoMoneda;
		private final List<PrecioMateriaPrima> mirrorTablaList = new ArrayList<PrecioMateriaPrima>();

		@Override
		public void limpiar() {
			getTabla().setNumRows(0);
			mirrorTablaList.clear();
		}

		@Override
		public void agregarElementos(Collection<PrecioMateriaPrima> elementos) {
			int fila = 0;
			ETipoMateriaPrima tipoMPActual = null;
			for(PrecioMateriaPrima pmp : elementos) {
				if(tipoMPActual == null || tipoMPActual != pmp.getMateriaPrima().getTipo()) {
					agregarFilaSeparadora(fila, pmp.getMateriaPrima().getTipo());
					tipoMPActual = pmp.getMateriaPrima().getTipo();
					fila ++;
				}
				agregarElemento(pmp);
				mirrorTablaList.add(pmp);
				fila ++;
			}
		}

		@Override
		public List<PrecioMateriaPrima> getElementos() {
	        List<PrecioMateriaPrima> elementos = new ArrayList<PrecioMateriaPrima>();
	        for(int fila = 0; fila < getTabla().getRowCount(); fila++) {
	        	if(!isFilaSeparadora(fila)) {
	        		elementos.add(getElemento(fila));
	        	}
	        }
	        return elementos;
		}

		@Override
		protected void filaTablaSeleccionada() {
			int selectedRow = getTabla().getSelectedRow();
			getBotonEliminar().setEnabled(selectedRow != -1 && !isFilaSeparadora(selectedRow));
		}

		@Override
		public boolean validarQuitar() {
			PrecioMateriaPrima elemento = getElemento(getTabla().getSelectedRow());
			mirrorTablaList.remove(elemento);
			return true;
		}

		@Override
		protected void botonQuitarPresionado() {
			mirrorTablaList.clear();
			mirrorTablaList.addAll(getElementos());
			refrescarItemsTablaWithMirrorList();
		}

		private void refrescarItemsTablaWithMirrorList() {
			Collections.sort(mirrorTablaList, new PrecioMateriaPrimaComparator());
			getTabla().setNumRows(0);
			agregarElementosSinMirror(mirrorTablaList);
		}

		private void agregarElementosSinMirror(List<PrecioMateriaPrima> elementos) {
			int fila = 0;
			ETipoMateriaPrima tipoMPActual = null;
			for(PrecioMateriaPrima pmp : elementos) {
				if(tipoMPActual == null || tipoMPActual != pmp.getMateriaPrima().getTipo()) {
					agregarFilaSeparadora(fila, pmp.getMateriaPrima().getTipo());
					tipoMPActual = pmp.getMateriaPrima().getTipo();
					fila ++;
				}
				agregarElemento(pmp);
				fila ++;
			}
		}

		private void agregarFilaSeparadora(int fila, ETipoMateriaPrima tipoMateriaPrima) {
			getTabla().addRow();
			getTabla().setBackgroundRow(fila, Color.CYAN);
			getTabla().setValueAt(tipoMateriaPrima.getDescripcion().toUpperCase(), fila, COL_ALIAS);
		}

		@Override
		protected void agregarElemento(PrecioMateriaPrima elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_MAT_PRIMA] = elemento.getMateriaPrima();
			row[COL_FECHA_ULT_MODIF] = elemento.getFechaUltModif()==null?"":DateUtil.dateToString(elemento.getFechaUltModif(), DateUtil.SHORT_DATE_WITH_HOUR);
			row[COL_PRECIO] = elemento.getPrecio() == null ? null : elemento.getPrecio().floatValue();
			row[COL_ALIAS] = elemento.getAlias();
			row[COL_COLOR_INDEX] = getColorIndex(elemento.getMateriaPrima());
			row[COL_TIPO_MONEDA] = elemento.getTipoMoneda();
			row[COL_SIGLAS] = elemento.getSiglas();
			row[COL_STOCK_ACTUAL] = elemento.getStockActual() == null ? null : elemento.getStockActual().floatValue();
			row[COL_PTO_PEDIDO] = elemento.getPuntoDePedido() == null ? null : elemento.getPuntoDePedido().floatValue();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);

			//Dejo modificar el stock sólo si no está seteado
			if(elemento.getStockActual() != null) {
				getTabla().lockCell(getTabla().getRowCount() - 1, COL_STOCK_ACTUAL);
			}
		}

		private String getColorIndex(MateriaPrima materiaPrima) {
			if(materiaPrima.getTipo() == ETipoMateriaPrima.ANILINA) {
				return String.valueOf(((Anilina)materiaPrima).getColorIndex());
			}
			return "";
		}

		@Override
		protected CLJTable construirTabla() {
			if(tablaMateriaPrimaPrecios == null) {
				tablaMateriaPrimaPrecios = new CLJTable(0, CANT_COLS);
				tablaMateriaPrimaPrecios.setStringColumn(COL_MAT_PRIMA, "Materia Prima", 130, 130, true);
				tablaMateriaPrimaPrecios.setStringColumn(COL_FECHA_ULT_MODIF, "F. Últ. Modif.", 90, 90, true);
				tablaMateriaPrimaPrecios.setFloatColumn(COL_PRECIO, "Precio", 0, Float.MAX_VALUE, 50, false);
				tablaMateriaPrimaPrecios.setComboColumn(COL_TIPO_MONEDA, "Moneda", getCmbTipoMoneda(), 50, false);
				tablaMateriaPrimaPrecios.setStringColumn(COL_ALIAS, "Alias", 110, 110, false);
				tablaMateriaPrimaPrecios.setStringColumn(COL_COLOR_INDEX, "Color Index", 60, 60, true);
				tablaMateriaPrimaPrecios.setStringColumn(COL_SIGLAS, "Siglas", 70, 70, false);
				tablaMateriaPrimaPrecios.setFloatColumn(COL_STOCK_ACTUAL, "Stock", 0, Float.MAX_VALUE, 50, false);
				tablaMateriaPrimaPrecios.setFloatColumn(COL_PTO_PEDIDO, "P. Pedido", 0, Float.MAX_VALUE, 70, false);
				tablaMateriaPrimaPrecios.setStringColumn(COL_OBJ, "", 0, 0, true);
				tablaMateriaPrimaPrecios.setReorderingAllowed(false);
			}
			return tablaMateriaPrimaPrecios;
		}

		private JComboBox getCmbTipoMoneda() {
			if(cmbTipoMoneda == null) {
				cmbTipoMoneda = new JComboBox();
				GuiUtil.llenarCombo(cmbTipoMoneda, Arrays.asList(ETipoMoneda.values()), true);
			}
			return cmbTipoMoneda;
		}

		@Override
		protected PrecioMateriaPrima getElemento(int fila) {
			PrecioMateriaPrima pmt = (PrecioMateriaPrima)getTabla().getValueAt(fila, COL_OBJ);
			PrecioMateriaPrima clonePmt = clone(pmt);
			String alias = ((String)getTabla().getValueAt(fila, COL_ALIAS));
			if(alias != null) {
				pmt.setAlias(alias.trim().toUpperCase());
			} else {
				pmt.setAlias("");
			}
			Float precio = (Float)getTabla().getTypedValueAt(fila, COL_PRECIO);
			if(precio != null) {
				pmt.setPrecio(new BigDecimal(precio));
			}
			pmt.setMateriaPrima((MateriaPrima)getTabla().getValueAt(fila, COL_MAT_PRIMA));
			pmt.setTipoMoneda((ETipoMoneda)getTabla().getValueAt(fila, COL_TIPO_MONEDA));
			String siglas = (String)getTabla().getValueAt(fila, COL_SIGLAS);
			if(siglas != null) {
				pmt.setSiglas(siglas.trim().toUpperCase());
			}
			Float stock = (Float)getTabla().getTypedValueAt(fila, COL_STOCK_ACTUAL);
			if(stock != null) {
				pmt.setStockActual(new BigDecimal(stock));
				if(pmt.getStockInicial()==null){
					pmt.setStockInicial(new BigDecimal(stock));
					pmt.setStockInicialDisponible(new BigDecimal(stock));
				}
			}
			Float pPedido = (Float)getTabla().getTypedValueAt(fila, COL_PTO_PEDIDO);
			if(pPedido != null) {
				pmt.setPuntoDePedido(new BigDecimal(pPedido));
			}
			setModificarFecha(clonePmt, pmt);
			return pmt;
		}

		private void setModificarFecha(PrecioMateriaPrima clonePmt, PrecioMateriaPrima pmt) {
			if(clonePmt.getPrecio() != null && pmt.getPrecio() != null) {
				pmt.setActualizarHorario(!new Float(clonePmt.getPrecio().floatValue()).equals(new Float(pmt.getPrecio().floatValue())));
			}
			if(clonePmt.getAlias()!= null && pmt.getAlias() != null) {
				pmt.setActualizarHorario(pmt.isActualizarHorario() || clonePmt.getAlias().trim().compareToIgnoreCase(pmt.getAlias().trim()) != 0);
			}
		}

		private PrecioMateriaPrima clone(PrecioMateriaPrima pmt) {
			PrecioMateriaPrima cl = new PrecioMateriaPrima();
			cl.setAlias(pmt.getAlias());
			cl.setPrecio(pmt.getPrecio());
			//TODO: CAMBIAR, ESTO ES PARA QUE GUARDE
			cl.setPuntoDePedido(new BigDecimal(0d));
			cl.setStockActual(new BigDecimal(0d));
			return cl;
		}

		@Override
		protected String validarElemento(int fila) {
			if(isFilaSeparadora(fila)) {
				return null;
			}
			MateriaPrima mp = (MateriaPrima)getTabla().getValueAt(fila, COL_MAT_PRIMA);
			if(getTabla().getValueAt(fila, COL_PRECIO) == null) {
				return "Falta completar el 'Precio' para la materia prima " + mp.getDescripcion() + ".";
			}
			Float valueAt = (Float)getTabla().getTypedValueAt(fila, COL_PRECIO);
			if(valueAt <= 0) {
				return "El 'Precio' para la materia prima " + mp.getDescripcion() + " debe ser mayor a cero.";
			}
			if(getTabla().getValueAt(fila, COL_TIPO_MONEDA) == null) {
				return "Falta seleccionar el 'Tipo de Moneda' para la materia prima " + mp.getDescripcion() + ".";
			}
			return null;
		}

		private boolean isFilaSeparadora(int fila) {
			return getTabla().getValueAt(fila, COL_OBJ) == null;
		}

		@Override
		public boolean validarAgregar() {
			JDialogSeleccionarMateriaPrima jDialogSeleccionarMateriaPrima = new JDialogSeleccionarMateriaPrima(GuiABMListaDePreciosProveedor.this.getFrame(), allMateriaPrimaList);
			GuiUtil.centrar(jDialogSeleccionarMateriaPrima);
			jDialogSeleccionarMateriaPrima.setVisible(true);
			if(jDialogSeleccionarMateriaPrima.isAcepto()) {
				List<MateriaPrima> valoresSeleccionados = jDialogSeleccionarMateriaPrima.getMatPrimaSelectedList();
				if(!valoresSeleccionados.isEmpty()) {
					mirrorTablaList.clear();
					mirrorTablaList.addAll(getElementos());
					for(MateriaPrima mp : valoresSeleccionados) {
						if(getTabla().getFirstRowWithValue(COL_MAT_PRIMA, mp) == -1) {
							PrecioMateriaPrima pmp = new PrecioMateriaPrima();
							pmp.setMateriaPrima(mp);
							mirrorTablaList.add(pmp);
						}
					}
					refrescarItemsTablaWithMirrorList();
				}
				
			}
			
			return false;
		}

	}

}
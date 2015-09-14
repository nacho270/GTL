package ar.com.textillevel.gui.modulos.abm.listaprecios;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.boss.BossEstilos;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.templates.GuiABMListaTemplate;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.entidades.ventas.cotizacion.Cotizacion;
import ar.com.textillevel.entidades.ventas.cotizacion.DefinicionPrecio;
import ar.com.textillevel.entidades.ventas.cotizacion.GrupoTipoArticuloBaseEstampado;
import ar.com.textillevel.entidades.ventas.cotizacion.GrupoTipoArticuloGama;
import ar.com.textillevel.entidades.ventas.cotizacion.ListaDePrecios;
import ar.com.textillevel.entidades.ventas.cotizacion.PrecioBaseEstampado;
import ar.com.textillevel.entidades.ventas.cotizacion.PrecioGama;
import ar.com.textillevel.entidades.ventas.cotizacion.PrecioTipoArticulo;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAncho;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoArticuloEstampado;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoArticuloTenido;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoComun;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoCantidadColores;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoCoberturaEstampado;
import ar.com.textillevel.entidades.ventas.cotizacion.VersionListaDePrecios;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;
import ar.com.textillevel.facade.api.remote.ListaDePreciosFacadeRemote;
import ar.com.textillevel.facade.api.remote.TipoArticuloFacadeRemote;
import ar.com.textillevel.gui.modulos.abm.listaprecios.comun.JDialogAgregarModificarDefinicionPreciosComun;
import ar.com.textillevel.gui.modulos.abm.listaprecios.estampado.JDialogAgregarModificarDefinicionPreciosEstampado;
import ar.com.textillevel.gui.modulos.abm.listaprecios.tenido.JDialogAgregarModificarDefinicionPreciosTenido;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.dialogs.JDialogInputFechaWithCheckbox;
import ar.com.textillevel.util.GTLBeanFactory;

public class GuiABMListaDePrecios extends GuiABMListaTemplate {

	private static final long serialVersionUID = 8012369007737291095L;
	
	private JPanel tabDetalle;
	private JPanel panDetalle;

	private ListaDePrecios listaActual;
	
	private PanelTablaVersionesListaDePrecio tablaVersiones;
	private PanelTablaDefinicionesPrecio tablaDefiniciones;
	
	private boolean shortCutAgregar = false;
	private boolean isEdicion;
	private ClienteFacadeRemote clienteFacade;
	private ListaDePreciosFacadeRemote listaDePreciosFacade;
	
	public GuiABMListaDePrecios(Integer idModulo) {
		super();
		setHijoCreado(true);
		setTitle("Administrar Listas de Precios");
		constructGui();
		setEstadoInicial();
	}

	private void constructGui() {
		panTabs.addTab("Datos de la lista de precios", getTabDetalle());
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
			panDetalle.add(getTablaVersiones(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 1, 0.5));
			panDetalle.add(getTablaDefiniciones(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 1, 0.5));
		}
		return panDetalle;
	}
	

	@Override
	public void cargarLista() {
		List<Cliente> clientes = getClienteFacade().getAllOrderByName();
		lista.removeAll();
		for(Cliente c : clientes){
			lista.addItem(c);
		}
	}

	@Override
	public void botonAgregarPresionado(int arg0) {
	}

	@Override
	public void botonCancelarPresionado(int arg0) {
		setModoEdicion(false);
		shortCutAgregar = false;
		itemSelectorSeleccionado(lista.getSelectedIndex());
		habilitacionSinEdicion();
	}

	@Override
	public void botonEliminarPresionado(int arg0) {
		if(lista.getSelectedIndex() >= 0) {
			if(CLJOptionPane.showQuestionMessage(GuiABMListaDePrecios.this, "¿Está seguro que desea eliminar la lista de precios seleccionada?", "Confirmación") == CLJOptionPane.YES_OPTION) {
				getListaDePreciosFacade().remove(getListaActual());
				cargarLista();
				itemSelectorSeleccionado(-1);
			}
		}
	}

	@Override
	public boolean botonGrabarPresionado(int arg0) {
		if (getListaActual().getVersiones().isEmpty()) {
			CLJOptionPane.showErrorMessage(GuiABMListaDePrecios.this.getFrame(), "Debe agregar al menos una version", "Error");
			return false;
		}
		for(VersionListaDePrecios v : getListaActual().getVersiones()) {
			if (v.getPrecios().isEmpty()) {
				CLJOptionPane.showErrorMessage(GuiABMListaDePrecios.this.getFrame(), "Debe definir los precios para version con validez a partir de " + DateUtil.dateToString(v.getInicioValidez(), DateUtil.SHORT_DATE), "Error");
				return false;
			}
		}
		if(getListaActual().getVersionActual().getDefinicionPorTipoProducto(ETipoProducto.REPROCESO_SIN_CARGO) == null) {
			// agrego automaticamente el reproceso sin cargo con precio 0
			DefinicionPrecio definicionReprocesoSinCargo = new DefinicionPrecio();
			definicionReprocesoSinCargo.setTipoProducto(ETipoProducto.REPROCESO_SIN_CARGO);
			RangoAnchoComun rangoAnchoComun = new RangoAnchoComun();
			rangoAnchoComun.setAnchoMinimo(0f);
			rangoAnchoComun.setAnchoMaximo(50f);
			rangoAnchoComun.setDefinicionPrecio(definicionReprocesoSinCargo);
			List<TipoArticulo> tiposArticulo = GTLBeanFactory.getInstance().getBean2(TipoArticuloFacadeRemote.class).getAllTipoArticulos();
			for(TipoArticulo ta : tiposArticulo){
				PrecioTipoArticulo pta = new PrecioTipoArticulo();
				pta.setPrecio(0f);
				pta.setRangoAncho(rangoAnchoComun);
				pta.setTipoArticulo(ta);
				rangoAnchoComun.getPrecios().add(pta);
			}
			definicionReprocesoSinCargo.getRangos().add(rangoAnchoComun);
			getListaActual().getVersionActual().getPrecios().add(definicionReprocesoSinCargo);
		}
		getListaDePreciosFacade().save(getListaActual());
		return true;
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if(nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			if(getListaActual()==null) {
				getTablaVersiones().validarAgregar();
				shortCutAgregar = true;
			}
			return true;
		} else {
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar un cliente", "Error");
			return false;
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		if(nivelItemSelector >= 0) {
			limpiarDatos();
			Cliente cliente = (Cliente) lista.getSelectedValue();
			setListaActual(getListaDePreciosFacade().getListaByIdCliente(cliente.getId()));
			
			if(getListaActual() != null) {
				ordenarVersiones();
				getTablaVersiones().agregarElementos(getListaActual().getVersiones());
				getBtnModificar().setText("Modificar >>");
				setModoEdicion(false);
				getTablaVersiones().setCotizacionVigente(getListaDePreciosFacade().getCotizacionVigente(cliente));
			} else {
				getBtnModificar().setText("Agregar >>");
			}
		}
	}

	private void ordenarVersiones() {
		//ordeno en forma descendente
		Collections.sort(getListaActual().getVersiones(), new Comparator<VersionListaDePrecios>() {

			public int compare(VersionListaDePrecios o1, VersionListaDePrecios o2) {
				return o1.getInicioValidez().compareTo(o2.getInicioValidez())*(-1);
			}

		});
	}

	@Override
	public void limpiarDatos() {
		getTablaDefiniciones().getTabla().removeAllRows();
		getTablaVersiones().getTabla().removeAllRows();
	}

	@Override
	public void setEstadoInicial() {
		//setModoEdicion(false);
		habilitacionSinEdicion();
		cargarLista();
		if(lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	private void habilitacionSinEdicion() {
		this.isEdicion = false;
		GuiUtil.setEstadoPanel(getTabDetalle(), true);
		getTablaVersiones().getBotonAgregar().setEnabled(false);
//		getTablaVersiones().getBotonModificar().setEnabled(false);
		getTablaVersiones().getBotonEliminar().setEnabled(false);
		getTablaVersiones().getBtnImprimirVersion().setEnabled(false);
		getTablaDefiniciones().getBotonAgregar().setEnabled(false);
		getTablaDefiniciones().getBotonModificar().setEnabled(false);
		getTablaDefiniciones().getBotonEliminar().setEnabled(false);
	}

	@Override
	public void setModoEdicion(boolean estado) {
		this.isEdicion = estado;
		if(estado == false){
			habilitacionSinEdicion();
		}else{
			GuiUtil.setEstadoPanel(getTabDetalle(), estado);
			getTablaVersiones().getBotonEliminar().setEnabled(false);
			getTablaVersiones().getBtnImprimirVersion().setEnabled(false);
			GuiUtil.setEstadoPanel(getTablaDefiniciones(), estado);
			getTablaDefiniciones().getBotonModificar().setEnabled(false);
			getTablaDefiniciones().getBotonEliminar().setEnabled(false);
			getTablaDefiniciones().getBotonAgregar().setEnabled(false);
		}
		if(shortCutAgregar){
			if(getTablaVersiones().getTabla().getRowCount() > 0) {
				getTablaVersiones().getTabla().setRowSelectionInterval(0, 0);
				getTablaVersiones().handleClickTablaVersiones();
			}
		}
	}

	public ClienteFacadeRemote getClienteFacade() {
		if (clienteFacade == null) {
			clienteFacade = GTLBeanFactory.getInstance().getBean2(ClienteFacadeRemote.class);
		}
		return clienteFacade;
	}

	public ListaDePreciosFacadeRemote getListaDePreciosFacade() {
		if (listaDePreciosFacade == null) {
			listaDePreciosFacade = GTLBeanFactory.getInstance().getBean2(ListaDePreciosFacadeRemote.class);
		}
		return listaDePreciosFacade;
	}

	private class PanelTablaVersionesListaDePrecio extends PanelTabla<VersionListaDePrecios> {

		private static final long serialVersionUID = 524085936965031187L;

		private static final int CANT_COLS = 3;
		private static final int COL_FECHA_INICIO_VALIDEZ = 0;
		public static final int COL_ULT_COTIZACION = 1;
		private static final int COL_OBJ = 2;
		
		private JButton btnImprimirVersion;
		
		public PanelTablaVersionesListaDePrecio() {
			setBorder(BorderFactory.createTitledBorder("Versiones"));
			agregarBoton(getBtnImprimirVersion());
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_FECHA_INICIO_VALIDEZ, "VALIDA A PARTIR DE FECHA", 200, 200, true);
			tabla.setStringColumn(COL_ULT_COTIZACION, "ULTIMA COTIZACIÓN", 300, 300, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			tabla.setHeaderAlignment(COL_FECHA_INICIO_VALIDEZ, CLJTable.CENTER_ALIGN);
			tabla.setAlignment(COL_FECHA_INICIO_VALIDEZ, CLJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_ULT_COTIZACION, CLJTable.CENTER_ALIGN);
			tabla.setAlignment(COL_ULT_COTIZACION, CLJTable.CENTER_ALIGN);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setReorderingAllowed(false);
			tabla.setSelectionMode(CLJTable.SINGLE_SELECTION);
			tabla.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					handleClickTablaVersiones();
				}
			});
			return tabla;
		}

		@Override
		protected void agregarElemento(VersionListaDePrecios elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_FECHA_INICIO_VALIDEZ] = DateUtil.dateToString(elemento.getInicioValidez(), DateUtil.SHORT_DATE);
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected VersionListaDePrecios getElemento(int fila) {
			return (VersionListaDePrecios) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}
		
		@Override
		public boolean validarQuitar() {
			VersionListaDePrecios versionActual = getElemento(getTabla().getSelectedRow());
			boolean existeCotizacion = checkCotizacionVigente(versionActual);
			if(existeCotizacion) {
				return false;
			} else {
				getListaActual().getVersiones().remove(versionActual);
				return true;
			}
		}

		@Override
		public boolean validarAgregar() {
			boolean existenVersionesAnteriores = getTablaVersiones().getTabla().getRowCount() > 0;
			if(existenVersionesAnteriores) {
				VersionListaDePrecios ultVersion = getElemento(0);
				if(ultVersion.getId() == null) {
					CLJOptionPane.showErrorMessage(GuiABMListaDePrecios.this, StringW.wordWrap("No se puede agregar otra versión porque no se ha grabado la última agregada. Por favor, elimine esta última o bien grabe los cambios y reintente la operación."), "Error");
					return false;
				}
			}
			
			if(getListaActual() == null) {
				setListaActual(new ListaDePrecios());
				getListaActual().setCliente((Cliente) lista.getSelectedValue());
			}
			JDialogInputFechaWithCheckbox dialogoFecha = new JDialogInputFechaWithCheckbox(GuiABMListaDePrecios.this.getFrame(), "Fecha de inicio de validez", "Copiar última versión", existenVersionesAnteriores, existenVersionesAnteriores);
			dialogoFecha.setVisible(true);
			Date fechaInicioValidez = dialogoFecha.getFecha();
			if(fechaInicioValidez != null) {
				boolean hayVersionesMasViejas = hayVersionesMasViejas(fechaInicioValidez);
				if (getListaActual().getVersiones().isEmpty() || !hayVersionesMasViejas) {
					VersionListaDePrecios nuevaVersion = null;
					//Clono la última versión
					if(existenVersionesAnteriores && dialogoFecha.isChkSelected()) {
						nuevaVersion = getTablaVersiones().getElemento(0).deepClone();
						nuevaVersion.setInicioValidez(fechaInicioValidez);
					} else {
						nuevaVersion = new VersionListaDePrecios(fechaInicioValidez);
					}
					getListaActual().getVersiones().add(nuevaVersion);
					ordenarVersiones();
					
					getTablaVersiones().limpiar();
					getTablaVersiones().agregarElementos(getListaActual().getVersiones());
					
					setCotizacionVigente(getListaDePreciosFacade().getCotizacionVigente(getListaActual().getCliente()));
					
					getTablaVersiones().getTabla().setRowSelectionInterval(0, 0);
					handleClickTablaVersiones();
				}
			}
			return false;
		}

		private boolean hayVersionesMasViejas(Date fechaInicioValidez) {
			for(VersionListaDePrecios v : getListaActual().getVersiones()) {
				if(!v.getInicioValidez().before(fechaInicioValidez)) {
					return true;
				}
			}
			return false;
		}

		private JButton getBtnImprimirVersion() {
			if (btnImprimirVersion == null) {
				btnImprimirVersion = BossEstilos.createButton("ar/com/textillevel/imagenes/b_imprimir_moderno.png", "ar/com/textillevel/imagenes/b_imprimir_moderno_des.png");
				btnImprimirVersion.setEnabled(false);
				btnImprimirVersion.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int selectedRow = getTabla().getSelectedRow();
						VersionListaDePrecios version = getTablaVersiones().getElemento(selectedRow);
						if(version.getId() == null) {//i.e es una versión no persistida se avisa que primero grabe y después imprima
							CLJOptionPane.showErrorMessage(GuiABMListaDePrecios.this, StringW.wordWrap("La versión seleccionada no ha sido grabada y no se puede generar la cotización. Por favor, grabe los cambios y después reintente."), "Error");
							return;
						}
						new ImprimirListaDePreciosHandler(GuiABMListaDePrecios.this.getFrame(), getListaActual().getCliente(), version).imprimir();
						
						getTablaVersiones().setCotizacionVigente(getListaDePreciosFacade().getCotizacionVigente(getListaActual().getCliente()));
					}
				});
			}
			return btnImprimirVersion;
		}

		private void handleClickTablaVersiones() {
			int selectedRow = getTabla().getSelectedRow();
			if (selectedRow >= 0) {
				VersionListaDePrecios version = getElemento(selectedRow);
				if (version != null) {
					getTablaDefiniciones().getBotonAgregar().setEnabled(isEdicion);
					getTablaDefiniciones().getBotonModificar().setEnabled(getTablaDefiniciones().getTabla().getSelectedRow() > -1 && isEdicion);
					getTablaDefiniciones().limpiar();
					getTablaDefiniciones().agregarElementos(version.getPrecios());
					//sólo se habilita con la última versión de lista de precios y si hay precios configurados o bien una que nos la última pero q tiene una cotización vigente
					getBtnImprimirVersion().setEnabled((getTablaVersiones().getTabla().getValueAt(selectedRow, PanelTablaVersionesListaDePrecio.COL_ULT_COTIZACION) != null || selectedRow == 0) && version.getPrecios().size() > 0 && isEdicion);
					getTablaVersiones().getBotonEliminar().setEnabled(isEdicion);
				} else {
					getTablaDefiniciones().getBotonAgregar().setEnabled(false);
					getBtnImprimirVersion().setEnabled(false);
					getTablaVersiones().getBotonEliminar().setEnabled(false);
				}
			}else{
				getTablaDefiniciones().getBotonAgregar().setEnabled(false);
				getTablaDefiniciones().getBotonModificar().setEnabled(false);
				getBtnImprimirVersion().setEnabled(false);
				getTablaDefiniciones().limpiar();
			}
		}

		public VersionListaDePrecios getVersionSeleccionada() {
			if(getTabla().getSelectedRow() != -1) {
				return getTablaVersiones().getElemento(getTabla().getSelectedRow());
			} else {
				return null;
			}
		}

		public void setCotizacionVigente(Cotizacion cotizacion) {
			if(cotizacion != null) {
				for(int i=0; i < getTabla().getRowCount(); i++) {
					VersionListaDePrecios elemento = getElemento(i);
					if(cotizacion.getVersionListaPrecio().getId().equals(elemento.getId())) {
						getTabla().setValueAt("COTIZACION NRO. '" + cotizacion.getNumero() + "' VIGENTE. VENCE: " + cotizacion.getFechaVencimientoStr(), i, COL_ULT_COTIZACION);
						return;
					}
				}
			}
		}
	
	}
	
	private class PanelTablaDefinicionesPrecio extends PanelTabla<DefinicionPrecio> {

		private static final long serialVersionUID = -5833558544844113513L;

		private static final int CANT_COLS = 3;
		private static final int COL_TIPO_PRODUCTO = 0;
		private static final int COL_RESUMEN = 1;
		private static final int COL_OBJ = 2;
		
		public PanelTablaDefinicionesPrecio() {
			setBorder(BorderFactory.createTitledBorder("Precios por tipo de producto"));
			agregarBotonModificar();
			getBotonAgregar().setEnabled(false);
			getBotonEliminar().setEnabled(false);
			getBotonModificar().setEnabled(false);
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_TIPO_PRODUCTO, "TIPO DE PRODUCTO", 200, 200, true);
			tabla.setMultilineColumn(COL_RESUMEN, "RESUMEN", 350, true, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			tabla.setHeaderAlignment(COL_TIPO_PRODUCTO, CLJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_RESUMEN, CLJTable.CENTER_ALIGN);
			tabla.setAlignment(COL_TIPO_PRODUCTO, CLJTable.CENTER_ALIGN);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setReorderingAllowed(false);
			tabla.setSelectionMode(CLJTable.SINGLE_SELECTION);
			tabla.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int selectedRow = getTabla().getSelectedRow();
					if (selectedRow >= 0) {
						if(isEdicion && e.getClickCount() == 2){
							botonModificarPresionado(selectedRow);
						}
					}
				}
			});
			return tabla;
		}
		
		@Override
		protected void filaTablaSeleccionada() {
			int selectedRow = getTabla().getSelectedRow();
			if(selectedRow > -1) {
				DefinicionPrecio definicionPrecio = getElemento(selectedRow);
				if (definicionPrecio.getTipoProducto()==ETipoProducto.REPROCESO_SIN_CARGO) {
					getBotonAgregar().setEnabled(false);
					getBotonModificar().setEnabled(false);
					getBotonEliminar().setEnabled(false);
				} else {
					getBotonModificar().setEnabled(isEdicion);
					getBotonEliminar().setEnabled(isEdicion);
				}
			}
		}

		@Override
		protected void agregarElemento(DefinicionPrecio elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_TIPO_PRODUCTO] = elemento.getTipoProducto();
			row[COL_RESUMEN] = GeneradorResumen.generarResumen(elemento);
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);			
		}

		@Override
		protected DefinicionPrecio getElemento(int fila) {
			return (DefinicionPrecio) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		} 
		
		@Override
		public boolean validarAgregar() {
			ETipoProducto tipoProductoSeleccionado = seleccionarTipoProducto();
			if (tipoProductoSeleccionado != null) {
				JDialogAgregarModificarDefinicionPrecios<? extends RangoAncho, ?> d = createDialogForTipoArticulo(tipoProductoSeleccionado, false);
				if (tipoProductoSeleccionado == ETipoProducto.TENIDO && !d.isAcepto()) {
					return false;
				}
				d.setVisible(true);
				if (d.isAcepto()) {
					VersionListaDePrecios versionSeleccionada = getTablaVersiones().getElemento(getTablaVersiones().getTabla().getSelectedRow());
					versionSeleccionada.getPrecios().add(d.getDefinicion());
					refrescarTabla();
				}
			}
			return false;
		}
		
		@SuppressWarnings("unchecked")
		private <T extends RangoAncho, D extends JDialogAgregarModificarDefinicionPrecios<T, ?>> D createDialogForTipoArticulo(ETipoProducto tipoProductoSeleccionado, boolean isModificar) {
			DefinicionPrecio defincionAModificar = null;
			Cliente cliente = (Cliente) lista.getSelectedValue();
			if (isModificar) {
				int fila = getTabla().getSelectedRow();
				if (fila == -1) {
					throw new RuntimeException("No hay fila seleccionada");
				}
				defincionAModificar = getElemento(fila);
			}
			if (tipoProductoSeleccionado == ETipoProducto.TENIDO) {
				if (isModificar) {
					return (D) new JDialogAgregarModificarDefinicionPreciosTenido(GuiABMListaDePrecios.this.getFrame(), cliente, tipoProductoSeleccionado, defincionAModificar);
				}
				return (D) new JDialogAgregarModificarDefinicionPreciosTenido(GuiABMListaDePrecios.this.getFrame(), cliente, tipoProductoSeleccionado);
			}
			if (tipoProductoSeleccionado == ETipoProducto.ESTAMPADO) {
				if (isModificar) {
					return (D) new JDialogAgregarModificarDefinicionPreciosEstampado(GuiABMListaDePrecios.this.getFrame(), cliente, tipoProductoSeleccionado, defincionAModificar);
				}
				return (D) new JDialogAgregarModificarDefinicionPreciosEstampado(GuiABMListaDePrecios.this.getFrame(), cliente, tipoProductoSeleccionado);
			}
			if (isModificar) {
				return (D) new JDialogAgregarModificarDefinicionPreciosComun(GuiABMListaDePrecios.this.getFrame(), cliente, tipoProductoSeleccionado, defincionAModificar);
			}
			return (D) new JDialogAgregarModificarDefinicionPreciosComun(GuiABMListaDePrecios.this.getFrame(), cliente, tipoProductoSeleccionado);
		}

		
		
		private void refrescarTabla() {
			getTabla().removeAllRows();
			agregarElementos(getTablaVersiones().getVersionSeleccionada().getPrecios());
		}

		@Override
		public boolean validarQuitar() {
			VersionListaDePrecios versionSeleccionada = getTablaVersiones().getVersionSeleccionada();
			boolean existeCotizacionVigente = checkCotizacionVigente(versionSeleccionada);
			if(existeCotizacionVigente) {
				return false;
			} else {
				versionSeleccionada.getPrecios().remove(getElemento(getTablaDefiniciones().getTabla().getSelectedRow()));
				return true;
			}
		}

		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			VersionListaDePrecios versionSeleccionada = getTablaVersiones().getVersionSeleccionada();
			boolean existeCotizacion = checkCotizacionVigente(versionSeleccionada);
			if(existeCotizacion) {
				return;
			}
			DefinicionPrecio definicionSeleccionada = getElemento(getTabla().getSelectedRow());
			JDialogAgregarModificarDefinicionPrecios<? extends RangoAncho, ?> d = createDialogForTipoArticulo(definicionSeleccionada.getTipoProducto(), true);
			d.setVisible(true);
			if (d.isAcepto()) {
				versionSeleccionada.getPrecios().set(getTabla().getSelectedRow(), d.getDefinicion());
				refrescarTabla();
			}
		}

		private ETipoProducto seleccionarTipoProducto() {
			final List<ETipoProducto> tiposUsados = new ArrayList<ETipoProducto>();
			CollectionUtils.forAllDo(getElementos(), new Closure() {
				public void execute(Object arg0) {
					tiposUsados.add( ((DefinicionPrecio) arg0).getTipoProducto());
				}
			});
			Collection<ETipoProducto> disjuncion = GenericUtils.restaConjuntosOrdenada(Arrays.asList(ETipoProducto.values()), tiposUsados);
			disjuncion.remove(ETipoProducto.REPROCESO_SIN_CARGO);
			Object[] disjuncionArray = disjuncion.toArray();
			String[] tiposProducto= new String[disjuncionArray.length];
			for(int i = 0 ; i < disjuncionArray.length;i++){
				tiposProducto[i] = ((ETipoProducto)disjuncionArray[i]).getDescripcion();
			}
			Object opcion = JOptionPane.showInputDialog(null, "Seleccione el tipo de producto:", "Lista de opciones", JOptionPane.INFORMATION_MESSAGE, null, tiposProducto,tiposProducto[0]);
			if(opcion!=null){
				return ETipoProducto.getByDescripcion((String)opcion);
			}
			return null;
		}
	}

	private PanelTablaVersionesListaDePrecio getTablaVersiones() {
		if (tablaVersiones == null) {
			tablaVersiones = new PanelTablaVersionesListaDePrecio();
		}
		return tablaVersiones;
	}

	public PanelTablaDefinicionesPrecio getTablaDefiniciones() {
		if (tablaDefiniciones == null) {
			tablaDefiniciones = new PanelTablaDefinicionesPrecio();
		}
		return tablaDefiniciones;
	}

	public ListaDePrecios getListaActual() {
		return listaActual;
	}

	public void setListaActual(ListaDePrecios listaActual) {
		this.listaActual = listaActual;
	}

	private boolean checkCotizacionVigente(VersionListaDePrecios versionParaEditar) {
		boolean existenVersionesAnteriores = getTablaVersiones().getTabla().getRowCount() > 0;
		if(versionParaEditar.getId() != null) {
			Cotizacion ultimaCotizacion = getListaDePreciosFacade().getUltimaCotizacionVigente(versionParaEditar);
			if(ultimaCotizacion != null) {
				int res = CLJOptionPane.showQuestionMessage(GuiABMListaDePrecios.this, StringW.wordWrap("No se puede editar la lista de precios porque la cotización NRO. '" + ultimaCotizacion.getNumero() + "' aún está vigente. ¿Desea crear una nueva versión?."), "Advertencia");
				if(CLJOptionPane.YES_OPTION == res) {
					JDialogInputFechaWithCheckbox dialogoFecha = new JDialogInputFechaWithCheckbox(GuiABMListaDePrecios.this.getFrame(), "Fecha de inicio de validez", "Copiar última versión", existenVersionesAnteriores, existenVersionesAnteriores);
					dialogoFecha.setVisible(true);
					Date fechaInicioValidez = dialogoFecha.getFecha();
					if(fechaInicioValidez != null) {
						VersionListaDePrecios nuevaVersion = versionParaEditar.deepClone();
						nuevaVersion.setInicioValidez(fechaInicioValidez);
						getListaActual().getVersiones().add(nuevaVersion);
						ordenarVersiones();
						getTablaVersiones().limpiar();
						getTablaVersiones().agregarElementos(getListaActual().getVersiones());
						getTablaVersiones().setCotizacionVigente(getListaDePreciosFacade().getCotizacionVigente(getListaActual().getCliente()));
					}
					return true;
				} else {
					return true;
				}
			}
		}
		return false;
	}
	
	private static class GeneradorResumen {
		public static String generarResumen(DefinicionPrecio definicion) {
			String descripcion = "";
			for(RangoAncho ra : definicion.getRangos()) {
				if(ra instanceof RangoAnchoArticuloEstampado) {
					descripcion += generarResumenRango((RangoAnchoArticuloEstampado) ra, definicion.getTipoProducto().getUnidad());
				} else if (ra instanceof RangoAnchoArticuloTenido){
					descripcion += generarResumenRango((RangoAnchoArticuloTenido) ra, definicion.getTipoProducto().getUnidad());
				} else {
					descripcion += generarResumenRango((RangoAnchoComun) ra, definicion.getTipoProducto().getUnidad());
				}
			}
			return "<html>" + descripcion + "</html>";
		}

		private static String generarResumenRango(RangoAnchoComun ra, EUnidad unidad) {
			String descripcion = "<b>" + ra.toStringConUnidad(EUnidad.METROS) + "</b><br>";
			for (PrecioTipoArticulo pta : ra.getPrecios()) {
				descripcion += pta.getTipoArticulo().getSigla() 
							+ " ==> $ <b>" + GenericUtils.getDecimalFormat().format(pta.getPrecio()) + " * x "
							+ unidad.getDescripcion().toLowerCase() + "</b><br>";
				descripcion += "<br>";
			}
			return descripcion;
		}
		
		private static String generarResumenRango(RangoAnchoArticuloEstampado ra, EUnidad unidad) {
			String descripcion = "<b>" + ra.toStringConUnidad(EUnidad.METROS) + "</b><br>";
			for (GrupoTipoArticuloBaseEstampado gtabe : ra.getGruposBase()) {
				for (PrecioBaseEstampado pbe : gtabe.getPrecios()) {
					for (RangoCantidadColores rcc : pbe.getRangosDeColores()) {
						for (RangoCoberturaEstampado rce : rcc.getRangos()) {
							descripcion += gtabe.getTipoArticulo().getSigla() + " - "
									+ "Base " + pbe.getGama().getNombre().toUpperCase() + " <br>"
									+ "- " + rcc.getMinimo() + " a " + rcc.getMaximo() + " colores "
									+ " y " + rce.getMinimo() + "&#37; a " + rce.getMaximo() + "&#37;  de cobertura "
									+ " ==> $ <b> " + GenericUtils.getDecimalFormat().format(rce.getPrecio()) + " * x " + unidad.getDescripcion().toLowerCase() + "</b><br>";
						}
					}
				}
				descripcion += "<br>";
			}
			return descripcion;
		}
		
		private static String generarResumenRango(RangoAnchoArticuloTenido ra, EUnidad unidad) {
			String descripcion = "<b>" + ra.toStringConUnidad(EUnidad.METROS) + "</b><br>";
			for (GrupoTipoArticuloGama gtag : ra.getGruposGama()) {
				for (PrecioGama pg : gtag.getPrecios()) {
					descripcion += gtag.getTipoArticulo().getSigla() + " - "
							+ pg.getGamaCliente().getNombre() + " ==> $ <b>" + GenericUtils.getDecimalFormat().format(pg.getPrecio()) + " * x "
							+ unidad.getDescripcion().toLowerCase() + "</b><br>";
				}
				descripcion += "<br>";
			}
			return descripcion;
		}
	}
}
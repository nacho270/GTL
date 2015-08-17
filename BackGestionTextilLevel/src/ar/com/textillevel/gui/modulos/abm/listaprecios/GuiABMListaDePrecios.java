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
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;

import ar.clarin.fwjava.boss.BossEstilos;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.templates.GuiABMListaTemplate;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.cotizacion.DefinicionPrecio;
import ar.com.textillevel.entidades.ventas.cotizacion.ListaDePrecios;
import ar.com.textillevel.entidades.ventas.cotizacion.VersionListaDePrecios;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;
import ar.com.textillevel.facade.api.remote.ListaDePreciosFacadeRemote;
import ar.com.textillevel.gui.modulos.abm.listaprecios.comun.JDialogAgregarModificarDefinicionPreciosComun;
import ar.com.textillevel.gui.modulos.abm.listaprecios.estampado.JDialogAgregarModificarDefinicionPreciosEstampado;
import ar.com.textillevel.gui.modulos.abm.listaprecios.tenido.JDialogAgregarModificarDefinicionPreciosTenido;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.dialogs.JDialogInputFecha;
import ar.com.textillevel.util.GTLBeanFactory;

public class GuiABMListaDePrecios extends GuiABMListaTemplate {

	private static final long serialVersionUID = 8012369007737291095L;
	
	private JPanel tabDetalle;
	private JPanel panDetalle;

	private ListaDePrecios listaActual;
	
	private PanelTablaVersionesListaDePrecio tablaVersiones;
	private PanelTablaDefinicionesPrecio tablaDefiniciones;
	
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
		itemSelectorSeleccionado(lista.getSelectedIndex());
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
		getListaDePreciosFacade().save(getListaActual());
		return false;
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if(nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
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
			if (getListaActual() != null) {
				getTablaVersiones().agregarElementos(getListaActual().getVersiones());
			}
		}
	}

	@Override
	public void limpiarDatos() {
		getTablaDefiniciones().getTabla().removeAllRows();
		getTablaVersiones().getTabla().removeAllRows();
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicion(false);
		cargarLista();
		if(lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);
		getTablaVersiones().getBotonEliminar().setEnabled(false);
		getTablaVersiones().getBtnImprimirVersion().setEnabled(false);
		GuiUtil.setEstadoPanel(getTablaDefiniciones(), false);
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

		private static final int CANT_COLS = 2;
		private static final int COL_FECHA_INICIO_VALIDEZ = 0;
		private static final int COL_OBJ = 1;
		
		private JButton btnImprimirVersion;
		
		public PanelTablaVersionesListaDePrecio() {
			setBorder(BorderFactory.createTitledBorder("Versiones"));
			agregarBoton(getBtnImprimirVersion());
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_FECHA_INICIO_VALIDEZ, "VALIDA A PARTIR DE FECHA", 200, 200, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			tabla.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int selectedRow = getTabla().getSelectedRow();
					if (selectedRow >= 0) {
						VersionListaDePrecios version = getElemento(selectedRow);
						if (version != null) {
							getTablaDefiniciones().getBotonAgregar().setEnabled(true);
							getTablaDefiniciones().agregarElementos(version.getPrecios());
							getBtnImprimirVersion().setEnabled(true);
						} else {
							getTablaDefiniciones().getBotonAgregar().setEnabled(false);
							getBtnImprimirVersion().setEnabled(false);
						}
					}else{
						getTablaDefiniciones().getBotonAgregar().setEnabled(false);
						getBtnImprimirVersion().setEnabled(false);
					}
						
				}
			});
			tabla.setHeaderAlignment(COL_FECHA_INICIO_VALIDEZ, CLJTable.CENTER_ALIGN);
			tabla.setAlignment(COL_FECHA_INICIO_VALIDEZ, CLJTable.CENTER_ALIGN);
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
			getListaActual().getVersiones().remove(getElemento(getTabla().getSelectedRow()));
			return true;
		}

		@Override
		public boolean validarAgregar() {
			if(getListaActual() == null) {
				setListaActual(new ListaDePrecios());
			}
			JDialogInputFecha dialogoFecha = new JDialogInputFecha(GuiABMListaDePrecios.this.getFrame(), "Fecha de inicio de validez");
			dialogoFecha.setVisible(true);
			Date fechaInicioValidez = dialogoFecha.getFecha();
			if(fechaInicioValidez != null) {
				VersionListaDePrecios nuevaVersion = new VersionListaDePrecios(fechaInicioValidez);
				getListaActual().getVersiones().add(nuevaVersion);
				agregarElemento(nuevaVersion);
			}
			return false;
		}

		public JButton getBtnImprimirVersion() {
			if (btnImprimirVersion == null) {
				btnImprimirVersion = BossEstilos.createButton("ar/com/textillevel/imagenes/b_imprimir_moderno.png", "ar/com/textillevel/imagenes/b_imprimir_moderno_des.png");
				btnImprimirVersion.setEnabled(false);
				btnImprimirVersion.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						new ImprimirListaDePreciosHandler().imprimir();
					}
				});
			}
			return btnImprimirVersion;
		}
	}
	
	private class PanelTablaDefinicionesPrecio extends PanelTabla<DefinicionPrecio> {

		private static final long serialVersionUID = -5833558544844113513L;

		private static final int CANT_COLS = 2;
		private static final int COL_TIPO_PRODUCTO = 0;
		private static final int COL_OBJ = 1;
		
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
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			tabla.setHeaderAlignment(COL_TIPO_PRODUCTO, CLJTable.CENTER_ALIGN);
			tabla.setAlignment(COL_TIPO_PRODUCTO, CLJTable.CENTER_ALIGN);
			return tabla;
		}

		@Override
		protected void agregarElemento(DefinicionPrecio elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_TIPO_PRODUCTO] = elemento.getTipoProducto();
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
				JDialogAgregarModificarDefinicionPrecios d = createDialogForTipoArticulo(tipoProductoSeleccionado, false);
				d.setVisible(true);
				if (d.isAcepto()) {
					VersionListaDePrecios versionSeleccionada = getTablaVersiones().getElemento(getTablaVersiones().getTabla().getSelectedRow());
					versionSeleccionada.getPrecios().add(d.getDefinicion());
					refrescarTabla();
				}
			}
			return false;
		}
		
		private JDialogAgregarModificarDefinicionPrecios createDialogForTipoArticulo(ETipoProducto tipoProductoSeleccionado, boolean isModificar) {
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
					return new JDialogAgregarModificarDefinicionPreciosTenido(GuiABMListaDePrecios.this.getFrame(), cliente, tipoProductoSeleccionado, defincionAModificar);
				}
				return new JDialogAgregarModificarDefinicionPreciosTenido(GuiABMListaDePrecios.this.getFrame(), cliente, tipoProductoSeleccionado);
			}
			if (tipoProductoSeleccionado == ETipoProducto.ESTAMPADO) {
				if (isModificar) {
					return new JDialogAgregarModificarDefinicionPreciosEstampado(GuiABMListaDePrecios.this.getFrame(), cliente, tipoProductoSeleccionado, defincionAModificar);
				}
				return new JDialogAgregarModificarDefinicionPreciosEstampado(GuiABMListaDePrecios.this.getFrame(), cliente, tipoProductoSeleccionado);
			}
			if (isModificar) {
				return new JDialogAgregarModificarDefinicionPreciosComun(GuiABMListaDePrecios.this.getFrame(), cliente, tipoProductoSeleccionado, defincionAModificar);
			}
			return new JDialogAgregarModificarDefinicionPreciosComun(GuiABMListaDePrecios.this.getFrame(), cliente, tipoProductoSeleccionado);
		}

		private void refrescarTabla() {
			getTabla().removeAllRows();
			agregarElementos(getTablaVersiones().getElemento(getTablaVersiones().getTabla().getSelectedRow()).getPrecios());
		}

		@Override
		public boolean validarQuitar() {
			VersionListaDePrecios versionSeleccionada = getTablaVersiones().getElemento(getTabla().getSelectedRow());
			versionSeleccionada.getPrecios().remove(getElemento(getTablaDefiniciones().getTabla().getSelectedRow()));
			return true;
		}
		
		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {

		}
		
		private ETipoProducto seleccionarTipoProducto() {
			final List<ETipoProducto> tiposUsados = new ArrayList<ETipoProducto>();
			CollectionUtils.forAllDo(getElementos(), new Closure() {
				public void execute(Object arg0) {
					tiposUsados.add( ((DefinicionPrecio) arg0).getTipoProducto());
				}
			});
			Collection<ETipoProducto> disjuncion = GenericUtils.restaConjuntosOrdenada(Arrays.asList(ETipoProducto.values()), tiposUsados);
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

	public PanelTablaVersionesListaDePrecio getTablaVersiones() {
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
}
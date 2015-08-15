package ar.com.textillevel.gui.modulos.abm;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.templates.GuiABMListaTemplate;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.cotizacion.DefinicionPrecio;
import ar.com.textillevel.entidades.ventas.cotizacion.ListaDePrecios;
import ar.com.textillevel.entidades.ventas.cotizacion.VersionListaDePrecios;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;
import ar.com.textillevel.facade.api.remote.ListaDePreciosFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.dialogs.JDialogInputFecha;
import ar.com.textillevel.util.GTLBeanFactory;

public class GuiABMListaDePrecios extends GuiABMListaTemplate {

	private static final long serialVersionUID = 8012369007737291095L;
	
	private static final String ICONO_BOTON_MODIF = "ar/clarin/fwjava/imagenes/b_modificar_fila.png";
	private static final String ICONO_BOTON_MODIF_DES = "ar/clarin/fwjava/imagenes/b_modificar_fila_des.png";
	private static final String ICONO_BOTON_VOLVER_A_DEFAULT = "ar/clarin/fwjava/imagenes/b_autocomp.png";
	private static final String ICONO_BOTON_VOLVER_A_DEFAULT_DES = "ar/clarin/fwjava/imagenes/b_autocomp_des.png";

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
		
		public PanelTablaVersionesListaDePrecio() {
			setBorder(BorderFactory.createTitledBorder("Versiones"));
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_FECHA_INICIO_VALIDEZ, "VALIDA A PARTIR DE FECHA", 200, 200, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			tabla.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					VersionListaDePrecios version = getElemento(getTabla().getSelectedRow());
					if (version != null) {
						getTablaDefiniciones().getBotonAgregar().setEnabled(true);
						getTablaDefiniciones().agregarElementos(version.getPrecios());
					} else {
						getTablaDefiniciones().getBotonAgregar().setEnabled(false);
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
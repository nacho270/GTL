package ar.com.textillevel.gui.modulos.odt.gui.secuencias;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToggleButton.ToggleButtonModel;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.ImageUtil;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.gui.modulos.odt.gui.secuencias.event.DobleClickPasoSecuenciaEventListener;
import ar.com.textillevel.gui.modulos.odt.gui.secuencias.event.SecuenciaBloqueadaEventData;
import ar.com.textillevel.gui.modulos.odt.gui.secuencias.event.SecuenciaBloqueadaEventListener;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelBusquedaClienteMinimal;
import ar.com.textillevel.modulos.odt.entidades.secuencia.generica.SecuenciaTipoProducto;

public class PanelVisualizadorSecuencias extends JPanel{

	private static final long serialVersionUID = 3332524807368551332L;

	private PanelTablaSecuencia panelTablaSecuencias;
	private PanelPasosSecuencia panelPasos;
	private PanelBusquedaClienteMinimal panelFiltro;
	
	private final EventListenerList listeners = new EventListenerList();
	
	private final Frame frame;
	private final Dialog dialog;
	private final boolean edicion;
	
	public PanelVisualizadorSecuencias(Frame frame, boolean edicion){
		this.frame = frame;
		this.dialog = null;
		this.edicion = edicion;
		construct();
	}

	private void construct() {
		setLayout(new GridBagLayout());
		add(getPanelFiltro(),GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		add(getPanelTablaSecuencias(),GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		add(getPanelPasos(),GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
	}

	public PanelVisualizadorSecuencias(Dialog dialog, boolean edicion) {
		this.frame = null;
		this.dialog = dialog;
		this.edicion = edicion;
		construct();
	}

	public void addDobleClickSecuenciaEventListener(DobleClickPasoSecuenciaEventListener event){
		getPanelPasos().addDobleClickSecuenciaEventListener(event);
	}
	
	public PanelPasosSecuencia getPanelPasos() {
		if(panelPasos == null){
			if(frame == null) {
				panelPasos = new PanelPasosSecuencia(dialog,null, edicion);
			}else{
				panelPasos =  new PanelPasosSecuencia(frame,null, edicion);
			}
		}
		return panelPasos;
	}
	
	private void loadPasos(SecuenciaTipoProducto elemento) {
		getPanelPasos().setSecuenciaActual(elemento);
		getPanelPasos().loadData();
	}
	
	public void setModoNoModificacion(){
		GuiUtil.setEstadoPanel(getPanelFiltro(), false);
		getPanelTablaSecuencias().getBotonAgregar().setVisible(false);
		getPanelTablaSecuencias().getBotonEliminar().setVisible(false);
		if(getPanelTablaSecuencias().getBotonModificar()!=null){
			getPanelTablaSecuencias().getBotonModificar().setVisible(false);
		}
	}
	
	public void setModoConsulta(boolean estado) {
		GuiUtil.setEstadoPanel(getPanelFiltro(), estado);
		getPanelTablaSecuencias().setModoConsulta(!estado);
	}
	
	public void agregarBotonFijarSecuencia(){
		getPanelTablaSecuencias().agregarBotonFijarSecuencia();
	}
	
	public void addSecuenciaBloqueadaEventListener(SecuenciaBloqueadaEventListener l){
		listeners.add(SecuenciaBloqueadaEventListener.class, l);
	}
	
	private class PanelTablaSecuencia extends PanelTabla<SecuenciaTipoProducto>{

		private static final long serialVersionUID = 3557061364474197775L;

		private static final int CANT_COLS = 3;
		private static final int COL_CLIENTE = 0;
		private static final int COL_NOMBRE_SECUENCIA = 1;
		private static final int COL_OBJ = 2;
		
		private JToggleButton btnFijarSecuencia;
		
		public PanelTablaSecuencia(){
			agregarBotonModificar();
		}
		
		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_CLIENTE, "Cliente", 200, 200, true);
			tabla.setStringColumn(COL_NOMBRE_SECUENCIA,"Secuencia",200, 200, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setHeaderAlignment(COL_CLIENTE, CLJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_NOMBRE_SECUENCIA, CLJTable.CENTER_ALIGN);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setReorderingAllowed(false);
			return tabla;
		}
		
		@Override
		protected void dobleClickTabla(int filaSeleccionada) {
			getBtnFijarSecuencia().doClick();
		}

		@Override
		protected void filaTablaSeleccionada() {
			if(getTabla().getSelectedRow()>-1){
				loadPasos(getElemento(getTabla().getSelectedRow()));
				getBtnFijarSecuencia().setEnabled(true);
			}else{
				getPanelPasos().limpiar();
				getBtnFijarSecuencia().setEnabled(false);
			}
		}
		
		@Override
		protected void agregarElemento(SecuenciaTipoProducto elemento) {
			String cl = elemento.getCliente()!=null?elemento.getCliente().getDescripcionResumida():"01";
			getTabla().addRow(new Object[]{cl,elemento.getNombre(),elemento});
		}

		@Override
		protected SecuenciaTipoProducto getElemento(int fila) {
			return (SecuenciaTipoProducto)getTabla().getValueAt(fila, COL_OBJ);
		}
		
		@Override
		public boolean validarAgregar() {
			return PanelVisualizadorSecuencias.this.validarAgregar();
		}

		@Override
		public boolean validarQuitar() {
			return PanelVisualizadorSecuencias.this.validarQuitar();
		}
		
		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			PanelVisualizadorSecuencias.this.botonModificarPresionado(filaSeleccionada);
		}
		
		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		public void agregarBotonFijarSecuencia(){
			getPanelBotonesExtra().add(getBtnFijarSecuencia());
		}
		
		public JToggleButton getBtnFijarSecuencia() {
			if (btnFijarSecuencia == null) {
				btnFijarSecuencia = new JToggleButton();
				btnFijarSecuencia.setContentAreaFilled(false);
				btnFijarSecuencia.setEnabled(false);
				btnFijarSecuencia.setBorder(null);
				Icon icon = ImageUtil.loadIcon("ar/com/textillevel/imagenes/b_candado.png");
				btnFijarSecuencia.setIcon(icon);
				btnFijarSecuencia.setSelectedIcon(ImageUtil.loadIcon("ar/com/textillevel/imagenes/b_candado_pressed.png"));
				btnFijarSecuencia.setRolloverSelectedIcon(ImageUtil.loadIcon("ar/com/textillevel/imagenes/b_candado_pressed.png"));
				btnFijarSecuencia.setDisabledIcon(ImageUtil.loadIcon("ar/com/textillevel/imagenes/b_candado_des.png"));
				btnFijarSecuencia.setFocusPainted(false);
				btnFijarSecuencia.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
				btnFijarSecuencia.setSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
				btnFijarSecuencia.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						boolean lock = ((ToggleButtonModel) btnFijarSecuencia.getModel()).isSelected();
						GuiUtil.setEstadoPanel(getPanelTablaSecuencias(), !lock);
						getBtnFijarSecuencia().setEnabled(true);
						final SecuenciaBloqueadaEventData evtData = new SecuenciaBloqueadaEventData(lock ? getElemento(getTabla().getSelectedRow()) : null);
						final SecuenciaBloqueadaEventListener[] l = listeners.getListeners(SecuenciaBloqueadaEventListener.class);
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								for (int i = 0; i < l.length; i++) {
									l[i].secuenciaBloqueada(evtData);
								}
							}
						});
					}
				});
			}
			return btnFijarSecuencia;
		}
	}
	
	public PanelTablaSecuencia getPanelTablaSecuencias() {
		if(panelTablaSecuencias == null){
			panelTablaSecuencias = new PanelTablaSecuencia();
			panelTablaSecuencias.setPreferredSize(new Dimension(200, 200));
			panelTablaSecuencias.setSize(new Dimension(200, 200));
		}
		return panelTablaSecuencias;
	}
	
	public void botonModificarPresionado(int filaSeleccionada) {

	}

	public boolean validarQuitar() {
		return false;
	}

	public boolean validarAgregar() {
		return false;
	}

	protected void clienteEncontrado(Cliente cliente) {
		
	}

	protected void botonLimpiarPresionado() {
		
	}
	
	public PanelBusquedaClienteMinimal getPanelFiltro() {
		if(panelFiltro == null){
			panelFiltro = new PanelBusquedaClienteMinimal(){

				private static final long serialVersionUID = -1900347914549888884L;

				@Override
				protected void botonLimpiarPresionado() {
					PanelVisualizadorSecuencias.this.botonLimpiarPresionado();
				}

				@Override
				protected void clienteEncontrado(Cliente cliente) {
					PanelVisualizadorSecuencias.this.clienteEncontrado(cliente);
				}
			};
		}
		return panelFiltro;
	}

	public void agregarElemento(SecuenciaTipoProducto s) {
		getPanelTablaSecuencias().agregarElemento(s);
	}
	
	public void agregarElementos(List<SecuenciaTipoProducto> secuencias) {
		getPanelTablaSecuencias().agregarElementos(secuencias);
		
	}

	public SecuenciaTipoProducto getSecuencia(int fila) {
		return getPanelTablaSecuencias().getElemento(fila);
	}

	public int getSelectedRowSecuencia() {
		return getPanelTablaSecuencias().getTabla().getSelectedRow();
	}

	public void limpiarSecuencias() {
		getPanelTablaSecuencias().limpiar();
	}
	
	public void limpiarPasos() {
		getPanelPasos().limpiar();
	}
	
	public void limpiarFiltro(){
		getPanelFiltro().limpiar();
	}

	public void llenarFiltro(Cliente cliente) {
		getPanelFiltro().setCliente(cliente);
	}
}
